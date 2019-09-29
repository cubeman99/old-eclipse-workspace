package transition;

import game.HUD;
import geometry.Vector;
import graphics.Draw;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;


public class Fade extends Transition {

	// ======================= Members ========================
	
	/** The counter used to time the transition. */
	private int counter;

	// ===================== Constructors =====================
	
	/** Constructs the default transition. */
	public Fade(Screen oldScreen, Screen newScreen, HUD hud) {
		super(oldScreen, newScreen, hud);
		
		this.counter = 30;
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the screen's state. */
	public void update() {
		counter--;
		if (counter < 0)
			counter = 0;
	}
	/** Called every step to draw the transition. */
	public void draw(Graphics2D g) {
		
		if (counter > 21) {
			oldScreen.draw(g, 0, 16);
			hud.draw(g);
			g.setColor(new Color(248, 248, 248, (int)(255 * ((30.0 - counter) / 9.0))));
		}
		else if (counter <= 9) {
			newScreen.draw(g, 0, 16);
			hud.draw(g);
			g.setColor(new Color(248, 248, 248, (int)(255 * (counter / 9.0))));
		}
		else {
			g.setColor(new Color(248, 248, 248));
		}
		
		if (counter > 0) {
			Draw.fillRect(g, new Vector(0, 0), GamePanel.canvasSize.getVector());
		}
	}
	
	// ===================== Information ======================
	
	/** Returns true if the transition has finished. */
	public boolean isTransitionFinished() {
		return counter == 0;
	}
	/** Returns true if the old screen is no longer being drawn. */
	public boolean isOldScreenFinished() {
		return counter <= 20;
	}
}
