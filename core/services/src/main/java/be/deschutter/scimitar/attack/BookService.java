package be.deschutter.scimitar.attack;

public interface BookService {

    Booking book(int x, int y, int z, long ltEta, String battleGroupName);

    default Booking book(int x, int y, int z, long ltEta) {
        return book(x, y, z, ltEta, null);
    }

    Booking join(int x, int y, int z, long ltEta);

    Booking find(int x, int y, int z, long ltEta);

    Booking assign(int x, int y, int z, long ltEta, final String username);

    Booking unbook(int x, int y, int z, long ltEta);

    Booking give(int x, int y, int z, long ltEta, final String username);

    Booking unjoin(int x, int y, int z, long ltEta);

    Booking remove(int x, int y, int z, long ltEta, String username);

    Booking addBcalc(int x, int y, int z, long ltEta, String url);

    Booking changeStatus(int x, int y, int z, long ltEta, String bookingStatus);
}
