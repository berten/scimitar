package be.deschutter.scimitar.intel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IntelEao extends JpaRepository<Intel,String> {
    Intel findByPlanetId(String planetId);
}
