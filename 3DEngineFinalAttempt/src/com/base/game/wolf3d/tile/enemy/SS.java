package com.base.game.wolf3d.tile.enemy;

import com.base.engine.core.Util;
import com.base.engine.rendering.Bitmap;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.SpriteAnimation;
import com.base.game.wolf3d.Wolf3D;
import com.base.game.wolf3d.tile.pickups.Pickup;

public class SS extends BasicEnemy {
	public static final Texture[][] SPRITE_SHEET    = Util.createTextureSheet(new Bitmap("guard.png"), 8, 7, 64, 64, 1);
	public static final DynamicAnimation ANIM_STAND = Util.createAnimationStrip(SPRITE_SHEET, 0, 0, 1, 8);
	public static final DynamicAnimation ANIM_WALK  = Util.createAnimationStrip(SPRITE_SHEET, 0, 1, 4, 8);
	public static final DynamicAnimation ANIM_DIE   = Util.createAnimationStrip(SPRITE_SHEET, 0, 5, 5, 1);
	public static final DynamicAnimation ANIM_PAIN1 = Util.createAnimationStrip(SPRITE_SHEET, 7, 5, 1, 1);
	public static final DynamicAnimation ANIM_PAIN2 = Util.createAnimationStrip(SPRITE_SHEET, 7, 5, 1, 1);
	public static final DynamicAnimation ANIM_DRAW  = Util.createAnimationStrip(SPRITE_SHEET, 0, 6, 2, 1);
	public static final DynamicAnimation ANIM_SHOOT = new DynamicAnimation(new SpriteAnimation(SPRITE_SHEET[2][6], SPRITE_SHEET[1][6]));
	
//	public static final Texture[][] SPRITE_SHEET    = Util.createTextureSheet(new Bitmap("ss.png"), 9, 5, 59, 64, 1);
//	public static final DynamicAnimation ANIM_STAND = Util.createAnimationStrip(SPRITE_SHEET, 0, 0, 1, 8);
//	public static final DynamicAnimation ANIM_WALK  = Util.createAnimationStrip(SPRITE_SHEET, 0, 0, 4, 8);
//	public static final DynamicAnimation ANIM_DIE   = Util.createAnimationStrip(SPRITE_SHEET, 1, 4, 4, 1);
//	public static final DynamicAnimation ANIM_PAIN1 = Util.createAnimationStrip(SPRITE_SHEET, 0, 4, 1, 1);
//	public static final DynamicAnimation ANIM_DRAW  = Util.createAnimationStrip(SPRITE_SHEET, 5, 3, 1, 1);
//	public static final DynamicAnimation ANIM_SHOOT = new DynamicAnimation(new SpriteAnimation(SPRITE_SHEET[7][3], SPRITE_SHEET[6][3]));

	
	public SS() {
		super("Schutzstaffel");

		scoreValue  = 500;
		speed       = 2.0f;
		patrolSpeed = 0.5f;
		drawSpeed   = 5;
		aimDelay    = 0.5f;
		shootDelay  = 0.3f;
		minShots    = 4;
		maxShots    = 4;
		feelsPain   = true;
		drop        = Wolf3D.OBJ_USED_CLIP;
		initHealth(100);

		patrolAnimSpeed = 3;
		chaseAnimSpeed  = 7;
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
	public SS clone() {
		return new SS();
	}
	
    @Override
    public Pickup getDrop() {
    	if (!getPlayer().getWeapon(Wolf3D.WEAPON_MACHINE_GUN).isUnlocked())
    		return Wolf3D.OBJ_WEAPON_MACHINE_GUN;
    	return super.getDrop();
    }
	
	@Override
	public int hitscan(boolean isSprinting, boolean inSight, float dist) {
		return super.hitscan(isSprinting, inSight, dist * (2.0f / 3.0f));
	}
}
