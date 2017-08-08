package be.deschutter.scimitar;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AllianceEao extends JpaRepository<Alliance, AlliancePk> {
    Long countByTick(long tick);
}
