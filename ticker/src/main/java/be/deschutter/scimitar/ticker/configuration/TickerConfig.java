package be.deschutter.scimitar.ticker.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"be.deschutter"})
@EnableScheduling
public class TickerConfig {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TickerConfig.class);
    }
}
