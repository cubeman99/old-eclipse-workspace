package zelda.game.entity.object.global;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;
import zelda.game.monster.Monster;
import zelda.game.world.tile.GridTile;


public class ObjectSomariaBlock extends FrameObject {
	
	@Override
	public void initialize() {
		setBreakSprite(Resources.SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_SOMARIA_BLOCK_DESTROY);
		setSpriteEntitySource(4, 8);
		sprite.setSheet(Resources.SHEET_PLAYER_ITEMS);
		spriteEntity.setSheet(Resources.SHEET_PLAYER_ITEMS);
		soundBreak = null;
		

		GridTile t = frame.getGridTile(getLocation());
		
		if ((t != null && (!t.isCoverable() || !t.isSurface()))
				|| Collision.placeMeetingSolid(this, getPosition())
				|| Collision.placeMeeting(this, Monster.class))
		{
			breakObject();
		}
		
		soundBreak = Sounds.EFFECT_POOF;
		
		properties.set("solid",         true);
		properties.set("carriable",     true);
		properties.set("movable",       true);
		properties.set("swordable",     true);
		properties.set("boomerangable", true);
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid",         true);
		objectData.addProperty("carriable",     true);
		objectData.addProperty("movable",       true);
		objectData.addProperty("swordable",     true);
		objectData.addProperty("boomerangable", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(4, 8);
	}

	@Override
	public FrameObject clone() {
		return new ObjectSomariaBlock();
	}
}
