package graphics.library;

import java.awt.Color;

import graphics.Palette;
import graphics.Spectrum;
import graphics.Tileset;

/**
 * The library that stores all game palettes.
 * @author	Robert Jordan
 */
public class Palettes {

	// ======================= Members ========================
	
	public Spectrum spectrumSprites;
	public Spectrum spectrumOverworld;
	
	public Palette green;
	public Palette blue;
	public Palette red;
	public Palette orange;
	public Palette greenShaded;
	public Palette blueShaded;
	public Palette redShaded;
	public Palette purpleShaded;
	public Palette grayShaded;
	public Palette goldShaded;
	public Palette redInverted;
	public Palette blueInverted;
	
	public Tileset spectrumSpritesTileset;

	// ===================== Constructors =====================
	
	/** Constructs the palettes library. */
	public Palettes() {
		
		definePalettes();
		defineSpectrums();
		
	}
	/** Called to define all color palettes. */
	private void definePalettes() {
		green			= new Palette(rgb(248, 208, 136), rgb( 16, 168,  64), rgb(  0,   0,   0));
		blue			= new Palette(rgb(248, 208, 136), rgb( 24, 128, 248), rgb(  0,   0,   0));
		red				= new Palette(rgb(248, 208, 136), rgb(248,   8,  40), rgb(  0,   0,   0));
		orange			= new Palette(rgb(248, 208, 136), rgb(248, 120,   8), rgb(  0,   0,   0));

		greenShaded		= new Palette(rgb(  0, 168,   0), rgb(  0,  80,   0), rgb(  0,   0,   0));
		blueShaded		= new Palette(rgb(112, 168, 248), rgb(  0,   0, 248), rgb(  0,   0,   0));
		redShaded		= new Palette(rgb(248, 176,  48), rgb(216,   0,   0), rgb(  0,   0,   0));
		purpleShaded	= new Palette(rgb(216, 128, 160), rgb(160,   0,  88), rgb(  0,   0,   0));
		grayShaded		= new Palette(rgb(216, 216, 248), rgb(136, 136, 200), rgb(  0,   0,   0));
		goldShaded		= new Palette(rgb(248, 208, 136), rgb(248, 176,  48), rgb(  0,   0,   0));
		
		redInverted		= new Palette(rgb(  0,   0,   0), rgb(216,   0,   0), rgb(248, 176,  48));
		blueInverted	= new Palette(rgb(  0,   0,   0), rgb(  0,   0, 248), rgb(112, 168, 248));
		
	}
	/** Called to define all color spectrums. */
	private void defineSpectrums() {
		
		spectrumSprites = new Spectrum();
		spectrumSprites.addPalette(green);
		spectrumSprites.addPalette(blue);
		spectrumSprites.addPalette(red);
		spectrumSprites.addPalette(orange);

		spectrumSprites.addPalette(greenShaded);
		spectrumSprites.addPalette(blueShaded);
		spectrumSprites.addPalette(redShaded);
		spectrumSprites.addPalette(purpleShaded);
		spectrumSprites.addPalette(grayShaded);
		spectrumSprites.addPalette(goldShaded);

		spectrumSprites.addPalette(redInverted);
		spectrumSprites.addPalette(blueInverted);
		
		
		spectrumOverworld = new Spectrum();
	}
	/** A quick macro for creating a color. */
	private Color rgb(int r, int g, int b) {
		return new Color(r, g, b);
	}
}