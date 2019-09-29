package hstone;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import cmg.graphics.Draw;
import cmg.math.GMath;
import cmg.math.geometry.Circle;
import cmg.math.geometry.Vector;

public class PieChart {
	private Circle circle;
	private Map<String, Integer> tally;
	private Map<String, Color> colors;
	
	public PieChart(double x, double y, double radius) {
		circle = new Circle(x, y, radius);
		tally  = new HashMap<String, Integer>();
		colors = new HashMap<String, Color>();
	}
	
	public Circle getCircle() {
		return circle;
	}
	
	public void setColor(String key, Color color) {
		colors.put(key, color);
	}
	
	public void add(String key) {
		if (tally.containsKey(key))
			tally.put(key, tally.get(key) + 1);
		else
			tally.put(key, 1);
	}
	
	public void reset() {
		for (Map.Entry<String, Integer> entry : tally.entrySet()) {
			entry.setValue(0);
		}
	}
	
	public void draw() {
		int total = 0;
		for (Map.Entry<String, Integer> entry : tally.entrySet()) {
			total += entry.getValue();
		}
		
		Draw.setColor(Color.WHITE);
		Draw.fill(circle);
		
		double theta = 0;
		for (Map.Entry<String, Integer> entry : tally.entrySet()) {
			double percent = entry.getValue() / (double) total;
			double angle   = percent * GMath.TWO_PI;
			
			if (colors.containsKey(entry.getKey())) {
				Draw.setColor(colors.get(entry.getKey()));
				Draw.fillArc(circle.position, circle.radius, theta, angle);
			}
			
			Draw.setColor(Color.BLACK);
			Draw.drawLine(circle.position, circle.position.plus(Vector.polarVector(circle.radius, theta)));
			theta += angle;
			Draw.drawLine(circle.position, circle.position.plus(Vector.polarVector(circle.radius, theta)));
		}
		
		Draw.setColor(Color.BLACK);
		Draw.draw(circle);
	}
}
