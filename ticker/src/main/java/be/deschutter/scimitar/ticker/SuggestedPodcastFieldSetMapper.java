package be.deschutter.scimitar.ticker;

import be.deschutter.scimitar.PlanetStaging;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class SuggestedPodcastFieldSetMapper
    implements FieldSetMapper<PlanetStaging> {
    @Override
    public PlanetStaging mapFieldSet(final FieldSet fieldSet)
        throws BindException {
        return null;
    }
}
