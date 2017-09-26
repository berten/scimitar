package be.deschutter.scimitar.user;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class BgListener implements Listener {
    @Autowired
    private BattleGroupEao battleGroupEao;

    @Autowired
    private ScimitarUserEao scimitarUserEao;

    @Override
    public String getCommand() {
        return "bg";
    }

    @Override
    public String getPattern() {
        return "add name | remove name | adduser name user | remuser name user";
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_HC','ROLE_BC','ROLE_ADMIN')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_HC','ROLE_BC','ROLE_ADMIN')")
    public String getResult(final String username, final String... parameters) {
        if (parameters.length == 2) {
            if (parameters[0].equalsIgnoreCase("add")) {
                final String name = parameters[1];
                final BattleGroup bg = battleGroupEao
                    .findByNameIgnoreCase(name);
                if (bg != null) {
                    return String
                        .format("A battlegroup with name %s already exists",
                            name);
                } else {
                    BattleGroup newBg = new BattleGroup();
                    newBg.setName(name);
                    battleGroupEao.save(newBg);
                    return String
                        .format("Battlegroup with name %s created", name);
                }

            } else if (parameters[0].equalsIgnoreCase("remove")) {
                final String name = parameters[1];
                final BattleGroup bg = battleGroupEao
                    .findByNameIgnoreCase(name);
                if (bg != null) {
                    battleGroupEao.delete(bg);
                    return String
                        .format("Battlegroup %s successfully removed", name);
                } else {
                    return String
                        .format("No battlegroup found with name %s", name);
                }
            }
        } else if (parameters.length == 3) {
            if (parameters[0].equalsIgnoreCase("adduser")) {
                String bgName = parameters[1];
                String userToAdd = parameters[2];

                final BattleGroup bg = battleGroupEao
                    .findByNameIgnoreCase(bgName);
                if (bg == null) {
                    return String
                        .format("No battlegroup found with name %s", bgName);
                } else {
                    final ScimitarUser user = scimitarUserEao
                        .findByUsernameIgnoreCase(userToAdd);
                    if (user == null) {
                        return String
                            .format("No user found with name %s", userToAdd);
                    } else {
                        bg.addUser(user);
                        battleGroupEao.save(bg);
                        return String
                            .format("User %s is now a member of battlegroup %s",
                                userToAdd, bgName);
                    }
                }

            } else if (parameters[0].equalsIgnoreCase("remuser")) {
                String bgName = parameters[1];
                String userToAdd = parameters[2];

                final BattleGroup bg = battleGroupEao
                    .findByNameIgnoreCase(bgName);
                if (bg == null) {
                    return String
                        .format("No battlegroup found with name %s", bgName);
                } else {
                    final ScimitarUser user = scimitarUserEao
                        .findByUsernameIgnoreCase(userToAdd);
                    if (user == null) {
                        return String
                            .format("No user found with name %s", userToAdd);
                    } else {
                        bg.remUser(user);
                        battleGroupEao.save(bg);
                        return String
                            .format("User %s is no longer a member of battlegroup %s",
                                userToAdd, bgName);
                    }
                }
            } else
                return getErrorMessage();
        } else
            return getErrorMessage();
        return getErrorMessage();
    }
}
