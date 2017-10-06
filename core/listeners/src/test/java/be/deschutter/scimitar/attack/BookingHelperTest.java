package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import be.deschutter.scimitar.security.SecurityHelper;
import be.deschutter.scimitar.user.BattleGroup;
import be.deschutter.scimitar.user.RoleEnum;
import be.deschutter.scimitar.user.ScimitarUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookingHelperTest {
    @InjectMocks
    private BookingHelper bookingHelper;
    @Mock
    private SecurityHelper securityHelper;
    @Mock
    private PlanetService planetService;
    private ScimitarUser loggedInUser;

    @Before
    public void setUp() throws Exception {
        loggedInUser = new ScimitarUser("loggedinuser");
        loggedInUser.addRole(RoleEnum.MEMBER);
        when(securityHelper.getLoggedInUser()).thenReturn(loggedInUser);

        final Planet planet = new Planet("planetId");
        planet.setX(1);
        planet.setY(2);
        planet.setZ(3);
        planet.setRace("Zik");
        when(planetService.findBy("planetId")).thenReturn(planet);

    }

    @Test
    public void getBookingInfo() throws Exception {
        final ScimitarUser scimitarUser = new ScimitarUser();
        scimitarUser.setUsername("anotheruser");
        Booking booking = new UserBooking(scimitarUser);
        booking.setPlanetId("planetId");
        booking.setTick(123);

        assertThat(bookingHelper.getBookingInfo(booking))
            .isEqualTo("Booking on 1:2:3 (Zik) @LT 123 by User: anotheruser");
    }

    @Test
    public void getBookingInfo_bg() throws Exception {
        Booking booking = new BattleGroupBooking(new BattleGroup("bgName"));
        booking.setPlanetId("planetId");
        booking.setTick(123);

        assertThat(bookingHelper.getBookingInfo(booking))
            .isEqualTo("Booking on 1:2:3 (Zik) @LT 123 by BG: bgName");
    }

    @Test
    public void getBookingInfo_isPartOfTeamup() throws Exception {
        final ScimitarUser scimitarUser = new ScimitarUser();
        scimitarUser.setUsername("anotheruser");
        Booking booking = new UserBooking(scimitarUser);
        booking.addUser(scimitarUser);
        booking.setPlanetId("planetId");
        booking.setTick(123);
        booking.addUser(loggedInUser);
        booking.setComment("Fake DE");
        booking.setBcalc("38943844ea");
        booking.setStatus(BookingStatus.RECALL_LAST_MINUTE);
        assertThat(bookingHelper.getBookingInfo(booking)).isEqualTo(
            "Booking on 1:2:3 (Zik) @ LT 123 by User: anotheruser: teamup:anotheruser, loggedinuser comment:Fake DE bcalc:38943844ea status:RECALL_LAST_MINUTE");
    }

    @Test
    public void getBookingInfo_isOriginalClaimer() throws Exception {
        Booking booking = new UserBooking(loggedInUser);
        booking.setPlanetId("planetId");
        booking.setTick(123);
        booking.addUser(loggedInUser);
        booking.setComment("Fake DE");
        booking.setBcalc("38943844ea");
        booking.setStatus(BookingStatus.RECALL_LAST_MINUTE);
        assertThat(bookingHelper.getBookingInfo(booking)).isEqualTo(
            "Booking on 1:2:3 (Zik) @ LT 123 by User: loggedinuser: teamup:loggedinuser comment:Fake DE bcalc:38943844ea status:RECALL_LAST_MINUTE");
    }

    @Test
    public void getBookingInfo_isBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final ScimitarUser user1 = new ScimitarUser("user1");
        Booking booking = new UserBooking(user1);
        booking.addUser(user1);
        booking.setPlanetId("planetId");
        booking.setTick(123);
        booking.addUser(loggedInUser);
        booking.setComment("Fake DE");
        booking.setBcalc("38943844ea");
        booking.setStatus(BookingStatus.RECALL_LAST_MINUTE);
        assertThat(bookingHelper.getBookingInfo(booking)).isEqualTo(
            "Booking on 1:2:3 (Zik) @ LT 123 by User: user1: teamup:user1, loggedinuser comment:Fake DE bcalc:38943844ea status:RECALL_LAST_MINUTE");
    }
}