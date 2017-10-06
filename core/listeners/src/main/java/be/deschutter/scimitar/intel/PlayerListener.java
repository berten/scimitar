package be.deschutter.scimitar.intel;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PlayerListener implements Listener {
    private final PlayerService playerService;

    @Autowired
    public PlayerListener(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public String getCommand() {
        return "player";
    }

    @Override
    public String getPattern() {
        return "show nickname|phone|email | add nickname | addnick nickname newnickname";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {
        if (parameters.length == 2) {
            if (parameters[0].equals("show")) {
                final Player player = playerService
                    .findBy(parameters[1]);
                return String
                    .format("Known nicks: %s phone: %s email: %s Best Ranks: %s",
                        player.getNicks().stream()
                            .collect(Collectors.joining(", ")),
                        showPhoneNumber(player), showEmail(player),
                        player.getPlanetHistories().isEmpty() ?
                            "no known ranks" :
                            showPlanetHistories(player));
            }

        } else if (parameters.length == 3) {
            return getErrorMessage();
        } else
            return getErrorMessage();
        return getErrorMessage();
    }

    private String showPlanetHistories(final Player player) {
        return player.getPlanetHistories().stream()
            .sorted((o1, o2) -> o1.getRank().compareTo(o2.getRank()))
            .limit(5).map(planetHistory -> String
                .format("%d (%d)", planetHistory.getRank(),
                    planetHistory.getRound()))
            .collect(Collectors.joining(" | "));
    }

    private String showEmail(final Player player) {
        return player.getEmail() == null ? "none" : player.getEmail();
    }

    private String showPhoneNumber(final Player player) {
        return player.getPhoneNumber() == null ?
            "none" :
            player.getPhoneNumber();
    }
}
