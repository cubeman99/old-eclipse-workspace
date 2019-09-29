package snake;

import java.awt.Point;
import java.util.ArrayList;


public class Snake {
	public static final char CHAR_BODY = 'O';
	public static final char CHAR_HEAD = 'X';
	
	public static final char[] SEGMENT_CHARS = {
		(char) 205, // Straight: horizontal
		(char) 186, // Straight: vertical
		(char) 200, //   Corner: right to up
		(char) 188, //   Corner: up to left
		(char) 187, //   Corner: left to down
		(char) 201  //   Corner: down to right
	};
	
	public SnakeRunner snakeRunner;
	public int length;
	public int currentLength;
	public Point headPosition;
	public Direction direction;
	public ArrayList<Point> body;
	public char charHead = '?';
	
	public Snake(SnakeRunner snakeRunner, int length, Point position, Direction dir) {
		this.snakeRunner   = snakeRunner;
		this.length        = length;
		this.currentLength = length;
		this.headPosition  = position;
		this.direction     = dir;
		this.body          = new ArrayList<Point>();
		
		for (int i = 0; i < length; i++) {
			body.add(new Point(position));
		}
		setDirection(dir);
	}
	
	public void move() {
		if (currentLength < length) {
			currentLength++;
		}
		else {
			// Delete tail character
			snakeRunner.console.setCursorPos(getTailPoint());
			snakeRunner.console.print(" ");
		}
		
		headPosition.x += direction.dx;
		headPosition.y += direction.dy;

		for (int i = body.size() - 1; i > 0; i--) {
			body.set(i, body.get(i - 1));
		}
		body.set(0, new Point(headPosition));

		snakeRunner.console.setCursorPos(body.get(1));
		snakeRunner.console.print(getSegmentChar(1));
		// Draw head Character
		snakeRunner.console.setCursorPos(headPosition);
		snakeRunner.console.print(charHead);
	}
	
	public void addSegment() {
		length++;
		body.add(new Point(getTailPoint()));
	}
	
	public Point getTailPoint() {
		return body.get(body.size() - 1);
	}
	
	public void setDirection(Direction dir) {
		this.direction = dir;
		if (dir == Direction.RIGHT)
			charHead = (char) 16;
		else if (dir == Direction.LEFT)
			charHead = (char) 17;
		else if (dir == Direction.UP)
			charHead = (char) 30;
		else if (dir == Direction.DOWN)
			charHead = (char) 31;
		snakeRunner.console.setCursorPos(headPosition);
		snakeRunner.console.print(charHead);
	}
	
	public char getSegmentChar(int index) {
		Point seg1, seg2;
		Point seg = body.get(index);
		if (index == 1)
			seg1 = headPosition;
		else if (index > 1)
			seg1 = body.get(index - 1);
		else
			return '?';
		
		if (index < body.size() - 1)
			seg2 = body.get(index + 1);
		else
			return '?';
		
		/*
			O    O  OO  OO
			OO  OO   O  O
		*/
		
		if (seg1.y == seg2.y)
			return SEGMENT_CHARS[0];
		else if (seg1.x == seg2.x)
			return SEGMENT_CHARS[1];
		else if ((seg1.y > seg2.y && seg1.x > seg2.x && seg.y == seg1.y) || (seg2.y > seg1.y && seg2.x > seg1.x && seg.x == seg2.x))
			return SEGMENT_CHARS[2];
		else if ((seg1.y > seg2.y && seg1.x < seg2.x && seg.y == seg1.y) || (seg2.y > seg1.y && seg2.x < seg1.x && seg.y == seg2.y))
			return SEGMENT_CHARS[3];
		else if ((seg1.y < seg2.y && seg1.x < seg2.x) || (seg2.y < seg1.y && seg2.x < seg1.x))
			return SEGMENT_CHARS[4];
		else if ((seg1.y < seg2.y && seg1.x > seg2.x) || (seg2.y < seg1.y && seg2.x > seg1.x))
			return SEGMENT_CHARS[5];
		
		return '?';
	}
}