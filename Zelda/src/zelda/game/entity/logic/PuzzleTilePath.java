package zelda.game.entity.logic;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.Colors;
import zelda.game.control.script.ScriptFunctions;
import zelda.game.entity.Entity;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.object.dungeon.color.ObjectColorTile;


public class PuzzleTilePath extends LogicPuzzle {
	private ArrayList<ObjectColorTile> tiles;
	private ObjectColorTile currentTile;
	
	public PuzzleTilePath() {
		super();
		sprite.newAnimation(new Animation(3, 0));
	}
	
	@Override
	public boolean isSolved() {
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).getColor() == Colors.BLUE) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void update() {
		Point loc = game.getPlayer().getStandLocation();
		
		for (int i = 0; i < tiles.size(); i++) {
			ObjectColorTile t = tiles.get(i);
			if (t.getColor() == Colors.BLUE && t.getLocation().equals(loc)
					&& t.getLocation().isAdjacentTo(currentTile.getLocation()))
			{
				t.setColor(Colors.YELLOW);
				currentTile.setColor(Colors.RED);
				currentTile = t;
				Sounds.COLLECT_ITEM.play();
				break;
			}
		}
		
		super.update();
	}
	
	@Override
	public void begin() {
		super.begin();
		
		String startName = properties.get("start_tile", "");
		PropertyHolder holder = ScriptFunctions.getTarget(startName, this, frame);
		if (holder instanceof ObjectColorTile)
			currentTile = (ObjectColorTile) holder;

		tiles = new ArrayList<ObjectColorTile>();
		ArrayList<Entity> entities = frame.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof ObjectColorTile) {
				ObjectColorTile t = (ObjectColorTile) entities.get(i);
				tiles.add(t);
			}
		}
	}
	
	@Override
	public void onFrameBegin() {
		super.onFrameBegin();
		
		if (currentTile != null) {
			currentTile.setColor(Colors.YELLOW);
			Sounds.EFFECT_POOF.play();
			game.addEntity(new Effect(new Sprite(
					Resources.SHEET_SPECIAL_EFFECTS,
					Animations.EFFECT_APPEAR_POOF, false),
					currentTile.getPosition()));
		}
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("start_tile", "");
	}
	
	@Override
	public PuzzleTilePath clone() {
		return new PuzzleTilePath();
	}
}
