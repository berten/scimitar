package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.scans.ScanType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class NewsListenerTest extends ShowScanTest {
    @InjectMocks
    private NewsListener listener;



    @Override
    protected String getExpectedPattern() {
        return "news";
    }

    @Override
    protected ScanType getScanType() {
        return ScanType.N;
    }

    @Override
    protected Listener getListener() {
        return listener;
    }
}