package be.deschutter.scimitar.call;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@PreAuthorize("hasAuthority('ROLE_MEMBER')")
public class CallListener implements Listener {
    private final ScimitarUserEao scimitarUserEao;

    private final TaskExecutor taskExecutor;
    @Value("${twilio.account.sid}")
    private String twilioAcountSid;

    @Value("${twilio.account.authtoken}")
    private String twilioAuthToken;

    @Value("${twilio.account.phonenumber}")
    private String twilioPhoneNumber;

    @Autowired
    public CallListener(final ScimitarUserEao scimitarUserEao,
        final TaskExecutor taskExecutor) {
        this.scimitarUserEao = scimitarUserEao;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public String getCommand() {
        return "call";
    }

    @Override
    public String getPattern() {
        return "username";
    }

    @Override
    public String getResult(final String username, final String... parameters) {
        if (parameters.length == 1) {
            final ScimitarUser user = scimitarUserEao
                .findByUsernameIgnoreCase(parameters[0]);
            if (user == null) {
                return "User " + parameters[0] + " does not exist";
            }
            if (user.getPhoneNumber() == null || user.getPhoneNumber()
                .isEmpty()) {
                return "User " + username
                    + " does not have a phonenumber added";
            }

            taskExecutor.execute(new CallRunner(user.getPhoneNumber(), twilioAcountSid,
                twilioAuthToken, twilioPhoneNumber));

            return "Call queued to " + user.getUsername();

        } else
            return getErrorMessage();
    }

}
