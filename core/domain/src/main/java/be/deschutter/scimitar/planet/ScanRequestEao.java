package be.deschutter.scimitar.planet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScanRequestEao extends JpaRepository<ScanRequest,Integer>{

    List<ScanRequest> findByScanIdIsNull();
}
