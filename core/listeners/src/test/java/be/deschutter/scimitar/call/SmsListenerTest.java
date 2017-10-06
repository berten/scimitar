package be.deschutter.scimitar.call;

import be.deschutter.scimitar.security.SecurityHelper;
import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.UserService;
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
    private SecurityHelper securityHelper;
    @Mock
    private UserService userService;
    @Mock
    private ClickatellSender clickatellSender;

    @Before
    public void setUp() throws Exception {
        final ScimitarUser berten = new ScimitarUser();
        berten.setUsername("Berten");
        berten.setPhoneNumber("bertennumber");
        when(securityHelper.getLoggedInUser())
            .thenReturn(berten);
        when(userService.findBy("berten")).thenReturn(berten);
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
        assertThat(smsListener.getResult()).isEqualTo(
            "Error: use following pattern for command sms: username text");
        verifyZeroInteractions(clickatellSender);
    }

    @Test
    public void getResult_knownUsername() throws Exception {
        final ScimitarUser t = new ScimitarUser();
        t.setPhoneNumber("phone");
        t.setUsername("knownUsername");
        when(userService.findBy("knownusername"))
            .thenReturn(t);
        assertThat(smsListener
            .getResult("knownusername", "dit", "is", "een", "tekst"))
            .isEqualTo(
                "Message dit is een tekst - Berten/bertennumber sent to knownUsername");
        verify(clickatellSender)
            .sendMessage("phone", "dit is een tekst - Berten/bertennumber");
    }



}