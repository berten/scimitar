package be.deschutter.scimitar.user;

import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.attack.BattleGroupDoesNotExistException;
import be.deschutter.scimitar.attack.BattleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class BgListener implements Listener {
    private final BattleGroupService battleGroupService;
    private final UserService userService;

    @Autowired
    public BgListener(final BattleGroupService battleGroupService,
        final UserService userService) {
        this.battleGroupService = battleGroupService;
        this.userService = userService;
    }

    @Override
    public String getCommand() {
        return "bg";
    }

    @Override
    public String getPattern() {
        return "add name | remove name | adduser name user | remuser name user";
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_HC')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_HC')")
    public String getResult(final String... parameters) {
        if (parameters.length == 2) {
            if (parameters[0].equalsIgnoreCase("add")) {
                final String name = parameters[1];
                try {
                    final BattleGroup bg = battleGroupService.findByName(name);
                    return String
                        .format("A battlegroup with name %s already exists",
                            bg.getName());
                } catch (BattleGroupDoesNotExistException e) {
                    BattleGroup newBg = new BattleGroup();
                    newBg.setName(name);
                    battleGroupService.save(newBg);
                    return String
                        .format("Battlegroup with name %s created", name);
                }

            } else if (parameters[0].equalsIgnoreCase("remove")) {
                final String name = parameters[1];
                final BattleGroup bg = battleGroupService.findByName(name);
                battleGroupService.delete(bg);
                return String
                    .format("Battlegroup %s successfully removed", name);
            }
        } else if (parameters.length == 3) {
            if (parameters[0].equalsIgnoreCase("adduser")) {
                String bgName = parameters[1];
                String userToAdd = parameters[2];

                final BattleGroup bg = battleGroupService.findByName(bgName);

                final ScimitarUser user = userService.findBy(userToAdd);

                bg.addUser(user);
                battleGroupService.save(bg);
                return String
                    .format("User %s is now a member of battlegroup %s",
                        userToAdd, bgName);

            } else if (parameters[0].equalsIgnoreCase("remuser")) {
                String bgName = parameters[1];
                String userToAdd = parameters[2];

                final BattleGroup bg = battleGroupService.findByName(bgName);

                final ScimitarUser user = userService.findBy(userToAdd);

                bg.remUser(user);
                battleGroupService.save(bg);
                return String
                    .format("User %s is no longer a member of battlegroup %s",
                        userToAdd, bgName);

            } else
                return getErrorMessage();
        } else
            return getErrorMessage();
        return getErrorMessage();
    }
}
