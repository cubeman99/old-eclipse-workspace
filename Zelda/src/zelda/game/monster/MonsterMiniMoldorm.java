package zelda.game.monster;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.GMath;
import zelda.game.world.tile.ObjectTile;


public class MonsterMiniMoldorm extends BasicMonster {
	private static final int NUM_BODY_PARTS = 2;
	private static final int MAX_HISTORY = 20;

	private double directionSpeed;
	private int timer;
	private Vector[] bodyPositions;
	private Sprite[] bodySprites;
	private ArrayList<Vector> positionHistory;



	public MonsterMiniMoldorm() {
		super();

		health.fill(4);
		contactDamage = 2;
		reboundOffFrameEdge = true;
		reboundOffWalls = true;
		moveSpeed = 1.0;
		sprite.newAnimation(new DynamicAnimation(new Animation(6, 17), 8, 1, 0));
		velocity.setPolar(moveSpeed, GMath.random.nextDouble() * GMath.TWO_PI);

		setReaction(ReactionCause.FIRE, null);
		setReaction(ReactionCause.ROD_FIRE, null);
		setReaction(ReactionCause.EMBER_SEED, null);
		setReaction(ReactionCause.GALE_SEED, null);
		setReaction(ReactionCause.PEGASUS_SEED, null);
		setReaction(ReactionCause.BOOMERANG, null);
		setReaction(ReactionCause.SWITCH_HOOK, new DAMAGE(1));

		directionSpeed = 0.05;
		timer = 0;
		positionHistory = new ArrayList<Vector>();
		bodyPositions = new Vector[NUM_BODY_PARTS];
		bodySprites = new Sprite[NUM_BODY_PARTS];
		for (int i = 0; i < NUM_BODY_PARTS; i++) {
			bodyPositions[i] = new Vector(position);
			bodySprites[i] = new Sprite(Resources.SHEET_MONSTERS, 14 + i, 17);
		}
	}

	@Override
	public void enterFrame(ObjectTile t) {
		super.enterFrame(t);

		positionHistory.clear();
		for (int i = 0; i < MAX_HISTORY; i++) {
			positionHistory.add(new Vector(position));
		}
	}

	@Override
	public MonsterMiniMoldorm clone() {
		return new MonsterMiniMoldorm();
	}

	@Override
	public void update() {
		super.update();

		for (int i = 0; i < NUM_BODY_PARTS; i++) {
			bodyPositions[i].set(positionHistory.get(MAX_HISTORY
					- ((i + 1) * 7)));
		}
		positionHistory.add(new Vector(position));
		positionHistory.remove(0);
	}

	@Override
	public void updateMotion() {
		timer++;
		faceDir = (int) (velocity.direction() / GMath.QUARTER_PI + 0.5) % 8;
		velocity.setPolar(moveSpeed, velocity.direction() + directionSpeed);

		if (timer > 60 && GMath.random.nextInt(60) == 0) {
			directionSpeed *= -1.0;
			timer = 0;
		}

		// TODO: should be part of EntityObject
		/*
		if (reboundOffWalls) {
			if (Collision.placeMeetingSolid(hardCollidable,
					position.plus(velocity.x, 0)))
				velocity.x = -velocity.x;
			if (Collision.placeMeetingSolid(hardCollidable,
					position.plus(0, velocity.y)))
				velocity.y = -velocity.y;
		}
		*/

		if (reboundOffWalls) {
			Vector velPrev = new Vector(velocity);
			
			velocity.y = 0;
			if (placeColliding()) {
				velPrev.x = -velPrev.x;
			}

			velocity.set(0, velPrev.y);
			if (placeColliding())
				velPrev.y = -velPrev.y;
			
			velocity.set(velPrev);
			dir = (int) ((velocity.direction() / (GMath.TWO_PI / numDirectionAngles)) + 0.5);
		}
	}

	@Override
	public void draw() {
		for (int i = NUM_BODY_PARTS - 1; i >= 0; i--) {
			bodySprites[i].setSheet(sprite.getSheet());
			Draw.drawSprite(bodySprites[i], bodyPositions[i]);
		}
		super.draw();
	}
}
