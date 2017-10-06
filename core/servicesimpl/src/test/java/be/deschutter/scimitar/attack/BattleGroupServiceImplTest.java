package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.BattleGroup;
import be.deschutter.scimitar.user.BattleGroupEao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BattleGroupServiceImplTest {
    @InjectMocks
    private BattleGroupServiceImpl battleGroupService;
    @Mock
    private BattleGroupEao battleGroupEao;

    @Test
    public void findByName() throws Exception {
        final BattleGroup bg = new BattleGroup("bgName");
        when(battleGroupEao.findByNameIgnoreCase("bgName")).thenReturn(bg);
        assertThat(battleGroupService.findByName("bgName")).isEqualTo(bg);
    }

    @Test(expected = BattleGroupDoesNotExistException.class)
    public void findByName_notFound() throws Exception {
        try {
            battleGroupService.findByName("bgName");
        } catch (BattleGroupDoesNotExistException e) {
            assertThat(e.getBattlegroupName()).isEqualTo("bgName");
            throw e;
        }
        fail("Should have thrown an exception");
    }

}