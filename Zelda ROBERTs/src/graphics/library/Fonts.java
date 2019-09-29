package graphics.library;

import graphics.RasterFont;

/**
 * The library that stores all game raster fonts.
 * @author	Robert Jordan
 */
public class Fonts {

	// ======================= Members ========================
	
	public RasterFont fontSmall;
	public RasterFont fontLarge;

	// ===================== Constructors =====================
	
	/** Constructs the raster fonts library. */
	public Fonts() {
		fontSmall = new RasterFont("font_small", 8, 8, 1, 1, 0, 0, 128, (char)18);
		fontLarge = new RasterFont("font_large", 8, 12, 1, 1, 0, 0, 128, (char)18);
	}
}