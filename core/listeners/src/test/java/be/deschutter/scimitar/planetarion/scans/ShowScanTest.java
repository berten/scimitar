package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
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
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public abstract class ShowScanTest {

    private static final String SCANURL = "scanurl";
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
        when(paConfig.getScanurl())
            .thenReturn(SCANURL);
        when(tickerInfoEao.findFirstByOrderByTickDesc())
            .thenReturn(new TickerInfo(123));
        final Planet p = new Planet();
        p.setId("planetId");
        when(planetEao.findByXAndYAndZAndTick(1, 2, 3, 123)).thenReturn(p);
    }

    @Test
    public void getResult_PlanetDoesNotExist() throws Exception {
        assertThat(getListener().getResult("berten", "1", "1", "1"))
            .isEqualTo("Planet 1:1:1 could not be found");
    }

    @Test
    public void getResult_ToFewParameters() throws Exception {
        assertThat(getListener().getResult("berten", "1", "1")).isEqualTo(
            "Error: use following pattern for command " + getExpectedPattern()
                + ": x y z");
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(getListener().getCommand()).isEqualTo(getExpectedPattern());
    }

    protected abstract String getExpectedPattern();

    @Test
    public void getResult_NumberFormat() throws Exception {
        assertThat(getListener().getResult("berten", "1", "1", "z")).isEqualTo(
            "Error: use following pattern for command " + getExpectedPattern()
                + ": x y z");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(getListener().getPattern()).isEqualTo("x y z");
    }

    @Test
    public void getResult() throws Exception {
        final Scan s = new Scan();
        s.setTick(100);
        s.setPlanetId("planetId");
        s.setScanType(getScanType());
        s.setId("scanId");
        when(scanEao.findFirstByPlanetIdAndScanTypeOrderByTickDesc("planetId",
            getScanType())).thenReturn(s);

        assertThat(getListener().getResult("berten", "1", "2", "3")).isEqualTo(
            "Latest " + getScanType().name() + " scan on 1:2:3 (23 ticks old): "
                + SCANURL+"=scanId");
    }

    @Test
    public void getResult_noScan() throws Exception {
        assertThat(getListener().getResult("berten", "1", "2", "3")).isEqualTo(
            "No " + getScanType().name() + " scan could be found for 1:2:3");
    }

    protected abstract ScanType getScanType();

    protected abstract Listener getListener();
}
