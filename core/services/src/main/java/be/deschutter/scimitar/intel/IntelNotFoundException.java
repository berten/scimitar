package be.deschutter.scimitar.intel;

public class IntelNotFoundException extends RuntimeException {

    private final int x;
    private final int y;
    private final int z;

    public IntelNotFoundException(int x, int y, int z) {
        super(String.format("No intel found for planet %d:%d:%d",x,y,z));
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
