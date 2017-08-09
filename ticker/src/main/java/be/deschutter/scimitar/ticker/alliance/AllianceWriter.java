package be.deschutter.scimitar.ticker.alliance;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class AllianceWriter implements ItemWriter<AllianceStaging> {
    private final AllianceStagingEao allianceStagingEao;

    @Autowired
    public AllianceWriter(AllianceStagingEao allianceStagingEao) {
        this.allianceStagingEao = allianceStagingEao;
    }

    @Override
    public void write(final List<? extends AllianceStaging> list)
        throws Exception {
        allianceStagingEao.save(list);

    }
}
