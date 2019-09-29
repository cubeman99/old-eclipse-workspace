package game;

import java.awt.Color;
import java.util.ArrayList;
import main.Keyboard;
import common.GMath;
import common.Point;
import common.Vector;
import common.graphics.Draw;

public class Car {
	public World world;
	private Vector position;
	private Road currentRoad;
	private int direction;
	private Vector turnAnchor;
	private double turnAngle;
	private double turnAmount;
	private double turnRadius;
	private double turnSign;
	private boolean turning;
	private double speed;
	private double faceAngle;
	private ArrayList<Road> pathToIntersection;
	private Road nextIntersection;
	
	private Road testRoad;
	private double prevDist;
	
	public Car(Road road) {
		this.world         = road.world;
		this.currentRoad   = road;
		this.position      = new Vector(road.position).plus(0.5, 0.75);
		this.direction     = 0;
		this.turnAnchor    = null;
		this.turnAngle     = 0;
		this.turnAmount    = 0;
		this.turnRadius    = 0;
		this.turnSign      = 1;
		this.turning       = false;
		this.speed         = 0.02;
		this.faceAngle     = 0;
		this.pathToIntersection = new ArrayList<Road>();
		this.nextIntersection   = null;
		this.testRoad = null;
		prevDist = 0;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public void update() {
		
		
		if (Keyboard.enter.pressed())
			calculatePathToIntersection(currentRoad);
		
		if (turning) {
			turnAngle += speed / turnRadius;
			if (turnAngle >= turnAmount) {
				turning  = false;
				turnAngle = turnAmount;
			}
			
			position.set(turnAnchor.plus(Vector.polarVector(turnRadius, (direction * GMath.HALF_PI) - turnSign * (GMath.HALF_PI + turnAmount - turnAngle))));
			faceAngle = (direction * GMath.HALF_PI) - turnSign * (turnAmount - turnAngle);
			
			
			if (!turning) {
				//calculatePathToIntersection();
			}
		}
		else {
			position.add(GMath.DIRECTION_VECTORS[direction].scaledBy(speed));
			faceAngle = direction * GMath.HALF_PI;
			
    		Road newRoad = getRoadBeneath(position);
    		if (newRoad != currentRoad)
    			nextRoad(newRoad);
		}
	}
	
	private double getDistanceToNextRoad() {
		if (turning)
			return ((turnAmount - turnAngle) * turnRadius);
		if (direction == 0)
			return (1 - (position.x % 1));
		if (direction == 1)
			return (position.y % 1);
		if (direction == 2)
			return (position.x % 1);
		return (1 - (position.y % 1));
	}
	
	private double getDistanceToIntersection() {
		double dist = getDistanceToNextRoad();
		
		for (int i = 1; i < pathToIntersection.size(); i++) {
			if (i == pathToIntersection.size() - 1)
				dist += pathToIntersection.get(i).getDistanceTo(nextIntersection);
			else
				dist += pathToIntersection.get(i).getDistanceTo(pathToIntersection.get(i + 1));
			
		}
		
		return dist;
	}
	
	private void calculatePathToIntersection(Road roadFrom) {
		pathToIntersection.clear();
		pathToIntersection.add(currentRoad);
		Road road   = roadFrom;
		int prevDir = direction;
		
		while (!road.isIntersection()) {
			pathToIntersection.add(road);
			
			for (int i = 0; i < 4; i++) {
				if ((i != (prevDir + 2) % 4 || road.getNeighborCount() == 1) && road.getNeighbor(i) != null) {
					prevDir = i;
					road    = road.getNeighbor(i);
					break;
				}
			}
		}
		
		nextIntersection = road;
		System.out.println("Caluclated. " + direction);
		testRoad = currentRoad;
	}
	
	private void nextRoad(Road newRoad) {
		ArrayList<Integer> possibleNextDirections = new ArrayList<Integer>();
		int nextRelDir = 2;
		int nextDir    = (direction + 2) % 4;
		Road nextRoad  = currentRoad;
		Road prevRoad  = currentRoad;
		turnAmount     = 0;
		turnAngle      = 0;
		turning        = false;
		turnSign       = 1;
		
		for (int dir = 0; dir < 4; dir++) {
			Road next = newRoad.getNeighbor((direction + dir) % 4);
			if (next != null && next != currentRoad)
				possibleNextDirections.add(dir);
		}
		if (possibleNextDirections.size() > 0) {
			nextRelDir = possibleNextDirections.get(GMath.random.nextInt(possibleNextDirections.size()));
			nextDir    = (direction + nextRelDir) % 4;
			nextRoad   = newRoad.getNeighbor(nextDir);
		}

		if (nextRelDir == 1) {
			turnRadius = 0.75;
			turnAnchor = position.plus(GMath.DIRECTION_VECTORS[nextDir].scaledBy(0.75));
			turnAmount = GMath.HALF_PI;
			turning    = true;
		}
		else if (nextRelDir == 3) {
			turnRadius = 0.25;
			turnAnchor = position.plus(GMath.DIRECTION_VECTORS[nextDir].scaledBy(0.25));
			turnAmount = GMath.HALF_PI;
			turning    = true;
			turnSign   = -1;
		}
		else if (nextRelDir == 2) {
			turnRadius = 0.25;
			turnAnchor = position.plus(GMath.DIRECTION_VECTORS[(direction + 1) % 4].scaledBy(0.25));
			turnAmount = GMath.PI;
			turning    = true;
		}
		
		currentRoad = newRoad;
		direction   = nextDir;
		
		if (pathToIntersection.size() > 0) {
    		if (pathToIntersection.get(0) == prevRoad)
    			pathToIntersection.remove(0);
		}
		
		if (newRoad.isIntersection()) {
			calculatePathToIntersection(nextRoad);
		}
	}
	
	public Road getRoadBeneath(Vector v) {
		return world.get((int) v.x, (int) v.y);
	}
	
	public void draw() {
		Draw.setColor(Color.CYAN);
		Draw.drawCircle(position, 0.2);
		Draw.drawLine(position, position.plus(Vector.polarVector(0.2, faceAngle)));
		
		if (currentRoad != null) {
			Draw.setColor(new Color(255, 255, 100));
			Draw.drawRect(new Vector(currentRoad.getPosition()), new Vector(1, 1));
		}

		Draw.setColor(new Color(0, 255, 0));
		if (pathToIntersection.size() > 1) {
			for (int i = 1; i < pathToIntersection.size(); i++) {
				Draw.drawLine(new Vector(pathToIntersection.get(i).getPosition()).plus(0.5, 0.5),
						  new Vector(pathToIntersection.get(i - 1).getPosition()).plus(0.5, 0.5));
			}
		}
		if (pathToIntersection.size() > 0) {
			Draw.drawLine(new Vector(pathToIntersection.get(pathToIntersection.size() - 1).getPosition()).plus(0.5, 0.5),
					  new Vector(nextIntersection.getPosition()).plus(0.5, 0.5));
			
			if (nextIntersection != null) {
				double dist = getDistanceToIntersection();
				Draw.getGraphics().drawString("Dist: " + dist, 20, 30);
				Draw.getGraphics().drawString("d: " + (dist - prevDist), 20, 55);
				prevDist = dist;
				if (dist - prevDist >= 0)
					System.out.println(dist - prevDist);
				
			}
		}
		if (nextIntersection != null) {
			Draw.setColor(new Color(255, 255, 0));
			Draw.drawRect(new Vector(nextIntersection.getPosition()), new Vector(1, 1));
		}
		if (testRoad != null) {
//			Draw.setColor(new Color(0, 255, 255));
//			Draw.drawRect(new Vector(testRoad.getPosition()), new Vector(1, 1));
		}
	}
}
