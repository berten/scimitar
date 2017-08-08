package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.Galaxy;
import be.deschutter.scimitar.GalaxyEao;
import be.deschutter.scimitar.PlanetEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LookupListener implements Listener {

    @Autowired
    private PlanetEao planetEao;

    @Autowired
    private GalaxyEao galaxyEao;


    @Override
    public String getCommand() {
        return "lookup";
    }

    @Override
    public String getPattern() {
        return "x y [z]";
    }

    @Override
    public String getResult(String... parameters) {
        if (parameters != null && parameters.length == 2) {
            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            Galaxy g = galaxyEao.findFirstByXAndYOrderByTickDesc(x, y);
            return g.getX() + ":" + g.getY() + " '" + g.getGalaxyName() + "' (" + g.getPlanets() + ") Score: " + g.getScore() + " (" + g.getScoreRank() + ") Value: " + g.getValue() + " (" + g.getValueRank() + ") Size: " + g.getSize() + " (" + g.getSizeRank() + ") XP: " + g.getXp() + "(" + g.getXpRank() + ")";
        }
        if (parameters != null && parameters.length == 3) {
            return "stuffz2";
        } else
            return "Error: use following pattern for command " + getCommand() + ": " + getPattern();
    }


}