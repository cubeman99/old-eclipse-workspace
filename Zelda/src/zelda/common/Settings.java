package zelda.common;

import zelda.common.geometry.Point;


/**
 * This class defines global constants used for the game.
 * 
 * @author David Jordan
 */
public class Settings {

	/** Global tile size. **/
	public static final int TS = 16;

	/** The acceleration due to gravity constant. **/
	public static final double GRAVITY = 0.125;

	/** The maximum fall velocity. **/
	public static final double TERMINAL_Z_VELOCITY = 3;

	/** The gravity for dead projectiles. **/
	public static final double GRAVITY_DEAD_PROJECTILE = 0.07;

	/** The view size of the game window (not including the HUD bar). **/
	public static final Point VIEW_SIZE = new Point(160, 128);

	/** The maximum number of rupees carriable. **/
	public static final int MAX_RUPEES = 999;

	/** The maximum ammo carriable for all items. **/
	public static final int MAX_AMMO = 99;

	/** The maximum number of heart containers obtainable. **/
	public static final int MAX_HEART_CONTAINERS = 14;

	/** The maximum number of dungeons per world. **/
	public static final int MAX_NUM_DENGEONS = 8;

	public static final int ACTION_A = 0;
	public static final int ACTION_B = 1;

}
