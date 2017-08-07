package be.deschutter.scimitar.ticker;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;

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


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 2 * * * *")
    public void tick() {
        System.out.println(planetFile);
    }

    @PostConstruct
    public void getUpToSpeed() {


        try {

            File planetListing = new File("planet_listing.txt");
            FileUtils.copyURLToFile(new URL(planetFile), planetListing);

            File galaxyListing = new File("galaxy_listing.txt");
            FileUtils.copyURLToFile(new URL(galaxyFile), galaxyListing);

            File allianceListing = new File("alliance_listing.txt");
            FileUtils.copyURLToFile(new URL(allianceFile), allianceListing);

            File userFeedFile = new File("user_feed.txt");
            FileUtils.copyURLToFile(new URL(userFeed), userFeedFile);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
