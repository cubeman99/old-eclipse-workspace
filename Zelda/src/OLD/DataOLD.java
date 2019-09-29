package OLD;

import zelda.common.geometry.Point;


public abstract class DataOLD {
	public Point sheetSourcePos;


	public DataOLD(int sx, int sy) {
		this.sheetSourcePos = new Point(sx, sy);
	}
}
