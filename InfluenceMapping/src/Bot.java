
import java.awt.Graphics;
import java.util.ArrayList;

public class Bot extends Unit {
	public ArrayList<Node> targetPath = null;
	public Unit targetUnit = null;
	public Node targetNode = null;
	
	public Vector aimCursor;
	public Vector aimGoal;
	public double aimSkill = 6; // [1 - 10]
	public boolean targetInSight = false;
	
	public ArrayList<Double> aimEncounterData = new ArrayList<Double>();
	public double avgAimDistance = 200;
	
	public Node coverNode 			= null;
	public boolean covering 		= false;
	
	public boolean goingForHealth 	= false;
	public boolean outOfAmmo		= false;
	public Pickup targetPickup	  	= null;
	
	public Bot(int team) {
		this(team, 0, 0);
	}
	
	public Bot(int team, double x, double y) {
		super(team, x, y);
		
		aimCursor	= new Vector(x, y);
		aimGoal		= new Vector(x, y);
		weaponType = new WeaponAR1();
		giveNewWeapon();
	}
	
	public void update() {
		super.update();
		
		if (Game.keys.use.isDown)
			weapon.clipAmmo = weapon.maxClipAmmo;
		if (Game.keys.switchWeapons.isDown) {
			weapon.clipAmmo = 0;
			weapon.packAmmo = 0;
		}
		
		targetInSight = false;
		
		if (dead) {
			targetUnit	= null;
			targetNode	= null;
			targetPath	= null;
			coverNode	= null;
			covering	= false;
		}
		else {
			////////////////////////
			//   BOT AI METHODS   //
			////////////////////////
			
			outOfAmmo = (weapon.clipAmmo + weapon.packAmmo == 0);
			
			if (!goingForHealth && health < 40) {
				targetPickup = getNearestPickup(Pickup.TYPE_HEALTH);
				if (targetPickup != null) {
					targetPath = Game.nodeMap.findPath(getCenter(), targetPickup.getCenter());
					goingForHealth = true;
					covering = false;
					weapon.reload();
				}
			}
			/*
			if (!goingForHealth && outOfAmmo) {
				targetPickup = getNearestPickup(Pickup.TYPE_AMMO);
				if (targetPickup != null) {
					targetPath = Game.nodeMap.findPath(getCenter(), targetPickup.getCenter());
				}
			}*/

			if (!goingForHealth && !covering && (weapon.reloading || outOfAmmo)) {
				coverNode = getNearestCoverPath();
				if (coverNode != null)
					covering = true;
			}
			
			if (goingForHealth) {
				if (targetPath == null || targetPickup.away)
					goingForHealth = false;
			}
			else if (covering || outOfAmmo) {
				unitCover();
			}
			else if (targetUnit == null) {
				unitLookForEnemy();
				roamNodeGrid();
			}
			else if (targetUnit.dead) {
				targetUnit = null;
			}
			else {
				unitEngageFire();
			}
			
			
			
			if (targetPath == null) {
				if (targetUnit != null && !covering)
					moveSpeed = 0;
			}
			else if (targetPath.isEmpty())
				targetPath = null;
			else {
				unitFollowPath();
			}
			
//			if (!targetInSight) {
//				aimGoal.x = x + GMath.lenDirX(avgAimDistance, faceDir);
//				aimGoal.y = y + GMath.lenDirY(avgAimDistance, faceDir);
//			}
		}
		
		updateAimCursor();
	}
	
	public Pickup getNearestPickup(int type) {
		double best  = -1;
		Pickup pBest = null;
		for (Pickup p : Game.world.pickups) {
			if (!p.away && p.type == type && (pBest == null || pointIsSafe(p.getCenter()))) {
				double checkBest = GMath.distance(getCenter(), p.getCenter());
				if (checkBest < best || best < 0) {
					best = checkBest;
					pBest = p;
				}
			}
		}
		return pBest;
	}
	
	public boolean pointIsSafe(Vector v) {
		for (Unit u : Game.unitControl.units) {
			if (u.team != team) {
				if (Game.nodeMap.lineCollisionFree(new Line(v.x, v.y, u.x, u.y))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void unitFollowPath() {
		// Follow a path
		direction = GMath.direction(getCenter(), targetPath.get(0).getVector());
		moveSpeed = maxMoveSpeed;
		faceDir = direction;
		if (GMath.distance(getCenter(), targetPath.get(0).getVector()) < maxMoveSpeed) {
			targetPath.remove(0);
		}
		if (targetUnit == null) {
			aimGoal.x = x + GMath.lenDirX(avgAimDistance, faceDir);
			aimGoal.y = y + GMath.lenDirY(avgAimDistance, faceDir);
		}
	}
	
	public void unitEngageFire() {
		targetNode = null;
		
		if (Game.nodeMap.lineCollisionFree(new Line(this, targetUnit))) {
			// In Sight! Line up aim and SHOOT!
			targetInSight = true;
			aimAtTarget();
			targetPath = null;
			fireWeapon();
		}
		else {
			// Find a Path
//			targetPath = Game.nodeMap.findPath(this.getCenter(), targetUnit.getCenter());
				targetUnit = null;
//			if (targetPath == null) {
//				targetUnit = null;
//			}
//			else {
//				targetPath.remove(0);
//			}
		}
	}
	
	public void unitLookForEnemy() {
		// Look for enemies
		for (Unit u : Game.unitControl.units){
			if (u.team != team && !u.dead) {
				// Enemy Found!
				if (Game.nodeMap.lineCollisionFree(new Line(this, u))) {
					// In Sight!
					targetUnit = u;
					targetInSight = true;
					//aimEncounterData.add(GMath.distance(this, u));
					//updateAimEncounterData();
				}
			}
		}
	}
	
	public void unitCover() {
		// First Check if Cover is lost:
		if (coverNode != null) {
			for (Unit u : Game.unitControl.units) {
				if (u.team != team) {
					if (Game.nodeMap.lineCollisionFree(new Line(coverNode.x, coverNode.y, u.x, u.y))) {
						coverNode = getNearestCoverPath();
						covering = (coverNode != null);
						break;
					}
				}
			}
		}
		if (covering) {
			
			if (GMath.distance(getCenter(), coverNode.getVector()) > 5) {
				direction = GMath.direction(getCenter(), coverNode.getVector());
				moveSpeed = maxMoveSpeed;
			}
			else
				moveSpeed = 0;
			
			if (!weapon.reloading && weapon.clipAmmo != 0) {
				covering   = false;
				coverNode  = null;
			}
		}
	}
	
	public Node getNearestCoverPath() {
		Node nearest = null;
		double dist  = 0;
		for (Node n : Game.nodeMap.nodeList) {
			double d = GMath.distance(x, y, n.x, n.y);
			if ((d < dist || nearest == null) && d < 200 && Game.nodeMap.lineCollisionFree(new Line(x, y, n.x, n.y))) {
				if (pointIsSafe(n.getVector())) {
					nearest = n;
					dist	= d;
				}
			}
		}
		return nearest;
	}
	
	public void aimAtTarget() {
		if (targetUnit != null) {
			aimGoal.set(targetUnit.getCenter());
			double dist = GMath.distance(this, targetUnit);
			aimGoal.x += targetUnit.getHspeed() * (dist / weapon.bulletSpeed);
			aimGoal.y += targetUnit.getVspeed() * (dist / weapon.bulletSpeed);
		}
	}
	
	public void updateAimCursor() {
		if (GMath.distance(getCenter(), aimGoal) > 32) {
			aimCursor.x += (aimSkill * (aimGoal.x - aimCursor.x)) / (40.0d);
			aimCursor.y += (aimSkill * (aimGoal.y - aimCursor.y)) / (40.0d);
		}
		else {
			aimCursor.set(aimGoal);
		}
		faceDir = GMath.direction(getCenter(), aimCursor);
	}
	
	public void updateAimEncounterData() {
		avgAimDistance = 0;
		for (double dbl : aimEncounterData)
			avgAimDistance += dbl;
		avgAimDistance /= aimEncounterData.size();
	}
	
	public void roamNodeGrid() {
		// Roam the Node Grid
		moveSpeed = 0;
		if (Game.nodeMap.nodeList.size() > 0) {
			if (targetNode == null)
				targetNode = Game.nodeMap.getNearestNode(x, y);
			else if (GMath.distance(getCenter(), targetNode.getVector()) < maxMoveSpeed) {
				// Move to next node
				targetNode = targetNode.getRandomNeighbor();
			}
		}
		if (!Game.nodeMap.nodeList.contains(targetNode))
			targetNode = null;
		if (targetNode != null) {
			direction = GMath.direction(getCenter(), targetNode.getVector());
			moveSpeed = maxMoveSpeed;
			faceDir = direction;
		}
		
		aimGoal.x = x + GMath.lenDirX(avgAimDistance, faceDir);
		aimGoal.y = y + GMath.lenDirY(avgAimDistance, faceDir);
	}
	
	public void clearTargetPath() {
		targetPath = null;
	}
	
	public void draw(Graphics g) {
		super.draw(g);
		
		if (targetPath != null) {
			//g.setColor(teamControl.color);
			//g.drawLine((int)x, (int)y, (int)targetPath.get(0).x, (int)targetPath.get(0).y);
			//NodeMap.drawPath(g, targetPath, teamControl.color);
		}
		
		if (targetNode != null) {
			//targetNode.draw(g, teamControl.color, true);
		}
		if (covering)
			g.drawOval((int)x - 4, (int)y - 4, 8, 8);
		
		if (!dead) {
			if (coverNode != null) {
				//g.setColor(Color.white);
				//coverNode.draw(g, Color.white, true);
				//g.drawLine((int) x, (int) y, (int) coverNode.x, (int) coverNode.y);
			}
			
			g.setColor(teamControl.color);
			aimCursor.draw(g, 2);
		}
	}
}
