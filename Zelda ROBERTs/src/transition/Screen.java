package transition;

import java.awt.Graphics2D;

import game.GameInstance;
import geometry.Vector;

/**
 * A base class for all screens that display in the game.
 * @author	Robert Jordan
 */
public class Screen {

	// ======================= Members ========================
	
	/** The instance of the game. */
	public GameInstance game;
	/** The id used by the screen. */
	public String id;

	// ===================== Constructors =====================
	
	/** Constructs the default screen. */
	public Screen() {
		this.game	= null;
		this.id		= "";
	}
	/** Constructs a screen with the given id. */
	public Screen(String id) {
		this.game	= null;
		this.id		= id;
	}
	/** Initializes the screen and sets up the container variables. */
	public void initialize(GameInstance game) {
		this.game = game;
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the screen's state. */
	public void update() {
		
	}
	/** Called every step to draw the screen. */
	public void draw(Graphics2D g, double x, double y) {
		draw(g, new Vector(x, y));
	}
	/** Called every step to draw the screen. */
	public void draw(Graphics2D g, Vector point) {
		
	}
	
	// ====================== Transition ======================
	
	/** Called when the screen becomes the current game screen. */
	public void enterScreen() {
		
	}
	/** Called when the screen no longer is the current game screen. */
	public void leaveScreen() {
		
	}
	
	// ===================== Information ======================
	
	/** Gets the key identifier of the screen. */
	public String getID() {
		return id;
	}
}