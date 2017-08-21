package be.deschutter.scimitar.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScimitarUserEao extends JpaRepository<ScimitarUser,Integer> {

    ScimitarUser findByUsernameIgnoreCase(String username);
}
