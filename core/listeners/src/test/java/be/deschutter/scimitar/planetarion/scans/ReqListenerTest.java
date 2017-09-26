package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.scans.ScanRequest;
import be.deschutter.scimitar.scans.ScanRequestEao;
import be.deschutter.scimitar.scans.ScanType;
import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.ScimitarUserEao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Topic;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReqListenerTest {
    @InjectMocks
    private ReqListener reqListener;

    @Mock
    private ScimitarUserEao scimitarUserEao;

    @Mock
    private PlanetEao planetEao;

    @Mock
    private ScanRequestEao scanRequestEao;
    @Mock
    private TickerInfoEao tickerInfoEao;
    @Mock
    private JmsTemplate jmsTemplate;
@Mock
    private Topic scanRequestTopic;

    private ScimitarUser scimitarUser;
    private ScanRequest sr3;
    private ScanRequest sr4;
    private ScanRequest sr5;
    private ScanRequest sr2;
    private ScanRequest sr1;
    private Planet p3;
    private Planet p2;
    private Planet p1;

    @Before
    public void setUp() throws Exception {
        scimitarUser = new ScimitarUser();
        scimitarUser.setUsername("username");
        when(scimitarUserEao.findByUsernameIgnoreCase("username"))
            .thenReturn(scimitarUser);
        when(tickerInfoEao.findFirstByOrderByTickDesc())
            .thenReturn(new TickerInfo(123));

        final Planet planet = new Planet();
        planet.setTick(123);
        planet.setId("planetId");
        when(planetEao.findByXAndYAndZAndTick(1, 1, 1, 123L))
            .thenReturn(planet);

        p1 = new Planet();
        p1.setDists(0);
        p1.setX(1);
        p1.setY(2);
        p1.setZ(3);
        when(planetEao.findByIdAndTick("planetid1", 123)).thenReturn(p1);
        when(planetEao.findByXAndYAndZAndTick(1, 2, 3, 123)).thenReturn(p1);

        p2 = new Planet();
        p2.setDists(20);
        p2.setX(4);
        p2.setY(5);
        p2.setZ(6);
        when(planetEao.findByIdAndTick("planetid2", 123)).thenReturn(p2);

        p3 = new Planet();
        p3.setDists(3);
        p3.setX(7);
        p3.setY(8);
        p3.setZ(9);
        when(planetEao.findByIdAndTick("planetid3", 123)).thenReturn(p3);

        sr1 = new ScanRequest();
        sr1.setPlanetId("planetid1");
        sr1.setScanType(ScanType.P);
        sr1.setTick(123);
        sr1.setId(1L);

        sr2 = new ScanRequest();
        sr2.setPlanetId("planetid1");
        sr2.setScanType(ScanType.D);
        sr2.setTick(122);
        sr2.setId(2L);

        sr3 = new ScanRequest();
        sr3.setPlanetId("planetid2");
        sr3.setScanType(ScanType.A);
        sr3.setTick(122);
        sr3.setId(3L);

        sr4 = new ScanRequest();
        sr4.setPlanetId("planetid1");
        sr4.setScanType(ScanType.J);
        sr4.setTick(122);
        sr4.setId(4L);

        sr5 = new ScanRequest();
        sr5.setPlanetId("planetid3");
        sr5.setScanType(ScanType.J);
        sr5.setTick(122);
        sr5.setId(5L);

    }

    @Test
    public void getCommand() throws Exception {
        assertThat(reqListener.getCommand()).isEqualTo("req");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(reqListener.getPattern()).isEqualTo(
            "x y z P|D|U|N|I|J|A [dists] | x y z blocks <amps> | cancel <id> | list [amps] | links [amps]");
    }

    @Test
    public void blocks() throws Exception {
        assertThat(
            reqListener.getResult("username", "1", "2", "3", "blocks", "13"))
            .isEqualTo("Updated intelligence on 1:2:3 to 13 dists");
        final ArgumentCaptor<Planet> captor = ArgumentCaptor
            .forClass(Planet.class);
        verify(planetEao).save(captor.capture());
        assertThat(captor.getValue().getDists()).isEqualTo(13);
    }

    @Test
    public void getResult_List_NoAmps() throws Exception {

        when(scanRequestEao.findByScanIdIsNull())
            .thenReturn(Arrays.asList(sr1, sr2, sr3, sr5));
        assertThat(reqListener.getResult("username", "list"))
            .isEqualTo("[1:2:3 (0) 1:P|2:D] [4:5:6 (20) 3:A] [7:8:9 (3) 5:J]");
    }

    @Test
    public void getResult_List_WithAmps() throws Exception {

        when(scanRequestEao.findByScanIdIsNull())
            .thenReturn(Arrays.asList(sr1, sr2, sr3, sr5));
        assertThat(reqListener.getResult("username", "list", "13"))
            .isEqualTo("[1:2:3 (0) 1:P|2:D] [7:8:9 (3) 5:J]");
    }

    @Test
    public void getResult_Links_NoAmps() throws Exception {

        when(scanRequestEao.findByScanIdIsNull())
            .thenReturn(Arrays.asList(sr1, sr2, sr3, sr5));
        assertThat(reqListener.getResult("username", "links")).isEqualTo(
            "[1 (0) : https://game.planetarion.com/waves.pl?id=0&x=1&y=2&z=3] [2 (0) : https://game.planetarion.com/waves.pl?id=1&x=1&y=2&z=3] [3 (20) : https://game.planetarion.com/waves.pl?id=6&x=4&y=5&z=6] [5 (3) : https://game.planetarion.com/waves.pl?id=5&x=7&y=8&z=9]");
    }

    @Test
    public void getResult_Links_WithAmps() throws Exception {

        when(scanRequestEao.findByScanIdIsNull())
            .thenReturn(Arrays.asList(sr1, sr2, sr3, sr5));
        assertThat(reqListener.getResult("username", "links", "13")).isEqualTo(
            "[1 (0) : https://game.planetarion.com/waves.pl?id=0&x=1&y=2&z=3] [2 (0) : https://game.planetarion.com/waves.pl?id=1&x=1&y=2&z=3] [5 (3) : https://game.planetarion.com/waves.pl?id=5&x=7&y=8&z=9]");
    }

    @Test
    public void cancel() throws Exception {
        assertThat(reqListener.getResult("username", "cancel", "13"))
            .isEqualTo("Scanrequest 13 successfully cancelled");
        verify(scanRequestEao).delete(13);
    }

    @Test
    public void cancel_not_an_id() throws Exception {
        assertThat(reqListener.getResult("username", "cancel", "xx")).isEqualTo(
            "Error: use following pattern for command req: x y z P|D|U|N|I|J|A [dists] | x y z blocks <amps> | cancel <id> | list [amps] | links [amps]");
    }

    @Test
    public void getResult_1Parameter_NotLinkLIst() throws Exception {

        assertThat(reqListener.getResult("username", "sports")).isEqualTo(
            "Error: use following pattern for command req: x y z P|D|U|N|I|J|A [dists] | x y z blocks <amps> | cancel <id> | list [amps] | links [amps]");
    }

    @Test
    public void getResult_2Parameter_NotLinkLIstCancel() throws Exception {

        assertThat(reqListener.getResult("username", "sports", "blaat"))
            .isEqualTo(
                "Error: use following pattern for command req: x y z P|D|U|N|I|J|A [dists] | x y z blocks <amps> | cancel <id> | list [amps] | links [amps]");
    }

    @Test
    public void getResult_XYZNotNumeric() throws Exception {
        assertThat(reqListener.getResult("username", "1", "1", "Z", "pda"))
            .isEqualTo(
                "Error: use following pattern for command req: x y z P|D|U|N|I|J|A [dists] | x y z blocks <amps> | cancel <id> | list [amps] | links [amps]");
    }

    @Test
    public void getResult() throws Exception {
        assertThat(reqListener.getResult("username", "1", "1", "1", "p"))
            .isEqualTo("Requested p scan(s) for coords: 1:1:1");
        final ArgumentCaptor<ScanRequest> captor = ArgumentCaptor
            .forClass(ScanRequest.class);
        verify(scanRequestEao).save(captor.capture());
        assertThat(captor.getValue().getScanType()).isEqualTo(ScanType.P);
        assertThat(captor.getValue().getPlanetId()).isEqualTo("planetId");
        assertThat(captor.getValue().getTick()).isEqualTo(123);
        assertThat(captor.getValue().getScanId()).isNull();
        assertThat(captor.getValue().isDelivered()).isFalse();
        assertThat(captor.getValue().getRequestedBy()).isSameAs(scimitarUser);
        verify(jmsTemplate).convertAndSend(scanRequestTopic,"New Scan Request: P on 1:1:1 https://game.planetarion.com/waves.pl?id=0&x=1&y=1&z=1");
    }

    @Test
    public void getResult_multipleScans() throws Exception {
        assertThat(reqListener.getResult("username", "1", "1", "1", "pda"))
            .isEqualTo("Requested pda scan(s) for coords: 1:1:1");
        final ArgumentCaptor<ScanRequest> captor = ArgumentCaptor
            .forClass(ScanRequest.class);
        verify(scanRequestEao, times(3)).save(captor.capture());

        assertThat(captor.getAllValues())
            .extracting("scanType", "planetId", "tick", "scanId",
                "requestedBy.username", "delivered").contains(
            tuple(ScanType.P, "planetId", 123L, null, "username", false),
            tuple(ScanType.D, "planetId", 123L, null, "username", false),
            tuple(ScanType.A, "planetId", 123L, null, "username", false));
    }

    @Test
    public void getResult_PlanetDoesntExist() throws Exception {
        assertThat(reqListener.getResult("username", "6", "6", "6", "p"))
            .isEqualTo("Planet 6:6:6 does not exist");
        verifyZeroInteractions(scanRequestEao);
    }

}