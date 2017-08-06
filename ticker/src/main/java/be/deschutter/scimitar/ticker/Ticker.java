package be.deschutter.scimitar.ticker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class Ticker {

    @Value("${botfiles.planet.url}")
    private String planetFile;

    @Value("${botfiles.galaxy.url}")
    private String galaxyFile;

    @Value("${botfiles.alliance.url}")
    private String allianceFile;

    @Value("${botfiles.userfeed.url}")
    private String userFeed;

    @Scheduled(cron = "0 2 * * * *")
    public void tick() {
        System.out.println(planetFile);
    }

    @PostConstruct
    public void getUpToSpeed() {
        System.out.println(planetFile);
    }
}
