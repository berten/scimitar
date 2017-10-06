package be.deschutter.scimitar.attack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoveListenerTest {
    @InjectMocks
    private RemoveListener removeListener;
    @Mock
    private BookService bookService;
    @Mock
    private BookingHelper bookingHelper;
    @Test
    public void getCommand() throws Exception {
        assertThat(removeListener.getCommand()).isEqualTo("remove");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(removeListener.getPattern()).isEqualTo("x y z LT|ETA username");
    }

    @Test
    public void getResult() throws Exception {
        final UserBooking expectedBooking = new UserBooking();
        when(bookService.remove(1,2,3,14,"username")).thenReturn(
            expectedBooking);
        when(bookingHelper.getBookingInfo(expectedBooking)).thenReturn("expectedReturnString");
        assertThat(removeListener.getResult("1","2","3","14","username")).isEqualTo("expectedReturnString");
    }

}