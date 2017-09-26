package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.scans.Scan;
import be.deschutter.scimitar.scans.ScanEao;
import be.deschutter.scimitar.scans.ScanType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

public abstract class ShowScanListener implements Listener {

    @Autowired
    private ScanEao scanEao;
    @Autowired
    private PlanetEao planetEao;
    @Autowired
    private TickerInfoEao tickerInfoEao;
    @Autowired
    private PaConfig paConfig;

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
    public String getResult(final String username, final String... parameters) {
        if (parameters.length == 3) {
            try {
                int x = Integer.parseInt(parameters[0]);
                int y = Integer.parseInt(parameters[1]);
                int z = Integer.parseInt(parameters[2]);

                final TickerInfo currentTick = tickerInfoEao
                    .findFirstByOrderByTickDesc();

                final Planet planet = planetEao
                    .findByXAndYAndZAndTick(x, y, z, currentTick.getTick());

                if(planet != null) {
                    final Scan scan = scanEao
                        .findFirstByPlanetIdAndScanTypeOrderByTickDesc(planet.getId(), getScanType());
                    if (scan != null)
                        return String.format(
                            "Latest %s scan on %d:%d:%d (%d ticks old): %s=%s",
                            getScanType().name(), x, y, z,currentTick.getTick()-scan.getTick(),paConfig.getScanurl(), scan.getId());
                    else
                        return String.format("No %s scan could be found for %d:%d:%d",
                            getScanType().name(), x, y, z);
                } else {
                    return String.format("Planet %d:%d:%d could not be found",x,y,z);
                }
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else
            return getErrorMessage();
    }

    public abstract ScanType getScanType();
}
