package game.nodes;

import game.Ring;
import common.GMath;
import common.Settings;
import common.graphics.Draw;
import common.graphics.Sprite;
import main.ImageLoader;


/**
 * Node.
 * 
 * @author David Jordan
 */
public abstract class Node extends Enemy {
	protected Sprite sprite;
	public Ring ring;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Construct a new node on the given ring with the given angle. **/
	public Node(Ring ring, double angle, int subImage) {
		super(ring.control, ring.getRadius(), angle);
		this.health     = Settings.NODE_HEALTH;
		this.damage     = Settings.NODE_DAMAGE;
		this.maskRadius = 22;
		
		this.sprite = ImageLoader.getSprite("nodes", subImage);
		this.ring   = ring;
		
		initialize();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	
	
	
	// ==================== MUTATORS ==================== //
	
	
	
	// ============== OVERRIDDEN METHODS ============== //

	@Override
	/** Return whether this node is dead. **/
	public double getRadius() {
		return ring.getRadius();
	}
	
	@Override
	/** Return whether this node is dead. **/
	public double getAngle() {
		return (ring.getAngle() + angle);
	}
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	/** Update the node. **/
	public void update() {}
	
	

	@Override
	/** Draw the node. **/
	public void draw() {
		Draw.draw(sprite, ring.getRadius(), ring.getAngle() + angle, ring.getAngle() + angle - GMath.HALF_PI);
	}
}
