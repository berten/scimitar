package be.deschutter.planetarion.katana.controller;

import be.deschutter.planetarion.katana.Ship;
import be.deschutter.planetarion.katana.StatHolder;
import be.deschutter.planetarion.katana.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class StatsController {

//    @Autowired
//    private StatHolder statHolder;

    @RequestMapping(value = "stats", method = RequestMethod.GET)
    public ModelAndView shipstats() {

        return new ModelAndView("stats", "stats",
            orderedStats(new Stats()));
    }

    private List<StatRace> orderedStats(final Stats stats) {

        final List<StatRace> statRaces = new ArrayList<>();

        for (Ship ship : stats.getShip()) {
            getStatRace(statRaces, ship).add(ship);
        }

        return statRaces.stream().sorted(
            StatRace::compareTo

        ).collect(Collectors.toList());
    }

    private StatRace getStatRace(final List<StatRace> statRaces,
        final Ship ship) {
        for (StatRace statRace : statRaces) {
            if (statRace.getRace().equals(ship.getRace())) {
                return statRace;
            }
        }
        final StatRace statRace = new StatRace(ship.getRace());
        statRaces.add(statRace);
        return statRace;
    }

    public class StatRace implements Comparable {

        private String race;
        private List<Ship> ships = new ArrayList<>();

        public StatRace(final String race) {
            this.race = race;
        }

        public String getRace() {
            return race;
        }

        public void add(final Ship ship) {
            this.ships.add(ship);
        }

        public List<Ship> getShips() {
            return ships;
        }

        @Override
        public String toString() {
            return race;
        }

        @Override
        public int compareTo(final Object o) {
            return ((StatRace) o).getRaceInteger().compareTo(getRaceInteger());
        }

        private Integer getRaceInteger() {
            switch (race) {
            case "Terran":
                return 5;
            case "Cathaar":
                return 4;
            case "Xandathrii":
                return 3;
            case "Zikonian":
                return 2;
            case "Eitraides":
                return 1;
            default:
                throw new IllegalStateException();
            }
        }

    }
}
