package be.deschutter.scimitar.planetarion;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoidcostListenerTest {
    @InjectMocks
    private RoidcostListener roidcostListener;
    @Mock
    private PaConfig paConfig;

    @Before
    public void setUp() throws Exception {
        when(paConfig.getMiningPerRoid()).thenReturn(250d);
        when(paConfig.getValuePerResource()).thenReturn(0.01);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(roidcostListener.getCommand()).isEqualTo("roidcost");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(roidcostListener.getPattern())
            .isEqualTo("roids value_cost <mining_bonus>");
    }

    @Test
    public void getResult_NoGovernmentStuffz() throws Exception {
        assertThat(roidcostListener.getResult("Berten", "150", "65000", "30"))
            .isEqualTo(
                "Capping 150 roids at 65k value with 30% bonus will repay in 134 ticks (6 days)");
    }

    @Test
    public void getResult_NoGovernmentStuffz_ValueWithKilos() throws Exception {
        assertThat(roidcostListener.getResult("Berten", "150", "65k", "30"))
            .isEqualTo(
                "Capping 150 roids at 65k value with 30% bonus will repay in 134 ticks (6 days)");
    }

    @Test
    public void getResult_NoGovernmentStuffz_ValueWithKilos_NoBonus()
        throws Exception {
        assertThat(roidcostListener.getResult("Berten", "150", "65k"))
            .isEqualTo(
                "Capping 150 roids at 65k value with 0% bonus will repay in 174 ticks (8 days)");
    }

    @Test
    public void getResult_WithGovernmentStuffz() throws Exception {
        final List<Government> governments = new ArrayList<>();
        final Government demo = new Government();
        demo.setCode("Demo");
        demo.setProductionCostBonus(-0.08);
        governments.add(demo);

        final Government tot = new Government();
        tot.setCode("Tot");
        tot.setProductionCostBonus(-0.08);
        governments.add(tot);

        when(paConfig.getGovernments()).thenReturn(governments);
        assertThat(roidcostListener.getResult("Berten", "150", "65000", "30"))
            .isEqualTo(
                "Capping 150 roids at 65k value with 30% bonus will repay in 134 ticks (6 days) | Demo: 123 ticks (6 days) | Tot: 123 ticks (6 days)");
    }

}