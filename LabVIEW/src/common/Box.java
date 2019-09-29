package common;

public class Box {
	public Point position;
	public Point size;
	
	
	public Box(Point position, Point size) {
		this.position = position;
		this.size     = size;
	}
	
	public int x() {
		return position.x;
	}
	
	public int y() {
		return position.y;
	}
	
	public int x1() {
		return position.x;
	}
	
	public int y1() {
		return position.y;
	}
	
	public int x2() {
		return (position.x + size.x);
	}
	
	public int y2() {
		return (position.y + size.y);
	}
	
	public int width() {
		return size.x;
	}
	
	public int height() {
		return size.y;
	}
	
	public void grow(int xAmount, int yAmount) {
		position.sub(xAmount / 2, yAmount / 2);
		size.add(new Point(xAmount, yAmount));
	}
	
	public static Box combine(Box...boxes) {
		if (boxes.length == 0)
			return null;
		Point min = boxes[0].position;
		Point max = boxes[0].position.plus(boxes[0].size);
		for (int i = 1; i < boxes.length; i++) {
			min.x = (int) GMath.min(min.x, boxes[i].position.x);
			min.y = (int) GMath.min(min.y, boxes[i].position.y);
			max.x = (int) GMath.max(max.x, boxes[i].position.plus(boxes[i].size).x);
			max.y = (int) GMath.max(max.y, boxes[i].position.plus(boxes[i].size).y);
		}
		return new Box(min, max.minus(min));
	}
}
