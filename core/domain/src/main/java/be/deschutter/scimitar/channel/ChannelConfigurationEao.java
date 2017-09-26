package be.deschutter.scimitar.channel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.Channel;
import java.util.List;

public interface ChannelConfigurationEao
    extends JpaRepository<ChannelConfiguration, Integer> {
    List<ChannelConfiguration> findByChannelType(ChannelType channelType);

    ChannelConfiguration findByChannelName(String channelName);
}
