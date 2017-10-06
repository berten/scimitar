package be.deschutter.scimitar.channel;

import java.util.List;

public interface ChannelService {
    ChannelConfiguration findBy(String channelName);
    List<ChannelConfiguration> findBy(ChannelType channelType);
    ChannelConfiguration save(ChannelConfiguration channelConfiguration);
}
