package be.deschutter.scimitar.slack;

import be.deschutter.scimitar.events.ReturnType;
import me.ramswaroop.jbot.core.slack.models.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EventFactoryImplTest {
    @InjectMocks
    private EventFactoryImpl eventFactory;

    @Test
    public void makeEvent() throws Exception {
        final Event e = new Event();
        e.setChannelId("chanId");
        e.setUserId("userId");
        e.setText("!lookup x y:z");
        final be.deschutter.scimitar.events.Event result = eventFactory
            .makeEvent(e);

        assertThat(result.getChannel()).isEqualTo("chanId");
        assertThat(result.getCurrentUsername()).isEqualTo("userId");
        assertThat(result.getLoggedInUsername()).isEqualTo("userId");
        assertThat(result.getCommand()).isEqualTo("lookup");
        assertThat(result.getReturnType()).isEqualTo(ReturnType.CHANNEL_MSG);
        assertThat(result.getParameters()).contains("x","y","z");
    }

}