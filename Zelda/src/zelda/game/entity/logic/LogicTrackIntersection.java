package zelda.game.entity.logic;

import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.Direction;
import zelda.common.util.Rail;
import zelda.game.control.script.Function;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;


public class LogicTrackIntersection extends LogicEntity {
	private GridTile trackTile;
	private Point location;
	
	
	public LogicTrackIntersection() {
		super();
		sprite.newAnimation(new Animation(3, 2));
	}
	
	public void toggle() {
		if (trackTile != null) {
			int type = trackTile.getProperties().getInt("track_type", -1);
			
			for (int i = 0; i < 2; i++) {
				int dir   = (type - 1 + i) % 4;
				Point loc = location.plus(Direction.getDirPoint(dir));
				
				if (frame.contains(loc)) {
					GridTile t    = frame.getGridTile(loc);
					int checkType = t.getProperties().getInt("track_type", -1);
					
					if (Rail.hasInput(checkType, dir)) {
						int newType = (i == 0 ? (type - 2) : dir);
						newType     = (newType % 4) + 3;
						trackTile.getProperties().set("track_type", newType);
						trackTile.setAnimation(new Animation(
								Rail.TRACK_SOURCES[newType]));
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void begin() {
		super.begin();
		
		location = ((ObjectTile) objectData.getSource()).getLocation();
		
		frame.contains(location);
		trackTile = frame.getGridTile(location);
		if (trackTile != null) {
			int type = trackTile.getProperties().getInt("track_type", -1);
			if (type < 3 || type > 6)
				trackTile = null;
		}
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addFunction(new Function("toggle") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				toggle();
				return "";
			}
		});
	}
	
	@Override
	public LogicTrackIntersection clone() {
		return new LogicTrackIntersection();
	}
}
