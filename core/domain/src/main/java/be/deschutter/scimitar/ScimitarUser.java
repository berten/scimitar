package be.deschutter.scimitar;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ScimitarUser {
    @Id
    @GeneratedValue
    private Integer id;
    @OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(final String role) {
        if(roles == null)
            roles = new HashSet<>();
        roles.add(new Role(role));
    }
}
