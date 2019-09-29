package tp.planner.tool;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import tp.common.Point;
import tp.common.Rectangle;
import tp.common.graphics.Draw;
import tp.main.Mouse;
import tp.planner.Item;
import tp.planner.ItemData;
import tp.planner.tile.Tile;

public class ToolBrush extends Tool {

	public ToolBrush() {
		super("Brush Tool", "b", KeyEvent.VK_B);
		
	}

	@Override
	public void update() {
		Item item = ItemData.get(control.getItemIndex());
		Tile hoverTile = control.getHoverTile(item.getGrid(control));
		boolean placed = false;
		
		if (control.isMouseDown(Mouse.left)) 
			placed = item.getGrid(control).put(control.getCursorPoint(), item, control.getReplaceMode());
		
		if (Mouse.left.pressed() && hoverTile != null && !placed && hoverTile.getItem() == item && hoverTile.getGrid().getGridIndex() == Item.TYPE_OBJECT) {
			Tile t = control.getHoverTile(control.world.objGrid);
			
			if (control.getCursorPoint().equals(t.getPosition())) {
				if (t.getItem().isFlippable())
					t.setRandomSubimage((t.getRandomSubimage() + 1) % 2);
				else if (t.getItem().getRandomSubimageCount() > 0)
					t.setRandomSubimage((t.getRandomSubimage() + 1) % t.getItem().getRandomSubimageCount());
				
				Rectangle r = t.getAbsoluteRect();
				control.world.bufferArea(r.getX1(), r.getY1(), r.getWidth(), r.getHeight());
			}
		}
		
		if (control.isMouseDown(Mouse.right) && hoverTile != null)
			hoverTile.removeSelfFromGrid();
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		Rectangle r  = control.getCursorBox();
		if (r == null)
			return;
		if (!control.getHUD().isBusy())
			Draw.drawItem(ItemData.get(control.getItemIndex()), r.getCorner().x, r.getCorner().y, 0.5f);
	}
}
