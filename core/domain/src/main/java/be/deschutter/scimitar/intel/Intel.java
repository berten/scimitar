package be.deschutter.scimitar.intel;

import be.deschutter.scimitar.alliance.Alliance;
import be.deschutter.scimitar.planet.PlanetPk;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
@IdClass(PlanetPk.class)
public class Intel {
    @Id
    private String planetId;
    @OneToOne
    private Player player;
    @ManyToMany(cascade = CascadeType.ALL)
    private Alliance alliance;

    public Intel(final String planetId) {

        this.planetId = planetId;
    }

    public Intel() {
    }

    public String getPlanetId() {
        return planetId;
    }

    public void setPlanetId(final String planetId) {
        this.planetId = planetId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(final Alliance alliance) {
        this.alliance = alliance;
    }
}
