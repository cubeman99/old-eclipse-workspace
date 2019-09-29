
public class Connecter {
	public static final int CONNECT_GROUP_GROUND 	= 1;
	public static final int CONNECT_GROUP_TREE 		= 2;
	public static final int CONNECT_GROUP_LEAVES 	= 3;
	
	public static int[] blockConnect(Block b) {
		int x 	 = b.worldx;
		int y 	 = b.worldy;
		ConnectGroup connectgroup = b.template().connectgroup;
		
		boolean c1, c2, c3, c4, c5, c6, c7, c8;
		c1 = c2 = c3 = c4 = c5 = c6 = c7 = c8 = false;
		int mx = GameControl.WORLD_WIDTH - 1;
		int my = GameControl.WORLD_HEIGHT - 1;
		
		int[] subs = new int[4];
		
		if( x < mx ) 			c1 = (GameControl.blocks[x + 1][y].template().connectgroup		== connectgroup);
		if( x < mx && y > 0 ) 	c2 = (GameControl.blocks[x + 1][y - 1].template().connectgroup	== connectgroup);
		if( y > 0 ) 			c3 = (GameControl.blocks[x][y - 1].template().connectgroup		== connectgroup);
		if( x > 0 && y > 0 ) 	c4 = (GameControl.blocks[x - 1][y - 1].template().connectgroup	== connectgroup);
		if( x > 0 ) 			c5 = (GameControl.blocks[x - 1][y].template().connectgroup		== connectgroup);
		if( x > 0 && y < my ) 	c6 = (GameControl.blocks[x - 1][y + 1].template().connectgroup	== connectgroup);
		if( y < my ) 			c7 = (GameControl.blocks[x][y + 1].template().connectgroup		== connectgroup);
		if( x < mx && y < my ) 	c8 = (GameControl.blocks[x + 1][y + 1].template().connectgroup	== connectgroup);
		
		// North East
		subs[0] = 12;
		if( !c1 ) subs[0] = 0;
		if( !c3 ) subs[0] = 1;
		if( c1 && c3 && !c2 ) subs[0] = 8;
		if( !c1 && !c3 ) subs[0] = 4;
		
		// North West
		subs[1] = 12;
		if( !c3 ) subs[1] = 1;
		if( !c5 ) subs[1] = 2;
		if( c3 && c5 && !c4 ) subs[1] = 9;
		if( !c3 && !c5 ) subs[1] = 5;
		
		// South West
		subs[2] = 12;
		if( !c5 ) subs[2] = 2;
		if( !c7 ) subs[2] = 3;
		if( c5 && c7 && !c6 ) subs[2] = 10;
		if( !c5 && !c7 ) subs[2] = 6;
		
		// South East
		subs[3] = 12;
		if( !c7 ) subs[3] = 3;
		if( !c1 ) subs[3] = 0;
		if( c7 && c1 && !c8 ) subs[3] = 11;
		if( !c7 && !c1 ) subs[3] = 7;
		
		return subs;
	}
}
