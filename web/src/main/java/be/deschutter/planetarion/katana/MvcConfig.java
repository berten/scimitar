package be.deschutter.planetarion.katana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"be.deschutter"})
@EntityScan(basePackages = {"be.deschutter"})
@EnableJpaRepositories(basePackages = {"be.deschutter"})
public class MvcConfig {
    public static void main(String[] args) {
        SpringApplication.run(MvcConfig.class, args);
    }
}