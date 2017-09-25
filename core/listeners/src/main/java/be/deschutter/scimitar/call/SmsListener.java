package be.deschutter.scimitar.call;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component

public class SmsListener implements Listener {

    private final ScimitarUserEao scimitarUserEao;
    private final ClickatellSender clickatellSender;

    @Autowired
    public SmsListener(final ScimitarUserEao scimitarUserEao,
        final ClickatellSender clickatellSender) {
        this.scimitarUserEao = scimitarUserEao;
        this.clickatellSender = clickatellSender;
    }

    @Override
    public String getCommand() {
        return "sms";
    }

    @Override
    public String getPattern() {
        return "username text";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String username, final String... parameters) {
        if (parameters.length < 2)
            return getErrorMessage();
        else {
            final ScimitarUser user = scimitarUserEao
                .findByUsernameIgnoreCase(parameters[0]);
            if (user == null) {
                return String.format("User %s does not exist", parameters[0]);
            }
            if (user.getPhoneNumber() == null || user.getPhoneNumber()
                .isEmpty()) {
                return String
                    .format("User %s does not have a phonenumber added",
                        user.getUsername());
            }

            final ScimitarUser loggedInUser = scimitarUserEao
                .findByUsernameIgnoreCase(username);

            try {
                final String messageToSend = createMessage(loggedInUser,
                    parameters);
                clickatellSender
                    .sendMessage(user.getPhoneNumber(), messageToSend);

                return String.format("Message %s sent to %s", messageToSend,
                    user.getUsername());

            } catch (Exception e) {
                e.printStackTrace();
                return getErrorMessage();
            }

        }

    }

    private String createMessage(final ScimitarUser loggedInUser,
        final String[] parameters) {
        return String.format("%s - %s/%s", String.join(" ",
            Arrays.asList(parameters).subList(1, parameters.length)),
            loggedInUser.getUsername(), loggedInUser.getPhoneNumber());
    }
}
