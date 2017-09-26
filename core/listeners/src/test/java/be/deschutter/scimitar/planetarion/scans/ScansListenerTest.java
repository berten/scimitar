package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.scans.Scan;
import be.deschutter.scimitar.scans.ScanEao;
import be.deschutter.scimitar.scans.ScanType;
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
    private TickerInfoEao tickerInfoEao;
    @Mock
    private PlanetEao planetEao;
    @Mock
    private ScanEao scanEao;

    @Mock
    private PaConfig paConfig;

    @Before
    public void setUp() throws Exception {
        when(tickerInfoEao.findFirstByOrderByTickDesc())
            .thenReturn(new TickerInfo(123));
        final Planet p = new Planet();
        p.setId("planetId");
        when(planetEao.findByXAndYAndZAndTick(1, 2, 3, 123)).thenReturn(p);
        when(paConfig.getScanurl()).thenReturn(SCANURL);
    }

    @Test
    public void getResult_PlanetDoesNotExist() throws Exception {
        assertThat(scansListener.getResult("berten", "1", "1", "1"))
            .isEqualTo("Planet 1:1:1 could not be found");
    }

    @Test
    public void getResult_ToFewParameters() throws Exception {
        assertThat(scansListener.getResult("berten", "1", "1"))
            .isEqualTo("Error: use following pattern for command scans: x y z");
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(scansListener.getCommand()).isEqualTo("scans");
    }

    @Test
    public void getResult_NumberFormat() throws Exception {
        assertThat(scansListener.getResult("berten", "1", "1", "z"))
            .isEqualTo("Error: use following pattern for command scans: x y z");
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

        when(scanEao.findFirst5ByPlanetIdOrderByTickDesc("planetId"))
            .thenReturn(Arrays.asList(s, s2, s3));

        assertThat(scansListener.getResult("berten", "1", "2", "3")).isEqualTo(
            "Latest 3 scans on 1:2:3 A (23 ticks old): " + SCANURL
                + "=scanId | J (0 ticks old): " + SCANURL
                + "=scanId2 | D (3 ticks old): " + SCANURL + "=scanId3");
    }

    @Test
    public void getResult_noScan() throws Exception {
        assertThat(scansListener.getResult("berten", "1", "2", "3"))
            .isEqualTo("No scans could be found for 1:2:3");
    }

}
