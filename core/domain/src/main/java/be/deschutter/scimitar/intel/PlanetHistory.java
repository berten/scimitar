package be.deschutter.scimitar.intel;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class PlanetHistory {
    @GeneratedValue
    @Id
    private Integer id;
    private String planetName;
    private String rulerName;
    private String alliance;
    @OneToMany
    private Player player;
    private Long score;
    private Long size;
    private Long value;
    private Long xp;
    private Long rank;
    private Long round;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(final String planetName) {
        this.planetName = planetName;
    }

    public String getRulerName() {
        return rulerName;
    }

    public void setRulerName(final String rulerName) {
        this.rulerName = rulerName;
    }

    public String getAlliance() {
        return alliance;
    }

    public void setAlliance(final String alliance) {
        this.alliance = alliance;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(final Long score) {
        this.score = score;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(final Long size) {
        this.size = size;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(final Long value) {
        this.value = value;
    }

    public Long getXp() {
        return xp;
    }

    public void setXp(final Long xp) {
        this.xp = xp;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(final Long rank) {
        this.rank = rank;
    }

    public Long getRound() {
        return round;
    }

    public void setRound(final Long round) {
        this.round = round;
    }
}
