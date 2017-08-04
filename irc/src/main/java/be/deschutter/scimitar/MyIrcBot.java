package be.deschutter.scimitar;

import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.IOException;

@Component
public class MyIrcBot {



    @Autowired
    private Scimitar scimitar;

    private PircBotX pircBotX;

    @PostConstruct
    public void createPircBot()
        throws IOException, IrcException, JAXBException {

        org.pircbotx.Configuration configuration = new org.pircbotx.Configuration.Builder()
            .setName("ScimitarBot") //Set the nick of the bot. CHANGE IN YOUR CODE
            .setServerHostname("irc.netgamers.org") //Join the freenode network
            .addAutoJoinChannel(
                "#hiertestikmijnbot") //Join the official #pircbotx channel
            .addListener(
                    scimitar) //Add our listener that will be called on Events
            .buildConfiguration();

        this.pircBotX = new PircBotX(configuration);

    }

    public void startBot() throws IOException, IrcException {
        pircBotX.startBot();
    }

    public Boolean isConnected() {
        return pircBotX.getState() == PircBotX.State.CONNECTED;
    }
}