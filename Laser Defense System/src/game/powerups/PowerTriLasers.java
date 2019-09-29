package game.powerups;

import common.Settings;
import game.entity.lasers.LaserHoming;
import game.nodes.Enemy;

public class PowerTriLasers extends PowerUp {

	public PowerTriLasers(Enemy e) {
		super(e, Settings.POWERUP_DURATION, 1);
	}
	
	@Override
	protected void begin() {
		control.triLasers = true;
	}
	
	@Override
	protected void finish() {
		control.triLasers= false;
	}
}
