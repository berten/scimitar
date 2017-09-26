package be.deschutter.scimitar.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleGroupEao extends JpaRepository<BattleGroup, Integer> {
    BattleGroup findByNameIgnoreCase(String name);
}
