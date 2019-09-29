package game.powerups;

import common.Settings;
import game.nodes.Enemy;

public class PowerArmor extends PowerUp {

	public PowerArmor(Enemy e) {
		super(e, 0, 4);
	}
	
	@Override
	protected void begin() {
		control.addArmor(Settings.POWERUP_ARMOR_AMOUNT);
	}
}
