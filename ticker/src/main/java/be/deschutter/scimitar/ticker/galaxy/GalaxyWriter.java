package be.deschutter.scimitar.ticker.galaxy;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class GalaxyWriter implements ItemWriter<GalaxyStaging> {
    private final GalaxyStagingEao galaxyStagingEao;

    @Autowired
    public GalaxyWriter(GalaxyStagingEao galaxyStagingEao) {
        this.galaxyStagingEao = galaxyStagingEao;
    }

    @Override
    public void write(final List<? extends GalaxyStaging> list)
        throws Exception {
        galaxyStagingEao.save(list);
    }
}
