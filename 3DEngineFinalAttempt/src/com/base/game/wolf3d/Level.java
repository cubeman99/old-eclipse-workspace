package com.base.game.wolf3d;

import java.util.ArrayList;
import com.base.engine.common.GMath;
import com.base.engine.common.Line2f;
import com.base.engine.common.Point;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Vertex;
import com.base.engine.entity.MeshRenderer;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.resourceManagement.ResourceManager;
import com.base.game.wolf3d.tile.ObjectTile;
import com.base.game.wolf3d.tile.Player;
import com.base.game.wolf3d.tile.PlayerStart;
import com.base.game.wolf3d.tile.Tile;
import com.base.game.wolf3d.tile.enemy.BasicEnemy;
import com.base.game.wolf3d.tile.mesh.Door;
import com.base.game.wolf3d.tile.mesh.Elevator;
import com.base.game.wolf3d.tile.mesh.MeshTile;


public class Level extends SceneObject {
	private Game game;
	private LevelFileData data;
	
	private Material matWall1;
	private Material matWall2;
	private Material matFloor;
	private Material matCeiling;
	private Material matDoorSlot;
	private ArrayList<MeshRenderer> meshes;
	
	private Player player;
	private Vector2f startPosition;
	
	private Tile[][] tileGrid;
	private Room[][] roomGrid;
	private ArrayList<ObjectTile> objects;
	private ArrayList<MeshTile> meshTiles;
	private ArrayList<Room> rooms;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Level(Game game) {
		this.game = game;
		
		data      = null;
		meshes    = new ArrayList<MeshRenderer>();
		objects   = new ArrayList<ObjectTile>();
		meshTiles = new ArrayList<MeshTile>();
		rooms     = new ArrayList<Room>();
		startPosition = new Vector2f();

		// Create the player.
		player = new Player();
		addObject(player, 16.5f, 0, 16.5f);
		
		matFloor = new Material();
		matFloor.setTexture(new Texture("floor.png"));
		matCeiling = new Material();
		matCeiling.setTexture(new Texture("ceiling.png"));
		matWall1 = new Material();
		matWall1.setTexture(new Texture("wall1.png"));
		matWall2 = new Material();
		matWall2.setTexture(new Texture("wall2.png"));
		matDoorSlot = new Material();
		matDoorSlot.setTexture(new Texture("door_slot.png"));
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Room getRoom(Point location) {
		return getRoom(location.x, location.y);
	}
	
	public Room getRoom(Vector2f position) {
		return getRoom((int) position.x, (int) position.y);
	}
	
	public Room getRoom(int x, int y) {
		if (!inBounds(x, y))
			return null;
		return roomGrid[x][y];
	}
	
	public int getWidth() {
		return data.getWidth();
	}
	
	public int getHeight() {
		return data.getHeight();
	}
	
	public boolean inBounds(Point loc) {
		return inBounds(loc.x, loc.y);
	}
	
	public boolean inBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < data.getWidth() && y < data.getHeight());
	}
	
	public Tile getTile(int x, int y) {
		return tileGrid[x][y];
	}
	
	public Tile getTile(Point loc) {
		return tileGrid[loc.x][loc.y];
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Game getGame() {
		return game;
	}
	
	public ArrayList<MeshTile> getMeshTiles() {
		return meshTiles;
	}
	
	public ArrayList<ObjectTile> getObjects() {
		return objects;
	}
	
	public LevelFileData getData() {
		return data;
	}
	
	private Texture getIdTexture(int id) {
		return getIdTexture(id, false);
	}
	
	private Texture getIdTexture(int id, boolean dark) {
		if (id >= LevelFileData.OBJECT_OFFSET) {
			int texId = id - LevelFileData.OBJECT_OFFSET;
			Tile tile = Wolf3D.OBJECT_TILES[texId];
			
			if (tile instanceof ObjectTile)
				return ((ObjectTile) Wolf3D.OBJECT_TILES[texId]).getTexture();
		}
		else if (id >= LevelFileData.TEXTURE_OFFSET) {
			int texId = id - LevelFileData.TEXTURE_OFFSET;
			int texX  = texId % 6;
			int texY  = texId / 6;
			if (dark && texX % 2 == 0)
    			texX++;
    		else if (!dark && texX % 2 == 1)
    			texX--;
			return Wolf3D.WALL_TEXTURES[texX][texY];
		}
		return null;
	}
	
	private boolean castRay(RayCast rayCast, Line2f line) {
		Vector2f intersect = Line2f.intersection(rayCast.trajectory, line);
		if (intersect != null) {
			float dist = rayCast.trajectory.end1.distanceTo(intersect);
    		if (rayCast.result == null || dist < rayCast.distance) {
    			rayCast.result = intersect;
    			rayCast.distance = dist;
    			return true;
    		}
		}
		return false;
	}
	
	private boolean castRay(RayCast rayCast, int x, int y) {
		boolean hit = false;
		hit = castRay(rayCast, new Line2f(x, y, x + 1, y))         || hit;
		hit = castRay(rayCast, new Line2f(x, y + 1, x + 1, y + 1)) || hit;
		hit = castRay(rayCast, new Line2f(x, y, x, y + 1))         || hit;
		hit = castRay(rayCast, new Line2f(x + 1, y, x + 1, y + 1)) || hit;
		return hit;
	}
	
	private boolean castRay(RayCast rayCast, Rect2f rect) {
		boolean hit = false;
		for (int i = 0; i < 4; i++)
			hit = castRay(rayCast, rect.getEdge(i)) || hit;
		return hit;
	}
	
	public Vector2f castRay(Vector2f from, Vector2f to) {
		RayCast rayCast = new RayCast();
		rayCast.trajectory = new Line2f(from, to);
		castRay(rayCast);
		return rayCast.result;
	}
	
	public RayCast getRayCast(Vector2f from, Vector2f to) {
		RayCast rayCast = new RayCast();
		rayCast.trajectory = new Line2f(from, to);
		castRay(rayCast);
		return rayCast;
	}
	
	public boolean castRay(RayCast rayCast) {
		rayCast.object   = null;
		rayCast.result   = null;
		rayCast.distance = -1;
		
		// Walls
		for (int y = 0; y < data.getHeight(); y++) {
			for (int x = 0; x < data.getWidth(); x++) {
    			if (LevelFileData.isWall(data.getData(x, y))) {
    				castRay(rayCast, x, y);
    			}
			}
		}
		
		// Mesh Tiles
		for (MeshTile meshTile : meshTiles) {
    		if (meshTile.isHitSolid()) {
				if (castRay(rayCast, meshTile.getHitBox()))
					rayCast.object = meshTile;
			}
		}
		
		return (rayCast.result != null);
	}

	

	// ==================== MUTATORS ==================== //

	public void addTile(MeshTile meshTile) {
		meshTiles.add(meshTile);
		meshTile.initialize(this);
	}
	
	public void addTile(MeshTile meshTile, float x, float y, float z) {
		meshTile.getTransform().setPosition(x, y, z);
		addTile(meshTile);
	}
	
	public void addObject(ObjectTile obj) {
		objects.add(obj);
		if (obj instanceof Tile)
			((Tile) obj).initialize(this);
	}
	
	public void addObject(ObjectTile obj, float x, float y, float z) {
		obj.getTransform().setPosition(x, y, z);
		addObject(obj);
	}
	
	public void addObject(ObjectTile obj, Vector3f pos) {
		addObject(obj, pos.x, pos.y, pos.z);
	}

	public void alertEnemies(Vector2f center) {
		alertEnemies(center, Wolf3D.ENEMY_ALERT_DISTANCE);
	}
	
	public void alertEnemies(Vector2f center, float minDistance) {
		for (ObjectTile obj : objects) {
			if (obj instanceof BasicEnemy) {
				BasicEnemy enemy = (BasicEnemy) obj;
				if ((center == null || minDistance < 0 || enemy.getPosition().distanceTo(center) <= minDistance))
				{
					enemy.alert();
				}
			}
		}
	}
	
	private void addFloor(int x, int y) {
		Vertex[] vertices = new Vertex[4];
		vertices[0] = new Vertex(x    , 0, y    , x    , y    );
		vertices[1] = new Vertex(x + 1, 0, y    , x + 1, y    );
		vertices[2] = new Vertex(x + 1, 0, y + 1, x + 1, y + 1);
		vertices[3] = new Vertex(x    , 0, y + 1, x    , y + 1);
		int[] indices = new int[] {0, 2, 1, 0, 3, 2};
		addMesh(vertices, indices, matFloor);
	}
	
	private void addCeiling(int x, int y) {
		Vertex[] vertices = new Vertex[4];
		vertices[0] = new Vertex(x    , 1, y    , x + 1, y    );
		vertices[1] = new Vertex(x + 1, 1, y    , x    , y    );
		vertices[2] = new Vertex(x + 1, 1, y + 1, x    , y + 1);
		vertices[3] = new Vertex(x    , 1, y + 1, x + 1, y + 1);
		int[] indices = new int[] {0, 1, 2, 0, 2, 3};
		addMesh(vertices, indices, matCeiling);
	}
	
	private void addWall(int x1, int y1, int x2, int y2, float tex1, float tex2, Material mat) {
		Vertex[] vertices = new Vertex[4];
		vertices[0] = new Vertex(x1, 1, y1, tex1, 1);
		vertices[1] = new Vertex(x2, 1, y2, tex2, 1);
		vertices[2] = new Vertex(x2, 0, y2, tex2, 0);
		vertices[3] = new Vertex(x1, 0, y1, tex1, 0);
		int[] indices = new int[] {0, 2, 1, 0, 3, 2};
		addMesh(vertices, indices, mat);
	}
	
	public void addMesh(Vertex[] vertices, int[] indices, Material material) {
		meshes.add(new MeshRenderer(new Mesh(vertices, indices, true, true), material));
	}
	
	public Mesh createQuad(float x1, float y1, float z1, float x2, float y2, float z2, float texX1, float texY1, float texX2, float texY2) {
    	Vertex[] vertices = new Vertex[] {
        	new Vertex(x1, y1, z1, texX1, texY1),
        	new Vertex(x2, y2, z1, texX1, texY2),
        	new Vertex(x1, y1, z1, texX2, texY1),
        	new Vertex(x2, y2, z1, texX2, texY2),
    	};
    	
    	int[] indices = new int[] {
    		0, 1, 3,
    		0, 3, 2,
    	};
    	
    	return new Mesh(vertices, indices, true, true);
	}
	
	private boolean isValidRoomLoc(int x, int y) {
		return (inBounds(x, y) && roomGrid[x][y] == null && !data.isSolid(x, y) && !(tileGrid[x][y] instanceof Door));
	}
	
	private boolean fillRoom(int x, int y, Room room) {
		if (tileGrid[x][y] instanceof Door) {
			if (!room.hasDoor((Door) tileGrid[x][y]))
					room.addDoor((Door) tileGrid[x][y]);
			return false;
		}
		if (!isValidRoomLoc(x, y))
			return false;
		roomGrid[x][y] = room;
		return true;
	}
	
	private Room generateRoom(Point startPos) {
		Room room = new Room(this);
		roomGrid[startPos.x][startPos.y] = room;
		boolean expand = true;
		
		
		while (expand) {
			expand = false;
    		for (int x = 0; x < getWidth(); x++) {
    			for (int y = 0; y < getHeight(); y++) {
    				if (roomGrid[x][y] == room) {
    					expand = fillRoom(x + 1, y, room) || expand;
    					expand = fillRoom(x - 1, y, room) || expand;
    					expand = fillRoom(x, y + 1, room) || expand;
    					expand = fillRoom(x, y - 1, room) || expand;
    				}
    			}			
    		}
		}
		
		// Link rooms through doors.
		for (RoomConnection connection : room.getConnections()) {
			for (Room testRoom : rooms) {
				for (RoomConnection testConnection : testRoom.getConnections()) {
					if (connection.getDoor() == testConnection.getDoor()) {
						connection.setConnection(testRoom);
						testConnection.setConnection(room);
					}
				}
			}
		}
		rooms.add(room);
		
		
		// Generate new rooms connected by doors to this one.
		for (int i = 0; i < room.getConnections().size(); i++) {
			RoomConnection connection = room.getConnections().get(i);
			
			if (connection.getConnection() == null) {
				// Generate a new room from here.
				Point loc = new Point(connection.getDoor().getPosition());
				int x = loc.x; 
				int y = loc.y;
				
				if (isValidRoomLoc(x + 1, y) && getRoom(x + 1, y) != room)
					generateRoom(loc.plus(1, 0));
				else if (isValidRoomLoc(x - 1, y) && getRoom(x - 1, y) != room)
					generateRoom(loc.plus(-1, 0));
				else if (isValidRoomLoc(x, y + 1) && getRoom(x, y + 1) != room)
					generateRoom(loc.plus(0, 1));
				else if (isValidRoomLoc(x, y - 1) && getRoom(x, y - 1) != room)
					generateRoom(loc.plus(0, -1));
				else
					room.getConnections().remove(i--);
			}
		}

		return room;
	}
	
	private void generateRooms(Point startPos) {
		generateRoom(startPos);
		System.out.println("Generated " + rooms.size() + " rooms.");
		for (int i = 0; i < rooms.size(); i++) {
			rooms.get(i).setIndex(i);
		}
	}
	
	public void load(String fileName) {
		data = new LevelFileData(ResourceManager.getPath(fileName, "levels/"));
		meshes.clear();
		objects.clear();
		meshTiles.clear();
		rooms.clear();
		
		player = new Player();
		addObject(player, 14.5f, 0, 11.5f);
		tileGrid = new Tile[data.getWidth()][data.getHeight()];
		roomGrid = new Room[data.getWidth()][data.getHeight()];
		
		for (int y = 0; y < data.getHeight(); y++) {
			for (int x = 0; x < data.getWidth(); x++) {
				int id = data.getData(x, y);
				int doorOrientation = -1;
				boolean createWalls = !LevelFileData.isWall(id);
				
				if (LevelFileData.isObject(id)) {
					int objId = LevelFileData.getObjectId(id);
					Tile obj = Wolf3D.OBJECT_TILES[objId].clone();
					obj.setData(data.getTileData(x, y));
					tileGrid[x][y] = obj;
					
					if (obj instanceof PlayerStart) {
						startPosition.set(x + 0.5f, y + 0.5f);
						player.setPosition(startPosition);
						player.setDirection(data.getTileData(x, y).getInt("direction") * GMath.QUARTER_PI);
					}
					else if (obj instanceof MeshTile) {
						MeshTile tile = (MeshTile) obj;
						meshTiles.add(tile);
						tile.getTransform().setPosition(x, 0, y);
						
						if (obj instanceof Door) {
							Door door = (Door) obj;
	    					doorOrientation = door.getOrientation();
	    					createWalls = false;
						}
						else if (obj instanceof Elevator)
							createWalls = false;
					}
					else if(obj instanceof ObjectTile) {
						ObjectTile objTile = (ObjectTile) obj;
						objTile.getTransform().setPosition(x + 0.5f, 0, y + 0.5f);
    					objects.add(objTile);
					}
					else {
//						System.err.println("Error: object not catagorized (" + obj.getClass().getSimpleName() + ")!");
					}
				}
				
				if (!LevelFileData.isWall(id)) {
    				addFloor(x, y);
    				addCeiling(x, y);
					
				}
				
				if (createWalls) {
    				Material mat;
    				
    				mat = createWallMaterial(x + 1, y, true, doorOrientation);
        			if (LevelFileData.isWall(data.getData(x + 1, y)))
        				addWall(x + 1, y, x + 1, y + 1, y + 1, y, mat);
    				mat = createWallMaterial(x - 1, y, true, doorOrientation);
        			if (LevelFileData.isWall(data.getData(x -1, y)))
        				addWall(x, y + 1, x, y, y + 1, y, mat);
    				mat = createWallMaterial(x, y + 1, false, doorOrientation);
        			if (LevelFileData.isWall(data.getData(x, y + 1)))
        				addWall(x + 1, y + 1, x, y + 1, x + 1, x, mat);
    				mat = createWallMaterial(x, y - 1, false, doorOrientation);
        			if (LevelFileData.isWall(data.getData(x, y - 1)))
        				addWall(x, y, x + 1, y, x + 1, x, mat);
				}
			}
		}
		
		

		for (MeshTile tile : meshTiles)
			tile.initialize(this);
		for (ObjectTile obj : objects)
			obj.initialize(this);
		
		player.setPosition(startPosition);
		generateRooms(new Point(startPosition));
	}
	
	private Material createWallMaterial(int x, int y, boolean flipped, int doorOrientation) {
		if (doorOrientation >= 0)
			return matDoorSlot;
		return new Material().setTexture(getIdTexture(data.getData(x, y), flipped));
	}
	
	private void sortObjects() {
		Vector2f playerPos = player.getPosition();
		Vector2f playerDir = player.getDirection();
		float[] distances = new float[objects.size()];
		
		// Calculate distances.
		for (int i = 0; i < objects.size(); i++) {
			distances[i] = objects.get(i).getPosition().minus(playerPos).scalarProjection(playerDir);
		}
		
		// Sort based on distances (high to low).
		for (int i = 0; i < objects.size(); i++) {
			float maxDist = distances[i];
			int index = i;
			for (int j = i + 1; j < objects.size(); j++) {
				if (distances[j] > maxDist) {
					index = j;
					maxDist = distances[j];
				}
			}
			if (index != i) {
				float temp = distances[i];
				distances[i] = distances[index];
				distances[index] = temp;
				
				ObjectTile tempObj = objects.get(i);
				objects.set(i, objects.get(index));
				objects.set(index, tempObj);
			}
		}
	}

	
	
	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update(float delta) {
		super.update(delta);
		
		// Update object tiles.
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).update(delta);
			if (objects.get(i).isDestroyed())
				objects.remove(i--);
		}
		
		// Update mesh tiles.
		for (int i = 0; i < meshTiles.size(); i++) {
			meshTiles.get(i).update(delta);
			if (meshTiles.get(i).isDestroyed())
				meshTiles.remove(i--);
		}
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		super.render(shader, renderingEngine);
		
		// Render Walls.
		for (MeshRenderer mesh : meshes) {
			mesh.render(shader, renderingEngine);
		}

		// Render Tiles.
		for (MeshTile meshTile : meshTiles)
			meshTile.render(shader, renderingEngine);
		
		// Sort Objects from furthest to nearest.
		sortObjects();
		
		// Render Objects.
		for (int i = 0; i < objects.size(); i++)
			objects.get(i).render(shader, renderingEngine);
	}
}
