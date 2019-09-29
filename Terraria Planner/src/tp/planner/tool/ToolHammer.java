package tp.planner.tool;

import java.awt.event.KeyEvent;
import tp.main.Mouse;
import tp.planner.tile.Tile;

public class ToolHammer extends Tool {

	public ToolHammer() {
		super("Hammer Tool", "h", KeyEvent.VK_H);
		
	}

	@Override
	public void update() {
		Tile hoverTile = control.getHoverTile(control.world.wallGrid);
		
		if (control.isMouseDown(Mouse.left) && hoverTile != null)
			hoverTile.removeSelfFromGrid();
		else if (hoverTile != null)
			control.setCursorBox(hoverTile.getRect());
		
		if (Mouse.right.pressed()) {
			Tile hoverTile2 = control.getHoverTile(control.world.objGrid, control.world.wallGrid);
			
			if (hoverTile2 != null) {
				if (hoverTile2.getItem().getWallObjectItem() != null) {
					control.setItemIndex(hoverTile2.getItem().getWallObjectItem().getIndex(), true);
					control.setToolIndex(0);
				}
			}
		}
	}
}
