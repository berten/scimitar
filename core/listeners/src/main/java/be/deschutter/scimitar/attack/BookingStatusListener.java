package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class BookingStatusListener implements Listener {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookingHelper bookingHelper;

    @Override
    public String getCommand() {
        return "bookingstatus";
    }

    @Override
    public String getPattern() {
        return "x y z LT|ETA " + Arrays.stream(BookingStatus.values())
            .map(Enum::name).sorted(String::compareTo)
            .collect(Collectors.joining("|"));
    }

    @Override
    public String getResult(final String... parameters) {
        if (parameters.length == 5) {
            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);

            long ltEta = Long.parseLong(parameters[3]);

            final Booking booking = bookService
                .changeStatus(x, y, z, ltEta, parameters[4]);
            return bookingHelper.getBookingInfo(booking);
        } else
            return getErrorMessage();
    }
}
