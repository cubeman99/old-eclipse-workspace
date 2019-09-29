import java.util.Random;


public class TerrainGen {
	
	public static int[] generateTerrain(Random rand, int width, int height) {
		
		int chunk_amount = 0;
		int chunk_width = 16;
		for( int i = 0; i < width; i++ ) {
			chunk_amount += 1;
		}
		
		int[] heightmap = new int[(chunk_amount * chunk_width) + 1];
		
		int prev_start = -100;
		
		for( int i = 0; i < chunk_amount; i++ ) {
			int[] chunk_heightmap = new int[chunk_width + 1];
			
			chunk_heightmap[0] = (int)(rand.nextFloat() * (float)height);
			
			if( prev_start >= -10 ) {
				chunk_heightmap[0] = prev_start;
			}
			float r = 1;//GameControl.terrain_spanmax - GameControl.terrain_spanmin;
			chunk_heightmap[0] = Math.min(GameControl.terrain_spanmax, Math.max(GameControl.terrain_spanmin, chunk_heightmap[0]));
			chunk_heightmap[chunk_width] = (int)(chunk_heightmap[0] + (rand.nextFloat() * (r / 5.0f)));
			chunk_heightmap[chunk_width] = Math.min(GameControl.terrain_spanmax, Math.max(GameControl.terrain_spanmin, chunk_heightmap[chunk_width]));
			
			prev_start = chunk_heightmap[chunk_width];
			
			
			
			for( int L = chunk_width + 1; L >= 3; L = (L + 1) / 2 ) {
				float random_scale = (float)L / 6.0f;
				random_scale *= random_scale;
				//random_scale = 0.0f;
				for( int x = 0; x <= chunk_width + 1 - L; x += L - 1 ) {
					int hL = (int)(L / 2.0f);
					chunk_heightmap[x + hL] = (int)(((float)chunk_heightmap[x] + (float)chunk_heightmap[x + L - 1]) / 2.0f);
					chunk_heightmap[x + hL] += random_scale - (int)(rand.nextFloat() * random_scale * 2.0f);
				}
			}
			
			for( int j = 0; j < chunk_width; j++ )
				heightmap[(i * chunk_width) + j] = chunk_heightmap[j];
		}
		
		return heightmap;
	}
}
