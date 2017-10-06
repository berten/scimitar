package be.deschutter.scimitar.attack;

public class BookingNotJoinableException extends RuntimeException {
    private String username;

    public BookingNotJoinableException(final String username) {
        super(String.format("%s has chosen not to open this target for teamups",username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
