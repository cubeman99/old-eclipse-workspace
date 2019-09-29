package zelda.game.entity.object.dungeon;

import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.projectile.Projectile;
import zelda.game.player.Player;


public abstract class ObjectAbstractSwitch extends FrameObject {
	private int toggleTimer;

	public void toggle() {
		toggleTimer = 20;
		properties.script("event_toggle", this, frame);
		Sounds.OBJECT_SWITCH.play();
	}
	
	public boolean canToggle() {
		return (toggleTimer == 0);
	}
	
	@Override
	public void update() {
		super.update();
		
		Player player = game.getPlayer();
		
		if (toggleTimer > 0)
			toggleTimer--;
		
		if (canToggle() && player.checkSwordHitObject(new Point(position)))
		{
			toggle();
		}
	}
	
	@Override
	public void initialize() {
		toggleTimer = 0;
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid", true);
		objectData.addEvent("event_toggle", "Called when the switch changes toggle state.");
	}
	
	@Override
	public void onHitByProjectile(Projectile proj) {
		super.onHitByProjectile(proj);
		if (canToggle()) {
			toggle();
		}
	}
	
	@Override
	public Point createSpriteSource() {
		return new Point();
	}
}
