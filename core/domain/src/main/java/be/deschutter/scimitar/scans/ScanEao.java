package be.deschutter.scimitar.scans;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanEao extends JpaRepository<Scan, String> {
    Scan findFirstByPlanetIdAndScanTypeOrderByTickDesc(String planetId,
        ScanType scanType);

    List<Scan> findFirst5ByPlanetIdOrderByTickDesc(String planetId);
}
