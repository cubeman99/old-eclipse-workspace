package menu;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Keyboard;

import game.GameInstance;
import game.Inventory;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.library.Library;

/**
 * The menu used to switch out the player's weapons.
 * @author	Robert Jordan
 */
public class EssenceMenu extends InventoryMenu {

	// ====================== Constants =======================
	
	// ======================= Members ========================
	
	public int sideMenuIndex;
	public boolean inSideMenu;
	
	// ===================== Constructors =====================
	
	/** Constructs the default weapon menu. */
	public EssenceMenu() {
		super("menu_essences", "menu_essences_" + (useAdvancedMenus ? "b" : "a"), Inventory.SLOT_ESSENCES);
		
		this.inSideMenu = false;
		this.sideMenuIndex = 0;
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
		if (Keyboard.left.pressed() || Keyboard.right.pressed()) {
			inSideMenu = !inSideMenu;
			displayItemDescription();
		}
		if (Keyboard.up.pressed()) {
			if (inSideMenu) {
				sideMenuIndex--;
				if (sideMenuIndex < 0)
					sideMenuIndex = 1;
			}
			else {
				index--;
				if (index < 0)
					index = 7;
			}
			displayItemDescription();
		}
		if (Keyboard.down.pressed()) {
			if (inSideMenu) {
				sideMenuIndex++;
				if (sideMenuIndex > 1)
					sideMenuIndex = 0;
			}
			else {
				index++;
				if (index > 7)
					index = 0;
			}
			displayItemDescription();
		}
	}
	/** Updates the menu actions. */
	private void updateMenuActions() {

		if (Keyboard.a.pressed()) {
			if (inSideMenu && sideMenuIndex == 1) {
				// Open save game menu
			}
		}
		if (Keyboard.start.pressed()) {
			game.closeMenu();
		}
		else if (Keyboard.select.pressed()) {
			game.nextMenu("menu_weapons");
		}
	}

	// ======================= Drawing ========================
	
	/** Called every step to draw the room's tiles and entities in the room. */
	public void draw(Graphics2D g, Vector point) {
		super.draw(g, point);

		//drawBackground(g, point);
		
		drawEssences(g, point);
		drawSideMenu(g, point);
		
		//drawItemDescription(g, point);
	}
	/** Draws the essences pieces. */
	private void drawEssences(Graphics2D g, Vector point) {
		// Define the essence positions
		Vector[] essencePoints = new Vector[8];
		essencePoints[0] = new Vector(24, 16);
		essencePoints[1] = new Vector(48, 16);
		essencePoints[2] = new Vector(64, 32);
		essencePoints[3] = new Vector(64, 56);
		essencePoints[4] = new Vector(48, 72);
		essencePoints[5] = new Vector(24, 72);
		essencePoints[6] = new Vector(8, 56);
		essencePoints[7] = new Vector(8, 32);
		
		// Draw the items
		for (int i = 0; i < 8; i++) {
			if (game.inventory.itemExists(Inventory.SLOT_ESSENCES, i)) {
				game.inventory.getItem(Inventory.SLOT_ESSENCES, i).draw(g, point.plus(essencePoints[i]).plus(8, 0));
			}
		}

		// Draw the cursor
		if (!inSideMenu) {
			drawCursor(g, point.plus(essencePoints[index]), 16);
		}
	}
	/** Draws the side menu. */
	private void drawSideMenu(Graphics2D g, Vector point) {
		
		// Draw the heart piece number
		g.setColor(Color.BLACK);
		Draw.drawRasterString(g, Library.fonts.fontSmall, "0/4", point.plus(120, 40));
		
		if (useAdvancedMenus) {
		// Draw the heart piece shape
			Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(4, 0), point.plus(112, 8));
			Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(2, 1), point.plus(112, 24));
			Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(3, 0), point.plus(128, 8));
			Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(3, 1), point.plus(128, 24));
		}
		else {
			Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(0, 2), point.plus(112, 8));
			Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(0, 1), point.plus(112, 24));
			Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(1, 0), point.plus(128, 8));
			Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(1, 1), point.plus(128, 24));
		}
		// Draw the save button
		Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(5, 2), point.plus(112, 80));
		Draw.drawTile(g, Library.tilesets.menuLargeLight, new Point(6, 2), point.plus(128, 80));

		// Define the cursor positions
		Vector[] sidePoints = new Vector[2];
		sidePoints[0] = new Vector(104, 32);
		sidePoints[1] = new Vector(104, 80);
		
		// Draw the cursor
		if (inSideMenu) {
			drawCursor(g, point.plus(sidePoints[sideMenuIndex]), 32);
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
		if (!inSideMenu) {
			if (game.inventory.itemExists(slot, index)) {
				name = game.inventory.getItem(slot, index).getName();
				desc = game.inventory.getItem(slot, index).getDescription();
			}
		}
		else {
			if (sideMenuIndex == 0) {
				name = "Pieces of Heart";
				desc = "4 more makes a heart container.";
			}
			else {
				name = "Save Screen";
				desc = "Goto the Save Screen.";
			}
		}
		setItemDescription(name, desc);
	}
}