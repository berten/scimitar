package be.deschutter.scimitar.galaxy;

public class GalaxyDoesNotExistException extends RuntimeException {
    private final int x;
    private final int y;

    public GalaxyDoesNotExistException(final int x, final int y) {
        super(String.format("Galaxy %d:%d does not exist", x, y));
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
