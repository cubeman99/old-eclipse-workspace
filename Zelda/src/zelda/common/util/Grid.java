package zelda.common.util;

import zelda.common.geometry.Point;


public class Grid<E> {
	private Object[][] map;
	private int width;
	private int height;


	// ================== CONSTRUCTORS ================== //

	public Grid() {
		this.width = 0;
		this.height = 0;
		this.map = new Object[0][0];
	}

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.map = new Object[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++)
				map[x][y] = null;
		}
	}

	public Grid(Point size) {
		this(size.x, size.y);
	}



	// =================== ACCESSORS =================== //

	@SuppressWarnings("unchecked")
	public E get(int x, int y) {
		return (E) map[x][y];
	}

	@SuppressWarnings("unchecked")
	public E get(Point pos) {
		return (E) map[pos.x][pos.y];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Point getSize() {
		return new Point(width, height);
	}

	public boolean contains(int x, int y) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}

	public boolean contains(Point pos) {
		return (pos.x >= 0 && pos.y >= 0 && pos.x < width && pos.y < height);
	}



	// ==================== MUTATORS ==================== //

	public E set(int x, int y, E element) {
		map[x][y] = element;
		return element;
	}

	public E set(Point pos, E element) {
		map[pos.x][pos.y] = element;
		return element;
	}

	public void setWidth(int width) {
		setSize(width, height);
	}

	public void setHeight(int height) {
		setSize(width, height);
	}

	public void setSize(int newWidth, int newHeight) {
		Object[][] newMap = new Object[newWidth][newHeight];

		for (int x = 0; x < newWidth; x++) {
			for (int y = 0; y < newHeight; y++) {
				if (contains(x, y))
					newMap[x][y] = map[x][y];
				else
					newMap[x][y] = null;
			}
		}

		width = newWidth;
		height = newHeight;
		map = newMap;
	}
}
