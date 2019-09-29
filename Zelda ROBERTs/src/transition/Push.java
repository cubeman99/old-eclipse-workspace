package transition;

import game.HUD;

import java.awt.Graphics2D;

import main.GamePanel;


public class Push extends Transition {

	// ======================= Members ========================
	
	/** The offset position of the screens. */
	private int position;
	/** The direction the transition is moving in. */
	private int direction;
	/** The speed the transition is moving at. */
	private int speed;

	// ===================== Constructors =====================
	
	/** Constructs a transition with the given screens. */
	public Push(int direction, int speed, Screen oldScreen, Screen newScreen, HUD hud) {
		super(oldScreen, newScreen, hud);

		this.position	= ((direction % 2 == 0) ? GamePanel.canvasSize.x : GamePanel.canvasSize.y - 16);
		this.direction	= direction;
		this.speed		= speed;
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the screen's state. */
	public void update() {
		position -= speed;
		if (position < 0)
			position = 0;
	}
	/** Called every step to draw the transition. */
	public void draw(Graphics2D g) {
		
		switch (direction) {
		case 0:
			oldScreen.draw(g, position - GamePanel.canvasSize.x, 16);
			newScreen.draw(g, position, 16);
			break;
		case 1:
			oldScreen.draw(g, 0, position + GamePanel.canvasSize.y + 16);
			newScreen.draw(g, 0, position + 16);
			break;
		case 2:
			oldScreen.draw(g, GamePanel.canvasSize.x - position, 16);
			newScreen.draw(g, -position, 16);
			break;
		case 3:
			oldScreen.draw(g, 0, GamePanel.canvasSize.y - position + 16);
			newScreen.draw(g, 0, -position + 16);
			break;
		}

		hud.draw(g);
	}
	
	// ===================== Information ======================
	
	/** Returns true if the transition has finished. */
	public boolean isTransitionFinished() {
		return position == 0;
	}
	/** Returns true if the old screen is no longer being drawn. */
	public boolean isOldScreenFinished() {
		return position == 0;
	}
}
