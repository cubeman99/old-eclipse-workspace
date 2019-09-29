package zelda.game.player.items;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.effect.EffectCling;
import zelda.game.player.Player;
import OLD.AnimationOLD;
import OLD.AnimationStripOLD;
import OLD.SpriteOLD;
import OLD.SpriteSheetOLD;


public class ItemSwordOLD extends Item {
	private static final double SWING_ANIMATION_SPEED = 1;// 0.25 + 0.125;
	private static final double SPIN_ANIMATION_SPEED = 1;// 0.5 - 0.125;
	private static final double CHARGE_ANIMATION_SPEED = 0.25;
	private static final double CLING_ANIMATION_SPEED = 0.125;
	private static final int SWORD_CHARGE_TIME = 40; // 2/3 second

	private boolean swinging;
	private boolean spinning;
	private boolean clinging;

	private SpriteSheetOLD sheetSwordSwing;
	private SpriteSheetOLD sheetSwordCharge;
	private SpriteSheetOLD sheetSwordSpin;
	private AnimationStripOLD[] stripSwordSwing;
	private AnimationStripOLD[] stripSwordCharge;
	private AnimationStripOLD[] stripSwordCling;
	private AnimationStripOLD[] stripSwordSpin;

	private AnimationOLD swordAnimation;
	private boolean holdingSword;
	private int swordChargeTimer;



	// ================== CONSTRUCTORS ================== //

	public ItemSwordOLD(Player player) {
		super(player, "Sword");
		numLevels = 3;
		name[0] = "Wooden Sword";
		description[0] = "A hero<ap>s blade.";
		name[1] = "Noble Sword";
		description[1] = "A sacred blade.";
		name[2] = "Master Sword";
		description[2] = "The blade of legends.";

		setSlotIcons(Resources.SHEET_ICONS_THIN, 0, 0, 1, 0, 2, 0);


		sheetSwordSwing = new SpriteSheetOLD("swordSwing.png", 64, 64);
		sheetSwordSwing.centerOrigin();
		sheetSwordCharge = new SpriteSheetOLD("swordCharge.png", 64, 64);
		sheetSwordCharge.centerOrigin();
		sheetSwordSpin = new SpriteSheetOLD("swordSpin.png", 64, 64);
		sheetSwordSpin.centerOrigin();

		swinging = false;
		spinning = false;
		clinging = false;
		stripSwordSwing = new AnimationStripOLD[4];
		stripSwordCling = new AnimationStripOLD[4];
		stripSwordCharge = new AnimationStripOLD[4];
		stripSwordSpin = new AnimationStripOLD[4];

		swordAnimation = null;
		holdingSword = false;
		swordChargeTimer = 0;

		for (int i = 0; i < Direction.NUM_DIRS; i++) {
			stripSwordSwing[i] = new AnimationStripOLD(sheetSwordSwing, 0, i,
					5, SWING_ANIMATION_SPEED).setTimings(3, 3, 4, 4, 3);
			stripSwordCling[i] = new AnimationStripOLD(sheetSwordSwing, 3, i,
					2, CLING_ANIMATION_SPEED);
			stripSwordCharge[i] = new AnimationStripOLD(sheetSwordCharge,
					i * 2, 0, 2, CHARGE_ANIMATION_SPEED);
			stripSwordSpin[i] = createSpinAnimation(i);
		}
	}

	// =================== ACCESSORS =================== //

	public boolean checkHitObject(Point objPosition) {
		Rectangle r = new Rectangle(objPosition, new Point(16, 16));

		if (spinning) {
			Point p = new Point(player.getCenter());
			Point p2 = p;
			int dir = getSpinDir();
			int dist = 12;

			if (dir >= 1 && dir <= 3)
				p.y -= dist;
			if (dir >= 5 && dir <= 7)
				p.y += dist;
			if (dir >= 3 && dir <= 5)
				p.x -= dist;
			if (dir <= 1 || dir >= 7)
				p.x += dist;

			if (swordAnimation.getSubImage() == 8)
				p2 = new Point(player.getCenter());

			return r.contains(p) || r.contains(p2);
		}
		else if (swinging || clinging) {
			Point p = new Point(player.getCenter().plus(
					Vector.polarVector(12, GMath.HALF_PI * player.getDir())));
			return r.contains(p);
		}

		return false;
	}

	public boolean isSwinging() {
		return swinging;
	}

	public boolean isSpinning() {
		return spinning;
	}

	public boolean isHolding() {
		return holdingSword;
	}

	public boolean isCharged() {
		return (swordChargeTimer >= SWORD_CHARGE_TIME);
	}

	private int getSpinDir() {
		if (spinning && swordAnimation != null) {
			int dir = GMath.getWrappedValue(
					player.getDir() - swordAnimation.getSubImage(), 8);
			return dir;
		}
		return 0;
	}



	// ==================== MUTATORS ==================== //

	public void cling() {
		if (!clinging) {
			clinging = true;
			holdingSword = false;
			swordAnimation = new AnimationOLD(stripSwordCling[player.getDir()],
					false);
			Vector v = player.getCenter().plus(
					Vector.polarVector(14, player.getDir() * GMath.HALF_PI));
			player.getGame().addEntity(new EffectCling(v));
			player.setBusy(true);
		}
	}

	private void hold() {
		holdingSword = true;
		swinging = false;
		clinging = false;
		swordAnimation = new AnimationOLD(stripSwordCharge[player.getDir()]);
		swordChargeTimer = 0;
		player.setBusy(false);
		player.setItemBusy(true);
		player.resetAnimation();
	}

	private void swing() {
		swinging = true;
		swordAnimation = new AnimationOLD(stripSwordSwing[player.getDir()],
				false);
		player.setBusy(true);
	}

	private void spin() {
		spinning = true;
		holdingSword = false;
		swordAnimation = new AnimationOLD(stripSwordSpin[player.getDir()],
				false);
		player.setBusy(true);
	}

	private void charge() {
		swordAnimation = new AnimationOLD(stripSwordCharge[player.getDir()]);
	}

	private void sheathe() {
		swinging = false;
		spinning = false;
		clinging = false;
		holdingSword = false;
		swordAnimation = null;
		player.setBusy(false);
		player.setItemBusy(false);
		player.resetAnimation();
	}

	private AnimationStripOLD createSpinAnimation(int dir) {
		ArrayList<SpriteOLD> sprites = new ArrayList<SpriteOLD>();

		int startPos = sheetSwordSpin.getWidth() - ((dir + 1) * 2);

		for (int i = 0; i < sheetSwordSpin.getWidth() + 1; i++) {
			SpriteOLD spr = sheetSwordSpin.getSprite((startPos + i)
					% sheetSwordSpin.getWidth(), 0);
			sprites.add(spr);
		}
		AnimationStripOLD strip = new AnimationStripOLD(sprites,
				SPIN_ANIMATION_SPEED);
		int[] timings = new int[9];
		for (int i = 0; i < 9; i++)
			timings[i] = (i % 2 == 0 ? 3 : 2);

		return strip.setTimings(timings);
	}



	// =============== INHERITED METHODS =============== //

	@Override
	public void onEnd() {
		if (holdingSword)
			sheathe();
	}

	@Override
	public void interrupt() {
		if (swinging || spinning || holdingSword || clinging)
			sheathe();
	}

	@Override
	public void update() {
		if (swinging && swordAnimation.isDone())
			hold();
		else if (spinning && swordAnimation.isDone())
			sheathe();
		else if (clinging && swordAnimation.isDone())
			hold();
		else if (holdingSword) {
			if (!isCharged()) {
				swordChargeTimer++;
				if (isCharged())
					charge();
			}
			if (!keyDown()) {
				if (isCharged())
					spin();
				else
					sheathe();
			}
		}

		if (keyPressed()
				&& ((!player.isBusy() && !player.isItemBusy()) || swinging))
			swing();

		if (swordAnimation != null)
			swordAnimation.update();
	}

	@Override
	public void drawUnder() {
		Point swordDrawPos = new Point(player.getCenter().minus(0,
				player.getZPosition()));

		if (swordAnimation != null) {
			SpriteOLD spr = null;

			if (swinging || spinning || clinging) {
				spr = swordAnimation.getCurrentSprite();
			}
			else if (holdingSword) {
				if (isCharged())
					spr = swordAnimation.getCurrentSprite();
				else
					spr = swordAnimation.getSprite(0);
			}

			if (spr != null)
				Draw.drawSprite(spr, swordDrawPos);
		}
	}
}
