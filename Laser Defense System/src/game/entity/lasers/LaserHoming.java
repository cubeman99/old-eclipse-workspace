package game.entity.lasers;

import java.util.ArrayList;
import common.GMath;
import common.Line;
import common.Settings;
import common.Vector;
import common.graphics.Draw;
import game.Control;
import game.entity.particles.LaserHomingTrail;
import game.nodes.Enemy;


public class LaserHoming extends Laser {
	private Enemy target;
	private double speed;
	private double direction;
	
	public LaserHoming(Control control) {
		super(2);
		
		this.control     = control;
		this.damage      = Settings.LASER_DAMAGE;
		this.speed       = 5;
		this.direction   = control.player.angle;
		this.target      = control.getNearestEnemy(new Vector());
		if (target != null)
			target.damageFuture(damage);
	}
	
	private void turnTowardsTarget() {
		double s, d, slow_ang = 0;
		
		
		double turnAmount = GMath.toRadians(10);
		
		double currDirection = GMath.directionSimplify(direction);
		double goalDirection = GMath.directionSimplify(GMath.direction(getPosition(), target.getPosition()));

		if (GMath.abs(goalDirection - currDirection) <= GMath.PI) {
		    s = GMath.sign(goalDirection - currDirection);
		    d = GMath.abs(goalDirection - currDirection);
		}
		else {
		    s = GMath.sign(currDirection - GMath.PI);
		    if (s < 0)
		        d = (GMath.TWO_PI - goalDirection) + currDirection;
		    else
		        d = (GMath.TWO_PI - currDirection) + goalDirection;
		}

		slow_ang = GMath.toRadians(10);
		if (slow_ang == 0)
		    slow_ang = GMath.toRadians(2);

		currDirection += Math.min(turnAmount, turnAmount * (d / slow_ang)) * s;
		currDirection  = GMath.directionSimplify(currDirection);

		direction = currDirection;
	}
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	public void update() {
		
		LaserHomingTrail eff = new LaserHomingTrail(radius, angle, direction);
		control.addEntity(eff);
		
		if (target == null) {
			target = control.getNearestEnemy(getPosition());
			if (target != null)
				target.damageFuture(damage);
		}
		else if (target.isDead()) {
			target = null;
		}
		else {
			turnTowardsTarget();
			setPosition(getPosition().plus(Vector.polarVector(speed, direction)));
		}

		Line motion = new Line(Vector.polarVector(radius, angle), Vector.polarVector(radius - 1, angle));
		ArrayList<Enemy> enemies = control.getEnemyList();
		
		// Check for enemy hits:
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			
			if (GMath.abs(radius - enemy.getRadius()) <= enemy.getMaskRadius()) {	
				double dist = enemy.getPosition().distanceToSegment(motion);
				
				if (dist < enemy.getMaskRadius()) {
					if (enemy != target && target != null) {
						target.giveFutureHealth(damage);
					}
					
					enemy.damage(damage);
					super.destroy();
				}
			}
		}
		
		// Check if out of bounds:
		if (radius > Settings.RING_SPAWN_RADIUS)
			destroy();
	}

	@Override
	public void draw() {
		Draw.draw(sprite, radius, angle, direction);
	}
}
