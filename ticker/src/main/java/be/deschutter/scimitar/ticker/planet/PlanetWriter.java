package be.deschutter.scimitar.ticker.planet;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class PlanetWriter implements ItemWriter<PlanetStaging> {
    private final PlanetStagingEao planetStagingEao;

    @Autowired
    public PlanetWriter(PlanetStagingEao planetStagingEao) {
        this.planetStagingEao = planetStagingEao;
    }

    @Override
    public void write(final List<? extends PlanetStaging> list)
        throws Exception {
        planetStagingEao.save(list);
    }
}
