import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class WireSimulator {
	public static final int WIDTH  = 20;
	public static final int HEIGHT = 20;
	public static final int SNAP_DISTANCE = 12;
	
	public static final int GRID_SIZE = 8;
	
	public static Point dragPoint = new Point(0, 0);
	public static boolean dragging = false;
	
	public static ArrayList<Wire> wireList = new ArrayList<Wire>();
	public static ArrayList<Gate> gateList = new ArrayList<Gate>();
	
	public static boolean itemType = false;
	
	public static Wire[][] wireGrid;
	
	
	public WireSimulator() {
		wireGrid = new Wire[WIDTH][HEIGHT];
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				wireGrid[x][y] = new Wire(x, y);
			}
		}
	}

	public static boolean inBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT);
	}
	
	public static boolean inBounds(Point p) {
		return inBounds(p.x, p.y);
	}
	
	public static ConnectPoint getSnappedPoint(int x, int y) {
		double minDist = -1;
		ConnectPoint cp = new ConnectPoint(x, y);
		for (Gate G : gateList) {
			for (DataPoint D : G.inputs) {
				double dist = GMath.distance(x, y, G.x + D.x, G.y + D.y);
				if (dist < minDist || minDist < 0) {
					minDist = dist;
					cp = new ConnectPoint(G.x, G.y, D);
				}
			}
			for (DataPoint D : G.outputs) {
				double dist = GMath.distance(x, y, G.x + D.x, G.y + D.y);
				if (dist < minDist || minDist < 0) {
					minDist = dist;
					cp = new ConnectPoint(G.x, G.y, D);
				}
			}
		}
		if (minDist > SNAP_DISTANCE)
			return new ConnectPoint(x, y);
		return cp;
	}
	
	public static ArrayList<Point> getWireSplitPoints(Point end1, Point end2) {
		ArrayList<Point> pts = new ArrayList<Point>();
		pts.add(end1);
		if (Math.abs(end1.x - end2.x) >= Math.abs(end1.y - end2.y)) {
			pts.add(new Point((end1.x + end2.x) / 2, end1.y));
			pts.add(new Point((end1.x + end2.x) / 2, end2.y));
		}
		else {
			pts.add(new Point(end1.x, (end1.y + end2.y) / 2));
			pts.add(new Point(end2.x, (end1.y + end2.y) / 2));
		}
		pts.add(end2);
		return pts;
	}
	
	public static Point getMousePoint() {
		return new Point(Game.mouseX, Game.mouseY);
	}

	public static Point getGridSnapped(int x, int y) {
		return getGridSnapped(new Point(x, y));
	}
	
	public static Point getGridSnapped(Point p) {
		Point pp = new Point();
		pp.x = Math.round(p.x / GRID_SIZE) * GRID_SIZE;
		pp.y = Math.round(p.y / GRID_SIZE) * GRID_SIZE;
		return pp;
	}
	
	public void update() {
		Point ms = new Point((int) (Game.mouseX / GRID_SIZE), (int) (Game.mouseY / GRID_SIZE));

		if (Game.mbLeft && inBounds(ms)) {
			if (itemType) {
				gateList.add(new GateInvert(ms.x, ms.y));
			}
			else
				wireGrid[ms.x][ms.y].setWire(true);
		}
		if (Game.mbRight && inBounds(ms)) {
			if (itemType) {
				for (Gate G : gateList) {
					if (G.inBounds(ms.x, ms.y)) {
						gateList.remove(G);
						break;
					}
				}
			}
			else
				wireGrid[ms.x][ms.y].setWire(false);
		}
		/*
		for (Wire W : wireList) {
			W.update();
		}
		*/
		for (Gate G : gateList) {
			G.update();
		}
		if (Game.keyPressed[KeyEvent.VK_SPACE])
			itemType = !itemType;
		/*
		if (itemType) {
			if (Game.mbLeft && inBounds(ms) && !dragging) {
				dragging = true;
				dragPoint = new Point(ms);
			}
			if (!Game.mbLeft && dragging) {
				dragging = false;
				wireList.add(new Wire(getWireSplitPoints(dragPoint, ms)));
			}
		}
		else {
			dragging = false;
			if (Game.mbLeftPressed && inBounds(ms)) {
				if (!Game.keyDown[KeyEvent.VK_CONTROL])
					gateList.add(new GateInvert(ms.x, ms.y));
				else
					gateList.add(new GateLED(ms.x, ms.y));
			}
		}
		if (Game.mbRight && inBounds(ms)) {
		}
		*/
	}
	
	public static boolean getWireState(int x, int y) {
		if (inBounds(x, y)) {
			return wireGrid[x][y].getState();
		}
		return false;
	}
	
	public void drawGrid(Graphics g) {
		int gs = GRID_SIZE;
		g.setColor(Color.lightGray);
		for (int x = 0; x < WIDTH; x++) {
			g.drawLine(x * gs, 0, x * gs, HEIGHT * gs);
		}
		for (int y = 0; y < HEIGHT; y++) {
			g.drawLine(0, y * gs, WIDTH * gs, y * gs);
		}
	}
	
	public void draw(Graphics g) {
		int gs = GRID_SIZE;
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH * gs, HEIGHT * gs);
		drawGrid(g);
		
		
		
		
		/*if (dragging) {
			g.setColor(Color.black);
			//g.drawLine(dragPoint.x, dragPoint.y, Game.mouseX, Game.mouseY);
			ArrayList<Point> pts = getWireSplitPoints(dragPoint, getMousePoint());
			for (int i = 0; i < pts.size() - 1; i++) {
				g.drawLine(pts.get(i).x, pts.get(i).y, pts.get(i + 1).x, pts.get(i + 1).y);
			}
		}*/
		/*
		for (int i = 0; i < wireList.size(); i++)
			wireList.get(i).draw(g);
		*/
		
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				wireGrid[x][y].draw(g);
			}
		}
		for (int i = 0; i < gateList.size(); i++)
			gateList.get(i).draw(g);
	}
}
