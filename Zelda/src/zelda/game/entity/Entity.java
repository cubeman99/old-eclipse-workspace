package zelda.game.entity;

import zelda.game.control.GameInstance;
import zelda.game.world.Frame;
import zelda.common.util.Cloneable;

public abstract class Entity implements Comparable<Entity>, Cloneable {
	protected GameInstance game;
	protected Frame frame;
	protected boolean hidden;
	private boolean destroyed;
	private int depth;


	// ================== CONSTRUCTORS ================== //

	public Entity() {
		this(null);
	}
	
	public Entity(GameInstance game) {
		this.destroyed = false;
		this.hidden    = false;
		this.depth     = 0;
		this.game      = game;
		this.frame     = null;
	}



	// ========= ABSTRACT/UNIMPLEMENTED METHODS ========= //

	public abstract void update();

	public abstract void draw();

	public void preUpdate() {}

	public void postUpdate() {}

	public void preDraw() {}

	public void postDraw() {}

	public void onDestroy() {}



	// =================== ACCESSORS =================== //

	public boolean isHidden() {
		return hidden;
	}

	public GameInstance getGame() {
		return game;
	}

	public Frame getFrame() {
		return frame;
	}

	public int getDepth() {
		return depth;
	}

	public final boolean isDestroyed() {
		return destroyed;
	}



	// ==================== MUTATORS ==================== //

	public final void destroy() {
		destroyed = true;
		onDestroy();
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setGame(GameInstance game) {
		this.game = game;
	}

	public void setFrame(Frame frame) {
		this.frame = frame;
	}



	// =============== INHERITED METHODS =============== //

	@Override
	public int compareTo(Entity e) {
		return new Integer(depth).compareTo(e.depth);
	}
	
	@Override
	public Entity clone() {
		return null;
	}
}
