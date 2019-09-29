package zelda.game.monster;

import java.util.ArrayList;
import zelda.common.Settings;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.world.tile.GridTile;


public class MonsterLeever extends BasicMonster {
	private Animation animationMove;
	private Animation animationBurrow;
	private Animation animationUnburrow;
	private boolean burrowed;
	private int timer;
	private int duration;
	private boolean burrowing;



	public MonsterLeever(int typeColor) {
		super(typeColor, TYPE_NONE);

		health.fill(2);
		contactDamage = 2;

		burrowed = true;
		passable = true;
		moveSpeed = 0.5;
		numDirectionAngles = 4;
		timer = 0;
		duration = 100;
		burrowing = false;
		int y = 12;
		if (typeColor == COLOR_BLUE) {
			y = 13;
		}
		else if (typeColor == COLOR_ORANGE) {
			y = 14;
		}

		animationMove = new Animation().addFrame(8, 1, y).addFrame(8, 2, y);
		animationBurrow = new Animation().addFrame(8, 3, y).addFrame(17, 4, y);
		animationUnburrow = new Animation().addFrame(18, 4, y)
				.addFrame(9, 3, y);

		sprite.newAnimation(animationMove);
	}

	private void burrow() {
		duration = 100;
		timer = 0;
		burrowed = true;
		burrowing = true;
		velocity.zero();
		sprite.newAnimation(false, animationBurrow);
	}

	private void unburrow() {
		ArrayList<Point> possibleLocations = new ArrayList<Point>();


		if (typeColor == COLOR_RED) {
			Point loc = new Point(game.getPlayer().getCenter()).dividedBy(16);
			Point dirPoint = Direction.getDirPoint(game.getPlayer()
					.getFaceDir());


			for (int i = 0; i <= 5 && frame.contains(loc); i++) {
				GridTile t = frame.getGridTile(loc);
				if (t != null && !t.isSolid() && i >= 3)
					possibleLocations.add(new Point(loc));
				loc.add(dirPoint);
			}

			if (possibleLocations.size() > 0) {
				int index = GMath.random.nextInt(possibleLocations.size());
				position.set(possibleLocations.get(index).scaledBy(16));
			}
			else {
				// No possible unburrow locations.
				return;
			}
		}
		else if (typeColor == COLOR_BLUE) {
			position.set(getRandomLocation().scaledBy(Settings.TS));
		}

		duration = 180;
		timer = 0;
		burrowed = false;
		burrowing = true;
		sprite.newAnimation(false, animationUnburrow);


		dir = (int) (GMath.direction(getCenter(), game.getPlayer().getCenter())
				/ GMath.HALF_PI + 0.5) % 4;
	}

	@Override
	public MonsterLeever clone() {
		return new MonsterLeever(typeColor);
	}

	@Override
	public void updateMotion() {
		timer++;

		if (burrowing) {
			passable = true;

			if (sprite.isAnimationDone()) {
				burrowing = false;
				sprite.newAnimation(true, animationMove);
			}
		}
		else if (burrowed) {
			passable = true;

			if (timer > duration) {
				unburrow();
			}
		}
		else {
			passable = false;

			if (timer > duration
					|| !Collision.canDodgeCollisions(this,
							position.plus(velocity))
					|| placeOutsideOfFrame(position.plus(velocity))) {
				burrow();
			}
			else if (typeColor == COLOR_RED) {
				velocity.set(Direction.lengthVector(0.5, dir));
			}
			else if (typeColor == COLOR_BLUE) {
				if (timer % 30 == 0) {
					dir = (int) (GMath.direction(getCenter(), game.getPlayer()
							.getCenter())
							/ GMath.HALF_PI + 0.5) % 4;
				}
				velocity.set(Direction.lengthVector(0.5, dir));
			}
			else if (typeColor == COLOR_ORANGE) {
				chasePlayer();
			}
		}
	}

	@Override
	public void draw() {
		if (burrowing || !burrowed)
			super.draw();
	}
}
