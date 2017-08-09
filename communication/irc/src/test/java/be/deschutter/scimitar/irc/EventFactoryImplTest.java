package be.deschutter.scimitar.irc;

import be.deschutter.scimitar.events.Event;
import be.deschutter.scimitar.events.ReturnType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.managers.ListenerManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventFactoryImplTest {
    @InjectMocks
    EventFactoryImpl e = new EventFactoryImpl();
    @Mock
    private Configuration<PircBotX> configuration;
    @Mock
    private ListenerManager<PircBotX> listenermanager;
    @Mock
    private PircBotX bot;
    @Mock
    private Channel chan;
    @Mock
    private User user;

    @Test
    public void makeEvent() throws Exception {

        when(bot.getConfiguration()).thenReturn(configuration);
        when(listenermanager.incrementCurrentId()).thenReturn(123L);
        when(configuration.getListenerManager()).thenReturn(listenermanager);
        when(chan.getName()).thenReturn("cname");
        when(user.getNick()).thenReturn("nick");
        when(user.getHostmask()).thenReturn("LoggedInNick.users.netgamers.org");


        final Event event = e
            .makeEvent(new MessageEvent<>(bot, chan, user, ".lookup x y:z"));

        assertThat(event.getChannel()).isEqualTo("cname");
        assertThat(event.getCurrentUsername()).isEqualTo("nick");
        assertThat(event.getLoggedInUsername()).isEqualTo("LoggedInNick");
        assertThat(event.getCommand()).isEqualTo("lookup");
        assertThat(event.getReturnType()).isEqualTo(ReturnType.PRIVATE_MSG);
        assertThat(event.getParameters()).contains("x", "y", "z");

    }

}