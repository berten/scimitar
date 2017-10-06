package be.deschutter.scimitar.intel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerEao playerEao;

    @Autowired
    public PlayerServiceImpl(final PlayerEao playerEao) {
        this.playerEao = playerEao;
    }

    @Override
    public Player findBy(final String nickPhoneEmail) {
        Player player = playerEao.findFirstByNicksContains(nickPhoneEmail);
        if (player == null)
            player = playerEao.findFirstByPhoneNumber(nickPhoneEmail);
        if (player == null)
            player = playerEao.findFirstByEmail(nickPhoneEmail);
        if (player == null)
            throw new PlayerNotFoundException(nickPhoneEmail);
        return player;
    }

    @Override
    public Player createPlayer(final String newNickname) {
        return null;
    }
}
