package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.HelpListener;
import be.deschutter.scimitar.Listener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HelpListenerTest {
    @InjectMocks
    private HelpListener helpListener;

    @Mock
    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        final Map<String, Listener> listeners = new HashMap<>();
        listeners.put("two", listener("two", "3 4"));
        listeners.put("one", listener("one", "1 2"));

        when(applicationContext.getBeansOfType(Listener.class)).thenReturn(
            listeners);

        helpListener.loadListeners();
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(helpListener.getCommand()).isEqualTo("help");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(helpListener.getPattern()).isEqualTo("[command]");
    }

    @Test
    public void getResult() throws Exception {
        assertThat(helpListener.getResult("user")).isEqualTo("List of commands: one, two");
    }

    @Test
    public void getResult_detail() throws Exception {
        assertThat(helpListener.getResult("user","one")).isEqualTo("Pattern for command one: 1 2");
    }

    @Test
    public void getResult_detail_doesnotExist() throws Exception {
        assertThat(helpListener.getResult("user","three")).isEqualTo("Command three was not found");
    }

    @Test
    public void getResult_multipleparamters() throws Exception {
        assertThat(helpListener.getResult("user","three","four","five")).isEqualTo("Error: use following pattern for command help: [command]");
    }

    private Listener listener(final String command, final String pattern) {
        return new Listener() {
            @Override
            public String getCommand() {
                return command;
            }

            @Override
            public String getPattern() {
                return pattern;
            }

            @Override
            public String getResult(String username, final String... parameters) {
                return "";
            }
        };
    }

}