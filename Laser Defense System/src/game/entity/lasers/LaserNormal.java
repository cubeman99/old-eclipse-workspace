package game.entity.lasers;

import common.Settings;


public class LaserNormal extends Laser {

	public LaserNormal() {
		super(0);
		this.damage = Settings.LASER_DAMAGE;
	}
	
	@Override
	public void update() {
		radius += 5;
		super.update();
	}
}
