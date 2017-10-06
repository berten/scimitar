package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MaxcapListener implements Listener {

    @Autowired
    private PlanetService planetService;
    @Autowired
    private PaConfig paConfig;

    @Override
    public String getCommand() {
        return "maxcap";
    }

    @Override
    public String getPattern() {
        return "xTarget yTarget zTarget xAttacker yAttacker zAttacker <War Bonus y|n>";
    }

    @Override
    public String getResult(final String... parameters) {
        if (parameters.length == 3) {
            return "not implemented yet";
        } else if (parameters.length == 6) {
            try {
                int xTarget = Integer.parseInt(parameters[0]);
                int yTarget = Integer.parseInt(parameters[1]);
                int zTarget = Integer.parseInt(parameters[2]);

                int xAttacker = Integer.parseInt(parameters[3]);
                int yAttacker = Integer.parseInt(parameters[4]);
                int zAttacker = Integer.parseInt(parameters[5]);

                final Planet attackingPlanet = planetService
                    .findBy(xAttacker, yAttacker, zAttacker);
                final Planet targetPlanet = planetService
                    .findBy(xTarget, yTarget, zTarget);

                final BigDecimal modifier = BigDecimal
                    .valueOf(targetPlanet.getValue())
                    .divide(BigDecimal.valueOf(attackingPlanet.getValue()));
                final double stuffz = Math.pow(modifier.doubleValue(), 0.5);

                final double maxcap = paConfig.getMaxCap() * stuffz;
                return "maxcap: " + Math.floor(maxcap * 100) + "% " + Math
                    .floor(targetPlanet.getSize() * maxcap);
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else
            return getErrorMessage();
    }
}
