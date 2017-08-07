package be.deschutter.scimitar.ticker.planet;

import be.deschutter.scimitar.PlanetStaging;
import be.deschutter.scimitar.PlanetStagingEao;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PlanetStagingWriter implements ItemWriter<PlanetStaging> {
    @Autowired
    private PlanetStagingEao planetStagingEao;

    @Override
    public void write(final List<? extends PlanetStaging> list)
            throws Exception {
        planetStagingEao.save(list);
    }
}
