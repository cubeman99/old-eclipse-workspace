package zelda.common.graphics;

import zelda.common.geometry.Point;
import zelda.common.graphics.AnimationFrame.FramePart;

public class TileAnimations {
	public static Animation FLOWERS;
	
	public static Animation WATER;
	public static Animation WATER_DEEP;
	public static Animation OCEAN;
	public static Animation OCEAN_SHORE;
	public static Animation PUDDLE;
	public static Animation WATERFALL;
	public static Animation WATERFALL_BOTTOM;
	public static Animation WATERFALL_TOP;
	public static Animation[] WATER_CURRENTS;
	
	public static FrameGroup[] LAVA;
	public static Animation LAVA_BASIC;
	public static Animation LAVA_HARDENED;
	public static Animation LAVAFALL;
	public static Animation LAVAFALL_BOTTOM;
	
	public static Animation SPIKES;
	public static Animation[] WALL_TORCHES;
	
	
	public static void initialize() {
		
		WATER         = quadGroup(11, 4, 4, 16);
		WATER_DEEP    = quadGroup(11, 5, 4, 16);
		WATERFALL     = quadGroup(11, 6, 4, 8);
		LAVAFALL      = quadGroup(11, 8, 4, 8);
		LAVA_HARDENED = quadGroup(18, 15, 1, 1);
		PUDDLE        = quadGroup(15, 10, 3, 16);
		PUDDLE.addFrame(PUDDLE.getFrame(1));
		
		LAVA = new FrameGroup[5];
		for (int i = 0; i < LAVA.length; i++)
			LAVA[i] = new FrameGroup(11, 10 + i, 4, 16);
		

		LAVA_BASIC = fromGroups(LAVA[4], LAVA[0], LAVA[2], LAVA[1]);
				
		FrameGroup groupWaterfall = new FrameGroup(11, 6, 4, 8);
		FrameGroup groupWaterfallBottom = new FrameGroup(11, 7, 4, 8);
		FrameGroup[] groupOcean = {new FrameGroup(15, 4, 4, 16, true),
								   new FrameGroup(16, 4, 4, 16, true)};
		FrameGroup[] groupShore = {new FrameGroup(17, 4, 4, 16, true),
				   				   new FrameGroup(18, 4, 4, 16, true)};
		
		WATERFALL_BOTTOM = fromGroups(groupWaterfall, groupWaterfall, groupWaterfallBottom, groupWaterfallBottom);
		OCEAN       = fromGroups(groupOcean[0], groupOcean[1], groupOcean[0], groupOcean[1]);
		OCEAN_SHORE = fromGroups(groupShore[0], groupShore[1], groupOcean[0], groupOcean[1]);
		

		FrameGroup groupFlower = new FrameGroup(15, 9, 4, 16);
		FrameGroup groupGrass  = new FrameGroup(18, 10, 1, 16);
		FLOWERS = fromGroups(groupFlower, groupGrass, groupGrass, groupFlower);
		
		FrameGroup groupTorchLight  = new FrameGroup(11, 15, 4, 16);
		FrameGroup groupTorchHolder[] = new FrameGroup[4];
		for (int i = 0; i < 4; i++)
			groupTorchHolder[i] = new FrameGroup(11 + i, 15, 1, 16);
		

		SPIKES = quadGroup(11, 18, 4, 16);
		WALL_TORCHES = new Animation[4];
		WALL_TORCHES[0] = fromGroups(groupTorchLight, groupTorchHolder[0],
									 groupTorchLight, groupTorchHolder[0]);
		WALL_TORCHES[1] = fromGroups(groupTorchHolder[1], groupTorchHolder[1],
				 					 groupTorchLight, groupTorchLight);
		WALL_TORCHES[2] = fromGroups(groupTorchHolder[2], groupTorchLight,
									 groupTorchHolder[2], groupTorchLight);
		WALL_TORCHES[3] = fromGroups(groupTorchLight, groupTorchLight,
									 groupTorchHolder[3], groupTorchHolder[3]);
	}

	public static Animation fromGroups(FrameGroup... groups) {
		Animation anim = new Animation();
		int length = groups[0].length;
		for (int i = 0; i < length; i++) {
    		AnimationFrame frame = new AnimationFrame(groups[0].frameDuration);
    		int index = 0;

			for (int y = 0; y < 2 && index < groups.length; y++) {
				for (int x = 0; x < 2 && index < groups.length; x++) {
    				FramePart part = new FramePart(groups[index++].getPart(i));
    				part.getDrawPos().add(x * 8, y * 8);
    				frame.addPart(part);
    			}
    		}
    		
    		anim.addFrame(frame);
		}
		return anim;
	}
	
	public static Animation quadGroup(int sx, int sy, int length, int duration) {
    	Animation anim = new Animation();
		for (int i = 0; i < length; i++) {
    		AnimationFrame frame = new AnimationFrame(duration);
    		for (int x = 0; x < 2; x++) {
    			for (int y = 0; y < 2; y++) {
    				frame.addPart(sx + i, sy, x * 8, y * 8);
    			}
    		}
    		anim.addFrame(frame);
		}
		return anim;
	}
	
	public static class FrameGroup {
		public Point sourcePos;
		public int length;
		public int frameDuration;
		public Point relativePos;
		
		public FrameGroup(int sx, int sy, int length, int frameDuration) {
			this(sx, sy, length, frameDuration, 1, 0);
		}
		
		public FrameGroup(int sx, int sy, int length, int frameDuration, boolean vertical) {
			this(sx, sy, length, frameDuration, vertical ? 0 : 1, vertical ? 1 : 0);
		}
		
		public FrameGroup(int sx, int sy, int length, int frameDuration, int relX, int relY) {
			this.sourcePos     = new Point(sx, sy);
			this.length        = length;
			this.frameDuration = frameDuration;
			this.relativePos   = new Point(relX, relY);
		}
		
		public FramePart getPart(int index) {
			return new FramePart(
					sourcePos.x + ((index % length) * relativePos.x),
					sourcePos.y + ((index % length) * relativePos.y),
					0, 0);
		}
	}
}
