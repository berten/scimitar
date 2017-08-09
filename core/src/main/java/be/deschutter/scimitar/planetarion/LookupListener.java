package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.Galaxy;
import be.deschutter.scimitar.GalaxyEao;
import be.deschutter.scimitar.Planet;
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
            if(g == null)
                return "Galaxy " + x + ":" + y + " does not exist";
            return g.getX() + ":" + g.getY() + " '" + g.getGalaxyName() + "' (" + g.getPlanets() + ") Score: " + g.getScore() + " (" + g.getScoreRank() + ") Value: " + g.getValue() + " (" + g.getValueRank() + ") Size: " + g.getSize() + " (" + g.getSizeRank() + ") XP: " + g.getXp() + " (" + g.getXpRank() + ")";
        }
        if (parameters != null && parameters.length == 3) {
            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);
            Planet p = planetEao.findFirstByXAndYAndZOrderByTickDesc(x,y,z);
            if(p == null)
                return "Planet " + x + ":" + y + ":" + z + " does not exist";
            return p.getX() + ":" + p.getY() + ":" + p.getZ() +" '" + p.getRulerName() + " of " + p.getPlanetName() + "' Score: " + p.getScore() + " (" + p.getScoreRank() + ") Value: " + p.getValue() + " (" + p.getValueRank() + ") Size: " + p.getSize() + " (" + p.getSizeRank() + ") XP: " + p.getXp() + " (" + p.getXpRank() + ")";
        } else
            return "Error: use following pattern for command " + getCommand() + ": " + getPattern();
    }


}