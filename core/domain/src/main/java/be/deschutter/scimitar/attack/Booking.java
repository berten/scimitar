package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.ScimitarUser;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorColumn(name = "BOOKING_TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Booking {
    @Id
    @GeneratedValue
    private Integer id;

    private String planetId;
    private long tick;
    private String bcalc;
    private String comment;
    private BookingStatus status = BookingStatus.LAUNCH;
    @ManyToMany
    private Set<ScimitarUser> users = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getPlanetId() {
        return planetId;
    }

    public void setPlanetId(final String planetId) {
        this.planetId = planetId;
    }

    public String getBcalc() {
        return bcalc;
    }

    public void setBcalc(final String bcalc) {
        this.bcalc = bcalc;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(final BookingStatus status) {
        this.status = status;
    }

    public Set<ScimitarUser> getUsers() {
        return users;
    }

    public void setUsers(final Set<ScimitarUser> users) {
        this.users = users;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(final long tick) {
        this.tick = tick;
    }

    public abstract String getInfo();

    public void addUser(final ScimitarUser user) {
        users.add(user);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public void removeUser(final ScimitarUser user) {
        users.remove(user);
    }
}
