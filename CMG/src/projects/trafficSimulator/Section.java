package projects.trafficSimulator;

import java.awt.Color;
import cmg.graphics.Draw;
import cmg.math.geometry.Line;
import cmg.math.geometry.Polygon;
import cmg.math.geometry.Vector;

public class Section {
	public static final int NUM_SIDES = 2;
	public static final double CONNECT_DISTANCE = 8;
	private LaneOLD lane;
	private int index;
	private Polygon outline;
	private Line centerLine;
	private Section[] connections;
	private Line[] snapLines;
	
	
	
	public Section(LaneOLD lane, int index) {
		this.lane   = lane;
		this.index  = index;
		outline     = new Polygon();
		connections = new Section[NUM_SIDES];
		snapLines   = new Line[NUM_SIDES];
		
		refresh();
	}
	
	public Line getCenterLine() {
		return centerLine;
	}
	
	public LaneOLD getLane() {
		return lane;
	}
	
	public Section getConnection(int side) {
		return connections[side];
	}
	
	public void setConnection(int side, Section sect) {
		connections[side] = sect;
	}
	
	public Vector getClosestSnapPoint(Vector point) {
		Vector v1 = snapLines[0].getClosestPoint(point);
		Vector v2 = snapLines[1].getClosestPoint(point);
		if (v1.distanceTo(point) < v2.distanceTo(point))
			return v1;
		return v2;
	}
	
	public Line getSnapLine(int side) {
		return snapLines[side];
	}
	
	public void updateConnections(boolean updateOthers) {
		for (int side = 0; side < NUM_SIDES; side++)
			connections[side] = checkConnection(side, updateOthers);
	}
	
	public Section checkConnection(int checkSide, boolean checkOthers) {
		Line snapEdge          = snapLines[checkSide];
		Vector point            = snapEdge.end1;
		Section closestSection = null;
		double closestDist     = 0;
		int connectSide        = 0;
		
		for (int i = 0; i < lane.getMap().getLanes().size(); i++) {
			LaneOLD l = lane.getMap().getLanes().get(i);
			if (l != lane) {
				for (int j = 0; j < l.getSections().size(); j++) {
					Section sect = l.getSections().get(j);
					
					for (int side = 0; side < NUM_SIDES; side++) {
    					Vector v = sect.getCenterLine().getClosestPoint(point);
    					
    					if (v != null) {
    	    				double dist = point.distanceTo(v);
    	    				if (dist < CONNECT_DISTANCE && (dist < closestDist || closestSection == null)) {
    	    					closestDist    = dist;
    	    					closestSection = sect;
    	    					connectSide    = side;
    	    				}
    					}
					}
				}
			}
		}
		
		if (closestSection != null && checkOthers) {
			for (int side = 0; side < NUM_SIDES; side++) {
				if (closestSection != null) {
					LaneOLD l = closestSection.getLane();
					for (int i = 0; i < l.getSections().size(); i++)
						l.getSections().get(i).updateConnections(false);
				}
			}
		}
		
		return closestSection;
	}
	
	public void onDestroy() {
		for (int side = 0; side < NUM_SIDES; side++) {
			if (connections[side] != null) {
				LaneOLD l = connections[side].getLane();
				for (int i = 0; i < l.getSections().size(); i++)
					l.getSections().get(i).updateConnections(false);
			}
		}
	}
	
	public void refresh() {
		centerLine = lane.getPath().getEdge(index);
		outline.clear();
		
		for (int side = 0; side < NUM_SIDES; side++) {
			connections[side] = null;

			Line l = lane.getEdgeLine(index, lane.getWidth() / 2, side);
			outline.addVertex(l.getEnd(side));
			outline.addVertex(l.getEnd(1 - side));
			
			snapLines[side]   = lane.getEdgeLine(index, lane.getWidth(), side);
		}
		
		updateConnections(true);
	}
	
	public void draw() {
		for (int side = 0; side < NUM_SIDES; side++) {
			if (connections[side] == null) {
				Draw.setColor(Color.BLACK);
				Draw.drawLine(outline.getVertex(side * 2),
						outline.getVertex((side * 2) + 1));
				
				Draw.setColor(Color.WHITE);
				Draw.draw(snapLines[side]);
			}
			else {
				Draw.setColor(Color.YELLOW);
				Draw.drawLine(outline.getVertex(side * 2),
						outline.getVertex((side * 2) + 1));
			}
		}
	}
}
