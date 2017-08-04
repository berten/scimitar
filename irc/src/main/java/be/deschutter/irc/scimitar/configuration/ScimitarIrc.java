package be.deschutter.irc.scimitar.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "be.deschutter")
public class ScimitarIrc {

    public static void main(String[] args) {
        SpringApplication.run(ScimitarIrc.class, args);
    }
}
