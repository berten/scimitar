package be.deschutter.scimitar;

import be.deschutter.scimitar.security.SecurityHelper;
import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegListener implements Listener {

    private final UserService userService;
    private SecurityHelper securityHelper;

    @Autowired
    public RegListener(final UserService userService,
        final SecurityHelper securityHelper) {
        this.userService = userService;
        this.securityHelper = securityHelper;
    }

    @Override
    public String getCommand() {
        return "reg";
    }

    @Override
    public String getPattern() {
        return "pnick";
    }

    @Override
    public String getResult(final String... parameters) {
        if(parameters.length == 1) {
            final ScimitarUser user = userService.findBy(parameters[0]);
            if (user.getSlackUsername() == null) {
                user.setSlackUsername(securityHelper.getAnonymousUserName());
                userService.save(user);
                return "pnick successfully added to slack profile";
            } else {
                return "A slack nick for this user was already added. Are you trying to hack me?";
            }
        } else
            return getErrorMessage();
    }
}
