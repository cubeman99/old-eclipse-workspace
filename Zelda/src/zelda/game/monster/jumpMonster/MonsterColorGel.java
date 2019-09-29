package zelda.game.monster.jumpMonster;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.reactions.ReactionCause;
import zelda.common.reactions.ReactionEffect;
import zelda.common.util.Colors;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.object.dungeon.color.ObjectColorTile;


public class MonsterColorGel extends JumpMonster {
	private DynamicAnimation animationBody;
	private DynamicAnimation animationColor;
	private Animation animationEyes;
	private int tileColor;
	private int colorTimer;
	
	public MonsterColorGel() {
		super(Colors.RED);
		tileColor = Colors.RED;
		
		health.fill(1);
		contactDamage = 2;
		
		colorTimer = 0;
		moveSpeed   = 1;
		stopTimeMin = 80;
		stopTimeMax = 120;
		motionDuration = 80 + GMath.random.nextInt(40);
		jumpVelocityMin = 1.2;
		jumpVelocityMax = 1.2;
//		gravity = 0.08;
		
		hardCollisionBox = new CollisionBox(4, 4, 8, 8);
		softCollisionBox = new CollisionBox(4, 4, 8, 8);
		
		
		animationBody = new DynamicAnimation(new Animation()
				.addFrame(8, 13, 20).addFrame(8, 13, 20, 1, 0), 3, 1, 0);
		animationColor = new DynamicAnimation(new Animation()
				.addFrame(8, 13, 21).addFrame(8, 13, 22), 3, 1, 0);
		animationEyes = new Animation().addFrame(8, 15, 23)
				.addFrame(8, 15, 23, 2, 0);
		
		int x = typeColor == COLOR_ORANGE ? 12 : 10;
		animationIdle = new Animation().addFrame(14, 21);
		animationJump = new Animation(x + 1, 8);
		sprite.newAnimation(animationBody);
		


		setReaction(ReactionCause.FIRE,           new CAMOUFLAGED(new BURN(1)));
		setReaction(ReactionCause.EMBER_SEED,     new CAMOUFLAGED(new BURN(1)));
		setReaction(ReactionCause.SCENT_SEED,     new CAMOUFLAGED(new SEED_REACTION(1, new KILL())));
		setReaction(ReactionCause.MYSTERY_SEED,   new CAMOUFLAGED(new MYSTERY_SEED()));
		setReaction(ReactionCause.ROD_FIRE,       new CAMOUFLAGED(new BURN(1)));
		setReaction(ReactionCause.ARROW,          new CAMOUFLAGED(new KILL()));
		setReaction(ReactionCause.SWORD_BEAM,     new CAMOUFLAGED(new KILL()));
		setReaction(ReactionCause.SWORD,          new CAMOUFLAGED(new KILL()));
		setReaction(ReactionCause.BIGGORON_SWORD, new CAMOUFLAGED(new KILL()));
		setReaction(ReactionCause.BOOMERANG,      new CAMOUFLAGED(new STUN()));
		setReaction(ReactionCause.BOMB,           new CAMOUFLAGED(new KILL()));
		setReaction(ReactionCause.SWITCH_HOOK,    new CAMOUFLAGED(new KILL()));
		
		setReaction(ReactionCause.SHIELD, new PLAYER_DAMAGE(2));
		setReaction(ReactionCause.SHOVEL, null);
	}
	
	private int getColorIndex(int color) {
		if (color == Colors.RED)
			return 0;
		if (color == Colors.YELLOW)
			return 1;
		return 2;
	}
	
	public boolean isCamouflaged() {
		return (typeColor == tileColor);
	}
	
	@Override
	protected void onLand() {
		super.onLand();
		position.set(getLocation().scaledBy(16));
	}
	
	@Override
	public Vector getShadowDrawPosition() {
		return getCenter().plus(0, 6);
	}
	
	@Override
	protected void onJump() {
		super.onJump();
		
		ArrayList<Integer> possibleDirs = new ArrayList<Integer>();
		for (int i = 0; i < 8; i++) {
			Point loc  = getLocation().plus(Direction.getAngledDirPoint(i));
			Vector pos = new Vector(loc.scaledBy(16));
			if (Collision.placeMeeting(this, pos, ObjectColorTile.class))
				possibleDirs.add(i);
		}
		int dir = 0;
		if (possibleDirs.size() > 0)
			dir = possibleDirs.get(GMath.random.nextInt(possibleDirs.size()));
		double speed = (dir % 2 == 0 ? 0.75 : 1);
		velocity.setPolar(speed, dir * GMath.QUARTER_PI);
		sprite.setFrameIndex(GMath.random.nextInt(2));
	}
	
	@Override
	public void updateMotion() {
//		typeColor = Colors.YELLOW;
//		tileColor = Colors.YELLOW;
		if (tileColor == typeColor) {
			super.updateMotion();
			sprite.setSpeed(inAir() ? 0 : 1);
		}
		else
			sprite.setSpeed(0);
		
		ObjectColorTile t = (ObjectColorTile) Collision.getInstanceMeeting(
				getCenter(), this, ObjectColorTile.class);
		if (t != null && t.getColor() != typeColor) {
			tileColor = t.getColor();
			if (colorTimer++ >= 60) {
    			typeColor = t.getColor();
    			colorTimer = 0;
    			sprite.changeAnimation(animationBody.getVariant(getColorIndex(typeColor)));
			}
		}
		
		sprite.changeAnimation(animationBody.getVariant(getColorIndex(typeColor)));
	}
	
	@Override
	public void draw() {
		super.draw();
		if (inAir() || motionTimer > motionDuration - 40) {
    		AnimationFrame af = animationEyes.getFrame(sprite.getFrameIndex());
    		Draw.drawAnimationFrame(af, Resources.SHEET_MONSTERS,
    				position.minus(getDrawOffset(), zPosition));
		}
		if (tileColor != typeColor) {
    		Animation anim = animationColor.getVariant(getColorIndex(tileColor));
    		Draw.drawAnimationFrame(anim.getFrame(sprite.getFrameIndex()),
    				Resources.SHEET_MONSTERS, position.minus(getDrawOffset(), zPosition));
		}
	}
	
	@Override
	public MonsterColorGel clone() {
		return new MonsterColorGel();
	}

	protected final class CAMOUFLAGED implements ReactionEffect {
		private ReactionEffect reaction;

		public CAMOUFLAGED(ReactionEffect reaction) {
			this.reaction = reaction;
		}

		@Override
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			if (!isCamouflaged()) {
				reaction.trigger(reactionCuase, level, source, sourcePos);
			}
		}
	}
}
