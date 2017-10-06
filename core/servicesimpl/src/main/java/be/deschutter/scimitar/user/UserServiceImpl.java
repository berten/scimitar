package be.deschutter.scimitar.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final ScimitarUserEao scimitarUserEao;

    @Autowired
    public UserServiceImpl(final ScimitarUserEao scimitarUserEao) {
        this.scimitarUserEao = scimitarUserEao;
    }

    @Override
    public ScimitarUser findBy(final String username) {
        final ScimitarUser user = scimitarUserEao
            .findByUsernameIgnoreCase(username);
        if(user == null) throw new UserNotFoundException("username");
        return user;
    }

    @Override
    public ScimitarUser save(final ScimitarUser user) {
        return scimitarUserEao.saveAndFlush(user);
    }
}
