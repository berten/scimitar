package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.scans.Scan;
import be.deschutter.scimitar.scans.ScanService;
import be.deschutter.scimitar.scans.ScanType;
import be.deschutter.scimitar.ticker.TickerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScansListenerTest {
    private static final String SCANURL = "scanurl";
    @InjectMocks
    private ScansListener scansListener;

    @Mock
    private TickerService tickerService;

    @Mock
    private ScanService scanService;

    @Mock
    private PaConfig paConfig;

    @Before
    public void setUp() throws Exception {
        when(tickerService.getCurrentTick()).thenReturn(new TickerInfo(123));

        when(paConfig.getScanurl()).thenReturn(SCANURL);
    }

    @Test
    public void getResult_ToFewParameters() throws Exception {
        assertThat(scansListener.getResult("1", "1"))
            .isEqualTo("Error: use following pattern for command scans: x y z");
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(scansListener.getCommand()).isEqualTo("scans");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(scansListener.getPattern()).isEqualTo("x y z");
    }

    @Test
    public void getResult() throws Exception {
        final Scan s = new Scan();
        s.setTick(100);
        s.setPlanetId("planetId");
        s.setScanType(ScanType.A);
        s.setId("scanId");

        final Scan s2 = new Scan();
        s2.setTick(123);
        s2.setPlanetId("planetId");
        s2.setScanType(ScanType.J);
        s2.setId("scanId2");

        final Scan s3 = new Scan();
        s3.setTick(120);
        s3.setPlanetId("planetId");
        s3.setScanType(ScanType.D);
        s3.setId("scanId3");

        when(scanService.findLast5Scans(1, 2, 3))
            .thenReturn(Arrays.asList(s, s2, s3));

        assertThat(scansListener.getResult("1", "2", "3")).isEqualTo(
            "Latest 3 scans on 1:2:3 A (23 ticks old): " + SCANURL
                + "=scanId | J (0 ticks old): " + SCANURL
                + "=scanId2 | D (3 ticks old): " + SCANURL + "=scanId3");
    }

}
