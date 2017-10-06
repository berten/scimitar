package be.deschutter.scimitar;

import be.deschutter.scimitar.channel.ChannelConfiguration;
import be.deschutter.scimitar.channel.ChannelDoesNotExistException;
import be.deschutter.scimitar.channel.ChannelService;
import be.deschutter.scimitar.channel.ChannelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class RegChanListener implements Listener {

    private final ChannelService channelService;

    @Autowired
    public RegChanListener(final ChannelService channelService) {
        this.channelService = channelService;
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
    public String getResult(final String... parameters) {
        if (parameters.length == 2) {
            final String channelName = parameters[1];
            try {
                final ChannelConfiguration channel = channelService
                    .findBy(channelName);
                return String.format("Channel %s allready regged for type %s",
                    channel.getChannelName(), channel.getChannelType());
            } catch (ChannelDoesNotExistException e) {
                ChannelType type = ChannelType
                    .valueOf(parameters[0].toUpperCase());

                final ChannelConfiguration c = new ChannelConfiguration();
                c.setChannelType(type);
                c.setChannelName(channelName);

                channelService.save(c);
                return "Channel Regged for type " + type;
            }

        } else if (parameters.length == 1) {
            return "This command should only be used in slack";
        } else
            return getErrorMessage();
    }
}
