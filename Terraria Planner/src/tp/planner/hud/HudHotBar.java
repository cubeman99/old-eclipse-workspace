package tp.planner.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import tp.common.Point;
import tp.common.graphics.Draw;
import tp.main.ImageLoader;
import tp.main.Mouse;
import tp.main.Keyboard.Key;
import tp.planner.Item;
import tp.planner.ItemData;


public class HudHotBar extends HudPanel {
	private static final int SLOT_SIZE = 42;
	private int visibleSlotCount;
	private int barSize;
	private Slot[] slots;
	private int barIndex;
	private int topPadding;
	
	
	// ================== CONSTRUCTOR ================== //
	
	public HudHotBar(HUD hud) {
		super(hud);
		barSize          = 12;
		slots            = new Slot[barSize];
		barIndex         = 5;
		visibleSlotCount = barSize;
		topPadding       = 4;
		
		setSlot(0,  "Dirt Block",			"1", KeyEvent.VK_1);
		setSlot(1,  "Grass", 				"2", KeyEvent.VK_2);
		setSlot(2,  "Stone Block", 			"3", KeyEvent.VK_3);
		setSlot(3,  "Wood", 				"4", KeyEvent.VK_4);
		setSlot(4,  "Wood Wall", 			"5", KeyEvent.VK_5);
		setSlot(5,  "Torch", 				"6", KeyEvent.VK_6);
		setSlot(6,  "Wood Platform", 		"7", KeyEvent.VK_7);
		setSlot(7,  "Wooden Door Closed",	"8", KeyEvent.VK_8);
		setSlot(8,  "Wooden Table",			"9", KeyEvent.VK_9);
		setSlot(9,  "Wooden Chair",			"0", KeyEvent.VK_0);
		setSlot(10, "Furnace",				"-", KeyEvent.VK_MINUS);
		setSlot(11, "Work Bench", 			"=", KeyEvent.VK_EQUALS);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public int getIndex() {
		return barIndex;
	}
	
	public Item getBarItem(int index) {
		return slots[index].item;
	}
	
	public int getBarSize() {
		return barSize;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void setIndex(int index) {
		this.barIndex = index;
	}
	
	private void setSlot(int index, String itemName, String hotKeyText, int hotKeyCode) {
		slots[index] = new Slot();
		slots[index].item       = ItemData.find(itemName);
		slots[index].hotKey     = new Key(hotKeyCode);
		slots[index].hotKeyText = hotKeyText;
	}
	
	private void drawItemSlot(Item i, String text, int dx, int dy) {
		hud.graphics.drawImage(ImageLoader.getImage("slotBackingBlue"), dx - (SLOT_SIZE / 2), dy - (SLOT_SIZE / 2), null);
		hud.drawItemIconCentered(i, dx, dy);
	}
	
	private void drawItemSlotSelection(int dx, int dy, boolean hovering) {
		int s2 = (SLOT_SIZE / 2);
		hud.graphics.drawImage(ImageLoader.getImage("slotBackingSelect"), dx - s2, dy - s2, dx + s2,
				dy + s2, hovering ? SLOT_SIZE : 0, 0, hovering ? SLOT_SIZE * 2 : SLOT_SIZE, SLOT_SIZE, null);
	}
	
	@Override
	public void update() {
		// Calculate position and size:
		int leftBound    = hud.toolBar.getRightBound();
		int rightBound   = hud.itemPalette.getLeftBound();
		visibleSlotCount = Math.max(0, Math.min(barSize, (rightBound - leftBound) / SLOT_SIZE));
		int barWidth        = (visibleSlotCount * (SLOT_SIZE));
		position.set((leftBound + rightBound - barWidth) / 2, hud.buttonBar.getLowerBound());
		size.set(barWidth, SLOT_SIZE + topPadding);
		
		// Scroll wheel to change slot:
		if (control.getToolIndex() == 0 && barIndex >= 0 && !hud.itemPalette.hasScrollPriority()) {
    		if (Mouse.wheelDown()) {
    			barIndex++;
    			if (barIndex >= barSize)
    				barIndex = 0;
    			control.setItemIndex(slots[barIndex].item.getIndex());
    		}
    		if (Mouse.wheelUp()) {
    			barIndex--;
    			if (barIndex < 0)
    				barIndex = barSize - 1;
    			control.setItemIndex(slots[barIndex].item.getIndex());
    		}
		}
		
		// Clicking on slots:
		for (int i = 0; i < barSize; i++) {
			Point dp = position.plus((SLOT_SIZE / 2) + (i * (SLOT_SIZE)), topPadding + (SLOT_SIZE / 2));
			
			if (slots[i].hotKey.pressed()) {
				barIndex = i;
				control.setItemIndex(slots[i].item.getIndex());
				control.setToolIndex(0);
			}
			
			if (hud.mouseOverArea(dp.x, dp.y, SLOT_SIZE)) {

    			if (hud.isDroppingDragItem())
    				slots[i].item = hud.getDragItem();

				if (Mouse.left.pressed()) {
					barIndex = i;
					control.setItemIndex(slots[i].item.getIndex());
					control.setToolIndex(0);
				}
			}
		}
	}
	
	@Override
	public void draw() {
		for (int i = 0; i < visibleSlotCount; i++) {
			Point dp = position.plus((SLOT_SIZE / 2) + (i * (SLOT_SIZE)), topPadding + (SLOT_SIZE / 2));
			drawItemSlot(slots[i].item, "", dp.x, dp.y);

			if (barIndex == i && control.getToolIndex() == 0)
				drawItemSlotSelection(dp.x, dp.y, false);
			else if (hud.mouseOverArea(dp.x, dp.y, SLOT_SIZE))
				hud.drawItemSlotSelection(dp.x, dp.y, true);
			
//			String keyStr = KeyEvent.getKeyText();
			Draw.configureText(Draw.FONT_ANDY.deriveFont(Font.PLAIN, 18), Color.WHITE, true);
			Draw.drawString(slots[i].hotKeyText, dp.x - 15, dp.y + 15, Draw.LEFT, Draw.BOTTOM);
		}
	}
	
	private class Slot {
		public Item item;
		public String hotKeyText;
		public Key hotKey;
	}
}
