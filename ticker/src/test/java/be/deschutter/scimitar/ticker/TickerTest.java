package be.deschutter.scimitar.ticker;


import javafx.scene.input.InputMethodTextRun;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TickerTest {
    public static final int TICKS_IN_DAY = 24;
    public static final int TICKS_IN_FIRST_DAY = 5;
    @InjectMocks
    private Ticker ticker;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(ticker,"ticksInDay", TICKS_IN_DAY);
        ReflectionTestUtils.setField(ticker,"ticksInFirstDay", TICKS_IN_FIRST_DAY);
    }

    @Test
    public void tickToCompareWith_FirstDayComparesToTick1() throws Exception {
        for(int i =1;i<=TICKS_IN_FIRST_DAY;i++) {
            assertThat(ticker.tickToCompareWith(i)).isEqualTo(1);
        }
    }

    @Test
    public void tickToCompareWith_FirstTickOf2ndDay() throws Exception {
        assertThat(ticker.tickToCompareWith(TICKS_IN_FIRST_DAY + 1)).isEqualTo(TICKS_IN_FIRST_DAY);
    }

    @Test
    public void tickToCompareWith_LastTickOf2ndDay() throws Exception {
        assertThat(ticker.tickToCompareWith(TICKS_IN_FIRST_DAY + TICKS_IN_DAY)).isEqualTo(TICKS_IN_FIRST_DAY);
    }

    @Test
    public void tickToCompareWith_FirstTickOf3rdDay() throws Exception {
        assertThat(ticker.tickToCompareWith(TICKS_IN_FIRST_DAY + TICKS_IN_DAY+ 1 )).isEqualTo(TICKS_IN_FIRST_DAY + TICKS_IN_DAY);
    }

    @Test
    public void name() throws Exception {
        assertThat(ticker.tickToCompareWith(TICKS_IN_FIRST_DAY + (TICKS_IN_DAY - 1))).isEqualTo(TICKS_IN_FIRST_DAY);
    }
}
