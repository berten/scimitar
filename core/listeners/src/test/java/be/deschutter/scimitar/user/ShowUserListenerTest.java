package be.deschutter.scimitar.user;

import be.deschutter.scimitar.security.SecurityHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowUserListenerTest {
    @InjectMocks
    private ShowUserListener showUserListener;

    @Mock
    private UserService userService;
    @Mock
    private SecurityHelper securityHelper;

    @Test
    public void getCommand() throws Exception {
        assertThat(showUserListener.getCommand()).isEqualTo("showuser");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(showUserListener.getPattern()).isEqualTo("<nickname>");
    }

    @Test
    public void getResult() throws Exception {
        final ScimitarUser u = new ScimitarUser();
        u.setUsername("Berten");
        u.addRole(RoleEnum.ADMIN);
        u.addRole(RoleEnum.MEMBER);
        when(securityHelper.getLoggedInUser()).thenReturn(u);
        assertThat(showUserListener.getResult())
            .isEqualTo("User roles for username Berten: ADMIN,MEMBER");
    }

    @Test
    public void getResult_knownUsername() throws Exception {
        final ScimitarUser u = new ScimitarUser();
        u.setUsername("KnownUsername");
        u.addRole(RoleEnum.ADMIN);
        u.addRole(RoleEnum.MEMBER);
        when(userService.findBy("knownUsername")).thenReturn(u);
        assertThat(showUserListener.getResult("knownUsername"))
            .isEqualTo("User roles for username KnownUsername: ADMIN,MEMBER");
    }

}