package tp.planner.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import tp.common.Point;
import tp.common.graphics.Draw;
import tp.main.Mouse;
import tp.planner.ItemData;
import tp.planner.tile.PacketSign;
import tp.planner.tile.Tile;

public class ToolHand extends Tool {
	private boolean panning;
	private Point panPoint;
	private Point viewPositionPrev;
	
	public ToolHand() {
		super("Hand Tool", "q", KeyEvent.VK_Q);
		panning          = false;
		panPoint         = null;
		viewPositionPrev = null;
	}

	@Override
	public void update() {
		
		if (panning) {
			Point add = panPoint.minus(Mouse.position());
			control.setViewPosition(viewPositionPrev.plus(add));
			if (!Mouse.right.down()) {
				panning          = false;
				panPoint         = null;
				viewPositionPrev = null;
			}
		}
		else if (Mouse.right.pressed()) {
			panning          = true;
			panPoint         = Mouse.position();
			viewPositionPrev = new Point(control.getViewPosition());
		}
	}
	
	@Override
	public void draw(Graphics g) {
		Point ms = Mouse.position();
		Tile hoverTile = control.getHoverTile(control.world.objGrid);
		
		if (hoverTile != null) {
			if (hoverTile.getItem() == ItemData.SIGN) {
				// Show Sign text:
				PacketSign packet = (PacketSign) hoverTile.getPacket();
				
				Draw.configureText(Draw.FONT_ANDY, 20, Color.WHITE, true);
				Draw.drawStringShadowed("asdads sad", ms.x, ms.y, Draw.LEFT, Draw.MIDDLE, 1);
			}
		}
	}
}
