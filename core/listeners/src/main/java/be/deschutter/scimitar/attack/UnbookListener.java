package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class UnbookListener implements Listener {
    @Autowired
    private BookService bookService;

    @Override
    public String getCommand() {
        return "unbook";
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

            long ltEta = Long.parseLong(parameters[3]);

            Booking booking = bookService.unbook(x, y, z, ltEta);
            return String.format("Unbooked %d:%d:%d for LT: %d", x, y, z,
                booking.getTick());
        } else
            return getErrorMessage();
    }
}
