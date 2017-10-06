package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.config.PaConfig;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import be.deschutter.scimitar.security.SecurityHelper;
import be.deschutter.scimitar.ticker.TickerService;
import be.deschutter.scimitar.user.BattleGroup;
import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.UserNotFoundException;
import be.deschutter.scimitar.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookingEao bookingEao;
    private final PlanetService planetService;
    private final SecurityHelper securityHelper;
    private BattleGroupService battleGroupService;
    private PaConfig paConfig;
    private TickerService tickerService;
    private UserService userService;

    @Autowired
    public BookServiceImpl(final BookingEao bookingEao,
        final PlanetService planetService, final SecurityHelper securityHelper,
        final BattleGroupService battleGroupService, final PaConfig paConfig,
        final TickerService tickerService, final UserService userService) {
        this.bookingEao = bookingEao;
        this.planetService = planetService;
        this.securityHelper = securityHelper;
        this.battleGroupService = battleGroupService;
        this.paConfig = paConfig;
        this.tickerService = tickerService;
        this.userService = userService;
    }

    @Override
    public Booking book(final int x, final int y, final int z, long ltEta,
        final String battlegroupName) {
        final Planet planet = planetService.findBy(x, y, z);
        long eta = defineLandingTick(ltEta);

        try {
            final Booking existingBooking = find(x, y, z, ltEta);
            throw new PlanetAlreadyBookedException(x, y, z, eta,
                existingBooking.getInfo());
        } catch (BookingDoesNotExistException e) {
            Booking booking = createBooking(battlegroupName);
            booking.setTick(eta);
            booking.setPlanetId(planet.getId());

            return bookingEao.save(booking);
        }

    }

    private long defineLandingTick(long ltEta) {
        if (ltEta < paConfig.getProtection() + 8) {
            ltEta = ltEta + tickerService.getCurrentTick().getTick();
        }
        return ltEta;
    }

    @Override
    public Booking join(final int x, final int y, final int z,
        final long ltEta) {
        Booking booking = find(x, y, z, ltEta);
        final ScimitarUser loggedInUser = securityHelper.getLoggedInUser();
        if (booking instanceof BattleGroupBooking) {
            final BattleGroup battleGroup = ((BattleGroupBooking) booking)
                .getBattleGroup();
            if (battleGroup.getScimitarUsers().contains(loggedInUser)) {
                booking.addUser(loggedInUser);
            } else {
                throw new CanNotJoinBattlegroupBookingWhenNotAMemberException(
                    battleGroup.getName());
            }
        } else if (booking instanceof UserBooking) {
            final UserBooking userBooking = (UserBooking) booking;
            if (userBooking.isJoinable())
                booking.addUser(loggedInUser);
            else
                throw new BookingNotJoinableException(
                    userBooking.getBookedBy().getUsername());
        }

        return bookingEao.save(booking);
    }

    @Override
    public Booking find(final int x, final int y, final int z,
        final long ltEta) {
        final Planet planet = planetService.findBy(x, y, z);
        final long landingTick = defineLandingTick(ltEta);
        Booking booking = bookingEao
            .findByPlanetIdAndTick(planet.getId(), landingTick);
        if (booking == null)
            throw new BookingDoesNotExistException(x, y, z, landingTick);
        return booking;
    }

    @Override
    public Booking assign(final int x, final int y, final int z,
        final long ltEta, final String username) {
        final Booking booking = find(x, y, z, defineLandingTick(ltEta));
        final ScimitarUser loggedInUser = securityHelper.getLoggedInUser();
        final ScimitarUser user = userService.findBy(username);
        if (booking instanceof BattleGroupBooking) {
            if (loggedInUser.isBc()) {
                booking.addUser(user);
            } else {
                throw new NotAuthorisedToAddUsersToBgTarget(
                    ((BattleGroupBooking) booking).getBattleGroup().getName());
            }
        } else if (booking instanceof UserBooking) {
            final ScimitarUser bookedByUser = ((UserBooking) booking)
                .getBookedBy();
            if (bookedByUser.equals(loggedInUser)) {
                booking.addUser(user);
            } else {
                throw new NotAuthorisedToAddUsersToTargetThatIsBookedBySomeoneElse(
                    bookedByUser.getUsername());
            }
        }
        return bookingEao.save(booking);
    }

    @Override
    public Booking unbook(final int x, final int y, final int z,
        final long ltEta) {
        final long landingTick = defineLandingTick(ltEta);
        final Booking booking = find(x, y, z, landingTick);
        final ScimitarUser loggedInUser = securityHelper.getLoggedInUser();
        if (booking instanceof UserBooking) {
            if (((UserBooking) booking).getBookedBy().equals(loggedInUser)
                || loggedInUser.isBc()) {
                bookingEao.delete(booking);
            } else {
                throw new NotAuthorisedToUnbookTargetException(x, y, z,
                    landingTick);
            }
        } else if (booking instanceof BattleGroupBooking) {
            if (loggedInUser.isBc()) {
                bookingEao.delete(booking);
            } else {
                throw new NotAuthorisedToUnbookTargetException(x, y, z,
                    landingTick);
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return booking;
    }

    @Override
    public Booking give(final int x, final int y, final int z, final long ltEta,
        final String usernameBg) {
        final long landingTick = defineLandingTick(ltEta);
        final Booking booking = find(x, y, z, landingTick);
        final ScimitarUser loggedInUser = securityHelper.getLoggedInUser();
        try {
            final ScimitarUser user = userService.findBy(usernameBg);
            if (booking instanceof UserBooking) {
                if (loggedInUser.equals(((UserBooking) booking).getBookedBy())
                    || loggedInUser.isBc()) {
                    ((UserBooking) booking).setBookedBy(user);
                    return bookingEao.save(booking);
                } else {
                    throw new NotAuthorisedToUnbookTargetException(x, y, z,
                        landingTick);
                }
            } else {
                if (loggedInUser.isBc()) {
                    UserBooking newBooking = new UserBooking();
                    newBooking.setBookedBy(user);
                    newBooking.setUsers(booking.getUsers());
                    newBooking.setTick(booking.getTick());
                    newBooking.setPlanetId(booking.getPlanetId());
                    newBooking.setJoinable(!booking.getUsers().isEmpty());
                    bookingEao.delete(booking);
                    return bookingEao.save(newBooking);
                } else {
                    throw new NotAuthorisedToUnbookTargetException(x, y, z,
                        landingTick);
                }
            }
        } catch (UserNotFoundException e) {

            final BattleGroup battleGroup = battleGroupService
                .findByName(usernameBg);
            if (booking instanceof BattleGroupBooking) {
                if (loggedInUser.isBc()) {
                    ((BattleGroupBooking) booking).setBattleGroup(battleGroup);
                    return bookingEao.save(booking);
                } else {
                    throw new NotAuthorisedToUnbookTargetException(x, y, z,
                        landingTick);
                }
            } else if (booking instanceof UserBooking) {
                if (loggedInUser.equals(((UserBooking) booking).getBookedBy())
                    || loggedInUser.isBc()) {
                    BattleGroupBooking newBooking = new BattleGroupBooking();
                    newBooking.setBattleGroup(battleGroup);
                    newBooking.setUsers(booking.getUsers());
                    newBooking.setTick(booking.getTick());
                    newBooking.setPlanetId(booking.getPlanetId());
                    bookingEao.delete(booking);
                    return bookingEao.save(newBooking);
                } else {
                    throw new NotAuthorisedToUnbookTargetException(x, y, z,
                        landingTick);
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public Booking unjoin(final int x, final int y, final int z,
        final long ltEta) {
        final Booking booking = find(x, y, z, defineLandingTick(ltEta));
        booking.removeUser(securityHelper.getLoggedInUser());
        return bookingEao.save(booking);
    }

    @Override
    public Booking remove(final int x, final int y, final int z,
        final long ltEta, final String username) {
        final long landingTick = defineLandingTick(ltEta);
        final Booking booking = find(x, y, z, landingTick);
        final ScimitarUser loggedInUser = securityHelper.getLoggedInUser();
        if ((booking instanceof UserBooking && ((UserBooking) booking)
            .getBookedBy().equals(loggedInUser)) || loggedInUser.isBc()) {
            booking.removeUser(userService.findBy(username));
            return bookingEao.save(booking);
        } else {
            throw new NotAuthorisedToUnbookTargetException(x, y, z,
                landingTick);
        }

    }

    @Override
    public Booking addBcalc(final int x, final int y, final int z,
        final long ltEta, final String url) {
        final long landingTick = defineLandingTick(ltEta);
        final Booking booking = find(x, y, z, landingTick);
        final ScimitarUser loggedInUser = securityHelper.getLoggedInUser();
        if ((booking instanceof UserBooking && ((UserBooking) booking)
            .getBookedBy().equals(loggedInUser)) || loggedInUser.isBc()
            || booking.getUsers().contains(loggedInUser)) {
            booking.setBcalc(url);
            return bookingEao.save(booking);
        } else {
            throw new NotAuthorisedToChangeBookingException(x, y, z,
                landingTick);
        }
    }

    @Override
    public Booking changeStatus(final int x, final int y, final int z,
        final long ltEta, final String bookingStatus) {
        final long landingTick = defineLandingTick(ltEta);
        final Booking booking = find(x, y, z, landingTick);
        final ScimitarUser loggedInUser = securityHelper.getLoggedInUser();
        if ((booking instanceof UserBooking && ((UserBooking) booking)
            .getBookedBy().equals(loggedInUser)) || loggedInUser.isBc()
            || booking.getUsers().contains(loggedInUser)) {
            booking.setStatus(BookingStatus.valueOf(bookingStatus));
            return bookingEao.save(booking);
        } else {
            throw new NotAuthorisedToChangeBookingException(x, y, z,
                landingTick);
        }
    }

    private Booking createBooking(final String battlegroupName) {

        if (battlegroupName != null) {
            final BattleGroup battleGroup = battleGroupService
                .findByName(battlegroupName);

            return new BattleGroupBooking(battleGroup);
        } else {
            final ScimitarUser scimitarUser = securityHelper.getLoggedInUser();
            Booking booking = new UserBooking(scimitarUser);
            booking.addUser(scimitarUser);
            return booking;
        }
    }
}
