package zelda.game.entity.effect;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.game.entity.object.global.ObjectSomariaBlock;
import zelda.game.player.Player;


public class EffectCreateSomariaBlock extends Effect {

	public EffectCreateSomariaBlock(Player player, Vector position) {
		super(new Sprite(Resources.SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_SOMARIA_BLOCK_CREATE, false), position);
		setDepth(-1);
		Sounds.EFFECT_MYSTERY.play();
	}

	@Override
	protected void onFinish() {
		ObjectSomariaBlock block = new ObjectSomariaBlock();
		block.enterFrame(frame, new Point(position), null);
		game.addEntity(block);
		super.onFinish();
	}
}
