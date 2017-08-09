package be.deschutter.scimitar;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetEao extends JpaRepository<Planet, PlanetPk> {
    Long countByTick(long tick);

    Planet findFirstByXAndYAndZOrderByTickDesc(int x, int y, int z);
}
