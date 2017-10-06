package be.deschutter.scimitar.intel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerEao extends JpaRepository<Player, Integer> {
    Player findFirstByNicksContains(String nickname);

    Player findFirstByPhoneNumber(String nickPhoneEmail);

    Player findFirstByEmail(String nickPhoneEmail);
}
