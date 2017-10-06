package be.deschutter.scimitar.user;

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
    private UserService scimitarUserEao;

    @Before
    public void setUp() throws Exception {
        when(scimitarUserEao.save(any(ScimitarUser.class))).then(
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
        assertThat(addUserListener.getResult()).isEqualTo(
            "Error: use following pattern for command adduser: nickname ADMIN|BC|HC|MEMBER");
    }

    @Test
    public void getResult_OneParameter() throws Exception {
        when(scimitarUserEao.findBy("newUser")).thenThrow(new UserNotFoundException("newUser"));
        assertThat(addUserListener.getResult("newUser"))
            .isEqualTo("User newUser added with roles: MEMBER");
    }

    @Test
    public void getResult_UserAlreadyExists() throws Exception {
        final ScimitarUser user = new ScimitarUser();
        user.setUsername("NewUser");
        user.addRole(RoleEnum.ADMIN);
        user.addRole(RoleEnum.MEMBER);
        when(scimitarUserEao.findBy("newUser"))
            .thenReturn(user);

        assertThat(addUserListener.getResult("newUser", "HC"))
            .isEqualTo(
                "User NewUser already exists with roles: ADMIN,MEMBER. Use !changeuser to change this user's access");
    }

    @Test
    public void getResult_NewUser() throws Exception {
        when(scimitarUserEao.findBy("newUser")).thenThrow(new UserNotFoundException("newUser"));
        assertThat(
            addUserListener.getResult("newUser", "HC", "admin"))
            .isEqualTo("User newUser added with roles: ADMIN,HC");
        final ArgumentCaptor<ScimitarUser> captor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(scimitarUserEao).save(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("newUser");
        assertThat(captor.getValue().getRoles()).extracting("role")
            .contains(RoleEnum.HC, RoleEnum.ADMIN);
    }
}