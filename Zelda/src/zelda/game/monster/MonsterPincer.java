package zelda.game.monster;

import zelda.common.Resources;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.GMath;
import zelda.game.player.Player;
import zelda.game.world.tile.ObjectTile;


public class MonsterPincer extends Monster {
	protected Sprite spriteEyes;
	protected Sprite spriteHead;
	protected Sprite spriteBody;
	protected int timer;
	protected double distance;
	protected boolean preparing;
	protected boolean striking;
	protected boolean returning;
	protected Vector holePosition;
	protected Vector strikeVelocity;


	public MonsterPincer() {
		super();

		collideWithWorld = false;
		fallsInHoles = false;
		bumpable = false;
		contactDamage = 2;
		passable = true;
		health.fill(3);

		properties.set("fixed_spawn", true);

		setReaction(ReactionCause.GALE_SEED, null);
		setReaction(ReactionCause.SWITCH_HOOK, new DAMAGE(1));

		timer = 0;
		preparing = false;
		striking = false;
		returning = false;
		distance = 0;
		strikeVelocity = new Vector();

		spriteEyes = new Sprite(Resources.SHEET_MONSTERS, 9, 8);
		spriteBody = new Sprite(Resources.SHEET_MONSTERS, 8, 8);
		spriteHead = new Sprite(Resources.SHEET_MONSTERS);
		spriteHead.newAnimation(new DynamicAnimation(new Animation(0, 8), 8, 1,
				0));
		spriteHead.setVariation(6);

		sprite = spriteHead;
	}

	@Override
	public MonsterPincer clone() {
		return new MonsterPincer();
	}

	@Override
	public void enterFrame(ObjectTile t) {
		super.enterFrame(t);
		holePosition = new Vector(t.getPosition());
	}

	@Override
	public void updateAI() {
		Player player = game.getPlayer();
		passable = true;

		if (striking) {
			passable = false;

			if (returning) {
				timer++;
				if (timer > 8)
					velocity.setPolar(1,
							GMath.direction(position, holePosition));

				if (position.distanceTo(holePosition) < 1) {
					position.set(holePosition);
					returning = false;
					striking = false;
					timer = 0;
					velocity.zero();
				}
			}
			else {
				velocity.set(strikeVelocity);
				distance += velocity.length();

				if (distance >= 32) {
					returning = true;
					timer = 0;
					velocity.zero();
				}
			}
		}
		else if (preparing) {
			timer++;
			if (timer > 35) {
				preparing = false;
				striking = true;
				returning = false;
				distance = 0;

				int div = 16;
				faceDir = (int) (GMath.direction(getCenter(),
						player.getCenter())
						/ (GMath.TWO_PI / 8) + 0.5)
						% div;
				double d = (int) (GMath.direction(getCenter(),
						player.getCenter())
						/ (GMath.TWO_PI / div) + 0.5)
						% div;

				if (faceDir < 0)
					faceDir = 8 + faceDir;

				strikeVelocity.setPolar(2, d * (GMath.TWO_PI / div));
				velocity.set(strikeVelocity);
			}
		}
		else {
			timer++;
			if (timer > 30 && getCenter().distanceTo(player.getCenter()) < 40) {
				preparing = true;
				timer = 0;
			}
		}

		spriteHead.setSheet(getNormalSheet());
		spriteBody.setSheet(getNormalSheet());
		spriteEyes.setSheet(getNormalSheet());
	}

	@Override
	public void draw() {
		if (striking) {
			for (int i = 1; i < 4; i++) {
				Vector add = new Vector(holePosition, position).scale(i / 5.0);
				Draw.drawSprite(spriteBody, holePosition.plus(add));
			}

			Draw.drawSprite(spriteHead, position);
		}
		else if (preparing) {
			Draw.drawSprite(spriteEyes, position);
		}
	}
}
