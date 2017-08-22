package be.deschutter.scimitar.call;

import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CallListenerTest {
    @InjectMocks
    private CallListener callListener;

    @Mock
    private ScimitarUserEao scimitarUserEao;
    @Mock
    private TaskExecutor taskExecutor;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(callListener,"twilioAcountSid", "sid");
        ReflectionTestUtils.setField(callListener,"twilioAuthToken", "token");
        ReflectionTestUtils.setField(callListener,"twilioPhoneNumber", "phoneNumber");
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(callListener.getCommand()).isEqualTo("call");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(callListener.getPattern()).isEqualTo("username");
    }

    @Test
    public void getResult_noUsername() throws Exception {
        assertThat(callListener.getResult("Berten")).isEqualTo("Error: use following pattern for command call: username");
        verifyZeroInteractions(taskExecutor);
    }

    @Test
    public void getResult_knownUsername() throws Exception {
        final ScimitarUser t = new ScimitarUser();
        t.setPhoneNumber("phone");
        t.setUsername("knownUsername");
        when(scimitarUserEao.findByUsernameIgnoreCase("knownusername")).thenReturn(
            t);
        assertThat(callListener.getResult("Berten","knownusername")).isEqualTo("Call queued to knownUsername");
        verify(taskExecutor).execute(any(Runnable.class));
    }

    @Test
    public void getResult_UnknownUsername() throws Exception {
        when(scimitarUserEao.findByUsernameIgnoreCase("unknownusername")).thenReturn(null);
        assertThat(callListener.getResult("Berten","unknownusername")).isEqualTo("User unknownusername does not exist");
        verifyZeroInteractions(taskExecutor);
    }

}