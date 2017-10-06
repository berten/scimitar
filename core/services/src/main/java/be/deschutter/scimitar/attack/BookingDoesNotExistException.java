package be.deschutter.scimitar.attack;

public class BookingDoesNotExistException extends RuntimeException {
    private final int x;
    private final int y;
    private final int z;
    private final long landingTick;

    public BookingDoesNotExistException(final int x, final int y, final int z,
        final long landingTick) {
        super(String
            .format("Could not found booking on %d:%d:%d for landtick %d", x, y,
                z, landingTick));
        this.x = x;
        this.y = y;
        this.z = z;
        this.landingTick = landingTick;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getLandingTick() {
        return landingTick;
    }
}
