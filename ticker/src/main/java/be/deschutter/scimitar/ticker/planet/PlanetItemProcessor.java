package be.deschutter.scimitar.ticker.planet;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PlanetItemProcessor
        implements ItemProcessor<PlanetStaging, PlanetStaging> {

    @Override
    public PlanetStaging process(final PlanetStaging planetStaging)
            throws Exception {
        return planetStaging;
    }
}
