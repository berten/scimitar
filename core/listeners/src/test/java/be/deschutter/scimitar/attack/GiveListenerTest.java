package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.ScimitarUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GiveListenerTest {
    @InjectMocks
    private GiveListener giveListener;
    @Mock
    private BookService bookService;

    @Test
    public void getCommand() throws Exception {
        assertThat(giveListener.getCommand()).isEqualTo("give");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(giveListener.getPattern())
            .isEqualTo("x y z LT|ETA username|bg");
    }

    @Test
    public void getResult() throws Exception {
        final UserBooking userBooking = new UserBooking(
            new ScimitarUser("username"));
        userBooking.setTick(234);
        when(bookService.give(1, 2, 3, 13, "usernamebg"))
            .thenReturn(userBooking);
        assertThat(giveListener.getResult("1", "2", "3", "13", "usernamebg"))
            .isEqualTo(
                "Successfully transferred target 1:2:3 with LT: 234 to User: username");
    }

}