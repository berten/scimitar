package be.deschutter.scimitar.attack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddBcalcListenerTest {
    @InjectMocks
    private AddBcalcListener addBcalcListener;
    @Mock
    private BookService bookService;
    @Mock
    private BookingHelper bookingHelper;
    @Test
    public void getCommand() throws Exception {
        assertThat(addBcalcListener.getCommand()).isEqualTo("addbcalc");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(addBcalcListener.getPattern()).isEqualTo("x y z LT|ETA url");
    }

    @Test
    public void getResult() throws Exception {
        final UserBooking expectedBooking = new UserBooking();
        when(bookService.addBcalc(1,2,3,14,"url")).thenReturn(
            expectedBooking);
        when(bookingHelper.getBookingInfo(expectedBooking)).thenReturn("expectedReturnString");
        assertThat(addBcalcListener.getResult("1","2","3","14","url")).isEqualTo("expectedReturnString");
    }

}