package be.deschutter.scimitar.scans;

import be.deschutter.scimitar.user.ScimitarUser;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ScanRequest {
    @Id
    @GeneratedValue
    private Long id;

    private String planetId;
    private long tick;
    @Enumerated(EnumType.STRING)
    private ScanType scanType;
    @ManyToOne
    private ScimitarUser requestedBy;

    private String scanId;

    private boolean delivered = false;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getPlanetId() {
        return planetId;
    }

    public void setPlanetId(final String planetId) {
        this.planetId = planetId;
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

    public ScimitarUser getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(final ScimitarUser requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(final String scanId) {
        this.scanId = scanId;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(final boolean delivered) {
        this.delivered = delivered;
    }
}
