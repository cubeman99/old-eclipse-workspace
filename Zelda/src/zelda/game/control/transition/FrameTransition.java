package zelda.game.control.transition;

import zelda.common.geometry.Vector;
import zelda.game.control.GameInstance;
import zelda.game.control.event.Event;
import zelda.game.world.Frame;


public class FrameTransition extends Event {
	protected GameInstance game;
	protected Frame frameOld;
	protected Frame frameNew;


	public FrameTransition(GameInstance game) {
		this(game, null);
	}

	public FrameTransition(GameInstance game, Frame frameNew) {
		this.game = game;
		this.frameOld = game.getFrame();
		this.frameNew = frameNew;
	}
	
	public void begin(Vector newPlayerPosition) {
		super.begin();
		Vector playerPos = new Vector(game.getPlayer().getPosition());
		
		game.getWorld().setCurrentLevel(frameNew.getLevel());
		game.getLevel().setCurrentFrame(frameNew);
		game.getPlayer().getPosition().set(newPlayerPosition);
		
		this.frameNew.enter();
		
		game.getWorld().setCurrentLevel(frameOld.getLevel());
		game.getLevel().setCurrentFrame(frameOld);
		game.getPlayer().getPosition().set(playerPos);
	}

	@Override
	public void begin() {
		begin(game.getPlayer().getPosition());
	}

	@Override
	public void end() {
		super.end();

		frameOld.leave();
		
		game.getWorld().setCurrentLevel(frameNew.getLevel());
		game.getLevel().setCurrentFrame(frameNew);
		game.addEntity(game.getPlayer());
		
		game.getPlayer().recordFrameEnterPosition();
		game.getPlayer().setDestroyed(false);
		game.getPlayer().onEnterFrame();
		
		game.getView().centerAt(game.getPlayer());
	}
}
