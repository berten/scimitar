package be.deschutter.scimitar.attack;

public class NoAttacksFoundException extends RuntimeException {
    public NoAttacksFoundException() {
        super("No attacks found");
    }
}
