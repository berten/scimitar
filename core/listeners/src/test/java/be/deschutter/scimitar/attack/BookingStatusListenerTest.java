package be.deschutter.scimitar.attack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookingStatusListenerTest {
    @InjectMocks
    private BookingStatusListener bookingStatusListener;
    @Mock
    private BookService bookService;
    @Mock
    private BookingHelper bookingHelper;
    @Test
    public void getCommand() throws Exception {
        assertThat(bookingStatusListener.getCommand()).isEqualTo("bookingstatus");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(bookingStatusListener.getPattern()).isEqualTo("x y z LT|ETA LAND|LAUNCH|RECALL|RECALL_LAST_MINUTE");
    }

    @Test
    public void getResult() throws Exception {
        final UserBooking expectedBooking = new UserBooking();
        when(bookService.changeStatus(1,2,3,14,"LAND")).thenReturn(
            expectedBooking);
        when(bookingHelper.getBookingInfo(expectedBooking)).thenReturn("expectedReturnString");
        assertThat(bookingStatusListener.getResult("1","2","3","14","LAND")).isEqualTo("expectedReturnString");
    }

}