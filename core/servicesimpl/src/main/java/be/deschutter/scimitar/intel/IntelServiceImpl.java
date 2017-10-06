package be.deschutter.scimitar.intel;

import be.deschutter.scimitar.alliance.Alliance;
import be.deschutter.scimitar.alliance.AllianceService;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntelServiceImpl implements IntelService {
    private final IntelEao intelEao;
    private final PlanetService planetService;
    private final AllianceService allianceService;
    private final PlayerService playerService;

    @Autowired
    public IntelServiceImpl(final IntelEao intelEao,
        final PlanetService planetService,
        final AllianceService allianceService,
        final PlayerService playerService) {
        this.intelEao = intelEao;
        this.planetService = planetService;
        this.allianceService = allianceService;
        this.playerService = playerService;
    }

    @Override
    public Intel findBy(final int x, final int y, final int z) {
        final Planet planet = planetService.findBy(x, y, z);
        final Intel intel = intelEao.findByPlanetId(planet.getId());
        if (intel == null)
            throw new IntelNotFoundException(x, y, z);
        return intel;
    }

    @Override
    public Intel changeNick(final int x, final int y, final int z,
        final String newNickname) {
        final Player player = findPlayer(newNickname);
        final Planet planet = planetService.findBy(x, y, z);
        Intel intel = findOrCreateIntel(planet);
        intel.setPlayer(player);
        return intelEao.save(intel);
    }

    private Intel findOrCreateIntel(final Planet planet) {
        Intel intel = intelEao.findByPlanetId(planet.getId());
        if (intel == null)
            intel = new Intel(planet.getId());
        return intel;
    }

    private Player findPlayer(final String newNickname) {
        try {
            return playerService.findBy(newNickname);
        } catch (PlayerNotFoundException e) {
            return playerService.createPlayer(newNickname);
        }
    }

    @Override
    public Intel changeAlliance(final int x, final int y, final int z,
        final String allianceName) {
        final Alliance alliance = allianceService.findBy(allianceName);
        Intel intel = findOrCreateIntel(planetService.findBy(x, y, z));
        intel.setAlliance(alliance);
        return intelEao.save(intel);
    }

    @Override
    public Intel addNick(final int x, final int y, final int z,
        final String newNickname) {
        final Intel intel = findOrCreateIntel(planetService.findBy(x, y, z));
        if (intel.getPlayer() == null)
            intel.setPlayer(findPlayer(newNickname));
        else
            intel.getPlayer().addNick(newNickname);
        return intelEao.save(intel);
    }
}
