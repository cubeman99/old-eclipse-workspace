package zelda.game.entity.object.dungeon;

import java.util.ArrayList;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.properties.PropertyHolder;
import zelda.game.control.script.Function;
import zelda.game.control.text.Message;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;


// Properties:
// stay_down = false
// enabled = false
// down => false
public class ObjectButton extends FrameObject {
	private boolean down;

	public ObjectButton() {
		properties.define("event_press", "open_doors!");
		properties.define("event_release", "close_doors!");
	}

	public void press() {
		down = true;
		Sounds.PLAYER_WADE.play();
		properties.set("down", true);
		sprite.newAnimation(new Animation(8, 8));
		properties.script("event_press", this, frame);
	}

	public void release() {
		down = false;
		Sounds.PLAYER_WADE.play();
		properties.set("down", false);
		sprite.newAnimation(new Animation(7, 8));
		properties.script("event_release", this, frame);
	}

	@Override
	public void update() {
		super.update();
		
		FrameObject obj = (FrameObject) Collision.getInstanceMeeting(this, FrameObject.class);
		
		if (Collision.isTouching(this, game.getPlayer().getHardCollidable())
				|| (obj != null && !obj.isDestroyed())) {
			if (!down)
				press();
		}
		else if (down && !properties.getBoolean("stay_down", false))
			release();
	}

	@Override
	public void initialize() {
		setDepth(10000);

		properties.define("down", false);
		down = properties.getBoolean("down", false);

		if (properties.getBoolean("down", false))
			sprite.newAnimation(new Animation(8, 8));
		else
			sprite.newAnimation(new Animation(7, 8));
	}

	@Override
	public Point createSpriteSource() {
		return new Point(7, 8);
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("down", false, "The down (pressed) state of the button.");
		objectData.addProperty("stay_down", false, "Whether the button stays pressed, thus never releasing.");
		
		objectData.addEvent("event_press", "", "Called when the button is pressed.");
		objectData.addEvent("event_release", "", "Called when the button is released.");
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
	public FrameObject clone() {
		return new ObjectButton();
	}
}
