package projects.towerDefense;

import cmg.math.geometry.Point;

public abstract class Range {
	public abstract boolean check(Point center, Point loc, double range);
}
