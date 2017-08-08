package be.deschutter.scimitar.ticker.alliance;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
@StepScope
public class AllianceFieldSetMapper
        implements FieldSetMapper<AllianceStaging> {

    @Value("#{jobParameters['tick']}")
    private int tick;

    @Override
    public AllianceStaging mapFieldSet(final FieldSet fieldSet)
            throws BindException {

        try {
            fieldSet.readInt("size");
        } catch (NumberFormatException e) {
            return null;
        }

        AllianceStaging allianceStaging = new AllianceStaging();
        allianceStaging.setTick(tick);
        allianceStaging.setRank(fieldSet.readInt("rank"));
        allianceStaging.setSize(fieldSet.readInt("size"));
        allianceStaging.setAllianceName(fieldSet.readString("alliance_name"));
        allianceStaging.setMembers(fieldSet.readInt("members"));
        allianceStaging.setCountedScore(fieldSet.readLong("counted_score"));
        allianceStaging.setPoints(fieldSet.readLong("points"));
        allianceStaging.setTotalScore(fieldSet.readLong("total_score"));
        allianceStaging.setTotalValue(fieldSet.readLong("total_value"));
        return allianceStaging;
    }
}
