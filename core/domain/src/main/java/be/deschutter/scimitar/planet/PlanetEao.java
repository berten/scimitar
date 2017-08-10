package be.deschutter.scimitar.planet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanetEao extends JpaRepository<Planet, PlanetPk> {
    Long countByTick(long tick);

    Planet findFirstByXAndYAndZOrderByTickDesc(int x, int y, int z);

    List<Planet> findFirst15ByXAndYAndZOrderByTickDesc(int x, int y, int z);
}
