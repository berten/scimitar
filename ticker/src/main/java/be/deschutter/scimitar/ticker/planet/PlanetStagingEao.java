package be.deschutter.scimitar.ticker.planet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetStagingEao extends JpaRepository<PlanetStaging, PlanetStagingPk> {
}
