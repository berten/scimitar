package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.BattleGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttackListListenerTest {
    @InjectMocks
    private AttackListListener attackListListener;
    @Mock
    private AttackService attackService;

    @Test
    public void getCommand() throws Exception {
        assertThat(attackListListener.getCommand()).isEqualTo("attacklist");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(attackListListener.getPattern()).isEqualTo("");
    }

    @Test
    public void getResult() throws Exception {
        final Attack a1 = new Attack();
        a1.setId(12);
        a1.setLandTick(300);
        a1.setComment("een commentaar");

        final Attack a2 = new Attack();
        a2.setId(13);
        a2.setLandTick(301);
        final BattleGroup battleGroup = new BattleGroup();
        battleGroup.setName("bgName");
        a2.setBattleGroup(battleGroup);
        when(attackService.findLast5Attacks()).thenReturn(Arrays.asList(a1,a2));
        assertThat(attackListListener.getResult()).isEqualTo("ID: 12 Comment: LT 300 een commentaar | ID: 13 BG:bgName Comment: LT 301");
    }
}