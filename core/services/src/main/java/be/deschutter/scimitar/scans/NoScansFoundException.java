package be.deschutter.scimitar.scans;

public class NoScansFoundException extends RuntimeException {
    private final int x;
    private final int y;
    private final int z;

    public NoScansFoundException(final int x, final int y, final int z) {
        super(String.format("No scans found for %d:%d:%d", x, y, z));
        this.x = x;
        this.y = y;
        this.z = z;
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
}
