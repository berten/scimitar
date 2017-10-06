package be.deschutter.scimitar.attack;

public class CanNotJoinBattlegroupBookingWhenNotAMemberException
    extends RuntimeException {
    private String battleGroupName;

    public CanNotJoinBattlegroupBookingWhenNotAMemberException(
        final String battleGroupName) {
        super(String.format(
            "You can not join a booking that is booked by battlegroup %s",
            battleGroupName));
        this.battleGroupName = battleGroupName;
    }

    public String getBattleGroupName() {
        return battleGroupName;
    }
}
