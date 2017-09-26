package be.deschutter.scimitar;

import be.deschutter.scimitar.channel.ChannelConfiguration;
import be.deschutter.scimitar.channel.ChannelConfigurationEao;
import be.deschutter.scimitar.channel.ChannelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class RegChanListener implements Listener {

    private final ChannelConfigurationEao channelConfigurationEao;

    @Autowired
    public RegChanListener(
        final ChannelConfigurationEao channelConfigurationEao) {
        this.channelConfigurationEao = channelConfigurationEao;
    }

    @Override
    public String getCommand() {
        return "regchan";
    }

    @Override
    public String getPattern() {
        return Arrays.stream(ChannelType.values())
            .sorted(Comparator.comparing(Enum::name)).map(Enum::name)
            .collect(Collectors.joining("|"));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_HC','ROLE_ADMIN')")
    public boolean hasAccess() {
        return true;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_HC','ROLE_ADMIN')")
    public String getResult(final String username, final String... parameters) {
        if (parameters.length == 2) {
            final String channelName = parameters[1];
            final ChannelConfiguration channel = channelConfigurationEao
                .findByChannelName(channelName);

            if(channel == null) {
                ChannelType type = ChannelType.valueOf(parameters[0].toUpperCase());

                final ChannelConfiguration c = new ChannelConfiguration();
                c.setChannelType(type);
                c.setChannelName(channelName);

                channelConfigurationEao.save(c);
                return "Channel Regged for type " + type;
            } else {
                return "Channel allready regged for type " + channel.getChannelType();
            }
        } else if (parameters.length == 1) {
            return "This command should only be used in slack";
        }
        else
            return getErrorMessage();
    }
}
