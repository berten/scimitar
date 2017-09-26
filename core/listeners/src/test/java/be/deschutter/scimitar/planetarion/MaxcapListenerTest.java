package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MaxcapListenerTest {
    @InjectMocks
    private MaxcapListener maxcapListener;
    @Mock
    private PlanetEao planetEao;
    @Mock
    private TickerInfoEao tickerInfoEao;
    @Mock
    private PaConfig paConfig;

    @Before
    public void setUp() throws Exception {
        when(paConfig.getMinCap()).thenReturn(0d);
        when(paConfig.getMaxCap()).thenReturn(0.25);

        when(tickerInfoEao.findFirstByOrderByTickDesc())
            .thenReturn(new TickerInfo(123));
        final Planet target = new Planet();
        target.setValue(400000);
        target.setSize(140);
        when(planetEao.findByXAndYAndZAndTick(1, 2, 3, 123)).thenReturn(target);

        final Planet attacker = new Planet();
        attacker.setValue(800000);
        when(planetEao.findByXAndYAndZAndTick(4, 5, 6, 123))
            .thenReturn(attacker);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(maxcapListener.getCommand()).isEqualTo("maxcap");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(maxcapListener.getPattern()).isEqualTo(
            "xTarget yTarget zTarget xAttacker yAttacker zAttacker <War Bonus y|n>");
    }

    @Test
    @Ignore
    public void getResult() throws Exception {
        assertThat(
            maxcapListener.getResult("berten", "1", "2", "3", "4", "5", "6"))
            .isEqualTo("maxcap");
    }

}