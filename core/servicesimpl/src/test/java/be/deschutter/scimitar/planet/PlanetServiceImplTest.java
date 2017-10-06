package be.deschutter.scimitar.planet;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.ticker.TickerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanetServiceImplTest {
    @InjectMocks
    private PlanetServiceImpl planetService;
    @Mock
    private PlanetEao planetEao;
    @Mock
    private TickerService tickerService;

    @Before
    public void setUp() throws Exception {
        when(tickerService.getCurrentTick()).thenReturn(new TickerInfo(123));
    }

    @Test
    public void findBy() throws Exception {
        final List<Planet> planets = new ArrayList<>();
        planets.add(new Planet());
        when(planetEao.findByXAndYAndTick(1, 2, 123)).thenReturn(planets);
        assertThat(planetService.findBy(1, 2)).isSameAs(planets);
    }

    @Test(expected = NoPlanetsFoundInGalaxyException.class)
    public void findBy_NullFound() throws Exception {
        when(planetService.findBy(1, 2)).thenReturn(null);
        try {
            planetService.findBy(1, 2);
        } catch (NoPlanetsFoundInGalaxyException e) {
            assertNoPlanetsFoundInGalaxyException(e);
            return;
        }
        fail("Should have thrown an exception");
    }

    private void assertNoPlanetsFoundInGalaxyException(
        final NoPlanetsFoundInGalaxyException e) {
        assertThat(e.getX()).isEqualTo(1);
        assertThat(e.getY()).isEqualTo(2);
        throw e;
    }

    @Test(expected = NoPlanetsFoundInGalaxyException.class)
    public void findBy_EmptyListFound() throws Exception {
        when(planetEao.findByXAndYAndTick(1, 2, 123))
            .thenReturn(new ArrayList<>());
        try {
            planetService.findBy(1, 2);
        } catch (NoPlanetsFoundInGalaxyException e) {
            assertNoPlanetsFoundInGalaxyException(e);
        }
        fail("Should have thrown an exception");
    }

    @Test
    public void findByPlanet() throws Exception {
        final Planet planet = new Planet();
        when(planetEao.findByXAndYAndZAndTick(1, 2, 3, 123)).thenReturn(planet);
        assertThat(planetService.findBy(1, 2, 3)).isEqualTo(planet);
    }

    @Test(expected = PlanetDoesNotExistException.class)
    public void findByPlanet_NothingFound() throws Exception {
        when(planetEao.findByXAndYAndZAndTick(1, 2, 3, 123)).thenReturn(null);
        try {
            planetService.findBy(1, 2, 3);
        } catch (PlanetDoesNotExistException e) {
            assertThat(e.getX()).isEqualTo(1);
            assertThat(e.getY()).isEqualTo(2);
            assertThat(e.getZ()).isEqualTo(3);
            throw e;
        }
        fail("Should have thrown an exception");
    }

    @Test
    public void findByPlanetId() throws Exception {
        final Planet planet = new Planet();
        when(planetEao.findByIdAndTick("planetId", 123)).thenReturn(planet);
        assertThat(planetService.findBy("planetId")).isEqualTo(planet);
    }
}