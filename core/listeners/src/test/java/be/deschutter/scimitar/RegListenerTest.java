package be.deschutter.scimitar;

import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegListenerTest {
    @InjectMocks
    private RegListener regListener;
    @Mock
    private ScimitarUserEao scimitarUserEao;
    private ScimitarUser scimitarUser;

    @Before
    public void setUp() throws Exception {
        scimitarUser = new ScimitarUser();
        scimitarUser.setUsername("berten");
        scimitarUser.setSlackUsername(null);

        when(scimitarUserEao.findByUsernameIgnoreCase("berten"))
            .thenReturn(scimitarUser);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(regListener.getCommand()).isEqualTo("reg");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(regListener.getPattern()).isEqualTo("pnick");
    }

    @Test
    public void getResult() throws Exception {
        assertThat(regListener.getResult("slackuser", "berten"))
            .isEqualTo("pnick successfully added to slack profile");
        final ArgumentCaptor<ScimitarUser> captor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(scimitarUserEao).saveAndFlush(captor.capture());

        assertThat(captor.getValue().getSlackUsername()).isEqualTo("slackuser");
    }

    @Test
    public void getResult_AlreadyHasSlackName() throws Exception {
        scimitarUser.setSlackUsername("slackusernamealreadyadded");
        assertThat(regListener.getResult("slackuser", "berten"))
            .isEqualTo("A slack nick for this user was already added. Are you trying to hack me?");
        verify(scimitarUserEao, never()).saveAndFlush(any(ScimitarUser.class));
    }

    @Test
    public void getResultNoParamters() throws Exception {
        assertThat(regListener.getResult("slackuser")).isEqualTo("Error: use following pattern for command reg: pnick");
    }
}