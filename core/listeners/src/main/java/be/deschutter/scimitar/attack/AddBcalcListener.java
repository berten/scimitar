package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddBcalcListener implements Listener{
    @Autowired
    private BookService bookService;
    @Autowired private BookingHelper bookingHelper;

    @Override
    public String getCommand() {
        return "addbcalc";
    }

    @Override
    public String getPattern() {
        return "x y z LT|ETA url";
    }

    @Override
    public String getResult(final String... parameters) {
        if (parameters.length == 5) {
            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);

            long ltEta = Long.parseLong(parameters[3]);

            final Booking booking = bookService
                .addBcalc(x, y, z, ltEta, parameters[4]);
            return bookingHelper.getBookingInfo(booking);
        } else
            return getErrorMessage();
    }
}
