package tp.planner;

import tp.common.GMath;
import tp.common.Point;
import tp.planner.tile.Tile;

public class ConnectionScheme {
	
	public static final ConnectionScheme BLOCK = new ConnectionScheme(
			new int[] {9,3, 9,0, 6,3, 12,0, 6,0, 6,4, 5,0, 0,4, 1,4, 1,3, 0,3, 1,2, 4,0, 1,0, 0,0, 1,1});
	
	public static final ConnectionScheme DIRTBLEND = new ConnectionScheme(
			new int[] {9,3, 9,0, 6,3, 12,0, 6,0, 6,4, 5,0, 0,4, 1,4, 1,3, 0,3, 1,2, 4,0, 1,0, 0,0, 1,1, 6,11, 12,5, 11,8, 12,8, 11,5, 8,10, 10,7, 2,6, 3,6, 3,5, 2,5, 8,5, 8,7, 8,6, 9,7, 3,13, 6,8, 0,13, 6,5, 3,14, 0,14, 9,11, 7,8, 7,5, 6,12, 0,12, 3,12, 13,1, 5,8, 5,5, 13,3, 0,11, 3,11, 13,0, 4,8, 4,5, 13,2});
	public static final ConnectionScheme ASHBLEND = new ConnectionScheme(
			new int[] {9,3, 9,0, 6,3, 12,0, 6,0, 6,4, 5,0, 0,4, 1,4, 1,3, 0,3, 1,2, 4,0, 1,0, 0,0, 1,1, 6,11, 12,5, 11,8, 12,8, 11,5, 8,10, 10,7, 2,6, 3,6, 3,5, 2,5, 8,5, 8,7, 8,6, 9,7, 3,13, 6,8, 0,13, 6,5, 3,14, 0,14, 9,11, 7,8, 7,5, 6,12, 0,12, 3,12, 13,1, 5,8, 5,5, 13,3, 0,11, 3,11, 13,0, 4,8, 4,5, 13,2});
	public static final ConnectionScheme STONEBLEND = new ConnectionScheme(
			new int[] {9,3, 9,0, 6,3, 12,0, 6,0, 6,4, 5,0, 0,4, 1,4, 1,3, 0,3, 1,2, 4,0, 1,0, 0,0, 1,1, 6,11, 12,5, 11,8, 12,8, 11,5, 8,10, 10,7, 2,6, 3,6, 3,5, 2,5, 8,5, 8,7, 8,6, 9,7, 3,13, 6,8, 0,13, 6,5, 3,14, 0,14, 9,11, 7,8, 7,5, 6,12, 0,12, 3,12, 13,1, 5,8, 5,5, 13,3, 0,11, 3,11, 13,0, 4,8, 4,5, 13,2});
	public static final ConnectionScheme MUDBLEND = new ConnectionScheme(
			new int[] {9,3, 9,0, 6,3, 12,0, 6,0, 6,4, 5,0, 0,4, 1,4, 1,3, 0,3, 1,2, 4,0, 1,0, 0,0, 1,1, 6,11, 12,5, 11,8, 12,8, 11,5, 8,10, 10,7, 2,6, 3,6, 3,5, 2,5, 8,5, 8,7, 8,6, 9,7, 3,13, 6,8, 0,13, 6,5, 3,14, 0,14, 9,11, 7,8, 7,5, 6,12, 0,12, 3,12, 13,1, 5,8, 5,5, 13,3, 0,11, 3,11, 13,0, 4,8, 4,5, 13,2});
	
	public static final ConnectionScheme DIRTGRASS = new ConnectionScheme(
			new int[] {9,3, 9,0, 6,3, 12,0, 6,0, 6,4, 5,0, 0,4, 1,4, 1,3, 0,3, 1,2, 4,0, 1,0, 0,0, 1,1, 6,11, 12,5, 11,8, 12,8, 11,5, 8,10, 10,7, 2,6, 3,6, 3,5, 2,5, 8,5, 8,7, 8,6, 9,7, 3,13, 6,8, 0,13, 6,5, 3,14, 0,14, 9,11, 7,8, 7,5, 6,12, 0,12, 3,12, 13,1, 5,8, 5,5, 13,3, 0,11, 3,11, 13,0, 4,8, 4,5, 13,2, 2,16, 1,15, 2,15, 0,15, 7,14, 9,14, 9,12, 7,12, 0,18, 3,18, 0,19, 3,19, 0,20, 3,21, 3,20, 0,21, 8,14, 9,13, 8,12, 7,13, 8,13, 0,6, 1,6, 1,5, 0,5, 6,1, 10,0, 6,2, 11,0, 5,15, 6,15, 7,15, 8,15, 9,15, 10,15, 11,15});
	public static final ConnectionScheme MUDGRASS = new ConnectionScheme(
			new int[] {9,3, 9,0, 6,3, 12,0, 6,0, 6,4, 5,0, 0,4, 1,4, 1,3, 0,3, 1,2, 4,0, 1,0, 0,0, 1,1, 6,11, 12,5, 11,8, 12,8, 11,5, 8,10, 10,7, 2,6, 3,6, 3,5, 2,5, 8,5, 8,7, 8,6, 9,7, 3,13, 6,8, 0,13, 6,5, 3,14, 0,14, 9,11, 7,8, 7,5, 6,12, 0,12, 3,12, 13,1, 5,8, 5,5, 13,3, 0,11, 3,11, 13,0, 4,8, 4,5, 13,2, 2,16, 1,15, 2,15, 0,15, 7,14, 9,14, 9,12, 7,12, 0,18, 3,18, 0,19, 3,19, 0,20, 3,21, 3,20, 0,21, 8,14, 9,13, 8,12, 7,13, 8,13, 0,6, 1,6, 1,5, 0,5, 6,1, 10,0, 6,2, 11,0, 5,15, 6,15, 7,15, 8,15, 9,15, 10,15, 11,15});
	
	public static final ConnectionScheme CACTUS = new ConnectionScheme(
			new int[] {});
	
	
	
	public static int setConnectSubimage(Tile t) {
		Tile[] n = getNeighborTiles(t);
		boolean[] solid = new boolean[4];
		Item[] nItems = new Item[4];
		for (int i = 0; i < 4; i++) {
			solid[i]  = (n[i] != null && n[i].isSolid());
			nItems[i] = (n[i] == null ? null : n[i].getItem());
		}
		Item item = t.getItem();
		Grid grid = t.getGrid();
		int sub = 0;
		int tx = t.getX();
		int ty = t.getY();
		
		// Torch & Switch
		if (item == ItemData.SWITCH || (item.getIndex() >= ItemData.TORCH.getIndex() && item.getIndex() < ItemData.TORCH.getIndex() + 9)) {
			if (solid[3] || nItems[3] == ItemData.WOODEN_BEAM)
				sub = 0;
			else if (solid[2] || nItems[2] == ItemData.WOODEN_BEAM)
				sub = 1;
			else if (solid[0] || nItems[0] == ItemData.WOODEN_BEAM)
				sub = 2;
		}
		
		// Crystal Shard & Red/Blue/Green Lights
		else if (item == ItemData.CRYSTAL_SHARD || item == ItemData.BLUE_LIGHT || item == ItemData.GREEN_LIGHT || item == ItemData.RED_LIGHT) {
			if (!solid[3]) {
				if (solid[2])
					sub = 3;
				else if (solid[0])
					sub = 2;
				else if (solid[1])
					sub = 1;
			}
		}
		
		// Wood Platform
		else if (item == ItemData.WOOD_PLATFORM) {
			
			if (nItems[0] == item && nItems[2] == item)
				sub = 1;
			else if (nItems[0] == item)
				sub = 2 + (solid[2] ? 2 : 0);
			else if (nItems[2] == item)
				sub = 3 + (solid[0] ? 2 : 0);
			else if (solid[2] && solid[0])
				sub = 0;
			else if (solid[2])
				sub = 6;
			else if (solid[0])
				sub = 7;
		}
		
		// Sign
		else if (item == ItemData.SIGN) {
			if (isTileSolid(grid.get(tx, ty + 2)) && isTileSolid(grid.get(tx + 1, ty + 2)))
				sub = 0;
			else if (isTileSolid(grid.get(tx, ty - 1)) && isTileSolid(grid.get(tx + 1, ty - 1)))
				sub = 1;
			else if (isTileSolid(grid.get(tx - 1, ty)) && isTileSolid(grid.get(tx - 1, ty + 1)))
				sub = 2;
			else if (isTileSolid(grid.get(tx + 2, ty)) && isTileSolid(grid.get(tx + 2, ty + 1)))
				sub = 3;
		}
		
		// Wire
		else if (item == ItemData.WIRE) {
			boolean[] nW = getNeighbors(t, t.getItem());
			
			if (nW[0]) sub = 1;
			if (nW[1]) sub = 2;
			if (nW[2]) sub = 3;
			if (nW[3]) sub = 4;
			if (nW[0] && nW[2]) sub = 5;
			if (nW[1] && nW[3]) sub = 6;
			if (nW[0] && nW[1]) sub = 7;
			if (nW[1] && nW[2]) sub = 8;
			if (nW[2] && nW[3]) sub = 9;
			if (nW[3] && nW[0]) sub = 10;
			if (nW[0] && nW[1] && nW[2]) sub = 11;
			if (nW[1] && nW[2] && nW[3]) sub = 12;
			if (nW[2] && nW[3] && nW[0]) sub = 13;
			if (nW[3] && nW[0] && nW[1]) sub = 14;
			if (nW[0] && nW[1] && nW[2] && nW[3]) sub = 15;
		}
		
		// Water/Lava
		else if (item == ItemData.WATER || item == ItemData.LAVA) {
			if (nItems[1] != item)
				sub = 1;
		}
		
		// Tree Trunk
		else if (item == ItemData.TREE_TRUNK) {
			boolean top    = !(nItems[1] == ItemData.TREE_TRUNK || nItems[1] == ItemData.TREE_TOP_BARE || nItems[1] == ItemData.TREE_TOP);
			boolean bottom = (nItems[3] != ItemData.TREE_TRUNK);
			sub = (top ? 2 : 0) + (bottom ? 0 : 1);
			
			if (nItems[0] == ItemData.TREE_STUMP && nItems[2] == ItemData.TREE_STUMP)
				sub = 12 + (top ? 3 : 0);
			else if (nItems[0] == ItemData.TREE_STUMP)
				sub = 11 + (top ? 3 : 0);
			else if (nItems[2] == ItemData.TREE_STUMP)
				sub = 10 + (top ? 3 : 0);
			else if (nItems[0] == ItemData.TREE_BRANCH_BARE && nItems[2] == ItemData.TREE_BRANCH_BARE)
				sub = 6 + (top ? 3 : 0);
			else if (nItems[0] == ItemData.TREE_BRANCH_BARE)
				sub = 5 + (top ? 3 : 0);
			else if (nItems[2] == ItemData.TREE_BRANCH_BARE)
				sub = 4 + (top ? 3 : 0);
		}
		
		// Tree Branch and Stump
		else if (item == ItemData.TREE_STUMP || item == ItemData.TREE_BRANCH_BARE) {
			if (nItems[0] == ItemData.TREE_TRUNK)
				sub = 1;
		}
		
		// Shroom Stem
		else if (item == ItemData.SHROOM_STEM) {
			boolean top    = !(nItems[1] == ItemData.SHROOM_STEM || nItems[1] == ItemData.SHROOM_TOP);
			boolean bottom = (nItems[3] != ItemData.SHROOM_STEM);
			sub = (top ? 2 : 0) + (bottom ? 0 : 1);
		}
		
		t.setSubimage(sub);
		return sub;
	}
	
	private static boolean isTileSolid(Tile t) {
		if (t == null)
			return false;
		return t.isSolid();
	}
	
	public static int setConnectIndex(Tile t) {
		ConnectionScheme cs = t.getItem().getConnectionScheme();
		
		if (cs == ConnectionScheme.DIRTBLEND)
			return setConnectIndexBlend(t, ItemData.DIRT_BLOCK);
		if (cs == ConnectionScheme.ASHBLEND)
			return setConnectIndexBlend(t, ItemData.ASH_BLOCK);
		if (cs == ConnectionScheme.STONEBLEND)
			return setConnectIndexBlend(t, ItemData.STONE_BLOCK);
		if (cs == ConnectionScheme.MUDBLEND)
			return setConnectIndexBlend(t, ItemData.MUD_BLOCK);
		
		if (cs == ConnectionScheme.DIRTGRASS)
			return setConnectIndexGrass(t, ItemData.DIRT_BLOCK);
		if (cs == ConnectionScheme.MUDGRASS)
			return setConnectIndexGrass(t, ItemData.MUD_BLOCK);
		
		return setConnectIndexBlock(t);
	}
	
	public static int setConnectIndexBlock(Tile t) {
		boolean[] n = getNeighbors(t, t.getItem());
		int index = 0;
		
		if (t.getGrid().getGridIndex() == Item.TYPE_WALL) {
			Tile[] nTiles = getNeighborTiles(t);
			for (int i = 0; i < 4; i++)
				n[i] = (nTiles[i] != null);
		}
		
		if (n[0]) index = 1;
		if (n[1]) index = 2;
		if (n[2]) index = 3;
		if (n[3]) index = 4;
		if (n[0] && n[2]) index = 5;
		if (n[1] && n[3]) index = 6;
		if (n[0] && n[1]) index = 7;
		if (n[1] && n[2]) index = 8;
		if (n[2] && n[3]) index = 9;
		if (n[3] && n[0]) index = 10;
		if (n[0] && n[1] && n[2]) index = 11;
		if (n[1] && n[2] && n[3]) index = 12;
		if (n[2] && n[3] && n[0]) index = 13;
		if (n[3] && n[0] && n[1]) index = 14;
		if (n[0] && n[1] && n[2] && n[3]) index = 15;
		
		t.setConnections(n);
		t.setConnectionIndex(index);
		return index;
	}
	
	public static int setConnectIndexBlend(Tile t, Item connectItem) {
		Tile[] nTile  = getNeighborTiles(t);
		boolean[] n   = getNeighbors(t, t.getItem());
		boolean[] b   = getNeighbors(t, connectItem);
		boolean[] cdr = new boolean[4];
		int index     = 0;
		
		if (t.getBaseItem() == ItemData.DIRT_BLOCK) {
			for (int i = 0; i < 4; i++) {
				if (nTile[i] != null)
					n[i] = n[i] || ((nTile[i].getItem().getConnectionScheme() == DIRTBLEND || nTile[i].getItem().getConnectionScheme() == DIRTGRASS) && nTile[i].getConnection((i + 2) % 4));
			}
		}
		else if (t.getBaseItem() == ItemData.MUD_BLOCK) {
			for (int i = 0; i < 4; i++) {
				if (nTile[i] != null)
					n[i] = n[i] || ((nTile[i].getItem().getConnectionScheme() == MUDBLEND || nTile[i].getItem().getConnectionScheme() == MUDGRASS) && nTile[i].getConnection((i + 2) % 4));
			}
		}
		else if (t.getBaseItem() == ItemData.ASH_BLOCK) {
			for (int i = 0; i < 4; i++) {
				if (nTile[i] != null)
					n[i] = n[i] || (nTile[i].getItem().getConnectionScheme() == ASHBLEND && nTile[i].getConnection((i + 2) % 4));
			}
		}
		else if (t.getBaseItem() == ItemData.STONE_BLOCK) {
			for (int i = 0; i < 4; i++) {
				if (nTile[i] != null)
					n[i] = n[i] || (nTile[i].getItem().getConnectionScheme() == STONEBLEND && nTile[i].getConnection((i + 2) % 4));
			}
		}
		
		int[] seq = {0, 0, 0, 0};
		seq[0] += (n[0] ? 1 : 0) + (b[0] ? 2 : 0);
		seq[1] += (n[1] ? 1 : 0) + (b[1] ? 2 : 0);
		seq[2] += (n[2] ? 1 : 0) + (b[2] ? 2 : 0);
		seq[3] += (n[3] ? 1 : 0) + (b[3] ? 2 : 0);
		
		if (n[0]) index = 1;
		if (n[1]) index = 2;
		if (n[2]) index = 3;
		if (n[3]) index = 4;
		if (n[0] && n[2]) index = 5;
		if (n[1] && n[3]) index = 6;
		if (n[0] && n[1]) index = 7;
		if (n[1] && n[2]) index = 8;
		if (n[2] && n[3]) index = 9;
		if (n[3] && n[0]) index = 10;
		if (n[0] && n[1] && n[2]) index = 11;
		if (n[1] && n[2] && n[3]) index = 12;
		if (n[2] && n[3] && n[0]) index = 13;
		if (n[3] && n[0] && n[1]) index = 14;
		if (n[0] && n[1] && n[2] && n[3]) index = 15;

		if (sequenceEquals(seq, 2,0,0,0)) {index = 31; cdr[0] = true;}
		if (sequenceEquals(seq, 0,2,0,0)) {index = 32; cdr[1] = true;}
		if (sequenceEquals(seq, 0,0,2,0)) {index = 33; cdr[2] = true;}
		if (sequenceEquals(seq, 0,0,0,2)) {index = 34; cdr[3] = true;}
		if (sequenceEquals(seq, 2,0,1,0)) {index = 35; cdr[0] = true;}
		if (sequenceEquals(seq, 1,0,2,0)) {index = 36; cdr[2] = true;}
		if (sequenceEquals(seq, 2,0,2,0)) {index = 37; cdr[0] = true; cdr[2] = true;}
		if (sequenceEquals(seq, 0,2,0,1)) {index = 38; cdr[1] = true;}
		if (sequenceEquals(seq, 0,1,0,2)) {index = 39; cdr[3] = true;}
		if (sequenceEquals(seq, 0,2,0,2)) {index = 40; cdr[1] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 1,1,2,0)) {index = 41; cdr[2] = true;}
		if (sequenceEquals(seq, 2,1,1,0)) {index = 42; cdr[0] = true;}
		if (sequenceEquals(seq, 1,2,1,0)) {index = 43; cdr[1] = true;}
		if (sequenceEquals(seq, 0,2,1,1)) {index = 44; cdr[1] = true;}
		if (sequenceEquals(seq, 0,1,1,2)) {index = 45; cdr[3] = true;}
		if (sequenceEquals(seq, 0,1,2,1)) {index = 46; cdr[2] = true;}
		if (sequenceEquals(seq, 1,0,2,1)) {index = 47; cdr[2] = true;}
		if (sequenceEquals(seq, 2,0,1,1)) {index = 48; cdr[0] = true;}
		if (sequenceEquals(seq, 1,0,1,2)) {index = 49; cdr[3] = true;}
		if (sequenceEquals(seq, 1,2,0,1)) {index = 50; cdr[1] = true;}
		if (sequenceEquals(seq, 1,1,0,2)) {index = 51; cdr[3] = true;}
		if (sequenceEquals(seq, 2,1,0,1)) {index = 52; cdr[0] = true;}

		if (sequenceEquals(seq, 2,2,2,2)) {index = 16; cdr[0] = true; cdr[1] = true; cdr[2] = true; cdr[3] = true;}

		if (sequenceEquals(seq, 1,2,2,2)) {index = 17; cdr[1] = true; cdr[2] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 2,1,2,2)) {index = 18; cdr[2] = true; cdr[3] = true; cdr[0] = true;}
		if (sequenceEquals(seq, 2,2,1,2)) {index = 19; cdr[3] = true; cdr[0] = true; cdr[1] = true;}
		if (sequenceEquals(seq, 2,2,2,1)) {index = 20; cdr[0] = true; cdr[1] = true; cdr[2] = true;}

		if (sequenceEquals(seq, 1,2,1,2)) {index = 21; cdr[1] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 2,1,2,1)) {index = 22; cdr[2] = true; cdr[0] = true;}

		if (sequenceEquals(seq, 1,1,2,2)) {index = 23; cdr[2] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 2,1,1,2)) {index = 24; cdr[0] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 2,2,1,1)) {index = 25; cdr[0] = true; cdr[1] = true;}
		if (sequenceEquals(seq, 1,2,2,1)) {index = 26; cdr[1] = true; cdr[2] = true;}

		if (sequenceEquals(seq, 1,1,1,2)) {index = 27; cdr[3] = true;}
		if (sequenceEquals(seq, 2,1,1,1)) {index = 28; cdr[0] = true;}
		if (sequenceEquals(seq, 1,2,1,1)) {index = 29; cdr[1] = true;}
		if (sequenceEquals(seq, 1,1,2,1)) {index = 30; cdr[2] = true;}
		
		t.setConnections(cdr);
		t.setConnectionIndex(index);
		return index;
	}
	
	public static int setConnectIndexGrass(Tile t, Item connectItem) {
		Tile[] nTile  = getNeighborTiles(t);
		boolean[] n   = getNeighbors(t, t.getItem());
		boolean[] b   = getNeighbors(t, connectItem);
		boolean[] cdr = new boolean[4];
		int index     = 0;
		int x = t.getX();
		int y = t.getY();
		
		boolean[] m1 = new boolean[4];
		boolean[] m2 = new boolean[4];
		boolean[] b1 = new boolean[4];
		boolean[] b2 = new boolean[4];
		
		m1[0] = getNeighbor(t, t.getItem(), x + 1, y);     b1[0] = getNeighbor(t, connectItem, x + 1, y);
		m1[1] = getNeighbor(t, t.getItem(), x, y - 1);     b1[1] = getNeighbor(t, connectItem, x, y - 1);
		m1[2] = getNeighbor(t, t.getItem(), x - 1, y);     b1[2] = getNeighbor(t, connectItem, x - 1, y);
		m1[3] = getNeighbor(t, t.getItem(), x, y + 1);     b1[3] = getNeighbor(t, connectItem, x, y + 1);
		m2[0] = getNeighbor(t, t.getItem(), x + 1, y - 1); b2[0] = getNeighbor(t, connectItem, x + 1, y - 1);
		m2[1] = getNeighbor(t, t.getItem(), x - 1, y - 1); b2[1] = getNeighbor(t, connectItem, x - 1, y - 1);
		m2[2] = getNeighbor(t, t.getItem(), x - 1, y + 1); b2[2] = getNeighbor(t, connectItem, x - 1, y + 1);
		m2[3] = getNeighbor(t, t.getItem(), x + 1, y + 1); b2[3] = getNeighbor(t, connectItem, x + 1, y + 1);
		
		int[] seq = {0, 0, 0, 0};
		seq[0] += (m1[0] ? 1 : 0) + (b[0] ? 2 : 0);
		seq[1] += (m1[1] ? 1 : 0) + (b[1] ? 2 : 0);
		seq[2] += (m1[2] ? 1 : 0) + (b[2] ? 2 : 0);
		seq[3] += (m1[3] ? 1 : 0) + (b[3] ? 2 : 0);
		
		if (m1[0]) index = 1;
		if (m1[1]) index = 2;
		if (m1[2]) index = 3;
		if (m1[3]) index = 4;
		if (m1[0] && m1[2]) index = 5;
		if (m1[1] && m1[3]) index = 6;
		if (m1[0] && m1[1]) index = 7;
		if (m1[0] && m1[1] && !m2[0] && !b2[0]) index = 57;
		if (m1[1] && m1[2]) index = 8;
		if (m1[1] && m1[2] && !m2[1] && !b2[1]) index = 58;
		if (m1[2] && m1[3]) index = 9;
		if (m1[2] && m1[3] && !m2[2] && !b2[2]) index = 59;
		if (m1[3] && m1[0]) index = 10;
		if (m1[3] && m1[0] && !m2[3] && !b2[3]) index = 60;
		if (m1[0] && m1[1] && m1[2]) index = 11;
		if (m1[0] && m1[1] && m1[2] && !m2[0] && !b2[0]) index = 63;
		if (m1[0] && m1[1] && m1[2] && !m2[1] && !b2[1]) index = 64;
		if (m1[0] && m1[1] && m1[2] && !m2[0] && !b2[0] && !m2[1] && !b2[1]) index = 69;
		if (m1[1] && m1[2] && m1[3]) index = 12;
		if (m1[1] && m1[2] && m1[3] && !m2[1] && !b2[1]) index = 66;
		if (m1[1] && m1[2] && m1[3] && !m2[2] && !b2[2]) index = 68;
		if (m1[1] && m1[2] && m1[3] && !m2[1] && !b2[1] && !m2[2] && !b2[2]) index = 70;
		if (m1[2] && m1[3] && m1[0]) index = 13;
		if (m1[2] && m1[3] && m1[0] && !m2[2] && !b2[2]) index = 62;
		if (m1[2] && m1[3] && m1[0] && !m2[3] && !b2[3]) index = 61;
		if (m1[2] && m1[3] && m1[0] && !m2[2] && !b2[2] && !m2[3] && !b2[3]) index = 71;
		if (m1[3] && m1[0] && m1[1]) index = 14;
		if (m1[3] && m1[0] && m1[1] && !m2[3] && !b2[3]) index = 67;
		if (m1[3] && m1[0] && m1[1] && !m2[0] && !b2[0]) index = 65;
		if (m1[3] && m1[0] && m1[1] && !m2[3] && !b2[3] && !m2[0] && !b2[0]) index = 72;
		if (m1[0] && m1[1] && m1[2] && m1[3]) index = 15;
		if (m1[0] && m1[1] && m1[2] && m1[3] && !m2[0] && !b2[0] && !m2[1] && !b2[1] && !m2[2] && !b2[2] && !m2[3] && !b2[3]) index = 73;

		if( (m1[0] || b1[0]) && (m1[1] || b1[1]) && (m1[2] || b1[2]) && (m1[3] || b1[3]) ) {index = 16; cdr[0] = true; cdr[1] = true; cdr[2] = true; cdr[3] = true;};

		if (sequenceEquals(seq, 2,0,0,0))  {index = 31; cdr[0] = true;}
		if (sequenceEquals(seq, 0,2,0,0))  {index = 32; cdr[1] = true;}
		if (sequenceEquals(seq, 0,0,2,0))  {index = 33; cdr[2] = true;}
		if (sequenceEquals(seq, 0,0,0,2))  {index = 34; cdr[3] = true;}
		if (sequenceEquals(seq, 2,0,1,0))  {index = 35; cdr[0] = true;}
		if (sequenceEquals(seq, 1,0,2,0))  {index = 36; cdr[2] = true;}
		if (sequenceEquals(seq, 2,0,2,0))  {index = 37; cdr[0] = true; cdr[2] = true;}
		if (sequenceEquals(seq, 0,2,0,1))  {index = 38; cdr[1] = true;}
		if (sequenceEquals(seq, 0,1,0,2))  {index = 39; cdr[3] = true;}
		if (sequenceEquals(seq, 0,2,0,2))  {index = 40; cdr[1] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 1,1,2,0))  {index = 41; cdr[2] = true;}
		if (sequenceEquals(seq, 2,1,1,0))  {index = 42; cdr[0] = true;}
		if (sequenceEquals(seq, 1,2,1,0))  {index = 43; cdr[1] = true;}
		if (sequenceEquals(seq, 0,2,1,1))  {index = 44; cdr[1] = true;}
		if (sequenceEquals(seq, 0,1,1,2))  {index = 45; cdr[3] = true;}
		if (sequenceEquals(seq, 0,1,2,1))  {index = 46; cdr[2] = true;}
		if (sequenceEquals(seq, 1,0,2,1))  {index = 47; cdr[2] = true;}
		if (sequenceEquals(seq, 2,0,1,1))  {index = 48; cdr[0] = true;}
		if (sequenceEquals(seq, 1,0,1,2))  {index = 49; cdr[3] = true;}
		if (sequenceEquals(seq, 1,2,0,1))  {index = 50; cdr[1] = true;}
		if (sequenceEquals(seq, 1,1,0,2))  {index = 51; cdr[3] = true;}
		if (sequenceEquals(seq, 2,1,0,1))  {index = 52; cdr[0] = true;}

		// Ends
		if (sequenceEquals(seq, 1,2,2,0))  {index = 41; cdr[2] = true; cdr[1] = true;}
		if (sequenceEquals(seq, 2,2,1,0))  {index = 42; cdr[0] = true; cdr[1] = true;}
		if (sequenceEquals(seq, 0,2,2,1))  {index = 44; cdr[1] = true; cdr[2] = true;}
		if (sequenceEquals(seq, 0,1,2,2))  {index = 45; cdr[3] = true; cdr[2] = true;}
		if (sequenceEquals(seq, 1,0,2,2))  {index = 47; cdr[2] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 2,0,1,2))  {index = 48; cdr[0] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 2,2,0,1))  {index = 50; cdr[1] = true; cdr[0] = true;}
		if (sequenceEquals(seq, 2,1,0,2))  {index = 51; cdr[3] = true; cdr[0] = true;}

		// Corners
		if (sequenceEquals(seq, 2,1,0,0))  {index = 7;  cdr[0] = true;}
		if (sequenceEquals(seq, 1,2,0,0))  {index = 7;  cdr[1] = true;}
		if (sequenceEquals(seq, 2,2,0,0))  {index = 7;  cdr[0] = true; cdr[1] = true;}
		if (sequenceEquals(seq, 0,2,1,0))  {index = 8;  cdr[1] = true;}
		if (sequenceEquals(seq, 0,1,2,0))  {index = 8;  cdr[2] = true;}
		if (sequenceEquals(seq, 0,2,2,0))  {index = 8;  cdr[1] = true; cdr[2] = true;}
		if (sequenceEquals(seq, 0,0,2,1))  {index = 9;  cdr[2] = true;}
		if (sequenceEquals(seq, 0,0,1,2))  {index = 9;  cdr[3] = true;}
		if (sequenceEquals(seq, 0,0,2,2))  {index = 9;  cdr[2] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 1,0,0,2))  {index = 10; cdr[3] = true;}
		if (sequenceEquals(seq, 2,0,0,1))  {index = 10; cdr[0] = true;}
		if (sequenceEquals(seq, 2,0,0,2))  {index = 10; cdr[3] = true; cdr[0] = true;}

		// ALL Inside Corners
		if ((m1[0] || b1[0]) && (m1[1] || b1[1]) && (m1[2] || b1[2]) && (m1[3] || b1[3])) {
		    if( m1[0] && m1[1] && !m2[0] && !b2[0] && (m2[1] || b2[1]) && (m2[2] || b2[2]) && (m2[3] || b2[3]) ) {index = 74;  cdr[2] = true; cdr[3] = true;}
		    if( m1[1] && m1[2] && !m2[1] && !b2[1] && (m2[2] || b2[2]) && (m2[3] || b2[3]) && (m2[0] || b2[0]) ) {index = 75;  cdr[3] = true; cdr[0] = true;}
		    if( m1[2] && m1[3] && !m2[2] && !b2[2] && (m2[3] || b2[3]) && (m2[0] || b2[0]) && (m2[1] || b2[1]) ) {index = 76;  cdr[0] = true; cdr[1] = true;}
		    if( m1[3] && m1[0] && !m2[3] && !b2[3] && (m2[0] || b2[0]) && (m2[1] || b2[1]) && (m2[2] || b2[2]) ) {index = 77;  cdr[1] = true; cdr[2] = true;}
		    
		    if( m1[0] && m1[1] && m1[2] && !m2[0] && !b2[0] && !m2[1] && !b2[1] && (m2[3] || b2[2]) && (m2[3] || b2[3]) ) {index = 78;  cdr[3] = true;}
		    if( m1[1] && m1[2] && m1[3] && !m2[1] && !b2[1] && !m2[2] && !b2[2] && (m2[2] || b2[3]) && (m2[0] || b2[0]) ) {index = 79;  cdr[0] = true;}
		    if( m1[2] && m1[3] && m1[0] && !m2[2] && !b2[2] && !m2[3] && !b2[3] && (m2[0] || b2[0]) && (m2[1] || b2[1]) ) {index = 80;  cdr[1] = true;}
		    if( m1[3] && m1[0] && m1[1] && !m2[3] && !b2[3] && !m2[0] && !b2[0] && (m2[1] || b2[1]) && (m2[2] || b2[2]) ) {index = 81;  cdr[2] = true;}
		    
		    if( m1[0] && m1[1] && m1[2] && m1[3] ) {
		        if( !m2[0] && !b2[0] && !m2[1] && !b2[1] && !m2[2] && !b2[2] && (m2[3] || b2[3]) ) index = 82;
		        if( !m2[1] && !b2[1] && !m2[2] && !b2[2] && !m2[3] && !b2[3] && (m2[0] || b2[0]) ) index = 83;
		        if( !m2[2] && !b2[2] && !m2[3] && !b2[3] && !m2[0] && !b2[0] && (m2[1] || b2[1]) ) index = 84;
		        if( !m2[3] && !b2[3] && !m2[0] && !b2[0] && !m2[1] && !b2[1] && (m2[2] || b2[2]) ) index = 85;
		        if( !m2[0] && !b2[0] && (m2[1] || b2[1]) && !m2[2] && !b2[2] && (m2[3] || b2[3]) ) index = 86;
		        if( !m2[1] && !b2[1] && (m2[2] || b2[2]) && !m2[3] && !b2[3] && (m2[0] || b2[0]) ) index = 87;
		        if( !m2[0] && !b2[0] && !m2[1] && !b2[1] && !m2[2] && !b2[2] && !m2[3] && !b2[3] ) index = 88;
		    }
		}
		
		if (sequenceEquals(seq, 2,1,2,0))  {index = 53; cdr[0] = true; cdr[2] = true;}
		if (sequenceEquals(seq, 2,2,2,0))  {index = 53; cdr[0] = true; cdr[1] = true; cdr[2] = true;}
		if (sequenceEquals(seq, 0,2,1,2))  {index = 54; cdr[1] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 0,2,2,2))  {index = 54; cdr[1] = true; cdr[2] = true; cdr[3] = true;}
		if (sequenceEquals(seq, 2,0,2,1))  {index = 55; cdr[2] = true; cdr[0] = true;}
		if (sequenceEquals(seq, 2,0,2,2))  {index = 55; cdr[2] = true; cdr[3] = true; cdr[0] = true;}
		if (sequenceEquals(seq, 1,2,0,2))  {index = 56; cdr[3] = true; cdr[1] = true;}
		if (sequenceEquals(seq, 2,2,0,2))  {index = 56; cdr[3] = true; cdr[0] = true; cdr[1] = true;}
		
		if (sequenceEquals(seq, 2,2,2,2))  {index = 16; cdr[0] = true; cdr[1] = true; cdr[2] = true; cdr[3] = true;}
		
		t.setConnections(cdr);
		t.setConnectionIndex(index);
		return index;
	}
	
	private static boolean sequenceEquals(int[] sequence, int i0, int i1, int i2, int i3) {
		return (sequence[0] == i0 && sequence[1] == i1 && sequence[2] == i2 && sequence[3] == i3);
	}
	
	private static boolean[] getNeighbors(Tile t, Item connectItem) {
		boolean[] neighbors = new boolean[4];
		neighbors[0] = getNeighbor(t, connectItem, t.getX() + 1, t.getY());
		neighbors[1] = getNeighbor(t, connectItem, t.getX(), t.getY() - 1);
		neighbors[2] = getNeighbor(t, connectItem, t.getX() - 1, t.getY());
		neighbors[3] = getNeighbor(t, connectItem, t.getX(), t.getY() + 1);
		return neighbors;
	}
	
	private static Tile[] getNeighborTiles(Tile t) {
		Tile[] neighbors = new Tile[4];
		Grid g = t.getGrid();
		neighbors[0] = g.get(t.getX() + 1, t.getY());
		neighbors[1] = g.get(t.getX(), t.getY() - 1);
		neighbors[2] = g.get(t.getX() - 1, t.getY());
		neighbors[3] = g.get(t.getX(), t.getY() + 1);
		return neighbors;
	}
	
	private static boolean getNeighbor(Tile t, Item connectItem, int x, int y) {
		if (t.getGrid().get(x, y) == null)
			return false;
		return (t.getGrid().get(x, y).getBaseItem() == connectItem.getBaseItem());
	}
	
	private static boolean getNeighborTile(Tile t, Item connectItem, int x, int y) {
		if (t.getGrid().get(x, y) == null)
			return false;
		return (t.getGrid().get(x, y).getBaseItem() == connectItem.getBaseItem());
	}
	
	public ConnectionScheme(int[] points) {
		offsets = new Point[points.length / 2];
		
		if (points.length % 2 != 0)
			System.out.println("Odd number of connection scheme points!");
		else {
    		for (int i = 0; i < points.length; i += 2)
    			offsets[i / 2] = new Point(points[i], points[i + 1]);
		}
	}
	
	public Point getOffset(int index) {
		return offsets[index];
	}
	
	private Point[] offsets;



	public static void setPlacementSubimage(Tile t) {
		Item item = t.getItem();

		if (item == ItemData.SHORT_WEEDS || item == ItemData.TALL_WEEDS) {
			
			Tile t2 = t.getGrid().get(t.getX(), t.getY() + (item == ItemData.TALL_WEEDS ? 2 : 1));
			if (t2 != null && t2.getItem() == ItemData.CLAY_POT) {
				t.setRandomSubimage(6 + GMath.random.nextInt(2));
			}
			else
				t.setRandomSubimage(GMath.random.nextInt(item.getRandomSubimageCount()));
		}
		else if (item.getRandomSubimageCount() > 0)
			t.setRandomSubimage(GMath.random.nextInt(item.getRandomSubimageCount()));
	}
}
