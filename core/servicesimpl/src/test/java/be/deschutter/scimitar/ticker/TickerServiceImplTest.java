package be.deschutter.scimitar.ticker;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TickerServiceImplTest {
    @InjectMocks
    private TickerServiceImpl tickerService;
    @Mock
    private TickerInfoEao tickerInfoEao;

    @Test
    public void getCurrentTick() throws Exception {
        final TickerInfo ti = new TickerInfo(123);
        when(tickerInfoEao.findFirstByOrderByTickDesc()).thenReturn(ti);
        assertThat(tickerService.getCurrentTick()).isEqualTo(ti);
    }

    @Test(expected = NoTickFoundException.class)
    public void getCurrentTick_NotickFound() throws Exception {
        tickerService.getCurrentTick();
    }
}