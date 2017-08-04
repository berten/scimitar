package be.deschutter.planetarion.katana;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Stats {

    private List<Ship> ship = new ArrayList<>();

    public List<Ship> getShip() {
        return ship;
    }

    public void setShip(List<Ship> ship) {
        this.ship = ship;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "ship=" + ship +
                '}';
    }
}
