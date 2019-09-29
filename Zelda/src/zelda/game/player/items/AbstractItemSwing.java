package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.collision.Collidable;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.player.Player;


public abstract class AbstractItemSwing extends Item {
	protected static final int NUM_SWING_DIRS = 8;
	protected static final int PEAK_SWING_FRAME = 2;
	protected boolean swinging;
	protected Sprite sprite;
	protected int[] swingDirs;
	protected int[] swingHitDirs;
	protected Vector[] swingOffsetPoints;
	protected double swingItemLength;
	protected double swingSign;
	protected boolean isSword;



	// ================== CONSTRUCTORS ================== //

	public AbstractItemSwing(Player player, String name) {
		super(player, name);

		swinging = false;
		sprite = new Sprite(Resources.SHEET_PLAYER_ITEMS);
		swingDirs = new int[0];
		swingHitDirs = new int[0];
		swingItemLength = 15;
		swingSign = 1;
		isSword = false;
		usedInMineCart = true;

		swingOffsetPoints = new Vector[] {new Vector(0, 8), new Vector(0, 16),
				new Vector(8, 16), new Vector(16, 16), new Vector(16, 8),
				new Vector(16, 0), new Vector(8, 0), new Vector(0, 0)};

		sprite.setLooped(false);
	}



	// =============== ABSTRACT METHODS =============== //

	protected void onFinishSwing() {
		sheathe();
	};

	protected void onSwingPeak() {
	}

	protected void onSwing() {
	}



	// =================== ACCESSORS =================== //

	public boolean isTouching(Collidable c) {
		if (!swinging || c == null || c.getCollisionBox() == null)
			return false;
		if (sprite.getFrameIndex() >= swingDirs.length)
			return false;

		Vectangle vect = new Vectangle(c);
		int dir = swingDirs[sprite.getFrameIndex()];
		Vector origin = player.getPosition().plus(
				sprite.getCurrentFrame().getPart(0).getDrawPos()
						.plus(swingOffsetPoints[dir]));

		for (int i = 0; i < swingItemLength; i += 2) {
			Vector v = origin.plus(Direction.angledLengthVector(i, dir));
			if (vect.contains(v)) {
				return true;
			}
		}

		int dirPrev = dir;
		if (sprite.getFrameIndex() > 0)
			dirPrev = swingDirs[sprite.getFrameIndex() - 1];


		if (dirPrev != dir) {
			Vector originPrev = player.getPosition().plus(
					sprite.getAnimation().getFrame(sprite.getFrameIndex() - 1)
							.getPart(0).getDrawPos()
							.plus(swingOffsetPoints[dirPrev]));
			Vector endPrev = originPrev.plus(Direction.angledLengthVector(
					swingItemLength, dirPrev));
			Vector end = origin.plus(Direction.angledLengthVector(
					swingItemLength, dir));

			for (double i = 0; i < 1; i += 0.1) {
				Vector v1 = originPrev.plus(new Vector(originPrev, origin)
						.scale(i));
				Vector v2 = endPrev.plus(new Vector(endPrev, end).scale(i));
				for (double j = 0; j <= 1; j += 0.1) {
					Vector v = v1.plus(new Vector(v1, v2).scale(j));
					if (vect.contains(v)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean checkHitObject(Point objPosition) {
		Vector swingPoint = getSwingHitPoint();
		if (swingPoint == null)
			return false;

		Vectangle v = new Vectangle(new Vector(objPosition), new Vector(16, 16));
		return v.contains(swingPoint);
	}

	public boolean isSwinging() {
		return swinging;
	}

	protected Vector getSwingHitPoint() {
		if (!swinging)
			return null;
		if (sprite.getFrameIndex() >= swingHitDirs.length)
			return null;
		int dir = swingHitDirs[sprite.getFrameIndex()];
		if (dir < 0)
			return null;
		Point add = Direction.getAngledDirPoint(dir);
		return player.getCenter().plus(new Vector(add).scale(16));
	}
	
	protected Vector getDrawOffset() {
		if (player.isInMineCart())
			return Direction.lengthVector(4, player.getDir()).inverse();
		return new Vector();
	}
	


	// ==================== MUTATORS ==================== //

	protected void swingDefault(int playerDir, int length,
			DynamicAnimation animSwing, DynamicAnimation animPlayer) {
		int start = (playerDir + (playerDir == 0 ? 1 : -1)) * 2;
		int len = (playerDir == 0 ? -length : length);
		swing(start, len, animSwing, animPlayer);
	}

	protected void swing(int startDir, int length, DynamicAnimation animSwing,
			DynamicAnimation animPlayer) {
		onSwing();
		swinging = true;
		player.setBusy(true);

		int start = GMath.getWrappedValue(startDir, NUM_SWING_DIRS);
		int sign = GMath.sign(length);
		int absLength = Math.abs(length);

		swingSign = sign;
		swingDirs = new int[absLength];
		swingHitDirs = new int[absLength];

		for (int i = 0; i < absLength; i++) {
			int index = GMath.getWrappedValue(start + (sign * i),
					NUM_SWING_DIRS);
			swingDirs[i] = index;
			swingHitDirs[i] = index;
		}

		sprite.setLooped(false);
		sprite.newAnimation(animSwing);
		player.setAnimation(false, animPlayer);
	}

	protected void sheathe() {
		swinging = false;
		player.setBusy(false);
		player.resetAnimation();
		sprite.removeAnimation();
	}
	


	// =============== INHERITED METHODS =============== //

	@Override
	public void interrupt() {
		if (swinging)
			sheathe();
	}

	@Override
	public void update() {
		super.update();

		if (swinging) {
			if (sprite.isAnimationDone())
				onFinishSwing();
		}

		int frameIndex = sprite.getFrameIndex();
		sprite.update(player.getDir());

		if (swinging && sprite.getFrameIndex() == PEAK_SWING_FRAME
				&& frameIndex < PEAK_SWING_FRAME) {
			onSwingPeak();
		}
	}

	@Override
	public void drawUnder() {
//		sprite.draw(player.getPosition().minus(0, player.getZPosition()));
	}
	
	@Override
	public void drawOver() {
		sprite.draw(player.getPosition().plus(getDrawOffset()).minus(0, player.getZPosition()));
		// DEBUG
		// drawSwordPoints();
	}
}
