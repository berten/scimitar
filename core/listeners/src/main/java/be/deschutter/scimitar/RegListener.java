package be.deschutter.scimitar;

import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegListener implements Listener {

    private final ScimitarUserEao scimitarUserEao;

    @Autowired
    public RegListener(final ScimitarUserEao scimitarUserEao) {
        this.scimitarUserEao = scimitarUserEao;
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
    public String getResult(final String username, final String... parameters) {
        if(parameters.length == 1) {
            final ScimitarUser user = scimitarUserEao.findByUsernameIgnoreCase(parameters[0]);
            if (user.getSlackUsername() == null) {
                user.setSlackUsername(username);
                scimitarUserEao.saveAndFlush(user);
                return "pnick successfully added to slack profile";
            } else {
                return "A slack nick for this user was already added. Are you trying to hack me?";
            }
        } else
            return getErrorMessage();
    }
}
