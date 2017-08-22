package be.deschutter.scimitar.user;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HC')")
public class ChangeUserListener implements Listener {

    private final ScimitarUserEao scimitarUserEao;

    @Autowired
    public ChangeUserListener(final ScimitarUserEao scimitarUserEao) {
        this.scimitarUserEao = scimitarUserEao;
    }

    @Override
    public String getCommand() {
        return "changeuser";
    }

    @Override
    public String getPattern() {

        return "nickname add|remove " + String.join("|",
            stream(RoleEnum.values())
                .filter(roleEnum -> roleEnum != RoleEnum.ANONYMOUS)
                .map(Enum::name).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public String getResult(final String username, final String... parameters) {
        if (parameters.length < 3)
            return getErrorMessage();
        else {
            ScimitarUser user = scimitarUserEao
                .findByUsernameIgnoreCase(parameters[0]);
            if (user == null)
                return "Error: Unknown user " + parameters[0];
            else
                switch (parameters[1].toUpperCase()) {
                case "ADD":
                    for (int i = 2; i < parameters.length; i++) {
                        final String role = parameters[i].toUpperCase();
                        if (stream(RoleEnum.values()).map(Enum::name)
                            .collect(Collectors.toList())
                            .contains(role)) {
                            user.addRole(RoleEnum.valueOf(
                                role));
                        }
                    }
                    break;
                case "REMOVE":
                    for (int i = 2; i < parameters.length; i++) {
                        final String role = parameters[i].toUpperCase();
                        if (stream(RoleEnum.values()).map(Enum::name)
                            .collect(Collectors.toList())
                            .contains(role)) {
                            user.removeRole(RoleEnum.valueOf(
                                role));

                        }
                    }
                    break;
                default:
                    return getErrorMessage();
                }
            user = scimitarUserEao.saveAndFlush(user);
            return String.format("New access for users %s: %s", parameters[0],
                String.join(",",
                    user.getRoles().stream().map(role -> role.getRole().name())
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())));
        }
    }
}
