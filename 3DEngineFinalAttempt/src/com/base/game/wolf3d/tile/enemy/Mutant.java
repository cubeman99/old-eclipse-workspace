package com.base.game.wolf3d.tile.enemy;

import com.base.engine.core.Util;
import com.base.engine.rendering.Bitmap;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.SpriteAnimation;
import com.base.game.wolf3d.Wolf3D;

public class Mutant extends BasicEnemy {
	public static final Texture[][] SPRITE_SHEET     = Util.createTextureSheet(new Bitmap("mutant.png"), 10, 6, 64, 64, 1);
	public static final DynamicAnimation ANIM_STAND  = Util.createAnimationStrip(SPRITE_SHEET, 0, 0, 1, 8);
	public static final DynamicAnimation ANIM_WALK   = Util.createAnimationStrip(SPRITE_SHEET, 8, 0, 4, 8);
	public static final DynamicAnimation ANIM_DIE    = new DynamicAnimation(new SpriteAnimation(SPRITE_SHEET[1][4], SPRITE_SHEET[2][4], SPRITE_SHEET[3][4], SPRITE_SHEET[5][4], SPRITE_SHEET[6][4]));
	public static final DynamicAnimation ANIM_PAIN1  = Util.createAnimationStrip(SPRITE_SHEET, 0, 4, 1, 1);
	public static final DynamicAnimation ANIM_PAIN2  = Util.createAnimationStrip(SPRITE_SHEET, 4, 4, 1, 1);
	public static final DynamicAnimation ANIM_DRAW   = new DynamicAnimation(new SpriteAnimation(SPRITE_SHEET[7][4]));
	public static final DynamicAnimation ANIM_SHOOT1 = Util.createAnimationStrip(SPRITE_SHEET, 7, 4, 2, 1);
	public static final DynamicAnimation ANIM_SHOOT2 = Util.createAnimationStrip(SPRITE_SHEET, 9, 4, 2, 1);
	
	

	public Mutant() {
		super("Mutant");

		drawSpeed   = 10;
		shootDelay  = 0.5f;
		aimDelay    = 0.0f;
		damageFrameIndex = 1;
		
		scoreValue  = 700;
		speed       = 1.5f;
		patrolSpeed = 0.5f;
		minShots    = 2;
		maxShots    = 2;
		feelsPain   = true;
		drop        = Wolf3D.OBJ_USED_CLIP;
		initHealth(55); // 45/55/55/65 (TODO: difficulty based)

		patrolAnimSpeed = 4;
		chaseAnimSpeed  = 7;
		animStand = ANIM_STAND;
		animWalk  = ANIM_WALK;
		animDie   = ANIM_DIE;
		animPain1 = ANIM_PAIN1;
		animPain2 = ANIM_PAIN2;
		animDraw  = ANIM_DRAW;
		animShoot = ANIM_SHOOT1;
		
		sprite.setSpeed(7);
		sprite.newAnimation(true, animStand);
		
	}
	
	@Override
	public Mutant clone() {
		return new Mutant();
	}
	
	@Override
	public void shoot() {
		if (shotIndex % 2 == 0)
			animShoot = ANIM_SHOOT1;
		if (shotIndex % 2 == 1)
			animShoot = ANIM_SHOOT2;
		super.shoot();
	}
}
