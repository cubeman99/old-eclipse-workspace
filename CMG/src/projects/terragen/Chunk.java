package projects.terragen;

public class Chunk {
	public static final int SIZE  = 16;
	public static final int DEPTH = 32;
	
	private Terrain terrain;
	private double[][] heightmap;
	private boolean generated;
	

	
	// ================== CONSTRUCTORS ================== //
	
	public Chunk(Terrain terrain) {
		this.terrain = terrain;
		
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				heightmap[x][y] = 1;
			}
		}
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	public double[][] getHeightmap() {
		return heightmap;
	}
	
	public boolean isGenerated() {
		return generated;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	
}
