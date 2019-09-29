package snake;

import java.awt.Point;


public enum Direction {
	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);
	
	public int dx, dy;
	
	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public Point getPoint() {
		return new Point(dx, dy);
	}
}