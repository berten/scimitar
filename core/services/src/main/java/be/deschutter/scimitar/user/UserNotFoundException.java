package be.deschutter.scimitar.user;

public class UserNotFoundException extends RuntimeException {
    private String username;

    public UserNotFoundException(final String username) {
        super(String.format("User %s not found",username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
