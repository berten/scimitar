package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.galaxy.Galaxy;
import be.deschutter.scimitar.galaxy.GalaxyEao;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class LookupListener implements Listener {

    private final PlanetEao planetEao;
    private final GalaxyEao galaxyEao;
    private TickerInfoEao tickerInfoEao;

    @Autowired
    public LookupListener(final PlanetEao planetEao, final GalaxyEao galaxyEao,
        final TickerInfoEao tickerInfoEao) {
        this.planetEao = planetEao;
        this.galaxyEao = galaxyEao;
        this.tickerInfoEao = tickerInfoEao;
    }

    @Override
    public String getCommand() {
        return "lookup";
    }

    @Override
    public String getPattern() {
        return "x y [z]";
    }


    @Override
    public String getResult(String username, String... parameters) {

        if (parameters != null && parameters.length == 2) {
            try {
                int x = Integer.parseInt(parameters[0]);
                int y = Integer.parseInt(parameters[1]);

                Galaxy g = galaxyEao.findFirstByXAndYOrderByTickDesc(x, y);
                if (g == null)
                    return "Galaxy " + x + ":" + y + " does not exist";
                return format(
                        "%d:%d '%s' (%d) Score: %d (%d) Value: %d (%d) Size: %d (%d) XP: %d (%d)",
                        g.getX(), g.getY(), g.getGalaxyName(), g.getPlanets(),
                        g.getScore(), g.getScoreRank(), g.getValue(),
                        g.getValueRank(), g.getSize(), g.getSizeRank(), g.getXp(),
                        g.getXpRank());
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        }
        if (parameters != null && parameters.length == 3) {
            try {
                int x = Integer.parseInt(parameters[0]);
                int y = Integer.parseInt(parameters[1]);
                int z = Integer.parseInt(parameters[2]);


                final TickerInfo currentTick = tickerInfoEao
                    .findFirstByOrderByTickDesc();

                Planet p = planetEao
                    .findByXAndYAndZAndTick(x, y, z, currentTick.getTick());

                if (p == null)
                    return "Planet " + x + ":" + y + ":" + z
                            + " does not exist";
                return format(
                        "%d:%d:%d (%s) '%s of %s' Score: %d (%d) Value: %d (%d) Size: %d (%d) XP: %d (%d)",
                        p.getX(), p.getY(), p.getZ(), p.getRace(), p.getRulerName(),
                        p.getPlanetName(), p.getScore(), p.getScoreRank(),
                        p.getValue(), p.getValueRank(), p.getSize(),
                        p.getSizeRank(), p.getXp(), p.getXpRank());
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else
            return getErrorMessage();
    }
}