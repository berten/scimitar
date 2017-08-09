package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.galaxy.Galaxy;
import be.deschutter.scimitar.galaxy.GalaxyEao;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LookupListenerTest {
    @InjectMocks
    private LookupListener lookupListener;

    @Mock
    private GalaxyEao galaxyEao;
    @Mock
    private PlanetEao planetEao;
    private Planet planet;
    private Galaxy galaxy;

    @Before
    public void setUp() throws Exception {
        planet = new Planet();
        planet.setX(3);
        planet.setY(6);
        planet.setZ(2);
        planet.setPlanetName("pname");
        planet.setRulerName("rname");
        planet.setScore(1000);
        planet.setValue(100);
        planet.setSize(40);
        planet.setXp(10);
        planet.setRace("Ter");
        planet.setScoreRank(1);
        planet.setValueRank(2);
        planet.setXpRank(3);
        planet.setSizeRank(4);
        when(planetEao.findFirstByXAndYAndZOrderByTickDesc(3, 6, 2))
            .thenReturn(planet);

        galaxy = new Galaxy();
        galaxy.setX(3);
        galaxy.setY(6);
        galaxy.setGalaxyName("galaxyname");
        galaxy.setScore(1000);
        galaxy.setValue(100);
        galaxy.setSize(40);
        galaxy.setXp(10);
        galaxy.setPlanets(8);
        galaxy.setScoreRank(1);
        galaxy.setValueRank(2);
        galaxy.setXpRank(3);
        galaxy.setSizeRank(4);
        when(galaxyEao.findFirstByXAndYOrderByTickDesc(3, 6))
            .thenReturn(galaxy);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(lookupListener.getCommand()).isEqualTo("lookup");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(lookupListener.getPattern()).isEqualTo("x y [z]");
    }

    @Test
    public void getResult_galaxy() throws Exception {
        assertThat(lookupListener.getResult("3", "6")).isEqualTo(
            "3:6 'galaxyname' (8) Score: 1000 (1) Value: 100 (2) Size: 40 (4) XP: 10 (3)");
    }

    @Test
    public void getResult_galaxy_DoesNotExist() throws Exception {
        assertThat(lookupListener.getResult("99", "99"))
            .isEqualTo("Galaxy 99:99 does not exist");
    }

    @Test
    public void getResult_planet_DoesNotExist() throws Exception {
        assertThat(lookupListener.getResult("99","99","99")).isEqualTo("Planet 99:99:99 does not exist");
    }

    @Test
    public void getResult_planet() throws Exception {
        assertThat(lookupListener.getResult("3","6","2")).isEqualTo("3:6:2 (Ter) 'rname of pname' Score: 1000 (1) Value: 100 (2) Size: 40 (4) XP: 10 (3)");
    }

    @Test
    public void getResu() throws Exception {
    }

    @Test
    public void getResult_ParametersGiveError() throws Exception {
        assertThat(lookupListener.getResult()).isEqualTo("Error: use following pattern for command lookup: x y [z]");
        assertThat(lookupListener.getResult("1")).isEqualTo("Error: use following pattern for command lookup: x y [z]");
        assertThat(lookupListener.getResult("1","a")).isEqualTo("Error: use following pattern for command lookup: x y [z]");
    }
}