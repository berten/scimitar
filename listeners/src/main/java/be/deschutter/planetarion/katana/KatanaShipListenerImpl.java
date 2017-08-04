package be.deschutter.planetarion.katana;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Eventje(event = "ship")
public class KatanaShipListenerImpl implements KatanaListener {
//    @Autowired
//    private StatHolder statHolder;

    @Override
    public String handle(KatanaEvent message) {

        Pattern pattern = Pattern.compile("(.*)");

        Matcher m = pattern.matcher(message.getParameters());
        if (m.matches()) {
            List<Ship> resultShips = new Stats().getShip().stream().filter(ship -> ship.getName().toUpperCase().contains(m.group().toUpperCase())).collect(Collectors.toList());
            if (resultShips.size() == 1) {
                return resultShips.get(0).toString();
            } else {
                return "";
            }
        }
        return null;
    }


}
