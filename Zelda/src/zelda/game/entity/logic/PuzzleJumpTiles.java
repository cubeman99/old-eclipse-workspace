package zelda.game.entity.logic;

import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.util.Colors;
import zelda.game.entity.Entity;
import zelda.game.entity.object.dungeon.color.ObjectJumpTile;
import zelda.game.world.tile.GridTile;


public class PuzzleJumpTiles extends LogicPuzzle {
	
	public PuzzleJumpTiles() {
		super();
		sprite.newAnimation(new Animation(1, 0));
	}
	
	private String getColorLetter(int color) {
		if (color == Colors.RED)
			return "R";
		if (color == Colors.BLUE)
			return "B";
		if (color == Colors.YELLOW)
			return "Y";
		return "";
	}
	
	private int getSortIndex(Point loc) {
		return ((loc.y * frame.getWidth()) + loc.x);
	}
	
	@Override
	public boolean isSolved() {
		ArrayList<ObjectJumpTile> tiles = new ArrayList<ObjectJumpTile>();

		ArrayList<Entity> entities = game.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof ObjectJumpTile) {
				ObjectJumpTile t = (ObjectJumpTile) entities.get(i);
				int index = getSortIndex(t.getLocation());
				
				for (int j = 0; j < tiles.size(); j++) {
					int testIndex = getSortIndex(tiles.get(j).getLocation());
					if (index < testIndex) {
						tiles.add(j, t);
						break;
					}
				}
				
				if (!tiles.contains(t))
					tiles.add(t);
			}
		}
		
		String response = "";
		for (int i = 0; i < tiles.size(); i++)
			response += getColorLetter(tiles.get(i).getColor());
		
		return response.equals(properties.get("solution", ""));
	}
	
	@Override
	public void begin() {
		super.begin();
		
		String solution = properties.get("solution", "");
		if (solution.isEmpty()) {
			// Generate the solution.
			for (int y = 0; y < frame.getHeight(); y++) {
				for (int x = 0; x < frame.getWidth(); x++) {
					GridTile t = frame.getGridTile(x, y);
					Point loc = t.getSourcePosition();
					if (loc.y == 6) {
						if (loc.x == 3)
							solution += "R";
						else if (loc.x == 4)
							solution += "B";
						else if (loc.x == 5)
							solution += "Y";
					}
				}
			}
			properties.set("solution", solution);
		}
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("solution", "");
	}
	
	@Override
	public PuzzleJumpTiles clone() {
		return new PuzzleJumpTiles();
	}
}
