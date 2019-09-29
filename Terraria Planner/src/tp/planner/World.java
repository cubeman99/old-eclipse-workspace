package tp.planner;

import java.awt.Graphics;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import tp.common.FileControl;
import tp.common.Point;
import tp.common.Settings;
import tp.common.graphics.GridCanvas;
import tp.planner.tile.Tile;

public class World {
	public Control control;
	public Grid objGrid;
	public Grid wallGrid;
	public Grid liquidGrid;
	public Grid wireGrid;
	private Point size;
	private Grid[] grids;
	private GridCanvas canvas;
	
	// ================== CONSTRUCTORS ================== //
	
	public World(Control control, int width, int height) {
		this.control = control;
		this.size    = new Point(width, height);
		this.canvas  = new GridCanvas(size);
		createGrids();
	}
	
	
	
	// =================== ACCESSORS =================== //

	public Point getSize() {
		return size;
	}
	
	public int getWidth() {
		return size.x;
	}

	public int getHeight() {
		return size.y;
	}
	
	public Grid getGrid(int gridIndex) {
		return grids[gridIndex];
	}
	
	public int getTotalGrids() {
		return grids.length;
	}
	
	public GridCanvas getCanvas() {
		return canvas;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void resize(int sizeX, int sizeY) {
		size.set(Math.min(Settings.MAX_CANVAS_SIZE.x, sizeX),
				Math.min(Settings.MAX_CANVAS_SIZE.y, sizeY));
		for (Grid grid : grids) {
			grid.resize(size.x, size.y);
		}
	}
	
	public void clear() {
		canvas.clear();
		for (Grid grid : grids) {
			grid.clear();
		}
	}
	
	private void createGrids() {
		objGrid    = new Grid(this, 0);
		wallGrid   = new Grid(this, 1);
		liquidGrid = new Grid(this, 2);
		wireGrid   = new Grid(this, 3);
		
		grids    = new Grid[4];
		grids[0] = objGrid;
		grids[1] = wallGrid;
		grids[2] = liquidGrid;
		grids[3] = wireGrid;
	}
	
	public void saveToFile(File file) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			
			out.write("10"); out.newLine(); // save version
			out.write("" + size.x); out.newLine(); // world width
			out.write("" + size.y); out.newLine(); // world height
			out.write("" + control.getBackdropIndex()); out.newLine(); // backdrop index
			
			// List of item names and indeces:
			out.write("" + ItemData.getTotalItems()); out.newLine();
			for (int i = 0; i < ItemData.getTotalItems(); i++) {
				out.write(ItemData.get(i).getName()); out.newLine();
			}
			
			for (int x = 0; x < size.x; x++) {
				for (int y = 0; y < size.y; y++) {
					boolean empty = true;
					for (Grid grid : grids) {
						Tile t = grid.get(x, y);
						if (t != null) {
							empty = false;
							break;
						}
					}
					
					if (!empty) {
						out.write("" + x); out.newLine();
						out.write("" + y); out.newLine();
						
						// TILE:
						Tile t = objGrid.get(x, y);
						if (t != null && t.getX() == x && t.getY() == y) {
							out.write("" + t.getItem().getIndex()); out.newLine(); // item index
							out.write("" + t.getConnectionIndex()); out.newLine(); // connection index
							out.write("" + t.getSubimage()); out.newLine(); // subimage
							out.write("" + t.getRandomSubimage()); out.newLine(); // random subimage
						}
						else {
							out.write("-1"); out.newLine();
							out.write("-1"); out.newLine();
							out.write("-1"); out.newLine();
							out.write("-1"); out.newLine();
						}
						
						// WALL:
						t = wallGrid.get(x, y);
						if (t != null) {
							out.write("" + t.getItem().getIndex()); out.newLine(); // item index
							out.write("" + t.getConnectionIndex()); out.newLine(); // connection index
						}
						else {
							out.write("-1"); out.newLine();
							out.write("-1"); out.newLine();
						}
						
						// WIRE:
						t = wireGrid.get(x, y);
						if (t != null)
							out.write("" + t.getSubimage()); // subimage
						else
							out.write("-1");
						out.newLine();

						// LIQUID:
						t = liquidGrid.get(x, y);
						if (t != null)
							out.write(t.getItem() == ItemData.WATER ? "1" : "2"); // water = 1, lava = 2
						else
							out.write("-1");
						out.newLine();
					}
				}
			}
			
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void loadFromFile(File file) {
		try {
			Scanner reader = new Scanner(file);
			
			// Version:
			int version = reader.nextInt(); reader.nextLine();
			if (version != 10) {
				reader.close();
				loadFromOldFile(file);
				return;
			}
			
			// World size:
			size.x = reader.nextInt(); reader.nextLine();
			size.y = reader.nextInt(); reader.nextLine();
			System.out.println("Version: " + version);
			System.out.println("World width: " + size.x);
			System.out.println("World height: " + size.y);
			
			// Backdrop index:
			int backdropIndex = reader.nextInt(); reader.nextLine();
			System.out.println("Backdrop: " + backdropIndex);
			control.setBackdropIndex(backdropIndex);
			
			resize(size.x, size.y);
			clear();
			
			// Read & create the conversion template:
			int itemCount = reader.nextInt(); reader.nextLine();
			Item[] template = new Item[itemCount];
			for (int i = 0; i < itemCount; i++) {
				template[i] = ItemData.find(reader.nextLine());
			}
			
			while (reader.hasNextLine()) {
				int x				= reader.nextInt(); reader.nextLine();
				int y				= reader.nextInt(); reader.nextLine();
				
				int objIndex		= reader.nextInt(); reader.nextLine();
				int objConnect		= reader.nextInt(); reader.nextLine();
				int objSubImage		= reader.nextInt(); reader.nextLine();
				int objRandSub		= reader.nextInt(); reader.nextLine();
				
				int wallIndex		= reader.nextInt(); reader.nextLine();
				int wallConnect		= reader.nextInt(); reader.nextLine();
				
				int wireSubImage	= reader.nextInt(); reader.nextLine();
				
				int liquid			= reader.nextInt(); reader.nextLine();
				
				if (!objGrid.isValid(x, y))
					continue;
				
				if (objIndex >= 0) {
					Item item = template[objIndex];
					if (item != null) {
    					Tile obj  = new Tile(objGrid, new Point(x, y), item);
    					obj.setSubimage(objSubImage);
    					obj.setRandomSubimage(objRandSub);
    					obj.setConnectionIndex(objConnect);
    					objGrid.putRaw(obj);
					}
				}
				if (wallIndex >= 0) {
					Item item = template[wallIndex];
					if (item != null) {
    					Tile wall = new Tile(wallGrid, new Point(x, y), item);
    					wall.setConnectionIndex(wallConnect);
    					wallGrid.putRaw(wall);
					}
				}
				if (wireSubImage >= 0) {
					Tile t = new Tile(wireGrid, new Point(x, y), ItemData.WIRE);
					wireGrid.putRaw(t);
					t.setSubimage(wireSubImage);
				}
				if (liquid > 0) {
					Tile liq = new Tile(liquidGrid, new Point(x, y), liquid == 1 ? ItemData.WATER : ItemData.LAVA);
					liquidGrid.putRaw(liq);
					liquidGrid.connectArea(x, y, 1, 2);
				}
			}
			
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		objGrid.refreshArea(0, 0, size.x, size.y);
		bufferArea(0, 0, size.x, size.y);
	}
	
	public void loadFromOldFile(File file) {
		try {
			Scanner reader = new Scanner(file);
			
			int version = reader.nextInt(); reader.nextLine();
			if (version != 6) {
				reader.close();
				System.out.println("Save file version " + version + " is not supported (Needs to be version 6).");
				return;
			}
			
			size.x = reader.nextInt(); reader.nextLine();
			size.y = reader.nextInt(); reader.nextLine();
			System.out.println("Version: " + version);
			System.out.println("World width: " + size.x);
			System.out.println("World height: " + size.y);
			
			resize(size.x, size.y);
			clear();

			int itemCount = reader.nextInt(); reader.nextLine();
			for (int i = 0; i < itemCount; i++)
				reader.nextLine();
			
			int backdropIndex = reader.nextInt(); reader.nextLine();
			System.out.println("Backdrop: " + backdropIndex);
			control.setBackdropIndex(backdropIndex);
			
			while (reader.hasNext()) {
				int x			= reader.nextInt(); reader.nextLine();
				int y			= reader.nextInt(); reader.nextLine();
				int objType		= reader.nextInt(); reader.nextLine();
				int objConnect	= reader.nextInt(); reader.nextLine();
				int wallType	= reader.nextInt(); reader.nextLine();
				int wallConnect	= reader.nextInt(); reader.nextLine();
				int subimage	= reader.nextInt(); reader.nextLine();
				int randsub     = reader.nextInt(); reader.nextLine();
				int liquid		= reader.nextInt(); reader.nextLine();
				int wire        = reader.nextInt(); reader.nextLine();
				
				if (!objGrid.isValid(x, y))
					continue;
				
				if (objType > 0) {
					Item item = FileControl.getConvertedItem(objType);
					if (item != null) {
    					Tile obj  = new Tile(objGrid, new Point(x, y), item);
    					obj.setSubimage(subimage);
    					obj.setRandomSubimage(randsub);
    					obj.setConnectionIndex(objConnect);
    					objGrid.putRaw(obj);
					}
				}
				if (wallType > 0) {
					Item item = FileControl.getConvertedItem(wallType);
					if (item != null) {
    					Tile wall = new Tile(wallGrid, new Point(x, y), item);
    					wall.setConnectionIndex(wallConnect);
    					wallGrid.putRaw(wall);
					}
				}
				if (liquid > 0) {
					Tile liq = new Tile(liquidGrid, new Point(x, y), liquid <= 100 ? ItemData.WATER : ItemData.LAVA);
					liquidGrid.putRaw(liq);
					liquidGrid.connectArea(x, y, 1, 2);
				}
				if (wire > 0) {
					Tile w = new Tile(wireGrid, new Point(x, y), ItemData.WIRE);
					wireGrid.putRaw(w);
					wireGrid.connectArea(x - 1, y - 1, 3, 3);
				}
			}
			
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		objGrid.refreshArea(0, 0, size.x, size.y);
		bufferArea(0, 0, size.x, size.y);
	}
	
	public void bufferArea(int x, int y, int width, int height) {
		for (int yy = Math.max(0, y); yy < Math.min(size.y, y + height + 1); yy++) {
			for (int xx = Math.max(0, x); xx < Math.min(size.x, x + width + 1); xx++) {
				canvas.clearPoint(xx, yy);
				
				wallGrid.bufferSpace(xx, yy);
				objGrid.bufferSpace(xx, yy);
				liquidGrid.bufferSpace(xx, yy);
				wireGrid.bufferSpace(xx, yy);
			}
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(canvas.getImage(), -control.getViewPosition().x, -control.getViewPosition().y, null);
	}
}
