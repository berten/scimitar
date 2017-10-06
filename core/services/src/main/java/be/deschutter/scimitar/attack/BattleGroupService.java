package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.BattleGroup;

public interface BattleGroupService {
    BattleGroup findByName(final String name);

    void delete(BattleGroup bg);

    BattleGroup save(BattleGroup battleGroup);
}
