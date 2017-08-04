package be.deschutter.irc.scimitar;

import be.deschutter.scimitar.events.Event;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScimitarImpl extends ListenerAdapter<PircBotX> implements Scimitar {


    @Autowired
    private EventFactory eventFactory;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Override
    public void onGenericMessage(GenericMessageEvent genericMessageEvent) {

    }

    @Override
    public void onMessage(MessageEvent messageEvent) throws Exception {
        Event event = eventFactory.makeEvent(messageEvent);

        RestTemplate restTemplate = new RestTemplate();
        Event reply = restTemplate.postForObject("http://localhost:8080/scimitar", event, Event.class);
        reply(reply,messageEvent.getBot());

    }

    private void reply(Event event, PircBotX bot) {
        switch (event.getReturnType()) {
        case CHANNEL_MSG:
            bot.sendIRC().message(event.getChannel(), event.getReply());
            break;
        case NOTICE:
            bot.sendIRC().notice(event.getCurrentUsername(), event.getReply());
            break;
        case PRIVATE_MSG:
            bot.sendIRC().message(event.getCurrentUsername(), event.getReply());
            break;
        default:
            throw new IllegalStateException("Didn't expect this");
        }
    }

    @Override
    public void onConnect(ConnectEvent<PircBotX> event) throws Exception {
        super.onConnect(event);
        event.getBot().sendIRC().message("p@cservice.netgamers.org",
            "login " + username + " " + password);
        event.getBot().sendIRC().mode(event.getBot().getNick(), "+ix");
    }

}
