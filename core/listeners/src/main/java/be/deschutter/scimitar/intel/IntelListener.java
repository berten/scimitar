package be.deschutter.scimitar.intel;

import be.deschutter.scimitar.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IntelListener implements Listener {
    @Autowired
    private IntelService intelService;

    @Override
    public String getCommand() {
        return "intel";
    }

    @Override
    public String getPattern() {
        return "x y z nick=<nick> alliance=<alliance> nickadd=<nick to add to player>";
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_MEMBER')")
    public String getResult(final String... parameters) {
        if (parameters.length == 3) {
            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);
            return findIntel(x, y, z);
        } else {
            int x = Integer.parseInt(parameters[0]);
            int y = Integer.parseInt(parameters[1]);
            int z = Integer.parseInt(parameters[2]);

            for (int i = 3; i < parameters.length; i++) {
                final String parameter = parameters[i];
                final String[] splitted = parameter.split("=");
                if (splitted.length == 2) {
                    switch (splitted[0]) {
                    case "nick":
                        intelService.changeNick(1, 2, 3, splitted[1]);
                        break;
                    case "alliance":
                        intelService.changeAlliance(1, 2, 3, splitted[1]);
                        break;
                    case "addnick":
                        intelService.addNick(1,2,3,splitted[1]);
                        break;
                    default:
                        return getErrorMessage();
                    }
                }
            }
            return findIntel(x, y, z);
        }
    }

    private String findIntel(final int x, final int y, final int z) {
        Intel intel = intelService.findBy(x, y, z);
        if (intel.getAlliance() != null && intel.getPlayer() != null)
            return String
                .format("Intel for %d:%d:%d alliance=%s nick=%s", x, y, z,
                    intel.getAlliance().getAllianceName(),
                    intel.getPlayer().getNicks().stream()
                        .collect(Collectors.joining(", ")));
        else if (intel.getAlliance() == null && intel.getPlayer() != null)
            return String.format("Intel for %d:%d:%d nick=%s", x, y, z,
                intel.getPlayer().getNicks().stream()
                    .collect(Collectors.joining(", ")));
        else if (intel.getAlliance() != null && intel.getPlayer() == null)
            return String.format("Intel for %d:%d:%d alliance=%s", x, y, z,
                intel.getAlliance().getAllianceName());
        else
            return getErrorMessage();
    }
}
