package menu;

import java.awt.Graphics2D;

import transition.Screen;

import game.GameInstance;
import geometry.Vector;
import graphics.Draw;
import graphics.LinkedImage;

/**
 * A class for maneuvering a graphical interface.
 * @author	Robert Jordan
 */
public class Menu extends Screen {

	// ======================= Members ========================
	
	/** The background image used by the menu. */
	protected LinkedImage background;

	// ===================== Constructors =====================
	
	/** Constructs the default menu. */
	public Menu() {
		super();
		
		this.background	= null;
	}
	/** Constructs a menu with the specified background image. */
	public Menu(String id, String background) {
		super(id);
		
		this.background	= new LinkedImage(background);
	}
	/** Initializes the menu and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the menu's state. */
	public void update() {
		
	}
	/** Called every step to draw the menu. */
	public void draw(Graphics2D g, Vector point) {
		Draw.drawImage(g, background.getImage(), point);
	}
	
	// ====================== Transition ======================
	
	/** Called when the screen becomes the current game screen. */
	public void enterScreen() {
		
	}
	/** Called when the screen no longer is the current game screen. */
	public void leaveScreen() {
		
	}
}