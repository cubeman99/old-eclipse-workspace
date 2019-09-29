package game.entity.particles;

import main.ImageLoader;

public class LaserHomingTrail extends Particle {

	public LaserHomingTrail(double radius, double angle, double rotation) {
		super(0, radius, angle);
		
		this.fadeTime        = 0;
		this.sprite          = ImageLoader.getSprite("lasers", 3);
		this.alphaLowerSpeed = 0.025;
		this.rotation        = rotation;
		
		initialize();
	}
	
}
