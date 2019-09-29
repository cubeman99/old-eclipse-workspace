package flow.game;

import flow.common.GMath;
import flow.common.Vector;

public class Dot {
	public Control control;
	public Vector position;
	public Vector velocity;
	
	public Dot(Control control, Vector position) {
		this.position = new Vector(position);
		this.control  = control;
		this.velocity = new Vector(GMath.random.nextDouble() * 4.0, GMath.random.nextDouble() * 4.0);
	}
	
	public void update() {
		/*
		for (Dot dot : control.dots) {
			if (dot != this)
				applyGravity(dot.position);
		}
		*/
		position.add(velocity);
		
		
		while (position.x >= control.size.x)
			position.x -= control.size.x;
		while (position.y >= control.size.y)
			position.y -= control.size.y;
		while (position.x < 0)
			position.x += control.size.x;
		while (position.y < 0)
			position.y += control.size.y;
	}
	
	public void applyGravity(Vector v) {
		Vector disp = v.minus(position);
//		v.x = GMath.sqrt(v.x);
//		v.y = GMath.sqrt(v.y)
//		v.x = Math.signum(v.x) * Math.sqrt(Math.abs(v.x));
//		v.y = Math.signum(v.y) * Math.sqrt(Math.abs(v.y));
		velocity.add(disp.scaledByInv(10000));
	}
}
