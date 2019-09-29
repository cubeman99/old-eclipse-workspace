package game.entity;


/**
 * Entity.
 * 
 * @author David Jordan
 */
public abstract class Entity {
	protected int depth; // lower depths get drawn first.
	protected boolean destroyed;
	
	public Entity(int depth) {
		this.depth = depth;
		destroyed  = false;
	}
	
	
	public int getDepth() {
		return depth;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public void destroy() {
		destroyed = true;
	}
	
	public abstract void update();
	
	public abstract void draw();
}
