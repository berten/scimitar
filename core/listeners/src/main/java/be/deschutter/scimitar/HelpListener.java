package be.deschutter.scimitar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Comparator;
import java.util.StringJoiner;

@Component
public class HelpListener implements Listener {

    private ApplicationContext context;
    private Collection<Listener> listeners;

    @PostConstruct
    public void loadListeners() {
        listeners = context.getBeansOfType(Listener.class).values();
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getPattern() {
        return "[command]";
    }

    @Override
    public String getResult(String... parameters) {

        if (parameters == null || parameters.length == 0) {
            StringJoiner joiner = new StringJoiner(", ");
            listeners.stream()
                .sorted(Comparator.comparing(Listener::getCommand))
                .forEach(listener -> {
                    try {
                        if(listener.hasAccess()) {
                            String command = listener.getCommand();
                            joiner.add(command);
                        }
                    } catch (AccessDeniedException exception) {
                        // We expect this to happen, these should not be shown in the !help list
                    }
                });
            return "List of commands: " + joiner.toString();
        } else if (parameters.length == 1) {
            final String command = parameters[0];
            String result = listeners.stream()
                .filter(listener -> listener.getCommand().equals(command))
                .map(Listener::getPattern).findFirst().orElse("");

            if (!"".equals(result)) {
                return "Pattern for command " + command + ": " + result;
            } else {
                return "Command " + command + " was not found";
            }
        } else
            return getErrorMessage();
    }

    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

}