package be.deschutter.scimitar.intel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceImplTest {
    @InjectMocks
    private PlayerServiceImpl playerService;
    @Mock
    private PlayerEao playerEao;

    @Test
    public void findByNickname() throws Exception {
        final Player player = new Player();
        when(playerEao.findFirstByNicksContains("nickname")).thenReturn(player);
        assertThat(playerService.findBy("nickname")).isSameAs(player);
    }

    @Test(expected = PlayerNotFoundException.class)
    public void findByNickname_NotFound() throws Exception {
        try {
            playerService.findBy("unknownnickname");
        } catch (PlayerNotFoundException e) {
            assertThat(e.getNickname()).isEqualTo("unknownnickname");
            throw e;
        }
        fail("Should have thrown an exception");

    }
}