package projects.trafficSimulator;

import java.awt.Color;
import cmg.graphics.Draw;
import cmg.math.geometry.Vector;

public class Intersection {
	private Vector position;
	private Map map;
	
	public Intersection(Map map, Vector pos) {
		this.map = map;
		this.position = new Vector(pos);
	}
	
	public Map getMap() {
		return map;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public void update() {
		
	}
	
	public void draw() {
		Draw.setColor(Color.WHITE);
		Draw.fillCircle(getPosition(), 20);
		Draw.setColor(Color.BLACK);
		Draw.drawCircle(getPosition(), 20);
	}
}
