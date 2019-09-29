package common;

public class Settings {

	/** The health of normal nodes. **/
	public static final double NODE_HEALTH             = 100;

	/** The health of reinforced nodes. **/
	public static final double NODE_REINFORCED_HEALTH  = 230;
	
	/** The health of mini-ships (deployed by deployer nodes). **/
	public static final double MINISHIP_HEALTH         = 100;

	/** The damage done to the planet by mini-ships. **/
	public static final double NODE_DAMAGE             = 25;

	/** The damage done to the planet by mini-ships. **/
	public static final double MINISHIP_DAMAGE         = 25;
	
	

	/** The maximum amount of armor of the planet. **/
	public static final double PLANET_ARMOR_MAX        = 100;
	
	/** The maximum health of the planet. **/
	public static final double PLANET_HEALTH_MAX       = 100;

	/** The percentage of damage that armor absorbs.**/
	public static final double ARMOR_ABSORB_PERCENTAGE = 0.5;

	/** The damage done by normal lasers. **/
	public static final double LASER_DAMAGE            = 40;

	/** The damage done by super lasers. **/
	public static final double LASER_SUPER_DAMAGE      = 80;
	
	/** The millisecond delay between laser shots from the player. **/
	public static final int LASER_FIRE_DELAY           = 133; // in milliseconds
	
	/** The maximum angular speed at which the player moves. **/
	public static final double PLAYER_MOVE_SPEED       = GMath.toRadians(7.5);
	
	
	/** The radius at which new rings spawn in. **/
	public static final double RING_SPAWN_RADIUS       = 1200;

	/** The maximum amount of nodes per ring. **/
	public static final double RING_SEPERATION         = 64;

	/** The maximum amount of nodes per ring. **/
	public static final int RING_MAXIMUM_NODES         = 18;
	
	// TODO
	public static final double RING_CRASH_RADIUS       = 80;

	/** The (minimum) speed at which the rings move in. **/
	public static final double RING_MOVE_IN_SPEED      = 0.065;
	
	/** The minimum radius for the node rings to start speeding up at. **/
	public static final double RING_SPEED_UP_RADIUS    = 200;
	
	/** The minimum radius for nodes to start attacking the planet. **/
	public static final double RING_ATTACK_RADIUS      = 140;

	
	
	// =================== POWER-UPS =================== //
	
	/** The standard duration for power-ups. **/
	public static final int POWERUP_DURATION           = 4000;
	
	/** The amount of health healed by health power-ups. **/
	public static final double POWERUP_HEALTH_AMOUNT   = 5;

	/** The amount of armor given by armor power-ups. **/
	public static final double POWERUP_ARMOR_AMOUNT    = 15;

	public static final double MISSILE_DAMAGE          = 400;
}
