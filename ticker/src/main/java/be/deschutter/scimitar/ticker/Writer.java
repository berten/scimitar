package be.deschutter.scimitar.ticker;

import be.deschutter.scimitar.PlanetStaging;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Writer implements ItemWriter<PlanetStaging> {
    @Override
    public void write(final List<? extends PlanetStaging> list)
        throws Exception {

    }
}
