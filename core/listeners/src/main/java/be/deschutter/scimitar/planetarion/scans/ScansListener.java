package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.scans.Scan;
import be.deschutter.scimitar.scans.ScanService;
import be.deschutter.scimitar.ticker.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScansListener implements Listener {
    private final TickerService tickerService;
    private final PaConfig paConfig;
    private final ScanService scanService;

    @Autowired
    public ScansListener(final TickerService tickerService,
        final PaConfig paConfig, final ScanService scanService) {
        this.tickerService = tickerService;
        this.paConfig = paConfig;
        this.scanService = scanService;
    }

    @Override
    public String getCommand() {
        return "scans";
    }

    @Override
    public String getPattern() {
        return "x y z";
    }

    @Override
    public String getResult(final String... parameters) {
        if (parameters.length == 3) {

            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);

            final TickerInfo currentTick = tickerService.getCurrentTick();

            List<Scan> scans = scanService.findLast5Scans(x, y, z);

            return String
                .format("Latest %d scans on %d:%d:%d %s", scans.size(), x, y, z,
                    scans.stream().map(scan -> String
                        .format("%s (%d ticks old): %s=%s", scan.getScanType(),
                            currentTick.getTick() - scan.getTick(),
                            paConfig.getScanurl(), scan.getId()))
                        .collect(Collectors.joining(" | ")));

        } else
            return getErrorMessage();
    }
}
