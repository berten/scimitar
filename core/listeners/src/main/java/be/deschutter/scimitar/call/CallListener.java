package be.deschutter.scimitar.call;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component

public class CallListener implements Listener {
    private final UserService userService;

    private final TaskExecutor taskExecutor;
    @Value("${twilio.account.sid}")
    private String twilioAcountSid;

    @Value("${twilio.account.authtoken}")
    private String twilioAuthToken;

    @Value("${twilio.account.phonenumber}")
    private String twilioPhoneNumber;

    @Autowired
    public CallListener(final UserService userService,
        final TaskExecutor taskExecutor) {
        this.userService = userService;
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
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {
        if (parameters.length == 1) {
            final ScimitarUser user = userService.findBy(parameters[0]);

            if (user.getPhoneNumber() == null || user.getPhoneNumber()
                .isEmpty()) {
                return "User " + parameters[0]
                    + " does not have a phonenumber added";
            }

            taskExecutor.execute(
                new CallRunner(user.getPhoneNumber(), twilioAcountSid,
                    twilioAuthToken, twilioPhoneNumber));

            return "Call queued to " + user.getUsername();

        } else
            return getErrorMessage();
    }

}
