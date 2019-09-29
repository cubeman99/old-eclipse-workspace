package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.game.player.Player;


public class ItemMonsterSword extends Item {
	private Sprite sprite;


	public ItemMonsterSword(Player player) {
		super(player, "MonsterSword");
		numLevels = 1;
		name[0] = "Sword";
		description[0] = "A very sharp sword.";

		Animation anim1 = new Animation(new AnimationFrame()
				.addPart(6, 1, 0, 0).addPart(7, 1, 16, 0));
		Animation anim2 = new Animation(new AnimationFrame()
				.addPart(6, 1, 4, 0).addPart(7, 1, 20, 0));
		slotIcons[0] = new Sprite(Resources.SHEET_ICONS_LARGE, anim1);
		slotEquippedIcons[0] = new Sprite(Resources.SHEET_ICONS_LARGE, anim2);

		sprite = new Sprite(Resources.SHEET_PLAYER_ITEMS,
				Animations.MONSTER_SWORD);

		twoHanded = true;
	}

	@Override
	public void update() {
		if (isEquipped()) {
			sprite.setVariation(player.getDir());
			sprite.setFrameIndex(player.getSprite().getFrameIndex());
		}
	}

	@Override
	public void drawUnder() {
		if (isEquipped() && player.getDir() == Direction.UP)
			sprite.draw(player.getPosition());
	}

	@Override
	public void drawOver() {
		if (isEquipped() && player.getDir() != Direction.UP)
			sprite.draw(player.getPosition());
	}
}
