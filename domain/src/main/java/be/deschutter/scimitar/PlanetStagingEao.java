package be.deschutter.scimitar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetStagingEao extends JpaRepository<PlanetStaging, String> {
}
