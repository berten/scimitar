package be.deschutter.scimitar.scans;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScanServiceImplTest {
    @InjectMocks
    private ScanServiceImpl scanService;
    @Mock
    private ScanEao scanEao;
    @Mock
    private PlanetService planetService;

    @Before
    public void setUp() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
    }

    @Test
    public void findLastScanFor() throws Exception {

        final Scan expectedScan = new Scan();
        when(scanEao.findFirstByPlanetIdAndScanTypeOrderByTickDesc("planetId",
            ScanType.A)).thenReturn(expectedScan);
        assertThat(scanService.findLastScanFor(1, 2, 3, ScanType.A))
            .isSameAs(expectedScan);
    }

    @Test(expected = ScanNotFoundException.class)
    public void findLastScanFor_NoScanFound() throws Exception {
        try {
            scanService.findLastScanFor(1, 2, 3, ScanType.A);
        } catch (ScanNotFoundException e) {
            assertThat(e.getScanType()).isSameAs(ScanType.A);
            assertThat(e.getX()).isEqualTo(1);
            assertThat(e.getY()).isEqualTo(2);
            assertThat(e.getZ()).isEqualTo(3);
            throw e;
        }
        fail("Should have thrown an exception");
    }

    @Test
    public void findLast5Scans() throws Exception {
        final List<Scan> scans = Collections.singletonList(new Scan());
        when(scanEao.findFirst5ByPlanetIdOrderByTickDesc("planetId"))
            .thenReturn(scans);
        assertThat(scanService.findLast5Scans(1,2,3)).isSameAs(scans);
    }

    @Test(expected = NoScansFoundException.class)
    public void findLast5Scans_exception() throws Exception {
        try {
            scanService.findLast5Scans(1,2,3);
        } catch (NoScansFoundException e) {
            assertThat(e.getX()).isEqualTo(1);
            assertThat(e.getY()).isEqualTo(2);
            assertThat(e.getZ()).isEqualTo(3);
            throw e;
        }
        fail("Should have thrown an exception");
    }

}