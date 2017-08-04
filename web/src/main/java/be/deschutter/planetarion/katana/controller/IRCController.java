package be.deschutter.planetarion.katana.controller;

import be.deschutter.planetarion.katana.MyIrcBot;
import org.pircbotx.exception.IrcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class IRCController {
    @Autowired
    private MyIrcBot myIrcBot;

    @RequestMapping("/admin/irc/status")
    public @ResponseBody Boolean getStatus() {
        return myIrcBot.isConnected();
    }

    @RequestMapping("/admin/irc/start")
    public @ResponseBody Boolean start() {
        try {
            myIrcBot.startBot();
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
        return true;
    }
}
