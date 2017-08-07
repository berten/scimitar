package be.deschutter.scimitar.ticker.planet;

import be.deschutter.scimitar.PlanetStaging;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PlanetStagingItemProcessor
    implements ItemProcessor<PlanetStaging, PlanetStaging> {

    @Override
    public PlanetStaging process(final PlanetStaging planetStaging)
        throws Exception {
        return planetStaging;
    }
}
