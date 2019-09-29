package entity.unit;

import common.GMath;
import common.Vector;
import control.Control;
import control.Team;


public class Zombie extends Unit {
	public static final double RADIUS = 0.5;
	
	public double dirSpeed = 0;
	public double dirSpeedMax = 0.2;
	
	public Zombie(Control control, Team team, Vector position) {
		super(team, position, RADIUS);
		setMaxHealth(100);
	}

	@Override
	public void update() {
//		position.add(body.velocity);
		
		dirSpeed += 0.3 - GMath.random.nextFloat() * 0.6;
		dirSpeed -= GMath.random.nextFloat() * GMath.sign(dirSpeed) * (GMath.sqr(dirSpeed) / GMath.sqr(dirSpeedMax));
		dirSpeed = Math.max(-dirSpeedMax, GMath.min(dirSpeedMax, dirSpeed));;
        direction += dirSpeed;
        
        body.velocity.add(Vector.polarVector(0.01, direction));
        if (body.velocity.length() > maxSpeed)
        	body.velocity.setLength(maxSpeed);
        
//        checkCollisions();
	}

}
