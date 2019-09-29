package tp.planner.tool;

import java.awt.event.KeyEvent;
import tp.main.Mouse;
import tp.planner.tile.Tile;

public class ToolEraser extends Tool {

	public ToolEraser() {
		super("Eraser Tool", "e", KeyEvent.VK_E);
		
	}

	@Override
	public void update() {
		Tile hoverTile = control.getHoverTile(control.world.objGrid);
		
		if (control.isMouseDown(Mouse.left) && hoverTile != null)
			hoverTile.removeSelfFromGrid();
		else if (hoverTile != null)
			control.setCursorBox(hoverTile.getRect());
	}
}
