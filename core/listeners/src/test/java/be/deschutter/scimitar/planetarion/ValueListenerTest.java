package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValueListenerTest {
    @InjectMocks
    private ValueListener valueListener;
    @Mock
    private PlanetEao planetEao;

    @Before
    public void setUp() throws Exception {
        Planet p1 = createPlanet(1452145, 1452145 - 3400, 3);
        Planet p2 = createPlanet(3400, 2400, 2);
        Planet p3 = createPlanet(1000, 10, 1);
        when(planetEao.findFirst15ByXAndYAndZOrderByTickDesc(1, 2, 3)).thenReturn(Arrays.asList(p1, p2, p3));
    }

    private Planet createPlanet(int value, int valueGrowth, int tick) {
        Planet p1 = new Planet();
        p1.setValue(value);
        p1.setValueGrowth(valueGrowth);
        p1.setTick(tick);
        return p1;
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(valueListener.getCommand()).isEqualTo("value");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(valueListener.getPattern()).isEqualTo("x y z");
    }

    @Test
    public void getResult() throws Exception {
        assertThat(valueListener.getResult("1", "2", "3")).isEqualTo("Value in the last 3 ticks on 1:2:3 pt1 1000 (10)| pt2 3400 (2400)| pt3 1.4M (1.4M)");

    }

    @Test
    public void getResult_toFewParamters() throws Exception {
        assertThat(valueListener.getResult("1", "2")).isEqualTo("Error: use following pattern for command value: x y z");

    }

    @Test
    public void getResult_toManyParamters() throws Exception {
        assertThat(valueListener.getResult("1", "2", "3", "4")).isEqualTo("Error: use following pattern for command value: x y z");

    }

    @Test
    public void getResult_ParameterNotNumber() throws Exception {
        assertThat(valueListener.getResult("1", "2", "z")).isEqualTo("Error: use following pattern for command value: x y z");
    }

}
