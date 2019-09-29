package zelda.game.entity.object.dungeon.color;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.util.Colors;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;


/**
 * 
 * @author David Jordan
 */
public class ObjectColorLantern extends FrameObject {
	private DynamicAnimation flameAnimations;
	private Sprite spriteFlame;


	public ObjectColorLantern() {
		imageSheet = Resources.SHEET_GENERAL_TILES;
		
		properties.set("color", Colors.NONE);
		
		flameAnimations = new DynamicAnimation(Animations
				.createStrip(8, 5, 3, 4), 4, 0, 4);
		spriteFlame = new Sprite(Resources.SHEET_SPECIAL_EFFECTS,
				flameAnimations);
	}

	@Override
	public void initialize() {
		
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("solid", true);
		objectData.addProperty("color");
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		if (!properties.isNull("color")) {
			spriteFlame.setVariation(properties.getInt("color"));
			spriteFlame.setFrameIndex(2);
			Draw.drawSprite(spriteFlame, pos.minus(0, 9));
		}
		
		super.drawTileSprite(pos, frame);
	}

	@Override
	public void update() {
		super.update();
		
		int color = properties.getInt("color", Colors.NONE);
		if (color != Colors.NONE)
			spriteFlame.update(color);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(0, 1);
	}

	@Override
	public FrameObject clone() {
		return new ObjectColorLantern();
	}

	@Override
	public void draw() {
		super.draw();
		
		int color = properties.getInt("color", Colors.NONE);
		if (color != Colors.NONE) {
			Draw.drawSprite(spriteFlame, position.x, position.y - 9);
		}
	}
}
