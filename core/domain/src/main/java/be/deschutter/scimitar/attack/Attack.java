package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.BattleGroup;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Attack {
    @Id
    @GeneratedValue
    private Integer id;

    @ElementCollection
    private Set<String> planetIds = new HashSet<>();

    private Integer waves;
    private Integer landTick;
    @ManyToOne
    private BattleGroup battleGroup;

    private String comment;

    private AttackStatus status = AttackStatus.NEW;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Set<String> getPlanetIds() {
        return planetIds;
    }

    public void setPlanetIds(final Set<String> planetIds) {
        this.planetIds = planetIds;
    }

    public Integer getWaves() {
        return waves;
    }

    public void setWaves(final Integer waves) {
        this.waves = waves;
    }

    public Integer getLandTick() {
        return landTick;
    }

    public void setLandTick(final Integer landTick) {
        this.landTick = landTick;
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }

    public void setBattleGroup(final BattleGroup battleGroup) {
        this.battleGroup = battleGroup;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public AttackStatus getStatus() {
        return status;
    }

    public void setStatus(final AttackStatus status) {
        this.status = status;
    }

    public void addPlanet(final String planetId) {
        planetIds.add(planetId);
    }

    public void removePlanet(final String planetId) {
        planetIds.remove(planetId);
    }
}
