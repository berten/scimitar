package be.deschutter.scimitar.user;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class ScimitarUser {
    @Id
    @GeneratedValue
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String planetId;
    private String slackUsername;
    @ManyToMany(mappedBy = "scimitarUsers")
    private Set<BattleGroup> battleGroups = new HashSet<>();

    public ScimitarUser(final String username) {

        this.username = username;
    }

    public ScimitarUser() {
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addRole(final RoleEnum role) {
        if (roles == null)
            roles = new HashSet<>();
        roles.add(new Role(role));
    }

    public void setPlanetId(final String planetId) {
        this.planetId = planetId;
    }

    public String getPlanetId() {
        return planetId;
    }

    public void removeRole(final RoleEnum role) {
        roles.stream().filter(role12 -> role12.getRole().equals(role))
            .findFirst().ifPresent(roles::remove);
    }

    public String getRolesAsString() {
        return String.join(",",
            roles.stream().map(role -> role.getRole().name())
                .sorted(String::compareTo).collect(Collectors.toList()));
    }

    public void setSlackUsername(final String slackUsername) {
        this.slackUsername = slackUsername;
    }

    public String getSlackUsername() {
        return slackUsername;
    }

    public Set<BattleGroup> getBattleGroups() {
        return battleGroups;
    }

    public void setBattleGroups(final Set<BattleGroup> battleGroups) {
        this.battleGroups = battleGroups;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final ScimitarUser that = (ScimitarUser) o;

        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    public boolean hasRole(final RoleEnum roleEnum) {
        return getRoles().stream().filter(role -> role.getRole() == roleEnum)
            .count() > 0;
    }

    public boolean isBc() {
        return getRoles().stream().map(Role::getRole)
            .collect(Collectors.toList()).contains(RoleEnum.BC);
    }
}
