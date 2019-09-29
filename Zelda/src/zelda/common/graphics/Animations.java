package zelda.common.graphics;

import sun.audio.AudioPlayer;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.util.Direction;
import zelda.common.graphics.AnimationFrame.FramePart;

public class Animations {
	public static DynamicAnimation[] PLAYER_DEFAULT;
	public static DynamicAnimation[] PLAYER_HOLDING;
	public static DynamicAnimation[] PLAYER_SWIMMING;
	public static DynamicAnimation[] PLAYER_RIDING;
	public static DynamicAnimation[][] PLAYER_SHIELD;
	public static DynamicAnimation[][] PLAYER_SHIELD_HOLD;
	public static DynamicAnimation[][] PLAYER_FORMS;
	public static DynamicAnimation PLAYER_PUSH;
	public static DynamicAnimation PLAYER_GRAB;
	public static DynamicAnimation PLAYER_PULL;
	public static DynamicAnimation PLAYER_DIG;
	public static DynamicAnimation PLAYER_THROW;
	public static DynamicAnimation PLAYER_SWING;
	public static DynamicAnimation PLAYER_STAB;
	public static DynamicAnimation PLAYER_SWING_BIGGORON_SWORD;
	public static DynamicAnimation PLAYER_SPIN_SWORD;
	public static DynamicAnimation PLAYER_AIM;
	public static DynamicAnimation PLAYER_JUMP;
	public static DynamicAnimation PLAYER_DUNK;
	public static Animation PLAYER_FALL;
	public static Animation PLAYER_DIVE;

	public static DynamicAnimation SWORD_SWING;
	public static DynamicAnimation SWORD_SPIN;
	public static DynamicAnimation SWORD_STAB;
	public static DynamicAnimation SWORD;
	public static DynamicAnimation SWORD_CHARGED;
	public static DynamicAnimation CANE_SWING;
	public static DynamicAnimation ROD_SWING;
	public static DynamicAnimation BIGGORON_SWORD_SWING;

	public static DynamicAnimation ITEM_ARROW_PLAYER;
	public static DynamicAnimation ITEM_SWITCH_HOOK;
	public static DynamicAnimation ITEM_SEED_SHOOTER;
	public static DynamicAnimation ITEM_SWORD_BEAM;
	public static Animation ITEM_BOMB;
	public static Animation ITEM_SCENT_POD;
	public static Animation ITEM_MAGIC_ROD_FIRE;
	public static Animation ITEM_ROCK_PROJECTILE;
	public static Animation[] ITEM_BOOMERANG_PLAYER;
	public static DynamicAnimation MONSTER_SWORD;

	public static DynamicAnimation ITEM_MONSTER_ARROW;
	public static DynamicAnimation ITEM_MONSTER_MAGIC;
	public static DynamicAnimation ITEM_MONSTER_SPEAR;
	public static Animation ITEM_MONSTER_BOOMERANG;
	public static Animation ITEM_MONSTER_ROCK;
	public static Animation ITEM_MONSTER_BONE;
	public static Animation ITEM_MONSTER_FIREBALL;
	public static Animation ITEM_MONSTER_IRON_MASK;
	public static Animation EFFECT_MONSTER_ARROW_DEAD;
	public static DynamicAnimation[] ITEM_MONSTER_SWORD;

	public static Animation EFFECT_FIRE;
	public static Animation EFFECT_CLING;
	public static Animation EFFECT_CLING_LIGHT;
	public static Animation EFFECT_SCENT_POD;
	public static Animation EFFECT_FALLING_OBJECT;
	public static Animation EFFECT_ARROW_DEAD;
	public static Animation EFFECT_GRASS;
	public static Animation EFFECT_WADE;
	public static Animation EFFECT_LEAVES;
	public static Animation EFFECT_GRASS_LEAVES;
	public static Animation EFFECT_BREAK_ROCKS;
	public static Animation EFFECT_BREAK_SIGN;
	public static Animation EFFECT_SOMARIA_BLOCK_CREATE;
	public static Animation EFFECT_SOMARIA_BLOCK_DESTROY;
	public static Animation EFFECT_EMBER_SEED;
	public static Animation EFFECT_SCENT_SEED;
	public static Animation EFFECT_PEGASUS_SEED;
	public static Animation EFFECT_GALE_SEED;
	public static Animation EFFECT_MYSTERY_SEED;
	public static Animation[] EFFECT_SEEDS;
	public static Animation EFFECT_PEGASUS_SPRINKLE;
	public static Animation EFFECT_SPRINT;
	public static Animation EFFECT_GALE;
	public static Animation EFFECT_BOMB_EXPLOSION;
	public static Animation EFFECT_MONSTER_EXPLOSION;
	public static Animation EFFECT_MONSTER_BURN;
	public static Animation EFFECT_SPLASH_WATER;
	public static Animation EFFECT_SPLASH_LAVA;
	public static Animation EFFECT_FIREBALL;
	public static Animation EFFECT_APPEAR_POOF;
	public static Animation EFFECT_DISAPPEAR_POOF;
	public static DynamicAnimation EFFECT_DIRT;
	
	public static DynamicAnimation MINE_CART_BACK;
	public static DynamicAnimation MINE_CART_FRONT;
	public static DynamicAnimation MINE_CART;
	
	public static Animation MAP_FLOOR_BOX;
	public static Animation MAP_CURSOR;
	


	public static void initialize() {
		
		// =================== PLAYER =================== //

		// Default player walking and standing.
		PLAYER_DEFAULT = Animations.createMoveStandAnimations(new Animation()
				.addFrame(6, 0, 0, 0, 0).addFrame(6, 1, 0, 0, 0), 2, 0);

		// Player while holding an object.
		PLAYER_HOLDING = Animations.createMoveStandAnimations(new Animation()
				.addFrame(6, 0, 5, 0, 0).addFrame(6, 1, 5, 0, 0), 2, 0);

		// Player while swimming.
		PLAYER_SWIMMING = Animations.createMoveStandAnimations(new Animation()
				.addFrame(6, 0, 13, 0, 2).addFrame(6, 1, 13, 0, 2), 2, 0);
		PLAYER_SWIMMING[1] = PLAYER_SWIMMING[0];
		
		// Player while riding in a mine cart.
		PLAYER_RIDING = Animations.createMoveStandAnimations(new Animation(2, 15), 1, 0);
		PLAYER_RIDING[1] = PLAYER_RIDING[0];
		
		PLAYER_DUNK = new DynamicAnimation(4);
		for (int i = 0; i < 4; i++) {
			PLAYER_DUNK.setVariant(i, new Animation().addFrame(
					new AnimationFrame(PLAYER_SWIMMING[0].getVariant(i)
					.getFrame(0)).setDuration(8))
					.addFrame(17, 0, 21, 0, 4));
		}
		
		PLAYER_DIVE = new Animation().addFramesRepeat(4,
				new AnimationFrame(16, 0, 21, 0, 4), new AnimationFrame(16, 1, 21, 0, 4));
		
		// Player with shield equipped.
		PLAYER_SHIELD = new DynamicAnimation[2][2];
		PLAYER_SHIELD[0] = Animations.createMoveStandAnimations(new Animation()
				.addFrame(6, 0, 1, 0, 0).addFrame(6, 1, 1, 0, 0), 2, 0);
		PLAYER_SHIELD[1] = new DynamicAnimation[] {
				new DynamicAnimation(PLAYER_SHIELD[0][0]),
				new DynamicAnimation(PLAYER_SHIELD[0][1])};
		PLAYER_SHIELD[1][0].getVariant(0).shiftSourcePositions(0, 2);
		PLAYER_SHIELD[1][1].getVariant(0).shiftSourcePositions(0, 2);

		// Player while holding shield in front of him.
		PLAYER_SHIELD_HOLD = new DynamicAnimation[2][2];
		PLAYER_SHIELD_HOLD[0] = Animations
				.createMoveStandAnimations(
						new Animation().addFrame(6, 0, 2, 0, 0).addFrame(6, 1,
								2, 0, 0), 2, 0);
		PLAYER_SHIELD_HOLD[1] = new DynamicAnimation[] {
				new DynamicAnimation(PLAYER_SHIELD_HOLD[0][0]),
				new DynamicAnimation(PLAYER_SHIELD_HOLD[0][1])};
		PLAYER_SHIELD_HOLD[1][0].getVariant(3).shiftSourcePositions(-4, 1);
		PLAYER_SHIELD_HOLD[1][1].getVariant(3).shiftSourcePositions(-4, 1);

		PLAYER_FORMS = new DynamicAnimation[8][];
		for (int i = 0; i < PLAYER_FORMS.length; i++) {
			PLAYER_FORMS[i] = Animations.createMoveStandAnimations(
					new Animation().addFrame(6, 0, 22 + i, 0, 0).addFrame(6, 1,
							22 + i, 0, 0), 2, 0);
		}


		PLAYER_PUSH = new DynamicAnimation(new Animation().addFrame(6, 0, 6, 0, 0)
				.addFrame(6, 1, 6, 0, 0), 4, 2, 0);
		PLAYER_DIG = new DynamicAnimation(new Animation().addFrame(8, 0, 9, 0,0)
				.addFrame(15, 1, 9, 0, 0), 4, 2, 0);

		PLAYER_GRAB = new DynamicAnimation(new Animation(1, 0, 7, 0, 0), 4, 2, 0);

		PLAYER_PULL = new DynamicAnimation(new Animation(1, 1, 7, 0, 0), 4, 2, 0);
		PLAYER_PULL.getVariant(0).shiftDrawPositions(-4, 0);
		PLAYER_PULL.getVariant(1).shiftDrawPositions(0, 2);
		PLAYER_PULL.getVariant(2).shiftDrawPositions(4, 0);
		PLAYER_PULL.getVariant(3).shiftDrawPositions(0, -1);

		PLAYER_THROW = new DynamicAnimation(new Animation(9, 0, 4, 0, 0), 4, 2, 0);

		PLAYER_FALL = new Animation().addFrame(16, 1, 20, 0, 0)
				.addFrame(10, 2, 20, 0, 0).addFrame(11, 3, 20, 0, 0);

		PLAYER_AIM = new DynamicAnimation(new Animation(12, 0, 4, 0, 0),
				new Animation(12, 3, 8, 0, 0), new Animation(12, 2, 4, 0, 0),
				new Animation(12, 2, 8, 0, 0), new Animation(12, 4, 4, 0, 0),
				new Animation(12, 1, 8, 0, 0), new Animation(12, 6, 4, 0, 0),
				new Animation(12, 0, 8, 0, 0));

		PLAYER_JUMP = new DynamicAnimation(
				new Animation()
    				.addFrame(9, 0, 11).addFrame(9, 1, 11)
    				.addFrame(6, 2, 11).addFrame(6, 1, 0),
				new Animation()
					.addFrame(9, 3, 11).addFrame(9, 4, 11)
					.addFrame(6, 5, 11).addFrame(6, 3, 0),
				new Animation()
					.addFrame(9, 0, 12).addFrame(9, 1, 12)
					.addFrame(6, 2, 12).addFrame(6, 5, 0),
				new Animation()
					.addFrame(9, 3, 12).addFrame(9, 4, 12)
					.addFrame(6, 5, 12).addFrame(6, 7, 0));

		SWORD_SWING = new DynamicAnimation(new Animation()
				.addFrame(3, 2, 0, 0, -16).addFrame(3, 1, 0, 13, -13)
				.addFrame(8, 0, 0, 20, 4).addFrame(3, 0, 0, 12, 4),
				new Animation().addFrame(3, 0, 0, 16, 0)
						.addFrame(3, 1, 0, 13, -13).addFrame(8, 2, 0, -4, -20)
						.addFrame(3, 2, 0, -4, -12), new Animation()
						.addFrame(3, 2, 0, 0, -16).addFrame(3, 3, 0, -13, -13)
						.addFrame(8, 4, 0, -20, 4).addFrame(3, 4, 0, -12, 4),
				new Animation().addFrame(3, 4, 0, -15, 2)
						.addFrame(3, 5, 0, -13, 15).addFrame(8, 6, 0, 3, 20)
						.addFrame(3, 6, 0, 3, 14));

		CANE_SWING = new DynamicAnimation(SWORD_SWING).shiftSourcePositions(0,
				4);
		ROD_SWING = new DynamicAnimation(SWORD_SWING)
				.shiftSourcePositions(0, 3);

		PLAYER_SPIN_SWORD = new DynamicAnimation(4);
		SWORD_SPIN = new DynamicAnimation(4);
		SWORD_SPIN.setVariant(
				0,
				new Animation().addFrame(3, 0, 0, 19, 4)
						.addFrame(2, 7, 0, 16, 16).addFrame(3, 6, 0, 3, 19)
						.addFrame(2, 5, 0, -13, 15).addFrame(3, 4, 0, -19, 4)
						.addFrame(2, 3, 0, -13, -13).addFrame(3, 2, 0, -4, -19)
						.addFrame(2, 1, 0, 16, -16).addFrame(3, 0, 0, 19, 4));

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				int x = ((i - j + 8) % 4) * 2;
				PLAYER_SPIN_SWORD.getVariant(i).addFrame(j < 4 ? 5 : 3,
						new Point(x, 4),
						new Vector(Direction.lengthVector(4, x / 2)));
			}
			if (i > 0) {
				for (int j = 0; j < 9; j++) {
					int index = (((4 - i) * 2) + j) % 8;
					SWORD_SPIN.getVariant(i).addFrame(
							SWORD_SPIN.getVariant(0).getFrame(index));
				}
			}
		}

		SWORD = new DynamicAnimation(new Animation(4, 0, 0, 12, 4),
				new Animation(4, 2, 0, -4, -12),
				new Animation(4, 4, 0, -12, 4), new Animation(4, 6, 0, 3, 14));

		MONSTER_SWORD = new DynamicAnimation(new Animation().addFrame(4, 0, 0,
				8, 4).addFrame(4, 0, 0, 10, 4), new Animation().addFrame(4, 2,
				0, 4, -8).addFrame(4, 2, 0, 4, -10), new Animation().addFrame(
				4, 4, 0, -8, 4).addFrame(4, 4, 0, -10, 4), new Animation()
				.addFrame(4, 6, 0, -4, 7).addFrame(4, 6, 0, -4, 9));

		ITEM_MONSTER_SWORD = new DynamicAnimation[4];
		for (int i = 0; i < 4; i++) {
			int x = i * 4;
			ITEM_MONSTER_SWORD[i] = new DynamicAnimation(
					new Animation().addFrame(4, x + 0, 0,  8,  4).addFrame(4, x + 0, 0,  10,   4),
					new Animation().addFrame(4, x + 1, 0,  4, -8).addFrame(4, x + 1, 0,   4, -10),
					new Animation().addFrame(4, x + 2, 0, -8,  4).addFrame(4, x + 2, 0, -10,   4),
					new Animation().addFrame(4, x + 3, 0, -4,  7).addFrame(4, x + 3, 0,  -4,   9));
		}


		SWORD_CHARGED = new DynamicAnimation(new Animation().addFrame(4, 0, 1,
				12, 4).addFrame(4, 0, 0, 12, 4), new Animation().addFrame(4, 1,
				1, -4, -12).addFrame(4, 2, 0, -4, -12), new Animation()
				.addFrame(4, 2, 1, -12, 4).addFrame(4, 4, 0, -12, 4),
				new Animation().addFrame(4, 3, 1, 3, 14).addFrame(4, 6, 0, 3,
						14));

		SWORD_STAB = new DynamicAnimation(new Animation().addFrame(6, 0, 0, 20,
				4).addFrame(8, 0, 0, 12, 4), new Animation().addFrame(6, 2, 0,
				-4, -20).addFrame(8, 2, 0, -4, -12), new Animation().addFrame(
				6, 4, 0, -20, 4).addFrame(8, 4, 0, -12, 4), new Animation()
				.addFrame(6, 6, 0, 3, 20).addFrame(8, 6, 0, 3, 14));

		PLAYER_STAB = new DynamicAnimation(new Animation()
				.addFrame(6, 0, 4, 4, 0).addFrame(7, 0, 4, 0, 0)
				.addFrame(1, 1, 0, 0, 0), new Animation()
				.addFrame(6, 2, 4, 0, -4).addFrame(7, 2, 4, 0, 0)
				.addFrame(1, 3, 0, 0, 0), new Animation()
				.addFrame(6, 4, 4, -4, 0).addFrame(7, 4, 4, 0, 0)
				.addFrame(1, 5, 0, 0, 0), new Animation()
				.addFrame(6, 6, 4, 0, 4).addFrame(7, 6, 4, 0, 0)
				.addFrame(1, 7, 0, 0, 0));


		PLAYER_SWING = new DynamicAnimation(new Animation()
				.addFrame(3, 4, 8, 0, 0).addFrame(3, 0, 4, 0, 0)
				.addFrame(8, 0, 4, 4, 0).addFrame(3, 0, 4, 0, 0),
				new Animation().addFrame(3, 3, 8, 0, 0).addFrame(3, 2, 4, 0, 0)
						.addFrame(8, 2, 4, 0, -4).addFrame(3, 2, 4, 0, 0),
				new Animation().addFrame(3, 2, 8, 0, 0).addFrame(3, 4, 4, 0, 0)
						.addFrame(8, 4, 4, -4, 0).addFrame(3, 4, 4, 0, 0),
				new Animation().addFrame(3, 1, 8, 0, 0).addFrame(3, 6, 4, 0, 0)
						.addFrame(8, 6, 4, 0, 4).addFrame(3, 6, 4, 0, 0));

		BIGGORON_SWORD_SWING = new DynamicAnimation(
			new Animation()
				.addFrame(12, 2, 2, -2, -16, /**/5, 1, -2, -32)
				.addFrame(5, 1, 2, 16, -16)
				.addFrame(4, 0, 2, 16, 0, /**/4, 1, 32, 0)
				.addFrame(4, 7, 2, 16, 16)
				.addFrame(9, 6, 2, 1, 16, /**/7, 1, 1, 32),
			new Animation()
				.addFrame(12, 0, 2, 16, 0, /**/4, 1, 32, 0)
				.addFrame(5, 1, 2, 16, -16)
				.addFrame(4, 2, 2, -2, -16, /**/5, 1, -2, -32)
				.addFrame(4, 3, 2, -13, -13)
				.addFrame(9, 4, 2, -16, 0, /**/6, 1, -32, 0),
			new Animation()
				.addFrame(12, 2, 2, -2, -16, /**/5, 1, -2, -32)
				.addFrame(5, 3, 2, -13, -13)
				.addFrame(4, 4, 2, -16, 0, /**/6, 1, -32, 0)
				.addFrame(4, 5, 2, -13, 15)
				.addFrame(9, 6, 2, 1, 16, /**/7, 1, 1, 32),
			new Animation()
				.addFrame(12, 4, 2, -16, 0, /**/6, 1, -32, 0)
				.addFrame(5, 5, 2, -13, 15)
				.addFrame(4, 6, 2, 1, 16, /**/7, 1, 1, 32)
				.addFrame(4, 7, 2, 16, 16)
				.addFrame(9, 0, 2, 16, 0, /**/4, 1, 32, 0));

		PLAYER_SWING_BIGGORON_SWORD = new DynamicAnimation(
				new Animation()
				.addFrame(12, 4, 8, 0, 0)
				.addFrame(9, 0, 4, 0, 0)
				.addFrame(13, 5, 8, 0, 0),
				new Animation()
				.addFrame(12, 3, 8, 0, 0)
				.addFrame(9, 2, 4, 0, 0)
				.addFrame(13, 6, 8, 0, 0),
				new Animation()
				.addFrame(12, 2, 8, 0, 0)
				.addFrame(9, 4, 4, 0, 0)
				.addFrame(13, 1, 8, 0, 0),
				new Animation()
				.addFrame(12, 1, 8, 0, 0)
				.addFrame(9, 6, 4, 0, 0)
				.addFrame(13, 5, 8, 0, 0));

		ITEM_SEED_SHOOTER = new DynamicAnimation(new Animation(1, 0, 5, 14, 4),
				new Animation(1, 1, 5, 14, -8),
				new Animation(1, 2, 5, -4, -14), new Animation(1, 3, 5, -11,
						-14), new Animation(1, 4, 5, -14, 4), new Animation(1,
						5, 5, -11, 11), new Animation(1, 6, 5, 3, 14),
				new Animation(1, 7, 5, 14, 11));



		ITEM_ARROW_PLAYER = new DynamicAnimation(new Animation(0, 11), 8, 1, 0);
		ITEM_SWITCH_HOOK = new DynamicAnimation(new Animation().addFrame(4, 0,
				7).addFrame(4, 1, 7), 4, 2, 0);

		ITEM_ROCK_PROJECTILE = new Animation(0, 3);

		ITEM_SWORD_BEAM = new DynamicAnimation(new Animation().addFrame(4, 0,
				10, 0, 0).addFrame(4, 1, 10, 0, 0), 4, 2, 0);

		ITEM_BOOMERANG_PLAYER = new Animation[] {
				new Animation().addFrame(2, 0, 9, 0, 0).addFrame(2, 3, 9, 0, 0)
						.addFrame(2, 2, 9, 0, 0).addFrame(2, 1, 9, 0, 0),
				new Animation().addFrame(2, 4, 9, 0, 0).addFrame(2, 7, 9, 0, 0)
						.addFrame(2, 6, 9, 0, 0).addFrame(2, 5, 9, 0, 0),};

		ITEM_BOMB = new Animation().addFrame(2, 2, 8, 0, 0).addFrame(2, 3, 8,
				0, 0);
		ITEM_MAGIC_ROD_FIRE = new Animation().addFrame(2, 5, 8, 0, 0)
				.addFrame(2, 6, 8, 0, 0).addFrame(2, 7, 8, 0, 0);



		// ================ MONSTER ITEMS ================ //

		ITEM_MONSTER_ARROW = new DynamicAnimation(new Animation(0, 0), 4, 1, 0);
		ITEM_MONSTER_BOOMERANG = createStrip(2, 4, 0, 4);
		ITEM_MONSTER_ROCK = new Animation(0, 3);
		EFFECT_MONSTER_ARROW_DEAD = new Animation();
		for (int i = 0; i < 4; i++)
			EFFECT_MONSTER_ARROW_DEAD.addFrame(6, i, 0);
		ITEM_MONSTER_SPEAR = new DynamicAnimation(new Animation().addFrame(2,
				0, 1).addFrame(2, 1, 1), 4, 2, 0);



		// =================== EFFECTS =================== //

		EFFECT_FIRE = new Animation()
				.addFrame(2, 1, 3)
				.addFramesRepeat(9, new AnimationFrame(2, 1, 2),
						new AnimationFrame(2, 1, 17),
						new AnimationFrame(2, 1, 1)).addFrame(2, 1, 2);

		EFFECT_MONSTER_BURN = new Animation()
				.addFrame(1, 1, 2)
				.addFrame(2, 1, 14)
				.addFrame(2, 1, 18)
				.addFramesRepeat(6, new AnimationFrame(3, 1, 1),
						new AnimationFrame(3, 1, 13),
						new AnimationFrame(3, 1, 17));

		EFFECT_SCENT_POD = new Animation().addFrame(8, 0, 2, 0, 0).addFrame(8,
				1, 2, 0, 0);
		EFFECT_CLING = new Animation().addFrame(4, 6, 4, 0, 0).addFrame(5, 7,
				4, 0, 0);
		EFFECT_CLING_LIGHT = new Animation(EFFECT_CLING).createFlicker(1, 2);

		EFFECT_FALLING_OBJECT = new Animation().addFrame(8, 3, 12, 0, 0)
				.addFrame(12, 4, 12, 0, 0).addFrame(13, 5, 12, 0, 0);

		EFFECT_ARROW_DEAD = new Animation();
		for (int i = 0; i < 4; i++)
			EFFECT_ARROW_DEAD.addFrame(6, i * 2, 11, 0, 0);

		EFFECT_GRASS = new Animation().addFrame(
				new AnimationFrame(6).addPart(6, 0, -4, 1).addPart(6, 0, 2, 1))
				.addFrame(
						new AnimationFrame(6).addPart(7, 0, -4, 1).addPart(7,
								0, 2, 1));

		EFFECT_WADE = new Animation()
				.addFrame(
						new AnimationFrame(8).addPart(4, 2, -5, -5).addPart(5,
								2, -11, -5))
				.addFrame(
						new AnimationFrame(8).addPart(4, 2, -6, -5).addPart(5,
								2, -10, -5))
				.addFrame(
						new AnimationFrame(8).addPart(4, 2, -7, -4).addPart(5,
								2, -9, -4))
				.addFrame(
						new AnimationFrame(8).addPart(4, 2, -8, -3).addPart(5,
								2, -8, -3));



		EFFECT_LEAVES = new Animation()
				.addFrame(
						new AnimationFrame(4).addPart(0, 1, 2, -1)
								.addPart(1, 1, -8, -4).addPart(0, 1, 0, -5)
								.addPart(0, 1, 6, -5))
				.addFrame(
						new AnimationFrame(4).addPart(2, 1, 2, 3)
								.addPart(3, 1, 3, -3).addPart(1, 1, 0, -4)
								.addPart(0, 1, -5, -4))
				.addFrame(
						new AnimationFrame(4).addPart(3, 1, 5, 5)
								.addPart(3, 1, 2, -1).addPart(1, 1, 0, -5)
								.addPart(3, 1, -2, -5))
				.addFrame(
						new AnimationFrame(4).addPart(3, 1, 5, 5)
								.addPart(3, 1, -1, 5).addPart(3, 1, 3, -3)
								.addPart(1, 1, -1, -9))
				.addFrame(
						new AnimationFrame(4).addPart(3, 1, 4, 7)
								.addPart(3, 1, 3, 1).addPart(3, 1, -5, -10)
								.addPart(1, 1, -7, 5))
				.addFrame(
						new AnimationFrame(4).addPart(2, 1, 5, 2)
								.addPart(3, 1, 6, 9).addPart(2, 1, -5, -10)
								.addPart(0, 1, -10, 9))
				.addFrame(
						new AnimationFrame(4).addPart(2, 1, 8, 11)
								.addPart(2, 1, 9, 2).addPart(2, 1, -5, -11)
								.addPart(0, 1, -10, 5))
				.addFrame(
						new AnimationFrame(4).addPart(2, 1, 8, 9)
								.addPart(2, 1, 9, 3).addPart(2, 1, -7, -12)
								.addPart(0, 1, -13, -1));

		EFFECT_GRASS_LEAVES = new Animation(EFFECT_LEAVES).createFlicker();

		EFFECT_BREAK_ROCKS = new Animation()
				.addFrame(
						new AnimationFrame(4).addPart(2, 0, -4, -5)
								.addPart(2, 0, 5, -6).addPart(2, 0, -6, 4)
								.addPart(2, 0, 4, 3))
				.addFrame(
						new AnimationFrame(4).addPart(2, 0, -6, -6)
								.addPart(2, 0, 7, -7).addPart(2, 0, -7, 5)
								.addPart(2, 0, 6, 4))
				.addFrame(
						new AnimationFrame(4).addPart(2, 0, -7, -7)
								.addPart(2, 0, 9, -8).addPart(2, 0, -9, 6)
								.addPart(2, 0, 8, 5))
				.addFrame(
						new AnimationFrame(4).addPart(2, 0, -9, -5)
								.addPart(2, 0, 11, -6).addPart(2, 0, -11, 8)
								.addPart(2, 0, 10, 7));

		EFFECT_BREAK_SIGN = new Animation(EFFECT_BREAK_ROCKS)
				.shiftSourcePositions(1, 0);


		EFFECT_SOMARIA_BLOCK_CREATE = new Animation().addFrame(3, 3, 12)
				.addFrame(3, 1, 12).addFrame(3, 0, 12);
		EFFECT_SOMARIA_BLOCK_DESTROY = new Animation().addFrame(6, 0, 12)
				.addFrame(8, 1, 12).addFrame(5, 5, 12);


		EFFECT_SCENT_SEED = new Animation().addFrame(3, 8, 12)
				.addFrame(3, 6, 12).addFrame(3, 7, 12);
		EFFECT_PEGASUS_SEED = new Animation().addFrame(3, 3, 4)
				.addFrame(3, 0, 4).addFrame(3, 1, 4);
		EFFECT_MYSTERY_SEED = new Animation(EFFECT_PEGASUS_SEED)
				.shiftSourcePositions(0, 4);

		EFFECT_EMBER_SEED = new Animation()
				.addFrame(2, 1, 3, 0, 0)
				.addFramesRepeat(9, new AnimationFrame(2, 1, 2),
						new AnimationFrame(2, 1, 17),
						new AnimationFrame(2, 1, 1)).addFrame(2, 1, 2, 0, 0);

		EFFECT_GALE = new Animation();
		for (int i = 0; i < 4 * 3; i++) {
			int y = 1 + (((5 - (i % 4)) % 4) * 4);
			EFFECT_GALE.addFrame(1, ((i % 6) < 3 ? 4 : 5), y);
		}
		EFFECT_GALE.createFlicker();

		EFFECT_GALE_SEED = new Animation();
		for (int i = 0; i < 29; i++) {
			int y = 1 + (((5 - (i % 4)) % 4) * 4);
			EFFECT_GALE_SEED.addFrame(1, ((i % 6) < 3 ? 4 : 5), y);
		}
		EFFECT_GALE_SEED.createFlicker(1, 12);
		EFFECT_SEEDS = new Animation[] {EFFECT_EMBER_SEED, EFFECT_SCENT_SEED,
				EFFECT_PEGASUS_SEED, EFFECT_GALE_SEED, EFFECT_MYSTERY_SEED};


		EFFECT_BOMB_EXPLOSION = new Animation()
				.addFrame(4, 0, 0)
				.addFrame(4, 0, 16)
				.addFrame(3, 0, 0)
				.addFrame(
						new AnimationFrame(7).addPart(0, 0, -6, -6)
								.addPart(0, 0, 6, -6).addPart(0, 0, -6, 2)
								.addPart(0, 0, 6, 2))
				.addFrame(
						new AnimationFrame(8).addPart(6, 1, -8, -8)
								.addPart(7, 1, 8, -8).addPart(6, 2, -8, 8)
								.addPart(7, 2, 8, 8))
				.addFrame(
						new AnimationFrame(9).addPart(1, 0, -8, -8)
								.addPart(1, 0, 8, -8).addPart(1, 0, -8, 8)
								.addPart(1, 0, 8, 8));

		EFFECT_MONSTER_EXPLOSION = new Animation()
				.addFrame(
						new AnimationFrame(5).addPart(0, 0, -4, -14).addPart(0,
								0, -12, -2))
				.addFrame(
						new AnimationFrame(2).addPart(6, 8, -16, -16).addPart(
								6, 8, 0, 0))
				.addFrame(
						new AnimationFrame(2).addPart(6, 4, -16, -16).addPart(
								6, 4, 0, 0))
				.addFrame(
						new AnimationFrame(2).addPart(6, 8, -16, -16).addPart(
								6, 8, 0, 0))
				.addFrame(
						new AnimationFrame(2).addPart(6, 4, -16, -16).addPart(
								6, 4, 0, 0)).addFrame(2, 6, 12, -8, -8)
				.addFrame(2, 6, 0, -8, -8).addFrame(2, 7, 12, -8, -8);


		EFFECT_SPLASH_WATER = new Animation()
				.addFrame(
						new AnimationFrame(4).addPart(2, 2, -8, -11).addPart(3,
								2, -8, -11))
				.addFrame(
						new AnimationFrame(4).addPart(2, 2, -10, -13).addPart(
								3, 2, -6, -13))
				.addFrame(
						new AnimationFrame(4).addPart(2, 2, -12, -15).addPart(
								3, 2, -4, -15));

		EFFECT_SPLASH_LAVA = new Animation()
			.addFrame(2, new FramePart(4,  3,  -1,  0), new FramePart(4,  3,  1,  0))
			.addFrame(2, new FramePart(4, 15,  -2, -1), new FramePart(4, 15,  2, -1))
			.addFrame(2, new FramePart(4, 19,  -3, -2), new FramePart(4, 19,  3, -2))
			.addFrame(2, new FramePart(4,  3,  -4, -3), new FramePart(4,  3,  4, -3))
			.addFrame(2, new FramePart(4, 15,  -5, -4), new FramePart(4, 15,  5, -4))
			.addFrame(2, new FramePart(4, 19,  -6, -3), new FramePart(4, 19,  6, -3))
			.addFrame(2, new FramePart(4,  3,  -7, -2), new FramePart(4,  3,  7, -2))
			.addFrame(2, new FramePart(4, 15,  -8, -1), new FramePart(4, 15,  8, -1))
			.addFrame(2, new FramePart(4, 19,  -9,  0), new FramePart(4, 19,  9,  0))
			.addFrame(2, new FramePart(4,  3, -10,  2), new FramePart(4,  3, 10,  2));

		EFFECT_DIRT = new DynamicAnimation(new Animation(new AnimationFrame(1)
				.addPart(5, 0, -14, -12).addPart(5, 0, -10, -6)),
				new Animation(new AnimationFrame(1).addPart(4, 0, -8, -10)
						.addPart(5, 0, -8, -8)), new Animation(
						new AnimationFrame(1).addPart(4, 0, -2, -12).addPart(4,
								0, -6, -6)), new Animation(
						new AnimationFrame(1).addPart(4, 0, -8, -8).addPart(5,
								0, -8, -10)));

		EFFECT_FIREBALL = new Animation().addFrame(2, 2, 0).addFrame(2, 3, 0)
				.addFrame(2, 2, 16).addFrame(2, 3, 16);


		EFFECT_PEGASUS_SPRINKLE = new Animation()
				.addFrame(
						new AnimationFrame(1).addPart(5, 12, -12, -10).addPart(
								5, 12, -4, -10))
				.addFrame(
						new AnimationFrame(1).addPart(8, 9, -14, -7)
								.addPart(8, 10, -10, -11).addPart(8, 9, -1, -7)
								.addPart(8, 10, -5, -11))
				.addFrame(
						new AnimationFrame(1).addPart(8, 6, -14, -7)
								.addPart(8, 5, -10, -11).addPart(8, 6, -1, -7)
								.addPart(8, 5, -5, -11))
				.addFrame(
						new AnimationFrame(1).addPart(8, 1, -14, -7)
								.addPart(8, 2, -10, -11).addPart(8, 1, -1, -7)
								.addPart(8, 2, -5, -11))
				.addFrame(
						new AnimationFrame(1).addPart(8, 14, -17, -5)
								.addPart(8, 13, -12, -9).addPart(8, 14, 2, -5)
								.addPart(8, 13, -3, -9))
				.addFrame(
						new AnimationFrame(1).addPart(8, 9, -17, -5)
								.addPart(8, 10, -12, -9).addPart(8, 9, -4, -5)
								.addPart(8, 10, -3, -9));

		EFFECT_SPRINT = new Animation().addFrame(2, 3, 12).addFrame(4, 4, 12)
				.addFrame(3, 5, 12).createFlicker();
		
		EFFECT_APPEAR_POOF    = new Animation().addFrame(6, 0, 12).addFrame(8, 1, 12).addFrame(5, 5, 12);
		EFFECT_DISAPPEAR_POOF = new Animation(EFFECT_APPEAR_POOF);
		
		
		MINE_CART = new DynamicAnimation(
			new Animation(
				new AnimationFrame(6)
					.addPart(0, 2, 0, -3).addPart(2, 2, 0, -3),
				new AnimationFrame(6)
					.addPart(1, 2, 0, -3).addPart(2, 2, 0, -2)),
			new Animation(
				new AnimationFrame(6)
					.addPart(0, 3, 0, -3).addPart(2, 3, 0, -3),
				new AnimationFrame(6)
					.addPart(1, 3, 0, -3).addPart(2, 3, 0, -3)));
		
		MINE_CART_FRONT = new DynamicAnimation(
				new Animation().addFrame(6, 0, 2, 0, -3).addFrame(6, 1, 2, 0, -3),
				new Animation().addFrame(6, 0, 3, 0, -3).addFrame(6, 1, 3, 0, -3));
		MINE_CART_BACK = new DynamicAnimation(
				new Animation().addFrame(6, 2, 2, 0, -3).addFrame(6, 2, 2,  0, -2),
				new Animation().addFrame(6, 2, 3, 0, -3).addFrame(6, 2, 3, -1, -3));
		
		
		MAP_FLOOR_BOX = new Animation(new AnimationFrame()
				.addPart(7, 4, 0, 0).addPart(8, 4, 8, 0));
		MAP_CURSOR = new Animation().addFrame(
				new AnimationFrame(32).addPart(5, 2, -4, -4)
				.addPart(6, 2, 4, -4).addPart(5, 3, -4, 4)
				.addPart(6, 3, 4, 4)).addFrame(32, 5, 0);
	}



	/**
	 * Create animations for both moving and standing based on the given base
	 * movement animation and the relative position between movementAnimations.
	 * 
	 * @param base - Animation for the first direction of movement.
	 * @param relX - Relative x-position of adjacent animations.
	 * @param relY - Relative y-position of adjacent animations.
	 * @return The moving animation and standing animation in a list
	 *         respectively.
	 */
	public static DynamicAnimation[] createMoveStandAnimations(Animation base,
			int relX, int relY) {
		DynamicAnimation[] anims = new DynamicAnimation[2];
		anims[0] = new DynamicAnimation(base, Direction.NUM_DIRS, relX, relY);
		Animation stand = new Animation(anims[0].getBaseAnimation().getFrame(0));
		stand.getFrame(0).setDuration(base.getDuration());
		anims[1] = new DynamicAnimation(stand, Direction.NUM_DIRS, relX, relY);

		return anims;
	}


	public static Animation createStrip(int frameDuration, int startX,
			int startY, int length) {
		Animation anim = new Animation();
		for (int i = 0; i < length; i++)
			anim.addFrame(frameDuration, startX + i, startY);
		return anim;
	}


	public static Animation createReversableStrip(int frameDuration,
			int startX, int startY, int length) {
		Animation anim = new Animation();
		for (int i = 0; i < length; i++)
			anim.addFrame(frameDuration, startX + i, startY);
		for (int i = length - 2; i > 0; i--)
			anim.addFrame(frameDuration, startX + i, startY);
		return anim;
	}
}
