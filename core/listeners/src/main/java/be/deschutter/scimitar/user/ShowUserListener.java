package be.deschutter.scimitar.user;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HC')")
public class ShowUserListener implements Listener {
    private final ScimitarUserEao scimitarUserEao;

    @Autowired
    public ShowUserListener(final ScimitarUserEao scimitarUserEao) {
        this.scimitarUserEao = scimitarUserEao;
    }

    @Override
    public String getCommand() {
        return "showuser";
    }

    @Override
    public String getPattern() {
        return "<nickname>";
    }

    @Override
    public String getResult(final String username, final String... parameters) {
        if (null == parameters || parameters.length == 0) {
            final ScimitarUser u = scimitarUserEao
                .findByUsernameIgnoreCase(username);

            return returnString(u);
        } else {
            final ScimitarUser u = scimitarUserEao
                .findByUsernameIgnoreCase(parameters[0]);
            if (null != u)
                return returnString(u);
            else
                return "User " + parameters[0] + " does not exist";
        }
    }

    private String returnString(final ScimitarUser u) {
        return "User roles for username " + u.getUsername() + ": " + u
            .getRolesAsString();
    }
}
