package game;

/**
 * A list of the different types of collidable boundaries.
 * @author	Robert Jordan
 */
public class CollisionType {

	// ====================== Constants =======================

	// Grouping
	public static final int allCoverable	= -6;
	public static final int allPits			= -5;
	public static final int allWater		= -4;
	public static final int allSolid		= -3;
	public static final int allNonSolid		= -2;
	public static final int all				= -1;
	
	// Basics
	public static final int air				= 0;
	public static final int land			= 1;
	public static final int solid			= 2;
	public static final int boundary		= 3;
	
	// Ledges
	public static final int ledgeLeft		= 4;
	public static final int ledgeUp			= 5;
	public static final int ledgeRight		= 6;
	public static final int ledgeDown		= 7;
	
	// Water
	public static final int water			= 8;
	public static final int waterLeft		= 9;
	public static final int waterUp			= 10;
	public static final int waterRight		= 11;
	public static final int waterDown		= 12;
	public static final int ocean			= 13;
	
	// Death
	public static final int lava			= 14;
	public static final int hole			= 15;
	public static final int pit				= 16;
	
	// Movement modifiers
	public static final int stairs			= 17;
	public static final int ladder			= 18;
	public static final int ice				= 19;
	public static final int platform		= 20;
	public static final int grass			= 21;
	public static final int shallowWater	= 22;
	
	// Special
	public static final int hurt			= 23;
	public static final int entrance		= 24;
	public static final int player			= 25;

	// ===================== Information ======================
	
	/** Returns true if the specified collision type is matches the group. */
	public static boolean matchesType(int groupingType, int collisionType) {
		if (groupingType == all) {
			return true;
		}
		else if (groupingType == allNonSolid) {
			switch (collisionType) {
			case air:
			case land:
			case boundary:
				
			case water:
			case waterLeft:
			case waterUp:
			case waterRight:
			case waterDown:
			case ocean:
				
			case lava:
			case hole:
			case pit:
				
			case stairs:
			case ladder:
			case ice:
			case platform:
			case grass:
			case shallowWater:
				
			case hurt:
			case entrance:
			case player: return true;
			}
		}
		else if (groupingType == allSolid) {
			switch (collisionType) {
			case solid:
				
			case ledgeLeft:
			case ledgeUp:
			case ledgeRight:
			case ledgeDown: return true;
			}
		}
		else if (groupingType == allWater) {
			switch (collisionType) {
			case water:
			case waterLeft:
			case waterUp:
			case waterRight:
			case waterDown:
			case ocean: return true;
			}
		}
		else if (groupingType == allPits) {
			switch (collisionType) {
			case hole:
			case pit: return true;
			}
		}
		else if (groupingType == allCoverable) {
			switch (collisionType) {
			case air:
			case land:
				
			case water:
			case waterLeft:
			case waterUp:
			case waterRight:
			case waterDown:
			case ocean:
				
			case lava:
			case hole:
			case pit:
				
			case ice:
			case grass:
			case shallowWater:
				
			case player: return true;
			}
		}
		else if (groupingType >= 0) {
			return groupingType == collisionType;
		}
		return false;
	}
}
