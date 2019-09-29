package game.powerups;

import common.Settings;
import game.nodes.Enemy;

public class PowerHealth extends PowerUp {

	public PowerHealth(Enemy e) {
		super(e, 0, 0);
	}
	
	@Override
	protected void begin() {
		control.healPlanet(Settings.POWERUP_HEALTH_AMOUNT);
	}
}
