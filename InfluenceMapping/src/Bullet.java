

import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Entity {
	public Unit owner;
	public int team = 0;
	Vector end1 = new Vector();
	Vector end2 = new Vector();
	
	public double x = 0;
	public double y = 0;
	public double direction = 0;
	public double speed = 0;
	
	// Adjustable Variables:
	public double damage = 0;
	public double bulletLength = 10;
	
	public Bullet(Unit owner, double x, double y, double direction, double speed, double damage) {
		this.owner		= owner;
		this.team		= owner.team;
		this.x			= x;
		this.y			= y;
		this.direction	= direction;
		this.speed		= speed;
		this.damage		= damage;

		// Adjustable Variables:
		bulletLength 	= 32;
	}
	
	public void update() {
		setEnds();
		
		
		// Collide with Units
		for (Unit u : Game.unitControl.units) {
			if (u != owner) {
				Line l = getLine();
				double dist;
				dist = l.pointDistance(u.x, u.y);
				if (dist < Unit.radius) {
					// HIT!
					if (u.team != team || Game.unitControl.friendlyFire) {
						// Do damage to opposing team members
						// Damage is greater when bullet is closer to the center of the enemy
						double dmg = damage * (1.0 - GMath.sqr(dist / Unit.radius));
						if (u.takeDamage(owner, dmg)) {
							// Enemy Down!
							owner.addKill();
						}
					}
					destroy();
					break;
				}
			}
		}
		
		// Collide with Solid Walls
		World w = Game.world;
		//double ts = w.tileSize;
		for (int xx = 0; xx < w.width; xx++) {
			for (int yy = 0; yy < w.height; yy++) {
				if (w.tiles[xx][yy].isSolid) {
					if (Collisions.segmentRectangle(getLine(), w.getTileRect(xx, yy))) {
						destroy();
						break;
					}
				}
			}
			if (entityDestroyed)
				break;
		}
		
		// Check if outside the Map
		if (!Game.world.pointInsideWorld(end1) || !Game.world.pointInsideWorld(end2)) {
			destroy();
		}
		
		// Move
		x += getHspeed();
		y += getVspeed();
	}
	
	public double getHspeed() {
		return GMath.lenDirX(speed, direction);
	}
	
	public double getVspeed() {
		return GMath.lenDirY(speed, direction);
	}
	
	public boolean collisionRectangle(double x1, double y1, double x2, double y2) {
		Line l = new Line(end1.x, end1.y, end2.x, end2.y);
		
		if (l.isVertical()) {
			return (l.x1 >= x1 && l.x1 < x2);
		}
		else {
			if (l.getY(x1) >= y1 && l.getY(x1) < y2)
				return true;
			if (l.getY(x2) >= y1 && l.getY(x2) < y2)
				return true;
		}
		return false;
	}
	
	public Line getLine() {
		return new Line(end1, end2);
	}
	public void setEnds() {
		end1.set(x, y);
		end2.set(x + GMath.lenDirX(bulletLength, direction), y + GMath.lenDirY(bulletLength, direction));
	}
	
	public void draw(Graphics g) {
		setEnds();
		g.setColor(Color.yellow);
		g.drawLine((int) end1.x, (int) end1.y, (int) end2.x, (int) end2.y);
	}
}
