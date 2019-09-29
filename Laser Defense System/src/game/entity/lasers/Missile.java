package game.entity.lasers;

import main.ImageLoader;
import common.GMath;
import common.Settings;
import common.Vector;
import common.graphics.Draw;
import common.graphics.SpriteSheet;
import game.Control;
import game.entity.Entity;
import game.entity.particles.ExplosionCloud;
import game.nodes.Enemy;

public class Missile extends Laser {
	private double speed;
	private SpriteSheet sheet;
	private double subImage;
	
	
	public Missile(Control control) {
		super(control, 0, 0);
		
		this.radius   = 50;
		this.angle    = GMath.getRandomAngle();
		this.damage   = Settings.MISSILE_DAMAGE;
		this.speed    = 0;
		this.sheet    = ImageLoader.getSpriteSheet("missile");
		this.subImage = 0;
		
		Enemy target  = control.getNearestEnemy(Vector.ORIGIN);
		if (target != null)
			angle = target.getAngle();
	}
	
	private void explode() {
		for (int i = 0; i < 50; i++) {
			control.addEntity(new ExplosionCloud(getRadius(), getAngle()));
		}
	}
	
	
	@Override
	public void update() {
		speed     = Math.min(5, speed + 0.25);
		subImage += 0.5;
		radius   += speed;
		sprite    = sheet.getSprite((int) subImage % 3);
		
		super.update();
		
		if (isDestroyed())
			explode();
	}
}
