package zelda.game.player.action;

import zelda.common.Sounds;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.game.player.Player;


public class ActionFrameDie extends PlayerAction {
	public static final int TYPE_FALL  = 0;
	public static final int TYPE_WATER = 1;
	public static final int TYPE_LAVA  = 2;
	private boolean waitingForView;
	private int type;
	
	public ActionFrameDie(Player player) {
		super(player);
		
	}
	
	@Override
	public void update() {
		player.getVelocity().zero();
		if (type == TYPE_LAVA)
			player.hurtFlicker();
		
		if (waitingForView) {
			if (game.getView().isCenteredAt(player)) {
				player.resetAnimation();
				player.beginAction(player.actionNormal);
				player.damage(2, null);
				Sounds.PLAYER_HURT.stop();
			}
		}
		else {
			if (player.getSprite().isLooped())
				player.setAnimation(false, Animations.PLAYER_DUNK);
			if (player.isAnimationDone()) {
				player.setPosition(player.getFrameEnterPosition());
				player.setAnimation(false, new Animation(-1, -1));
				waitingForView = true;
			}
		}
	}
	
	public void begin(int type) {
		this.type = type;
		begin();
	}
	
	@Override
	public void begin() {
		waitingForView = false;
		player.setPassable(true);
		player.setCollideWithWorld(false);
		player.getVelocity().zero();
		
		if (type == TYPE_FALL) {
			player.setAnimation(false, Animations.PLAYER_FALL);
			Sounds.PLAYER_FALL.play();
		}
		else {
			player.setAnimation(false, Animations.PLAYER_DUNK);
			Sounds.PLAYER_WADE.play();
		}
	}
}
