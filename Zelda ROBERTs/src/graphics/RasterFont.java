package graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import geometry.Point;

/**
 * A font that relies on images instead of vector graphics to
 * display characters.
 * @author	Robert Jordan
 */
public class RasterFont extends Tileset {

	// ======================= Members ========================
	
	/** The spacing used between characters. */
	protected Point spacing;
	/** The number of characters this font goes up to. */
	protected int numCharacters;
	/** The character index to use as the invalid character. */
	protected char invalidCharacter;

	/** The colorized image. */
	protected BufferedImage colorizedImage;
	/** The current font color. */
	protected Color color;

	// ===================== Constructors =====================
	
	/** Constructs the default raster font with no data. */
	public RasterFont() {
		super();
		this.spacing			= new Point();
		this.numCharacters		= 0;
		this.invalidCharacter	= 0;
		this.color				= new Color(255, 255, 255);
		
		this.colorizedImage		= null;
	}
	/** Constructs a raster font with the given image and dimensions. */
	public RasterFont(String imageName, int tileWidth, int tileHeight, int tileSpacingX, int tileSpacingY, int spacingX, int spacingY,
			int numCharacters, char invalidCharacter) {
		super(imageName, tileWidth, tileHeight, tileSpacingX, tileSpacingY);
		
		this.spacing			= new Point(spacingX, spacingY);
		this.numCharacters		= numCharacters;
		this.invalidCharacter	= invalidCharacter;
		
		this.colorizedImage		= new BufferedImage(tileSheet.getWidth(), tileSheet.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.colorizedImage.getGraphics().drawImage(tileSheet.getImage(), 0, 0, null);
	}
	/** Constructs a raster font with the given image and dimensions. */
	public RasterFont(String imageName, Point tileSize, Point tileSpacing, Point spacing,
			int numCharacters, char invalidCharacter) {
		super(imageName, tileSize, tileSpacing);
		
		this.spacing			= new Point(spacing);
		this.numCharacters		= numCharacters;
		this.invalidCharacter	= invalidCharacter;
		
		this.colorizedImage		= new BufferedImage(tileSheet.getWidth(), tileSheet.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.colorizedImage.getGraphics().drawImage(tileSheet.getImage(), 0, 0, null);
	}

	// ====================== Characters ======================
	
	/** Gets the image containing all the characters. */
	public Image getImage() {
		if (color.equals(new Color(255, 255, 255))) {
			return tileSheet.getImage();
		}
		return colorizedImage;
	}
	/** Sets the font color. */
	public void setColor(Color color) {
		if (!color.equals(this.color)) {
			this.color = color;
			int[] pixels = ((DataBufferInt)colorizedImage.getRaster().getDataBuffer()).getData();
			
			for (int i = 0; i < pixels.length; i++) {
				if (pixels[i] != new Color(0, 0, 0, 0).getRGB()) {
					pixels[i] = this.color.getRGB();
				}
			}
		}
	}
	/** Gets the index of the given character. */
	public Point getCharacterIndex(char character) {
		return new Point(character % 16, character / 16);
	}
	/** Gets the coordinates of the given character. */
	public Point getCharacterPoint(char character) {
		return super.getTilePoint(character % 16, character / 16);
	}
	/** Gets the between the characters. */
	public Point getCharacterSpacing() {
		return new Point(spacing);
	}
}