package snake;

import java.awt.Point;
import main.Keyboard;
import console.Console;

public class SnakeRunner {
	public Console console;
	public Snake snake;
	public int width;
	public int height;
	public int moveTick = 0;
	
	
	public SnakeRunner(Console console) {
		this.console = console;
		this.width   = console.getWidth();
		this.height  = console.getHeight();
		
		
		Point startingPos = new Point(2, 2);
		snake = new Snake(this, 4, startingPos, Direction.RIGHT);
	}
	
	public void update() {
		moveTick++;
		if (moveTick > 20) {
			moveTick = 0;
			snake.move();
		}
		
		if (Keyboard.up.pressed())
			snake.setDirection(Direction.UP);
		else if (Keyboard.down.pressed())
			snake.setDirection(Direction.DOWN);
		else if (Keyboard.left.pressed())
			snake.setDirection(Direction.LEFT);
		else if (Keyboard.right.pressed())
			snake.setDirection(Direction.RIGHT);
		
		if (Keyboard.space.pressed())
			snake.addSegment();
	}
}
