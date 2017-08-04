package be.deschutter;

import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.Controller;
import me.ramswaroop.jbot.core.slack.EventType;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.regex.Matcher;

/**
 * A Slack Bot sample. You can create multiple bots by just
 * extending {@link Bot} class like this one.
 *
 * @author ramswaroop
 * @version 1.0.0, 05/06/2016
 */
@Component
public class SlackBot extends Bot {

    private static final Logger logger = LoggerFactory.getLogger(SlackBot.class);

    @Autowired
    private EventFactory eventFactory;
    /**
     * Slack token from application.properties file. You can get your slack token
     * next <a href="https://my.slack.com/services/new/bot">creating a new bot</a>.
     */
    @Value("${slackBotToken}")
    private String slackToken;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    /**
     * Invoked when the bot receives a direct mention (@botname: message)
     * or a direct message. NOTE: These two event types are added by jbot
     * to make your task easier, Slack doesn't have any direct way to
     * determine these type of events.
     *
     * @param session
     * @param event
     */
    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE}, pattern = "(^[\\.!\\-])([a-zA-Z]*)\\s*(.*)")
    public void onReceiveDM(WebSocketSession session, Event event, Matcher matcher) {

        RestTemplate restTemplate = new RestTemplate();
        be.deschutter.scimitar.events.Event reply = restTemplate.postForObject("http://localhost:8080/scimitar", eventFactory.makeEvent(event), be.deschutter.scimitar.events.Event.class);
        reply(session, reply);


    }

    private void reply(WebSocketSession session, be.deschutter.scimitar.events.Event reply) {
        try {

            Message message = new Message(reply.getReply());
            message.setChannel(reply.getChannel());
            message.setUser(reply.getCurrentUsername());
            message.setUsername(reply.getCurrentUsername());
            message.setType(EventType.MESSAGE.name().toLowerCase());

            session.sendMessage(new TextMessage(message.toJSONString()));
            if (logger.isDebugEnabled()) {  // For debugging purpose only
                logger.debug("Reply (Message): {}", message.toJSONString());
            }
        } catch (IOException e) {
            logger.error("Error sending event: {}. Exception: {}", reply.getReply(), e.getMessage());
        }
    }

    /**
     * Invoked when bot receives an event of type message with text satisfying
     * the pattern {@code ([a-z ]{2})(\d+)([a-z ]{2})}. For example,
     * messages like "ab12xy" or "ab2bc" etc will invoke this method.
     *
     * @param session
     * @param event
     */
    @Controller(events = EventType.MESSAGE, pattern = "(^[\\.!\\-])([a-zA-Z]*)\\s*(.*)")
    public void onReceiveMessage(WebSocketSession session, Event event, Matcher matcher) {
        RestTemplate restTemplate = new RestTemplate();
        be.deschutter.scimitar.events.Event reply = restTemplate.postForObject("http://localhost:8080/scimitar", eventFactory.makeEvent(event), be.deschutter.scimitar.events.Event.class);
        reply(session, reply);
    }


}