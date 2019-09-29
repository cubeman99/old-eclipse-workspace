package critters.circleBug;

import java.awt.Color;
import info.gridworld.actor.Bug;


public class CircleBug extends Bug {
	private int radius;
	private int index;
	
	public CircleBug(int radius, Color c) {
		super(c);
		this.radius = radius;
		this.index  = 0;
	}
	
	public CircleBug(int radius) {
		this(radius, Color.RED);
	}

	@Override
	public void act() {
		if (!canMove() || index >= radius / 2) {
			index = 0;
			turn();
		}
		else {
			move();
			index++;
		}
	}
}
