package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import be.deschutter.scimitar.security.SecurityHelper;
import be.deschutter.scimitar.user.ScimitarUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookingHelper {
    @Autowired
    private PlanetService planetService;
    @Autowired
    private SecurityHelper securityHelper;

    public String getBookingInfo(Booking booking) {
        final Planet p = planetService.findBy(booking.getPlanetId());
        final ScimitarUser user = securityHelper.getLoggedInUser();
        if (user.isBc() || isPartOfTheTeamup(booking, user) || isOriginalBooker(
            booking, user))
            return String.format(
                "Booking on %d:%d:%d (%s) @ LT %d by %s: teamup:%s comment:%s bcalc:%s status:%s",
                p.getX(), p.getY(), p.getZ(),p.getRace(), booking.getTick(),booking.getInfo(),
                booking.getUsers().stream().map(ScimitarUser::getUsername)
                    .collect(Collectors.joining(", ")), booking.getComment(),
                booking.getBcalc(), booking.getStatus().name());
        else
            return String.format("Booking on %d:%d:%d (%s) @LT %d by %s",p.getX(),p.getY(),p.getZ(),p.getRace(),booking.getTick(),booking.getInfo());
    }

    private boolean isPartOfTheTeamup(final Booking booking,
        final ScimitarUser user) {
        return booking.getUsers().contains(user);
    }

    private boolean isOriginalBooker(final Booking booking,
        final ScimitarUser user) {
        return booking instanceof UserBooking && ((UserBooking) booking)
            .getBookedBy().equals(user);
    }
}
