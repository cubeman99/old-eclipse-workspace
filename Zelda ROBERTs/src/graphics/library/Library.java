package graphics.library;

/**
 * A static class that stores all graphics information.
 * @author	Robert Jordan
 */
public class Library {
	
	// ====================== Variables =======================

	/** The collection of game color palettes. */
	public static Palettes palettes;
	/** The collection of game animations. */
	public static Animations animations;
	/** The collection of game tilesets. */
	public static Tilesets tilesets;
	/** The collection of game raster fonts. */
	public static Fonts fonts;
	
	// ==================== Initialization ====================
	
	/** Initializes the library collections. */
	public static void initialize() {
		palettes	= new Palettes();
		animations	= new Animations();
		tilesets	= new Tilesets();
		fonts		= new Fonts();
	}
}