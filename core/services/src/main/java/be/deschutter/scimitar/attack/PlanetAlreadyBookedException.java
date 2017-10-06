package be.deschutter.scimitar.attack;

public class PlanetAlreadyBookedException extends RuntimeException {
    private final int x;
    private final int y;
    private final int z;
    private final long tick;
    private final String bookedBy;

    PlanetAlreadyBookedException(final int x, final int y, final int z,
        final long tick, final String bookedBy) {
        super(String
            .format("Target %d:%d:%d was already booked by %s for tick %d", x,
                y, z, bookedBy, tick));
        this.x = x;
        this.y = y;
        this.z = z;
        this.tick = tick;
        this.bookedBy = bookedBy;
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

    public long getTick() {
        return tick;
    }

    public String getBookedBy() {
        return bookedBy;
    }

}
