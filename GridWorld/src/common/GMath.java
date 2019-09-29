package common;

import java.util.Random;
import info.gridworld.grid.Location;

public class GMath {
	public static Random random = new Random();
	
	
	public static Location locationSum(Location l1, Location l2) {
		return new Location(
			l1.getRow() + l2.getRow(),
			l1.getCol() + l2.getCol()
		);
	}
	
	public static Location locationDiff(Location l1, Location l2) {
		return new Location(
			l1.getRow() - l2.getRow(),
			l1.getCol() - l2.getCol()
		);
	}
	
	public static double distance(double y, double x) {
		return Math.sqrt((x * x) + (y * y));
	}
	
	public static double distance(Location displacement) {
		return distance(displacement.getRow(), displacement.getCol());
	}
	
	public static double distance(Location l1, Location l2) {
		return distance(locationDiff(l2, l1));
	}
	
	public static double direction(Location l1, Location l2) {
		return direction(locationDiff(l2, l1));
	}
	
	public static double direction(Location displacement) {
		return direction(displacement.getRow(), displacement.getCol());
	}
	
	public static int getRoundedAngle(double dir) {
		return ((int) (dir / 45.0) * 45);
	}
	
	public static double direction(double y, double x) {
		double dir = 0;
		if (x != 0) {
		    dir = 90 - Math.toDegrees(Math.atan(-y / x));
		    if (x < 0)
		        dir += 180;
		}
		else if (y < 0)
		    dir = 0;
		else if (y > 0)
		    dir = 180;
		
		return dir;
	}
}
