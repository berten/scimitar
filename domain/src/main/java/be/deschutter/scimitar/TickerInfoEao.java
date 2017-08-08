package be.deschutter.scimitar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerInfoEao extends JpaRepository<TickerInfo, Long> {
    TickerInfo findByTick(Long tick);
}
