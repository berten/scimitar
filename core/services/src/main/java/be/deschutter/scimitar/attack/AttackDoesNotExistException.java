package be.deschutter.scimitar.attack;

public class AttackDoesNotExistException extends RuntimeException {
    private int attackId;

    public AttackDoesNotExistException(final int attackId) {
        super("Could not find attack with id " + attackId);
        this.attackId = attackId;
    }

    public int getAttackId() {
        return attackId;
    }
}
