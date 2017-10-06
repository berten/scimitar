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
public class ChangeUserListenerTest {
    @InjectMocks
    private ChangeUserListener changeUserListener;
    @Mock
    private UserService userService;
    private ScimitarUser user;

    @Before
    public void setUp() throws Exception {
        user = new ScimitarUser();

        when(userService.findBy("userToChange"))
            .thenReturn(user);
        when(userService.save(any(ScimitarUser.class))).then(
            (Answer<ScimitarUser>) invocationOnMock -> (ScimitarUser) invocationOnMock
                .getArguments()[0]);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(changeUserListener.getCommand()).isEqualTo("changeuser");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(changeUserListener.getPattern())
            .isEqualTo("nickname add|remove ADMIN|HC|BC|MEMBER");
    }

    @Test
    public void getResult_NoParameters() throws Exception {
        assertThat(changeUserListener.getResult()).isEqualTo(
            "Error: use following pattern for command changeuser: nickname add|remove ADMIN|HC|BC|MEMBER");
    }

    @Test
    public void getResult_OnlyOneParamter() throws Exception {
        assertThat(
            changeUserListener.getResult("userToChange", "change"))
            .isEqualTo(
                "Error: use following pattern for command changeuser: nickname add|remove ADMIN|HC|BC|MEMBER");
    }

    @Test
    public void getResult_Wrong_Operation() throws Exception {
        assertThat(changeUserListener
            .getResult("userToChange", "change", "ADMIN")).isEqualTo(
            "Error: use following pattern for command changeuser: nickname add|remove ADMIN|HC|BC|MEMBER");
    }

    @Test
    public void getResult_Wrong_role() throws Exception {
        user.addRole(RoleEnum.HC);
        assertThat(changeUserListener
            .getResult("userToChange", "add", "hellofa"))
            .isEqualTo("New access for users userToChange: HC");
    }

    @Test
    public void getResult_add() throws Exception {
        assertThat(
            changeUserListener.getResult("userToChange", "add", "HC"))
            .isEqualTo("New access for users userToChange: HC");

        final ArgumentCaptor<ScimitarUser> userCaptor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(userService).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRoles()).extracting("role")
            .contains(RoleEnum.HC);
    }

    @Test
    public void getResult_add_multiple() throws Exception {
        user.addRole(RoleEnum.MEMBER);
        assertThat(changeUserListener
            .getResult("userToChange", "add", "HC", "ADMIN"))
            .isEqualTo("New access for users userToChange: ADMIN,HC,MEMBER");

        final ArgumentCaptor<ScimitarUser> userCaptor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(userService).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRoles()).extracting("role")
            .contains(RoleEnum.HC, RoleEnum.ADMIN);
    }

    @Test
    public void getResult_remove() throws Exception {
        user.addRole(RoleEnum.HC);
        assertThat(changeUserListener
            .getResult("userToChange", "remove", "HC"))
            .isEqualTo("New access for users userToChange: ");

        final ArgumentCaptor<ScimitarUser> userCaptor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(userService).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRoles()).isEmpty();
    }

    @Test
    public void getResult_removeMultiple() throws Exception {
        user.addRole(RoleEnum.HC);
        user.addRole(RoleEnum.ADMIN);
        user.addRole(RoleEnum.MEMBER);
        assertThat(changeUserListener
            .getResult("userToChange", "remove", "hc", "ADMIN"))
            .isEqualTo("New access for users userToChange: MEMBER");

        final ArgumentCaptor<ScimitarUser> userCaptor = ArgumentCaptor
            .forClass(ScimitarUser.class);
        verify(userService).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRoles()).extracting("role")
            .contains(RoleEnum.MEMBER);
    }

}