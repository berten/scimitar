package be.deschutter.scimitar.channel;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ChannelConfiguration {
    @Id
    @GeneratedValue
    private Integer id;
    private String channelName;

    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(final String channelName) {
        this.channelName = channelName;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(final ChannelType channelType) {
        this.channelType = channelType;
    }
}
