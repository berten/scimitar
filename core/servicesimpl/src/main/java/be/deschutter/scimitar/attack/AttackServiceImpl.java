package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.security.SecurityHelper;
import be.deschutter.scimitar.ticker.TickerService;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import be.deschutter.scimitar.user.RoleEnum;
import be.deschutter.scimitar.user.ScimitarUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttackServiceImpl implements AttackService {
    private final PaConfig paConfig;
    private final TickerService tickerService;
    private final AttackEao attackEao;
    private final BattleGroupService battleGroupService;
    private final PlanetService planetService;
    private final SecurityHelper securityHelper;

    @Autowired
    public AttackServiceImpl(final PaConfig paConfig,
        final TickerService tickerService, final AttackEao attackEao,
        final BattleGroupService battleGroupService,
        final PlanetService planetService,
        final SecurityHelper securityHelper) {
        this.paConfig = paConfig;
        this.tickerService = tickerService;
        this.attackEao = attackEao;
        this.battleGroupService = battleGroupService;
        this.planetService = planetService;
        this.securityHelper = securityHelper;
    }

    @Override
    public Attack createAttack(final int ltEta, final int waves,
        final String battleGroupName) {

        final Attack a = new Attack();
        a.setLandTick(computeLandTick(ltEta));
        if (battleGroupName != null)
            a.setBattleGroup(battleGroupService.findByName(battleGroupName));

        return attackEao.save(a);
    }

    @Override
    public Attack addComment(final int attackId, final String comment) {
        final Attack attack = findAttack(attackId);
        attack.setComment(comment);
        return attackEao.save(attack);
    }

    @Override
    public Attack findAttack(final int attackId) {
        final Attack a = attackEao.findOne(attackId);
        if (a == null)
            throw new AttackDoesNotExistException(attackId);
        return a;
    }

    @Override
    public Attack addTarget(final int attackId, final int x, final int y,
        final int z) {
        final Attack attack = findAttack(attackId);
        if (z == 0) {
            planetService.findBy(x, y)
                .forEach(planet -> attack.addPlanet(planet.getId()));
        } else {
            final Planet planet = planetService
                .findBy(x, y, z);
            attack.addPlanet(planet.getId());
        }
        return attackEao.save(attack);

    }

    @Override
    public List<Attack> findLast5Attacks() {
        final ScimitarUser scimitarUser = securityHelper.getLoggedInUser();
        if (!scimitarUser.hasRole(RoleEnum.BC)) {
            final List<Attack> attacks = attackEao
                .findLast5ByBattleGroupIsNullOrBattleGroupInOrderByLandTickDesc(
                    scimitarUser.getBattleGroups());
            if (attacks == null || attacks.isEmpty())
                throw new NoAttacksFoundException();
            return attacks;
        } else {
            final List<Attack> attacks = attackEao
                .findLast5ByOrderByLandTickDesc();
            if (attacks == null || attacks.isEmpty())
                throw new NoAttacksFoundException();
            return attacks;
        }
    }

    @Override
    public Attack removeTarget(final int attackId, final int x, final int y,
        final int z) {
        final Attack attack = findAttack(attackId);
        if (z == 0) {
            planetService.findBy(x, y)
                .forEach(planet -> attack.removePlanet(planet.getId()));
        } else {
            final Planet planet = planetService
                .findBy(x, y, z);
            attack.removePlanet(planet.getId());
        }
        return attackEao.save(attack);
    }



    private int computeLandTick(final int ltEta) {
        int landtick;

        if (ltEta < paConfig.getProtection() + 8) {
            landtick = Math
                .toIntExact(tickerService.getCurrentTick().getTick() + ltEta);
        } else
            landtick = ltEta;
        return landtick;
    }
}
