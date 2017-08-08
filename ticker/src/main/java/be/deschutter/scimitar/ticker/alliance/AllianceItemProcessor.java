package be.deschutter.scimitar.ticker.alliance;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class AllianceItemProcessor
        implements ItemProcessor<AllianceStaging, AllianceStaging> {

    @Override
    public AllianceStaging process(final AllianceStaging allianceStaging)
            throws Exception {
        return allianceStaging;
    }
}
