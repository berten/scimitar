package be.deschutter.scimitar;

public interface Listener {

    String getCommand();

    String getPattern();


    String getResult(String... parameters);

    default String getErrorMessage() {
        return "Error: use following pattern for command " + getCommand() + ": "
                + getPattern();
    }

    default boolean hasAccess() {
        return true;
    }
}
