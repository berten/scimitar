package be.deschutter.scimitar.ticker.galaxy;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class GalaxyItemProcessor
        implements ItemProcessor<GalaxyStaging, GalaxyStaging> {

    @Override
    public GalaxyStaging process(final GalaxyStaging galaxyStaging)
            throws Exception {
        return galaxyStaging;
    }
}
