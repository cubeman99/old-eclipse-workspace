package graphics;

import java.awt.Color;

/**
 * A class to represent a large variety of color palettes.
 * @author	Robert Jordan
 */
public class Spectrum {

	// ====================== Constants =======================
	
	// ====================== Variables =======================
	
	// ======================= Members ========================
	
	/** The collection of palettes in the spectrum. */
	protected Palette[] palettes;
	
	// ===================== Constructors =====================
	
	/** Constructs the default spectrum. */
	public Spectrum() {
		
		this.palettes = new Palette[0];
	}
	/** Constructs a spectrum with the same information as the given spectrum. */
	public Spectrum(Spectrum spectrum) {
		
		this.palettes = new Palette[spectrum.palettes.length];
		
		for (int i = 0; i < palettes.length; i++) {
			this.palettes[i] = new Palette(spectrum.palettes[i]);
		}
	}
	
	// ===================== Information ======================

	/** Returns true if the spectrum has the same palettes for each index. */
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Spectrum) {
				if (((Spectrum)obj).palettes.length == palettes.length) {
					for (int i = 0; i < palettes.length; i++) {
						if (!((Spectrum)obj).palettes[i].equals(palettes[i]))
							return false;
					}
					return true;
				}
			}
		}
		return false;
	}
	/** Adds the specified palette to the spectrum. */
	public void addPalette(Palette palette) {
		Palette[] newPalettes = new Palette[palettes.length + 1];
		
		for (int i = 0; i < palettes.length; i++) {
			newPalettes[i] = palettes[i];
		}
		newPalettes[palettes.length] = new Palette(palette);
		palettes = newPalettes;
	}
	/** Adds the specified palette to the spectrum. */
	public void addPalette(Color... colors) {
		Palette[] newPalettes = new Palette[palettes.length + 1];
		
		for (int i = 0; i < palettes.length; i++) {
			newPalettes[i] = palettes[i];
		}
		newPalettes[palettes.length] = new Palette(colors);
		palettes = newPalettes;
	}
	/** Gets the palette at the specified index. */
	public Palette getPalette(int index) {
		return palettes[index];
	}
	/** Sets the palette at the specified index. */
	public void setPalette(int index, Palette palette) {
		palettes[index] = new Palette(palette);
	}
	/** Gets the list of palettes used in the spectrum. */
	public Palette[] getPalettes() {
		return palettes;
	}
	/** Gets the number of palettes in the spectrum. */
	public int getNumPalettes() {
		return palettes.length;
	}
	/** Returns true if the specified palette is in the spectrum. */
	public boolean isPaletteInSpectrum(Palette palette) {
		for (int i = 0; i < palettes.length; i++) {
			if (palette.equals(palettes[i]))
				return true;
		}
		return false;
	}
	/** Returns true if the specified palette is in the spectrum. */
	public boolean isPaletteInSpectrum(Color... colors) {
		for (int i = 0; i < palettes.length; i++) {
			if (palettes[i].isColorInPalette(colors))
				return true;
		}
		return false;
	}
	/** Gets the palette with the matching colors. */
	public Palette getPaletteInSpectrum(Color... colors) {
		for (int i = 0; i < palettes.length; i++) {
			if (palettes[i].isColorInPalette(colors))
				return palettes[i];
		}
		return null;
	}
}
