package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;

@Component
public class AttackShowListener implements Listener {
    private final AttackHelper attackHelper;
    private AttackService attackService;

    @Autowired
    public AttackShowListener(final AttackHelper attackHelper,
        final AttackService attackService) {
        this.attackHelper = attackHelper;
        this.attackService = attackService;
    }

    @Override
    public String getCommand() {
        return "attackshow";
    }

    @Override
    public String getPattern() {
        return "id";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {
        if (parameters.length == 1) {
            try {
                return attackHelper.getAttackInfo(
                    attackService.findAttack(parseInt(parameters[0])));
            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else
            return getErrorMessage();
    }
}
