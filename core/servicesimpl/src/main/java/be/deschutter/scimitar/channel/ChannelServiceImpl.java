package be.deschutter.scimitar.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelConfigurationEao channelConfigurationEao;

    @Autowired
    public ChannelServiceImpl(
        final ChannelConfigurationEao channelConfigurationEao) {
        this.channelConfigurationEao = channelConfigurationEao;
    }

    @Override
    public ChannelConfiguration findBy(final String channelName) {
        final ChannelConfiguration c = channelConfigurationEao
            .findByChannelName(channelName);
        if (c == null)
            throw new ChannelDoesNotExistException(channelName);
        return c;
    }

    @Override
    public List<ChannelConfiguration> findBy(final ChannelType channelType) {
        return channelConfigurationEao.findByChannelType(channelType);
    }

    @Override
    public ChannelConfiguration save(
        final ChannelConfiguration channelConfiguration) {
        return channelConfigurationEao.save(channelConfiguration);
    }

}
