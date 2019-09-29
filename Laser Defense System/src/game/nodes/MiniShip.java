package game.nodes;

import game.Ring;
import common.GMath;
import common.Settings;
import common.graphics.Draw;
import common.graphics.SpriteSheet;
import main.ImageLoader;


public class MiniShip extends Enemy {
	private SpriteSheet sheet;
	private Ring ring;
	private double radiusSpeed;
	private double travelDistance;
	private double angleSpeedScale;
	private int subImage;
	
	
	public MiniShip(Node n) {
		super(n.control, n.getRadius(), n.getAngle());
		this.health     = Settings.MINISHIP_HEALTH;
		this.damage     = Settings.MINISHIP_DAMAGE;
		this.maskRadius = 22;
		
		this.sheet           = ImageLoader.getSpriteSheet("miniShip");
		this.ring            = n.ring;
		this.subImage        = 0;
		this.radiusSpeed     = 0;
		this.angleSpeedScale = 1;
		this.travelDistance  = 0;
		
		initialize();
	}

	@Override
	public void update() {
		subImage += 1;
		
		if (travelDistance > 64) {
    		angleSpeedScale = Math.max(0, angleSpeedScale - 0.02);
		}
		
		radiusSpeed     = GMath.min(1, radiusSpeed + 0.005);
		radius         -= radiusSpeed + control.getRingSpeed();
		travelDistance += radiusSpeed;
		angle          += ring.getAngleSpeed() * angleSpeedScale;
		
		if (radius < Settings.RING_CRASH_RADIUS) {
			crash();
		}
	}

	@Override
	public void draw() {
		Draw.draw(sheet.getSprite(subImage % 3), getRadius(), getAngle(), getAngle() + GMath.PI);
	}

}
