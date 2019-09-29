package com.base.game.wolf3d.tile.enemy;

import com.base.engine.core.Util;
import com.base.engine.rendering.Bitmap;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.SpriteAnimation;
import com.base.game.wolf3d.Wolf3D;

public class Officer extends BasicEnemy {
	public static final Texture[][] SPRITE_SHEET    = Util.createTextureSheet(new Bitmap("officer.png"), 12, 5, 64, 64, 1);
	public static final DynamicAnimation ANIM_STAND = Util.createAnimationStrip(SPRITE_SHEET, 0, 0, 1, 8);
	public static final DynamicAnimation ANIM_WALK  = Util.createAnimationStrip(SPRITE_SHEET, 8, 0, 4, 8);
	public static final DynamicAnimation ANIM_DIE   = new DynamicAnimation(new SpriteAnimation(SPRITE_SHEET[5][3], SPRITE_SHEET[6][3], SPRITE_SHEET[7][3], SPRITE_SHEET[9][3], SPRITE_SHEET[10][3]));
	public static final DynamicAnimation ANIM_PAIN1 = Util.createAnimationStrip(SPRITE_SHEET, 4, 3, 1, 1);
	public static final DynamicAnimation ANIM_PAIN2 = Util.createAnimationStrip(SPRITE_SHEET, 8, 3, 1, 1);
	public static final DynamicAnimation ANIM_DRAW  = new DynamicAnimation(new SpriteAnimation(SPRITE_SHEET[11][3], SPRITE_SHEET[0][4]));
	public static final DynamicAnimation ANIM_SHOOT = new DynamicAnimation(new SpriteAnimation(SPRITE_SHEET[1][4], SPRITE_SHEET[0][4]));

	public Officer() {
		super("Officer");

		drawSpeed   = 10;
		scoreValue  = 400;
		speed       = 2.5f;
		patrolSpeed = 0.5f;
		drawSpeed   = 10;
		aimDelay    = 0.4f;
		shootDelay  = 1.0f;
		minShots    = 1;
		maxShots    = 1;
		feelsPain   = true;
		drop        = Wolf3D.OBJ_USED_CLIP;
		initHealth(50);

		patrolAnimSpeed = 4;
		chaseAnimSpeed  = 10;
		animStand = ANIM_STAND;
		animWalk  = ANIM_WALK;
		animDie   = ANIM_DIE;
		animPain1 = ANIM_PAIN1;
		animPain2 = ANIM_PAIN2;
		animDraw  = ANIM_DRAW;
		animShoot = ANIM_SHOOT;
		
		sprite.setSpeed(7);
		sprite.newAnimation(true, animStand);
		
	}
	
	@Override
	public Officer clone() {
		return new Officer();
	}
}
