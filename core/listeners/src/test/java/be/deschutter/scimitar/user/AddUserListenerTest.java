package be.deschutter.scimitar.user;

import be.deschutter.scimitar.RoleEnum;
import be.deschutter.scimitar.ScimitarUser;
import be.deschutter.scimitar.ScimitarUserEao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddUserListenerTest {
    @InjectMocks
    private AddUserListener addUserListener;
    @Mock
    private ScimitarUserEao scimitarUserEao;

    @Before
    public void setUp() throws Exception {
        when(scimitarUserEao.saveAndFlush(any(ScimitarUser.class))).then(
            (Answer<ScimitarUser>) invocationOnMock -> (ScimitarUser) invocationOnMock
                .getArguments()[0]);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(addUserListener.getCommand()).isEqualTo("adduser");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(addUserListener.getPattern())
            .isEqualTo("nickname ADMIN|BC|HC|MEMBER");
    }

    @Test
    public void getResult_NoParameters() throws Exception {
        assertThat(addUserListener.getResult("Berten")).isEqualTo(
            "Error: use following pattern for command adduser: nickname ADMIN|BC|HC|MEMBER");
    }

    @Test
    public void getResult_OneParameter() throws Exception {
        assertThat(addUserListener.getResult("Berten", "newUser"))
            .isEqualTo("User newUser added with roles: MEMBER");
    }

    @Test
    public void getResult_UserAlreadyExists() throws Exception {
        final ScimitarUser user = new ScimitarUser();
        user.setUsername("NewUser");
        user.addRole(RoleEnum.ADMIN);
        user.addRole(RoleEnum.MEMBER);
        when(scimitarUserEao.findByUsernameIgnoreCase("newUser"))
            .thenReturn(user);

        assertThat(addUserListener.getResult("Berten", "newUser", "HC"))
            .isEqualTo(
                "User NewUser already exists with roles: ADMIN,MEMBER. Use !changeuser to change this user's access");
    }

    @Test
    public void getResult_NewUser() throws Exception {

        assertThat(
            addUserListener.getResult("Berten", "newUser", "HC", "ADMIN"))
            .isEqualTo("User newUser added with roles: ADMIN,HC");
        final ArgumentCaptor<ScimitarUser> captor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(scimitarUserEao).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("newUser");
        assertThat(captor.getValue().getRoles()).extracting("role")
            .contains(RoleEnum.HC, RoleEnum.ADMIN);
    }
}