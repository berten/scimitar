package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.planet.ScanRequest;
import be.deschutter.scimitar.planet.ScanRequestEao;
import be.deschutter.scimitar.planet.ScanType;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReqListener implements Listener {
    private PlanetEao planetEao;
    private ScimitarUserEao scimitarUserEao;
    private ScanRequestEao scanRequestEao;
    private TickerInfoEao tickerInfoEao;

    @Autowired
    public ReqListener(final PlanetEao planetEao,
        final ScimitarUserEao scimitarUserEao,
        final ScanRequestEao scanRequestEao,
        final TickerInfoEao tickerInfoEao) {
        this.planetEao = planetEao;
        this.scimitarUserEao = scimitarUserEao;
        this.scanRequestEao = scanRequestEao;
        this.tickerInfoEao = tickerInfoEao;
    }

    @Override
    public String getCommand() {
        return "req";
    }

    @Override
    public String getPattern() {
        return "x y z " + Arrays.stream(ScanType.values()).map(Enum::name)
            .collect(Collectors.joining("|"))
            + " [dists] | x y z blocks <amps> | cancel <id> | list [amps] | links [amps]";
    }

    @Override
    public String getResult(final String username, final String... parameters) {
        final TickerInfo currentTick = tickerInfoEao
            .findFirstByOrderByTickDesc();

        if (parameters.length == 1) {
            if (parameters[0].toUpperCase().equals("LIST")) {
                return listScanRequestsWithLessDistsThen(currentTick, 300);

            } else if (parameters[0].toUpperCase().equals("LINKS")) {

            } else {
                return getErrorMessage();
            }
        } else if (parameters.length == 2) {
            if (parameters[0].toUpperCase().equals("CANCEL")) {

            } else if (parameters[0].toUpperCase().equals("LIST")) {
                try {
                    int amps = Integer.parseInt(parameters[1]);
                    return listScanRequestsWithLessDistsThen(currentTick, amps);
                } catch (NumberFormatException e) {
                    return getErrorMessage();
                }

            } else if (parameters[0].toUpperCase().equals("LINKS")) {

            } else {
                return getErrorMessage();
            }
        } else if (parameters.length == 5) {
            try {
                int x = Integer.parseInt(parameters[0]);
                int y = Integer.parseInt(parameters[1]);
                int z = Integer.parseInt(parameters[2]);

                int amps = Integer.parseInt(parameters[4]);
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else if (parameters.length == 4) {
            try {
                int x = Integer.parseInt(parameters[0]);
                int y = Integer.parseInt(parameters[1]);
                int z = Integer.parseInt(parameters[2]);

                Planet p = planetEao
                    .findByXAndYAndZAndTick(x, y, z, currentTick.getTick());

                if (null != p) {

                    parameters[3].toUpperCase().chars().forEach(value -> {
                        ScanRequest request = new ScanRequest();
                        request.setPlanetId(p.getId());
                        request.setTick(p.getTick());
                        request.setRequestedBy(
                            scimitarUserEao.findByUsernameIgnoreCase(username));
                        request.setScanType(
                            ScanType.valueOf(String.valueOf((char) value)));
                        scanRequestEao.save(request);
                    });

                    return String
                        .format("Requested %s scan(s) for coords: %s:%s:%s",
                            parameters[3], parameters[0], parameters[1],
                            parameters[2]);
                } else {
                    return String
                        .format("Planet %s:%s:%s does not exist", parameters[0],
                            parameters[1], parameters[2]);
                }

            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else {
            return getErrorMessage();
        }
        return getErrorMessage();
    }

    private String listScanRequestsWithLessDistsThen(
        final TickerInfo currentTick, int amps) {
        final List<ScanRequest> scanRequests = scanRequestEao
            .findByScanIdIsNull();

        Map<Planet, Set<ScanRequest>> reqs = new HashMap<>();
        scanRequests.forEach(scanRequest -> {
            final Planet planet = planetEao
                .findByPlanetIdAndTick(scanRequest.getPlanetId(),
                    currentTick.getTick());
            if (planet.getDists() <= amps)
                reqs.computeIfAbsent(planet, k -> new HashSet<>())
                    .add(scanRequest);
        });

        return reqs.entrySet().stream().sorted(
            Comparator.comparing(o -> o.getKey().getCoordCalculated())).map(planetSetEntry -> String
            .format("[%d:%d:%d (%d) %s]", planetSetEntry.getKey().getX(),
                planetSetEntry.getKey().getY(), planetSetEntry.getKey().getZ(),
                planetSetEntry.getKey().getDists(),
                planetSetEntry.getValue().stream().map(
                    scanRequest -> scanRequest.getId() +":" +scanRequest.getScanType().name() )
                    .collect(Collectors.joining("|"))))
            .collect(Collectors.joining(" "));
    }

}
