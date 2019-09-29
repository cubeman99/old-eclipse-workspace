

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public final class Node {
	public static final double radius = 2.0d;
	public static final double diameter = radius * 2.0d;
	public double x;
	public double y;
	public ArrayList<Node> neighbors = new ArrayList<Node>();
	
	public Node(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Node getRandomNeighbor() {
		if (neighbors.size() == 0)
			return null;
		int index = Game.random.nextInt(neighbors.size());
		return neighbors.get(index);
	}
	
	public Vector getVector() {
		return new Vector(x, y);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.drawOval((int) (x - radius), (int) (y - radius), (int) diameter, (int) diameter);
	}
	
	public void draw(Graphics g, Color c) {
		g.setColor(c);
		g.drawOval((int) (x - radius), (int) (y - radius), (int) diameter, (int) diameter);
	}
	
	public void draw(Graphics g, Color c, boolean filled) {
		g.setColor(c);
		if (filled) {
			g.fillOval((int) (x - radius), (int) (y - radius), (int) diameter, (int) diameter);
			g.drawOval((int) (x - radius), (int) (y - radius), (int) diameter, (int) diameter);
		}
		else
			g.drawOval((int) (x - radius), (int) (y - radius), (int) diameter, (int) diameter);
	}
}