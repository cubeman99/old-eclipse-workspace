package OLD;

import zelda.common.geometry.Point;
import zelda.game.world.Frame;
import zelda.game.zone.ZoneSheetType;


public class ZoneSheetOLD extends SpriteSheetOLD {
	public static final ZoneSheetType TILE_SHEET = new ZoneSheetType(0,
			"tileSheet", 0);
	public static final ZoneSheetType OBJECT_SHEET = new ZoneSheetType(1,
			"objectSheet", 0);
	public static final ZoneSheetType ZONE_SHEET = new ZoneSheetType(2,
			"zoneSheet", 1);
	public static final ZoneSheetType SHEETS[] = {TILE_SHEET, OBJECT_SHEET,
			ZONE_SHEET};

	private Frame frame;
	private ZoneSheetType sheetType;



	// ================== CONSTRUCTORS ================== //

	public ZoneSheetOLD(Frame frame, ZoneSheetType sheetType) {
		this.frame = frame;
		this.sheetType = sheetType;
	}



	// =================== ACCESSORS =================== //

	@Override
	public SpriteOLD getSprite(int sx, int sy) {
		return null;
		// return frame.getZone().getSheet(sheetType).getSprite(sx, sy);
	}

	@Override
	public SpriteOLD getSprite(Point sp) {
		return getSprite(sp.x, sp.y);
	}



	// ==================== MUTATORS ==================== //


}
