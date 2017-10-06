package be.deschutter.scimitar.planet;

import be.deschutter.scimitar.ticker.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService {
    private final PlanetEao planetEao;
    private TickerService tickerService;

    @Autowired
    public PlanetServiceImpl(final PlanetEao planetEao,
        final TickerService tickerService) {
        this.planetEao = planetEao;
        this.tickerService = tickerService;
    }

    @Override
    public Planet findBy(final int x, final int y, final int z) {
        final Planet planet = planetEao.findByXAndYAndZAndTick(x, y, z,
            tickerService.getCurrentTick().getTick());
        if (planet == null)
            throw new PlanetDoesNotExistException(x, y, z);
        return planet;
    }

    @Override
    public List<Planet> findBy(final int x, final int y) {
        final List<Planet> planets = planetEao
            .findByXAndYAndTick(x, y, tickerService.getCurrentTick().getTick());
        if (planets == null || planets.isEmpty())
            throw new NoPlanetsFoundInGalaxyException(x, y);
        return planets;
    }

    @Override
    public Planet findBy(final String planetId) {
        return planetEao.findByIdAndTick(planetId,
            tickerService.getCurrentTick().getTick());
    }

    @Override
    public List<Planet> findFirst15ByXAndYAndZOrderByTickDesc(final int x,
        final int y, final int z) {
        return planetEao
            .findFirst15ByIdOrderByTickDesc(findBy(x, y, z).getId());
    }
}
