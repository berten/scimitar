package be.deschutter.scimitar.scans;

public class ScanNotFoundException extends RuntimeException {
    private final int x;
    private final int y;
    private final int z;
    private final ScanType scanType;

    public ScanNotFoundException(final int x, final int y, final int z,
        final ScanType scanType) {
        super(String
            .format("No scan of type %s found for %d:%d:%d", scanType.name(), x,
                y, z));
        this.x = x;
        this.y = y;
        this.z = z;
        this.scanType = scanType;
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

    public ScanType getScanType() {
        return scanType;
    }
}
