package be.deschutter.scimitar.planetarion;

public interface Listener {

    String getCommand();

    String getPattern();

    String getResult(String... parameters);
}
