package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import main.GamePanel;
import main.Main;

import graphics.Draw;
import graphics.Tileset;
import graphics.library.Library;

/**
 * The HUD drawn at the top of the screen that shows player information.
 * @author	Robert Jordan
 */
public class HUD {

	// ====================== Constants =======================
	
	// ======================= Members ========================

	/** The instance of the game. */
	public GameInstance game;
	
	// ===================== Constructors =====================
	
	/** Constructs the default HUD. */
	public HUD() {
		this.game = null;
	}
	/** Initializes the HUD and sets up the container variables. */
	public void initialize(GameInstance game) {
		this.game = game;
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the HUD. */
	public void update() {
		
		
	}
	/** Called every step to draw the HUD. */
	public void draw(Graphics2D g) {
		g.setColor(new Color(248, 208, 136));
		//Draw.fillRect(g, 0, 0, GamePanel.canvasSize.x, 16);
		final Tileset tileset = (game.isInMenu() ?
				Library.tilesets.menuSmallLight : Library.tilesets.menuSmall);
		if (game.isInMenu())
			g.setColor(new Color(16, 16, 16));
		else
			g.setColor(new Color(0, 0, 0));
		
		for (int i = 0; i < GamePanel.canvasSize.x; i += 8) {
			for (int j = 0; j < 16; j += 8) {
				Draw.drawTile(g, tileset, 2, 4, i, j);
			}
		}
		
		if (!Main.debugMode) {
			drawItemFrames(g);
			drawRupees(g);
			drawHearts(g);
		}
		else {
			drawDebugInfo(g);
		}
	}

	// ======================= Drawing ========================
	
	/** Called to draw the item frames and items. */
	private void drawItemFrames(Graphics2D g) {
		final Tileset tileset = (game.isInMenu() ?
				Library.tilesets.menuSmallLight : Library.tilesets.menuSmall);
		if (game.inventory.isTwoHandedEquipped()) {
			// B bracket side
			Draw.drawTile(g, tileset, 7, 1, 8, 0);
			Draw.drawTile(g, tileset, 9, 1, 8, 8);
			// A bracket side
			Draw.drawTile(g, tileset, 8, 0, 56, 0);
			Draw.drawTile(g, tileset, 10, 1, 56, 8);
			// Item
			game.inventory.getItem(0, 0).draw(g, 16, 0);
		}
		else if (!game.advancedGame) {
			// B bracket
			Draw.drawTile(g, tileset, 7, 1, 0, 0);
			Draw.drawTile(g, tileset, 9, 1, 0, 8);
			Draw.drawTile(g, tileset, 10, 0, 32, 0);
			Draw.drawTile(g, tileset, 10, 1, 32, 8);
			// A bracket
			Draw.drawTile(g, tileset, 7, 0, 40, 0);
			Draw.drawTile(g, tileset, 9, 1, 40, 8);
			Draw.drawTile(g, tileset, 10, 0, 72, 0);
			Draw.drawTile(g, tileset, 10, 1, 72, 8);
			// Items
			if (game.inventory.itemExists(0, 0))
				game.inventory.getItem(0, 0).draw(g, 8, 0);
			if (game.inventory.itemExists(0, 1))
				game.inventory.getItem(0, 1).draw(g, 48, 0);
		}
		else {
			// B bracket side
			Draw.drawTile(g, tileset, 7, 1, 0, 0);
			Draw.drawTile(g, tileset, 9, 1, 0, 8);
			// Both bracket side
			Draw.drawTile(g, tileset, 11, 0, 32, 0);
			Draw.drawTile(g, tileset, 11, 1, 32, 8);
			// A bracket side
			Draw.drawTile(g, tileset, 8, 0, 64, 0);
			Draw.drawTile(g, tileset, 10, 1, 64, 8);
			// Items
			if (game.inventory.itemExists(0, 0))
				game.inventory.getItem(0, 0).draw(g, 8, 0);
			if (game.inventory.itemExists(0, 1))
				game.inventory.getItem(0, 1).draw(g, 40, 0);
		}
	}
	/** Called to draw the rupees and keys. */
	private void drawRupees(Graphics2D g) {
		// Text formats
		DecimalFormat rupeeFormat = new DecimalFormat("000");
		DecimalFormat keyFormat = new DecimalFormat("0");
		final Tileset tileset = (game.isInMenu() ?
				Library.tilesets.menuSmallLight : Library.tilesets.menuSmall);
		
		if (!game.advancedGame) {
			if (game.isInDungeon()) {
				// Keys
				Draw.drawTile(g, tileset, 1, 0, 80, 0);
				Draw.drawTile(g, tileset, 1, 1, 88, 0);
				Draw.drawRasterString(g, Library.fonts.fontSmall,
						keyFormat.format(game.smallKeys[game.currentDungeon]), 96, 0);
			}
			else {
				// Rupee icon
				Draw.drawTile(g, tileset, 0, 2, 80, 0);
			}
			// Rupees
			Draw.drawRasterString(g, Library.fonts.fontSmall, rupeeFormat.format(game.rupees), 80, 8);
		}
		else {
			if (game.isInDungeon()) {
				// Keys
				Draw.drawTile(g, tileset, 1, 0, 72, 0);
				Draw.drawTile(g, tileset, 1, 1, 80, 0);
				Draw.drawRasterString(g, Library.fonts.fontSmall,
						String.valueOf(game.smallKeys[game.currentDungeon]), 88, 0);
			}
			else {
				// Rupee icon
				Draw.drawTile(g, tileset, 0, 2, 72, 0);
			}
			// Rupees
			Draw.drawRasterString(g, Library.fonts.fontSmall, rupeeFormat.format(game.rupees), 72, 8);
		}
	}
	/** Called to draw the hearts. */
	private void drawHearts(Graphics2D g) {
		final Tileset tileset = (game.isInMenu() ?
				Library.tilesets.menuSmallLight : Library.tilesets.menuSmall);
		
		if (!game.advancedGame) {
			// Max of 14 hearts
			for (int i = 0; i < game.player.maxHealth / 4; i++) {
				int fullness = Math.min(4, Math.max(0, game.player.health - i*4));
				Draw.drawTile(g, tileset, fullness, 0, 104 + (i % 7) * 8, (i / 7) * 8);
			}
		}
		else {
			// Max of 16 hearts
			for (int i = 0; i < game.player.maxHealth / 4; i++) {
				int fullness = Math.min(4, Math.max(0, game.player.health - i*4));
				Draw.drawTile(g, tileset, fullness, 0, 96 + (i % 8) * 8, (i / 8) * 8);
			}
		}
	}
	/** Called to draw the debug information. */
	private void drawDebugInfo(Graphics2D g) {
		// Get variables
		long usedMemory	= (long)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000;
		int entities = 0;
		if (game.room != null && !game.inMenu)
			entities = game.room.getNumEntities();
		
		// Text formats
		DecimalFormat dFormat = new DecimalFormat("0.0");
		DecimalFormat nFormat = new DecimalFormat("#,##0");
		
		// Frames per second
		Draw.drawRasterString(g, Library.fonts.fontSmall,
				"FPS:" + dFormat.format(Main.fps), 0, 0, 0);
		// Used memory
		Draw.drawRasterString(g,  Library.fonts.fontSmall,
				"MEM:" + nFormat.format(usedMemory) + "KB", 0, 8);
		// Number of entities in the room
		Draw.drawRasterString(g, Library.fonts.fontSmall,
				"ENT:" + nFormat.format(entities), 80, 0, 0);
	}
}