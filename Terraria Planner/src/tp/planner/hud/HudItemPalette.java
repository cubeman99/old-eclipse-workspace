package tp.planner.hud;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import tp.common.Point;
import tp.common.Rectangle;
import tp.common.graphics.Draw;
import tp.main.ImageLoader;
import tp.main.Mouse;
import tp.planner.Item;
import tp.planner.ItemGroup;
import tp.planner.ItemGroupData;


public class HudItemPalette extends HudPanel {
	private static final Color COLOR_GROUP_PANEL_BACKGROUND = new Color(227, 236, 243);
	private static final Color COLOR_GROUP_PANEL_SELECTED   = new Color(0, 0, 0, 30);
	private static final Color COLOR_SCROLL_BAR_BACKING     = new Color(124, 150, 177);
	private static final Color COLOR_ITEM_GRID_BACKGROUND   = new Color(227, 236, 243);
	private static final Color COLOR_ITEM_GRID_CHECKER1     = new Color(227, 236, 243);
	private static final Color COLOR_ITEM_GRID_CHECKER2     = new Color(206, 217, 228);
	private static final Color COLOR_ITEM_GRID_SELECTED     = new Color(204, 234, 210); // Light green
	private static final Color COLOR_HIGHLIGHT              = new Color(255, 255, 255, 128); // transparent white
	private static final int GRID_SCROLL_SPEED = 30;
	
	private Image imageGridBorder;
	private Image imageGroupPanelBottom;
	private Image imageGroupSelected;
	private int totalColumns;
	private int groupPanelUpperPadding = 10;
	private int groupPanelLowerPadding = 10;
	private int groupPanelHeight;
	private int gridSep;
	private int sidePadding;
	private int groupIndex;
	private boolean resizing;
	private int gridScrollY;
	
	
	
	public HudItemPalette(HUD hud) {
		super(hud);
		imageGridBorder       = ImageLoader.loadImage("hudItemPanelGridBorder.png");
		imageGroupPanelBottom = ImageLoader.loadImage("hudItemPanelGroupBottom.png");
		imageGroupSelected    = ImageLoader.loadImage("hudItemPanelGroupSelected.png");
		
		resizing               = false;
		groupIndex             = 0;
		groupPanelUpperPadding = 32;
		groupPanelLowerPadding = 10;
		gridSep                = 36;
		totalColumns           = 6;
		groupPanelHeight       = 0;
		sidePadding            = imageGridBorder.getWidth(null);
		gridScrollY            = 16;
	}
	
	public boolean hasScrollPriority() {
		return Mouse.inArea(getGridRect());
	}
	
	private Rectangle getGridRect() {
		return new Rectangle(position.x, position.y + groupPanelHeight, size.x, size.y - groupPanelHeight);
	}
	
	private int getGridRows() {
		int total = ItemGroupData.get(groupIndex).getSize();
		int rows = total / totalColumns + 1;
		if (total % totalColumns == 0)
			rows--;
		return rows;
	}
	
	private Rectangle getScrollBarRect() {
		if (getMaxScrollDist() <= 0)
			return null;
		
		double a   = Math.max(0.0, (double) getGridRect().getHeight() / (double) (getGridRows() * gridSep));
		int h      = (int) (a * (double) getGridRect().getHeight());
		double a2  = (double) gridScrollY / (double) getMaxScrollDist();
		int offset = (int) (a2 * (double) (getGridRect().getHeight() - h));
		
		return new Rectangle(getRightBound() - sidePadding, position.y + groupPanelHeight + offset, sidePadding, h);
	}
	
	private int getMaxScrollDist() {
		return Math.max(0, (getGridRows() * gridSep) - getGridRect().getHeight());
	}

	public void openToItem(Item item) {
		for (int i = -1; i < ItemGroupData.getTotalGroups(); i++) {
			ItemGroup group = ItemGroupData.get(i < 0 ? groupIndex : i);
			
			int ind = group.findItem(item);
			if (ind >= 0) {
				if (i >= 0)
					groupIndex = i;
				return;
			}
		}
	}

	/** Draw the group panel for selecting categories of items. **/
	private void drawGroupPanel() {
		// Background color:
		hud.graphics.setColor(COLOR_GROUP_PANEL_BACKGROUND);
		hud.graphics.fillRect(position.x, position.y, size.x, groupPanelHeight);
		
		// Bottom gradient border:
		hud.graphics.drawImage(imageGroupPanelBottom, position.x, position.y
				+ groupPanelHeight - imageGroupPanelBottom.getHeight(null),
				getRightBound(), position.y + groupPanelHeight, 0, 0, 1, 52, null);

		// Left side bar:
		Rectangle r = getGridRect();
		hud.graphics.drawImage(imageGridBorder, r.getX1(), r.getY1(), r.getX1()
				+ sidePadding, r.getY2(), 0, 0, sidePadding, 1, null);

		// Side edge lines:
		hud.graphics.drawImage(imageGridBorder, position.x, position.y,
				position.x + 2, position.y + size.y, 0, 0, 2, 1, null);
		hud.graphics.drawImage(imageGridBorder, position.x + size.x - 2,
				position.y, position.x + size.x, position.y + size.y,
				sidePadding - 2, 0, sidePadding, 1, null);

		// Scroll Bar:
		Rectangle scrollBar = getScrollBarRect();
		if (scrollBar != null) {
			hud.graphics.setColor(COLOR_SCROLL_BAR_BACKING);
			hud.graphics.fillRect(r.getX2() - sidePadding, r.getY1(), sidePadding - 2, r.getHeight());
			hud.graphics.drawImage(imageGridBorder, scrollBar.getX1(),
					scrollBar.getY1(), scrollBar.getX2(), scrollBar.getY2(), 0,
					0, imageGridBorder.getWidth(null),
					imageGridBorder.getHeight(null), null);
		}
		else {
			hud.graphics.drawImage(imageGridBorder, r.getX2() - sidePadding, r.getY1(),
					r.getX2(), r.getY2(), 0, 0, sidePadding, 1, null);
		}
		
		// Group icons:
		int index = 0;
		for (int y = 0; index < ItemGroupData.getTotalGroups(); y++) {
			for (int x = 0; x < totalColumns && index < ItemGroupData.getTotalGroups(); x++) {
				Point p = new Point(position.x + sidePadding + (x * gridSep),
						position.y + groupPanelUpperPadding + (y * gridSep));
				Point center     = p.plus(gridSep / 2, gridSep / 2);
				boolean drawBack = false;

				if (groupIndex == index) {
					hud.graphics.setColor(COLOR_GROUP_PANEL_SELECTED);
					drawBack = true;
				}
				if (hud.mouseOverArea(center.x, center.y, gridSep)) {
					hud.graphics.setColor(COLOR_HIGHLIGHT);
					drawBack = true;
					
					if (!resizing && Mouse.left.pressed()) {
						groupIndex  = index;
						gridScrollY = 0;
					}
				}
				
				if (drawBack)
					hud.graphics.fillRect(p.x, p.y, gridSep, gridSep);
				if (groupIndex == index)
					hud.graphics.drawImage(imageGroupSelected, p.x, p.y, null);
				
				hud.drawIconCentered(ImageLoader.getImage("itemGroupIcons"), index, center.x, center.y);
						
				index++;
			}
		}
		
		// Group Text:
		String groupName = ItemGroupData.get(groupIndex).getName();
		Draw.configureText(Draw.FONT_NORMAL, Color.BLACK, true);
		Draw.drawString(groupName, position.x + (size.x / 2), position.y + (groupPanelUpperPadding / 2), Draw.CENTERED, Draw.MIDDLE);
	}
	
	/** Draw the item grid for selecting items. **/
	private void drawItemGrid() {
		ItemGroup group = ItemGroupData.get(groupIndex);
		
		// Draw grid background:
		hud.graphics.setColor(COLOR_ITEM_GRID_BACKGROUND);
		hud.graphics.fillRect(position.x, position.y, size.x, size.y);
		
		// Draw the grid:
		int index = 0;
		for (int y = 0; index < group.getSize(); y++) {
			for (int x = 0; x < totalColumns && index < group.getSize(); x++) {
				Point p = new Point(position.x + sidePadding + (x * gridSep),
						position.y + groupPanelHeight + (y * gridSep) - gridScrollY);
				Point center = p.plus(gridSep / 2, gridSep / 2);

				hud.graphics.setColor((x + y) % 2 == 0 ? COLOR_ITEM_GRID_CHECKER1 : COLOR_ITEM_GRID_CHECKER2);
				if (control.getItemIndex() == group.getItem(index).getIndex())
					hud.graphics.setColor(COLOR_ITEM_GRID_SELECTED);
				if (hud.mouseOverArea(center.x, center.y, gridSep)) {
					hud.graphics.setColor(COLOR_HIGHLIGHT);

					if (!resizing && Mouse.left.pressed()) {
						control.setItemIndex(group.getItem(index).getIndex());
						control.setToolIndex(0);
						hud.hotBar.setIndex(-1);
						hud.startDraggingItem(group.getItem(index));
					}
				}
				hud.graphics.fillRect(p.x, p.y, gridSep, gridSep);

				hud.drawItemIconCentered(group.getItem(index), center.x, center.y);

				if (control.getItemIndex() == group.getItem(index).getIndex())
					hud.graphics.drawImage(imageGroupSelected, p.x, p.y, null);

				index++;
			}
		}
	}
	
	@Override
	public boolean isBusy() {
		return resizing;
	}
	
	@Override
	public void update() {
		size.set((gridSep * totalColumns) + (2 * sidePadding), runner.getViewHeight() - hud.buttonBar.getLowerBound());
		position.set(runner.getViewWidth() - size.x, hud.buttonBar.getLowerBound());
		int ySpan = ItemGroupData.getTotalGroups() / totalColumns + 1;
		if (ItemGroupData.getTotalGroups() % totalColumns == 0)
			ySpan--;
		groupPanelHeight  = groupPanelUpperPadding + (gridSep * ySpan) + groupPanelLowerPadding;
		
		int dist = Math.abs(position.x + (sidePadding / 2) - Mouse.x());
		if (dist <= sidePadding / 2) {
			Mouse.setCursor(Cursor.E_RESIZE_CURSOR);
			if (Mouse.left.pressed()) {
				resizing = true;
			}
		}
		
		if (resizing) {
			Mouse.setCursor(Cursor.E_RESIZE_CURSOR);
			totalColumns = (int) Math.round((double) (runner.getViewWidth() - Mouse.x() - (2 * sidePadding)) / (double) gridSep);
			totalColumns = Math.min(ItemGroupData.getTotalGroups(), Math.max(4, totalColumns));
			if (!Mouse.left.down())
				resizing = false;
		}

		int maxScroll = getMaxScrollDist();
		
		if (Mouse.inArea(getGridRect())) {
			if (Mouse.wheelDown())
				gridScrollY += GRID_SCROLL_SPEED;
			if (Mouse.wheelUp())
				gridScrollY -= GRID_SCROLL_SPEED;
		}
		gridScrollY = Math.max(0, Math.min(maxScroll, gridScrollY));
	}
	
	@Override
	public void draw() {
		drawItemGrid();
		drawGroupPanel();
	}
}
