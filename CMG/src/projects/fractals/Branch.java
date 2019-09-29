package projects.fractals;

import cmg.graphics.Draw;
import cmg.math.GMath;
import cmg.math.geometry.Vector;

public class Branch {
	private static final double MAX_LENGTH = 20;
	private static final double MIN_LENGTH = 0.01;
	private static final double MAX_ANGLE = GMath.QUARTER_PI;
	
	private Branch parent;
	private Branch right;
	private Branch left;
	private double length;
	private double currentLength;
	private double angle;
	private int level;

	public Branch(double length) {
		this.length = length;
		angle  = 0;
		level  = 0;
		parent = null;
		right  = null;
		left   = null;
	}
	
	public Branch(Branch parent, boolean isRight) {
		this.parent = parent;
		
		double rand = GMath.random.nextDouble();
		angle  = (rand*rand * 0.5);//GMath.QUARTER_PI);
		if (isRight)
			angle *= -1;
		
		rand = GMath.random.nextDouble();
		length = MAX_LENGTH - (rand * rand * MAX_LENGTH);
		currentLength = 0;
		
		level  = parent.level + 1;
		right  = null;
		left   = null;
	}
	
	public void update() {
		
		if (currentLength < length && left == null && right == null) {
			currentLength += 1;
			
			if (currentLength >= length) {
				currentLength = length;
				
				if (level < 10) {
					if (GMath.random.nextInt(7) == 0) {
        				left  = new Branch(this, false);
        				right = new Branch(this, true);
					}
					else if (GMath.random.nextBoolean()) {
        				left = new Branch(this, false);
        				left.level -= 1;
					}
					else {
        				right = new Branch(this, true);
        				right.level -= 1;
					}
						
				}
			}
		}
		
		if (left != null)
			left.update();
		if (right != null)
			right.update();
			
	}
	
	public void extend() {
		left  = new Branch(this, false);
		right = new Branch(this, true);
		
		if (level < 15) {
    		if (GMath.random.nextInt(18) != 0)
    			left.extend();
    		if (GMath.random.nextInt(18) != 0)
    			right.extend();
		}
	}
	
	public void draw(Vector pos, double dir) {
		double nextDir = dir + angle;
		Vector nextPos = pos.plus(Vector.polarVector(currentLength, nextDir));
		Draw.drawLine(pos, nextPos);
		
		if (left != null)
			left.draw(nextPos, nextDir);
		if (right != null)
			right.draw(nextPos, nextDir);
	}
}
