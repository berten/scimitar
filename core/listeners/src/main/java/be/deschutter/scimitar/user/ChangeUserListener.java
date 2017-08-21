package be.deschutter.scimitar.user;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.RoleEnum;
import be.deschutter.scimitar.ScimitarUser;
import be.deschutter.scimitar.ScimitarUserEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

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
        StringJoiner joiner = new StringJoiner(", ");

        return "nickname add|remove " + String.join("|",
            stream(RoleEnum.values())
                .filter(roleEnum -> roleEnum != RoleEnum.ANONYMOUS)
                .map(Enum::name).collect(Collectors.toList()));
    }

    @Override
    public String getResult(final String username, final String... parameters) {
        if (parameters.length < 3)
            return getErrorMessage();
        else {
            final ScimitarUser user = scimitarUserEao
                .findByUsernameIgnoreCase(parameters[0]);
            if (user == null)
                return "Error: Unknown user " + parameters[0];
            else
                switch (parameters[1].toUpperCase()) {
                case "ADD":
                    for (int i = 2; i < parameters.length; i++) {
                        if (stream(RoleEnum.values()).map(Enum::name)
                            .collect(Collectors.toList())
                            .contains(parameters[i])) {
                            user.addRole(RoleEnum.valueOf(parameters[i]));
                        }
                    }
                    break;
                case "REMOVE":
                    for (int i = 2; i < parameters.length; i++) {
                        if (stream(RoleEnum.values()).map(Enum::name)
                            .collect(Collectors.toList())
                            .contains(parameters[i])) {
                            user.removeRole(RoleEnum.valueOf(parameters[i]));

                        }
                    }
                    break;
                default:
                    return getErrorMessage();
                }
            scimitarUserEao.saveAndFlush(user);
            return String.format("New access for users %s: %s", parameters[0],
                String.join(",",
                    user.getRoles().stream().map(role -> role.getRole().name())
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())));
        }
    }
}
