package be.deschutter.planetarion.katana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShipService {
//    @Autowired
//    private StatHolder statHolder;

    public Map<String, Result> analyze() {
        final Stats stats = new Stats();
        final Map<String, Result> results = new HashMap<>();
        for (Ship roidingShip : stats.getShip()) {
            results.computeIfAbsent(roidingShip.getRace(), k -> new Result());
        }
        for (Map.Entry<String, Result> stringResultEntry : results.entrySet()) {
            for (Ship ship : stats.getShip()) {

            }
        }
        return results;
    }

}
