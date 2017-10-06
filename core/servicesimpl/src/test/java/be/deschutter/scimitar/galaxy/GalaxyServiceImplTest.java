package be.deschutter.scimitar.galaxy;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.ticker.TickerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GalaxyServiceImplTest {

    @InjectMocks
    private GalaxyServiceImpl galaxyService;
    @Mock
    private GalaxyEao galaxyEao;
    @Mock
    private TickerService tickerService;

    @Before
    public void setUp() throws Exception {
        when(tickerService.getCurrentTick()).thenReturn(new TickerInfo(123));
    }

    @Test
    public void findBy() throws Exception {
        final Galaxy gal = new Galaxy();
        when(galaxyEao.findByXAndYAndTick(1, 2, 123)).thenReturn(gal);
        assertThat(galaxyService.findBy(1, 2)).isEqualTo(gal);
    }

    @Test(expected = GalaxyDoesNotExistException.class)
    public void findBy_galaxyDoesNotExist() throws Exception {
        try {
            galaxyService.findBy(1, 2);
        } catch (GalaxyDoesNotExistException e) {
            assertThat(e.getX()).isEqualTo(1);
            assertThat(e.getY()).isEqualTo(2);
            throw e;
        }
        fail("Should have thrown exception");
    }

}