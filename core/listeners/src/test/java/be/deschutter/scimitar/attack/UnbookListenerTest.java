package be.deschutter.scimitar.attack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnbookListenerTest {
    @InjectMocks
    private UnbookListener unbookListener;
    @Mock
    private BookService bookService;
    @Test
    public void getCommand() throws Exception {
        assertThat(unbookListener.getCommand()).isEqualTo("unbook");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(unbookListener.getPattern()).isEqualTo("x y z LT|ETA");
    }

    @Test
    public void getResult() throws Exception {
        final UserBooking b = new UserBooking();
        b.setTick(300);
        when(bookService.unbook(1,2,3,14)).thenReturn(b);
        assertThat(unbookListener.getResult("1","2","3","14")).isEqualTo("Unbooked 1:2:3 for LT: 300");
    }

}