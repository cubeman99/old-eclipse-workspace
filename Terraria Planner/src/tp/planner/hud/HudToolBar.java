package tp.planner.hud;

import java.awt.Color;
import java.awt.Image;
import tp.common.Point;
import tp.common.graphics.Draw;
import tp.common.graphics.Sheet;
import tp.main.ImageLoader;
import tp.main.Mouse;
import tp.planner.ItemData;


public class HudToolBar extends HudPanel {
	private Image imageBar;
	private Sheet sheetTools;
	private int buttonSep;
	
	
	public HudToolBar(HUD hud) {
		super(hud);
		imageBar   = ImageLoader.getImage("hudToolPanelMid");
		sheetTools = new Sheet("hudToolIcons", 32);
		
		buttonSep  = 38;
		size.set(52, runner.getViewHeight() - position.y);
		position.set(0, hud.buttonBar.getLowerBound());
	}
	
	@Override
	public void update() {
		size.y = runner.getViewHeight() - position.y;
		
		// Draw tool buttons:
		for (int i = 0; i < control.getTools().length; i++) {
			Point dp = position.plus(size.x / 2, (i * buttonSep) + (size.x / 2));

			if (hud.mouseOverArea(dp.x, dp.y, buttonSep) && Mouse.left.pressed()) {
				control.setToolIndex(i);
				Mouse.setCursor(control.getTool().getCursor());
			}
		}
	}
	
	@Override
	public void draw() {
		// Bar panel image:
		hud.graphics.drawImage(imageBar, position.x, position.y, position.x
				+ size.x, position.y + size.y, 0, 0, size.x, 1, null);
		
		// Draw current tool/item label:
		int leftBound  = hud.toolBar.getRightBound();
		int rightBound = hud.itemPalette.getLeftBound();
		Point labelPos = new Point((leftBound + rightBound) / 2, runner.getViewHeight() - 32);
		String label   = control.getTool().getName();
		if (control.getToolIndex() == 0)
			label = ItemData.get(control.getItemIndex()).getName();
		
		Draw.configureText(Draw.FONT_ANDY, Color.BLACK, true);
		Draw.drawStringShadowed(label, labelPos.x, labelPos.y, Draw.CENTERED, Draw.MIDDLE);
		
		// Draw tool buttons:
		for (int i = 0; i < control.getTools().length; i++) {
			Point dp = position.plus(size.x / 2, (i * buttonSep) + (size.x / 2));
			
			if (hud.mouseOverArea(dp.x, dp.y, buttonSep)) {
				if (control.getToolIndex() != i)
					hud.drawButtonOverlay(dp.x, dp.y, false);
				hud.graphics.setColor(new Color(255, 255, 255, 200));
				hud.graphics.fillRect(dp.x - 16, dp.y - 16, 32, 32);
			}
			
			if (i == 0)
				hud.drawItemIconCentered(ItemData.get(control.getItemIndex()), dp.x, dp.y);
			else
				Draw.drawSpriteCentered(sheetTools, dp.x, dp.y, i, 0);
//				hud.drawIconCentered(ImageLoader.getImage("toolIcons"), i, dp.x, dp.y);
			
			if (control.getToolIndex() == i)
				hud.drawButtonOverlay(dp.x, dp.y, true);
		}
	}
}
