package game.nodes;

import game.Control;
import game.entity.Entity;
import game.entity.particles.EnemyJunk;
import common.Vector;

public abstract class Enemy extends Entity {
	public Control control;
	protected double radius;
	protected double angle;
	protected double health;
	protected double futureHealth;
	protected double damage;
	protected double maskRadius;
	protected double damageAbsorb;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Enemy(Control control, double radius, double angle) {
		super(0);
		this.control      = control;
		this.radius       = radius;
		this.angle        = angle;
		this.health       = 100;
		this.futureHealth = 100;
		this.damage       = 25;
		this.maskRadius   = 24;
		this.damageAbsorb = 0;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return whether this node is dead. **/
	public boolean isDead() {
		if (health <= 0 && !isDestroyed())
			enemyDestroy();
		return isDestroyed();
	}
	
	/** Return whether this will be dead in the future. **/
	public boolean isFutureDead() {
		return (isDead() || futureHealth <= 0);
	}
	
	/** Return the position of this object in rectangle form. **/
	public Vector getPosition() {
		return Vector.polarVector(getRadius(), getAngle());
	}
	
	/** Return the radius component of this enemy's position. **/
	public double getRadius() {
		return radius;
	}

	/** Return the angle component of this enemy's position. **/
	public double getAngle() {
		return angle;
	}
	
	/** Return the radius of this enemy's circular collision mask. **/
	public double getMaskRadius() {
		return maskRadius;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Crash into the planet, damaging it and creating debris. **/
	public void crash() {
		control.damagePlanet(damage);
		
		// TODO: debris
		
		enemyDestroy();
	}
	
	/** Damage this node by a certain amount; taking away from its health. **/
	public void damage(double amount) {
		this.health -= amount;
		this.health += amount * damageAbsorb;
		
		// Check if health is below zero.
		isDead();
	}
	
	/** Damage this node future health (used for homing missiles). **/
	public void damageFuture(double amount) {
		this.futureHealth -= amount;
		this.futureHealth += amount * damageAbsorb;
	}
	
	/** Give back future health to this enemy (used for homing missiles). **/
	public void giveFutureHealth(double amount) {
		this.futureHealth += amount;
		this.futureHealth -= amount * damageAbsorb;
	}
	
	/** Set the percentage of damage absorbed. **/
	public void setDamageAbsorb(double damageAbsorb) {
		this.damageAbsorb = damageAbsorb;
	}
	
	/** Finish constructing the enemy. **/
	protected void initialize() {
		this.futureHealth = this.health;
	}
	
	private void enemyDestroy() {
		for (int i = 0; i < 30; i++) {
			control.addEntity(new EnemyJunk(this));
		}
		destroy();
	}
	
	
	
	// =============== ABSTRACT METHODS =============== //
	
	@Override
	public abstract void update();
	
	@Override
	public abstract void draw();
}
