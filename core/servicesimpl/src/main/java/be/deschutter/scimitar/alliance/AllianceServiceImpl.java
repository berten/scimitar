package be.deschutter.scimitar.alliance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllianceServiceImpl implements AllianceService {
    private final AllianceEao allianceEao;

    @Autowired
    public AllianceServiceImpl(final AllianceEao allianceEao) {
        this.allianceEao = allianceEao;
    }

    @Override
    public Alliance findBy(final String allianceName) {
        final Alliance alliance = allianceEao
            .findByAllianceNameIgnoreCaseContaining(allianceName);
        if (alliance == null)
            throw new AllianceNotFoundException(allianceName);
        return alliance;
    }
}
