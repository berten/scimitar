package be.deschutter.scimitar.ticker.planet.config;

import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.ticker.planet.PlanetFieldSetMapper;
import be.deschutter.scimitar.ticker.planet.PlanetItemProcessor;
import be.deschutter.scimitar.ticker.planet.PlanetStaging;
import be.deschutter.scimitar.ticker.planet.PlanetStagingEao;
import be.deschutter.scimitar.ticker.planet.PlanetWriter;
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
public class PlanetConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private TickerInfoEao tickerInfoEao;

    @Autowired
    private PlanetFieldSetMapper planetFieldSetMapper;
    @Autowired
    private PlanetItemProcessor planetItemProcessor;
    @Autowired
    private PlanetWriter planetWriter;
    @Autowired
    private PlanetEao planetEao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PlanetStagingEao planetStagingEao;

    @Bean
    public Job planetListingJob() {
        return jobs.get("PlanetJob").incrementer(new RunIdIncrementer())
            .listener(new JobExecutionListener() {
                @Override
                public void beforeJob(final JobExecution jobExecution) {
                    planetStagingEao.deleteAll();
                }

                @Override
                public void afterJob(final JobExecution jobExecution) {
                    final TickerInfo tick = tickerInfoEao.findByTick(
                        jobExecution.getJobParameters().getLong("tick"));

                    jdbcTemplate.execute(
                        "INSERT into planet (id,tick,planet_name,race,ruler_name,score,size,special,value,x,xp,y,z,score_rank,value_rank,size_rank,xp_rank) SELECT id,tick,planet_name,race,ruler_name,score,size,special,value,x,xp,y,z,score_rank,value_rank,size_rank,xp_rank from ("
                            + "  SELECT" + "    *," + "    rank()"
                            + "    OVER (ORDER BY score DESC ) as score_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY value DESC )as value_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY size DESC )as size_rank,"
                            + "    rank()"
                            + "    OVER (ORDER BY xp DESC )as xp_rank"
                            + "  FROM planet_staging" + ") as planet_rank");


                    tick.setPlanets(planetEao.countByTick(tick.getTick()));
                    tick.setProcessingTimePlanets(
                        new Date().getTime() - jobExecution
                            .getStartTime().getTime());
                    tickerInfoEao.save(tick);

                    tickerInfoEao.saveAndFlush(tick);
                }
            }).flow(planetStep()).end().build();
    }

    @Bean
    public Step planetStep() {
        return stepBuilderFactory
            .get("step").<PlanetStaging, PlanetStaging>chunk(
                1) //important to be one in this case to commit after every line read
            .reader(planetReader(null)).processor(planetItemProcessor)
            .writer(planetWriter).faultTolerant().build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<PlanetStaging> planetReader(
        @Value("#{jobParameters['planetFileName']}")
            String planetFileName) {

        try {
            File planetListing = new File("planet_listing.txt");
            FileUtils.copyURLToFile(new URL(planetFileName), planetListing);
            FlatFileItemReader<PlanetStaging> reader = new FlatFileItemReader<>();
            reader.setLinesToSkip(8);
            reader.setResource(new PathResource(planetListing.toURI()));
            reader.setLineMapper(planetLineMapper());
            return reader;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public LineMapper<PlanetStaging> planetLineMapper() {
        DefaultLineMapper<PlanetStaging> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(
            new String[] { "id", "x", "y", "z", "planet_name", "ruler_name",
                "race", "size", "score", "value", "xp", "special" });

        BeanWrapperFieldSetMapper<PlanetStaging> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PlanetStaging.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(planetFieldSetMapper);

        return lineMapper;
    }
}