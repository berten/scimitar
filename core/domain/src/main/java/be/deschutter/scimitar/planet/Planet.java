package be.deschutter.scimitar.planet;

import javax.persistence.*;

@Entity
@IdClass(PlanetPk.class)
@Table(indexes = {@Index(name="planet_x_index",columnList="x",unique = false),
        @Index(name="planet_y_index",columnList="y",unique = false),
        @Index(name="planet_xytick_index",columnList="x,y,tick",unique = false),
        @Index(name="planet_tick_index",columnList="tick",unique = false)})
public class Planet {
    @Id
    private long tick;
    @Id
    private String id;
    private int x;
    private int y;
    private int z;
    private String planetName;
    private String rulerName;
    private String race;
    private int size;
    private long score;
    private long value;
    private long xp;
    private String special;
    private int scoreRank;
    private int sizeRank;
    private int valueRank;
    private int xpRank;

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

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public String getRulerName() {
        return rulerName;
    }

    public void setRulerName(String rulerName) {
        this.rulerName = rulerName;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
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

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public int getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(int scoreRank) {
        this.scoreRank = scoreRank;
    }

    public int getXpRank() {
        return xpRank;
    }

    public void setXpRank(final int xpRank) {
        this.xpRank = xpRank;
    }
}
