package zelda.game.control.text;

import java.awt.Color;
import zelda.common.Font;
import OLD.SpriteOLD;


/**
 * Represents a character that can be used drawn by a font.
 * 
 * @author David Jordan
 */
public class Letter {
	public static final Color COLOR_RED = new Color(232, 8, 24);
	public static final Color COLOR_BLUE = new Color(32, 168, 248);

	private String code;
	private Color color;



	public Letter(String code) {
		this.code = code;
		this.color = null;
	}

	public String getCode() {
		return code;
	}

	public boolean isColored() {
		return (color != null);
	}

	public Color getColor() {
		return color;
	}

	public SpriteOLD getSprite(Font font) {
		return font.getLetterSprite(code);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Letter)
			return (code.equals(((Letter) obj).code));
		if (obj instanceof String)
			return (code.equals((String) obj));
		return false;
	}

	@Override
	public String toString() {
		return code;
	}
}
