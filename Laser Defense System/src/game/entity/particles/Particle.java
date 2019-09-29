package game.entity.particles;

import common.GMath;
import common.Timer;
import common.Vector;
import common.graphics.Draw;
import common.graphics.Sprite;
import game.entity.Entity;


public class Particle extends Entity {
	protected Sprite sprite;
	protected Timer fadeTimer;
	protected double radius;
	protected double angle;
	protected double rotation;
	protected double rotationSpeed;
	protected double speed;
	protected double direction;
	protected int fadeTime;
	protected double alpha;
	protected double alphaScale;
	protected double alphaLowerSpeed;
	
	
	public Particle(int depth, double radius, double angle) {
		super(depth);
		
		this.radius          = radius;
		this.angle           = angle;
		this.rotation        = angle;
		this.rotationSpeed   = 0;
		this.speed           = 0;
		this.direction       = 0;
		this.alpha           = 1;
		this.fadeTime        = 1000;
		this.alphaLowerSpeed = 0.05;
		this.alphaScale      = 1;
		
		initialize();
	}
	
	protected void initialize() {
		fadeTimer = new Timer(fadeTime);
		fadeTimer.start();
	}
	
	public Vector getPosition() {
		return Vector.polarVector(radius, angle);
	}
	
	public void setPosition(Vector v) {
		radius = v.length();
		angle  = v.direction();
	}
	
	
	@Override
	public void update() {
		rotation += rotationSpeed;
		
		if (speed > GMath.EPSILON) {
			setPosition(getPosition().plus(Vector.polarVector(speed, direction)));
		}
		
		if (fadeTimer.isExpired()) {
			alpha -= alphaLowerSpeed;
			if (alpha <= 0)
				destroy();
		}
	}

	@Override
	public void draw() {
		
		Draw.draw(sprite, radius, angle, rotation, alpha * alphaScale);
	}

}
