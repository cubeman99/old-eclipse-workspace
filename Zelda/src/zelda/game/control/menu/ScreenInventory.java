package zelda.game.control.menu;

import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.util.Grid;
import zelda.game.player.items.Item;
import zelda.main.Keyboard;


public class ScreenInventory extends MenuScreen {
	private InventorySubScreen subscreen;
	private Slot[][] slotGrid;



	// ================== CONSTRUCTORS ================== //

	public ScreenInventory(Menu menu, int index) {
		super(menu, index);

		Point size = new Point(24, 16);
		slotGrid = createGridGroup(4, 4, 24, 8, 32, 24, size);

		subscreen = new InventorySubScreen(this);
	}



	// =================== ACCESSORS =================== //

	private Point getSlotGridLocation() {
		for (int x = 0; x < slotGrid.length; x++) {
			for (int y = 0; y < slotGrid[0].length; y++) {
				if (slotGrid[x][y] == getCurrentSlot())
					return new Point(x, y);
			}
		}
		return null;
	}



	// ==================== MUTATORS ==================== //

	public void equipItem(int actionSlot) {
		menu.getGame().getPlayer().getInventory()
				.equipItem(actionSlot, getSlotGridLocation());
	}

	private void checkEquipKeys() {
		for (int i = 0; i < Keyboard.NUM_ACTION_KEYS; i++) {
			if (Keyboard.actions[i].pressed()) {
				Item item = (Item) getCurrentSlot().getFiller();
				
				if (item != null && item.getNumTypes() > 1)
					subscreen.open(item, i);
				else {
					Sounds.SCREEN_SELECT.play();
					equipItem(i);
				}
			}
		}
	}

	private void syncItemGrid() {
		Grid<Item> itemGrid = menu.getGame().getPlayer().getInventory()
				.getItemGrid();

		for (int x = 0; x < itemGrid.getWidth(); x++) {
			for (int y = 0; y < itemGrid.getHeight(); y++) {
				slotGrid[x][y].setFiller(itemGrid.get(x, y));
			}
		}
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update() {
		super.update();
		syncItemGrid();

		if (subscreen.isOpen()) {
			subscreen.update();
		}
		else {
			checkEquipKeys();
		}
	}

	@Override
	public boolean canClose() {
		return !subscreen.isOpen();
	}

	@Override
	protected void updateSlotTraversal() {
		if (!subscreen.isOpen())
			super.updateSlotTraversal();
	}

	@Override
	protected void drawSlotCursor() {
		if (!subscreen.isOpen())
			super.drawSlotCursor();
	}

	@Override
	public void draw() {
		super.draw();
		syncItemGrid();
		subscreen.draw();
	}
}
