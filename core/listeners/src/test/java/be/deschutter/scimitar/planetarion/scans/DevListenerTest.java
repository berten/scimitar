package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.scans.ScanType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DevListenerTest extends ShowScanTest {
    @InjectMocks
    private DevListener listener;



    @Override
    protected String getExpectedPattern() {
        return "dev";
    }

    @Override
    protected ScanType getScanType() {
        return ScanType.D;
    }

    @Override
    protected Listener getListener() {
        return listener;
    }
}