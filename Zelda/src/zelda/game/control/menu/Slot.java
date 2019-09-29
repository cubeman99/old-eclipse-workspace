package zelda.game.control.menu;

import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.util.Direction;


public class Slot implements SlotConnection {
	private SlotConnection[] connections;
	private Rectangle rect;
	private SlotGroup group;
	private SlotFiller filler;



	// ================== CONSTRUCTORS ================== //

	public Slot(SlotGroup group, int x, int y, Point size) {
		this.group = group;
		this.rect = new Rectangle(x, y, size.x, size.y);
		this.connections = new SlotConnection[Direction.NUM_DIRS];
		this.filler = null;
	}



	// =================== ACCESSORS =================== //

	public SlotFiller getFiller() {
		return filler;
	}

	public SlotGroup getGroup() {
		return group;
	}

	public Point getPosition() {
		return rect.corner;
	}

	public Point getSize() {
		return rect.size;
	}

	public Rectangle getRect() {
		return rect;
	}

	public SlotConnection getConnection(int dir) {
		return connections[dir];
	}



	// ==================== MUTATORS ==================== //

	public void setFiller(SlotFiller filler) {
		this.filler = filler;
	}

	public SlotGroup selectInGroup() {
		group.setCurrentSlot(this);
		return group;
	}

	public void setConnection(int dir, SlotConnection connection) {
		connections[dir] = connection;
	}

	public void setConnections(SlotConnection[] connections) {
		for (int i = 0; i < Direction.NUM_DIRS; i++)
			this.connections[i] = connections[i];
	}

	public void draw() {
		if (filler != null)
			filler.drawSlot(rect.corner);
	}
}
