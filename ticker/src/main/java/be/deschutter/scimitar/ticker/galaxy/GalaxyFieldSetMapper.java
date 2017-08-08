package be.deschutter.scimitar.ticker.galaxy;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
@StepScope
public class GalaxyFieldSetMapper
        implements FieldSetMapper<GalaxyStaging> {

    @Value("#{jobParameters['tick']}")
    private int tick;

    @Override
    public GalaxyStaging mapFieldSet(final FieldSet fieldSet)
            throws BindException {

        try {
            fieldSet.readInt("x");
        } catch (NumberFormatException e) {
            return null;
        }

        GalaxyStaging galaxyStaging = new GalaxyStaging();
        galaxyStaging.setTick(tick);
        galaxyStaging.setX(fieldSet.readInt("x"));
        galaxyStaging.setY(fieldSet.readInt("y"));
        galaxyStaging.setGalaxyName(fieldSet.readString("galaxy_name"));
        galaxyStaging.setScore(fieldSet.readLong("score"));
        galaxyStaging.setValue(fieldSet.readLong("value"));
        galaxyStaging.setXp(fieldSet.readLong("xp"));
        galaxyStaging.setSize(fieldSet.readInt("size"));
        return galaxyStaging;
    }
}
