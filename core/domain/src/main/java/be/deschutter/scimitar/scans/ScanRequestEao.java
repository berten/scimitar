package be.deschutter.scimitar.scans;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanRequestEao extends JpaRepository<ScanRequest,Integer>{

    List<ScanRequest> findByScanIdIsNull();
}
