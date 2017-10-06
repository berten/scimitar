package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AttackListListener implements Listener {
    private AttackService attackService;

    @Autowired
    public AttackListListener(final AttackService attackService) {
        this.attackService = attackService;
    }

    @Override
    public String getCommand() {
        return "attacklist";
    }

    @Override
    public String getPattern() {
        return "";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {

        return String.format("%s",
            attackService.findLast5Attacks().stream().map(attack -> {
                if (attack.getBattleGroup() == null)
                    return String
                        .format("ID: %d Comment: LT %d %s", attack.getId(),
                            attack.getLandTick(), attack.getComment() == null ? "" : attack.getComment()).trim();
                else
                    return String.format("ID: %d BG:%s Comment: LT %d %s",
                        attack.getId(), attack.getBattleGroup().getName(),
                        attack.getLandTick(), attack.getComment() == null ? "" : attack.getComment()).trim();
            }).collect(Collectors.joining(" | ")));

    }

}
