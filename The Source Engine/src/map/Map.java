package map;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import common.Draw;
import common.Vector;
import common.shape.Line;
import common.shape.Polygon;
import common.shape.Rectangle;
import control.Control;
import dynamics.Body;


/**
 * Map.
 * 
 * @author David Jordan
 */
public class Map {
	public Control control;
	public Vector size;
	public NodeMap nodeMap;
	public ArrayList<Polygon> walls;
	
	public Map(Control control, Vector size) {
		this.control   = control;
		this.size      = new Vector(size);
		this.walls     = new ArrayList<Polygon>();
		this.nodeMap   = new NodeMap(this);
		
//		addWall(new Rectangle(new Vector(), size).toPolygon());
	}
	
	public void clear() {
		walls.clear();
	}
	
	public void saveToFile(String filename) {
		try {
			FileWriter file = new FileWriter(filename);
			
			file.write(walls.size() + " ");
			for (Polygon p : walls) {
				file.write(p.vertexCount() + " ");
				for (int i = 0; i < p.vertexCount(); i++) {
					file.write(p.getVertex(i).x + " ");
					file.write(p.getVertex(i).y + " ");
				}
			}
			file.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean loadFromFile(String filename) {
		clear();
		
		try {
    		FileReader fr  = new FileReader(filename);
    		Scanner reader = new Scanner(fr);
    		
    		int wallCount = reader.nextInt();
    		for (int i = 0; i < wallCount && reader.hasNext(); i++) {
    			int vertexCount = reader.nextInt();
    			Polygon p = new Polygon();
    			
        		for (int j = 0; j < vertexCount && reader.hasNext(); j++) {
        			Vector v = new Vector(reader.nextDouble(), reader.nextDouble());
        			p.addVertex(v);
        		}
        		addWall(p);
    		}
    		
    		nodeMap.constructNodeMap();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	public void createPhysicsBodies() {
		for (Polygon p : walls) {
			control.physics.addBody(Body.newStaticBody(p));
		}
	}
	
	/** Return whether a line touches any of the walls. **/
	public boolean lineTouchesWalls(Line l) {
		for (int i = 0; i < walls.size(); i++) {
    		for (int j = 0; j < walls.get(i).edgeCount(); j++) {
				if (Line.intersection(l, walls.get(i).getEdge(j)) != null)
    				return true;
    		}
		}
		return false;
	}
	
	/** Return if a point is inside the map boundaries. **/
	public boolean inBounds(Vector point) {
		return getBoundsRect().contains(point);
	}
	
	/** Return a rectangle object representing the map boundaries. **/
	public Rectangle getBoundsRect() {
		return new Rectangle(new Vector(), size);
	}

	
	public void addWall(Line l) {
		walls.add(new Polygon(l.end1, l.end2));
		control.physics.addBody(Body.newStaticBody(new Polygon(l.end1, l.end2)));
	}
	
	public void addWall(Polygon p) {
		walls.add(p);
		control.physics.addBody(Body.newStaticBody(p));
	}
	
	public Vector getGamePoint(Vector viewPoint) {
		return control.viewControl.getGamePoint(viewPoint);
	}
	
	public Vector getViewPoint(Vector gamePoint) {
		return control.viewControl.getViewPoint(gamePoint);
	}
	
	public void update() {
//		Vector ms  = control.viewControl.getGamePoint(Mouse.getVector());
		
		
	}
	
	public void draw() {
//		Vector ms  = control.viewControl.getGamePoint(Mouse.getVector());
		
		// Draw Walls:
		Draw.setColor(Color.WHITE);
		for (int i = 0; i < walls.size(); i++) {
			Draw.drawPolygon(walls.get(i));
		}
		
		nodeMap.draw();
	}
}
