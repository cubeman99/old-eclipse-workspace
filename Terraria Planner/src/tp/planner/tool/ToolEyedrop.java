package tp.planner.tool;

import java.awt.event.KeyEvent;
import tp.main.Mouse;
import tp.planner.tile.Tile;

public class ToolEyedrop extends Tool {

	public ToolEyedrop() {
		super("Eyedrop Tool", "r", KeyEvent.VK_R);
		
	}

	@Override
	public void update() {
		Tile hoverTile = control.getHoverTile(control.world.objGrid, control.world.wallGrid, control.world.liquidGrid);
		
		if (control.isMouseDown(Mouse.left) && hoverTile != null) {
			control.setItemIndex(hoverTile.getItem().getIndex(), true);
			control.setToolIndex(0);
		}
		else if (hoverTile != null)
			control.setCursorBox(hoverTile.getRect());
	}
}
