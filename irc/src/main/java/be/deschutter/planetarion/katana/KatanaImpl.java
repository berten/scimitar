package be.deschutter.planetarion.katana;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KatanaImpl extends ListenerAdapter implements Katana {

    @Autowired
    private List<KatanaListener> listeners;

    @Autowired
    private EventFactory eventFactory;


    @Override
    public void onGenericMessage(GenericMessageEvent genericMessageEvent) {


    }

    @Override
    public void onMessage(MessageEvent messageEvent) throws Exception {
        KatanaEvent event = eventFactory.makeEvent(messageEvent);
        for (KatanaListener listener : listeners) {
            if (listener.canHandle(event)) {
                String result = listener.handle(event);
                if (result != null)
                    reply(event, result, messageEvent.getBot());
            }
        }
    }

    private void reply(KatanaEvent event, String result, PircBotX bot) {
        switch (event.getReturnType()) {
            case CHANNEL_MSG:
                bot.sendIRC().message(event.getChannel(), result);
                break;
            case NOTICE:
                bot.sendIRC().notice(event.getIrcUserName(), result);
                break;
            case PRIVATE_MSG:
                bot.sendIRC().message(event.getIrcUserName(), result);
                break;
            default:
                throw new IllegalStateException("Didn't expect this");
        }
    }

    @Override
    public void onConnect(ConnectEvent event) throws Exception {
        super.onConnect(event);
        event.getBot().sendIRC().mode(event.getBot().getNick(), "+ix");
        event.getBot().sendIRC().message("p@cservice.netgamers.org", "login username password");
    }

}
