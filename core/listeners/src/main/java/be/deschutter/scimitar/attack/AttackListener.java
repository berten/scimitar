package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;

@Component
public class AttackListener implements Listener {

    private AttackHelper attackHelper;
    private AttackService attackService;

    @Autowired
    public AttackListener(final AttackService attackService,
        final AttackHelper attackHelper) {
        this.attackService = attackService;
        this.attackHelper = attackHelper;
    }

    @Override
    public String getCommand() {
        return "attack";
    }

    @Override
    public String getPattern() {
        return "new lt|eta waves <bg> | addtarget id x y <z> | remtarget id x y <z> | comment your comment";
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_BC')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_BC')")
    public String getResult(final String... parameters) {
        if (parameters[0].equalsIgnoreCase("comment")) {
            return attackHelper.getAttackInfo(attackService
                .addComment(parseInt(parameters[1]), maakComment(parameters)));
        } else {
            if (parameters.length == 3) {
                if (parameters[0].equalsIgnoreCase("new")) {
                    final Attack savedAttack = attackService
                        .createAttack(parseInt(parameters[1]),
                            parseInt(parameters[2]));
                    return String.format("New attack created with id: %d",
                        savedAttack.getId());
                }
            } else if (parameters.length == 4) {
                if (parameters[0].equalsIgnoreCase("new")) {
                    final Attack savedAttack = attackService
                        .createAttack(parseInt(parameters[1]),
                            parseInt(parameters[2]), parameters[3]);
                    return String
                        .format("New attack created for bg %s with id: %d",
                            savedAttack.getBattleGroup().getName(),
                            savedAttack.getId());
                } else if (parameters[0].equalsIgnoreCase("addTarget")) {
                    return attackHelper.getAttackInfo(attackService
                        .addTarget(parseInt(parameters[1]),
                            parseInt(parameters[2]), parseInt(parameters[3])));
                } else if (parameters[0].equalsIgnoreCase("remTarget")) {

                    return attackHelper.getAttackInfo(attackService
                        .removeTarget(parseInt(parameters[1]),
                            parseInt(parameters[2]), parseInt(parameters[3])));

                }
            } else if (parameters.length == 5) {
                if (parameters[0].equalsIgnoreCase("addTarget")) {
                    return attackHelper.getAttackInfo(attackService
                        .addTarget(parseInt(parameters[1]),
                            parseInt(parameters[2]), parseInt(parameters[3]),
                            parseInt(parameters[4])));

                } else if (parameters[0].equalsIgnoreCase("remTarget")) {

                    return attackHelper.getAttackInfo(attackService
                        .removeTarget(parseInt(parameters[1]),
                            parseInt(parameters[2]), parseInt(parameters[3]),
                            parseInt(parameters[4])));

                }
            }
        }
        return getErrorMessage();
    }

    private String maakComment(final String[] parameters) {
        StringBuilder comment = new StringBuilder();
        for (int i = 2; i < parameters.length; i++) {
            comment.append(parameters[i]).append(" ");
        }
        return comment.toString().trim();
    }

}
