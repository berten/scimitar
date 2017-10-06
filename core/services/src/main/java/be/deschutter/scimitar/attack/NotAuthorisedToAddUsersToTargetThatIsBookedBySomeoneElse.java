package be.deschutter.scimitar.attack;

public class NotAuthorisedToAddUsersToTargetThatIsBookedBySomeoneElse
    extends RuntimeException {
    private String username;

    public NotAuthorisedToAddUsersToTargetThatIsBookedBySomeoneElse(
        final String username) {
        super(String.format(
            "You are not allowed to assign users to targets that are booked by %s",
            username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
