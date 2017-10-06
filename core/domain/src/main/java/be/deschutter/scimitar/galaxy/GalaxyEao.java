package be.deschutter.scimitar.galaxy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GalaxyEao extends JpaRepository<Galaxy, GalaxyPk> {
    Long countByTick(long tick);

    Galaxy findFirstByXAndYOrderByTickDesc(int x, int y);

    Galaxy findByXAndYAndTick(int x, int y, long tick);
}
