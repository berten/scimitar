package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class GiveListener implements Listener {
    private final BookService bookService;

    @Autowired
    public GiveListener(final BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String getCommand() {
        return "give";
    }

    @Override
    public String getPattern() {
        return "x y z LT|ETA username|bg";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {
        if (parameters.length == 5) {
            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);

            long ltEta = Long.parseLong(parameters[3]);

            final Booking booking = bookService
                .give(x, y, z, ltEta, parameters[4]);
            return String.format(
                "Successfully transferred target %d:%d:%d with LT: %d to %s", x,
                y, z, booking.getTick(), booking.getInfo());
        } else
            return getErrorMessage();

    }
}
