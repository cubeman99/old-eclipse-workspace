package zelda.game.control;

import zelda.common.Settings;
import zelda.common.geometry.Vector;
import zelda.common.util.GMath;
import zelda.game.entity.EntityObject;
import zelda.game.player.Player;
import zelda.game.world.Frame;


public class View {
	private GameInstance game;
	private Vector position;
	private Vector viewSize;
	private Vector canvasSize;
	private double panSpeed;



	// ================== CONSTRUCTORS ================== //

	public View(GameInstance game) {
		this.game     = game;
		this.position = new Vector();
		this.viewSize = new Vector(Settings.VIEW_SIZE);
		this.panSpeed = 1;
		this.canvasSize = new Vector(100, 100);
	}



	// =================== ACCESSORS =================== //

	public Vector getPosition() {
		return position;
	}

	public Vector getPositionCenteredAt(Vector focus) {
		Vector v = focus.minus(viewSize.scaledByInv(2));
		v.x = Math.max(0, Math.min(canvasSize.x - viewSize.x, v.x));
		v.y = Math.max(0, Math.min(canvasSize.y - viewSize.y, v.y));
		return v;
	}

	public Vector getPositionCenteredAt(EntityObject obj) {
		return getPositionCenteredAt(obj.getCenter());
	}
	
	public boolean isCenteredAt(EntityObject obj) {
		return (getPositionCenteredAt(obj).distanceTo(position) < 4);
	}



	// ==================== MUTATORS ==================== //
	
	public void setViewSize(Vector viewSize) {
		this.viewSize.set(viewSize);
	}
	
	public void setCanvas(Frame frame) {
		canvasSize.set(frame.getSize().scaledBy(Settings.TS));
	}
	
	public boolean panTo(Vector focus) {
		Vector goal = getPositionCenteredAt(focus);
		
		for (int i = 0; i < Vector.NUM_COMPONENTS; i++) {
			double diff = goal.comp(i) - position.comp(i);
			if (Math.abs(diff) > panSpeed)
				position.setComp(i, position.comp(i) + Math.signum(diff) * panSpeed);
			else
				position.setComp(i, goal.comp(i));
		}
		
		clip();
		
		return (position.distanceTo(goal) < panSpeed);
		
	}

	/** Center the view at the given position. **/
	public void centerAt(Vector focus) {
		position.set(getPositionCenteredAt(focus));
		clip();
	}

	/** Center the view at the given position. **/
	public void centerAt(EntityObject obj) {
		centerAt(obj.getCenter());
	}

	/** Clip the view position to the canvas size. **/
	private void clip() {
		position.x = Math.max(0,
				Math.min(canvasSize.x - viewSize.x, position.x));
		position.y = Math.max(0,
				Math.min(canvasSize.y - viewSize.y, position.y));
	}
}
