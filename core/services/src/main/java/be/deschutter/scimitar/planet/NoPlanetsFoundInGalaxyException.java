package be.deschutter.scimitar.planet;

public class NoPlanetsFoundInGalaxyException extends RuntimeException {
    private final int x;
    private final int y;

    NoPlanetsFoundInGalaxyException(final int x, final int y) {
        super(String.format("No planets were found in galaxy %d:%d", x, y));
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
