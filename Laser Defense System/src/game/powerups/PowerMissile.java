package game.powerups;

import common.Settings;
import game.nodes.Enemy;

public class PowerMissile extends PowerUp {

	public PowerMissile(Enemy e) {
		super(e, 0, 2);
	}
	
	@Override
	protected void begin() {
		control.player.fireMissile();
	}
}
