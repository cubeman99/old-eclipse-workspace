package zelda.editor.tileSheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.graphics.TileAnimations;
import zelda.common.properties.Properties;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.CollisionModel;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;
import zelda.game.zone.Zone;
import zelda.game.zone.ZoneSheet;


public class TilesetTemplate {
	protected static final CollisionModel MODEL_BLOCK = new CollisionModel(
			new CollisionBox(0, 0, 16, 16));

	protected static final CollisionModel MODEL_EDGE_E = new CollisionModel(
			new CollisionBox(8, 0, 8, 16));
	protected static final CollisionModel MODEL_EDGE_N = new CollisionModel(
			new CollisionBox(0, 0, 16, 7));
	protected static final CollisionModel MODEL_EDGE_W = new CollisionModel(
			new CollisionBox(0, 0, 8, 16));
	protected static final CollisionModel MODEL_EDGE_S = new CollisionModel(
			new CollisionBox(0, 8, 16, 8));

	protected static final CollisionModel MODEL_DOORWAY = new CollisionModel(
			new CollisionBox(0, 0, 16, 6));

	protected static final CollisionModel MODEL_CORNER_NE = new CollisionModel(
			new CollisionBox(8, 0, 8, 16), new CollisionBox(0, 0, 8, 7));
	protected static final CollisionModel MODEL_CORNER_NW = new CollisionModel(
			new CollisionBox(0, 0, 8, 8), new CollisionBox(8, 0, 8, 7));
	protected static final CollisionModel MODEL_CORNER_SW = new CollisionModel(
			new CollisionBox(0, 8, 16, 8), new CollisionBox(0, 0, 8, 8));
	protected static final CollisionModel MODEL_CORNER_SE = new CollisionModel(
			new CollisionBox(0, 8, 16, 8), new CollisionBox(8, 0, 8, 8));

	protected static final CollisionModel MODEL_INSIDE_CORNER_NE = new CollisionModel(
			new CollisionBox(8, 0, 8, 7));
	protected static final CollisionModel MODEL_INSIDE_CORNER_NW = new CollisionModel(
			new CollisionBox(0, 0, 8, 7));
	protected static final CollisionModel MODEL_INSIDE_CORNER_SW = new CollisionModel(
			new CollisionBox(0, 8, 8, 8));
	protected static final CollisionModel MODEL_INSIDE_CORNER_SE = new CollisionModel(
			new CollisionBox(8, 8, 8, 8));
	
	
	protected static final CollisionModel MODEL_BRIDGE_H_TOP = new CollisionModel(
			new CollisionBox(0, 0, 16, 4));
	protected static final CollisionModel MODEL_BRIDGE_H_BOTTOM = new CollisionModel(
			new CollisionBox(0, 13, 16, 3));
	protected static final CollisionModel MODEL_BRIDGE_H = new CollisionModel(
			new CollisionBox(0, 0, 16, 4), new CollisionBox(0, 13, 16, 3));

	protected static final CollisionModel MODEL_BRIDGE_V_LEFT = new CollisionModel(
			new CollisionBox(0, 0, 4, 16));
	protected static final CollisionModel MODEL_BRIDGE_V_RIGHT = new CollisionModel(
			new CollisionBox(12, 0, 4, 16));
	protected static final CollisionModel MODEL_BRIDGE_V = new CollisionModel(
			new CollisionBox(0, 0, 4, 16), new CollisionBox(12, 0, 4, 16));

	protected static final CollisionModel MODEL_NORMAL = new CollisionModel(
			new CollisionBox(4, 1, 9, 8));


	protected int index;
	protected Point size;
	protected Data[][] data;
	protected Point defaultTile;
	protected ZoneSheet zoneSheet;



	// ================== CONSTRUCTORS ================== //

	public TilesetTemplate(int width, int height, String name,
			String... zoneNames) {
		size = new Point(width, height);
		data = new Data[width][height];
		defaultTile = new Point(0, 0);
		zoneSheet = new ZoneSheet(name, zoneNames);
		index = -1;

		ArrayList<Zone> zones = Resources.zones.getZones();
		int nameIndex = 0;
		for (int i = 0; i < zones.size() && nameIndex < zoneNames.length; i++) {
			if (zones.get(i).getName().equals(zoneNames[nameIndex])) {
				nameIndex++;
				zones.get(i).setDefaultTemplate(this);
			}
		}

		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				data[x][y] = new Data(x, y);
			}
		}
	}



	// =================== ACCESSORS =================== //

	public Point getSize() {
		return size;
	}

	public int getIndex() {
		return index;
	}

	public Point getDefaultTileSourcePos() {
		return defaultTile;
	}



	// ==================== MUTATORS ==================== //

	public GridTile createTile(Frame frame, Point sourcePos, Point loc) {
		Data td = data[sourcePos.x][sourcePos.y];

		GridTile t = new GridTile(frame, loc, sourcePos, new Properties(
				td.properties));

		t.setAnimation(td.animation);
		t.setCollisionModel(new CollisionModel(td.collisionModel));
		t.setSheet(td.sheet);
		t.setTilesetTemplate(this);

		return t;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	protected void setZoneSheet(String name, String... zoneNames) {
		zoneSheet = new ZoneSheet(name, zoneNames);
	}

	protected void setAnimation(int x, int y, Animation anim) {
		data[x][y].animation = anim;
		data[x][y].sheet = Resources.zones.areaSheet;
	}

	protected void setModel(int x, int y, CollisionModel model) {
		data[x][y].collisionModel = model;
		data[x][y].properties.set("solid", true);
	}

	protected void setModelLedge(int x, int y, CollisionModel model,
			int ledgeDir) {
		data[x][y].collisionModel = model;
		data[x][y].properties.set("solid", true);
		data[x][y].properties.set("ledge", true);
		data[x][y].properties.set("ledge_dir", ledgeDir);
	}

	protected void setProperty(int x, int y, String name, String value) {
		data[x][y].properties.set(name, value);
	}

	protected void setProperty(int x, int y, String name, boolean value) {
		data[x][y].properties.set(name, value);
	}

	protected void setProperty(int x, int y, String name, int value) {
		data[x][y].properties.set(name, value);
	}

	protected void setProperty(int x, int y, String name, double value) {
		data[x][y].properties.set(name, value);
	}

	protected void loadDataMap(String filename) {
		try {
			Scanner reader = new Scanner(new File(filename));

			for (int y = 0; y < size.y && reader.hasNextLine(); y++) {
				String line = reader.nextLine();


				for (int x = 0; x < size.x && x < line.length(); x++) {
					String c = line.substring(x, x + 1);

					if (!c.equals(" ")) {
						Data td = data[x][y];

						// S = Solid
						if (c.equals("S")) {
							td.collisionModel = MODEL_BLOCK;
							td.properties.set("solid", true);
						}

						// A = Half-Solid
						else if (c.equals("A")) {
							td.properties.set("half_solid", true);
						}

						// L = Ledge
						else if (c.equals("L")) {
							td.collisionModel = MODEL_BLOCK;
							td.properties.set("ledge", true);
							td.properties.set("solid", true);
						}

						// G = Digable Ground
						else if (c.equals("G")) {
							td.properties.set("ground", true);
						}

						// H = hole
						else if (c.equals("H")) {
							td.collisionModel = MODEL_NORMAL;
							td.properties.set("ground", false);
							td.properties.set("hole", true);
						}

						// V = Lava
						else if (c.equals("V")) {
							td.collisionModel = MODEL_NORMAL;
							td.properties.set("lava", true);
							td.sheet     = Resources.zones.areaSheet;
							td.animation = TileAnimations.LAVA_BASIC;
						}

						// W = Water
						else if (c.equals("W")) {
							td.collisionModel = MODEL_NORMAL;
							td.properties.set("water", true);
							// td.setAnimation(5, 0, 4, 0.07);
						}

						// O = Ocean
						else if (c.equals("O")) {
							td.collisionModel = MODEL_NORMAL;
							td.properties.set("water", true);
							td.properties.set("ocean", true);
							// td.setAnimation(5, 15, 4, 0.07);
						}

						// F = Water Fall
						else if (c.equals("F")) {
							td.collisionModel = MODEL_BLOCK;
							td.properties.set("ledge", true);
							td.properties.set("waterfall", true);
						}

						// I = Ice
						else if (c.equals("I")) {
							td.collisionModel = MODEL_NORMAL;
							td.properties.set("ice", true);
						}

						// R = Stairs
						else if (c.equals("R")) {
							td.collisionModel = MODEL_NORMAL;
							td.properties.set("stairs", true);
						}

						// P = Puddle
						else if (c.equals("P")) {
							td.collisionModel = MODEL_NORMAL;
							td.properties.set("puddle", true);
							// td.setAnimation(5, 1, 4, 0.07);
						}
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}



	// ================= DATA SUB-CLASS ================= //

	private class Data {
		public Animation animation;
		public CollisionModel collisionModel;
		public Properties properties;
		public ZoneSheet sheet;

		public Data(int x, int y) {
			properties = new Properties();
			animation = new Animation(x, y);
			collisionModel = null;
			sheet = zoneSheet;
		}
	}
}
