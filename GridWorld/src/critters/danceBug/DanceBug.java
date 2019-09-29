package critters.danceBug;

import java.awt.Color;

import info.gridworld.actor.Bug;


public class DanceBug extends Bug {
	private int moveIndex;
	private int[] moves;
	
	public DanceBug(Color c, int[] moves) {
		super(c);
		
		this.moveIndex = 0;
		this.moves     = moves;
	}
	
	public DanceBug(int[] moves) {
		this(Color.RED, moves);
	}

	@Override
	public void act() {
		for (int i = 0; i < moves[moveIndex]; i++)
			turn();
		if (canMove())
			move();
		moveIndex++;
		if (moveIndex >= moves.length)
			moveIndex = 0;
	}
}
