package be.deschutter.scimitar.attack;

import java.util.List;

public interface AttackService {
    default Attack createAttack(int ltEta, int waves) {
        return createAttack(ltEta, waves, null);
    }

    Attack createAttack(int ltEta, int waves, String battleGroupName);

    Attack addComment(int attackId, String comment);

    Attack findAttack(int attackId);

    Attack addTarget(int attackId, int x, int y, int z);

    default Attack addTarget(int attackId, int x, int y) {
        return addTarget(attackId, x, y, 0);
    }

    List<Attack> findLast5Attacks();

    Attack removeTarget(int attackId, int x, int y, int z);

    default Attack removeTarget(int attackId, int x, int y) {
        return removeTarget(attackId, x, y, 0);
    }

}
