package zelda.game.world;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.Grid;
import zelda.editor.Editor;
import zelda.game.control.GameInstance;
import zelda.game.entity.Entity;
import zelda.game.entity.object.dungeon.ObjectChest;
import zelda.game.entity.object.global.ObjectReward;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;
import zelda.game.zone.Zone;


public class Frame implements PropertyHolder {
	public static final Point SIZE_SMALL = new Point(10, 8);
	public static final Point SIZE_LARGE = new Point(15, 11);

	private Level level;
	private Point size;
	private Point location;
	private Zone zone;
	private Properties properties;
	private boolean beginning;
	
	private Grid<GridTile> tileGrid;
	private ArrayList<ObjectTile> tileObjects;
	private ArrayList<Entity> entities;
	


	// ================== CONSTRUCTORS ================== //

	public Frame(Level level, int locationX, int locationY) {
		this.level      = level;
		this.size       = new Point(level.getFrameSize());
		this.location   = new Point(locationX, locationY);
		this.zone       = Resources.zones.getZone(0);
		this.properties = new Properties();
		
		this.beginning   = false;
		this.tileGrid    = new Grid<GridTile>(size);
		this.tileObjects = new ArrayList<ObjectTile>();
		this.entities    = new ArrayList<Entity>();

		properties.define("id", 0);
		properties.define("zone", zone.getIndex());
		// properties.define("transition0", "push");
		// properties.define("transition0_warp_level", "");
		// properties.define("transition0_warp_point", "");
	}



	// =================== ACCESSORS =================== //

	public GridTile getGridTile(int x, int y) {
		return tileGrid.get(x, y);
	}

	public GridTile getGridTile(Point location) {
		return tileGrid.get(location);
	}

	public GridTile getGridTile(String id) {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				GridTile t = tileGrid.get(x, y);
				if (t != null && t.getProperties().existEquals("id", id))
					return t;
			}
		}
		return null;
	}
	
	public ObjectTile getObjectTile(String id) {
		for (int i = 0; i < tileObjects.size(); i++) {
			ObjectTile t = tileObjects.get(i);
			if (t != null && t.getProperties().existEquals("id", id))
				return t;
		}
		return null;
	}
	
	public boolean hasTreasure() {
		for (int i = 0; i < tileObjects.size(); i++) {
    		ObjectTile t = tileObjects.get(i);
    		if ((t.getFrameObject() instanceof ObjectChest
    				&& !t.getProperties().getBoolean("opened", false))
    				|| (t.getFrameObject() instanceof ObjectReward
    				&& t.isEnabled()))
    		{
    			return true;
    		}
    	}
		return false;
	}

	public ArrayList<ObjectTile> getObjectTiles() {
		return tileObjects;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public int getWidth() {
		return size.x;
	}

	public int getHeight() {
		return size.y;
	}
	
	public boolean hasBegun() {
		return !beginning;
	}
	
	public Point getSize() {
		return size;
	}

	public Point getLocation() {
		return location;
	}

	public Level getLevel() {
		return level;
	}

	public boolean contains(Point p) {
		return (p.x >= 0 && p.y >= 0 && p.x < size.x && p.y < size.y);
	}

	public Rectangle getTileRect() {
		return new Rectangle(0, 0, size.x, size.y);
	}

	public Rectangle getRect() {
		return new Rectangle(0, 0, size.x * 16, size.y * 16);
	}

	public Vectangle getVect() {
		return new Vectangle(0, 0, size.x * 16, size.y * 16);
	}

	public Zone getZone() {
		return zone;
	}

	public boolean isInside(Vector v) {
		return getVect().contains(v);
	}

	public boolean isOutside(Vector v) {
		return !getVect().contains(v);
	}

	public GameInstance getGame() {
		return level.getWorld().getControl();
	}

	private ArrayList<GridTile> getGridTiles() {
		ArrayList<GridTile> gridTiles = new ArrayList<GridTile>();

		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tileGrid.get(x, y) != null)
					gridTiles.add(tileGrid.get(x, y));
			}
		}

		return gridTiles;
	}



	// ==================== MUTATORS ==================== //

	public Entity addEntity(Entity e) {
		return addEntity(getGame(), e);
	}

	public Entity addEntity(GameInstance game, Entity e) {
		entities.add(e);
		e.setFrame(this);
		e.setGame(game);
		return e;
	}

	public boolean removeEntity(Entity e) {
		return entities.remove(e);
	}

	public void addObjectTile(ObjectTile t) {
		tileObjects.add(t);
	}

	public boolean removeObjectTile(ObjectTile t) {
		return tileObjects.remove(t);
	}

	public void setGridTile(GridTile t) {
		tileGrid.set(t.getLocation(), t);
	}

	public void setZone(Zone zone) {
		this.zone = zone;
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tileGrid.get(x, y) != null) {
					tileGrid.get(x, y).getSprite()
							.setSheet(tileGrid.get(x, y).getImageSheet());
					properties.set("zone", zone.getIndex());
				}
			}
		}
	}

	public void setZone(String zoneName) {
		ArrayList<Zone> zones = Resources.zones.getZones();
		for (int i = 0; i < zones.size(); i++) {
			if (zones.get(i).getName().equals(zoneName)) {
				setZone(zones.get(i));
				return;
			}
		}
	}

	public void update() {
		if (beginning) {
			beginning = false;
			Dungeon dun = level.getDungeon();
			if (dun != null && dun.hasCompass() && hasTreasure())
				Sounds.TUNE_TREASURE.play();
			for (int i = 0; i < tileObjects.size(); i++)
				tileObjects.get(i).onFrameBegin();
		}
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tileGrid.get(x, y) != null)
					tileGrid.get(x, y).update();
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!e.isHidden()) {
				e.preUpdate();
				e.update();
				e.postUpdate();
			}
			if (e.isDestroyed())
				entities.remove(i--);
		}
	}

	public void enter() {
		beginning = true;
		properties.set("explored", true);
		getGame().getView().setCanvas(this);
		
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tileGrid.get(x, y) != null)
					tileGrid.get(x, y).onEnterFrame();
			}
		}
		for (int i = 0; i < tileObjects.size(); i++)
			tileObjects.get(i).onEnterFrame();
		
		for (int i = 0; i < tileObjects.size(); i++)
			tileObjects.get(i).onPostEnter();
	}

	public void leave() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tileGrid.get(x, y) != null)
					tileGrid.get(x, y).onLeaveFrame();
			}
		}
		for (int i = 0; i < tileObjects.size(); i++)
			tileObjects.get(i).onLeaveFrame();

		for (int i = 0; i < entities.size(); i++)
			entities.get(i).setDestroyed(true);
		entities.clear();
	}


	public void drawGridTiles() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tileGrid.get(x, y) != null)
					tileGrid.get(x, y).draw();
			}
		}
	}

	public void drawObjectTiles() {
		for (int i = 0; i < tileObjects.size(); i++)
			tileObjects.get(i).draw();
	}

	public void drawEntities() {
		// Create a list of sorted entities.
		ArrayList<Entity> entitiesSorted = new ArrayList<Entity>();
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isHidden())
				entitiesSorted.add(entities.get(i));
		}
		Collections.sort(entitiesSorted);

		// Draw all entities in order of depth.
		for (int i = entitiesSorted.size() - 1; i >= 0; i--)
			entitiesSorted.get(i).preDraw();
		for (int i = entitiesSorted.size() - 1; i >= 0; i--)
			entitiesSorted.get(i).draw();
		for (int i = entitiesSorted.size() - 1; i >= 0; i--)
			entitiesSorted.get(i).postDraw();
	}

	public void draw() {
		drawGridTiles();
		drawObjectTiles();
		drawEntities();
	}

	public void save(ObjectOutputStream out) throws IOException {
		out.writeObject(properties);

		ArrayList<GridTile> gridTiles = getGridTiles();

		// Save Grid Tiles.
		out.writeInt(gridTiles.size());
		for (int i = 0; i < gridTiles.size(); i++) {
			GridTile t = gridTiles.get(i);

			out.writeObject(t.getLocation());
			out.writeInt(t.getTilesetTemplate().getIndex());
			out.writeObject(t.getSourcePosition());
			out.writeObject(t.getProperties());
		}

		// Save Object Tiles.
		out.writeInt(tileObjects.size());
		for (int i = 0; i < tileObjects.size(); i++) {
			ObjectTile t = tileObjects.get(i);

			out.writeObject(t.getPosition());
			out.writeObject(t.getSourcePosition());
			out.writeObject(t.getSaveProperties());
		}
	}

	public void load(ObjectInputStream in, Editor editor) throws IOException,
			ClassNotFoundException {
		size = new Point(level.getFrameSize());
		properties = (Properties) in.readObject();
		zone = Resources.zones.getZone(properties.getInt("zone", 0));

		// Read Grid Tiles.
		int numGridTiles = in.readInt();
		for (int i = 0; i < numGridTiles; i++) {
			Point pos = (Point) in.readObject();
			int templateIndex = in.readInt();
			Point sourcePos = (Point) in.readObject();
			Properties p = (Properties) in.readObject();

			GridTile t = editor.tileset.newTile(templateIndex, this, sourcePos, pos);
			t.setProperties(p);

			tileGrid.set(pos, t);
		}

		// Read Object Tiles.
		int numObjectTiles = in.readInt();
		for (int i = 0; i < numObjectTiles; i++) {
			Point pos = (Point) in.readObject();
			Point sourcePos = (Point) in.readObject();
			Properties p = (Properties) in.readObject();

			ObjectTile t = editor.objectset.newTile(this, sourcePos, pos);
			t.setProperties(p);
//			t.linkProperties();
			addObjectTile(t);
			
			for (int j = 0; j < p.getNumProperties(); j++)
				t.onChangeProperty(p.getProperty(j));
		}
	}
	
	/** CUSTOM C++ PORTING SAVE METHOD. **/
	public void saveCPP(OutputStream out) throws IOException {
		out.write(size.x);
		out.write(size.y);
		
		for (int y = 0; y < size.y; y++)
		{
			for (int x = 0; x < size.x; x++)
			{
				GridTile t = tileGrid.get(x, y);
				
				if (t != null)
				{
					out.write(t.getTilesetTemplate().getIndex() + 1);
					out.write(t.getSourcePosition().x);
					out.write(t.getSourcePosition().y);
				}
				else
					out.write(0);
			}
		}
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void onChangeProperty(Property p) {
		// TODO
	}

	@Override
	public Properties getProperties() {
		return properties;
	}
}
