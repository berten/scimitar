package be.deschutter.planetarion.katana.controller;

import be.deschutter.scimitar.events.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scimitar")
public class ScimitarController {

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Event> getStatus(@RequestBody Event event) {
        event.setReply("Understood sir");
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
