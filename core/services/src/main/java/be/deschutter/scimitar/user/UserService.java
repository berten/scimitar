package be.deschutter.scimitar.user;

import be.deschutter.scimitar.user.ScimitarUser;

public interface UserService {
    ScimitarUser findBy(String username);

    ScimitarUser save(ScimitarUser user);
}
