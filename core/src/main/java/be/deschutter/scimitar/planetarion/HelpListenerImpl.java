package be.deschutter.scimitar.planetarion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class HelpListenerImpl implements Listener {

    private ApplicationContext context;
    private Collection<Listener> listeners;

    @PostConstruct
    public void letsTest() {
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
        if (parameters == null)
            return "List of commands: " + listeners.stream().map(Listener::getCommand).collect(Collectors.joining(", "));
        else if (parameters.length == 1)
            return "Pattern for command " + parameters[0] + ": " + listeners.stream().filter(listener -> listener.getCommand().equals(parameters[0])).map(Listener::getPattern).collect(Collectors.joining());
        else
            return "Error: use following pattern for command " + getCommand() + ": " + getPattern();
    }


    @Autowired
    public void context(ApplicationContext context) {
        this.context = context;
    }

}