package be.deschutter.scimitar.ticker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;

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
            URL url = new URL(planetFile);


            File planetListing = new File("planet_listing.txt");
            FileUtils.copyURLToFile(url, planetListing);

            jdbcTemplate.execute("COPY planetstaging(id,x,y,z,planetname,rulername,size,score,value,xp,special) from 'tail -n +8 "+ planetListing.getAbsolutePath()+"' DELIMITER '\t' CSV");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
