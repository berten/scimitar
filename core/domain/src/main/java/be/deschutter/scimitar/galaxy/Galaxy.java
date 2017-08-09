package be.deschutter.scimitar.galaxy;

import javax.persistence.*;

@Entity
@IdClass(GalaxyPk.class)
@Table(indexes = {@Index(name="galaxy_x_index",columnList="x",unique = false),
        @Index(name="galaxy_y_index",columnList="y",unique = false),
        @Index(name="galaxy_xytick_index",columnList="x,y,tick",unique = false),
        @Index(name="galaxy_tick_index",columnList="tick",unique = false)})
public class Galaxy {
    @Id
    private long tick;
    @Id
    private int x;
    @Id
    private int y;
    private String galaxyName;
    private int size;
    private long score;
    private long value;
    private long xp;
    private int scoreRank;
    private int sizeRank;
    private int valueRank;
    private int xpRank;
    @Column(nullable = true)
    private int planets;

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public String getGalaxyName() {
        return galaxyName;
    }

    public void setGalaxyName(String galaxyName) {
        this.galaxyName = galaxyName;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public int getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(int scoreRank) {
        this.scoreRank = scoreRank;
    }

    public int getSizeRank() {
        return sizeRank;
    }

    public void setSizeRank(int sizeRank) {
        this.sizeRank = sizeRank;
    }

    public int getValueRank() {
        return valueRank;
    }

    public void setValueRank(int valueRank) {
        this.valueRank = valueRank;
    }

    public int getPlanets() {
        return planets;
    }

    public void setPlanets(int planets) {
        this.planets = planets;
    }

    public int getXpRank() {
        return xpRank;
    }

    public void setXpRank(int xpRank) {
        this.xpRank = xpRank;
    }
}
