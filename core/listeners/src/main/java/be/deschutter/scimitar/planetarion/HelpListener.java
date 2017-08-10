package be.deschutter.scimitar.planetarion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

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
            return "List of commands: " + listeners.stream()
                .map(Listener::getCommand).collect(Collectors.joining(", "));
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