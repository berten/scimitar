package be.deschutter.scimitar;

import be.deschutter.scimitar.events.Event;
import org.pircbotx.hooks.events.MessageEvent;

public interface EventFactory {
    Event makeEvent(MessageEvent messageEvent);
}
