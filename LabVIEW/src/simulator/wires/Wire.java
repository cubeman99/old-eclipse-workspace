package simulator.wires;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import common.Point;
import simulator.IOPoint;
import simulator.datatypes.DataType;
import simulator.datatypes.Type;


public class Wire {
	public static final int NONE       = 0;
	public static final int HORIZONTAL = 1;
	public static final int VERTICAL   = 2;
	
	public static final Color DEFAULT_COLOR = Color.GRAY;
	public ArrayList<Point> joints;
	public DataType data;
	public IOPoint input;
	public IOPoint output;
	public Wire wireInput;
	
	
	public Wire() {
		joints = new ArrayList<Point>();
		input  = null;
		output = null;
		data   = null;
	}
	
	public Wire(Point end1, Point end2) {
		this();
		addJoint(end1);
		addJoint(end2);
	}
	
	public void addJoint(Point joint) {
		joints.add(joint);
	}
	
	public void addJoint(int jx, int jy) {
		joints.add(new Point(jx, jy));
	}
	
	public void setJoint(int index, Point jointSet) {
		joints.set(index, jointSet);
	}
	
	public void setLastJoint(Point jointSet) {
		if (joints.size() > 0)
			joints.set(joints.size() - 1, jointSet);
	}
	
	public void clearJoints() {
		joints.clear();
	}
	
	public void removeJoints(int start) {
		for (int i = joints.size() - 1; i >= start; i--) {
			joints.remove(i);
		}
	}
	
	public boolean horizontalJoints(int startIndex) {
		if (joints.size() < 2)
			return true;
		return (joints.get(startIndex).y == joints.get(startIndex + 1).y);
	}
	
	public void connectToJoint(Point p, int a2) {
		if (joints.size() == 0)
			return;
		
		int a1 = HORIZONTAL;
		if (joints.size() >= 2)
			a1 = horizontalJoints(joints.size() - 2) ? VERTICAL : HORIZONTAL;
		Point lastJoint = new Point(joints.get(joints.size() - 1));
		
		if (a1 == HORIZONTAL) {
			if (a2 == NONE || a2 == VERTICAL)
				addJoint(p.x, lastJoint.y);
			else {
				int splitX = (lastJoint.x + p.x) / 2;
				addJoint(splitX, lastJoint.y);
				addJoint(splitX, p.y);
			}
		}
		else {
			if (a2 == NONE || a2 == HORIZONTAL)
				addJoint(lastJoint.x, p.y);
			else {
				int splitY = (lastJoint.y + p.y) / 2;
				addJoint(lastJoint.x, splitY);
				addJoint(p.x, splitY);
			}
		}
		
		addJoint(p);
		simplify();
	}
	
	public void simplify() {
		boolean found = true;
		while (found) {
    		found = false;
			for (int i = 1; i < joints.size(); i++) {
    			if (joints.get(i).equals(joints.get(i - 1))) {
    				joints.remove(i);
    				found = true;
    				break;
    			}
    		}
		}
	}
	
	public Type getType() {
		if (input != null) {
			return input.type;
		}
		return null;
	}
	
	public Color getColor() {
		if (input == null)
			return DEFAULT_COLOR;
		if (input.type == null)
			return DEFAULT_COLOR;
		return input.type.getColor();
	}
	
	public void update() {
		if (input != null && joints.size() > 0) {
			setJoint(0, input.getAbsPoint());
		}
		if (output != null && joints.size() > 0) {
			setJoint(joints.size() - 1, output.getAbsPoint());
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(getColor());
		for (int i = 1; i < joints.size(); i++) {
			Point p1 = joints.get(i - 1);
			Point p2 = joints.get(i);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
}
