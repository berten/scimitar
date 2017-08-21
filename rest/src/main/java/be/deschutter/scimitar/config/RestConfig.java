package be.deschutter.scimitar.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "be.deschutter" },exclude = {SecurityAutoConfiguration.class })
@EntityScan(basePackages = { "be.deschutter" })
@EnableJpaRepositories(basePackages = { "be.deschutter" })
@EnableConfigurationProperties(PaConfig.class)
public class RestConfig {

    public static void main(String[] args) {
        SpringApplication.run(RestConfig.class, args);
    }

}