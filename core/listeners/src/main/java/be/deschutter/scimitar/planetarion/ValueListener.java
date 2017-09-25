package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.Formatter;
import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValueListener implements Listener {
    private final PlanetEao planetEao;
private final TickerInfoEao tickerInfoEao;
    @Autowired
    public ValueListener(final PlanetEao planetEao,
        final TickerInfoEao tickerInfoEao) {
        this.planetEao = planetEao;
        this.tickerInfoEao = tickerInfoEao;
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
    public String getResult(String username, String... parameters) {
        if (parameters != null && parameters.length == 3) {
            try {
                int x = Integer.parseInt(parameters[0]);
                int y = Integer.parseInt(parameters[1]);
                int z = Integer.parseInt(parameters[2]);
                final TickerInfo currentTick = tickerInfoEao
                    .findFirstByOrderByTickDesc();
                if(planetEao.findByXAndYAndZAndTick(x,y,z,currentTick.getTick()) != null) {

                    List<Planet> p = planetEao
                        .findFirst15ByXAndYAndZOrderByTickDesc(x, y, z);
                    if (p == null || p.isEmpty())
                        return "Planet " + x + ":" + y + ":" + z
                            + " does not exist";
                    String valueString = p.stream().sorted(Comparator.comparingLong(Planet::getTick)).map(
                        planet -> String
                            .format(" pt%d %s (%s)", planet.getTick(), Formatter.format(planet.getValue()),
                                Formatter.format(planet.getValueGrowth()))).collect(Collectors.joining("|"));

                    return String
                        .format("Value in the last %d ticks on %d:%d:%d%s", p.size(), x, y, z, valueString);
                } else {
                    return String
                        .format("Planet %d:%d:%d does not exist", x,y,z);
                }
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else
            return getErrorMessage();
    }
}
