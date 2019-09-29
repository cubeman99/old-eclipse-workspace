package zelda.game.entity.seeds;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Sprite;
import zelda.common.util.GMath;
import zelda.game.entity.Entity;


public class ScentPod extends Entity {
	private static final int FADE_TIME = GMath.seconds(4); // Fade after 4
														   // seconds
	private static final int TIME = GMath.seconds(5); // Destroy after 5 seconds

	private Vector position;
	private Sprite sprite;
	private int timer;


	public ScentPod(Vector position) {
		this.position = new Vector(position);

		timer = 0;
		sprite = new Sprite(Resources.SPRITE_SCENT_POD);
		
		Sounds.ITEM_SCENT_POD.play();
	}

	@Override
	public void update() {
		sprite.update();
		timer++;

		// Start flickering after a certain time.
		if (timer == FADE_TIME)
			sprite.getAnimation().createFlicker();

		// Destroy after the time is up.
		if (timer > TIME) {
			destroy();
		}
	}

	@Override
	public void draw() {
		sprite.draw(position);
	}
}
