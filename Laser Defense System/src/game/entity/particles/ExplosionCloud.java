package game.entity.particles;

import game.nodes.Enemy;
import game.nodes.Node;
import common.GMath;
import main.ImageLoader;

public class ExplosionCloud extends Particle {

	public ExplosionCloud(double radius, double angle) {
		super(110, radius, angle);
		
		this.fadeTime        = 0;
		this.sprite          = ImageLoader.getSprite("explosionCloud", GMath.random.nextInt(6));
		this.alphaLowerSpeed = 0.01;
		this.rotation        = GMath.getRandomAngle();
		this.rotationSpeed   = GMath.toRadians(5 - (GMath.random.nextDouble() * 10));
		this.direction       = GMath.getRandomAngle();
		this.speed           = 0.1 + (GMath.random.nextDouble() * 1.1);
		this.alphaScale      = 0.39 + (GMath.random.nextDouble() * 0.22);
		
		initialize();
	}
	
}
