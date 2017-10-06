package be.deschutter.scimitar.attack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttackShowListenerTest {
    @InjectMocks
    private AttackShowListener attackShowListener;
    @Mock
    private AttackService attackService;
    @Mock
    private AttackHelper attackHelper;

    @Before
    public void setUp() throws Exception {
        final Attack attack = new Attack();
        when(attackService.findAttack(123)).thenReturn(attack);
        when(attackHelper.getAttackInfo(attack)).thenReturn("attackString");
    }

    @Test
    public void getCommand() throws Exception {
        assertThat(attackShowListener.getCommand()).isEqualTo("attackshow");
    }

    @Test
    public void getPattern() throws Exception {
        assertThat(attackShowListener.getPattern()).isEqualTo("id");
    }

    @Test
    public void getResult() throws Exception {
        assertThat(attackShowListener.getResult("123"))
            .isEqualTo("attackString");
    }

}