package zelda.game.player;

import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.common.util.Grid;
import zelda.game.player.items.Item;


/**
 * Inventory.
 * 
 * @author David Jordan
 */
public class Inventory {
	public static final int MAX_EQUIPPED_ITEMS = 2;

	private ArrayList<Item> items;
	private Grid<Item> itemGrid;
	private Point itemGridSize;
	private Item[] equippedItems;



	// ================== CONSTRUCTORS ================== //


	public Inventory() {
		itemGridSize = new Point(4, 4);

		items = new ArrayList<Item>();
		itemGrid = new Grid<Item>(itemGridSize);
		equippedItems = new Item[MAX_EQUIPPED_ITEMS];
	}



	// =================== ACCESSORS =================== //

	public int getNumItems() {
		return items.size();
	}

	public boolean hasItem(Item item) {
		return (items.contains(item) && item.isObtained());
	}

	public Item getItem(int index) {
		return items.get(index);
	}

	public Item getEquippedItem(int actionSlot) {
		return equippedItems[actionSlot];
	}

	public Grid<Item> getItemGrid() {
		return itemGrid;
	}

	public boolean isEquippedItemTwoHanded() {
		if (equippedItems[0] != null)
			return equippedItems[0].isTwoHanded();
		return false;
	}

	public boolean isItemEquipped(Item item) {
		for (int i = 0; i < MAX_EQUIPPED_ITEMS; i++) {
			if (equippedItems[i] == item)
				return true;
		}
		return false;
	}

	public int getEquippedSlot(Item item) {
		for (int i = 0; i < MAX_EQUIPPED_ITEMS; i++) {
			if (equippedItems[i] == item)
				return i;
		}
		return -1;
	}

	private Point getEmptyGridSlot() {
		return getItemGridSlot(null);
	}

	private Point getItemGridSlot(Item item) {
		for (int y = 0; y < itemGridSize.y; y++) {
			for (int x = 0; x < itemGridSize.x; x++) {
				if (itemGrid.get(x, y) == item)
					return new Point(x, y);
			}
		}
		return null;
	}



	// ==================== MUTATORS ==================== //

	public void equipItem(int actionSlot, Item item) {
		equipItem(actionSlot, getItemGridSlot(item));
	}

	public void equipItem(int actionSlot, Point gridLocation) {
		if (gridLocation != null) {
			Item item = itemGrid.get(gridLocation);

			if (isEquippedItemTwoHanded())
				equippedItems[1 - actionSlot] = null;

			itemGrid.set(gridLocation, equippedItems[actionSlot]);
			if (equippedItems[actionSlot] != null)
				equippedItems[actionSlot].onEnd();

			if (item != null && item.isTwoHanded()) {
				itemGrid.set(getEmptyGridSlot(), equippedItems[1 - actionSlot]);
				if (equippedItems[1 - actionSlot] != null)
					equippedItems[1 - actionSlot].onEnd();
				equippedItems[1 - actionSlot] = item;
			}
			equippedItems[actionSlot] = item;

			if (item != null)
				item.onStart();
		}
	}
	
	public Inventory addItem(Item item) {
		items.add(item);
		return this;
	}

	public void interruptItems() {
		for (int i = 0; i < items.size(); i++) {
			if (hasItem(items.get(i)))
				items.get(i).interrupt();
		}
	}

	public void updateItems() {
		for (int i = 0; i < items.size(); i++) {
			if (hasItem(items.get(i)))
				items.get(i).update();
		}
	}

	public void drawItemsUnder() {
		for (int i = 0; i < items.size(); i++) {
			if (hasItem(items.get(i)))
				items.get(i).drawUnder();
		}
	}

	public void drawItemsOver() {
		for (int i = 0; i < items.size(); i++) {
			if (hasItem(items.get(i)))
				items.get(i).drawOver();
		}
	}

	public void obtainItem(Item item) {
		if (!item.isObtained()) {
			item.setObtained(true);

			if (!items.contains(item))
				items.add(item);

			if (equippedItems[0] == null)
				equippedItems[0] = item;
			else if (equippedItems[1] == null)
				equippedItems[1] = item;
			else {
				Point p = getEmptyGridSlot();
				if (p != null)
					itemGrid.set(p, item);
			}
		}
	}

	public void obtainAllItems() {
		for (int i = 0; i < items.size(); i++)
			obtainItem(items.get(i));
	}
}
