
import java.awt.*;
import java.util.ArrayList;


public class Unit {
	public static final int radius = 12;
	public static final int diameter = 2 * radius;
	
	public TeamControl teamControl;
	public double x;
	public double y;
	public double respawnX;
	public double respawnY;
	public double direction = 0;
	public double speed     = 0;
	public double faceDir   = 0;
	public int shoottimer	= 0;
	
	public int team;
	public double health;
	public double maxHealth;
	public double maxMoveSpeed	= 3.0;
	public double moveSpeed		= 0.0;
	public double moveAcceleration = 0.14;
	public ArrayList<Double> speedFactors = new ArrayList<Double>();
	
	public Weapon weaponType = new WeaponAR1();
	public Weapon weapon;
	
	public boolean dead				= false;
	public Timer respawnTimer		= new Timer();
	public Unit killer				= null;
	public int kills				= 0;
	public int deaths				= 0;
	public double killDeathRatio	= -1;
	
	public Unit(int team, double x, double y) {
		this.team = team;
		this.teamControl = TeamControl.getTeam(team);
		this.x = x;
		this.y = y;
		respawnX = x;
		respawnY = y;
		
		maxHealth = 100;
		health = maxHealth;
		
		giveNewWeapon();
		
		if (teamControl.spawnTiles.size() > 0)
			setPosition(teamControl.pickSpawnPosition());
	}
	
	public void giveNewWeapon() {
		try {
			weapon = weaponType.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void addKill() {
		kills += 1;
		if (deaths > 0)
			killDeathRatio = (double)kills / (double)deaths;
	}
	
	public void addDeath() {
		deaths += 1;
		if (deaths > 0)
			killDeathRatio = (double)kills / (double)deaths;
	}
	
	// PRE-UPDATE
	public void preUpdate() {
	}
	
	// UPDATE
	public void update() {
		if (dead) {
			if (respawnTimer.getSeconds() >= Game.unitControl.respawnTime) {
				unitRespawn();
			}
		}
		else if (health <= 0) {
			unitDie();
		}

		speedFactors.clear();
		
		if (shoottimer > 0)
			shoottimer--;
		
	}
	
	// POST-UPDATE
	public void postUpdate() {
		
		if (weapon.isFiring())
			addSpeedFactor(weapon.fireSpeedFactor);
		else
			addSpeedFactor(weapon.speedFactor);
		
		// Compute Speed with the product of all speed factors
		double maxSpd = maxMoveSpeed;
		for (double factor : speedFactors)
			maxSpd *= factor;
		moveSpeed = Math.min(moveSpeed, maxSpd);
		speed = moveSpeed;
		
		
		// Check Collisions with Solid walls
		collideWithWalls();
		// Push other Units aside
		if (Game.unitUnitCollisions)
			collideWithUnits();
		
		// Update weapon
		weapon.update();
		
		// Move
		x += getHspeed();
		y += getVspeed();
	}
	
	public void unitDie() {
		dead = true;
		addDeath();
		respawnTimer.reset();
		respawnTimer.start();
		
		setPosition(-100, -100);
	}
	
	public void unitRespawn() {
		dead = false;
		respawnTimer.stop();
		health = maxHealth;
		killer = null;
		if (teamControl.spawnTiles.size() > 0)
			setPosition(teamControl.pickSpawnPosition());
		else
			setPosition(respawnX, respawnY);
		
		try {
			weapon = weaponType.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean takeDamage(Unit dealer, double dmg) {
		health -= dmg;
		if (health <= 0) {
			killer = dealer;
		}
		return (health <= 0);
	}
	
	public void addSpeedFactor(double factor) {
		speedFactors.add(factor);
	}
	
	public boolean fireWeapon() {
		return weapon.fire(this, x, y, faceDir);
	}
	
	public void setPosition(Vector v) {
		x = v.x;
		y = v.y;
	}
	
	public void setPosition(double xx, double yy) {
		x = xx;
		y = yy;
	}
	
	public boolean collideWithWalls() {
		World w = Game.world;
		double newX = x + getHspeed();
		double newY = y + getVspeed();
		boolean collide = false;
		for (int xx = 0; xx < w.width; xx++) {
			for (int yy = 0; yy < w.height; yy++) {
				if (w.tiles[xx][yy].isSolid) {
					Vector v = Collisions.rectPointClosest(new Vector(newX, newY), w.getTileRect(xx, yy));
					if (v.distance(new Vector(newX, newY)) < radius) {
						cutDirection(GMath.direction(getCenter(), v));
						x = v.x + GMath.lenDirX(radius, GMath.direction(v.x, v.y, x, y));
						y = v.y + GMath.lenDirY(radius, GMath.direction(v.x, v.y, x, y));
						newX = x + getHspeed();
						newY = y + getVspeed();
						collide = true;
					}
				}
			}
		}
		return collide;
	}
	
	public boolean collideWithUnits() {
		double newX = x + getHspeed();
		double newY = y + getVspeed();
		boolean collide = false;
		for (Unit u : Game.unitControl.units) {
			if (u != this) {
				if (GMath.distance(newX, newY, u.x, u.y) < radius * 2) {
					speed *= 0.8d;
					double push = 0.5; // Math.max(1, speed)
					double addX = GMath.lenDirX(push, GMath.direction(x, y, u.x, u.y));
					double addY = GMath.lenDirY(push, GMath.direction(x, y, u.x, u.y));
					//if (!u.checkCollisions(u.x + addX, u.y + addY) ) {
						u.x += addX;
						u.y += addY;
						collide = true;
					//}
				}
			}
		}
		return collide;
	}
	
	public Vector getCenter() {
		return new Vector(x, y);
	}
	
	public Circle getCircle() {
		return new Circle(x, y, radius);
	}
	
	public boolean checkCollisions(double checkX, double checkY) {
		World w = Game.world;
		for (int xx = 0; xx < w.width; xx++) {
			for (int yy = 0; yy < w.height; yy++) {
				if (w.tiles[xx][yy].isSolid) {
					return Collisions.circleRectangle(new Circle(checkX, checkY, radius), w.getTileRect(xx, yy));
				}
			}
		}
		return false;
	}
	
	public void giveHealth(double amount) {
		health = Math.min(maxHealth, health + amount);
	}
	
	public double getHspeed() {
		return GMath.lenDirX(speed, direction);
	}
	
	public double getVspeed() {
		return GMath.lenDirY(speed, direction);
	}
	
	public void setHspeed(double hspeed) {
		double vspeed = getVspeed();
		direction = GMath.direction(hspeed, vspeed);
		speed = GMath.distance(0, 0, hspeed, vspeed);
	}
	
	public void setVspeed(double vspeed) {
		double hspeed = getHspeed();
		direction = GMath.direction(hspeed, vspeed);
		speed = GMath.distance(0, 0, hspeed, vspeed);
	}

	public void cutDirection(double d) {
		double dCut = d;
		
		Vector vecDir = new Vector(getHspeed(), getVspeed());
		double anchor = (direction - dCut);
		
		double proj = vecDir.length() * GMath.cos(anchor);
		Vector vecProj = new Vector(GMath.lenDirX(proj, dCut), GMath.lenDirY(proj, dCut));
		vecDir.subtract(vecProj);
		
		setHspeed(vecDir.x);
		setVspeed(vecDir.y);
	}
	

	public void draw(Graphics g) {
		
		if (!dead) {
			g.setColor(teamControl.color);
			g.drawOval((int) (x - radius), (int) (y - radius), diameter, diameter);
			g.drawLine((int)x, (int)y, (int)(x + GMath.lenDirX(radius, faceDir)), (int)(y + GMath.lenDirY(radius, faceDir)));
			
			// Draw health bar
			int xx = radius - 2;
			int yy = radius + 5;
			g.setColor(Color.darkGray);
			g.drawLine((int)x - xx, (int)y - yy, (int)x + xx, (int)y - yy);
			if (health > 0) {
				g.setColor(Color.green);
				g.drawLine((int)x - xx, (int)y - yy, (int) (x - xx + (2 * xx * (health / maxHealth))), (int)y - yy);
			}

			if (weapon.reloading && weapon.reloadTime != 0) {
				g.setColor(Color.darkGray);
				g.drawLine((int)x - xx, (int)y + yy, (int)x + xx, (int)y + yy);
				g.setColor(Color.white);
				g.drawLine((int)x - xx, (int)y + yy, (int) (x - xx + (2 * xx * (weapon.reloadTimer.getSeconds() / weapon.reloadTime))), (int)y + yy);
			}
			

			if (weapon.maxClipAmmo > 0) {
				g.setColor(Color.darkGray);
				g.drawLine((int)x - yy, (int)y - xx, (int)x - yy, (int)y + xx);
				g.setColor(Color.white);
				if (weapon.clipAmmo > 0)
					g.drawLine((int)x - yy, (int) (y + xx - (2.0d * (double)xx * ((double)weapon.clipAmmo / (double)weapon.maxClipAmmo))), (int)x - yy, (int)y + xx);
			}
			if (weapon.maxPackAmmo > 0) {
				g.setColor(Color.darkGray);
				g.drawLine((int)x + yy, (int)y - xx, (int)x + yy, (int)y + xx);
				g.setColor(Color.white);
				if (weapon.packAmmo > 0)
					g.drawLine((int)x + yy, (int) (y + xx - (2.0d * (double)xx * ((double)weapon.packAmmo / (double)weapon.maxPackAmmo))), (int)x + yy, (int)y + xx);
			}
		}
	}
}
