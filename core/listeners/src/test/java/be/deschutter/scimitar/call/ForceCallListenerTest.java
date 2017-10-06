package be.deschutter.scimitar.call;

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

@RunWith(MockitoJUnitRunner.class)
public class ForceCallListenerTest {

    @InjectMocks
    private ForceCallListener forceCallListener;

    @Mock
    private TaskExecutor taskExecutor;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils
            .setField(forceCallListener, "twilioAcountSid", "sid");
        ReflectionTestUtils
            .setField(forceCallListener, "twilioAuthToken", "token");
        ReflectionTestUtils
            .setField(forceCallListener, "twilioPhoneNumber", "phoneNumber");
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(forceCallListener.getCommand()).isEqualTo("forcecall");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(forceCallListener.getPattern()).isEqualTo("number");
    }

    @Test
    public void getResult_noNumber() throws Exception {
        assertThat(forceCallListener.getResult()).isEqualTo(
            "Error: use following pattern for command forcecall: number");
        verifyZeroInteractions(taskExecutor);
    }

    @Test
    public void getResult_knownUsername() throws Exception {

        assertThat(forceCallListener.getResult("phone"))
            .isEqualTo("Call queued to phone");
        verify(taskExecutor).execute(any(Runnable.class));
    }

}