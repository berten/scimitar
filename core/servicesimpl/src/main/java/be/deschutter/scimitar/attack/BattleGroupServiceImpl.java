package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.BattleGroup;
import be.deschutter.scimitar.user.BattleGroupEao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BattleGroupServiceImpl implements BattleGroupService {
    private final BattleGroupEao battleGroupEao;

    @Autowired
    public BattleGroupServiceImpl(final BattleGroupEao battleGroupEao) {
        this.battleGroupEao = battleGroupEao;
    }

    @Override
    public BattleGroup findByName(final String name) {
        final BattleGroup bg = battleGroupEao.findByNameIgnoreCase(name);
        if (bg == null)
            throw new BattleGroupDoesNotExistException(name);
        return bg;
    }

    @Override
    public void delete(final BattleGroup bg) {
        battleGroupEao.delete(bg);
    }

    @Override
    public BattleGroup save(final BattleGroup battleGroup) {
        return battleGroupEao.save(battleGroup);
    }
}
