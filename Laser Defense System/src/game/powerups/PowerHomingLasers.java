package game.powerups;

import common.Settings;
import game.entity.lasers.LaserHoming;
import game.nodes.Enemy;

public class PowerHomingLasers extends PowerUp {

	public PowerHomingLasers(Enemy e) {
		super(e, 550, 5);
	}
	
	@Override
	protected void tick() {
		control.player.fireLaser(new LaserHoming(control));
	}
}
