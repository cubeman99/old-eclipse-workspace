package zelda.game.entity.object.dungeon;

import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.game.entity.effect.EffectFallingObject;
import zelda.game.entity.object.FrameObject;


public class ObjectCrackedFloor extends FrameObject {
	private int timer;
	
	public void crumble() {
		destroy();
		game.addEntity(new EffectFallingObject(getCenter()));
		Sounds.EFFECT_FLOOR_CRUMBLE.play();
	}
	
	@Override
	public void update() {
		super.update();
		
		if (game.getPlayer().onGround() && getLocation().equals(game.getPlayer().getStandLocation())) {
			timer++;
			if (timer > 30) {
				crumble();
			}
		}
		else {
			timer = 0;
		}
	}
	
	@Override
	public void initialize() {
		timer = 0;
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("surface", true);
	}
	
	@Override
	public void preDraw() {
		super.draw();
	}
	
	@Override
	public void draw() {
		// Don't draw.
	}

	@Override
	public Point createSpriteSource() {
		return new Point(8, 9);
	}

	@Override
	public FrameObject clone() {
		return new ObjectCrackedFloor();
	}
}
