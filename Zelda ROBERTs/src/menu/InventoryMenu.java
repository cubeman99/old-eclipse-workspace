package menu;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.library.Library;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * A base class for the three different inventory menus.
 * @author	Robert Jordan
 */
public class InventoryMenu extends Menu {

	public static boolean useAdvancedMenus = true;
	
	// ======================= Members ========================
	
	/** The current slot the cursor is in. */
	protected int slot;
	/** The current index the cursor is at. */
	protected int index;
	
	/** The position of the scrolling text. */
	public int textPosition;
	/** The countdown for how long the text should pause for. */
	public int textPause;
	/** The position where the description text started. */
	public int textStart;
	/** The description being displayed at the bottom of the menu. */
	public String description;

	// ===================== Constructors =====================
	
	/** Constructs the default menu. */
	public InventoryMenu() {
		super();
		
		this.slot			= 0;
		this.index			= 0;
		this.textPosition	= 0;
		this.textPause		= 0;
		this.textStart		= 0;
		this.description	= "";
	}
	/** Constructs a menu of the specified size. */
	public InventoryMenu(String id, String background, int slot) {
		super(id, background);

		this.slot			= slot;
		this.index			= 0;
		this.textPosition	= 0;
		this.textPause		= 0;
		this.textStart		= 0;
		this.description	= "";
	}
	/** Initializes the menu and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the menu's state. */
	public void update() {
		super.update();
		
		updateItemDescription();
	}
	/** Called every step to update the item description's scroll position. */
	protected void updateItemDescription() {
		if (!description.isEmpty()) {
			if (textPause > 0) {
				// Update the pause counter
				textPause--;
				if (textPause == 0)
					textPosition++;
			}
			else if (textPosition == 0) {
				// Pause the text when it lands on the item name
				textPause = 32;
			}
			else {
				textPosition++;
				if (textPosition / 8 > description.length() + textStart / 8) {
					// Wrap the text when it reaches the end
					textPosition = -128 + textStart;
				}
			}
		}
	}
	
	// ======================= Drawing ========================
	
	/** Called every step to draw the menu. */
	public void draw(Graphics2D g, Vector point) {
		super.draw(g, point);
		
		drawItemDescription(g, point);
	}
	/** Draws the selection cursor at the given position. */
	protected void drawCursor(Graphics2D g, Vector point, int width) {
		Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(9, 0), point);
		Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(9, 1), point.plus(0, 8));
		Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(10, 0), point.plus(width + 8, 0));
		Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(10, 1), point.plus(width + 8, 8));
	}
	/** Draws the scrolling item description at the bottom of the screen. */
	protected void drawItemDescription(Graphics2D g, Vector point) {
		int position = textPosition - textStart;
		int textIndex = position / 8;
		if (position < 0) {
			// Round down always
			textIndex = (position - 7) / 8;
			position = ((position - 7) / 8) * 8;
		}
		else {
			position = (position / 8) * 8;
		}
		
		int startIndex = Math.max(0, textIndex);
		int endIndex = Math.max(0, Math.min(textIndex + 16, description.length()));
		String text = description.substring(startIndex, endIndex);

		// Draw the scrolling text
		g.setColor(new Color(16, 40, 88));
		if (position < 0)
			Draw.drawRasterString(g, Library.fonts.fontLarge, text, point.plus(16 - (position / 8) * 8, 108));
		else
			Draw.drawRasterString(g, Library.fonts.fontLarge, text, point.plus(16, 108));
	}
	
	// ====================== Transition ======================
	
	/** Called when the screen becomes the current game screen. */
	public void enterScreen() {
		super.enterScreen();
		
		displayItemDescription();
	}
	/** Called when the screen no longer is the current game screen. */
	public void leaveScreen() {
		super.leaveScreen();
		
		textPosition	= 0;
		textPause		= 0;
		textStart		= 0;
		description		= "";
	}
	
	// ==================== Text Scrolling ====================
	
	/** Called for the class to retrieve the description of the item it's over. */
	protected void displayItemDescription() {
		
	}
	/** Called to change the current description being displayed. */
	protected void setItemDescription(String name, String desc) {
		description = name;
		textPosition = 0;
		textPause = 0;
		textStart = 0;
		if (!description.isEmpty()) {
			if (description.length() % 2 == 1)
				description += " ";
			
			if (description.length() == 16) {
				description += " ";
				textStart = 0;
			}
			else {
				for (int i = (8 - description.length() / 2); i > 0; i--)
					description += " ";
				textStart = (16 - description.length()) * 8;
			}
			
			description += desc;
		}
	}
}