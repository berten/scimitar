package be.deschutter.scimitar.user;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component

public class ShowUserListener implements Listener {
    private final UserService userService;
    private SecurityHelper securityHelper;

    @Autowired
    public ShowUserListener(final UserService userService,
        final SecurityHelper securityHelper) {
        this.userService = userService;
        this.securityHelper = securityHelper;
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HC')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_HC')")
    public String getResult(final String... parameters) {
        if (null == parameters || parameters.length == 0) {
            return returnString(securityHelper.getLoggedInUser());
        } else {
            return returnString(userService.findBy(parameters[0]));
        }
    }

    private String returnString(final ScimitarUser u) {
        return "User roles for username " + u.getUsername() + ": " + u
            .getRolesAsString();
    }
}
