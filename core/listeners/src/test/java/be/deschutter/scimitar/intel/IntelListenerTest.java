package be.deschutter.scimitar.intel;

import be.deschutter.scimitar.alliance.Alliance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IntelListenerTest {
    @InjectMocks
    private IntelListener intelListener;
    @Mock
    private IntelService intelService;
    private Intel intel;

    @Before
    public void setUp() throws Exception {
        intel = new Intel();
        intel.setAlliance(new Alliance("allianceName"));
        final Player player = new Player();
        player.addNick("nick");
        player.addNick("nick2");
        intel.setPlayer(player);
        when(intelService.findBy(1, 2, 3)).thenReturn(intel);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(intelListener.getCommand()).isEqualTo("intel");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(intelListener.getPattern()).isEqualTo("x y z nick=<nick> alliance=<alliance> nickadd=<nick to add to player>");
    }

    @Test
    public void getResult() throws Exception {

        assertThat(intelListener.getResult("1", "2", "3")).isEqualTo(
            "Intel for 1:2:3 alliance=allianceName nick=nick, nick2");
    }

    @Test
    public void getResult_noAlliance() throws Exception {
        intel.setAlliance(null);
        assertThat(intelListener.getResult("1", "2", "3"))
            .isEqualTo("Intel for 1:2:3 nick=nick, nick2");
    }

    @Test
    public void getResult_noNick() throws Exception {
        intel.setPlayer(null);
        assertThat(intelListener.getResult("1", "2", "3"))
            .isEqualTo("Intel for 1:2:3 alliance=allianceName");
    }

    @Test
    public void getResult_AddNick() throws Exception {
        assertThat(intelListener.getResult("1", "2", "3", "nick=berten"))
            .isEqualTo("Intel for 1:2:3 alliance=allianceName nick=nick, nick2");
        verify(intelService).changeNick(1,2,3,"berten");
    }

    @Test
    public void getResult_AddAlliance() throws Exception {
        assertThat(intelListener.getResult("1", "2", "3", "alliance=alliance"))
            .isEqualTo("Intel for 1:2:3 alliance=allianceName nick=nick, nick2");
        verify(intelService).changeAlliance(1,2,3,"alliance");
    }

    @Test
    public void getResult_AddAlliance_changeNick() throws Exception {
        assertThat(intelListener.getResult("1", "2", "3", "alliance=alliance", "nick=berten"))
            .isEqualTo("Intel for 1:2:3 alliance=allianceName nick=nick, nick2");
        verify(intelService).changeAlliance(1,2,3,"alliance");
        verify(intelService).changeNick(1,2,3,"berten");
    }

    @Test
    public void getResult_AddAlliance_addNick() throws Exception {
        assertThat(intelListener.getResult("1", "2", "3", "alliance=alliance", "addnick=berten"))
            .isEqualTo("Intel for 1:2:3 alliance=allianceName nick=nick, nick2");
        verify(intelService).changeAlliance(1,2,3,"alliance");
        verify(intelService).addNick(1,2,3,"berten");
    }
}