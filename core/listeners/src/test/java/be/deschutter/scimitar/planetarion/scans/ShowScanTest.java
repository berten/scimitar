package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.scans.Scan;
import be.deschutter.scimitar.scans.ScanService;
import be.deschutter.scimitar.scans.ScanType;
import be.deschutter.scimitar.ticker.TickerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public abstract class ShowScanTest {

    private static final String SCANURL = "scanurl";
    @Mock
    private TickerService tickerService;
    @Mock
    private ScanService scanService;
    @Mock
    private PaConfig paConfig;

    @Before
    public void setUp() throws Exception {
        when(paConfig.getScanurl()).thenReturn(SCANURL);
        when(tickerService.getCurrentTick()).thenReturn(new TickerInfo(123));
    }



    @Test
    public void getResult_ToFewParameters() throws Exception {
        assertThat(getListener().getResult("1", "1")).isEqualTo(
            "Error: use following pattern for command " + getExpectedPattern()
                + ": x y z");
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(getListener().getCommand()).isEqualTo(getExpectedPattern());
    }

    protected abstract String getExpectedPattern();


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
        when(scanService.findLastScanFor(1, 2, 3, getScanType())).thenReturn(s);

        assertThat(getListener().getResult("1", "2", "3")).isEqualTo(
            "Latest " + getScanType().name() + " scan on 1:2:3 (23 ticks old): "
                + SCANURL + "=scanId");
    }



    protected abstract ScanType getScanType();

    protected abstract Listener getListener();
}
