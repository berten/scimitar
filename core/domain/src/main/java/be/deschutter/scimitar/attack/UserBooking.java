package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.ScimitarUser;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("USER")
public class UserBooking extends Booking {
    private ScimitarUser bookedBy;
    private boolean joinable = false;

    public UserBooking(final ScimitarUser scimitarUser) {
        bookedBy = scimitarUser;
    }

    public UserBooking() {
    }

    public ScimitarUser getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(final ScimitarUser bookedBy) {
        this.bookedBy = bookedBy;
    }

    @Override
    public String getInfo() {
        return "User: " + bookedBy.getUsername();
    }

    public boolean isJoinable() {
        return joinable;
    }

    public void setJoinable(final boolean joinable) {
        this.joinable = joinable;
    }
}
