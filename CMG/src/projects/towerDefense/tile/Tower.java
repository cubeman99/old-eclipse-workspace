package projects.towerDefense.tile;

import java.awt.Color;
import java.util.ArrayList;
import projects.towerDefense.Range;
import projects.towerDefense.RangeCircle;
import projects.towerDefense.RangeDiamond;
import projects.towerDefense.RangeSquare;
import projects.towerDefense.entity.Bullet;
import projects.towerDefense.entity.Creep;
import projects.towerDefense.entity.Entity;
import cmg.graphics.Draw;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;

public class Tower extends Tile {
	public static final int METHOD_RANDOM       = 0;
	public static final int METHOD_FRONT        = 1;
	public static final int METHOD_BACK         = 2;
	public static final int METHOD_NEAREST      = 3;
	public static final int METHOD_FARTHEST     = 4;
	public static final int METHOD_MOST_HEALTH  = 5;
	public static final int METHOD_LEAST_HEALTH = 6;
	
	protected Creep target;
	protected double shootTimer;
	protected double damage;
	protected int targetingMethod;
	protected double direction;
	protected double range;
	protected Range rangeFinder;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Tower() {
		super();
		
		damage          = 1;
		range           = 2.5;
		targetingMethod = METHOD_FRONT;
		rangeFinder     = new RangeCircle();
		
		direction  = GMath.random.nextDouble() * GMath.TWO_PI;
		target     = null;
		shootTimer = 0;
	}
	
	

	// =================== ACCESSORS =================== //
	
	public boolean isInRange(Point loc) {
		return (rangeFinder.check(location, loc, range));
	}
	
	public boolean canShoot() {
		return (shootTimer <= 0);
	}
	
	public boolean isValidTarget(Creep c) {
		return (c != null && !c.isTargetDead() && !c.isDead()
				&& isInRange(c.getLocation()));
	}
	
	public ArrayList<Creep> getCreepsInRange() {
		ArrayList<Creep> list = new ArrayList<Creep>();
		ArrayList<Entity> entities = level.getEntities();

		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof Creep) {
				Creep c = (Creep) entities.get(i);
				if (isValidTarget(c))
					list.add(c);
			}
		}
		
		return list;
	}
	
	public Creep getTarget() {
		ArrayList<Creep> creeps = getCreepsInRange();
		
		if (creeps.size() > 0) {
			if (targetingMethod == METHOD_RANDOM)
				return creeps.get(GMath.random.nextInt(creeps.size()));
			else if (targetingMethod == METHOD_FRONT)
				return creeps.get(0);
			else if (targetingMethod == METHOD_BACK)
				return creeps.get(creeps.size() - 1);
			else if (targetingMethod == METHOD_NEAREST)
				return creeps.get(0);
			else if (targetingMethod == METHOD_FARTHEST)
				return creeps.get(0);
			else if (targetingMethod == METHOD_MOST_HEALTH)
				return creeps.get(0);
			else if (targetingMethod == METHOD_LEAST_HEALTH)
				return creeps.get(0);
		}
		return null;
	}
	
	public Creep getNearestCreep() {
		Creep nearestCreep = null;
		double nearestDist = 0;
		ArrayList<Entity> entities = level.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof Creep) {
				Creep c = (Creep) entities.get(i);
				
				if (isValidTarget(c)) {
    				double dist = c.getPosition().distanceTo(getCenter());
    				if (nearestCreep == null || dist < nearestDist) {
    					nearestCreep = c;
    					nearestDist  = dist;
    				}
				}
			}
		}
		return nearestCreep;
	}

	
	
	// ==================== MUTATORS ==================== //


	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public Tile clone() {
		return new Tower();
	}
	
	@Override
	public void update() {
		if (shootTimer > 0)
			shootTimer -= 1;
		
		if (!isValidTarget(target))
			target = getTarget();
		
		if (target != null) {
			direction = GMath.direction(getCenter(), target.getPosition());
		}
		
		if (canShoot()) {
			if (target != null) {
				shootTimer = 60;
				level.addEntity(new Bullet(getCenter(), target, damage));
				target.applyTargetDamage(damage);
			}
		}
	}
	
	@Override
	public void draw() {
		Vector v = new Vector(location).scale(SIZE);
		Draw.setColor(Color.LIGHT_GRAY);
		Draw.fillRect(v, new Vector(SIZE, SIZE));

		Draw.setColor(Color.BLACK);
		Draw.drawRect(v, new Vector(SIZE, SIZE));
		
		Vector center = v.plus(SIZE / 2, SIZE / 2);
		Draw.setColor(Color.GRAY);
		Draw.fillCircle(center, SIZE / 3);

		Draw.setColor(Color.BLACK);
		Draw.drawLine(center, center.plus(Vector.polarVector((Tile.SIZE / 3) - 1, direction)));
	}
	
	public void postDraw() {
		if (isHovered()) {
			if (Mouse.wheelUp())
				range += 0.1;
			if (Mouse.wheelDown())
				range -= 0.1;
			
			Draw.setColor(new Color(255, 255, 255, 128));
			
			for (int x = 0; x < level.getWidth(); x++) {
				for (int y = 0; y < level.getHeight(); y++) {
					Point loc = new Point(x, y);
					if (isInRange(loc)) {
						Draw.fillRect(new Vector(loc).scaledBy(SIZE), new Vector(SIZE, SIZE));
					}
				}
			}
			
			Vector v = new Vector(location).scale(SIZE);
			Vector center = v.plus(SIZE / 2, SIZE / 2);
			Draw.setColor(Color.WHITE);
			Draw.drawCircle(center, range * SIZE);
			

			Draw.setColor(Color.RED);
			Draw.drawString(range + "", center, Draw.CENTER, Draw.MIDDLE);
		}
	}
}
