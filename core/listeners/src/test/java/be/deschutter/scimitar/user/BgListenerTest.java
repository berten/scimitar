package be.deschutter.scimitar.user;

import be.deschutter.scimitar.attack.BattleGroupDoesNotExistException;
import be.deschutter.scimitar.attack.BattleGroupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BgListenerTest {
    @InjectMocks
    private BgListener bgListener;

    @Mock
    private BattleGroupService battleGroupService;
    @Mock
    private UserService userService;
    private BattleGroup existingBattleGroup;
    private ScimitarUser existMemberOfBattleGroup;
    private ScimitarUser user;

    @Before
    public void setUp() throws Exception {
        existingBattleGroup = new BattleGroup("bgName");
        existMemberOfBattleGroup = new ScimitarUser();
        existMemberOfBattleGroup.setUsername("existinguser");
        existingBattleGroup.addUser(existMemberOfBattleGroup);
        when(battleGroupService.findByName("bgName"))
            .thenReturn(existingBattleGroup);
        user = new ScimitarUser();
        user.setUsername("user");
        when(userService.findBy("user")).thenReturn(user);

        when(userService.findBy("existinguser"))
            .thenReturn(existMemberOfBattleGroup);
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(bgListener.getCommand()).isEqualTo("bg");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(bgListener.getPattern()).isEqualTo(
            "add name | remove name | adduser name user | remuser name user");
    }

    @Test
    public void getResult_add_alreadyExists() throws Exception {

        assertThat(bgListener.getResult("add", "bgName"))
            .isEqualTo("A battlegroup with name bgName already exists");
        verify(battleGroupService, never()).save(any(BattleGroup.class));
    }

    @Test
    public void getResult_add() throws Exception {
        when(battleGroupService.findByName("newBgName")).thenThrow(new BattleGroupDoesNotExistException("newBgName"));
        assertThat(bgListener.getResult("add", "newBgName"))
            .isEqualTo("Battlegroup with name newBgName created");
        final ArgumentCaptor<BattleGroup> captor = ArgumentCaptor
            .forClass(BattleGroup.class);
        verify(battleGroupService).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("newBgName");
    }



    @Test
    public void getResult_remove() throws Exception {
        assertThat(bgListener.getResult("remove", "bgName"))
            .isEqualTo("Battlegroup bgName successfully removed");
        verify(battleGroupService).delete(existingBattleGroup);
    }





    @Test
    public void getResult_Remuser() throws Exception {
        assertThat(bgListener.getResult("remuser","bgName","existinguser")).isEqualTo("User existinguser is no longer a member of battlegroup bgName");
        final ArgumentCaptor<BattleGroup> captor = ArgumentCaptor
            .forClass(BattleGroup.class);
        verify(battleGroupService).save(captor.capture());
        assertThat(captor.getValue().getScimitarUsers()).doesNotContain(existMemberOfBattleGroup);

    }



    @Test
    public void getResult_Adduser() throws Exception {
        assertThat(bgListener.getResult("adduser","bgName","user")).isEqualTo("User user is now a member of battlegroup bgName");
        final ArgumentCaptor<BattleGroup> captor = ArgumentCaptor
            .forClass(BattleGroup.class);
        verify(battleGroupService).save(captor.capture());
        assertThat(captor.getValue().getScimitarUsers()).contains(user);

    }
}