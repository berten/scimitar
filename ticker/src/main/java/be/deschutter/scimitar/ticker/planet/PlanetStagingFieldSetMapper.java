package be.deschutter.scimitar.ticker.planet;

import be.deschutter.scimitar.PlanetStaging;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PlanetStagingFieldSetMapper
        implements FieldSetMapper<PlanetStaging> {
    @Override
    public PlanetStaging mapFieldSet(final FieldSet fieldSet)
            throws BindException {

        try {fieldSet.readInt("x");
        } catch (NumberFormatException e) {
            return null;
        }

        PlanetStaging planetStaging = new PlanetStaging();
        planetStaging.setId(fieldSet.readString("id"));
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
