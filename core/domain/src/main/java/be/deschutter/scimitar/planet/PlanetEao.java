package be.deschutter.scimitar.planet;

import be.deschutter.scimitar.TickerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanetEao extends JpaRepository<Planet, PlanetPk> {
    Long countByTick(long tick);

    Planet findFirstByXAndYAndZOrderByTickDesc(int x, int y, int z);

    List<Planet> findFirst15ByIdOrderByTickDesc(String planetId);

    Planet findByXAndYAndZAndTick(int x, int y, int z,
        long tick);

    Planet findByIdAndTick(String planetId, long tick);

    List<Planet> findByXAndYAndTick(int x, int y, long tick);
}
