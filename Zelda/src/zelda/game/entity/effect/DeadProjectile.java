package zelda.game.entity.effect;

import zelda.common.Settings;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Sprite;
import zelda.game.entity.EntityObject;
import zelda.game.entity.projectile.Projectile;


public class DeadProjectile extends EntityObject {
	private int timer;


	public DeadProjectile(Projectile p, Animation animation) {
		super(p.getGame());

		this.timer = 0;

		this.destroyedInHoles = false;
		this.drawShadow = false;
		this.dynamicDepth = false;
		this.bounces = false;

		this.zVelocity = 1.0;
		this.gravity = Settings.GRAVITY_DEAD_PROJECTILE;

		this.position = new Vector(p.getPosition());
		this.velocity = p.getVelocity().inverse().setLength(0.25);
		this.sprite = new Sprite(p.getSprite());
		this.sprite.newAnimation(true, animation);

		setDepth(p.getDepth());
	}


	@Override
	public void update() {
		if (timer++ >= 32) {
			destroy();
		}
		super.update();
	}
}
