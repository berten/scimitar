package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.scans.ScanType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UnitListenerTest extends ShowScanTest {
    @InjectMocks
    private UnitListener listener;


    @Override
    protected String getExpectedPattern() {
        return "unit";
    }

    @Override
    protected ScanType getScanType() {
        return ScanType.U;
    }

    @Override
    protected Listener getListener() {
        return listener;
    }
}