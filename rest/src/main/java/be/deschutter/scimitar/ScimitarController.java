package be.deschutter.scimitar;

import be.deschutter.scimitar.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scimitar")
public class ScimitarController {
    @Autowired
    private List<Listener> listeners;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Event> getStatus(
        @RequestBody
            Event event) {
        for (Listener listener : listeners) {
            try {
                if (listener.getCommand().equals(event.getCommand())) {
                    event.setReply(listener
                        .getResult(event.getParameters()));
                    return new ResponseEntity<>(event, HttpStatus.OK);
                }
            } catch (AccessDeniedException e) {
                event.setReply("Access Denied");
                return new ResponseEntity<>(event, HttpStatus.OK);
            } catch (NumberFormatException e) {
                event.setReply(listener.getErrorMessage());
                return new ResponseEntity<>(event, HttpStatus.OK);
            } catch (RuntimeException e) {
                event.setReply(e.getMessage());
                return new ResponseEntity<>(event, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(event, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public @ResponseBody
    ResponseEntity<String> getTest() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
