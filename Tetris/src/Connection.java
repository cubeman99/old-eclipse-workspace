import java.awt.Image;


public class Connection {
	
	public static Image getOverlayImage(int cIndex) {
		return GameData.TILE_OVERLAY_IMAGES[cIndex];
	}
	
	public static int getGridConnectIndex(int x, int y) {
		return getConnectIndex(Grid.getBooleanMap(Grid.tiles[x][y].color), x, y, Grid.WIDTH, Grid.HEIGHT);
	}
	
	public static int getConnectIndex(boolean[][] grid, int x, int y, int width, int height) {
		boolean[] cc = new boolean[4];
		for (int D = 0; D < 4; D++) {
			cc[D] = false;
			int cx = x + (int) GMath.lenDirX(1, D * 90);
			int cy = y - (int) GMath.lenDirY(1, D * 90);
			if (inBounds(cx, cy, width, height)) {
				cc[D] = grid[cx][cy];
			}
		}
		
		int index = 0;
		
		if (cc[0]) index = 1;
		if (cc[1]) index = 2;
		if (cc[2]) index = 3;
		if (cc[3]) index = 4;
		if (cc[0] && cc[2]) index = 5;
		if (cc[1] && cc[3]) index = 6;
		if (cc[0] && cc[1]) index = 7;
		if (cc[1] && cc[2]) index = 8;
		if (cc[2] && cc[3]) index = 9;
		if (cc[3] && cc[0]) index = 10;
		if (cc[0] && cc[1] && cc[2]) index = 11;
		if (cc[1] && cc[2] && cc[3]) index = 12;
		if (cc[2] && cc[3] && cc[0]) index = 13;
		if (cc[3] && cc[0] && cc[1]) index = 14;
		if (cc[0] && cc[1] && cc[2] && cc[3]) index = 15;
		
		return index;
	}
	
	public static boolean inBounds(int x, int y, int width, int height) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}
}
