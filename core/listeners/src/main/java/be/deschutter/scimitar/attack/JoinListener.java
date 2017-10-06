package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class JoinListener implements Listener {
    private final BookService bookService;
    private final BookingHelper bookingHelper;

    @Autowired
    public JoinListener(final BookService bookService,
        final BookingHelper bookingHelper) {
        this.bookService = bookService;
        this.bookingHelper = bookingHelper;
    }

    @Override
    public String getCommand() {
        return "join";
    }

    @Override
    public String getPattern() {
        return "x y z LT|ETA";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {
        if (parameters.length == 4) {
            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);
            int ltEta = Integer.parseInt(parameters[3]);
            return bookingHelper
                .getBookingInfo(bookService.join(x, y, z, ltEta));
        } else
            return getErrorMessage();
    }
}
