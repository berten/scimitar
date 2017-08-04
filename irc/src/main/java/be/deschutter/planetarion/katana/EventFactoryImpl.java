package be.deschutter.planetarion.katana;

import org.pircbotx.hooks.events.MessageEvent;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class EventFactoryImpl implements EventFactory {

    @Override
    public KatanaEvent makeEvent(MessageEvent messageEvent) {
        KatanaEvent katanaEvent = new KatanaEvent();

        katanaEvent.setChannel(messageEvent.getChannel().getName());
        katanaEvent.setIrcUserName(messageEvent.getUser().getNick());

        Pattern pattern = Pattern.compile("(.*)\\.users\\.netgamers\\.org");
        Pattern commandPatern = Pattern.compile("(^[\\.!\\-])([a-zA-Z]*)\\s*(.*)");


        Matcher m = pattern.matcher(messageEvent.getUser().getHostmask());
        if (m.matches()) {
            katanaEvent.setLoggedInUsername(m.replaceAll("$1"));

            Matcher matcher = commandPatern.matcher(messageEvent.getMessage());
            if (matcher.matches()) {
                katanaEvent.setReturnType(ReturnTypeEnum.findByPrefix(matcher.group(1)));
                katanaEvent.setCommand(matcher.group(2));
                katanaEvent.setParameters(matcher.group(3));
            }
        }

        return katanaEvent;
    }
}
