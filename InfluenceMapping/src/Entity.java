

import java.awt.Graphics;

public abstract class Entity {
	public int depth = 0;
	public boolean entityDestroyed = false;
	
	public Entity() {
	}
	
	public Entity(int depth) {
		this.depth = depth;
	}
	
	public void init() {
	}

	public void preUpdate() {
	}
	
	public void update() {
	}

	public void postUpdate() {
	}
	
	public void draw(Graphics g) {
	}
	
	public void destroy() {
		entityDestroyed = true;
		Game.entities.remove(this);
	}
}
