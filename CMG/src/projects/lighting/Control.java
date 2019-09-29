package projects.lighting;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Line;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Rectangle;
import cmg.math.geometry.Vector;

public class Control {
	private ArrayList<Polygon> shapes;
	private Vector source;
	private GameRunner runner;
	
	public Control(GameRunner runner) {
		shapes = new ArrayList<Polygon>();
		this.runner = runner;
//		addRectangle(100, 200, 60, 60);
//		addRectangle(300, 150, 60, 60);
//		addRectangle(400, 400, 60, 60);
//		addRectangle(200, 350, 60, 60);
		
		source = new Vector(300, 300);
	}
	
	public void addRectangle(double x, double y, double width, double height) {
		Rectangle r = new Rectangle(x, y, width, height);
		Polygon p   = new Polygon(r);
		p.addVertex(x - (width / 2), y + (height / 2));
		shapes.add(p);
	}
	
	public void update() {
		double speed = 4;
		if (Keyboard.right.down())
			source.x += speed;
		if (Keyboard.left.down())
			source.x -= speed;
		if (Keyboard.down.down())
			source.y += speed;
		if (Keyboard.up.down())
			source.y -= speed;
		
		
		if (Mouse.left.pressed()) {
			addRectangle(Mouse.x(), Mouse.y(), 60, 60);
		}
	}
	
	public Vector traceRay(Polygon poly, int vertexIndex) {
		Vector point = poly.getVertex(vertexIndex);
		Line ray = new Line(source, point);
		double maxDistance = 500;
		Vector end = source.plus(ray.getVector().setLength(maxDistance));
		ray.set(source, end);
		double minDistance = point.distanceTo(source);
		Vector maxEnd = end;
		boolean stopped = false;
		
		for (int i = 0; i < shapes.size(); i++) {
			Polygon p = shapes.get(i);
			
			for (int j = 0; j < p.edgeCount(); j++) {
				
				if (p != poly || (j != vertexIndex && (j + 1) % p.vertexCount() != vertexIndex)) {
    				Line edge    = p.getEdge(j);
    				Vector inter = Line.intersection(ray, edge);
    				
    				if (inter != null) {
    					if (poly == p) {
    						stopped = true;
    						maxEnd = point;
    					}
    					
        				double dist = source.distanceTo(inter);
        				
        				if (dist < minDistance)
        					return null;
        				if (!stopped && dist < maxDistance) {
        					maxDistance = dist;
        					maxEnd = inter;
        				}
    				}
				}
			}
		}
		
		return maxEnd;
	}
	
	public Vector castRay(Vector source, double direction) {
		double minDistance = 500;
		Vector end = source.plus(Vector.polarVector(minDistance, direction));
		Line ray = new Line(source, end);
		
		for (int i = 0; i < shapes.size(); i++) {
			Polygon p = shapes.get(i);
			
			for (int j = 0; j < p.edgeCount(); j++) {
				Vector inter = Line.intersection(ray, p.getEdge(j));
				
				if (inter != null) {
					double dist = source.distanceTo(inter);
					if (dist < minDistance) {
						minDistance = dist;
						end = inter;
					}
				}
			}
		}
		
		return end;
	}
	
	public void draw() {
//		Draw.setColor(Color.BLACK);
//		Draw.fillRect(0, 0, runner.getViewWidth(), runner.getViewHeight());
		
		
		Area shadows = new Area();
		Area vision  = new Area();
		shadows.add(new Area(new java.awt.geom.Rectangle2D.Double(0, 0, runner.getViewWidth(), runner.getViewHeight())));
		vision.add(new Area(new java.awt.geom.Ellipse2D.Double(source.x - 500, source.y - 500, 1000, 1000)));
		shadows.subtract(new Area(new java.awt.geom.Ellipse2D.Double(source.x - 495, source.y - 495, 990, 990)));
		
		for (int i = 0; i < shapes.size(); i++) {
			Polygon p     = shapes.get(i);
			Vector center = p.getCenter();
			
//			Draw.setColor(Color.GREEN);
//			Draw.fillCircle(center, 4);
//			Draw.drawLine(source, center);
			
			Line divider = new Line(source, center);

			double minDist = 0;
			int minIndex   = -1;
			double maxDist = 0;
			int maxIndex   = -1;
			
			for (int j = 0; j < p.vertexCount(); j++) {
				Vector v = p.getVertex(j);
				double dist = v.minus(divider.end1).scalarRejection(divider.getVector()) / v.distanceTo(source);
				
				if (maxIndex < 0 || dist > maxDist) {
					maxDist = dist;
					maxIndex = j;
				}
				if (minIndex < 0 || dist < minDist) {
					minDist = dist;
					minIndex = j;
				}
			}
			
			Vector maxBound = p.getVertex(maxIndex);
			Vector minBound = p.getVertex(minIndex);
			
			if (maxBound.distanceTo(source) < 500 || minBound.distanceTo(source) < 500) {
    				
    			int dir = -1;
    			if (p.getVertex(maxIndex + 1).distanceTo(source) < p.getVertex(maxIndex - 1).distanceTo(source))
    				dir = 1;
    
    			Vector maxEnd   = source.plus(maxBound.minus(source).setLength(500));
    			Vector minEnd   = source.plus(minBound.minus(source).setLength(500));
    			Polygon poly = new Polygon(minBound, minEnd);
    			
    			
    			double minDir = GMath.direction(source, minEnd);
    			double maxDir = GMath.direction(source, maxEnd);
    			double angle  = (maxDir - minDir);
    			if (minDir > maxDir)
    				angle = (maxDir - (minDir - GMath.TWO_PI));
    			
    			for (double j = 0; j < angle; j += 0.1) {
    				Vector mid = source.plus(Vector.polarVector(500, minDir + j));
//    				Draw.setColor(Color.GREEN);
//    				Draw.fillCircle(mid, 10);
    				poly.addVertex(mid);
    			}
    
    			poly.addVertex(maxEnd);
    			poly.addVertex(maxBound);
    			
    			for (int j = 0; j < p.vertexCount(); j++) {
    				int absIndex = GMath.getWrappedValue(maxIndex + (j * dir), p.vertexCount());
    				if (absIndex == minIndex)
    					break;
    				poly.addVertex(p.getVertex(absIndex));
    				Draw.setColor(Color.GREEN);
    				Draw.drawLine(p.getVertex(absIndex), p.getVertex(absIndex + dir));
    			}
    			
    			Draw.setColor(Color.RED);
    			Draw.fillCircle(maxBound, 4);
    //			Draw.drawLine(source, maxBound);
    			
    			Draw.setColor(Color.BLUE);
    			Draw.fillCircle(minBound, 4);
    //			Draw.drawLine(source, minBound);
    			
    			Area shadow = new Area(poly.getAWT());
    			vision.subtract(shadow);
    			shadows.add(shadow);
    			Draw.setColor(Color.YELLOW);
    			//Draw.fill(poly);
			}
		}

		Draw.setColor(Color.BLACK);
//		Draw.fillCircle(source, 500);
		Draw.drawCircle(source, 500);
		
		Draw.setColor(Color.BLACK);
		Draw.getGraphics().fill(shadows);
		
		
		for (int i = 0; i < shapes.size(); i++) {
			Polygon p = shapes.get(i);
			Draw.setColor(Color.GRAY);
			Draw.fill(p);
			Draw.setColor(Color.BLACK);
			Draw.draw(p);
		}
		
		
		float[] dist = {0.0f, 1.0f};
	    Color[] colors = {new Color(0, 0, 0, 0), Color.BLACK};
		RadialGradientPaint grad = new RadialGradientPaint((float) source.x, (float) source.y, 500, dist, colors);
		Draw.getGraphics().setPaint(grad);
		Draw.fillCircle(source, 500);
//		Draw.getGraphics().fill(vision);
	
		Draw.setColor(Color.YELLOW);
		Draw.fillCircle(source, 20);
		Draw.setColor(Color.BLACK);
		Draw.drawCircle(source, 20);
	}
}
