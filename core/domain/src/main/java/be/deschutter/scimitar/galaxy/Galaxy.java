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

    @Column(nullable = true)
    private int dayScoreGrowth = 0;
    @Column(nullable = true)
    private int dayValueGrowth = 0;
    @Column(nullable = true)
    private int daySizeGrowth = 0;
    @Column(nullable = true)
    private int dayXpGrowth = 0;
    @Column(nullable = true)
    private int dayPlanetsGrowth = 0;

    @Column(nullable = true)
    private int scoreGrowth = 0;
    @Column(nullable = true)
    private int valueGrowth = 0;
    @Column(nullable = true)
    private int sizeGrowth = 0;
    @Column(nullable = true)
    private int xpGrowth = 0;
    @Column(nullable = true)
    private int planetsGrowth = 0;

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

    public int getDayScoreGrowth() {
        return dayScoreGrowth;
    }

    public void setDayScoreGrowth(int dayScoreGrowth) {
        this.dayScoreGrowth = dayScoreGrowth;
    }

    public int getDayValueGrowth() {
        return dayValueGrowth;
    }

    public void setDayValueGrowth(int dayValueGrowth) {
        this.dayValueGrowth = dayValueGrowth;
    }

    public int getDaySizeGrowth() {
        return daySizeGrowth;
    }

    public void setDaySizeGrowth(int daySizeGrowth) {
        this.daySizeGrowth = daySizeGrowth;
    }

    public int getDayXpGrowth() {
        return dayXpGrowth;
    }

    public void setDayXpGrowth(int dayXpGrowth) {
        this.dayXpGrowth = dayXpGrowth;
    }

    public int getDayPlanetsGrowth() {
        return dayPlanetsGrowth;
    }

    public void setDayPlanetsGrowth(int dayPlanetsGrowth) {
        this.dayPlanetsGrowth = dayPlanetsGrowth;
    }

    public int getScoreGrowth() {
        return scoreGrowth;
    }

    public void setScoreGrowth(int scoreGrowth) {
        this.scoreGrowth = scoreGrowth;
    }

    public int getValueGrowth() {
        return valueGrowth;
    }

    public void setValueGrowth(int valueGrowth) {
        this.valueGrowth = valueGrowth;
    }

    public int getSizeGrowth() {
        return sizeGrowth;
    }

    public void setSizeGrowth(int sizeGrowth) {
        this.sizeGrowth = sizeGrowth;
    }

    public int getXpGrowth() {
        return xpGrowth;
    }

    public void setXpGrowth(int xpGrowth) {
        this.xpGrowth = xpGrowth;
    }

    public int getPlanetsGrowth() {
        return planetsGrowth;
    }

    public void setPlanetsGrowth(int planetsGrowth) {
        this.planetsGrowth = planetsGrowth;
    }
}
