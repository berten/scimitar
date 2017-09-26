package be.deschutter.scimitar;

public class ScimitarNotification {
    private String username;
    private String message;

    public ScimitarNotification() {
    }

    public ScimitarNotification(final String username, final String message) {

        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ScimitarNotification{" + "username='" + username + '\''
            + ", message='" + message + '\'' + '}';
    }
}
