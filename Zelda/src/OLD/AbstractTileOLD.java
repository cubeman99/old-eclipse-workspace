package OLD;

import zelda.common.geometry.Point;
import zelda.game.control.GameInstance;
import zelda.game.world.Frame;


public abstract class AbstractTileOLD {
	protected Frame frame;
	protected Point position;



	// ================== CONSTRUCTORS ================== //

	public AbstractTileOLD(Frame frame, int x, int y) {
		this.frame = frame;
		this.position = new Point(x, y);
	}



	// =================== ACCESSORS =================== //

	public Frame getFrame() {
		return frame;
	}

	public Point getPosition() {
		return position;
	}

	public int getY() {
		return position.y;
	}

	public int getX() {
		return position.x;
	}

	public GameInstance getControl() {
		return frame.getLevel().getWorld().getControl();
	}



	// ==================== MUTATORS ==================== //



	// =============== ABSTRACT METHODS =============== //

	public abstract DataOLD getData();

	public abstract void onEnterFrame();

	public abstract void onLeaveFrame();

	public abstract void update();

	public abstract void draw(int dx, int dy);
}
