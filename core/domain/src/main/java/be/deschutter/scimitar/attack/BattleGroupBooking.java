package be.deschutter.scimitar.attack;

import be.deschutter.scimitar.user.BattleGroup;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("BG")
public class BattleGroupBooking extends Booking {
    private BattleGroup battleGroup;

    public BattleGroupBooking(final BattleGroup battleGroup) {
        this.battleGroup = battleGroup;
    }

    public BattleGroupBooking() {
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }

    public void setBattleGroup(final BattleGroup battleGroup) {
        this.battleGroup = battleGroup;
    }

    @Override
    public String getInfo() {
        return "BG: " + battleGroup.getName();
    }


}
