package zelda.game.world;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import zelda.common.geometry.Point;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.Grid;
import zelda.editor.Editor;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;
import zelda.game.zone.Zone;


public class Level implements PropertyHolder {
	private World world;
	private String name;
	private Point frameSize;
	private Grid<Frame> frames;
	private Point location;
	private Properties properties;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Level(World world) {
		this(world, "", 0, 0, new Point());
	}

	public Level(World world, String name, int width, int height, Point frameSize) {
		this.world      = world;
		this.name       = name;
		this.frameSize  = new Point(frameSize);
		this.frames     = new Grid<Frame>(width, height);
		this.location   = new Point(0, 0);
		this.properties = new Properties();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				frames.set(x, y, new Frame(this, x, y));
			}
		}
	}



	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}

	public Point getLocation() {
		return location;
	}

	public Frame getCurrentFrame() {
		return frames.get(location);
	}

	public Point getFrameSize() {
		return frameSize;
	}

	public World getWorld() {
		return world;
	}

	public Point getSize() {
		return frames.getSize();
	}
	
	public Dungeon getDungeon() {
		String dungeonName = properties.get("dungeon", "");
		for (int i = 0; i < world.getNumDungeons(); i++) {
			Dungeon dun = world.getDungeon(i);
			if (dun.getName().equals(dungeonName))
				return dun;
		}
		return null;
	}

	public Frame getFrame(int x, int y) {
		return frames.get(x, y);
	}

	public Frame getFrame(Point loc) {
		return frames.get(loc);
	}

	public boolean frameExists(int x, int y) {
		return frames.contains(x, y);
	}

	public boolean frameExists(Point loc) {
		return frames.contains(loc);
	}

	public Frame getAbsoluteFrame(Point absoluteLoc) {
		return getFrame(absoluteLoc.dividedBy(frameSize));
	}

	public Point getRelativeLocation(Point absoluteLoc) {
		return absoluteLoc.mod(frameSize);
	}

	public boolean absoluteFrameExists(Point absoluteLoc) {
		return frameExists(absoluteLoc.dividedBy(frameSize));
	}

	public boolean gridTileExists(Point loc) {
		if (!absoluteFrameExists(loc))
			return false;
		return getAbsoluteFrame(loc).getTileRect().contains(
				getRelativeLocation(loc));
	}

	public ObjectTile getObjectTile(String id) {
		for (int x = 0; x < frames.getWidth(); x++) {
			for (int y = 0; y < frames.getHeight(); y++) {
				Frame frame = frames.get(x, y);
				if (frame != null) {
					ObjectTile t = frame.getObjectTile(id);
					if (t != null)
						return t;
				}
			}
		}
		return null;
	}

	public GridTile getGridTile(String id) {
		for (int x = 0; x < frames.getWidth(); x++) {
			for (int y = 0; y < frames.getHeight(); y++) {
				Frame frame = frames.get(x, y);
				if (frame != null) {
					GridTile t = frame.getGridTile(id);
					if (t != null)
						return t;
				}
			}
		}
		return null;
	}



	// ==================== MUTATORS ==================== //

	public void nextFrame(Point relativePos) {
		location.add(relativePos);
		// Point nextFramePos = location.plus(relativePos);
		//
		// if (frameExists(nextFramePos)) {
		// getCurrentFrame().leave();
		// getWorld().getControl().leaveFrame();
		// location.set(nextFramePos);
		// getCurrentFrame().enter();
		//
		// return true;
		// }
		//
		// return false;
	}

	public void resize(int width, int height) {
		frames.setSize(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (frames.get(x, y) == null) {
					frames.set(x, y, new Frame(this, x, y));
				}
			}
		}
	}

	public void setZone(Zone zone) {
		for (int x = 0; x < frames.getWidth(); x++) {
			for (int y = 0; y < frames.getHeight(); y++) {
				if (frames.get(x, y) != null) {
					frames.get(x, y).setZone(zone);
				}
			}
		}
	}

	public void setCurrentFrame(Frame frame) {
		for (int x = 0; x < frames.getWidth(); x++) {
			for (int y = 0; y < frames.getHeight(); y++) {
				if (frames.get(x, y) == frame) {
					location.set(x, y);
					return;
				}
			}
		}
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void save(ObjectOutputStream out) throws IOException {
		out.writeInt(frames.getWidth()); // width in frames
		out.writeInt(frames.getHeight()); // height in frames
		out.writeInt(frameSize.x); // Frame width
		out.writeInt(frameSize.y); // Frame height
		out.writeObject(name); // Level name
		out.writeObject(properties); // Properties
		
		for (int x = 0; x < frames.getWidth(); x++) {
			for (int y = 0; y < frames.getHeight(); y++) {
				frames.get(x, y).save(out);
			}
		}
	}

	public void load(ObjectInputStream in, Editor editor) throws IOException,
			ClassNotFoundException {
		frames     = new Grid<Frame>(in.readInt(), in.readInt());
		frameSize  = new Point(in.readInt(), in.readInt());
		name       = (String) in.readObject();
		properties = (Properties) in.readObject();
		
		for (int x = 0; x < frames.getWidth(); x++) {
			for (int y = 0; y < frames.getHeight(); y++) {
				Frame frame = new Frame(this, x, y);
				frames.set(x, y, frame);
				frame.load(in, editor);
			}
		}
	}
	
	/** CUSTOM C++ PORTING SAVE METHOD. **/
	public void saveCPP(OutputStream out) throws IOException {
		out.write(frames.getWidth());
		out.write(frames.getHeight());

		for (int y = 0; y < frames.getHeight(); y++)
    	{
    		for (int x = 0; x < frames.getWidth(); x++)
    		{
				Frame frame = frames.get(x, y);
				frame.saveCPP(out);
			}
		}
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public void onChangeProperty(Property p) {
		if (p.hasName("dungeon")) {
			if (!world.dungeonExists(p.getString())) {
				System.out.println("Added Dungeon \"" + p.get() + "\"");
				world.addDungeon(new Dungeon(p.getString()));
			}
		}
	}
}
