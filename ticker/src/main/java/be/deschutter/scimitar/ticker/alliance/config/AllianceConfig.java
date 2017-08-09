package be.deschutter.scimitar.ticker.alliance.config;

import be.deschutter.scimitar.alliance.AllianceEao;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.ticker.alliance.AllianceFieldSetMapper;
import be.deschutter.scimitar.ticker.alliance.AllianceItemProcessor;
import be.deschutter.scimitar.ticker.alliance.AllianceStaging;
import be.deschutter.scimitar.ticker.alliance.AllianceStagingEao;
import be.deschutter.scimitar.ticker.alliance.AllianceWriter;
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
                        "INSERT INTO alliance (alliance_name, tick,counted_score,members,points,counted_score_rank,size,total_score,total_value,score_rank,value_rank,size_rank,points_rank) SELECT alliance_name, tick,counted_score,members,points,counted_score_rank,size,total_score,total_value,score_rank,value_rank,size_rank,points_rank from ("
                            + "  SELECT"
                            + "    alliance_name, tick,counted_score,members,points,rank as counted_score_rank,size,total_score,total_value,"
                            + "    rank()"
                            + "    OVER (ORDER BY total_score DESC ) as score_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY total_value DESC ) as value_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY size DESC ) as size_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY points DESC ) as points_rank"
                            + "  FROM alliance_staging" + ") as alliance_rank");

                    tick.setAlliances(allianceEao.countByTick(tick.getTick()));
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
            new String[] { "rank", "alliance_name", "size", "members",
                "counted_score", "points", "total_score", "total_value" });

        BeanWrapperFieldSetMapper<AllianceStaging> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(AllianceStaging.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(allianceFieldSetMapper);

        return lineMapper;
    }
}