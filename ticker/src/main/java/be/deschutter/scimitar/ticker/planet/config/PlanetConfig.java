package be.deschutter.scimitar.ticker.planet.config;

import be.deschutter.scimitar.PlanetStaging;
import be.deschutter.scimitar.PlanetStagingEao;
import be.deschutter.scimitar.ticker.LogProcessListener;
import be.deschutter.scimitar.ticker.ProtocolListener;
import be.deschutter.scimitar.ticker.planet.PlanetStagingFieldSetMapper;
import be.deschutter.scimitar.ticker.planet.PlanetStagingItemProcessor;
import be.deschutter.scimitar.ticker.planet.PlanetStagingWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Configuration
public class PlanetConfig {
    @Value("${botfiles.planet.url}")
    private String planetFile;

    @Value("${botfiles.galaxy.url}")
    private String galaxyFile;

    @Value("${botfiles.alliance.url}")
    private String allianceFile;

    @Value("${botfiles.userfeed.url}")
    private String userFeed;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private PlanetStagingEao planetStagingEao;

    @Bean
    public Job planetListingJob() {
        return jobs.get("planetStagingJob").incrementer(new RunIdIncrementer())
                .flow(planetStep()).end().build();
    }


    @Bean
    public Step planetStep() {
        return stepBuilderFactory
                .get("step").<PlanetStaging, PlanetStaging>chunk(
                        1) //important to be one in this case to commit after every line read
                .reader(planetReader()).processor(processor()).writer(writer())
                .faultTolerant()
                .build();
    }

    @Bean
    public ItemReader<PlanetStaging> planetReader() {

        planetStagingEao.deleteAll();
        try {
            File planetListing = new File("planet_listing.txt");
            FileUtils.copyURLToFile(new URL(planetFile), planetListing);
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
                new String[]{"id", "x",
                        "y", "z", "planet_name", "ruler_name",
                        "race", "size", "score", "value",
                        "xp", "special"});

        BeanWrapperFieldSetMapper<PlanetStaging> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PlanetStaging.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(planetStagingFieldSetMapper());

        return lineMapper;
    }

    @Bean
    public PlanetStagingFieldSetMapper planetStagingFieldSetMapper() {
        return new PlanetStagingFieldSetMapper();
    }

    /**
     * configure the processor related stuff
     */
    @Bean
    public ItemProcessor<PlanetStaging, PlanetStaging> processor() {
        return new PlanetStagingItemProcessor();
    }

    @Bean
    public ItemWriter<PlanetStaging> writer() {
        return new PlanetStagingWriter();
    }


}
