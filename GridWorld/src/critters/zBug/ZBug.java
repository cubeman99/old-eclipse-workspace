package critters.zBug;

import java.awt.Color;
import info.gridworld.actor.Bug;


public class ZBug extends Bug {
	private int size;
	private int index;
	private int partIndex;
	
	public ZBug(int size, Color c) {
		super(c);
		this.size      = size;
		this.index     = 0;
		this.partIndex = 0;
		this.setDirection(90);
	}
	
	public ZBug(int size) {
		this(size, Color.RED);
	}

	@Override
	public void act() {
		if (partIndex < 3) {
			if (!canMove() || index >= size) {
				partIndex++;
				
				if (partIndex < 3) {
					turn();
					turn();
					turn();
					
					if (partIndex == 2) {
						turn();
						turn();
					}
				}
				
				index = 0;
			}
			else {
				move();
				index++;
			}
		}
	}
}
