package be.deschutter.scimitar.attack;

public class NotAuthorisedToAddUsersToBgTarget extends RuntimeException {
    private String battleGroupName;

    public NotAuthorisedToAddUsersToBgTarget(final String battleGroupName) {
        super(String.format("You are not allowed to assign players to targets booked by BG: %s",battleGroupName));
        this.battleGroupName = battleGroupName;
    }

    public String getBattleGroupName() {
        return battleGroupName;
    }
}
