package be.deschutter.scimitar.ticker;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class Ticker {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job planetListingJob;

    @Autowired
    private TickerInfoEao tickerInfoEao;

    @Value("${botfiles.planet.url}")
    private String planetFile;

    @Scheduled(cron = "0 33 * * * *")
    public void tick() {

        try {
            File planetListing = new File("planet_listing.txt");
            FileUtils.copyURLToFile(new URL(planetFile), planetListing);


            String tickLine = Files
                    .readAllLines(Paths.get(planetListing.toURI()), Charset.forName("ISO-8859-1")).get(3);

            String tick = tickLine.replace("Tick: ", "");
            final long currentTick = Long.parseLong(tick);

            TickerInfo lastTick = tickerInfoEao.findFirstByOrOrderByTickDesc();
            if (lastTick.getTick() + 1 == currentTick) {

                tickerInfoEao.saveAndFlush(new TickerInfo(currentTick));
                JobParameters param = new JobParametersBuilder()
                        //.addString("JobID", String.valueOf(System.currentTimeMillis()))
                        .addLong("tick", currentTick).toJobParameters();
                jobLauncher.run(planetListingJob, param);
            } else {

            }
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException | JobInstanceAlreadyCompleteException | IOException e) {
            e.printStackTrace();
        }
    }

}
