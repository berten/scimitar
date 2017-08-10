package be.deschutter.scimitar.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"be.deschutter"})
@EntityScan(basePackages = {"be.deschutter"})
@EnableJpaRepositories(basePackages = {"be.deschutter"})
public class RestConfig {
    public static void main(String[] args) {
        SpringApplication.run(RestConfig.class, args);
    }
}