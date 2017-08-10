package be.deschutter.scimitar.ticker.alliance.config;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.alliance.AllianceEao;
import be.deschutter.scimitar.ticker.alliance.*;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Configuration
public class AllianceConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TickerInfoEao tickerInfoEao;

    @Autowired()
    private AllianceFieldSetMapper allianceFieldSetMapper;
    @Autowired
    private AllianceItemProcessor allianceItemProcessor;
    @Autowired
    private AllianceWriter allianceWriter;
    @Autowired
    private AllianceEao allianceEao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AllianceStagingEao allianceStagingEao;

    @Bean
    public Job allianceListingJob() {
        return jobs.get("AllianceJob").incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(final JobExecution jobExecution) {
                        allianceStagingEao.deleteAll();
                    }

                    @Override
                    public void afterJob(final JobExecution jobExecution) {
                        final TickerInfo tick = tickerInfoEao.findByTick(
                                jobExecution.getJobParameters().getLong("tick"));

                        jdbcTemplate.execute(
                                "INSERT INTO alliance (alliance_name, tick,counted_score,members,points,counted_score_rank,size,total_score,total_value,score_rank,value_rank,size_rank,points_rank,members_rank) SELECT alliance_name, tick,counted_score,members,points,counted_score_rank,size,total_score,total_value,score_rank,value_rank,size_rank,points_rank,members_rank from ("
                                        + "  SELECT"
                                        + "    alliance_name, tick,counted_score,members,points,rank as counted_score_rank,size,total_score,total_value,"
                                        + "    rank()"
                                        + "    OVER (ORDER BY total_score DESC ) as score_rank,"
                                        + "    rank()"
                                        + "    OVER (ORDER BY total_value DESC ) as value_rank,"
                                        + "    rank()"
                                        + "    OVER (ORDER BY size DESC ) as size_rank,"
                                        + "    rank()"
                                        + "    OVER (ORDER BY points DESC ) as points_rank,"
                                        + "    rank()"
                                        + "    OVER (ORDER BY members DESC ) as members_rank"
                                        + "  FROM alliance_staging" + ") as alliance_rank");


                        tick.setAlliances(allianceEao.countByTick(tick.getTick()));


                        Long tickToCompareWith = jobExecution.getJobParameters().getLong("compareTick");
                        jdbcTemplate.execute(
                                "update alliance set day_score_growth=alliance.total_score -(SELECT p2.total_score from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + tickToCompareWith + "), " +
                                        "day_size_growth=alliance.size -(SELECT p2.size from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + tickToCompareWith + ")," +
                                        "day_value_growth=alliance.total_value -(SELECT p2.total_value from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + tickToCompareWith + ")," +
                                        "day_points_growth=alliance.points -(SELECT p2.points from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + tickToCompareWith + ")," +
                                        "day_members_growth=alliance.members -(SELECT p2.members from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + tickToCompareWith + ")," +
                                        "day_counted_score_growth=alliance.counted_score -(SELECT p2.counted_score from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + tickToCompareWith + ") where tick=" + tick.getTick());

                        jdbcTemplate.execute(
                                "update alliance set score_growth=alliance.total_score -(SELECT p2.total_score from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + (tick.getTick() - 1) + "), " +
                                        "size_growth=alliance.size -(SELECT p2.size from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + (tick.getTick() - 1) + ")," +
                                        "value_growth=alliance.total_value -(SELECT p2.total_value from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + (tick.getTick() - 1) + ")," +
                                        "points_growth=alliance.points -(SELECT p2.points from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + (tick.getTick() - 1) + ")," +
                                        "members_growth=alliance.members -(SELECT p2.members from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + (tick.getTick() - 1) + ")," +
                                        "counted_score_growth=alliance.counted_score -(SELECT p2.counted_score from alliance p2 where p2.alliance_name=alliance.alliance_name and tick=" + (tick.getTick() - 1) + ") where tick=" + tick.getTick());


                        tick.setProcessingTimeAlliances(
                                new Date().getTime() - jobExecution.getStartTime()
                                        .getTime());
                        tickerInfoEao.saveAndFlush(tick);
                    }
                }).flow(allianceStep()).end().build();
    }

    @Bean
    public Step allianceStep() {
        return stepBuilderFactory
                .get("step").<AllianceStaging, AllianceStaging>chunk(
                        1) //important to be one in this case to commit after every line read
                .reader(allianceReader(null)).processor(allianceItemProcessor)
                .writer(allianceWriter).faultTolerant().build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<AllianceStaging> allianceReader(
            @Value("#{jobParameters['allianceFileName']}")
                    String allianceFileName) {

        try {
            File allianceListing = new File("alliance_listing.txt");
            FileUtils.copyURLToFile(new URL(allianceFileName), allianceListing);
            FlatFileItemReader<AllianceStaging> reader = new FlatFileItemReader<>();
            reader.setLinesToSkip(8);
            reader.setResource(new PathResource(allianceListing.toURI()));
            reader.setLineMapper(allianceLineMapper());
            return reader;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public LineMapper<AllianceStaging> allianceLineMapper() {

        DefaultLineMapper<AllianceStaging> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(
                new String[]{"rank", "alliance_name", "size", "members",
                        "counted_score", "points", "total_score", "total_value"});

        BeanWrapperFieldSetMapper<AllianceStaging> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(AllianceStaging.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(allianceFieldSetMapper);

        return lineMapper;
    }
}