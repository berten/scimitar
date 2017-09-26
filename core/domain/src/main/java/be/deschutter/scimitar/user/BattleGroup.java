package be.deschutter.scimitar.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BattleGroup {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ScimitarUser> scimitarUsers = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<ScimitarUser> getScimitarUsers() {
        return scimitarUsers;
    }

    public void setScimitarUsers(final Set<ScimitarUser> scimitarUsers) {
        this.scimitarUsers = scimitarUsers;
    }

    public void addUser(final ScimitarUser user) {
        scimitarUsers.add(user);
    }

    public void remUser(final ScimitarUser user) {
        scimitarUsers.remove(user);
    }
}
