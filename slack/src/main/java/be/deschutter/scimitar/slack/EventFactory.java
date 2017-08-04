package be.deschutter.scimitar.slack;

import be.deschutter.scimitar.events.Event;

public interface EventFactory {
    Event makeEvent(me.ramswaroop.jbot.core.slack.models.Event messageEvent);
}
