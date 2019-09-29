package projects.gravitySimulator;

import common.Vector;

public class TestCollision {
	public double time;
	public Mass c1;
	public Mass c2;
	
	
	public TestCollision(Mass c1, Mass c2, double time) {
		this.time = time;
		this.c1   = c1;
		this.c2   = c2;
	}
	
	public void apply() {
		
		//assign the position at percentage t along each velocity vector to each ghost
		c1.setPosition(c1.getPosition().plus(c1.velocity.scaledBy(time)));
		c2.setPosition(c2.getPosition().plus(c2.velocity.scaledBy(time)));
		
		//difference vector between circles at time of collision
		Vector cDiff = c2.getPosition().minus(c1.getPosition());
		//normalized
		Vector collisionNormal = cDiff.normalized();
		
		//relative velocities of circles
		Vector relativeVelocity = c1.velocity.minus(c2.velocity);
		//coefficient of restitution range of 0 - 1.0
		//0 is completely inelastic (lump of clay) and 1 is purely elastic (superball)
		double restitution = Math.sqrt(c1.elasticity * c2.elasticity);
		
		//the impulse created at collision- 
		//this is given by the following equation (j is used to represent impulse)
		//j = -( 1 + e )vAB . n / n . n( 1 / mA + 1 / mB )
		//where e is the coefficient of restitution, vAB is the relative velocities
		//n is the collision normal and mA/mB are the respective masses of the different circles
		// "."s represent the vector dot product
		double impulse = collisionNormal.dot(relativeVelocity.scaledBy(-(1 + restitution)));
		impulse /= collisionNormal.dot(collisionNormal.scaledBy(1 / c1.mass + 1 / c2.mass));
		
		//scale each impulse along the collision normal by the circles' respective masses
		Vector reactionA = collisionNormal.scaledBy(impulse / c1.mass);
		Vector reactionB = collisionNormal.scaledBy(-impulse / c2.mass);
		
		//Multiply this by the the remainder of the interval (1 - t)
		c1.velocity = c1.velocity.plus(reactionA).scaledBy(1 - time);
		c2.velocity = c2.velocity.plus(reactionB).scaledBy(1 - time);
		
		c2.collision = null;
		c2.collided  = true;
	}
}
