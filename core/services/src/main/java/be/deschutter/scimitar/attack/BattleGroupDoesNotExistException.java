package be.deschutter.scimitar.attack;

public class BattleGroupDoesNotExistException extends RuntimeException {
    private String battlegroupName;

    public BattleGroupDoesNotExistException(final String battlegroupName) {
        super(String.format("Battlegroup %s does not exist",battlegroupName));
        this.battlegroupName = battlegroupName;
    }

    public String getBattlegroupName() {
        return battlegroupName;
    }
}
