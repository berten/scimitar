package be.deschutter.scimitar.intel;

import be.deschutter.scimitar.alliance.Alliance;
import be.deschutter.scimitar.alliance.AllianceService;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IntelServiceImplTest {
    @InjectMocks
    private IntelServiceImpl intelService;
    @Mock
    private PlanetService planetService;
    @Mock
    private IntelEao intelEao;
    @Mock
    private AllianceService allianceService;
    @Mock
    private PlayerService playerService;

    @Before
    public void setUp() throws Exception {
        when(intelEao.save(any(Intel.class))).thenAnswer(
            (Answer<Intel>) invocationOnMock -> (Intel) invocationOnMock
                .getArguments()[0]);
    }

    @Test
    public void findBy() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        final Intel intel = new Intel();
        when(intelEao.findByPlanetId("planetId")).thenReturn(intel);
        assertThat(intelService.findBy(1, 2, 3)).isSameAs(intel);

    }

    @Test(expected = IntelNotFoundException.class)
    public void findBy_not_found() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        try {
            intelService.findBy(1, 2, 3);
        } catch (IntelNotFoundException e) {
            assertThat(e.getX()).isEqualTo(1);
            assertThat(e.getY()).isEqualTo(2);
            assertThat(e.getZ()).isEqualTo(3);
            throw e;
        }
        fail("Should have thrown an exception");
    }

    @Test
    public void changeAlliance() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        final Alliance alliance = new Alliance();
        when(allianceService.findBy("allianceName")).thenReturn(alliance);
        final Intel intel = new Intel();
        when(intelEao.findByPlanetId("planetId")).thenReturn(intel);

        assertThat(intelService.changeAlliance(1, 2, 3, "allianceName"))
            .isSameAs(intel);

    }

    @Test
    public void changeAlliance_noIntelFound() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        final Alliance alliance = new Alliance();
        when(allianceService.findBy("allianceName")).thenReturn(alliance);

        assertThat(
            intelService.changeAlliance(1, 2, 3, "allianceName").getAlliance())
            .isSameAs(alliance);

    }

    @Test
    public void changeNick() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        final Intel intel = new Intel();
        when(intelEao.findByPlanetId("planetId")).thenReturn(intel);
        final Player p = new Player();
        when(playerService.findBy("newNick")).thenReturn(p);
        assertThat(intelService.changeNick(1, 2, 3, "newNick")).isSameAs(intel);

    }

    @Test
    public void changeNick_nickNotFound() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        final Intel intel = new Intel();
        when(intelEao.findByPlanetId("planetId")).thenReturn(intel);
        final Player p = new Player("newNick");
        when(playerService.createPlayer("newNick")).thenReturn(p);
        when(playerService.findBy(any(String.class)))
            .thenThrow(new PlayerNotFoundException("newNick"));
        assertThat(
            intelService.changeNick(1, 2, 3, "newNick").getPlayer().getNicks())
            .contains("newNick");

    }

    @Test
    public void addNick() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        final Intel intel = new Intel();
        final Player p = new Player("existingNick");
        intel.setPlayer(p);
        when(intelEao.findByPlanetId("planetId")).thenReturn(intel);

        assertThat(
            intelService.addNick(1, 2, 3, "newNick").getPlayer().getNicks())
            .contains("newNick", "existingNick");
    }

    @Test
    public void addNick_HasNoPlayerYet_PlayerExists() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        final Intel intel = new Intel();
        when(intelEao.findByPlanetId("planetId")).thenReturn(intel);
        final Player p = new Player();
        p.addNick("newNick");
        when(playerService.findBy("newNick")).thenReturn(p);

        assertThat(
            intelService.addNick(1, 2, 3, "newNick").getPlayer().getNicks())
            .contains("newNick");
    }

    @Test
    public void addNick_HasNoPlayerYet_PlayerDoesNotExist() throws Exception {
        when(planetService.findBy(1, 2, 3)).thenReturn(new Planet("planetId"));
        final Intel intel = new Intel();
        when(intelEao.findByPlanetId("planetId")).thenReturn(intel);

        final Player p = new Player("newNick");
        when(playerService.createPlayer("newNick")).thenReturn(p);
        when(playerService.findBy(any(String.class)))
            .thenThrow(new PlayerNotFoundException("newNick"));

        assertThat(
            intelService.addNick(1, 2, 3, "newNick").getPlayer().getNicks())
            .contains("newNick");
    }
}