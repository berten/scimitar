package be.deschutter.scimitar.ticker;

public class NoTickFoundException extends RuntimeException {
    public NoTickFoundException() {
        super("No tick was found in the database");
    }
}
