package zelda.editor.gui;

import zelda.common.geometry.Point;
import zelda.common.util.Direction;


public class Bound {
	public Panel parent;
	public int dir;
	public BoundType type;
	public Panel panel;
	public int maxSize;
	public int minSize;
	public int size;
	public int position;

	public Bound(int dir) {
		this.dir = dir;
		this.type = BoundType.WINDOW;
		this.panel = null;
		this.size = 0;
		this.minSize = 0;
		this.maxSize = 0;
		this.position = 0;
	}

	public Bound(int dir, Panel panel) {
		this.dir = dir;
		this.type = BoundType.PANEL;
		this.panel = panel;
		this.size = 0;
		this.minSize = 0;
		this.maxSize = 0;
		this.position = 0;
	}

	public Bound(int dir, int size) {
		this.dir = dir;
		this.type = BoundType.FIXED;
		this.panel = null;
		this.size = size;
		this.minSize = 0;
		this.maxSize = 99999;
		this.position = 0;
	}

	public Bound(int dir, int size, int minSize, int maxSize) {
		this.dir = dir;
		this.type = BoundType.FIXED;
		this.panel = null;
		this.size = size;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.position = 0;
	}

	public void computePosition(Point windowSize) {
		if (type == BoundType.WINDOW) {
			if (dir == Direction.EAST)
				position = windowSize.x;
			else if (dir == Direction.NORTH)
				position = 0;
			else if (dir == Direction.WEST)
				position = 0;
			else if (dir == Direction.SOUTH)
				position = windowSize.y;
		}
		else if (type == BoundType.PANEL) {
			position = panel.getBoundPos((dir + 2) % Direction.NUM_DIRS);
		}
		else if (type == BoundType.FIXED) {
			position = parent.getBoundPos((dir + 2) % Direction.NUM_DIRS)
					+ size * (dir == 1 || dir == 2 ? -1 : 1);
		}
	}


	public enum BoundType {
		WINDOW, // Connects to the edge of the window.
		FIXED, // Is a fixed distance from the opposite edge.
		PANEL // Connects to the edge of a panel.
	}
}