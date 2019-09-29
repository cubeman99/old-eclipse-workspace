package com.base.game.wolf3d.tile.enemy;

import com.base.engine.common.GMath;
import com.base.engine.core.Util;
import com.base.engine.rendering.Bitmap;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.SpriteAnimation;
import com.base.game.wolf3d.Wolf3D;
import com.base.game.wolf3d.tile.pickups.Pickup;

public class Dog extends BasicEnemy {
	public static final Texture[][] SPRITE_SHEET     = Util.createTextureSheet(new Bitmap("dog.png"), 8, 6, 64, 64, 1);
	public static final DynamicAnimation ANIM_STAND  = Util.createAnimationStrip(SPRITE_SHEET, 0, 0, 1, 8);
	public static final DynamicAnimation ANIM_WALK   = Util.createAnimationStrip(SPRITE_SHEET, 0, 0, 4, 8);
	public static final DynamicAnimation ANIM_DIE    = Util.createAnimationStrip(SPRITE_SHEET, 0, 4, 4, 1);
	public static final DynamicAnimation ANIM_BITE   = new DynamicAnimation(new SpriteAnimation(
			SPRITE_SHEET[0][5], SPRITE_SHEET[1][5], SPRITE_SHEET[2][5], SPRITE_SHEET[0][5], SPRITE_SHEET[0][0]));
	
	

	public Dog() {
		super("dog");
		
		scoreValue  = 200;
		range       = 1;
		speed       = 3.0f;
		patrolSpeed = 1.5f;
		drawSpeed   = 10;
		shootDelay  = 0.7f;
		aimDelay    = 0.0f;
		minShots    = 1;
		maxShots    = 1;
		feelsPain   = false;
		drop        = Wolf3D.OBJ_USED_CLIP;
		damageFrameIndex = 2;
		initHealth(1);

		patrolAnimSpeed = 7;
		chaseAnimSpeed  = 10;
		animStand = ANIM_STAND;
		animWalk  = ANIM_WALK;
		animDie   = ANIM_DIE;
		animShoot = ANIM_BITE;
		
		sprite.setSpeed(7);
		sprite.newAnimation(true, animStand);
		
	}
	
	@Override
	public void onDie() {
		super.onDie();
		Wolf3D.SOUND_DOG_DIE.play(this);
	}
	
	@Override
	public Dog clone() {
		return new Dog();
	}
	
	@Override
	public Pickup getDrop() {
		return null;
	}
	
	@Override
	public int calculateDamage() {
		return GMath.random.nextInt(16);
	}
}
