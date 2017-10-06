package be.deschutter.scimitar.attack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookListenerTest {
    @InjectMocks
    private BookListener bookListener;
    @Mock
    private BookService bookService;
    @Mock
    private BookingHelper bookingHelper;

    @Test
    public void getCommand() throws Exception {
        assertThat(bookListener.getCommand()).isEqualTo("book");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(bookListener.getPattern()).isEqualTo("x y z LT|ETA <bg>");
    }

    @Test
    public void getResult() throws Exception {
        final UserBooking expectedBooking = new UserBooking();
        when(bookService.book(1, 2, 3, 123)).thenReturn(expectedBooking);
        when(bookingHelper.getBookingInfo(expectedBooking))
            .thenReturn("bookingtext");
        assertThat(bookListener.getResult("1", "2", "3", "123"))
            .isEqualTo("bookingtext");

    }

    @Test
    public void getResult_withBg() throws Exception {
        final UserBooking expectedBooking = new UserBooking();
        when(bookService.book(1, 2, 3, 123, "bgName"))
            .thenReturn(expectedBooking);
        when(bookingHelper.getBookingInfo(expectedBooking))
            .thenReturn("bookingtext");
        assertThat(bookListener.getResult("1", "2", "3", "123", "bgName"))
            .isEqualTo("bookingtext");
    }

}