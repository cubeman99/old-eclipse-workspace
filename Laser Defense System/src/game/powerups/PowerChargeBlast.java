package game.powerups;

import common.Settings;
import game.nodes.Enemy;

public class PowerChargeBlast extends PowerUp {

	public PowerChargeBlast(Enemy e) {
		super(e, 0, 6);
	}
	
	@Override
	protected void begin() {
		control.player.giveChargeBlast();
	}
}
