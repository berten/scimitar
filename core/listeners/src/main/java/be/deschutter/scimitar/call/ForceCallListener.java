package be.deschutter.scimitar.call;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@PreAuthorize("hasAnyAuthority('ROLE_HC','ROLE_BC','ROLE_ADMIN')")
public class ForceCallListener implements Listener {

    private final TaskExecutor taskExecutor;
    @Value("${twilio.account.sid}")
    private String twilioAcountSid;

    @Value("${twilio.account.authtoken}")
    private String twilioAuthToken;

    @Value("${twilio.account.phonenumber}")
    private String twilioPhoneNumber;

    @Autowired
    public ForceCallListener(final TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public String getCommand() {
        return "forcecall";
    }

    @Override
    public String getPattern() {
        return "number";
    }

    @Override
    public String getResult(final String username, final String... parameters) {
        if (parameters.length == 1) {

            taskExecutor.execute(
                new CallRunner(parameters[0], twilioAcountSid, twilioAuthToken,
                    twilioPhoneNumber));

            return "Call queued to " + parameters[0];

        } else
            return getErrorMessage();
    }

}
