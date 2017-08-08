package be.deschutter.scimitar;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class TickerInfo {
    @Id
    private long tick;
    private Long processingTimePlanets;
    private Long processingTimeGalaxies;
    private Long processingTimeAlliances;
    private int planets;
    private int galaxies;
    private int alliances;
    private LocalDateTime tickTime;

    public TickerInfo(final long tick) {
        this.tick = tick;
    }

    private TickerInfo() {

    }

    public long getTick() {
        return tick;
    }

    public void setTick(final long tick) {
        this.tick = tick;
    }

    public Long getProcessingTimePlanets() {
        return processingTimePlanets;
    }

    public void setProcessingTimePlanets(final Long processingTimePlanets) {
        this.processingTimePlanets = processingTimePlanets;
    }

    public int getPlanets() {
        return planets;
    }

    public void setPlanets(final int planets) {
        this.planets = planets;
    }

    public int getGalaxies() {
        return galaxies;
    }

    public void setGalaxies(final int galaxies) {
        this.galaxies = galaxies;
    }

    public int getAlliances() {
        return alliances;
    }

    public void setAlliances(final int alliances) {
        this.alliances = alliances;
    }

    public Long getProcessingTimeAlliances() {
        return processingTimeAlliances;
    }

    public void setProcessingTimeAlliances(final Long processingTimeAlliances) {
        this.processingTimeAlliances = processingTimeAlliances;
    }

    public Long getProcessingTimeGalaxies() {
        return processingTimeGalaxies;
    }

    public void setProcessingTimeGalaxies(final Long processingTimeGalaxies) {
        this.processingTimeGalaxies = processingTimeGalaxies;
    }

    public LocalDateTime getTickTime() {
        return tickTime;
    }

    public void setTickTime(final LocalDateTime tickTime) {
        this.tickTime = tickTime;
    }
}
