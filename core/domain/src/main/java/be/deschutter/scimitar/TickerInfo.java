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
    private Long planets;
    private Long galaxies;
    private Long alliances;
    private LocalDateTime tickTime;

    public TickerInfo(final long tick) {
        this.tick = tick;
        tickTime = LocalDateTime.now();
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

    public Long getPlanets() {
        return planets;
    }

    public void setPlanets(Long planets) {
        this.planets = planets;
    }

    public Long getGalaxies() {
        return galaxies;
    }

    public void setGalaxies(Long galaxies) {
        this.galaxies = galaxies;
    }

    public Long getAlliances() {
        return alliances;
    }

    public void setAlliances(Long alliances) {
        this.alliances = alliances;
    }
}
