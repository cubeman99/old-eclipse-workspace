package game.powerups;

import common.Settings;
import game.entity.lasers.LaserHoming;
import game.nodes.Enemy;

public class PowerSuperLasers extends PowerUp {

	public PowerSuperLasers(Enemy e) {
		super(e, Settings.POWERUP_DURATION, 3);
	}
	
	@Override
	protected void begin() {
		control.superLasers = true;
	}
	
	@Override
	protected void finish() {
		control.superLasers = false;
	}
}
