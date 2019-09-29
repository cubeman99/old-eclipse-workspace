package zelda.game.entity.logic;

import java.util.ArrayList;
import zelda.common.graphics.Animation;
import zelda.common.properties.FrameEntityData;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.Colors;
import zelda.common.util.GMath;
import zelda.game.control.script.ScriptFunctions;
import zelda.game.entity.Entity;
import zelda.game.entity.object.dungeon.color.ObjectColorTile;
import zelda.game.monster.Monster;
import zelda.game.monster.jumpMonster.MonsterColorGel;

public class PuzzleColoredGel extends LogicPuzzle {
	private ArrayList<ObjectColorTile> tiles;
	private PropertyHolder colorHolder;
	private int color;
	
	
	public PuzzleColoredGel() {
		super();
		sprite.newAnimation(new Animation(0, 0));
	}
	
	public void changeColor(int color) {
		this.color = color;
	}
	
	@Override
	public void solve() {
		super.solve();
		objectData.getSource().getProperties().set("solved", true);
	}
	
	@Override
	public boolean isSolved() {
		return (game.getInstanceNumber(Monster.class) == 0);
	}
	
	@Override
	public void update() {
		if (colorHolder != null)
			color = colorHolder.getProperties().getInt("color", color);
		
		ArrayList<ObjectColorTile> oldTiles = new ArrayList<ObjectColorTile>();
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).getColor() != color)
				oldTiles.add(tiles.get(i));
		}
		for (int i = 0; oldTiles.size() > 0 && i < 2; i++) {
			ObjectColorTile t = oldTiles.get(GMath.random
					.nextInt(oldTiles.size()));
			t.setColor(color);
		}
		
		super.update();
	}
	
	@Override
	public void begin() {
		super.begin();
		
		String tileName = properties.get("color_tile", "");
		colorHolder = ScriptFunctions.getTarget(tileName, this, frame);
		
		tiles = new ArrayList<ObjectColorTile>();
		ArrayList<Entity> entities = frame.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof ObjectColorTile) {
				ObjectColorTile t = (ObjectColorTile) entities.get(i);
				tiles.add(t);
			}
		}
		
		if (!properties.getBoolean("solved", false)) {
    		for (int i = 0; i < 5; i++) {
    			MonsterColorGel gel = new MonsterColorGel();
    			gel.setGame(game);
    			gel.setFrame(frame);
    			gel.setObjectData(new FrameEntityData(null));
    			gel.setup();
    			gel.getPosition().set((4 + (i * 2)) * 16, 3 * 16);
    			game.addEntity(gel);
    			gel.begin();
    		}
		}
	}
	
	@Override
	public void setup() {
		super.setup();
		
		color = Colors.RED;
		objectData.addProperty("color_tile", "");
	}
	
	@Override
	public PuzzleColoredGel clone() {
		return new PuzzleColoredGel();
	}
}
