package zelda.common.util;

import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;


public class Direction {
	public static final int RIGHT = 0;
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int DOWN = 3;

	public static final int EAST = 0;
	public static final int NORTH = 1;
	public static final int WEST = 2;
	public static final int SOUTH = 3;

	public static final int NUM_DIRS = 4;

	private static final Point[] DIR_POINTS = {new Point(1, 0),
			new Point(0, -1), new Point(-1, 0), new Point(0, 1)};
	private static final Point[] ANGLED_DIR_POINTS = {new Point(1, 0),
			new Point(1, -1), new Point(0, -1), new Point(-1, -1),
			new Point(-1, 0), new Point(-1, 1), new Point(0, 1),
			new Point(1, 1)};



	public static boolean isVertical(int dir) {
		return (dir % 2 == 1);
	}

	public static boolean isHorizontal(int dir) {
		return (dir % 2 == 0);
	}

	public static Vector lengthVector(double length, int dir) {
		if (length < 0)
			return Vector.polarVector(-length, ((dir + 2) % NUM_DIRS)
					* GMath.HALF_PI);
		return Vector.polarVector(length, dir * GMath.HALF_PI);
	}

	public static Vector angledLengthVector(double length, int dir) {
		if (length < 0)
			return Vector.polarVector(-length, ((dir + 4) % NUM_DIRS)
					* GMath.QUARTER_PI);
		return Vector.polarVector(length, dir * GMath.QUARTER_PI);
	}

	public static Point getDirPoint(int dir) {
		return DIR_POINTS[dir];
	}

	public static Point getAngledDirPoint(int dir) {
		return ANGLED_DIR_POINTS[dir];
	}
}
