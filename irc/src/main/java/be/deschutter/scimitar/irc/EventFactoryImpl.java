package be.deschutter.scimitar.irc;

import be.deschutter.scimitar.events.Event;
import be.deschutter.scimitar.events.ReturnType;
import org.pircbotx.hooks.events.MessageEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EventFactoryImpl implements EventFactory {

    @Override
    public Event makeEvent(MessageEvent messageEvent) {
        Event event = new Event();

        event.setChannel(messageEvent.getChannel().getName());
        event.setCurrentUsername(messageEvent.getUser().getNick());

        Pattern pattern = Pattern.compile("(.*)\\.users\\.netgamers\\.org");
        Pattern commandPatern = Pattern.compile("(^[\\.!\\-])([a-zA-Z]*)\\s*(.*)");


        Matcher m = pattern.matcher(messageEvent.getUser().getHostmask());
        if (m.matches()) {
            event.setLoggedInUsername(m.replaceAll("$1"));

            Matcher matcher = commandPatern.matcher(messageEvent.getMessage());
            if (matcher.matches()) {
                event.setReturnType(ReturnType.findByPrefix(matcher.group(1)));
                event.setCommand(matcher.group(2));
                String parameters = matcher.group(3);
                if(!StringUtils.isEmpty(parameters)) {
                    event.setParameters(parameters.split(":|\\s+"));
                }
            }
        }

        return event;
    }
}
