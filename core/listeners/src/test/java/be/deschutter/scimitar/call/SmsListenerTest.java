package be.deschutter.scimitar.call;

import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SmsListenerTest {
    @InjectMocks
    private SmsListener smsListener;
    @Mock
    private ScimitarUserEao scimitarUserEao;
    @Mock
    private ClickatellSender clickatellSender;

    @Before
    public void setUp() throws Exception {
        final ScimitarUser berten = new ScimitarUser();
        berten.setUsername("Berten");
        berten.setPhoneNumber("bertennumber");
        when(scimitarUserEao.findByUsernameIgnoreCase("Berten"))
            .thenReturn(berten);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(smsListener.getCommand()).isEqualTo("sms");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(smsListener.getPattern()).isEqualTo("username text");
    }

    @Test
    public void getResult_noUsername() throws Exception {
        assertThat(smsListener.getResult("Berten")).isEqualTo(
            "Error: use following pattern for command sms: username text");
        verifyZeroInteractions(clickatellSender);
    }

    @Test
    public void getResult_knownUsername() throws Exception {
        final ScimitarUser t = new ScimitarUser();
        t.setPhoneNumber("phone");
        t.setUsername("knownUsername");
        when(scimitarUserEao.findByUsernameIgnoreCase("knownusername"))
            .thenReturn(t);
        assertThat(smsListener
            .getResult("Berten", "knownusername", "dit", "is", "een", "tekst"))
            .isEqualTo(
                "Message dit is een tekst - Berten/bertennumber sent to knownUsername");
        verify(clickatellSender)
            .sendMessage("phone", "dit is een tekst - Berten/bertennumber");
    }

    @Test
    public void getResult_UnknownUsername() throws Exception {
        when(scimitarUserEao.findByUsernameIgnoreCase("unknownusername"))
            .thenReturn(null);
        assertThat(smsListener.getResult("Berten", "unknownusername", "text"))
            .isEqualTo("User unknownusername does not exist");
        verifyZeroInteractions(clickatellSender);
    }

}