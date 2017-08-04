package be.deschutter.planetarion.katana;

import org.junit.Before;
import org.junit.Test;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScimitarEventTest {


    private MessageEvent messageEvent;

    @Before
    public void setUp() throws Exception {

        messageEvent = mock(MessageEvent.class);

        User user = mock(User.class);
        when(user.getHostmask()).thenReturn("Test.users.netgamers.org");
        when(user.getNick()).thenReturn("ircNick");
        Channel channel = mock(Channel.class);
        when(channel.getName()).thenReturn("#channelName");
        when(messageEvent.getChannel()).thenReturn(channel);
        when(messageEvent.getUser()).thenReturn(user);
    }

//    @Test
//    public void hasIrcNick() throws Exception {
//
//        when(messageEvent.getMessage()).thenReturn("-test");
//        KatanaEvent e = new KatanaEvent(messageEvent);
//
//        assertThat(e.getIrcUserName()).isEqualTo("ircNick");
//    }
//
//    @Test
//    public void hasChannel() throws Exception {
//        when(messageEvent.getMessage()).thenReturn("-test");
//        KatanaEvent e = new KatanaEvent(messageEvent);
//
//        assertThat(e.getChannel()).isEqualTo("#channelName");
//
//    }
//
//    @Test
//    public void findsCommand() throws Exception {
//        when(messageEvent.getMessage()).thenReturn("-test");
//        KatanaEvent e = new KatanaEvent(messageEvent);
//
//        assertThat(e.getCommand()).isEqualTo("test");
//    }
//
//    @Test
//    public void findsReturnType_notice() throws Exception {
//        when(messageEvent.getMessage()).thenReturn("-test");
//        KatanaEvent e = new KatanaEvent(messageEvent);
//
//        assertThat(e.getReturnType()).isEqualTo(ReturnType.NOTICE);
//        assertThat(e.getCommand()).isEqualTo("test");
//        assertThat(e.getParameters()).isEqualTo("");
//    }
//
//    @Test
//    public void findsReturnType_pm() throws Exception {
//        when(messageEvent.getMessage()).thenReturn(".test");
//        KatanaEvent e = new KatanaEvent(messageEvent);
//
//        assertThat(e.getReturnType()).isEqualTo(ReturnType.PRIVATE_MSG);
//        assertThat(e.getCommand()).isEqualTo("test");
//        assertThat(e.getParameters()).isEqualTo("");
//    }
//
//    @Test
//    public void findsReturnType_channel() throws Exception {
//        when(messageEvent.getMessage()).thenReturn("!test");
//        KatanaEvent e = new KatanaEvent(messageEvent);
//
//        assertThat(e.getReturnType()).isEqualTo(ReturnType.CHANNEL_MSG);
//        assertThat(e.getCommand()).isEqualTo("test");
//        assertThat(e.getParameters()).isEqualTo("");
//    }
//
//    @Test
//    public void findsLoggedInUsername() throws Exception {
//        when(messageEvent.getMessage()).thenReturn("-test");
//        KatanaEvent e = new KatanaEvent(messageEvent);
//
//        assertThat(e.getLoggedInUsername()).isEqualTo("Test");
//    }
//
//    @Test
//    public void findsParameters() throws Exception {
//        when(messageEvent.getMessage()).thenReturn("-test t2 t3");
//
//        KatanaEvent e = new KatanaEvent(messageEvent);
//
//        assertThat(e.getParameters()).isEqualTo("t2 t3");
//    }
}