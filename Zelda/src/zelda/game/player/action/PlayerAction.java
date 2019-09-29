package zelda.game.player.action;

import zelda.game.control.GameInstance;
import zelda.game.player.Player;


public abstract class PlayerAction {
	protected GameInstance game;
	protected Player player;
	
	public PlayerAction(Player player) {
		this.game   = player.getGame();
		this.player = player;
	}
	
	public abstract void begin();
	
	public void end() {};
	
	public abstract void update();
	
	public void draw() {};
}
