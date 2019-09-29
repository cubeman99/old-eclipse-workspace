package zelda.game.monster;

import java.awt.Color;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;
import zelda.game.monster.Monster.BUMP_OVERRIDE;
import zelda.game.monster.Monster.COMBO;
import zelda.game.monster.Monster.DAMAGE;
import zelda.game.monster.Monster.EFFECT_CLING;
import zelda.game.monster.Monster.IF_LEVEL;
import zelda.game.monster.Monster.KILL;
import zelda.game.monster.Monster.PLAYER_BUMP;
import zelda.game.monster.Monster.PROJECTILE_CRASH;
import zelda.game.monster.Monster.STUN;


public class MonsterSpark extends BasicMonster {
	private int hugDir;
	private int timer;
	
	public MonsterSpark() {
		super();

		health.fill(1);
		
		hardCollisionBox = new CollisionBox(1, 1, 14, 14);
		softCollisionBox = new CollisionBox(1, 1, 14, 14);
		
		moveSpeed = 1;
		bumpable  = false;
		hugDir    = -1;
		dir       = GMath.random.nextInt(4);
		
		sprite.newAnimation(Animations.createStrip(2, 14, 12, 2));

		

		setReaction(ReactionCause.BOOMERANG, new DISSAPEAR()); // TODO: Drop Fairy
		
		setReaction(ReactionCause.BOMB, null);
		setReaction(ReactionCause.BIGGORON_SWORD, null);
		setReaction(ReactionCause.SHOVEL, new PLAYER_BUMP());
		setReaction(ReactionCause.SWORD, null);
		setReaction(ReactionCause.FIRE, null);
		setReaction(ReactionCause.EMBER_SEED, null);
		setReaction(ReactionCause.ROD_FIRE, null);
		setReaction(ReactionCause.SCENT_SEED, null);
		setReaction(ReactionCause.PEGASUS_SEED, null);
		setReaction(ReactionCause.GALE_SEED, null);
		setReaction(ReactionCause.SWITCH_HOOK, null);
		setReaction(ReactionCause.SWORD_BEAM, new EFFECT_CLING(true));
		setReaction(ReactionCause.ARROW, new PROJECTILE_CRASH());

	}
	
	private boolean isHugging() {
		return (hugDir >= 0);
	}
	
	private boolean isPlaceHuggingAt(int dir, Vector position) {
		Vector testPos = position.plus(Direction.lengthVector(moveSpeed, dir));
		return placeColliding(this, testPos);
	}
	
	private boolean isHuggingAt(int dir) {
		Vector testPos = position.plus(Direction.lengthVector(moveSpeed, dir));
		return placeColliding(this, testPos);
	}
	
	private boolean checkHug(int startDir) {
		for (int i = 0; i < Direction.NUM_DIRS; i++) {
			int testDir = (startDir + i) % 4;
			if (isHuggingAt(testDir)) {
				hugDir = testDir;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public MonsterSpark clone() {
		return new MonsterSpark();
	}
	
	@Override
	public void updateMotion() {
		velocity.set(Direction.lengthVector(moveSpeed, dir));
		
		// Avoid walls.
		if (placeColliding(this, position.plus(velocity))) {
			int[] testDirs = GMath.random.nextBoolean() ? new int[] {dir + 1, dir + 3, dir + 2} : new int[] {dir + 3, dir + 1, dir + 2};
			if (isHugging()) {
				int dirPrev = dir;
				dir    = (hugDir + 2) % 4;
				hugDir = dirPrev;
				velocity.set(Direction.lengthVector(moveSpeed, dir));
				checkHug(hugDir);
			}
			else {
    			for (int i = 0; i < 3; i++) {
    				int testDir = testDirs[i] % 4;
    				if (!placeColliding(this, position.plus(Direction.lengthVector(moveSpeed, testDir)))) {
    					hugDir = dir;
    					dir    = testDir;
    					velocity.set(Direction.lengthVector(moveSpeed, dir));
    					checkHug(hugDir);
    					break;
    				}
    			}
			}
		}
		
		// Turn around corners.
		if (isHuggingAt(hugDir) && !isPlaceHuggingAt(hugDir, position.plus(velocity))) {
			int dirPrev = dir;
			dir         = hugDir;
			hugDir      = (dirPrev + 2) % 4;
			velocity.set(Direction.lengthVector(moveSpeed, dir));
			performCollisions();
			position.add(Direction.lengthVector(moveSpeed, dirPrev));
			velocity.set(Direction.lengthVector(moveSpeed, dir));
		}
	}
}
