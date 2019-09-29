package transition;

import game.HUD;

import java.awt.Graphics2D;


public abstract class Transition {

	// ======================= Members ========================
	
	/** The screen the transition is leaving. */
	protected Screen oldScreen;
	/** The screen the transition is entering. */
	protected Screen newScreen;
	/** The game HUD. */
	protected HUD hud;

	// ===================== Constructors =====================
	
	/** Constructs the default transition. */
	protected Transition(Screen oldScreen, Screen newScreen, HUD hud) {
		
		this.oldScreen	= oldScreen;
		this.newScreen	= newScreen;
		this.hud		= hud;
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the transition's state. */
	public void update() {
		
	}
	/** Called every step to draw the transition. */
	public void draw(Graphics2D g) {
		
	}
	
	// ===================== Information ======================
	
	/** Returns true if the transition has finished. */
	public abstract boolean isTransitionFinished();
	/** Returns true if the old screen is no longer being drawn. */
	public abstract boolean isOldScreenFinished();
}
