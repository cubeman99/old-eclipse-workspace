package zelda.game.control.event;

import zelda.common.Sounds;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.game.entity.collectable.CollectableReward;


public class EventReward extends EventQueue {
	protected CollectableReward reward;


	public EventReward(CollectableReward reward) {
		this.reward = reward;
	}

	@Override
	public void begin() {
		if (reward == null) {
			end();
			return;
		}
		
		game.getPlayer().resetAnimation();
		game.getPlayer().getSprite().update();
		game.getPlayer().setBusy(true);
		
		addEvent(new TimedEvent(4));

		addEvent(new EventTextMessage(reward.getMessage()) {
			@Override
			public void begin() {
				super.begin();

				Sounds.TUNE_REWARD.play();
				if (reward.getLiftType() == CollectableReward.TYPE_ONE_HAND)
					game.getPlayer().setAnimation(true, new Animation(0, 16));
				else
					game.getPlayer().setAnimation(true, new Animation(1, 16));

				game.getPlayer().getSprite().update();
				reward.collect();
			}
			
			@Override
			public void end() {
				super.end();
				game.getPlayer().setBusy(false);
			}
			
			@Override
			public void draw() {
				Draw.setViewPosition(game.getView().getPosition().minus(0, 16));
				
				if (reward != null) {
					Sprite spr = reward.getSprite();
					int offset = 0;
					if (reward.getLiftType() == CollectableReward.TYPE_ONE_HAND)
						offset -= 4;
					
					Draw.drawSprite(spr, game.getPlayer()
							.getPosition().minus(-offset, 15));
				}
				super.draw();
			}
		});

		super.begin();
	}
}
