package be.deschutter.scimitar.intel;

public interface PlayerService {
    Player findBy(String nickname);

    Player createPlayer(String newNickname);
}
