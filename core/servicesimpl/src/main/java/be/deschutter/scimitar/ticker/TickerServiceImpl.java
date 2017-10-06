package be.deschutter.scimitar.ticker;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TickerServiceImpl implements TickerService {
    private final TickerInfoEao tickerInfoEao;

    @Autowired
    public TickerServiceImpl(final TickerInfoEao tickerInfoEao) {
        this.tickerInfoEao = tickerInfoEao;
    }

    @Override
    public TickerInfo getCurrentTick() {
        final TickerInfo tickerInfo = tickerInfoEao
            .findFirstByOrderByTickDesc();
        if (tickerInfo == null)
            throw new NoTickFoundException();
        return tickerInfo;
    }
}
