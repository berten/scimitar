package be.deschutter.scimitar.intel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerListenerTest {
    @InjectMocks
    private PlayerListener playerListener;

    @Mock
    private PlayerService playerService;
    private Player player;

    @Before
    public void setUp() throws Exception {
        player = new Player("nick");
        player.addNick("nick2");
        player.setEmail("email");
        player.setPhoneNumber("+phonenumber");
        player.getPlanetHistories().add(createPlanetHistory(4L, 44L));
        player.getPlanetHistories().add(createPlanetHistory(114L, 40L));
        player.getPlanetHistories().add(createPlanetHistory(8L, 37L));
        player.getPlanetHistories().add(createPlanetHistory(39L, 56L));
        when(playerService.findBy("nick")).thenReturn(player);
    }

    private PlanetHistory createPlanetHistory(long rank, long round) {
        final PlanetHistory p1 = new PlanetHistory();
        p1.setRank(rank);
        p1.setRound(round);
        return p1;
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(playerListener.getCommand()).isEqualTo("player");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(playerListener.getPattern()).isEqualTo("show nickname|phone|email | add nickname | addnick nickname newnickname");
    }

    @Test
    public void getResult() throws Exception {
        assertThat(playerListener.getResult("show","nick")).isEqualTo("Known nicks: nick, nick2 phone: +phonenumber email: email Best Ranks: 4 (44) | 8 (37) | 39 (56) | 114 (40)");
    }

    @Test
    public void getResult_noPhone() throws Exception {
        player.setPhoneNumber(null);
        assertThat(playerListener.getResult("show","nick")).isEqualTo("Known nicks: nick, nick2 phone: none email: email Best Ranks: 4 (44) | 8 (37) | 39 (56) | 114 (40)");
    }

    @Test
    public void getResult_noEmail() throws Exception {
        player.setEmail(null);
        assertThat(playerListener.getResult("show","nick")).isEqualTo("Known nicks: nick, nick2 phone: +phonenumber email: none Best Ranks: 4 (44) | 8 (37) | 39 (56) | 114 (40)");
    }

    @Test
    public void getResult_noPlanetHistory() throws Exception {
        player.setPlanetHistories(new ArrayList<>());
        assertThat(playerListener.getResult("show","nick")).isEqualTo("Known nicks: nick, nick2 phone: +phonenumber email: email Best Ranks: no known ranks");
    }

}