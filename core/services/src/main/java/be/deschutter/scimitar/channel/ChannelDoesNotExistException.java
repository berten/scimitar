package be.deschutter.scimitar.channel;

public class ChannelDoesNotExistException extends RuntimeException {
    private String channelName;

    public ChannelDoesNotExistException(final String channelName) {
        super(
            String.format("Channel with name %s does not exist", channelName));
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
}
