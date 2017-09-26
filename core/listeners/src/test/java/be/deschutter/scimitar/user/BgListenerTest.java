package be.deschutter.scimitar.user;

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
    private BattleGroupEao battleGroupEao;
    @Mock
    private ScimitarUserEao scimitarUserEao;
    private BattleGroup existingBattleGroup;
    private ScimitarUser existMemberOfBattleGroup;
    private ScimitarUser user;

    @Before
    public void setUp() throws Exception {
        existingBattleGroup = new BattleGroup();
        existMemberOfBattleGroup = new ScimitarUser();
        existMemberOfBattleGroup.setUsername("existinguser");
        existingBattleGroup.addUser(existMemberOfBattleGroup);
        when(battleGroupEao.findByNameIgnoreCase("bgName"))
            .thenReturn(existingBattleGroup);
        user = new ScimitarUser();
        user.setUsername("user");
        when(scimitarUserEao.findByUsernameIgnoreCase("user")).thenReturn(user);

        when(scimitarUserEao.findByUsernameIgnoreCase("existinguser"))
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
        assertThat(bgListener.getResult("username", "add", "bgName"))
            .isEqualTo("A battlegroup with name bgName already exists");
        verify(battleGroupEao, never()).save(any(BattleGroup.class));
    }

    @Test
    public void getResult_add() throws Exception {
        assertThat(bgListener.getResult("username", "add", "newBgName"))
            .isEqualTo("Battlegroup with name newBgName created");
        final ArgumentCaptor<BattleGroup> captor = ArgumentCaptor
            .forClass(BattleGroup.class);
        verify(battleGroupEao).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("newBgName");
    }

    @Test
    public void getResult_removeDoesntExist() throws Exception {
        assertThat(
            bgListener.getResult("username", "remove", "NonExistingbgName"))
            .isEqualTo("No battlegroup found with name NonExistingbgName");
        verify(battleGroupEao, never()).delete(any(BattleGroup.class));
    }

    @Test
    public void getResult_remove() throws Exception {
        assertThat(bgListener.getResult("username", "remove", "bgName"))
            .isEqualTo("Battlegroup bgName successfully removed");
        verify(battleGroupEao).delete(existingBattleGroup);
    }

    @Test
    public void getResult_Remuser_UnknownBg() throws Exception {
        assertThat(bgListener.getResult("username","remuser","unknownbg","user")).isEqualTo("No battlegroup found with name unknownbg");

    }

    @Test
    public void getResult_Remuser_UnknownUser() throws Exception {
        assertThat(bgListener.getResult("username","remuser","bgName","unknownuser")).isEqualTo("No user found with name unknownuser");

    }

    @Test
    public void getResult_Remuser() throws Exception {
        assertThat(bgListener.getResult("username","remuser","bgName","existinguser")).isEqualTo("User existinguser is no longer a member of battlegroup bgName");
        final ArgumentCaptor<BattleGroup> captor = ArgumentCaptor
            .forClass(BattleGroup.class);
        verify(battleGroupEao).save(captor.capture());
        assertThat(captor.getValue().getScimitarUsers()).doesNotContain(existMemberOfBattleGroup);

    }

    @Test
    public void getResult_Adduser_UnknownBg() throws Exception {
        assertThat(bgListener.getResult("username","adduser","unknownbg","user")).isEqualTo("No battlegroup found with name unknownbg");

    }

    @Test
    public void getResult_Adduser_UnknownUser() throws Exception {
        assertThat(bgListener.getResult("username","adduser","bgName","unknownuser")).isEqualTo("No user found with name unknownuser");

    }

    @Test
    public void getResult_Adduser() throws Exception {
        assertThat(bgListener.getResult("username","adduser","bgName","user")).isEqualTo("User user is now a member of battlegroup bgName");
        final ArgumentCaptor<BattleGroup> captor = ArgumentCaptor
            .forClass(BattleGroup.class);
        verify(battleGroupEao).save(captor.capture());
        assertThat(captor.getValue().getScimitarUsers()).contains(user);

    }
}