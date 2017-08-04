package be.deschutter.scimitar.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot","be.deschutter"})
public class ScimitarSlack {

    public static void main(String[] args) {
        SpringApplication.run(ScimitarSlack.class, args);
    }
}
