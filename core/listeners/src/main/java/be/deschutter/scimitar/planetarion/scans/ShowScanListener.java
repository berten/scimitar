package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.scans.Scan;
import be.deschutter.scimitar.scans.ScanService;
import be.deschutter.scimitar.scans.ScanType;
import be.deschutter.scimitar.ticker.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

public abstract class ShowScanListener implements Listener {

    @Autowired
    private TickerService tickerService;
    @Autowired
    private PaConfig paConfig;
    @Autowired
    private ScanService scanService;

    @Override
    public String getPattern() {
        return "x y z";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {
        if (parameters.length == 3) {

            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);

            final Scan scan = scanService
                .findLastScanFor(x, y, z, getScanType());

            return String
                .format("Latest %s scan on %d:%d:%d (%d ticks old): %s=%s",
                    getScanType().name(), x, y, z,
                    tickerService.getCurrentTick().getTick() - scan.getTick(),
                    paConfig.getScanurl(), scan.getId());

        } else
            return getErrorMessage();
    }

    public abstract ScanType getScanType();
}
