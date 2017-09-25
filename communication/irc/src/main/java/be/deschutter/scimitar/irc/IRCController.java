package be.deschutter.scimitar.irc;

import org.pircbotx.exception.IrcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class IRCController {
    @Autowired
    private MyIrcBot myIrcBot;

    @RequestMapping("/admin/irc/status")
    public @ResponseBody
    Boolean getStatus() {
        return myIrcBot.isConnected();
    }

    @RequestMapping("/admin/irc/start")
    public @ResponseBody
    Boolean start() {
        try {
            myIrcBot.startBot();
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
        return true;
    }

}
