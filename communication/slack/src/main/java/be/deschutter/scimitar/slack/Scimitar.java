package be.deschutter.scimitar.slack;

import be.deschutter.scimitar.ScimitarNotification;
import be.deschutter.scimitar.channel.ChannelConfiguration;
import be.deschutter.scimitar.channel.ChannelConfigurationEao;
import be.deschutter.scimitar.channel.ChannelType;
import be.deschutter.scimitar.events.ReturnType;
import be.deschutter.scimitar.user.ScimitarUserEao;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.Controller;
import me.ramswaroop.jbot.core.slack.EventType;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;

@Component
public class Scimitar extends Bot {

    private static final Logger logger = LoggerFactory
        .getLogger(Scimitar.class);

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private ScimitarUserEao scimitarUserEao;
    /**
     * Slack token from application.properties file. You can get your slack
     * token next <a href="https://my.slack.com/services/new/bot">creating a new
     * bot</a>.
     */
    @Value("${slackBotToken}")
    private String slackToken;
    private WebSocketSession session;
    @Autowired
    private ChannelConfigurationEao channelConfigurationEao;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    /**
     * Invoked when the bot receives a direct mention (@botname: message) or a
     * direct message. NOTE: These two event types are added by jbot to make
     * your task easier, Slack doesn't have any direct way to determine these
     * type of events.
     * @param session
     * @param event
     */
    @Controller(events = { EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE },
        pattern = "(^[\\.!\\-])([a-zA-Z]*)\\s*(.*)")
    public void onReceiveDM(WebSocketSession session, Event event,
        Matcher matcher) {

        sendEvent(session, event);

    }

    private void sendEvent(final WebSocketSession session, final Event event) {
        RestTemplate restTemplate = new RestTemplate();

        final be.deschutter.scimitar.events.Event eventje = eventFactory
            .makeEvent(event);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.set("loggedInUsername", event.getUserId());

        HttpEntity<be.deschutter.scimitar.events.Event> e = new HttpEntity<>(
            eventje, headers);

        ResponseEntity<be.deschutter.scimitar.events.Event> reply = restTemplate
            .exchange("http://localhost:8080/scimitar", HttpMethod.POST, e,
                be.deschutter.scimitar.events.Event.class);
        if (reply.getStatusCode() == HttpStatus.FORBIDDEN) {
            eventje.setReply("No access");
            reply(session, eventje);
        }

        reply(session, reply.getBody());
    }

    private void reply(WebSocketSession session,
        be.deschutter.scimitar.events.Event reply) {
        try {

            Message message = new Message(reply.getReply());
            message.setChannel(reply.getChannel());

            message.setUser(reply.getCurrentUsername());
            message.setUsername(reply.getCurrentUsername());
            message.setType(EventType.MESSAGE.name().toLowerCase());

            final TextMessage webSocketMessage = new TextMessage(
                message.toJSONString());

            session.sendMessage(webSocketMessage);
            if (logger.isDebugEnabled()) {  // For debugging purpose only
                logger.debug("Reply (Message): {}", message.toJSONString());
            }
        } catch (IOException e) {
            logger.error("Error sending event: {}. Exception: {}",
                reply.getReply(), e.getMessage());
        }
    }

    /**
     * Invoked when bot receives an event of type message with text satisfying
     * the pattern {@code ([a-z ]{2})(\d+)([a-z ]{2})}. For example, messages
     * like "ab12xy" or "ab2bc" etc will invoke this method.
     * @param session
     * @param event
     */
    @Controller(events = EventType.MESSAGE,
        pattern = "(^[\\.!\\-])([a-zA-Z]*)\\s*(.*)")
    public void onReceiveMessage(WebSocketSession session, Event event,
        Matcher matcher) {
        sendEvent(session, event);
    }

    @JmsListener(destination = "notifications", containerFactory = "myFactory")
    public void receiveMessage(ScimitarNotification notification) {
        System.out.println(notification);
        final String slackUsername = scimitarUserEao
            .findByUsernameIgnoreCase(notification.getUsername())
            .getSlackUsername();

        final be.deschutter.scimitar.events.Event e = new be.deschutter.scimitar.events.Event();
        e.setCurrentUsername(slackUsername);
        e.setLoggedInUsername(slackUsername);
        e.setChannel("D6K5KRG6S");
        e.setReply(notification.getMessage());
        e.setReturnType(ReturnType.PRIVATE_MSG);
        reply(session, e);

    }

    @JmsListener(destination = "scanRequests", containerFactory = "myFactory")
    public void receiveScanRequest(String message) {
        final List<ChannelConfiguration> channels = channelConfigurationEao
            .findByChannelType(ChannelType.SCAN);

        channels.forEach(channelConfiguration -> {
            final be.deschutter.scimitar.events.Event e = new be.deschutter.scimitar.events.Event();
            e.setChannel(channelConfiguration.getChannelName());
            e.setReply(message);
            e.setReturnType(ReturnType.CHANNEL_MSG);
            reply(session, e);
        });
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        super.afterConnectionEstablished(session);
        this.session = session;
    }
}