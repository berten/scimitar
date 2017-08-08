package be.deschutter.scimitar.ticker.planet;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
@StepScope
public class PlanetFieldSetMapper
        implements FieldSetMapper<PlanetStaging> {

    @Value("#{jobParameters['tick']}")
    private int tick;

    @Override
    public PlanetStaging mapFieldSet(final FieldSet fieldSet)
            throws BindException {

        try {
            fieldSet.readInt("x");
        } catch (NumberFormatException e) {
            return null;
        }

        PlanetStaging planetStaging = new PlanetStaging();
        planetStaging.setId(fieldSet.readString("id"));
        planetStaging.setTick(tick);
        planetStaging.setX(fieldSet.readInt("x"));
        planetStaging.setY(fieldSet.readInt("y"));
        planetStaging.setZ(fieldSet.readInt("z"));
        planetStaging.setPlanetName(fieldSet.readString("planet_name"));
        planetStaging.setRulerName(fieldSet.readString("ruler_name"));
        planetStaging.setRace(fieldSet.readString("race"));
        planetStaging.setScore(fieldSet.readLong("score"));
        planetStaging.setValue(fieldSet.readLong("value"));
        planetStaging.setXp(fieldSet.readLong("xp"));
        planetStaging.setSize(fieldSet.readInt("size"));
        planetStaging.setSpecial(fieldSet.readString("special"));
        return planetStaging;
    }
}
