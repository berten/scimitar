package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.scans.ScanRequest;
import be.deschutter.scimitar.scans.ScanRequestEao;
import be.deschutter.scimitar.scans.ScanType;
import be.deschutter.scimitar.security.SecurityHelper;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
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
    private ScanRequestEao scanRequestEao;
    private TickerInfoEao tickerInfoEao;
    private JmsTemplate jmsTemplate;
    private Topic scanRequestTopic;
    private SecurityHelper securityHelper;

    @Autowired
    public ReqListener(final PlanetEao planetEao, final ScanRequestEao scanRequestEao, final TickerInfoEao tickerInfoEao,
        final JmsTemplate jmsTemplate, final Topic scanRequestTopic,
        final SecurityHelper securityHelper) {
        this.planetEao = planetEao;
        this.scanRequestEao = scanRequestEao;
        this.tickerInfoEao = tickerInfoEao;
        this.jmsTemplate = jmsTemplate;
        this.scanRequestTopic = scanRequestTopic;
        this.securityHelper = securityHelper;
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
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {
        final TickerInfo currentTick = tickerInfoEao
            .findFirstByOrderByTickDesc();

        if (parameters.length == 1) {
            if (parameters[0].toUpperCase().equals("LIST")) {
                return listScanRequestsWithLessDistsThen(currentTick, 300);
            } else if (parameters[0].toUpperCase().equals("LINKS")) {
                return listScanLinksWithLessDistsThen(currentTick, 300);
            } else {
                return getErrorMessage();
            }
        } else if (parameters.length == 2) {
            if (parameters[0].toUpperCase().equals("CANCEL")) {
                try {
                    int id = Integer.parseInt(parameters[1]);
                    scanRequestEao.delete(id);
                    return String
                        .format("Scanrequest %d successfully cancelled", id);
                } catch (NumberFormatException e) {
                    return getErrorMessage();
                }

            } else if (parameters[0].toUpperCase().equals("LIST")) {
                try {
                    int amps = Integer.parseInt(parameters[1]);
                    return listScanRequestsWithLessDistsThen(currentTick, amps);
                } catch (NumberFormatException e) {
                    return getErrorMessage();
                }

            } else if (parameters[0].toUpperCase().equals("LINKS")) {
                try {
                    int amps = Integer.parseInt(parameters[1]);
                    return listScanLinksWithLessDistsThen(currentTick, amps);
                } catch (NumberFormatException e) {
                    return getErrorMessage();
                }

            } else {
                return getErrorMessage();
            }
        } else if (parameters.length == 5) {
            try {
                int x = Integer.parseInt(parameters[0]);
                int y = Integer.parseInt(parameters[1]);
                int z = Integer.parseInt(parameters[2]);

                int amps = Integer.parseInt(parameters[4]);
                if (parameters[3].toUpperCase().equals("BLOCKS")) {
                    final Planet planet = planetEao
                        .findByXAndYAndZAndTick(x, y, z, currentTick.getTick());
                    planet.setDists(amps);
                    planetEao.save(planet);
                    return String
                        .format("Updated intelligence on %d:%d:%d to %d dists",
                            x, y, z, amps);
                }
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
                        request
                            .setRequestedBy(securityHelper.getLoggedInUser());
                        request.setScanType(
                            ScanType.valueOf(String.valueOf((char) value)));
                        scanRequestEao.save(request);
                        jmsTemplate.convertAndSend(scanRequestTopic, String
                            .format(
                                "New Scan Request: %s on %d:%d:%d https://game.planetarion.com/waves.pl?id=%d&x=%d&y=%d&z=%d",
                                request.getScanType().name(), x, y, z,
                                request.getScanType().ordinal(), x, y, z));
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
                .findByIdAndTick(scanRequest.getPlanetId(),
                    currentTick.getTick());
            if (planet.getDists() <= amps)
                reqs.computeIfAbsent(planet, k -> new HashSet<>())
                    .add(scanRequest);
        });

        return reqs.entrySet().stream()
            .sorted(Comparator.comparing(o -> o.getKey().getCoordCalculated()))
            .map(planetSetEntry -> String
                .format("[%d:%d:%d (%d) %s]", planetSetEntry.getKey().getX(),
                    planetSetEntry.getKey().getY(),
                    planetSetEntry.getKey().getZ(),
                    planetSetEntry.getKey().getDists(),
                    planetSetEntry.getValue().stream().sorted(Comparator
                        .comparingInt(o2 -> o2.getScanType().ordinal())).map(
                        scanRequest -> scanRequest.getId() + ":" + scanRequest
                            .getScanType().name())
                        .collect(Collectors.joining("|"))))
            .collect(Collectors.joining(" "));
    }

    private String listScanLinksWithLessDistsThen(final TickerInfo currentTick,
        int amps) {
        final List<ScanRequest> scanRequests = scanRequestEao
            .findByScanIdIsNull();

        return scanRequests.stream().map(scanRequest -> {
            final Planet planet = planetEao
                .findByIdAndTick(scanRequest.getPlanetId(),
                    currentTick.getTick());
            return new ScanRequestBuffed(scanRequest, planet);
        }).filter(scanRequestBuffed -> scanRequestBuffed.getPlanet().getDists()
            < amps).map(scanRequest -> String.format(
            "[%d (%d) : https://game.planetarion.com/waves.pl?id=%d&x=%d&y=%d&z=%d]",
            scanRequest.getScanRequest().getId(),
            scanRequest.getPlanet().getDists(),
            scanRequest.getScanRequest().getScanType().ordinal(),
            scanRequest.getPlanet().getX(), scanRequest.getPlanet().getY(),
            scanRequest.getPlanet().getZ())).collect(Collectors.joining(" "));
    }

    private class ScanRequestBuffed {
        private ScanRequest scanRequest;
        private Planet planet;

        ScanRequestBuffed(final ScanRequest scanRequest, final Planet planet) {

            this.scanRequest = scanRequest;
            this.planet = planet;
        }

        ScanRequest getScanRequest() {
            return scanRequest;
        }

        Planet getPlanet() {
            return planet;
        }
    }
}
