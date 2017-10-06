package be.deschutter.scimitar.alliance;

import javax.persistence.*;

@Entity
@IdClass(AlliancePk.class)
@Table(indexes = {@Index(name = "alliance_name_index", columnList = "alliancename", unique = false)
})
public class Alliance {
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

    private int countedScoreRank;
    private int scoreRank;
    private int sizeRank;
    private int valueRank;
    private int pointsRank;
    private int membersRank;

    @Column(nullable = true)
    private int dayCountedScoreGrowth = 0;
    @Column(nullable = true)
    private int dayScoreGrowth = 0;
    @Column(nullable = true)
    private int dayValueGrowth = 0;
    @Column(nullable = true)
    private int daySizeGrowth = 0;
    @Column(nullable = true)
    private int dayPointsGrowth = 0;
    @Column(nullable = true)
    private int dayMembersGrowth = 0;

    @Column(nullable = true)
    private int countedScoreGrowth = 0;
    @Column(nullable = true)
    private int scoreGrowth = 0;
    @Column(nullable = true)
    private int valueGrowth = 0;
    @Column(nullable = true)
    private int sizeGrowth = 0;
    @Column(nullable = true)
    private int pointsGrowth = 0;
    @Column(nullable = true)
    private int membersGrowth = 0;

    public Alliance(final String allianceName) {
        this.allianceName = allianceName;
    }

    public Alliance() {
    }

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

    public int getMembersRank() {
        return membersRank;
    }

    public void setMembersRank(int membersRank) {
        this.membersRank = membersRank;
    }

    public int getDayCountedScoreGrowth() {
        return dayCountedScoreGrowth;
    }

    public void setDayCountedScoreGrowth(int dayCountedScoreGrowth) {
        this.dayCountedScoreGrowth = dayCountedScoreGrowth;
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

    public int getDayPointsGrowth() {
        return dayPointsGrowth;
    }

    public void setDayPointsGrowth(int dayPointsGrowth) {
        this.dayPointsGrowth = dayPointsGrowth;
    }

    public int getDayMembersGrowth() {
        return dayMembersGrowth;
    }

    public void setDayMembersGrowth(int dayMembersGrowth) {
        this.dayMembersGrowth = dayMembersGrowth;
    }

    public int getCountedScoreGrowth() {
        return countedScoreGrowth;
    }

    public void setCountedScoreGrowth(int countedScoreGrowth) {
        this.countedScoreGrowth = countedScoreGrowth;
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

    public int getPointsGrowth() {
        return pointsGrowth;
    }

    public void setPointsGrowth(int pointsGrowth) {
        this.pointsGrowth = pointsGrowth;
    }

    public int getMembersGrowth() {
        return membersGrowth;
    }

    public void setMembersGrowth(int membersGrowth) {
        this.membersGrowth = membersGrowth;
    }
}
