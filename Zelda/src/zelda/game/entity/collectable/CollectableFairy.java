package zelda.game.entity.collectable;

import zelda.common.Resources;
import zelda.common.graphics.Animation;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.util.GMath;
import zelda.game.player.Player;


public class CollectableFairy extends Collectable {
	private final double SPEED = 0.8;
	private double directionSpeed;
	private Player player;
	private double hoverHeight;


	public CollectableFairy(Player player) {
		super(24, player.currencyHearts, new Sprite(Resources.SHEET_ICONS_THIN,
				8, 3), null);
		sprite = new Sprite(Resources.SHEET_ICONS_THIN);
		sprite.newAnimation(new DynamicAnimation(new Animation(8, 3),
				new Animation(9, 3)));
		sprite.setOrigin(4, 12);

		this.player = player;

		hoverHeight = 8;
		affectedByGravity = false;
		bounces = false;
		directionSpeed = 0;
	}

	public CollectableFairy(CollectableFairy copy) {
		this(copy.player);
	}

	@Override
	public CollectableFairy clone() {
		return new CollectableFairy(this);
	}

	@Override
	public void update() {
		if (Math.abs(zPosition - hoverHeight) > 2) {
			if (zPosition < hoverHeight)
				zVelocity = Math.min(0.5, zVelocity + 0.05);
			if (zPosition > hoverHeight)
				zVelocity = Math.max(-0.5, zVelocity - gravity);
		}
		else {
			zVelocity = 0;
			zPosition = hoverHeight;
		}

		double len = velocity.length();
		if (Math.abs(len - SPEED) > 0.01) {
			if (len == 0)
				velocity.setPolar(0.04, GMath.random.nextDouble()
						* GMath.TWO_PI);
			velocity.setLength(len + (0.04 * Math.signum(SPEED - len)));
		}
		else {
			velocity.setLength(SPEED);
		}

		velocity.setDirection(velocity.direction() + directionSpeed);
		directionSpeed += 0.05 * (0.5 - GMath.random.nextDouble());
		directionSpeed = Math.max(-0.1, Math.min(0.1, directionSpeed));

		if (velocity.x > 0)
			sprite.newAnimation(new Animation(8, 3));
		if (velocity.x < 0)
			sprite.newAnimation(new Animation(9, 3));

		super.update();
	}
}
