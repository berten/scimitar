package be.deschutter.scimitar.attack;

public class NotAuthorisedToChangeBookingException extends RuntimeException {
    private final int x;
    private final int y;
    private final int z;
    private final long landingTick;

    public NotAuthorisedToChangeBookingException(final int x, final int y,
        final int z, final long landingTick) {
        super(String.format(
            "You are not allowed to change the bookinginformation for %d:%d:%d LT: %d",
            x, y, z, landingTick));
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
