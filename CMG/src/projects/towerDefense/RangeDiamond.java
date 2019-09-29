package projects.towerDefense;

import cmg.math.geometry.Point;

public class RangeDiamond extends Range {
	@Override
	public boolean check(Point center, Point loc, double range) {
		return (Math.abs(center.x - loc.x)
				+ Math.abs(center.y - loc.y) <= range);
	}
}
