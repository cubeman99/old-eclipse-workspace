package game.entity.lasers;

import common.Settings;


public class LaserSuper extends Laser {

	public LaserSuper() {
		super(1);
		this.damage = Settings.LASER_SUPER_DAMAGE;
	}
	
	@Override
	public void update() {
		radius += 5;
		super.update();
	}
}
