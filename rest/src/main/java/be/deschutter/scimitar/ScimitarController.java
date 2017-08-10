package be.deschutter.scimitar;

import be.deschutter.scimitar.events.Event;
import be.deschutter.scimitar.planetarion.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scimitar")
public class ScimitarController {
@Autowired
private List<Listener> listeners;
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Event> getStatus(@RequestBody Event event) {
        for (Listener listener : listeners) {
            if(listener.getCommand().equals(event.getCommand())) {
                event.setReply(listener.getResult(event.getLoggedInUsername(),event.getParameters()));
                return new ResponseEntity<>(event, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(event, HttpStatus.NO_CONTENT);
    }
}
