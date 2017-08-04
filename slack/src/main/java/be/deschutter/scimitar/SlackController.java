package be.deschutter.scimitar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SlackController {

    @Autowired
    private SlackBot slackBot;

    @RequestMapping("/admin/slack/status")
    public @ResponseBody
    Boolean getStatus() {

        return true;
    }

    @RequestMapping("/admin/slack/start")
    public @ResponseBody
    Boolean start() {
        slackBot.startWebSocketConnection();
        return true;
    }
}
