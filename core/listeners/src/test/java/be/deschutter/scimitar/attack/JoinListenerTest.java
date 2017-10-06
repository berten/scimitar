package be.deschutter.scimitar.attack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JoinListenerTest {
    @InjectMocks
    private JoinListener joinListener;
    @Mock
    private BookService bookService;
    @Mock
    private BookingHelper bookingHelper;
    @Test
    public void getCommand() throws Exception {
        assertThat(joinListener.getCommand()).isEqualTo("join");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(joinListener.getPattern()).isEqualTo("x y z LT|ETA");
    }

    @Test
    public void getResult() throws Exception {
        final UserBooking b = new UserBooking();
        when(bookService.join(1,2,3,123)).thenReturn(b);
        when(bookingHelper.getBookingInfo(b)).thenReturn("bookingstring");
        assertThat(joinListener.getResult("1","2","3","123")).isEqualTo("bookingstring");
    }

}