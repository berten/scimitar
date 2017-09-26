package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.scans.ScanType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PlanetListenerTest extends ShowScanTest {
    @InjectMocks
    private PlanetListener listener;


    @Override
    protected String getExpectedPattern() {
        return "planet";
    }

    @Override
    protected ScanType getScanType() {
        return ScanType.P;
    }

    @Override
    protected Listener getListener() {
        return listener;
    }
}