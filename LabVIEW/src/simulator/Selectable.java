package simulator;

import main.Mouse;
import common.Box;
import common.Point;

public abstract class Selectable {
	

	public abstract Point getPosition();
	public abstract Point getSize();
	
	public Box getBox() {
		return new Box(getPosition(), getSize());
	}
	
	public boolean mouseOver() {
		return Mouse.inArea(getBox());
	}
}
