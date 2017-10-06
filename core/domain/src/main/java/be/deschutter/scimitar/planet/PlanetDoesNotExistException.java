package be.deschutter.scimitar.planet;

public class PlanetDoesNotExistException extends RuntimeException {
    private final int x;
    private final int y;
    private final int z;

    public PlanetDoesNotExistException(final int x, final int y, final int z) {

        super(String.format("No planet was found for %d:%d:%d ", x, y, z));
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
