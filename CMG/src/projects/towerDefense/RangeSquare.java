package projects.towerDefense;

import cmg.math.geometry.Point;

public class RangeSquare extends Range {
	@Override
	public boolean check(Point center, Point loc, double range) {
		return (Math.abs(center.x - loc.x) <= range &&
				Math.abs(center.y - loc.y) <= range);
	}
}
