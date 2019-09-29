package control;

import common.Point;

public class World {
	public Control control;
	public Point size;
	
	
	public World(Control control, Point size) {
		this.control = control;
		this.size    = size;
	}
}
