package be.deschutter.scimitar.ticker.configuration;

import be.deschutter.scimitar.PlanetStaging;
import be.deschutter.scimitar.ticker.LogProcessListener;
import be.deschutter.scimitar.ticker.ProtocolListener;
import be.deschutter.scimitar.ticker.SuggestedPodcastFieldSetMapper;
import be.deschutter.scimitar.ticker.SuggestedPodcastItemProcessor;
import be.deschutter.scimitar.ticker.Writer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "be.deschutter" })
@EnableScheduling
@EnableBatchProcessing
@EntityScan(basePackages = { "be.deschutter" })
public class TickerConfig {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TickerConfig.class);
    }

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // tag::jobstep[]
    @Bean
    public Job addNewPodcastJob() {
        return jobs.get("planetStagingJob").listener(protocolListener())
            .start(step()).build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory
            .get("step").<PlanetStaging, PlanetStaging>chunk(
                1) //important to be one in this case to commit after every line read
            .reader(reader()).processor(processor()).writer(writer())
            .listener(logProcessListener()).faultTolerant()
            .skipLimit(10) //default is set to 0
            .build();
    }
    @Bean
    public ItemReader<PlanetStaging> reader() {
        FlatFileItemReader<PlanetStaging> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);//first line is title definition
        reader.setResource(new ClassPathResource("planet_listing.txt"));
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public LineMapper<PlanetStaging> lineMapper() {
        DefaultLineMapper<PlanetStaging> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(
            new String[] { "id", "x",
                "y", "z", "planet_name", "ruler_name",
                "race", "size", "score", "value",
                "xp", "special" });

        BeanWrapperFieldSetMapper<PlanetStaging> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PlanetStaging.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(suggestedPodcastFieldSetMapper());

        return lineMapper;
    }

    @Bean
    public SuggestedPodcastFieldSetMapper suggestedPodcastFieldSetMapper() {
        return new SuggestedPodcastFieldSetMapper();
    }

    /**
     * configure the processor related stuff
     */
    @Bean
    public ItemProcessor<PlanetStaging, PlanetStaging> processor() {
        return new SuggestedPodcastItemProcessor();
    }

    @Bean
    public ItemWriter<PlanetStaging> writer() {
        return new Writer();
    }

    @Bean
    public ProtocolListener protocolListener() {
        return new ProtocolListener();
    }

    @Bean
    public LogProcessListener logProcessListener() {
        return new LogProcessListener();
    }

}
