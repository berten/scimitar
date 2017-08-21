package be.deschutter.scimitar.user;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;

@Component
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HC')")
public class AddUserListener implements Listener {
    private final ScimitarUserEao scimitarUserEao;

    @Autowired
    public AddUserListener(final ScimitarUserEao scimitarUserEao) {
        this.scimitarUserEao = scimitarUserEao;
    }

    @Override
    public String getCommand() {
        return "adduser";
    }

    @Override
    public String getPattern() {
        return "nickname " + String.join("|", stream(RoleEnum.values())
            .filter(roleEnum -> roleEnum != RoleEnum.ANONYMOUS).map(Enum::name)
            .sorted(String::compareTo).collect(Collectors.toList()));

    }

    @Override
    @Transactional
    public String getResult(final String username, final String... parameters) {
        if (parameters.length == 0)

            return getErrorMessage();
        else if (parameters.length == 1) {
            final String usernameToAdd = parameters[0];
            final ScimitarUser user = scimitarUserEao
                .findByUsernameIgnoreCase(usernameToAdd);
            if (user != null)
                return userExistsMessage(user);
            else {
                ScimitarUser u = new ScimitarUser();
                u.setUsername(usernameToAdd);
                u.addRole(RoleEnum.MEMBER);
                u = scimitarUserEao.saveAndFlush(u);
                return makeReturnString(u);
            }
        } else {
            final String usernameToAdd = parameters[0];
            final ScimitarUser user = scimitarUserEao
                .findByUsernameIgnoreCase(usernameToAdd);
            if (user != null)
                return userExistsMessage(user);
            else {
                ScimitarUser u = new ScimitarUser();
                u.setUsername(usernameToAdd);
                IntStream.range(1, parameters.length).filter(
                    i -> stream(RoleEnum.values()).map(Enum::name)
                        .collect(Collectors.toList()).contains(parameters[i]))
                    .mapToObj(i -> RoleEnum.valueOf(parameters[i]))
                    .forEach(u::addRole);
                u = scimitarUserEao.saveAndFlush(u);
                return makeReturnString(u);
            }

        }
    }

    private String userExistsMessage(final ScimitarUser user) {
        return String.format(
            "User %s already exists with roles: %s. Use !changeuser to change this user's access",
            user.getUsername(), user.getRolesAsString());
    }

    private String makeReturnString(final ScimitarUser u) {
        return String.format("User %s added with roles: %s", u.getUsername(),
            String.join(",",
                u.getRoles().stream().map(role -> role.getRole().name())
                    .sorted(String::compareTo).collect(Collectors.toList())));
    }
}
