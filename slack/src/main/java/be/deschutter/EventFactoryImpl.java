package be.deschutter;

import be.deschutter.scimitar.events.Event;
import be.deschutter.scimitar.events.ReturnType;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EventFactoryImpl implements EventFactory {

    @Override
    public Event makeEvent(me.ramswaroop.jbot.core.slack.models.Event messageEvent) {
        Event event = new Event();

        event.setChannel(messageEvent.getChannelId());
        event.setCurrentUsername(messageEvent.getUserId());
        event.setLoggedInUsername(messageEvent.getUserId());

        Pattern commandPatern = Pattern.compile("(^[\\.!\\-])([a-zA-Z]*)\\s*(.*)");
        Matcher matcher = commandPatern.matcher(messageEvent.getText());
        if (matcher.matches()) {
            event.setReturnType(ReturnType.findByPrefix(matcher.group(1)));
            event.setCommand(matcher.group(2));
            event.setParameters(matcher.group(3));
        }
        return event;
    }
}
