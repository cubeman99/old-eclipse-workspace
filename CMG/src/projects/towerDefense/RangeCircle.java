package projects.towerDefense;

import cmg.math.geometry.Point;

public class RangeCircle extends Range {
	@Override
	public boolean check(Point center, Point loc, double range) {
		return (center.distanceTo(loc) <= range);
	}
}
