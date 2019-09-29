package projects.bezierCurves;

import java.util.Random;
import cmg.math.GMath;

public class TerrainGen {

	
	public static float[][] generateTerrain(int dimensions) {
		float[][] terrain = new float[dimensions][dimensions];
		Random rand = GMath.random;
		
		for( int x = 0; x < dimensions; x++ ) {
			for( int y = 0; y < dimensions; y++ ) {
				terrain[x][y] = 2;
				terrain[x][y] = rand.nextFloat() * ((float)dimensions - 1.0f);
			}
		}
		
		return terrain;
	}
	
	
	public static float[][] generateTerrainDiamondSquare(int dimensions, int depth) {
		float[][] terrain = new float[dimensions][dimensions];
		Random rand = GMath.random;

		terrain[0][0]                           = rand.nextFloat() * (depth - 1);
		terrain[0][dimensions - 1]              = rand.nextFloat() * (depth - 1);
		terrain[dimensions - 1][0]              = rand.nextFloat() * (depth - 1);
		terrain[dimensions - 1][dimensions - 1] = rand.nextFloat() * (depth - 1);
		
		int L = dimensions;
		for( int i = 0; L >= 3; L = (L + 1) / 2, i += 1 ) {
			for( int x = 0; x <= dimensions - L; x += L - 1 ) {
				for( int y = 0; y <= dimensions - L; y += L - 1 ) {
					int halfL = (L - 1) / 2;
					// Give new points the midpoints of their surroundings
					//  then apply randomness
					float r = (float)L / 2.0f;
					terrain[x + halfL][y]     = ((terrain[x][y] + terrain[x + L - 1][y]) / 2.0f) + ((-r / 2.0f) + (rand.nextFloat() * r));
					terrain[x][y + halfL]     = ((terrain[x][y] + terrain[x][y + L - 1]) / 2.0f) + ((-r / 2.0f) + (rand.nextFloat() * r));
					terrain[x + halfL][y + L - 1] = ((terrain[x][y + L - 1] + terrain[x + L - 1][y + L - 1]) / 2.0f) + ((-r / 2.0f) + (rand.nextFloat() * r));
					terrain[x + L - 1][y + halfL] = ((terrain[x + L - 1][y] + terrain[x + L - 1][y + L - 1]) / 2.0f) + ((-r / 2.0f) + (rand.nextFloat() * r));
					
					terrain[x + halfL][y + halfL] = (terrain[x + halfL][y] + terrain[x][y + halfL] +
						terrain[x + halfL][y + L - 1] + terrain[x + L - 1][y + halfL]) / 4.0f;
				}
			}
		}
		return terrain;
	}
	
	public static boolean[][][] generateTerrainRandom(int dimensions) {
		boolean[][][] terrain = new boolean[dimensions][dimensions][dimensions];
		Random rand = GMath.random;
		
		for( int x = 0; x < dimensions; x++ ) {
			for( int y = 0; y < dimensions; y++ ) {
				for( int z = 0; z < dimensions; z++ ) {
					terrain[x][y][z] = rand.nextBoolean();
				}
			}
		}
		
		return terrain;
	}
}
