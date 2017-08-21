package be.deschutter.scimitar.planetarion;

public interface Listener {

    String getCommand();

    String getPattern();


    String getResult(String username, String... parameters);

    default String getErrorMessage() {
        return "Error: use following pattern for command " + getCommand() + ": "
                + getPattern();
    }
}
