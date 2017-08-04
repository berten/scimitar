package be.deschutter.planetarion.katana;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShipServiceTest {
    @InjectMocks
    private ShipService shipService;

    @Mock
    private StatHolder statHolder;


    @Before
    public void setUp() {

    }

    @Test
    public void analyze_1Race_1PodClass_NothingShootsThePod() throws Exception {
        new Asserter().assertThatFor(
            new StatBuilder().withShip("ship1", "Fi", "Ter").thatIsAPod()
                .completeShip().completeStats()).race("Ter").roids("Ter");
    }

    @Test
    public void analyze_1Race_1PodClass_ShipThatShootsPod_NoFlak() throws Exception {
        new Asserter().assertThatFor(
            new StatBuilder().withShip("pod", "Fi", "Ter").thatIsAPod()
                .completeShip().withShip("ship2","Co","Ter").completeShip().completeStats()).race("Ter").roids("Ter");
    }

    private class StatBuilder {
        List<Ship> shipList = new ArrayList<>();

        public ShipBuilder withShip(String shipname, String clazz,
            final String race) {
            return new ShipBuilder(shipname, clazz, race, this);
        }

        public Stats completeStats() {
            final Stats stats = new Stats();
            stats.setShip(shipList);

            return stats;
        }

        private class ShipBuilder {
            private final String shipname;
            private final String race;
            private final StatBuilder statBuilder;
            private String clazz;
            private List<String> targets = new ArrayList<>();
            private Boolean emp = false;
            private Boolean steal = false;
            private Boolean cloak = false;
            private Boolean pod = false;
            private Integer empRes = 1;
            private Integer metal = 1;
            private Integer crystal = 1;
            private Integer eonium = 1;
            private Integer guns = 1;
            private Integer armor = 1;
            private Integer initiative = 1;

            private ShipBuilder(String shipname, String clazz,
                final String race, final StatBuilder statBuilder) {
                this.shipname = shipname;
                this.clazz = clazz;
                this.race = race;
                this.statBuilder = statBuilder;
            }

            public ShipBuilder thatTargets(final String... targetClasses) {
                if (targets.isEmpty() && !pod)
                    this.targets.addAll(Arrays.asList(targetClasses));
                else
                    throw new UnsupportedOperationException(
                        "You can only add targetclassesOnce");
                return this;
            }

            public StatBuilder completeShip() {
                final Ship ship = new Ship();
                ship.setArmor(armor);
                ship.setName(shipname);
                ship.setGuns(guns);
                ship.setEmpres(empRes);
                ship.setMetal(metal);
                ship.setEonium(eonium);
                ship.setArmor(armor);
                ship.setRace(race);
                ship.setType("Normal");
                if (!pod) {
                    ship.setTarget1(targets.get(0));
                    ship.setTarget2(targets.get(1));
                    ship.setTarget3(targets.get(2));
                } else
                    ship.setTarget1("Ro");
                ship.setClazz(clazz);
                ship.setInitiative(initiative);

                statBuilder.addShip(ship);
                return statBuilder;
            }

            public ShipBuilder thatIsAPod() {
                if (targets.isEmpty()) {
                    pod = true;
                    initiative = 100;
                } else
                    throw new UnsupportedOperationException(
                        "You can only add targetclassesOnce");

                return this;
            }
        }

        private void addShip(final Ship ship) {
            shipList.add(ship);
        }
    }

    private class Asserter {
        private Map<String, Result> analyzedStats;

        public Asserter assertThatFor(final Stats stats) {
            when(statHolder.getStats()).thenReturn(stats);
            this.analyzedStats = shipService.analyze();
            return this;
        }

        public ResultAnalyzer race(final String race) {
            return new ResultAnalyzer(analyzedStats.get(race), race);
        }

        private class ResultAnalyzer {
            private Result result;
            private String race;

            public ResultAnalyzer(final Result result, final String race) {
                this.result = result;
                this.race = race;
            }

            public ResultAnalyzer roids(final String roidedRace) {
                for (String racesRoided : result.getRoidedRaces()) {
                    if (racesRoided.equals(roidedRace)) {
                        return this;
                    }
                }

                fail("Expected " + race + " to roid " + roidedRace);
                throw new RuntimeException();
            }
        }
    }

}