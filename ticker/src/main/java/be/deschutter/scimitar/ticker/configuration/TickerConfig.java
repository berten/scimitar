package be.deschutter.scimitar.ticker.configuration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"be.deschutter"})
@EnableScheduling
@EnableBatchProcessing
@EntityScan(basePackages = {"be.deschutter"})
@EnableJpaRepositories(basePackages = {"be.deschutter"})
public class TickerConfig {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(TickerConfig.class);
    }
}