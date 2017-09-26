package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.scans.Scan;
import be.deschutter.scimitar.scans.ScanEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScansListener implements Listener {
    @Autowired
    private ScanEao scanEao;
    @Autowired
    private PlanetEao planetEao;
    @Autowired
    private TickerInfoEao tickerInfoEao;

    @Autowired
    private PaConfig paConfig;

    @Override
    public String getCommand() {
        return "scans";
    }

    @Override
    public String getPattern() {
        return "x y z";
    }

    @Override
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

                if (planet != null) {
                    final List<Scan> scans = scanEao
                        .findFirst5ByPlanetIdOrderByTickDesc(planet.getId());
                    if (scans != null && !scans.isEmpty())
                        return String.format("Latest %d scans on %d:%d:%d %s",
                            scans.size(), x, y, z, scans.stream().map(
                                scan -> String.format(
                                    "%s (%d ticks old): %s=%s",scan.getScanType(),
                                    currentTick.getTick() - scan.getTick(),paConfig.getScanurl(),
                                    scan.getId()))
                                .collect(Collectors.joining(" | ")));
                    else
                        return String
                            .format("No scans could be found for %d:%d:%d", x,
                                y, z);
                } else {
                    return String
                        .format("Planet %d:%d:%d could not be found", x, y, z);
                }
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else
            return getErrorMessage();
    }
}
