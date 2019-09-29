package zelda.game.control.event;

import zelda.common.Sounds;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.object.dungeon.ObjectChest;


public class EventOpenChest extends EventQueue {
	protected ObjectChest chestObj;
	protected CollectableReward reward;
	protected EventTextMessage textMessage;
	protected double liftHeight;


	public EventOpenChest(ObjectChest obj) {
		this.chestObj = obj;
	}

	@Override
	public void begin() {
		reward = game.collectables.getCollectable(chestObj.getProperties().get(
				"reward", "rupees_1"));

		if (reward == null) {
			end();
			return;
		}

		liftHeight = 0;


		game.getPlayer().resetAnimation();
		game.getPlayer().getSprite().update();
		game.getPlayer().setBusy(true);

		if (reward.getLiftType() == CollectableReward.TYPE_RAISE) {
			addEvent(new Event() {
				@Override
				public void update() {
					super.update();
					liftHeight += 0.25;
					if (liftHeight >= 8) {
						liftHeight = 8;
						end();
					}
				}

				@Override
				public void draw() {
					super.draw();
					Draw.setViewPosition(game.getView().getPosition().minus(0, 16));

					Sprite spr = reward.getSprite();
					int offset = 0;
					if (reward.getLiftType() == CollectableReward.TYPE_RAISE)
						Draw.drawSprite(spr,
								chestObj.getPosition().minus(-offset,
										7 + liftHeight));
				}
			});
		}
		else
			addEvent(new TimedEvent(16));



		addEvent(textMessage = new EventTextMessage(reward.getMessage()) {
			@Override
			public void begin() {
				super.begin();
				Sounds.TUNE_REWARD.play();
				if (reward.getLiftType() != CollectableReward.TYPE_RAISE) {
					if (reward.getLiftType() == CollectableReward.TYPE_ONE_HAND)
						game.getPlayer().setAnimation(true,
								new Animation(0, 16));
					else
						game.getPlayer().setAnimation(true,
								new Animation(1, 16));

					game.getPlayer().getSprite().update();
				}
				if (reward != null)
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

					if (reward.getLiftType() == CollectableReward.TYPE_RAISE)
						Draw.drawSprite(spr,
								chestObj.getPosition().minus(-offset, 15));
					else
						Draw.drawSprite(spr, game.getPlayer().getPosition()
								.minus(-offset, 15));
				}
				super.draw();
			}
		});

		super.begin();
	}
}
