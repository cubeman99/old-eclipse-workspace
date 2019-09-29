package entity.unit;

import weapon.Weapon;
import weapon.WeaponM4;
import common.Draw;
import common.GMath;
import common.Vector;
import common.shape.Line;
import control.Control;
import control.Team;


public class Bot extends Unit {
	public double dirSpeed = 0;
	public double dirSpeedMax = 0.2;
	public double fieldOfView;
	public Vector crosshair;
	public Vector crosshairGoal;
	public Unit target;
	
	// ================== CONSTRUCTORS ================== //
	
	public Bot(Control control, Team team, Vector position) {
		super(team, position, Unit.UNIT_RADIUS);
		
		setMaxHealth(100);
		weapon        = new WeaponM4(this);
		fieldOfView   = GMath.toRadians(90);
		crosshair     = new Vector();
		crosshairGoal = new Vector();
		target        = null;
		
		direction     = GMath.random.nextDouble() * GMath.TWO_PI;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return whether a point is in this unit's field of view. **/
	public boolean pointInSight(Vector point) {
		if (control.map.lineTouchesWalls(new Line(getPosition(), point)))
			return false;
		double dir = GMath.direction(getPosition(), point);
		return (GMath.angleBetween(dir, direction) < fieldOfView * 0.5);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	private void updateCrosshair() {
		double aimSkill = 6.0;
		if (GMath.distance(getPosition(), crosshairGoal) > 1) {
			crosshair.x += (aimSkill * (crosshairGoal.x - crosshair.x)) / (40.0d);
			crosshair.y += (aimSkill * (crosshairGoal.y - crosshair.y)) / (40.0d);
		}
		else {
			crosshair.set(crosshairGoal);
		}
	}
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	public void update() {
		
		dirSpeed += 0.3 - GMath.random.nextFloat() * 0.6;
		dirSpeed -= GMath.random.nextFloat() * GMath.sign(dirSpeed) * (GMath.sqr(dirSpeed) / GMath.sqr(dirSpeedMax));
		dirSpeed = Math.max(-dirSpeedMax, GMath.min(dirSpeedMax, dirSpeed));;
        direction += dirSpeed;
        
        body.velocity.add(Vector.polarVector(0.01, direction));
        if (body.velocity.length() > maxSpeed)
        	body.velocity.setLength(maxSpeed);
        
        if (target != null) {
        	direction = GMath.direction(getPosition(), target.getPosition());
        	body.velocity.zero();
        	weapon.fire();
            crosshairGoal.set(target.getPosition());
            if (target.isDestroyed())
            	target = null;
            else if (!pointInSight(target.getPosition()))
        		target =  null;
        }
        else {
        	for (Team team : control.teams) {
        		if (team != this.team) {
        			for (Unit u : team.getAliveUnits()) {
        				if (u != this && pointInSight(u.getPosition())) {
        					target = u;
        					break;
        				}
        			}
        		}
        		if (target != null)
        			break;
        	}
            crosshairGoal.set(getPosition().plus(Vector.polarVector(5, direction)));
        }
        
        direction = GMath.direction(getPosition(), crosshairGoal);
        
        weapon.update();
        updateCrosshair();
	}
	
	@Override
	public void draw() {
		super.draw();
		
		Draw.setColor(team.color);
		Draw.drawCircle(crosshair, 0.05);
	}
}
