package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import be.deschutter.scimitar.user.BattleGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttackHelperImplTest {
    @InjectMocks
    private AttackHelperImpl attackHelper;
    @Mock
    private PlanetService planetService;
    private Attack existingAttack;

    @Before
    public void setUp() throws Exception {

        existingAttack = new Attack();
        existingAttack.setLandTick(34);
        existingAttack.setId(666);
        existingAttack.setComment("com1 com2");
        existingAttack.addPlanet("planet3");
        existingAttack.addPlanet("planet4");
        existingAttack.addPlanet("planet5");

        final Planet p3 = new Planet();
        p3.setId("planet3");
        p3.setX(4);
        p3.setY(5);
        p3.setZ(6);

        when(planetService.findBy("planet3")).thenReturn(p3);
        final Planet p4 = new Planet();
        p4.setId("planet4");
        p4.setX(7);
        p4.setY(8);
        p4.setZ(9);

        when(planetService.findBy("planet4")).thenReturn(p4);

        final Planet p5 = new Planet();
        p5.setId("planet5");
        p5.setX(4);
        p5.setY(5);
        p5.setZ(7);

        when(planetService.findBy("planet5")).thenReturn(p5);
    }

    @Test
    public void getAttackInfo() throws Exception {
        assertThat(attackHelper.getAttackInfo(existingAttack)).isEqualTo(
            "Attack 666: Comment: LT 34 com1 com2 Targets: 4:5:6 4:5:7 7:8:9");
    }

    @Test
    public void getAttackInfo_withBg() throws Exception {
        final BattleGroup battleGroup = new BattleGroup();
        battleGroup.setName("bgName");
        existingAttack.setBattleGroup(battleGroup);
        assertThat(attackHelper.getAttackInfo(existingAttack)).isEqualTo(
            "Attack 666: BG: bgName Comment: LT 34 com1 com2 Targets: 4:5:6 4:5:7 7:8:9");
    }

}