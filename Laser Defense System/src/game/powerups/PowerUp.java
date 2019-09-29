package game.powerups;

import main.ImageLoader;
import common.GMath;
import common.Settings;
import common.Timer;
import common.graphics.Draw;
import common.graphics.Sprite;
import game.Control;
import game.entity.Entity;
import game.nodes.Enemy;

public abstract class PowerUp extends Entity {
	public Control control;
	private double speed;
	private double radius;
	private double angle;
	private double rotation;
	private double rotationSpeed;
	private Sprite sprite;
	private Timer durationTimer;
	private boolean falling;
	
	public PowerUp(Enemy e, int duration, int subImage) {
		super(0);
		
		this.control       = e.control;
		this.radius        = e.getRadius();
		this.angle         = e.getAngle();
		this.speed         = 0;
		this.rotation      = GMath.getRandomAngle();
		this.rotationSpeed = GMath.toRadians(4.5 - (GMath.random.nextDouble() * 9.0));
		this.sprite        = ImageLoader.getSprite("powerUpIcons", subImage);
		this.durationTimer = new Timer(duration);
		this.falling       = true;
	}
	
	/** Return whether this power up lasts for a duration. **/
	public boolean hasDuration() {
		return (durationTimer.getExpireTime() > 0);
	}
	
	
	/** Start the duration of this power-up. **/
	protected void begin() {}
	
	/** End the duration of this power-up. **/
	protected void finish() {}

	/** Tick every step. **/
	protected void tick() {}
	
	
	@Override
	public void update() {
		
		if (falling) {
			rotation += rotationSpeed;
			radius   -= speed;
			speed    += 0.125;
			
    		if (radius < Settings.RING_CRASH_RADIUS) {
    			falling = false;
    			durationTimer.start();
    			begin();
    		}
		}
		if (!falling) {
			tick();
			
			if (durationTimer.isExpired()) {
				finish();
				destroy();
			}
		}
	}
	
	@Override
	public void draw() {
		if (falling)
			Draw.draw(sprite, radius, angle, rotation);
	}
}
