package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import be.deschutter.scimitar.security.SecurityHelper;
import be.deschutter.scimitar.ticker.TickerService;
import be.deschutter.scimitar.user.BattleGroup;
import be.deschutter.scimitar.user.RoleEnum;
import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.UserNotFoundException;
import be.deschutter.scimitar.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookingEao bookingEao;
    @Mock
    private PlanetService planetService;
    @Mock
    private BattleGroupService battleGroupService;
    @Mock
    private SecurityHelper securityHelper;
    @Mock
    private PaConfig paConfig;
    @Mock
    private TickerService tickerService;
    @Mock
    private UserService userService;
    private ScimitarUser loggedInUser;
    private BattleGroup battleGroup;
    private ScimitarUser user;

    @Before
    public void setUp() throws Exception {
        when(tickerService.getCurrentTick()).thenReturn(new TickerInfo(123));
        final Planet p = new Planet();
        p.setId("planetId");
        when(planetService.findBy(1, 2, 3)).thenReturn(p);
        loggedInUser = new ScimitarUser();
        loggedInUser.setUsername("loggedInUser");
        when(securityHelper.getLoggedInUser()).thenReturn(loggedInUser);

        user = new ScimitarUser("username");
        when(userService.findBy("username")).thenReturn(user);

        when(bookingEao.save(any(Booking.class))).thenAnswer(
            (Answer<Booking>) invocationOnMock -> (Booking) invocationOnMock
                .getArguments()[0]);

        battleGroup = new BattleGroup();
        battleGroup.setName("bgName");
        when(battleGroupService.findByName("bgName")).thenReturn(battleGroup);
        when(paConfig.getProtection()).thenReturn(24);
    }

    @Test
    public void book() throws Exception {
        final Booking book = bookService.book(1, 2, 3, 456);

        verify(bookingEao).save(any(Booking.class));

        assertThat(book.getPlanetId()).isEqualTo("planetId");
        assertThat(book).isInstanceOf(UserBooking.class);
        assertThat(((UserBooking) book).getBookedBy()).isEqualTo(loggedInUser);
        assertThat(book.getStatus()).isSameAs(BookingStatus.LAUNCH);
        assertThat(book.getUsers()).contains(loggedInUser);
        assertThat(book.getTick()).isEqualTo(456);
    }

    @Test
    public void book_etaRatherThenLt() throws Exception {
        final Booking book = bookService.book(1, 2, 3, 12);

        verify(bookingEao).save(any(Booking.class));

        assertThat(book.getPlanetId()).isEqualTo("planetId");
        assertThat(book).isInstanceOf(UserBooking.class);
        assertThat(((UserBooking) book).getBookedBy()).isEqualTo(loggedInUser);
        assertThat(book.getStatus()).isSameAs(BookingStatus.LAUNCH);
        assertThat(book.getUsers()).contains(loggedInUser);
        assertThat(book.getTick()).isEqualTo(135);
    }

    @Test(expected = PlanetAlreadyBookedException.class)
    public void book_alreadyExists_Bg() throws Exception {
        try {
            final BattleGroup battleGroup = new BattleGroup();
            battleGroup.setName("bgName");
            when(bookingEao.findByPlanetIdAndTick("planetId", 456))
                .thenReturn(new BattleGroupBooking(battleGroup));

            bookService.book(1, 2, 3, 456);

        } catch (PlanetAlreadyBookedException e) {
            assertExceptionBookedByBg(e);

            throw e;
        }
        fail("Should have thrown an exception");
    }

    @Test(expected = PlanetAlreadyBookedException.class)
    public void book_alreadyExists_User() throws Exception {
        try {
            when(bookingEao.findByPlanetIdAndTick("planetId", 456))
                .thenReturn(new UserBooking(loggedInUser));

            bookService.book(1, 2, 3, 456);

        } catch (PlanetAlreadyBookedException e) {
            assertExceptionBookedByUser(e);

            throw e;
        }
        fail("Should have thrown an exception");
    }

    @Test
    public void book_forBg() throws Exception {
        final Booking book = bookService.book(1, 2, 3, 456, "bgName");

        verify(bookingEao).save(any(Booking.class));

        assertThat(book.getPlanetId()).isEqualTo("planetId");
        assertThat(book).isInstanceOf(BattleGroupBooking.class);
        assertThat(((BattleGroupBooking) book).getBattleGroup())
            .isEqualTo(battleGroup);
        assertThat(book.getStatus()).isSameAs(BookingStatus.LAUNCH);
        assertThat(book.getTick()).isEqualTo(456);
    }

    @Test(expected = PlanetAlreadyBookedException.class)
    public void book_alreadyExists_Bg_ForBg() throws Exception {
        try {
            final BattleGroup battleGroup = new BattleGroup();
            battleGroup.setName("bgName");
            when(bookingEao.findByPlanetIdAndTick("planetId", 456))
                .thenReturn(new BattleGroupBooking(battleGroup));

            bookService.book(1, 2, 3, 456, "bgName");

        } catch (PlanetAlreadyBookedException e) {
            assertExceptionBookedByBg(e);
            throw e;
        }
        fail("Should have thrown an exception");
    }

    @Test
    public void join_UserBooking() throws Exception {

        final UserBooking expectedBooking = new UserBooking(new ScimitarUser());
        expectedBooking.setJoinable(true);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        final Booking booking = bookService.join(1, 2, 3, 123);

        assertThat(booking.getUsers()).contains(loggedInUser);
    }

    @Test(expected = BookingNotJoinableException.class)
    public void join_UserBooking_NotJoinable() throws Exception {

        final ScimitarUser scimitarUser = new ScimitarUser();
        scimitarUser.setUsername("bookingUser");
        final UserBooking expectedBooking = new UserBooking(scimitarUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.join(1, 2, 3, 123);
        } catch (BookingNotJoinableException e) {
            assertThat(e.getUsername()).isEqualTo("bookingUser");
            throw e;
        }
        fail("Should have thrown an exception");

    }

    @Test
    public void join_bgBooking() throws Exception {

        final BattleGroup bg = new BattleGroup("bg");
        bg.addUser(loggedInUser);
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(bg);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        final Booking booking = bookService.join(1, 2, 3, 123);

        assertThat(booking.getUsers()).contains(loggedInUser);
    }

    @Test(expected = CanNotJoinBattlegroupBookingWhenNotAMemberException.class)
    public void join_bgBooking_NotAMemberOfBg() throws Exception {

        final BattleGroup bg = new BattleGroup("bg");
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(bg);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.join(1, 2, 3, 123);
        } catch (CanNotJoinBattlegroupBookingWhenNotAMemberException e) {
            assertThat(e.getBattleGroupName()).isEqualTo("bg");
            throw e;
        }
        fail("Should have thrown exception");
    }

    @Test
    public void findBy() throws Exception {
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        assertThat(bookService.find(1, 2, 3, 123)).isSameAs(expectedBooking);
    }

    @Test(expected = BookingDoesNotExistException.class)
    public void findBy_notFound() throws Exception {
        try {
            bookService.find(1, 2, 3, 123);
        } catch (BookingDoesNotExistException e) {
            assertThat(e.getLandingTick()).isEqualTo(123);
            assertThat(e.getX()).isEqualTo(1);
            assertThat(e.getY()).isEqualTo(2);
            assertThat(e.getZ()).isEqualTo(3);
            throw e;
        }
        fail("Sould have thrown exception");
    }

    @Test
    public void assign_BgBooking() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final BattleGroup bg = new BattleGroup("bg");
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(bg);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        final Booking booking = bookService.assign(1, 2, 3, 123, "username");
        assertThat(booking.getUsers()).contains(user);
    }

    @Test(expected = NotAuthorisedToAddUsersToBgTarget.class)
    public void assign_BgBooking_NoBc() throws Exception {
        final BattleGroup bg = new BattleGroup("bg");
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(bg);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.assign(1, 2, 3, 123, "username");
        } catch (NotAuthorisedToAddUsersToBgTarget e) {
            assertThat(e.getBattleGroupName()).isEqualTo("bg");
            throw e;
        }
        fail("Should have thrown an exception");
    }

    @Test
    public void assign_UserBooking() throws Exception {
        final UserBooking expectedBooking = new UserBooking(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        final Booking booking = bookService.assign(1, 2, 3, 123, "username");
        assertThat(booking.getUsers()).contains(user);

    }

    @Test(
        expected = NotAuthorisedToAddUsersToTargetThatIsBookedBySomeoneElse.class)
    public void assign_UserBooking_NotOriginalBooker() throws Exception {
        final UserBooking expectedBooking = new UserBooking(
            new ScimitarUser("someoneElse"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.assign(1, 2, 3, 123, "username");
        } catch (NotAuthorisedToAddUsersToTargetThatIsBookedBySomeoneElse e) {
            assertThat(e.getUsername()).isEqualTo("someoneElse");
            throw e;
        }
        fail("Should have thrown an exception");

    }

    @Test
    public void unbook_UserBooking_isBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final UserBooking expectedBooking = new UserBooking(
            new ScimitarUser("someoneElse"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        final Booking booking = bookService.unbook(1, 2, 3, 123);

        assertThat(booking).isSameAs(expectedBooking);
        verify(bookingEao).delete(expectedBooking);
    }

    @Test
    public void unbook_UserBooking_isOwner() throws Exception {
        final UserBooking expectedBooking = new UserBooking(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        final Booking booking = bookService.unbook(1, 2, 3, 123);

        assertThat(booking).isSameAs(expectedBooking);
        verify(bookingEao).delete(expectedBooking);
    }

    @Test
    public void unbook_UserBooking_isBC_isOwner() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final UserBooking expectedBooking = new UserBooking(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        final Booking booking = bookService.unbook(1, 2, 3, 123);

        assertThat(booking).isSameAs(expectedBooking);
        verify(bookingEao).delete(expectedBooking);
    }

    @Test(expected = NotAuthorisedToUnbookTargetException.class)
    public void unbook_UserBooking_isNotOwner_IsNotBc() throws Exception {
        final UserBooking expectedBooking = new UserBooking(
            new ScimitarUser("someotheruser"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.unbook(1, 2, 3, 123);
        } catch (NotAuthorisedToUnbookTargetException e) {
            assertNotAuthorisedToUnbookTargetException(expectedBooking, e);
            throw e;
        }
        fail("Should have thrown an exception");

    }

    @Test(expected = NotAuthorisedToUnbookTargetException.class)
    public void unbook_BgBooking_IsNotBc() throws Exception {
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.unbook(1, 2, 3, 123);
        } catch (NotAuthorisedToUnbookTargetException e) {
            assertNotAuthorisedToUnbookTargetException(expectedBooking, e);
            throw e;
        }
        fail("Should have thrown an exception");

    }

    @Test
    public void unjoin() throws Exception {
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        expectedBooking.addUser(user);
        expectedBooking.addUser(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        final Booking booking = bookService.unjoin(1, 2, 3, 123);
        assertThat(booking.getUsers()).doesNotContain(loggedInUser)
            .contains(user);
    }

    private void assertNotAuthorisedToUnbookTargetException(
        final Booking expectedBooking,
        final NotAuthorisedToUnbookTargetException e) {
        assertThat(e.getX()).isEqualTo(1);
        assertThat(e.getY()).isEqualTo(2);
        assertThat(e.getZ()).isEqualTo(3);
        assertThat(e.getLandingTick()).isEqualTo(123);
        verify(bookingEao, never()).delete(expectedBooking);
    }

    @Test
    public void unbook_BgBooking_IsBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.unbook(1, 2, 3, 123)).isSameAs(expectedBooking);

        verify(bookingEao).delete(expectedBooking);

    }

    @Test
    public void remove_BgBooking_IsBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(
            bookService.remove(1, 2, 3, 123, "anotherusername").getUsers())
            .doesNotContain(anotheruser).contains(user);

    }

    @Test(expected = NotAuthorisedToUnbookTargetException.class)
    public void remove_BgBooking_IsNotBc() throws Exception {
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        try {
            bookService.remove(1, 2, 3, 123, "anotherusername");
        } catch (NotAuthorisedToUnbookTargetException e) {
            assertUnauthorised(e);
            throw e;
        }

        fail("Should have thrown an exception");

    }

    @Test
    public void remove_UserBooking_IsBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new UserBooking(new ScimitarUser("bg"));
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(
            bookService.remove(1, 2, 3, 123, "anotherusername").getUsers())
            .doesNotContain(anotheruser).contains(user);

    }

    @Test(expected = NotAuthorisedToUnbookTargetException.class)
    public void remove_UserBooking_IsNotBcAndNotOwner() throws Exception {
        final Booking expectedBooking = new UserBooking(new ScimitarUser("bg"));
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.remove(1, 2, 3, 123, "anotherusername");
        } catch (NotAuthorisedToUnbookTargetException e) {
            assertUnauthorised(e);
            throw e;
        }

        fail("Should have thrown an exception");
    }

    @Test
    public void remove_UserBooking_IsOwner() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new UserBooking(loggedInUser);
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(
            bookService.remove(1, 2, 3, 123, "anotherusername").getUsers())
            .doesNotContain(anotheruser).contains(user);

    }

    @Test
    public void addbcalc_BgBooking_IsBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.addBcalc(1, 2, 3, 123, "url").getBcalc())
            .isEqualTo("url");

    }

    @Test
    public void addbcalc_BgBooking_IsPartOfTeamup() throws Exception {
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        expectedBooking.addUser(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.addBcalc(1, 2, 3, 123, "url").getBcalc())
            .isEqualTo("url");

    }

    @Test(expected = NotAuthorisedToChangeBookingException.class)
    public void addbcalc_BgBooking_IsNotBc() throws Exception {
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        try {
            bookService.addBcalc(1, 2, 3, 123, "url");
        } catch (NotAuthorisedToChangeBookingException e) {
            assertUnauthorised(e);
            throw e;
        }

        fail("Should have thrown an exception");

    }

    @Test
    public void addbcalc_UserBooking_IsBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new UserBooking(new ScimitarUser("bg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.addBcalc(1, 2, 3, 123, "url").getBcalc())
            .isEqualTo("url");

    }

    @Test
    public void addbcalc_UserBooking_IsPartOfTeaup() throws Exception {
        final Booking expectedBooking = new UserBooking(new ScimitarUser("bg"));
        expectedBooking.addUser(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.addBcalc(1, 2, 3, 123, "url").getBcalc())
            .isEqualTo("url");

    }

    @Test(expected = NotAuthorisedToChangeBookingException.class)
    public void addbcalc_UserBooking_IsNotBcAndNotOwner() throws Exception {
        final Booking expectedBooking = new UserBooking(new ScimitarUser("bg"));
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.addBcalc(1, 2, 3, 123, "url");
        } catch (NotAuthorisedToChangeBookingException e) {
            assertUnauthorised(e);
            throw e;
        }

        fail("Should have thrown an exception");
    }

    @Test
    public void addbcalc_UserBooking_IsOwner() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new UserBooking(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.addBcalc(1, 2, 3, 123, "url").getBcalc())
            .isEqualTo("url");

    }

    @Test
    public void changeStatus_UserBooking_IsOwner() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new UserBooking(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.changeStatus(1, 2, 3, 123, "LAND").getStatus())
            .isSameAs(BookingStatus.LAND);

    }

    @Test
    public void changeStatus_BgBooking_IsBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.changeStatus(1, 2, 3, 123, "LAND").getStatus())
            .isSameAs(BookingStatus.LAND);

    }

    @Test
    public void changeStatus_BgBooking_IsPartOfTeamup() throws Exception {
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        expectedBooking.addUser(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.changeStatus(1, 2, 3, 123, "LAND").getStatus())
            .isSameAs(BookingStatus.LAND);

    }

    @Test(expected = NotAuthorisedToChangeBookingException.class)
    public void changeStatus_BgBooking_IsNotBc() throws Exception {
        final Booking expectedBooking = new BattleGroupBooking(
            new BattleGroup("bg"));
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        try {
            bookService.changeStatus(1, 2, 3, 123, "LAND");
        } catch (NotAuthorisedToChangeBookingException e) {
            assertUnauthorised(e);
            throw e;
        }

        fail("Should have thrown an exception");

    }

    @Test
    public void changeStatus_UserBooking_IsBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final Booking expectedBooking = new UserBooking(new ScimitarUser("bg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.changeStatus(1, 2, 3, 123, "LAND").getStatus())
            .isSameAs(BookingStatus.LAND);

    }

    @Test
    public void changeStatus_UserBooking_IsPartOfTeaup() throws Exception {
        final Booking expectedBooking = new UserBooking(new ScimitarUser("bg"));
        expectedBooking.addUser(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        assertThat(bookService.changeStatus(1, 2, 3, 123, "LAND").getStatus())
            .isSameAs(BookingStatus.LAND);

    }

    @Test(expected = NotAuthorisedToChangeBookingException.class)
    public void changeStatus_UserBooking_IsNotBcAndNotOwner() throws Exception {
        final Booking expectedBooking = new UserBooking(new ScimitarUser("bg"));
        final ScimitarUser anotheruser = new ScimitarUser("anotherusername");
        expectedBooking.addUser(anotheruser);
        expectedBooking.addUser(user);
        when(userService.findBy("anotherusername")).thenReturn(anotheruser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);
        try {
            bookService.changeStatus(1, 2, 3, 123, "LAND");
        } catch (NotAuthorisedToChangeBookingException e) {
            assertUnauthorised(e);
            throw e;
        }

        fail("Should have thrown an exception");
    }

    @Test(expected = PlanetAlreadyBookedException.class)
    public void book_alreadyExists_User_ForBg() throws Exception {
        try {
            when(bookingEao.findByPlanetIdAndTick("planetId", 456))
                .thenReturn(new UserBooking(loggedInUser));

            bookService.book(1, 2, 3, 456, "bgName");

        } catch (PlanetAlreadyBookedException e) {
            assertExceptionBookedByUser(e);

            throw e;
        }
        fail("Should have thrown an exception");
    }

    @Test
    public void give_UserBookingToUserBooking() throws Exception {
        final ScimitarUser anotheruser = new ScimitarUser("anotheruser");
        when(userService.findBy("anotheruser")).thenReturn(anotheruser);
        final UserBooking expectedBooking = new UserBooking(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        final Booking newBooking = bookService
            .give(1, 2, 3, 123, "anotheruser");

        assertThat(newBooking).isInstanceOf(UserBooking.class);
        assertThat(((UserBooking) newBooking).getBookedBy())
            .isEqualTo(anotheruser);

    }

    @Test
    public void give_UserBookingToUserBooking_isBc() throws Exception {
        loggedInUser.addRole(RoleEnum.BC);
        final ScimitarUser anotheruser = new ScimitarUser("anotheruser");
        when(userService.findBy("anotheruser")).thenReturn(anotheruser);
        final UserBooking expectedBooking = new UserBooking(
            new ScimitarUser("differentuser"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        final Booking newBooking = bookService
            .give(1, 2, 3, 123, "anotheruser");

        assertThat(newBooking).isInstanceOf(UserBooking.class);
        assertThat(((UserBooking) newBooking).getBookedBy())
            .isEqualTo(anotheruser);

    }

    @Test(expected = NotAuthorisedToUnbookTargetException.class)
    public void give_UserBookingToUserBooking_isNotBcAndNotOwner()
        throws Exception {
        final ScimitarUser anotheruser = new ScimitarUser("anotheruser");
        when(userService.findBy("anotheruser")).thenReturn(anotheruser);
        final UserBooking expectedBooking = new UserBooking(
            new ScimitarUser("differentuser"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        try {
            bookService.give(1, 2, 3, 123, "anotheruser");
        } catch (NotAuthorisedToUnbookTargetException e) {
            assertUnauthorised(e);
        }

        fail("Should have thrown exception");

    }

    @Test
    public void give_UserBookingToBgBooking() throws Exception {
        when(userService.findBy("bgName"))
            .thenThrow(new UserNotFoundException(""));
        final UserBooking expectedBooking = new UserBooking(loggedInUser);
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        final Booking newBooking = bookService.give(1, 2, 3, 123, "bgName");

        assertThat(newBooking).isInstanceOf(BattleGroupBooking.class);
        assertThat(((BattleGroupBooking) newBooking).getBattleGroup())
            .isEqualTo(battleGroup);
        verify(bookingEao).delete(expectedBooking);
    }

    @Test
    public void give_UserBookingToBgBooking_isBc() throws Exception {
        when(userService.findBy("bgName"))
            .thenThrow(new UserNotFoundException(""));
        loggedInUser.addRole(RoleEnum.BC);
        final UserBooking expectedBooking = new UserBooking(
            new ScimitarUser("differentuser"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        final Booking newBooking = bookService.give(1, 2, 3, 123, "bgName");

        assertThat(newBooking).isInstanceOf(BattleGroupBooking.class);
        assertThat(((BattleGroupBooking) newBooking).getBattleGroup())
            .isEqualTo(battleGroup);
        verify(bookingEao).delete(expectedBooking);

    }

    @Test(expected = NotAuthorisedToUnbookTargetException.class)
    public void give_UserBookingToBgBooking_isNotBcAndNotOwner()
        throws Exception {
        when(userService.findBy("bgName"))
            .thenThrow(new UserNotFoundException(""));
        final UserBooking expectedBooking = new UserBooking(
            new ScimitarUser("differentuser"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        try {
            bookService.give(1, 2, 3, 123, "bgName");
        } catch (NotAuthorisedToUnbookTargetException e) {
            assertUnauthorised(e);
        }

        fail("Should have thrown exception");

    }

    @Test
    public void give_BgBookingToBgBooking_isBc() throws Exception {
        when(userService.findBy("bgName"))
            .thenThrow(new UserNotFoundException(""));
        loggedInUser.addRole(RoleEnum.BC);
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(
            new BattleGroup("anotherbg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        final Booking newBooking = bookService.give(1, 2, 3, 123, "bgName");

        assertThat(newBooking).isInstanceOf(BattleGroupBooking.class);
        assertThat(((BattleGroupBooking) newBooking).getBattleGroup())
            .isEqualTo(battleGroup);

    }

    @Test(expected = NotAuthorisedToUnbookTargetException.class)
    public void give_BgBookingToBgBooking_isNotBc() throws Exception {
        when(userService.findBy("bgName"))
            .thenThrow(new UserNotFoundException(""));
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(
            new BattleGroup("anotherbg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        try {
            bookService.give(1, 2, 3, 123, "bgName");
        } catch (NotAuthorisedToUnbookTargetException e) {
            assertUnauthorised(e);
            return;
        }

        fail("Should have thrown exception");

    }

    @Test
    public void give_BgBookingToUserBooking_isBc() throws Exception {
        final ScimitarUser anotheruser = new ScimitarUser("anotheruser");
        when(userService.findBy("anotheruser")).thenReturn(anotheruser);
        loggedInUser.addRole(RoleEnum.BC);
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(
            new BattleGroup("anotherbg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        final Booking newBooking = bookService
            .give(1, 2, 3, 123, "anotheruser");

        assertThat(newBooking).isInstanceOf(UserBooking.class);
        assertThat(((UserBooking) newBooking).getBookedBy())
            .isEqualTo(anotheruser);
        verify(bookingEao).delete(expectedBooking);

    }

    @Test(expected = NotAuthorisedToUnbookTargetException.class)
    public void give_BgBookingToUserBooking_isNotBc() throws Exception {
        final ScimitarUser anotheruser = new ScimitarUser("anotheruser");
        when(userService.findBy("anotheruser")).thenReturn(anotheruser);
        final BattleGroupBooking expectedBooking = new BattleGroupBooking(
            new BattleGroup("anotherbg"));
        when(bookingEao.findByPlanetIdAndTick("planetId", 123))
            .thenReturn(expectedBooking);

        try {
            bookService.give(1, 2, 3, 123, "anotheruser");
        } catch (NotAuthorisedToUnbookTargetException e) {
            assertUnauthorised(e);
            return;
        }

        fail("Should have thrown exception");

    }

    private void assertUnauthorised(
        final NotAuthorisedToUnbookTargetException e) {
        assertThat(e.getX()).isEqualTo(1);
        assertThat(e.getY()).isEqualTo(2);
        assertThat(e.getZ()).isEqualTo(3);
        assertThat(e.getLandingTick()).isEqualTo(123);
        throw e;
    }

    private void assertUnauthorised(
        final NotAuthorisedToChangeBookingException e) {
        assertThat(e.getX()).isEqualTo(1);
        assertThat(e.getY()).isEqualTo(2);
        assertThat(e.getZ()).isEqualTo(3);
        assertThat(e.getLandingTick()).isEqualTo(123);
        throw e;
    }

    private void assertExceptionBookedByBg(
        final PlanetAlreadyBookedException e) {
        assertThat(e.getBookedBy()).isEqualTo("BG: bgName");
        assertThat(e.getTick()).isEqualTo(456);
        assertThat(e.getX()).isEqualTo(1);
        assertThat(e.getY()).isEqualTo(2);
        assertThat(e.getZ()).isEqualTo(3);

        verify(bookingEao, never()).save(any(Booking.class));
    }

    private void assertExceptionBookedByUser(
        final PlanetAlreadyBookedException e) {
        assertThat(e.getBookedBy()).isEqualTo("User: loggedInUser");
        assertThat(e.getTick()).isEqualTo(456);
        assertThat(e.getX()).isEqualTo(1);
        assertThat(e.getY()).isEqualTo(2);
        assertThat(e.getZ()).isEqualTo(3);

        verify(bookingEao, never()).save(any(Booking.class));
    }

}