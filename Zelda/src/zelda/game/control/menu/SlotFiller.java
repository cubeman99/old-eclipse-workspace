package zelda.game.control.menu;

import zelda.common.geometry.Point;


public interface SlotFiller {

	public abstract String getName();

	public abstract String getDescription();

	public abstract void drawSlot(Point pos);
}
