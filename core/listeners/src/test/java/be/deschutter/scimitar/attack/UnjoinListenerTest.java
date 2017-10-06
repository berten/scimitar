package be.deschutter.scimitar.attack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnjoinListenerTest {
    @InjectMocks
    private UnjoinListener unjoinListener;
    @Mock
    private BookService bookService;

    @Test
    public void getCommand() throws Exception {
        assertThat(unjoinListener.getCommand()).isEqualTo("unjoin");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(unjoinListener.getPattern()).isEqualTo("x y z LT|ETA");
    }

    @Test
    public void getResult() throws Exception {
        final UserBooking b = new UserBooking();
        b.setTick(300);
        when(bookService.unjoin(1, 2, 3, 14)).thenReturn(b);
        assertThat(unjoinListener.getResult("1", "2", "3", "14"))
            .isEqualTo("Unjoined 1:2:3 for LT: 300");
    }

}