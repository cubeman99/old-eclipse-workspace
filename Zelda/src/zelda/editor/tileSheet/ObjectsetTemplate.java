package zelda.editor.tileSheet;

import zelda.common.geometry.Point;
import zelda.common.properties.FrameEntityData;
import zelda.common.properties.Properties;
import zelda.game.entity.FrameEntity;
import zelda.game.world.Frame;
import zelda.game.world.tile.FrameTileObject;
import zelda.game.world.tile.ObjectTile;


public class ObjectsetTemplate {
	private Point size;
	private Data[][] data;



	// ================== CONSTRUCTORS ================== //

	public ObjectsetTemplate(int width, int height) {
		this.size = new Point(width, height);
		this.data = new Data[width][height];
	}



	// =================== ACCESSORS =================== //

	public Point getSize() {
		return size;
	}

	public FrameEntity getFrameObject(int x, int y) {
		if (!objectExists(new Point(x, y)))
			return null;
		return data[x][y].frameObject;
	}

	public boolean objectExists(Point sourcePos) {
		return (data[sourcePos.x][sourcePos.y] != null);
	}



	// ==================== MUTATORS ==================== //

	public ObjectTile createTile(Frame frame, Point sourcePos, Point pos) {
		Data td = data[sourcePos.x][sourcePos.y];

		ObjectTile t = new ObjectTile(frame, sourcePos, new Properties(td.properties));
		t.setPosition(pos);
		t.setFrameObject(td.frameObject);
		t.getProperties().set(td.properties);
		
		return t;
	}

	protected Point addObject(FrameEntity obj) {
		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				if (data[x][y] == null) {
					setObject(x, y, obj);
					return new Point(x, y);
				}
			}
		}
		return null;
	}

	protected void setObject(int x, int y, FrameEntity obj) {
		data[x][y] = new Data(x, y);
		data[x][y].frameObject = obj;
		obj.setObjectData(new FrameEntityData(null));
		obj.setup();
	}

	protected void setProperty(int x, int y, String name, Object value) {
		if (data[x][y] != null) {
			data[x][y].properties.set(name, value);
			if (data[x][y].frameObject != null)
				data[x][y].frameObject.getProperties().set(name, value);
		}
	}



	// ================= DATA SUB-CLASS ================= //

	private static class Data {
		public FrameEntity frameObject;
		public Properties properties;

		public Data(int x, int y) {
			frameObject = null;
			properties = new Properties();
		}
	}
}
