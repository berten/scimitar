package be.deschutter.scimitar;

import be.deschutter.scimitar.channel.ChannelConfiguration;
import be.deschutter.scimitar.channel.ChannelDoesNotExistException;
import be.deschutter.scimitar.channel.ChannelService;
import be.deschutter.scimitar.channel.ChannelType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegChanListenerTest {
    @InjectMocks
    private RegChanListener regChanListener;

    @Mock
    private ChannelService channelService;

    @Test
    public void getCommand() throws Exception {
        assertThat(regChanListener.getCommand()).isEqualTo("regchan");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(regChanListener.getPattern())
            .isEqualTo("ATTACK|DEFENCE|HC|MAIN|SCAN");
    }

    @Test
    public void getResult_NoExtraParameter() throws Exception {
        assertThat(regChanListener.getResult("SCAN"))
            .isEqualTo("This command should only be used in slack");
    }

    @Test
    public void getResult_Saved() throws Exception {
        when(channelService.findBy("slackChannelId")).thenThrow(new ChannelDoesNotExistException("slackChannelId"));
        assertThat(
            regChanListener.getResult("SCAN", "slackChannelId"))
            .isEqualTo("Channel Regged for type SCAN");
        final ArgumentCaptor<ChannelConfiguration> captor = ArgumentCaptor
            .forClass(ChannelConfiguration.class);
        verify(channelService).save(captor.capture());
        assertThat(captor.getValue().getChannelType()).isEqualTo(
            ChannelType.SCAN);
        assertThat(captor.getValue().getChannelName()).isEqualTo("slackChannelId");
    }


    @Test
    public void getResult_AlreadyExistsForChannelId_NotSaved() throws Exception {
        final ChannelConfiguration c = new ChannelConfiguration();
        c.setChannelName("SLACKCHANNELID");
        c.setChannelType(ChannelType.ATTACK);
        when(channelService.findBy("slackChannelId")).thenReturn(
            c);
        assertThat(
            regChanListener.getResult("SCAN", "slackChannelId"))
            .isEqualTo("Channel SLACKCHANNELID allready regged for type ATTACK");
        final ArgumentCaptor<ChannelConfiguration> captor = ArgumentCaptor
            .forClass(ChannelConfiguration.class);
        verify(channelService,never()).save(any(ChannelConfiguration.class));
    }

}