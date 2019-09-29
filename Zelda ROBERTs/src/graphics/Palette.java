package graphics;

import java.awt.Color;

/**
 * A class that contains color information about specific tiles and sprites.
 * @author	Robert Jordan
 */
public class Palette {

	// ====================== Constants =======================
	
	/** The number of different colors can be in each palette. */
	public static final int NUM_COLORS	= 4;
	
	// ======================= Members ========================

	/** The different colors used by the palette. */
	private Color[] colors;
	
	// ===================== Constructors =====================
	
	/** Constructs the default palette. */
	public Palette() {
		this.colors = new Color[NUM_COLORS];
		
		for (int i = 0; i < NUM_COLORS; i++) {
			this.colors[i] = Color.WHITE;
		}
	}
	/** Constructs a palette with the given colors. */
	public Palette(Color... colors) {
		this.colors = new Color[NUM_COLORS];
		
		for (int i = 0; i < NUM_COLORS; i++) {
			if (colors.length < 4 && i - (4 - colors.length) < 0)
				this.colors[i] = Color.WHITE;
			else if (colors.length < 4)
				this.colors[i] = colors[i - (4 - colors.length)];
			else
				this.colors[i] = colors[i];
		}
	}
	/** Constructs a pallete with the same information as the given palette. */
	public Palette(Palette palette) {
		this.colors = new Color[NUM_COLORS];
		
		for (int i = 0; i < NUM_COLORS; i++) {
			this.colors[i] = palette.colors[i];
		}
	}
	
	// ===================== Information ======================
	
	/** Returns true if the palette has the same colors for each index. */
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Palette) {
				for (int i = 0; i < NUM_COLORS; i++) {
					if (!((Palette)obj).colors[i].equals(colors[i]))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	/** Gets the color at the specified index. */
	public Color getColor(int index) {
		return colors[index];
	}
	/** Sets the color at the specified index. */
	public void setColor(int index, Color color) {
		colors[index] = color;
	}
	/** Gets the list of palette colors. */
	public Color[] getColors() {
		return colors;
	}
	/** Sets the list of palette colors. */
	public void setColors(Color... colors) {
		this.colors = new Color[NUM_COLORS];
		
		for (int i = 0; i < NUM_COLORS; i++) {
			if (colors.length < 4 && i - (4 - colors.length) < 0)
				this.colors[i] = Color.WHITE;
			else if (colors.length < 4)
				this.colors[i] = colors[i - (4 - colors.length)];
			else
				this.colors[i] = colors[i];
		}
	}
	/** Gets the index of the specified color. */
	public int getIndexOfColor(Color color) {
		for (int i = 0; i < NUM_COLORS; i++) {
			if (color.equals(colors[i]))
				return i;
		}
		return -1;
	}
	/** Returns true if all the colors are in the palette. */
	public boolean isColorInPalette(Color...colors) {
		boolean colorsCorrect = false;
		for (int i = 0; i < colors.length && colorsCorrect; i++) {
			colorsCorrect = false;
			for (int j = 0; j < NUM_COLORS; j++) {
				if (colors[i].equals(this.colors[j])) {
					colorsCorrect = true;
					break;
				}
			}
		}
		return colorsCorrect;
	}
}