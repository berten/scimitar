package be.deschutter.scimitar.intel;

public class PlayerNotFoundException extends RuntimeException {
    private String nickname;

    public PlayerNotFoundException(final String nickname) {
        super(String.format("No player found for nickname %s", nickname));
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
