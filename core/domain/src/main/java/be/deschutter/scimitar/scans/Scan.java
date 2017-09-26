package be.deschutter.scimitar.scans;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Scan {
    @Id
    private String id;
    private String planetId;

    private long tick;
    @Enumerated(EnumType.STRING)
    private ScanType scanType;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(final long tick) {
        this.tick = tick;
    }

    public ScanType getScanType() {
        return scanType;
    }

    public void setScanType(final ScanType scanType) {
        this.scanType = scanType;
    }

    public String getPlanetId() {
        return planetId;
    }

    public void setPlanetId(final String planetId) {
        this.planetId = planetId;
    }
}
