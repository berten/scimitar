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
    private Job galaxyListingJob;

    @Autowired
    private Job allianceListingJob;

    @Autowired
    private TickerInfoEao tickerInfoEao;
    @Value("${ticksInFirstDay}")
    private int ticksInFirstDay;
    @Value("${ticksInDay}")
    private int ticksInDay;

    @Value("${botfiles.planet.url}")
    private String planetFile;

    @Value("${botfiles.galaxy.url}")
    private String galaxyFile;

    @Value("${botfiles.alliance.url}")
    private String allianceFile;

    @Value("${botfiles.userfeed.url}")
    private String userFeed;

    @Value("${botfiles.missedticks.planet.url}")
    private String planetFileMissedTicks;

    @Value("${botfiles.missedticks.galaxy.url}")
    private String galaxyFileMissedTicks;

    @Value("${botfiles.missedticks.alliance.url}")
    private String allianceFileMissedTicks;


    @Scheduled(cron = "0 2 * * * *")
    public void tick() {

        try {
            File planetListing = new File("planet_listing.txt");
            FileUtils.copyURLToFile(new URL(planetFile), planetListing);


            String tickLine = Files
                    .readAllLines(Paths.get(planetListing.toURI()), Charset.forName("ISO-8859-1")).get(3);

            String tick = tickLine.replace("Tick: ", "");
            final long currentTick = Long.parseLong(tick);

            TickerInfo lastTick = tickerInfoEao.findFirstByOrderByTickDesc();
            final long previousTick = lastTick != null ? lastTick.getTick() : 0;
            if (previousTick + 1 == currentTick) {

                tickerInfoEao.saveAndFlush(new TickerInfo(currentTick));
                Long tickToCompareWith = tickToCompareWith(currentTick);
                JobParameters param = new JobParametersBuilder()
                        .addString("planetFileName", planetFile)
                        .addLong("tick", currentTick).addLong("compareTick", tickToCompareWith).toJobParameters();
                jobLauncher.run(planetListingJob, param);

                param = new JobParametersBuilder()
                        .addString("galaxyFileName", galaxyFile)
                        .addLong("tick", currentTick).addLong("compareTick", tickToCompareWith).toJobParameters();
                jobLauncher.run(galaxyListingJob, param);

                param = new JobParametersBuilder()
                        .addString("allianceFileName", allianceFile)
                        .addLong("tick", currentTick).addLong("compareTick", tickToCompareWith).toJobParameters();
                jobLauncher.run(allianceListingJob, param);


            } else {

                for (long i = previousTick + 1; i <= currentTick; i++) {
                    tickerInfoEao.saveAndFlush(new TickerInfo(i));
                    Long tickToCompareWith = tickToCompareWith(i);
                    JobParameters param = new JobParametersBuilder()
                            .addString("planetFileName", planetFileMissedTicks.replace("%tick", "" + i))
                            .addLong("tick", i).addLong("compareTick", tickToCompareWith).toJobParameters();
                    jobLauncher.run(planetListingJob, param);

                    param = new JobParametersBuilder()
                            .addString("galaxyFileName", galaxyFileMissedTicks.replace("%tick", "" + i))
                            .addLong("tick", i).addLong("compareTick", tickToCompareWith).toJobParameters();
                    jobLauncher.run(galaxyListingJob, param);

                    param = new JobParametersBuilder()
                            .addString("allianceFileName", allianceFileMissedTicks.replace("%tick", "" + i))
                            .addLong("tick", i).addLong("compareTick", tickToCompareWith).toJobParameters();
                    jobLauncher.run(allianceListingJob, param);
                }

            }
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException | JobInstanceAlreadyCompleteException | IOException e) {
            e.printStackTrace();
        }
    }
// This is not perfect etc, but it's tested. it works!!
    public Long tickToCompareWith(long currentTick) {
        if (currentTick <= ticksInFirstDay) return 1L;

        long l = (((currentTick - ticksInFirstDay) / ticksInDay) * ticksInDay) + ticksInFirstDay;
        if (l == currentTick) return currentTick - ticksInDay;
        return l;

    }
}