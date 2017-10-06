package be.deschutter.scimitar.planet;

import java.util.List;

public interface PlanetService {

    Planet findBy(int x, int y, int z);

    List<Planet> findBy(int x, int y);

    Planet findBy(String planetId);

    List<Planet> findFirst15ByXAndYAndZOrderByTickDesc(int x, int y, int z);
}
