package be.deschutter.scimitar.ticker.galaxy.config;

import be.deschutter.scimitar.galaxy.GalaxyEao;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.ticker.galaxy.GalaxyFieldSetMapper;
import be.deschutter.scimitar.ticker.galaxy.GalaxyItemProcessor;
import be.deschutter.scimitar.ticker.galaxy.GalaxyStaging;
import be.deschutter.scimitar.ticker.galaxy.GalaxyStagingEao;
import be.deschutter.scimitar.ticker.galaxy.GalaxyWriter;
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
public class GalaxyConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TickerInfoEao tickerInfoEao;

    @Autowired()
    private GalaxyFieldSetMapper galaxyFieldSetMapper;
    @Autowired
    private GalaxyItemProcessor galaxyItemProcessor;
    @Autowired
    private GalaxyWriter galaxyWriter;
    @Autowired
    private GalaxyEao galaxyEao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GalaxyStagingEao galaxyStagingEao;

    @Bean
    public Job galaxyListingJob() {
        return jobs.get("GalaxyJob").incrementer(new RunIdIncrementer())
            .listener(new JobExecutionListener() {
                @Override
                public void beforeJob(final JobExecution jobExecution) {
                    galaxyStagingEao.deleteAll();
                }

                @Override
                public void afterJob(final JobExecution jobExecution) {
                    final TickerInfo tick = tickerInfoEao.findByTick(
                        jobExecution.getJobParameters().getLong("tick"));

                    Long tickToCompareWith = jobExecution.getJobParameters().getLong("compareTick");

                    jdbcTemplate.execute(
                        "INSERT into galaxy (tick,galaxy_name,score,size,value,x,xp,y,score_rank,value_rank,size_rank,xp_rank) SELECT tick,galaxy_name,score,size,value,x,xp,y,score_rank,value_rank,size_rank,xp_rank from ("
                            + "  SELECT" + "    *," + "    rank()"
                            + "    OVER (ORDER BY score DESC ) as score_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY value DESC )as value_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY size DESC )as size_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY xp DESC )as xp_rank"
                            + "  FROM galaxy_staging" + ") as galaxy_rank");

                    jdbcTemplate.execute(
                        "update galaxy set planets=(select count(*) from planet where planet.x=galaxy.x and planet.y=galaxy.y and planet.tick=galaxy.tick) where tick="
                            + tick.getTick());

                    jdbcTemplate.execute(
                            "update galaxy set day_score_growth=galaxy.score -(SELECT g2.score from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + tickToCompareWith + "), " +
                                    "day_size_growth=galaxy.size -(SELECT g2.size from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + tickToCompareWith + ")," +
                                    "day_value_growth=galaxy.value -(SELECT g2.value from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + tickToCompareWith + ")," +
                                    "day_xp_growth=galaxy.xp -(SELECT g2.xp from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + tickToCompareWith + ")," +
                                    "day_planets_growth=galaxy.planets -(SELECT g2.planets from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + tickToCompareWith + ") where tick=" + tick.getTick());

                    jdbcTemplate.execute(
                            "update galaxy set score_growth=galaxy.score -(SELECT g2.score from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + (tick.getTick() -1) + "), " +
                                    "size_growth=galaxy.size -(SELECT g2.size from galaxy g2 where g2.x=galaxy.x and  g2.y=galaxy.y and tick=" + (tick.getTick() -1) + ")," +
                                    "value_growth=galaxy.value -(SELECT g2.value from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + (tick.getTick() -1) + ")," +
                                    "xp_growth=galaxy.xp -(SELECT g2.xp from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + (tick.getTick() -1) + ")," +
                                    "planets_growth=galaxy.planets -(SELECT g2.planets from galaxy g2 where g2.x=galaxy.x and g2.y=galaxy.y and tick=" + (tick.getTick() -1) + ") where tick=" + tick.getTick());


                    tick.setGalaxies(galaxyEao.countByTick(tick.getTick()));
                    tick.setProcessingTimeGalaxies(
                        new Date().getTime() - jobExecution
                            .getStartTime().getTime());
                    tickerInfoEao.saveAndFlush(tick);
                }
            }).flow(galaxyStep()).end().build();
    }

    @Bean
    public Step galaxyStep() {
        return stepBuilderFactory
            .get("step").<GalaxyStaging, GalaxyStaging>chunk(
                1) //important to be one in this case to commit after every line read
            .reader(galaxyReader(null)).processor(galaxyItemProcessor)
            .writer(galaxyWriter).faultTolerant().build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<GalaxyStaging> galaxyReader(
        @Value("#{jobParameters['galaxyFileName']}")
            String galaxyFileName) {

        try {
            File galaxyListing = new File("galaxy_listing.txt");
            FileUtils.copyURLToFile(new URL(galaxyFileName), galaxyListing);
            FlatFileItemReader<GalaxyStaging> reader = new FlatFileItemReader<>();
            reader.setLinesToSkip(8);
            reader.setResource(new PathResource(galaxyListing.toURI()));
            reader.setLineMapper(galaxyLineMapper());
            return reader;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public LineMapper<GalaxyStaging> galaxyLineMapper() {

        DefaultLineMapper<GalaxyStaging> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(
            new String[] { "x", "y", "galaxy_name", "size", "score", "value",
                "xp" });

        BeanWrapperFieldSetMapper<GalaxyStaging> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(GalaxyStaging.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(galaxyFieldSetMapper);

        return lineMapper;
    }
}