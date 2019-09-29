package projects.bezierCurves;

import java.awt.Color;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.geometry.Line;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;

public class Control {
	private ArrayList<Vector> points;
	private double time;
	private boolean playing;
	private Point size;
	private float[][] terrain;
	
	public Control(GameRunner runner) {
		points = new ArrayList<Vector>();
		time = 0;
		playing = false;

		size = new Point(40, 40);
		terrain = TerrainGen.generateTerrainDiamondSquare(size.x, size.y);
	}
	
	public void update() {
		if (Mouse.left.pressed()) {
			points.add(Mouse.getVector());
		}
		if (Keyboard.space.pressed()) {
			playing = true;
			time = 0;
		}
		if (playing) {
			time += 0.01;
			if (time >= 1) {
				time = 1;
				playing = false;
			}
		}
	}
	
	public void drawCurve(Vector v1, Vector v2, Vector v3) {
		Line l1     = new Line(v1, v2);
		Line l2     = new Line(v2, v3);
		Vector curr = null;
		Vector prev = null;
		
		for (double x = 0; x <= 1; x += 0.05) {
			curr = new Line(l1.getPoint(x), l2.getPoint(x)).getPoint(x);
			if (prev != null)
				Draw.drawLine(prev, curr);
			prev = curr;
		}
		Draw.drawLine(curr, v3);
		
		if (playing) {
			Line mid = new Line(l1.getPoint(time), l2.getPoint(time));
			Vector v = mid.getPoint(time);
			
			Draw.setColor(Color.BLUE);
			Draw.draw(mid);

			Draw.setColor(Color.GREEN);
			Draw.fillCircle(v, 4);
			Draw.setColor(Color.BLACK);
			Draw.drawCircle(v, 4);
		}
	}
	
//	public void getTerrainPoint()
	
	public void drawTerrain() {
		Vector origin = new Vector(20, 300);
		
		Draw.setColor(Color.BLACK);
		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				float z = terrain[x][y];
				Vector v = new Vector(x + y, (0.5 * y) - (0.5 * x));
				v.y -= z;
				Draw.fillCircle(origin.plus(v.scaledBy(10)), 4);
			}
		}
	}
	
	public void draw() {
		/*
		if (points.size() == 3) {
			Line l1 = new Line(points.get(0), points.get(1));
			Line l2 = new Line(points.get(1), points.get(2));
			
			Line mid = new Line(l1.getPoint(time), l2.getPoint(time));
			Vector v = mid.getPoint(time);
			
			Draw.setColor(Color.BLUE);
			Draw.draw(mid);

			Draw.setColor(Color.GREEN);
			Draw.fillCircle(v, 4);
			Draw.setColor(Color.BLACK);
			Draw.drawCircle(v, 4);

			Draw.setColor(Color.RED);
			drawCurve(points.get(0), points.get(1), points.get(2));
		}
		*/
		

		Draw.setColor(Color.GRAY);
		for (int i = 1; i < points.size(); i++)
			Draw.drawLine(points.get(i - 1), points.get(i));


		if (points.size() >= 3) {
			Draw.setColor(Color.RED);
			Line prev = new Line(points.get(0), points.get(1));
			Line next = null;
			
    		for (int i = 1; i < points.size() - 1; i++) {
    			next = new Line(points.get(i), points.get(i + 1));
    			if (i == 1)
    				drawCurve(points.get(0), points.get(i), next.getCenter());
    			else if (i == points.size() - 2)
    				drawCurve(prev.getCenter(), points.get(i), points.get(points.size() - 1));
    			else
    				drawCurve(prev.getCenter(), points.get(i), next.getCenter());
    			prev = next;
    		}
		}
		
		drawTerrain();
		
		
		for (int i = 0; i < points.size(); i++) {
			Draw.setColor(Color.YELLOW);
			Draw.fillCircle(points.get(i), 8);
			Draw.setColor(Color.BLACK);
			Draw.drawCircle(points.get(i), 8);
		} 
	}
}
