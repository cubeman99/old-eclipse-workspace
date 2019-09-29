package game;

import game.entity.lasers.Laser;
import game.entity.lasers.LaserHoming;
import game.entity.lasers.LaserNormal;
import game.entity.lasers.LaserSuper;
import game.entity.lasers.Missile;
import main.ImageLoader;
import main.Keyboard;
import common.GMath;
import common.Settings;
import common.Timer;
import common.graphics.Draw;


/**
 * Represents the controllable player ship.
 * 
 * @author David Jordan
 */
public class Player {
	public Control control;
	public double angle;
	private Timer shootTimer;
	private double angleSpeed;
	private int chargeBlastCount;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Construct the player under the given Control. **/
	public Player(Control control) {
		this.control          = control;
		this.angle            = 0;
		this.shootTimer       = new Timer(Settings.LASER_FIRE_DELAY);
		this.angleSpeed       = 0;
		this.chargeBlastCount = 0;
		
		this.shootTimer.start();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the amount of charge blasts the player has in stock. **/
	public int getChargeBlastCount() {
		return chargeBlastCount;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Update the player movement and etc. **/
	public void update() {
		
		if (Keyboard.up.down() && shootTimer.isExpired()) {
			fireLaser(new LaserHoming(control));
		}
		
		if (Keyboard.space.down() && shootTimer.isExpired()) {
			fireLaser(0);
			
			if (control.triLasers) {
				fireLaser( 0.04);
				fireLaser(-0.04);
			}
			
			shootTimer.start();
			// TODO: 3 lasers
		}
		
		if (Keyboard.down.pressed()) {
			fireMissile();
		}
		
		double amove = 0;
		
		if (Keyboard.left.down())
			amove += 1;
		if (Keyboard.right.down())
			amove -= 1;
		
		if (amove != 0) {
			angleSpeed += GMath.toRadians(amove * 0.3 * (GMath.sign(angleSpeed) == GMath.sign(amove) ? 1 : 2));
			angleSpeed  = Math.min(Settings.PLAYER_MOVE_SPEED, angleSpeed);
			angleSpeed  = Math.max(-Settings.PLAYER_MOVE_SPEED, angleSpeed);
		}
		else
			angleSpeed *= 0.81;
		
		angle += angleSpeed;
	}
	
	/** Fire the given laser from the player's ship. **/
	public void fireMissile() {
		control.addEntity(new Missile(control));
	}
	
	/** Give a charge blast to the user's stock. **/
	public void giveChargeBlast() {
		chargeBlastCount++;
	}
	
	/** Fire the given laser from the player's ship. **/
	public void fireLaser(Laser laser) {
		control.addEntity(laser);
		laser.configure(control, 78, angle);
	}
	
	/** Fire the given laser from the player's ship. **/
	public void fireLaser(double angleOffset) {
		Laser laser = (control.superLasers ? new LaserSuper() : new LaserNormal());
		control.addEntity(laser);
		laser.configure(control, 78, angle + angleOffset);
	}
	
	/** Draw the player ship. **/ 
	public void draw() {
		Draw.setAngle(angle - GMath.HALF_PI);
		
		Draw.drawImage(ImageLoader.getImage("playerShip"), 78, angle, angle, 14, 14, 1);
	}
}
