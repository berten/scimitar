package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.BattleGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttackListenerTest {
    @InjectMocks
    private AttackListener attackListener;
    @Mock
    private AttackHelper attackHelper;
    @Mock
    private AttackService attackService;

    @Test
    public void getCommand() throws Exception {
        assertThat(attackListener.getCommand()).isEqualTo("attack");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(attackListener.getPattern()).isEqualTo(
            "new lt|eta waves <bg> | addtarget id x y <z> | remtarget id x y <z> | comment your comment");
    }

    @Test
    public void getResult_Comment() throws Exception {
        final Attack a = new Attack();
        when(attackService.addComment(123, "een aantal woorden")).thenReturn(a);
        when(attackHelper.getAttackInfo(a)).thenReturn("attackInfo");
        assertThat(attackListener
            .getResult("comment", "123", "een", "aantal", "woorden"))
            .isEqualTo("attackInfo");
    }

    @Test
    public void getResult_new_NoBg() throws Exception {
        final Attack a = new Attack();
        a.setLandTick(14);
        a.setWaves(2);
        a.setId(1);
        when(attackService.createAttack(14,2)).thenReturn(a);
        assertThat(attackListener.getResult("new","14","2")).isEqualTo("New attack created with id: 1");
    }

    @Test
    public void getResult_new_WithBg() throws Exception {
        final Attack a = new Attack();
        a.setLandTick(14);
        a.setWaves(2);
        a.setId(1);
        final BattleGroup battleGroup = new BattleGroup();
        battleGroup.setName("bgName");
        a.setBattleGroup(battleGroup);
        when(attackService.createAttack(14,2,"bgName")).thenReturn(a);
        assertThat(attackListener.getResult("new","14","2","bgName")).isEqualTo("New attack created for bg bgName with id: 1");
    }

    @Test
    public void getResult_addTarget_Galxy() throws Exception {
        final Attack a = new Attack();
        when(attackService.addTarget(123,1,2)).thenReturn(a);
        when(attackHelper.getAttackInfo(a)).thenReturn("attackInfo");
        assertThat(attackListener.getResult("addtarget","123","1","2")).isEqualTo("attackInfo");
    }

    @Test
    public void getResult_addTarget_Planet() throws Exception {
        final Attack a = new Attack();
        when(attackService.addTarget(123,1,2,3)).thenReturn(a);
        when(attackHelper.getAttackInfo(a)).thenReturn("attackInfo");
        assertThat(attackListener.getResult("addtarget","123","1","2","3")).isEqualTo("attackInfo");
    }

    @Test
    public void getResult_remTarget_Galxy() throws Exception {
        final Attack a = new Attack();
        when(attackService.removeTarget(123,1,2)).thenReturn(a);
        when(attackHelper.getAttackInfo(a)).thenReturn("attackInfo");
        assertThat(attackListener.getResult("remtarget","123","1","2")).isEqualTo("attackInfo");
    }

    @Test
    public void getResult_remarget_Planet() throws Exception {
        final Attack a = new Attack();
        when(attackService.removeTarget(123,1,2,3)).thenReturn(a);
        when(attackHelper.getAttackInfo(a)).thenReturn("attackInfo");
        assertThat(attackListener.getResult("remtarget","123","1","2","3")).isEqualTo("attackInfo");
    }


}