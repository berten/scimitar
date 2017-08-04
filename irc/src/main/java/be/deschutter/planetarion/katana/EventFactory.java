package be.deschutter.planetarion.katana;

import org.pircbotx.hooks.events.MessageEvent;

public interface EventFactory {
    KatanaEvent makeEvent(MessageEvent messageEvent);
}
