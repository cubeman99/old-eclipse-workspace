package com.base.game.wolf3d.tile.enemy;

import com.base.engine.common.GMath;
import com.base.engine.core.Util;
import com.base.engine.rendering.Bitmap;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.SpriteAnimation;
import com.base.game.wolf3d.Wolf3D;



public class Guard extends BasicEnemy {
	public static final Texture[][] SPRITE_SHEET    = Util.createTextureSheet(new Bitmap("guard.png"), 8, 7, 64, 64, 1);
	public static final DynamicAnimation ANIM_STAND = Util.createAnimationStrip(SPRITE_SHEET, 0, 0, 1, 8);
	public static final DynamicAnimation ANIM_WALK  = Util.createAnimationStrip(SPRITE_SHEET, 0, 1, 4, 8);
	public static final DynamicAnimation ANIM_DIE   = Util.createAnimationStrip(SPRITE_SHEET, 0, 5, 5, 1);
	public static final DynamicAnimation ANIM_PAIN  = Util.createAnimationStrip(SPRITE_SHEET, 7, 5, 1, 1);
	public static final DynamicAnimation ANIM_DRAW  = Util.createAnimationStrip(SPRITE_SHEET, 0, 6, 2, 1);
	public static final DynamicAnimation ANIM_SHOOT = new DynamicAnimation(new SpriteAnimation(SPRITE_SHEET[2][6], SPRITE_SHEET[1][6]));
	
	
	
	public Guard() {
		super("Guard");
		
		animationSpeed   = 7;
		patrolAnimSpeed  = 4;
		chaseAnimSpeed   = 7;
		drawSpeed        = 5;
		range            = 50;
		damageFrameIndex = 0;
		
		scoreValue  = 100;
		speed       = 1.5f;
		patrolSpeed = 0.5f;
		aimDelay    = 0.7f;
		shootDelay  = 1.0f;
		minShots    = 1;
		maxShots    = 1;
		feelsPain   = true;
		drop        = Wolf3D.OBJ_USED_CLIP;
		initHealth(25);
    		
		animStand = ANIM_STAND;
		animWalk  = ANIM_WALK;
		animDie   = ANIM_DIE;
		animPain1 = ANIM_PAIN;
		animPain2 = ANIM_PAIN;
		animDraw  = ANIM_DRAW;
		animShoot = ANIM_SHOOT;
		
		soundAlert = Wolf3D.SOUND_GUARD_ALERT;
		
		sprite.setSpeed(7);
		sprite.newAnimation(true, animStand);
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		setDirection(getData().getInt("direction", 0) * GMath.QUARTER_PI);
		state = getData().getInt("state", state);
		sprite.newAnimation(true, animStand);
	}
	
	@Override
	public Guard clone() {
		return new Guard();
	}
}
