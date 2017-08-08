package be.deschutter.scimitar;

import javax.persistence.*;

@Entity
@IdClass(AlliancePk.class)
@Table(indexes = {@Index(name = "alliance_name_index", columnList = "alliancename", unique = false)
})
public class Alliance {
    private int countedScoreRank;
    @Id
    private String allianceName;
    @Id
    private long tick;
    private int size;
    private int members;
    private long countedScore;
    private long points;
    private long totalScore;
    private long totalValue;
    private int scoreRank;
    private int sizeRank;
    private int valueRank;
    private int pointsRank;

    public int getCountedScoreRank() {
        return countedScoreRank;
    }

    public void setCountedScoreRank(int countedScoreRank) {
        this.countedScoreRank = countedScoreRank;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public long getCountedScore() {
        return countedScore;
    }

    public void setCountedScore(long countedScore) {
        this.countedScore = countedScore;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(long totalValue) {
        this.totalValue = totalValue;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
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

    public int getPointsRank() {
        return pointsRank;
    }

    public void setPointsRank(int pointsRank) {
        this.pointsRank = pointsRank;
    }
}
