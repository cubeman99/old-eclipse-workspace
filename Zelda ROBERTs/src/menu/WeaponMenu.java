package menu;

import items.Ammo;
import items.weapons.Weapon;

import java.awt.Graphics2D;

import main.GamePanel;
import main.Keyboard;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.library.Library;

/**
 * The menu used to switch out the player's weapons.
 * @author	Robert Jordan
 */
public class WeaponMenu extends InventoryMenu {

	// ======================= Members ========================

	/** True if the menu is currently displaying ammo. */
	private boolean inAmmoMenu;
	/** The current index of the cursor in the ammo menu. */
	private int ammoIndex;
	/** The list of ammo used by the selected item. */
	private Ammo[] ammo;
	/** The size of the ammo menu when growing for display. */
	private Point ammoMenuSize;
	
	// ===================== Constructors =====================
	
	/** Constructs the default weapon menu. */
	public WeaponMenu() {
		super("menu_weapons", "menu_weapons_" + (useAdvancedMenus ? "b" : "a"), 1);
		
		this.inAmmoMenu		= false;
		this.ammoIndex		= 0;
		this.ammo			= null;
		this.ammoMenuSize	= new Point(16, 8);
	}
	/** Initializes the menu and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the menu's state. */
	public void update() {
		super.update();
		
		if (!inAmmoMenu) {
			updateMenuNavigation();
			updateMenuActions();
		}
		else if (inAmmoMenu) {
			updateAmmoMenu();
			updateAmmoMenuActions();
		}
	}
	/** Called every step to update the normal menu. */
	private void updateMenuNavigation() {
		// Move the cursor
		if (Keyboard.left.pressed()) {
			index--;
			if (index < 0)
				index = 15;
			displayItemDescription();
		}
		if (Keyboard.right.pressed()) {
			index++;
			if (index > 15)
				index = 0;
			displayItemDescription();
		}
		if (Keyboard.up.pressed()) {
			index -= 4;
			if (index < 0)
				index += 16;
			displayItemDescription();
		}
		if (Keyboard.down.pressed()) {
			index += 4;
			if (index > 15)
				index -= 16;
			displayItemDescription();
		}
	}
	/** Updates the menu actions. */
	private void updateMenuActions() {
		// Equip the item
		if (Keyboard.a.pressed() || Keyboard.b.pressed()) {
			if (game.inventory.itemExists(1, index)) {
				if (game.inventory.getWeapon(1, index).isConsumable()) {
					ammo = game.inventory.getWeapon(1, index).getAmmoTypes();
					if (ammo != null) {
						if (ammo.length > 1) {
							// The ammo menu should be opened
							inAmmoMenu = true;
							ammoMenuSize = new Point(16, 8);
							// Find the current ammo
							for (int i = 0; i < ammo.length; i++) {
								if (game.inventory.getWeapon(1, index).getCurrentAmmo() == ammo[i]) {
									ammoIndex = i;
									break;
								}
							}
						}
					}
				}
			}
			if (!inAmmoMenu) {
				game.inventory.equipWeapon(index, (Keyboard.b.pressed() ? 0 : 1));
				displayItemDescription();
			}
		}
		// Up the level
		if (Keyboard.x.pressed() && game.inventory.itemExists(1, index)) {
			Weapon weapon = game.inventory.getWeapon(1, index);
			if (weapon.getLevel() >= weapon.getMaxLevel())
				weapon.setLevel(1);
			else
				weapon.setLevel(weapon.getLevel() + 1);
		}
		// Up the ammo
		if (Keyboard.y.pressed() && game.inventory.itemExists(1, index)) {
			if (game.inventory.getWeapon(1, index).isConsumable()) {
				Ammo ammo = game.inventory.getWeapon(1, index).getCurrentAmmo();
				if (ammo != null) {
					if (ammo.isAtCapacity())
						ammo.setCount(0);
					else
						ammo.addCount(1);
				}
			}
		}
		
		if (Keyboard.start.pressed() && !inAmmoMenu) {
			game.closeMenu();
		}
		else if (Keyboard.select.pressed() && !inAmmoMenu) {
			game.nextMenu("menu_key_items");
		}
	}
	/** Called every step to update the ammo menu. */
	private void updateAmmoMenu() {
		Point maxSize = new Point(ammo.length * 24 + 8, 32);

		// Update ammo menu growth
		if (ammoMenuSize.x < maxSize.x) {
			ammoMenuSize.x += 8;
		}
		else if (ammoMenuSize.y < maxSize.y) {
			ammoMenuSize.y += 4;
			if (ammoMenuSize.y >= maxSize.y) {
				displayItemDescription();
			}
		}
		else {
			if (Keyboard.left.pressed()) {
				ammoIndex--;
				if (ammoIndex < 0)
					ammoIndex = ammo.length - 1;
				displayItemDescription();
			}
			if (Keyboard.right.pressed()) {
				ammoIndex++;
				if (ammoIndex > ammo.length - 1)
					ammoIndex = 0;
				displayItemDescription();
			}
		}
	}
	/** Updates the menu actions. */
	private void updateAmmoMenuActions() {

		// Equip the item
		if (Keyboard.a.pressed() || Keyboard.b.pressed() || Keyboard.start.pressed()) {
			game.inventory.getWeapon(1, index).setCurrentAmmo(ammo[ammoIndex]);
			game.inventory.equipWeapon(index, (Keyboard.b.pressed() ? 0 : 1));
			inAmmoMenu = false;
			displayItemDescription();
		}
		
		// Up the ammo
		if (Keyboard.y.pressed()) {
			if (ammo[ammoIndex].isAtCapacity())
				ammo[ammoIndex].setCount(0);
			else
				ammo[ammoIndex].addCount(1);
		}
	}
	
	// ======================= Drawing ========================
	
	/** Called every step to draw the room's tiles and entities in the room. */
	public void draw(Graphics2D g, Vector point) {
		super.draw(g, point);
		
		drawWeapons(g, point);
		if (inAmmoMenu)
			drawAmmoMenu(g, point);
	}
	/** Draws the weapons. */
	private void drawWeapons(Graphics2D g, Vector point) {
		// Draw the items
		for (int i = 0; i < 16; i++) {
			if (game.inventory.itemExists(1, i)) {
				game.inventory.getItem(1, i).draw(g, point.plus(24 + (i % 4) * 32, 8 + (i / 4) * 24));
			}
		}
		
		// Draw the cursor
		if (!inAmmoMenu) {
			drawCursor(g, point.plus(16 + (index % 4) * 32, 8 + (index / 4) * 24), 24);
		}
	}
	/** Draws the ammo menu. */
	private void drawAmmoMenu(Graphics2D g, Vector point) {
		
		Point maxSize = new Point(ammo.length * 24 + 8, 32);
		
		Point modulusSize = new Point((ammoMenuSize.x / 16) * 16, (ammoMenuSize.y / 8) * 8);
		Vector offset = point.plus((GamePanel.canvasSize.x - modulusSize.x) / 2, 16);
		
		if (index < 8) {
			offset.y += 40;
		}
		
		// Draw the ammo background
		for (int i = 0; i < modulusSize.x; i += 8) {
			for (int j = 0; j < modulusSize.y; j += 8) {
				Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(1, 4),
						offset.plus(i, j));
			}
		}
		
		// If the ammo menu is finished growing, draw the contents
		if (ammoMenuSize.x >= maxSize.x && ammoMenuSize.y >= maxSize.y) {
			
			// Draw the ammo
			for (int i = 0; i < ammo.length; i++) {
				ammo[i].draw(g, offset.plus(8 + i * 24, 0));
			}
			
			// Draw the arrow cursor
			Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(5, 5),
					offset.plus(12 + ammoIndex * 24, 24));
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
		if (!inAmmoMenu) {
			if (game.inventory.itemExists(slot, index)) {
				name = game.inventory.getItem(slot, index).getName();
				desc = game.inventory.getItem(slot, index).getDescription();
			}
		}
		else {
			name = ammo[ammoIndex].getName();
			desc = ammo[ammoIndex].getDescription();
		}
		setItemDescription(name, desc);
	}
}