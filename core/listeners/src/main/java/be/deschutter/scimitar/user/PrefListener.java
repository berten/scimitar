package be.deschutter.scimitar.user;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component

public class PrefListener implements Listener {
    private final ScimitarUserEao scimitarUserEao;
    private final PlanetEao planetEao;

    @Autowired
    public PrefListener(final ScimitarUserEao scimitarUserEao,
        final PlanetEao planetEao) {
        this.scimitarUserEao = scimitarUserEao;
        this.planetEao = planetEao;
    }

    @Override
    public String getCommand() {
        return "pref";
    }

    @Override
    public String getPattern() {
        return "phone=+9999999 planet=x:y:z email=email@test.com";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String username, final String... parameters) {
        final ScimitarUser user = scimitarUserEao
            .findByUsernameIgnoreCase(username);
        String returnMessage = "";
        for (String parameter : parameters) {
            final String[] split = parameter.split("=");
            switch (split[0]) {
            case "phone":
                if (split[1].startsWith("+")) {
                    user.setPhoneNumber(split[1]);
                    returnMessage +=
                        "Phone successfully stored as:" + split[1] + " ";
                } else {
                    return "Please add your phonenumber starting with a +";
                }
                break;
            case "email":
                if (split[1].contains("@")) {
                    user.setEmail(split[1]);
                    returnMessage +=
                        "Email successfully stored as:" + split[1] + " ";
                } else {
                    return "Please add a valid email addres";
                }
                break;
            case "planet":
                final String[] splitInXYZ = split[1].split(":");
                if (splitInXYZ.length == 3) {
                    final Planet planet = planetEao
                        .findFirstByXAndYAndZOrderByTickDesc(
                            Integer.parseInt(splitInXYZ[0]),
                            Integer.parseInt(splitInXYZ[1]),
                            Integer.parseInt(splitInXYZ[2]));

                    user.setPlanetId(planet.getId());
                    returnMessage +=
                        "Planet successfully stored as: " + split[1] + " ";
                } else {
                    return "Please user valid x:y:z coords";
                }
                break;
            }
        }
        if (returnMessage.isEmpty()) {
            return getErrorMessage();
        }
        scimitarUserEao.saveAndFlush(user);
        return returnMessage;
    }

}
