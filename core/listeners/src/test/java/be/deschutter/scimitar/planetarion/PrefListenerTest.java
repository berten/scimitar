package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.user.ScimitarUser;
import be.deschutter.scimitar.user.ScimitarUserEao;
import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetEao;
import be.deschutter.scimitar.user.PrefListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrefListenerTest {
    @InjectMocks
    private PrefListener prefListener;
    @Mock
    private PlanetEao planetEao;
    @Mock
    private ScimitarUserEao scimitarUserEao;
    private ScimitarUser user;

    @Before
    public void setUp() throws Exception {
        user = new ScimitarUser();
        when(scimitarUserEao.findByUsernameIgnoreCase("Berten"))
            .thenReturn(user);

        final Planet p = new Planet();
        p.setId("planetId");
        when(planetEao.findFirstByXAndYAndZOrderByTickDesc(1, 2, 3))
            .thenReturn(p);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(prefListener.getCommand()).isEqualTo("pref");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(prefListener.getPattern())
            .isEqualTo("phone=+9999999 planet=x:y:z email=email@test.com");
    }

    @Test
    public void getResult_noParameters() throws Exception {
        assertThat(prefListener.getResult("Berten")).isEqualTo(
            "Error: use following pattern for command pref: phone=+9999999 planet=x:y:z email=email@test.com");
    }

    @Test
    public void getResult_Phone_FaultyNumber() throws Exception {
        assertThat(prefListener.getResult("Berten", "phone=00303000"))
            .isEqualTo("Please add your phonenumber starting with a +");
    }

    @Test
    public void getResult_Phone() throws Exception {
        assertThat(prefListener.getResult("Berten", "phone=+0000000"))
            .isEqualTo("Phone successfully stored as:+0000000 ");
        final ArgumentCaptor<ScimitarUser> captor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(scimitarUserEao).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getPhoneNumber()).isEqualTo("+0000000");
    }

    @Test
    public void getResult_Email_FaultyEmail() throws Exception {
        assertThat(prefListener.getResult("Berten", "email=testzonderatdotcom"))
            .isEqualTo("Please add a valid email addres");
    }

    @Test
    public void getResult_Email() throws Exception {
        assertThat(prefListener.getResult("Berten", "email=test@test.com"))
            .isEqualTo("Email successfully stored as:test@test.com ");
        final ArgumentCaptor<ScimitarUser> captor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(scimitarUserEao).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    public void getResult_Planet_Faulty_Planet() throws Exception {
        assertThat(prefListener.getResult("Berten", "planet=123"))
            .isEqualTo("Please user valid x:y:z coords");
    }

    @Test
    public void getResult_Planet_Faulty_Planet_SemiColon() throws Exception {
        assertThat(prefListener.getResult("Berten", "planet=1;2;3"))
            .isEqualTo("Please user valid x:y:z coords");
    }

    @Test
    public void getResult_Planet_Faulty_Planet_Galaxy() throws Exception {
        assertThat(prefListener.getResult("Berten", "planet=1:3"))
            .isEqualTo("Please user valid x:y:z coords");
    }

    @Test
    public void getResult_Planet() throws Exception {
        assertThat(prefListener.getResult("Berten", "planet=1:2:3"))
            .isEqualTo("Planet successfully stored as: 1:2:3 ");
        final ArgumentCaptor<ScimitarUser> captor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(scimitarUserEao).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getPlanetId()).isEqualTo("planetId");
    }

    @Test
    public void getResult_Everything() throws Exception {
        assertThat(prefListener
            .getResult("Berten", "planet=1:2:3", "phone=+000000",
                "email=test@test.com")).isEqualTo(
            "Planet successfully stored as: 1:2:3 Phone successfully stored as:+000000 Email successfully stored as:test@test.com ");
        final ArgumentCaptor<ScimitarUser> captor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(scimitarUserEao).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getPlanetId()).isEqualTo("planetId");
        assertThat(captor.getValue().getPhoneNumber()).isEqualTo("+000000");
        assertThat(captor.getValue().getEmail()).isEqualTo("test@test.com");
    }
}