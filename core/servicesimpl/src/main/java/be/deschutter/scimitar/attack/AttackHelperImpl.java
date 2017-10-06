package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AttackHelperImpl implements AttackHelper {

    private PlanetService planetService;

    @Autowired
    public AttackHelperImpl(final PlanetService planetService) {
        this.planetService = planetService;
    }

    @Override
    public String getAttackInfo(final Attack attack) {
        if (attack.getBattleGroup() != null) {
            return String
                .format("Attack %d: BG: %s Comment: LT %d %s Targets: %s",
                    attack.getId(), attack.getBattleGroup().getName(),
                    attack.getLandTick(), attack.getComment(),
                    getAttackCoords(attack));
        } else
            return String.format("Attack %d: Comment: LT %d %s Targets: %s",
                attack.getId(), attack.getLandTick(), attack.getComment(),
                getAttackCoords(attack));
    }

    private String getAttackCoords(final Attack savedAttack) {
        return savedAttack.getPlanetIds().stream().map(s -> {
            final Planet planet = planetService.findBy(s);
            return String.format("%d:%d:%d", planet.getX(), planet.getY(),
                planet.getZ());
        }).sorted(String::compareTo).collect(Collectors.joining(" "));
    }

}
