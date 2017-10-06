package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.Formatter;
import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValueListener implements Listener {
    private PlanetService planetService;

    @Autowired
    public ValueListener(final PlanetService planetService) {
        this.planetService = planetService;
    }

    @Override
    public String getCommand() {
        return "value";
    }

    @Override
    public String getPattern() {
        return "x y z";
    }

    @Override
    public String getResult(String... parameters) {
        if (parameters != null && parameters.length == 3) {
            try {
                int x = Integer.parseInt(parameters[0]);
                int y = Integer.parseInt(parameters[1]);
                int z = Integer.parseInt(parameters[2]);

                List<Planet> p = planetService
                    .findFirst15ByXAndYAndZOrderByTickDesc(x, y, z);

                String valueString = p.stream()
                    .sorted(Comparator.comparingLong(Planet::getTick)).map(
                        planet -> String
                            .format(" pt%d %s (%s)", planet.getTick(),
                                Formatter.format(planet.getValue()),
                                Formatter.format(planet.getValueGrowth())))
                    .collect(Collectors.joining("|"));

                return String.format("Value in the last %d ticks on %d:%d:%d%s",
                    p.size(), x, y, z, valueString);

            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else
            return getErrorMessage();
    }
}
