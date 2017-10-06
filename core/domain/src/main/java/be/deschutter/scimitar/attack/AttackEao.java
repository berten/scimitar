package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.BattleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AttackEao extends JpaRepository<Attack,Integer> {
    List<Attack> findLast5ByOrderByLandTickDesc();

    List<Attack> findLast5ByBattleGroupIsNullOrBattleGroupInOrderByLandTickDesc(
        Set<BattleGroup> battleGroups);
}
