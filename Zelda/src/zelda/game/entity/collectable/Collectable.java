package zelda.game.entity.collectable;

import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.EntityObject;
import zelda.main.Sound;


public class Collectable extends EntityObject {
	protected static final int FADE_TIME = 400;
	protected static final int LIFE_TIME = 513;

	protected int timer;
	protected int amount;
	protected Currency currency;
	protected Sound sound;

	

	// ================== CONSTRUCTORS ================== //

	public Collectable(int amount, Currency curr, Sprite spriteCollectable, Sound sound) {
		super();

		this.amount   = amount;
		this.currency = curr;
		this.sprite   = new Sprite(spriteCollectable);
		this.sound    = sound;
		this.timer    = 0;

		hardCollisionBox = new CollisionBox(-3, -5, 6, 6);
		softCollisionBox = new CollisionBox(-4, -8, 10, 10);

		zVelocity = 1.3;
		collideWithWorld = false;
		baseDepth = 10;
		collideWithFrameBoundaries = true;
	}

	public Collectable(Collectable copy) {
		this(copy.amount, copy.currency, copy.sprite, copy.sound);
	}



	// =================== ACCESSORS =================== //

	public boolean isReady() {
		return (timer > 20);
	}



	// ==================== MUTATORS ==================== //

	public void collect() {
		if (currency != null) {
			currency.give(amount);
			Sounds.play(sound);
		}
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public Collectable clone() {
		return new Collectable(this);
	}

	@Override
	public void update() {
		super.update();
		collideWithWorld = true;
		timer++;

		if (timer == FADE_TIME) {
			sprite.getAnimation().scale(2).addFrame(2, -1, -1);
		}
		if (timer > LIFE_TIME) {
			destroy();
		}
		if (timer > 20
				&& Math.abs(zPosition - game.getPlayer().getZPosition()) < 16) {
			if (Collision.isTouching(softCollidable, game.getPlayer())
					|| game.getPlayer().checkSwordTouch(softCollidable)) {
				collect();
				destroy();
			}
		}
	}
}
