package projects.towerDefense;

import java.awt.Color;
import java.util.ArrayList;
import projects.towerDefense.entity.Entity;
import projects.towerDefense.panel.Panel;
import projects.towerDefense.tile.Path;
import projects.towerDefense.tile.Tile;
import projects.towerDefense.tile.TileGround;
import projects.towerDefense.tile.Tower;
import cmg.graphics.Draw;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.Direction;
import cmg.math.GMath;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;

public class Level extends Panel {
	private Point size;
	private Tile[][] tiles;
	private CreepPath path;
	private Point cursor;
	private ArrayList<Entity> entities;
	
	

	// ================== CONSTRUCTORS ================== //
	
	public Level(GameInstance game, int width, int height) {
		super(game);
		size      = new Point(width, height);
		tiles     = new Tile[size.x][size.y];
		path      = new CreepPath();
		cursor    = new Point(0, 0);
		entities  = new ArrayList<Entity>();
		
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				setTile(new Point(x, y), new TileGround());
			}
		}
	}

	
	
	// =================== ACCESSORS =================== //
	
	public Point getMouseLocation() {
		Vector ms = getMousePosition().scaledByInv(Tile.SIZE);
		return new Point(GMath.floor(ms.x), GMath.floor(ms.y));
	}
	
	public GameInstance getGame() {
		return game;
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public boolean contains(Point loc) {
		return (loc.x >= 0 && loc.y >= 0 && loc.x < size.x && loc.y < size.y);
	}
	
	public Tile getTile(Point loc) {
		return tiles[loc.x][loc.y];
	}
	
	public Point getSize() {
		return size;
	}
	
	public int getWidth() {
		return size.x;
	}
	
	public int getHeight() {
		return size.y;
	}
	
	public CreepPath getPath() {
		return path;
	}
	
	public Point getCursor() {
		return cursor;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void addEntity(Entity e) {
		entities.add(e);
		e.initialize(this);
	}
	
	public void computePath() {
		Path current = null;
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tiles[x][y] != null && tiles[x][y] instanceof Path) {
					Path p = (Path) tiles[x][y];
					if (p.getType() == Path.TYPE_START)
						current = p;
					else
						p.setType(Path.TYPE_NONE);
				}	
			}
		}
		
		path.clear();
		int dir = 0;
		while (current != null) {
			path.addStep(current);
			Path prev = current;
			
			if (current.getType() != Path.TYPE_START)
				current.setType(Path.TYPE_PATH);
			
			if (current.numConnections() == 4)
				current = current.getConnection(dir);
			else {
				for (int i = 0; i < 3; i++) {
					int checkDir = (dir + 3 + i) % 4;
					if (current.getConnection(checkDir) != null) {
						current = current.getConnection(checkDir);
						dir     = checkDir;
						break;
					}
					else if (i == 2)
						current = null;
				}
			}
			
			if (current == null || (path.contains(current) && current.numConnections() < 4)) {
				current = null;
				prev.setType(Path.TYPE_END);
			}
		}
	}
	
	public void setTile(Point loc, Tile t) {
		tiles[loc.x][loc.y] = t;
		t.initialize(this, loc);
		
		computePath();
	}
	
	public void update() {
		super.update();
		Point cursorPrev  = new Point(cursor);
		int cursorMoveDir = -1;
		
		// Check mouse movement.
		if (!Mouse.position().equals(Mouse.positionLast())
				&& contains(getMouseLocation()))
		{
			cursor.set(getMouseLocation());
		}
		
		// Cursor movement.
		for (int dir = 0; dir < Direction.NUM_DIRS; dir++) {
			if (Keyboard.arrows[dir].pressed()) {
				cursorMoveDir = dir;
				Point nextPos = cursor.getAdjacent(dir);
				if (!Keyboard.space.down())
					nextPos.set(nextPos.plus(size).mod(size));
				if (contains(nextPos))
					cursor.set(nextPos);
			}
		}
		
		// Check for placing tiles.
		if (contains(cursor)) {
			if (Keyboard.control.down()) {
        		if (Mouse.left.pressed()) {
        			if (!(getTile(cursor) instanceof Path))
        				setTile(cursor, new Path());
        		}
        		else if (Mouse.left.down() && cursorPrev.isAdjacentTo(cursor)) {
        			for (int i = 0; i < Direction.NUM_DIRS; i++) {
        				if (cursorPrev.getAdjacent(i).equals(cursor)) {
        					cursorMoveDir = i;
        					break;
        				}
        			}
        			if (cursorMoveDir >= 0) {
            			if (!(getTile(cursor) instanceof Path))
            				setTile(cursor, new Path());
            			if (getTile(cursorPrev) instanceof Path) {
                			Path p1 = (Path) getTile(cursorPrev);
                			Path p2 = (Path) getTile(cursor);
                			p1.setConnection(cursorMoveDir, p2);
                			p2.setConnection((cursorMoveDir + 2) % 4, p1);
            			}
        			}
        		}
			}
			else {
        		if (Mouse.left.pressed()) {
        			setTile(cursor, new Tower());
        		}
			}
			
			if (Mouse.right.pressed() || (Mouse.right.down() && !cursor.equals(cursorPrev))) {
    			setTile(cursor, new TileGround());
    		}
		}
		
		// Update tiles.
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tiles[x][y] != null)
					tiles[x][y].update();
			}
		}
		
		// Update entities.
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!e.isDestroyed())
				e.update();
			if (e.isDestroyed())
				entities.remove(i--);
		}
	}
	
	public void draw() {
		super.draw();
		
		// Draw background.
		Draw.setColor(Color.LIGHT_GRAY);
		Draw.fillRect(Vector.ORIGIN, new Vector(size).scaledBy(Tile.SIZE));

		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tiles[x][y] != null)
					tiles[x][y].preDraw();
			}
		}
		
		// Draw tiles.
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tiles[x][y] != null)
					tiles[x][y].draw();
			}
		}
		

		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tiles[x][y] != null)
					tiles[x][y].postDraw();
			}
		}
		
		// Draw entities.
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (!e.isDestroyed())
				e.draw();
		}
		
		// Draw the cursor.
		if (contains(cursor)) {
    		Draw.setColor(new Color(255, 255, 255, 128));
    		Draw.fillRect(new Vector(cursor).scale(Tile.SIZE), new Vector(Tile.SIZE, Tile.SIZE));
    		Draw.setColor(Color.GREEN);
    		Draw.drawRect(new Vector(cursor).scale(Tile.SIZE), new Vector(Tile.SIZE, Tile.SIZE));
		}
	}
}
