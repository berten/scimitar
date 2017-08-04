package be.deschutter.planetarion.katana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"be.deschutter"})
public class MvcConfig {

    public static void main(String[] args) {
        SpringApplication.run(MvcConfig.class, args);
    }
}