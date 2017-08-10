package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValueListener implements Listener {
    @Autowired
    private PlanetEao planetEao;

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
                List<Planet> p = planetEao
                        .findFirst15ByXAndYAndZOrderByTickDesc(x, y, z);
                if (p == null || p.isEmpty())
                    return "Planet " + x + ":" + y + ":" + z
                            + " does not exist";
                String valueString = p.stream().sorted((o1, o2) -> Long.compare(o1.getTick(), o2.getTick())).map(planet -> String.format(" pt%d %s (%s)", planet.getTick(), Formatter.format(planet.getValue()), Formatter.format(planet.getValueGrowth()))).collect(Collectors.joining("|"));

                return String.format("Value in the last %d ticks on %d:%d:%d%s", p.size(), x, y, z, valueString);
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else
            return getErrorMessage();
    }
}
