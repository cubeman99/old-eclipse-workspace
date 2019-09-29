package zelda.game.monster;

import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.monster.Monster.BUMP_OVERRIDE;
import zelda.game.monster.Monster.COMBO;
import zelda.game.monster.Monster.DAMAGE;
import zelda.game.monster.Monster.EFFECT_CLING;
import zelda.game.monster.Monster.IF_LEVEL;
import zelda.game.monster.Monster.KILL;
import zelda.game.monster.Monster.PLAYER_BUMP;
import zelda.game.monster.Monster.PROJECTILE_CRASH;
import zelda.game.monster.Monster.STUN;


public class MonsterBladeTrap extends BasicMonster {
	private boolean charging;
	private boolean returning;
	private Vector spawnPosition;
	
	public MonsterBladeTrap(int typeColor) {
		super(typeColor, TYPE_NONE);
		
		health.fill(1);
		
		moveSpeed = 1.5;
		charging = false;
		
		sprite.newAnimation(new Animation(0, 12 + typeColor));
		
		
		setReaction(ReactionCause.BOMB, null);
		setReaction(ReactionCause.SCENT_SEED, null);
		setReaction(ReactionCause.BIGGORON_SWORD, null);
		setReaction(ReactionCause.BOOMERANG, null);

		setReaction(ReactionCause.SHOVEL,       new PLAYER_BUMP());
		setReaction(ReactionCause.SHIELD,       new PLAYER_BUMP());
		setReaction(ReactionCause.SWORD,        new PLAYER_BUMP(new EFFECT_CLING()));
		setReaction(ReactionCause.EMBER_SEED,   null);
		setReaction(ReactionCause.SCENT_SEED,   null);
		setReaction(ReactionCause.PEGASUS_SEED, null);
		setReaction(ReactionCause.GALE_SEED,    null);
		setReaction(ReactionCause.FIRE,         null);
		setReaction(ReactionCause.ROD_FIRE,     null);
		setReaction(ReactionCause.SWITCH_HOOK,  new EFFECT_CLING());
		setReaction(ReactionCause.SWORD_BEAM,   new EFFECT_CLING(true));
		setReaction(ReactionCause.ARROW,        new PROJECTILE_CRASH());

	}
	
	@Override
	public void begin() {
		super.begin();
		spawnPosition = new Vector(position);
	}
	
	@Override
	public void updateMotion() {
		collideWithWorld = false;
		
		if (charging) {
			collideWithWorld = true;
			speed = Math.min(speed + 0.2, moveSpeed);
			velocity.set(Direction.lengthVector(speed, dir));
			if (placeColliding() || Collision.placeMeeting(this, position.plus(velocity), MonsterBladeTrap.class)) {
				charging  = false;
				returning = true;
				velocity.zero();
				Sounds.EFFECT_CLING.play();
			}
		}
		else if (returning) {
			velocity.setPolar(0.75, GMath.direction(position, spawnPosition));
			if (position.distanceTo(spawnPosition) < 0.5) {
				position.set(spawnPosition);
				returning = false;
				velocity.zero();
			}
		}
		else if (inLineWithPlayer(15)) {
			dir = (int) (GMath.direction(getCenter(), game.getPlayer()
					.getCenter()) / GMath.HALF_PI + 0.5) % 4;
			velocity.set(Direction.lengthVector(8, dir));
			
			if (!placeColliding()) {
    			speed = 0;
    			charging = true;
    			Sounds.ITEM_SWORD_SLASH[1].play();
			}
			velocity.zero();
		}
	}
	
	@Override
	public MonsterBladeTrap clone() {
		return new MonsterBladeTrap(typeColor);
	}
}
