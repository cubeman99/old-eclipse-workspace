package com.base.game.wolf3d;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.base.engine.common.Point;
import com.base.game.wolf3d.tile.TileData;

public class LevelFileData {
	public static int NUM_TEXTURES   = 114;
	public static int NUM_OBJECTS    = 50;
	public static int TEXTURE_OFFSET = 1;
	public static int OBJECT_OFFSET  = TEXTURE_OFFSET + NUM_TEXTURES;
	
	private int width;
	private int height;
	private int[][] idMap;
	private TileData[][] tileData;
	
	

	// ================== CONSTRUCTORS ================== //

	public LevelFileData(int width, int height) {
		this.width    = width;
		this.height   = height;
		this.idMap    = new int[width][height];
		this.tileData = new TileData[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tileData[x][y] = new TileData();
			}			
		}
	}
	
	public LevelFileData(String fileName) {
		load(fileName);
	}
	
	

	// =================== ACCESSORS =================== //

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isSolid(int x, int y) {
		return isWall(idMap[x][y]);
	}
	
	public TileData getTileData(Point loc) {
		return getTileData(loc.x, loc.y);
	}
	
	public TileData getTileData(int x, int y) {
		return tileData[x][y];
	}
	
	public int getData(int x, int y) {
		return idMap[x][y];
	}
	
	public int getData(Point loc) {
		return getData(loc.x, loc.y);
	}
	
	public int[][] getData() {
		return idMap;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public LevelFileData flipVertical() {
		for (int y = 0; y < height / 2; y++) {
			for (int x = 0; x < width; x++) {
				int temp = idMap[x][y];
				idMap[x][y] = idMap[x][height - y - 1];
				idMap[x][height - y - 1] = temp;
				
				TileData temp2 = tileData[x][y];
				tileData[x][y] = tileData[x][height - y - 1];
				tileData[x][height - y - 1] = temp2;
			}
		}
		return this;
	}
	
	public void setWidth(int newWidth) {
		setSize(newWidth, height);
	}
	
	public void setHeight(int newHeight) {
		setSize(width, newHeight);
	}
	
	public void setSize(int newWidth, int newHeight) {
		int[][] newData = new int[newWidth][newHeight];
		
		for (int x = 0; x < newWidth; x++) {
			for (int y = 0; y < newHeight; y++) {
				newData[x][y] = 0;
				if (x < width && y < height)
					newData[x][y] = idMap[x][y];
			}
		}
		
		// TODO: tileData
		
		width  = newWidth;
		height = newHeight;
	}
	
	public void setData(Point loc, int value) {
		idMap[loc.x][loc.y] = value;
	}
	
	public void setData(int x, int y, int value) {
		idMap[x][y] = value;
	}
	
	public void save(String fileName) {
		save(new File(fileName));
	}

	public void load(String fileName) {
		load(new File(fileName));
	}
	
	public void save(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream output = new ObjectOutputStream(fos);
			
			// Save map data.
			output.writeInt(width);
			output.writeInt(height);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					output.writeInt(idMap[x][y]);
					output.writeObject(tileData[x][y]);
				}
			}
			
			output.flush();
			output.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream input = new ObjectInputStream(fis);
			
			// Load map data.
			width  = input.readInt();
			height = input.readInt();
			idMap    = new int[width][height];
			tileData = new TileData[width][height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					idMap[x][y] = input.readInt();
					tileData[x][y] = (TileData) input.readObject();
				}
			}
			
			input.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
	public static boolean isWall(int id) {
		return (id >= TEXTURE_OFFSET && id < TEXTURE_OFFSET + NUM_TEXTURES);
	}
	
	public static boolean isObject(int id) {
		return (id >= OBJECT_OFFSET && id < OBJECT_OFFSET + NUM_OBJECTS);
	}

	public static int getWallId(int id) {
		return id - TEXTURE_OFFSET;
	}
	
	public static int getObjectId(int id) {
		return id - OBJECT_OFFSET;
	}
}
