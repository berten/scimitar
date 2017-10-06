package be.deschutter.scimitar.intel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Player {
    @Id
    @GeneratedValue
    private Integer id;

    private Set<String> nicks = new HashSet<>();

    private String phoneNumber;
    private String email;
    private List<String> notes = new ArrayList<>();
    @ManyToOne
    private List<PlanetHistory> planetHistories = new ArrayList<>();

    public Player(final String nick) {
        addNick(nick);
    }

    public Player() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Set<String> getNicks() {
        return nicks;
    }

    public void setNicks(final Set<String> nicks) {
        this.nicks = nicks;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(final List<String> notes) {
        this.notes = notes;
    }

    public List<PlanetHistory> getPlanetHistories() {
        return planetHistories;
    }

    public void setPlanetHistories(final List<PlanetHistory> planetHistories) {
        this.planetHistories = planetHistories;
    }

    public void addNick(final String nick) {
        nicks.add(nick);
    }
}
