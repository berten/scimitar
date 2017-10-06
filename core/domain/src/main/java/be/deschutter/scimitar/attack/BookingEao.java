package be.deschutter.scimitar.attack;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingEao extends JpaRepository<Booking, Integer> {
    Booking findByPlanetIdAndTick(String planetId, long tick);
}
