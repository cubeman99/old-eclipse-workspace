package menu;

import java.awt.Graphics2D;

import main.Keyboard;

import game.GameInstance;
import game.Inventory;
import geometry.Vector;

/**
 * The menu used to switch out the player's weapons.
 * @author	Robert Jordan
 */
public class KeyItemMenu extends InventoryMenu {

	// ====================== Constants =======================
	
	// ======================= Members ========================
	
	// ===================== Constructors =====================
	
	/** Constructs the default weapon menu. */
	public KeyItemMenu() {
		super("menu_key_items", "menu_key_items_" + (useAdvancedMenus ? "b" : "a"), Inventory.SLOT_EQUIPMENT);
	}
	/** Initializes the menu and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the menu's state. */
	public void update() {
		super.update();
		
		updateMenuNavigation();
		updateMenuActions();
	}
	/** Called every step to update the normal menu. */
	private void updateMenuNavigation() {
		// Move the cursor
		if (Keyboard.left.pressed()) {
			index--;
			if (slot == 3 && index < 0) {
				slot = 2;
				index = 2;
			}
			else if (slot == 2 && index < 0) {
				slot = 3;
				index = 14;
			}
			displayItemDescription();
		}
		if (Keyboard.right.pressed()) {
			index++;
			if (slot == 3 && index >= 15) {
				slot = 2;
				index = 0;
			}
			else if (slot == 2 && index >= 3) {
				slot = 3;
				index = 0;
			}
			displayItemDescription();
		}
		if (Keyboard.up.pressed()) {
			if (slot == 3) {
				if (index / 5 == 0) {
					if (index <= 2) {
						slot = 2;
					}
					else {
						index += 10;
					}
				}
				else {
					index -= 5;
				}
			}
			else {
				slot = 3;
				index += 10;
			}
			displayItemDescription();
		}
		if (Keyboard.down.pressed()) {
			if (slot == 3) {
				if (index / 5 == 2) {
					index %= 5;
					if (index <= 2) {
						slot = 2;
					}
				}
				else {
					index += 5;
				}
			}
			else {
				slot = 3;
			}
			displayItemDescription();
		}
	}
	/** Updates the menu actions. */
	private void updateMenuActions() {

		if (Keyboard.a.pressed()) {
			if (slot == 2) {
				game.inventory.equipEquipment(index);
			}
		}
		if (Keyboard.start.pressed()) {
			game.closeMenu();
		}
		else if (Keyboard.select.pressed()) {
			game.nextMenu("menu_essences");
		}
	}

	// ======================= Drawing ========================
	
	/** Called every step to draw the room's tiles and entities in the room. */
	public void draw(Graphics2D g, Vector point) {
		super.draw(g, point);
		
		drawKeyItems(g, point);
		drawEquipment(g, point);
	}
	/** Draws the key items. */
	private void drawKeyItems(Graphics2D g, Vector point) {
		for (int i = 0; i < 15; i++) {
			Vector itemPoint = point.plus(24 + (i % 5) * 24, 8 + (i / 5) * 24);
			if (game.inventory.itemExists(Inventory.SLOT_KEY_ITEMS, i)) {
				game.inventory.getItem(Inventory.SLOT_KEY_ITEMS, i).draw(g, itemPoint);
			}
		}
		
		if (slot == 3) {
			drawCursor(g, point.plus(16 + (index % 5) * 24, 8 + (index / 5) * 24), ((index % 5 == 4) ? 24 : 16));
		}
	}
	/** Draws the equipment. */
	private void drawEquipment(Graphics2D g, Vector point) {
		for (int i = 0; i < 3; i++) {
			Vector itemPoint = point.plus(16 + i * 24, 80);
			if (game.inventory.itemExists(Inventory.SLOT_EQUIPMENT, i)) {
				game.inventory.getItem(Inventory.SLOT_EQUIPMENT, i).draw(g, itemPoint);
			}
		}
		
		if (slot == 2) {
			drawCursor(g, point.plus(8 + index * 24, 80), 16);
		}
	}

	// ====================== Transition ======================
	
	/** Called when the screen becomes the current game screen. */
	public void enterScreen() {
		super.enterScreen();
	}
	/** Called when the screen no longer is the current game screen. */
	public void leaveScreen() {
		super.leaveScreen();
	}
	
	// ==================== Text Scrolling ====================

	/** Called for the class to retrieve the description of the item it's over. */
	protected void displayItemDescription() {
		String name = "";
		String desc = "";
		if (game.inventory.itemExists(slot, index)) {
			name = game.inventory.getItem(slot, index).getName();
			desc = game.inventory.getItem(slot, index).getDescription();
		}
		setItemDescription(name, desc);
	}
}