package critters.spiralBug;

import java.awt.Color;
import info.gridworld.actor.Bug;


public class SpiralBug extends Bug {
	private int index;
	private int size;
	
	public SpiralBug(Color c) {
		super(c);
		this.size      = 1;
		this.index     = 0;
		setDirection(90);
	}
	
	public SpiralBug() {
		this(Color.RED);
	}

	@Override
	public void act() {
		if (!canMove() || index >= size) {
			size++;
			index = 0;
			turn();
			turn();
		}
		else {
			move();
			index++;
		}
	}
}
