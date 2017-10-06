package be.deschutter.scimitar.galaxy;

import be.deschutter.scimitar.ticker.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GalaxyServiceImpl implements GalaxyService {
    private final GalaxyEao galaxyEao;
    private final TickerService tickerService;

    @Autowired
    public GalaxyServiceImpl(final GalaxyEao galaxyEao,
        final TickerService tickerService) {
        this.galaxyEao = galaxyEao;
        this.tickerService = tickerService;
    }

    @Override
    public Galaxy findBy(final int x, final int y) {
        final Galaxy galaxy = galaxyEao
            .findByXAndYAndTick(x, y, tickerService.getCurrentTick().getTick());
        if (galaxy == null)
            throw new GalaxyDoesNotExistException(x, y);
        return galaxy;
    }
}
