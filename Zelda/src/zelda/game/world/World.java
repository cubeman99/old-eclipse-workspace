package zelda.game.world;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.editor.Editor;
import zelda.game.control.GameInstance;


public class World {
	private ArrayList<Level> levels;
	private ArrayList<Dungeon> dungeons;
	private int levelIndex;
	private GameInstance control;

	

	// ================== CONSTRUCTORS ================== //

	public World(GameInstance control) {
		this.control    = control;
		this.levels     = new ArrayList<Level>();
		this.dungeons   = new ArrayList<Dungeon>();
		this.levelIndex = 0;
	}



	// =================== ACCESSORS =================== //

	public Level getCurrentLevel() {
		return levels.get(levelIndex);
	}

	public Frame getCurrentFrame() {
		return getCurrentLevel().getCurrentFrame();
	}

	public GameInstance getControl() {
		return control;
	}

	public int getNumLevels() {
		return levels.size();
	}
	
	public Level getLevel(int index) {
		return levels.get(index);
	}

	public int getNumDungeons() {
		return dungeons.size();
	}
	
	public Dungeon getDungeon(int index) {
		return dungeons.get(index);
	}
	
	public boolean dungeonExists(String name) {
		for (int i = 0; i < dungeons.size(); i++) {
			if (dungeons.get(i).getName().equals(name))
				return true;
		}
		return false;
	}
	
	public ArrayList<Level> getDungeonLevels(Dungeon dungeon) {
		ArrayList<Level> dungeonLevels =  new ArrayList<Level>();
		for (int i = 0; i < levels.size(); i++) {
			Level level = levels.get(i);
			
			if (level.getDungeon() == dungeon) {
				int floor = level.getProperties().getInt("floor", 0);
				for (int j = 0; j < dungeonLevels.size(); j++) {
					int testFloor = dungeonLevels.get(j).getProperties().getInt("floor", 0);
					if (floor <= testFloor) {
						dungeonLevels.add(j, level);
						break;
					}
				}
				if (!dungeonLevels.contains(level))
					dungeonLevels.add(level);
			}
		}
		return dungeonLevels;
	}
	
	public Level getLevel(String levelName) {
		for (int i = 0; i < levels.size(); i++) {
			if (levels.get(i).getName().equals(levelName))
				return levels.get(i);
		}
		return null;
	}



	// ==================== MUTATORS ==================== //

	public void update() {
		getCurrentFrame().update();
	}

	public void setCurrentLevel(int levelIndex) {
		this.levelIndex = levelIndex;
	}

	public void setCurrentLevel(Level level) {
		for (int i = 0; i < levels.size(); i++) {
			if (levels.get(i) == level) {
				setCurrentLevel(i);
				return;
			}
		}
	}

	public boolean removeLevel(Level level) {
		return levels.remove(level);
	}
	
	public Dungeon addDungeon(Dungeon dun) {
		dungeons.add(dun);
		return dun;
	}

	public Level addLevel(String name, int width, int height, Point frameSize) {
		Level lvl = new Level(this, name, width, height, frameSize);
		levels.add(lvl);
		return lvl;
	}

	public void setControl(GameInstance control) {
		this.control = control;
	}
	
	public void save(ObjectOutputStream out) throws IOException {
		out.writeInt(levels.size()); // Number of levels.

		for (int i = 0; i < levels.size(); i++) {
			levels.get(i).save(out);
		}
		
		out.writeInt(dungeons.size()); // Number of dungeons.
		
		for (int i = 0; i < dungeons.size(); i++) {
			dungeons.get(i).save(out);
		}
	}

	public void load(ObjectInputStream in, Editor editor) throws IOException, ClassNotFoundException {
		int numLevels = in.readInt();
		levels.clear();

		for (int i = 0; i < numLevels; i++) {
			Level lvl = new Level(this);
			levels.add(lvl);
			lvl.load(in, editor);
		}
		
		int numDungeons = in.readInt();

		for (int i = 0; i < numDungeons; i++) {
			Dungeon dun = new Dungeon();
			dun.load(in);
			dungeons.add(dun);
		}
	}
}
