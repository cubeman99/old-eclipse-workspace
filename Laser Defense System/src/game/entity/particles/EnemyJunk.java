package game.entity.particles;

import game.nodes.Enemy;
import game.nodes.Node;
import common.GMath;
import main.ImageLoader;

public class EnemyJunk extends Particle {

	public EnemyJunk(Enemy e) {
		super(100, e.getRadius(), e.getAngle());
		
		this.fadeTime        = 270;
		this.sprite          = ImageLoader.getSprite("nodeJunk", GMath.random.nextInt(7));
		this.alphaLowerSpeed = 0.025;
		this.rotation        = GMath.getRandomAngle();
		this.rotationSpeed   = GMath.toRadians(4 - (GMath.random.nextDouble() * 8));
		this.direction       = GMath.getRandomAngle();
		this.speed           = GMath.random.nextDouble() * 1.5;
		
		initialize();
	}
	
}
