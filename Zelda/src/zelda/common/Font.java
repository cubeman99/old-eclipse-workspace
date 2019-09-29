package zelda.common;

import java.awt.Color;
import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.game.control.text.Letter;
import zelda.game.control.text.MessageScanner;
import OLD.BufferedSpriteSheetOLD;
import OLD.SpriteOLD;
import OLD.SpriteSheetOLD;


public class Font {
	private String letterSequence;
	private ArrayList<String> letterCodes;
	private SpriteSheetOLD letterSheet;
	private BufferedSpriteSheetOLD colorizedLetterSheet;
	private Color color;



	// ================== CONSTRUCTORS ================== //

	public Font(String imageName, int letterWidth, int letterHeight,
			String sequence) {
		letterSequence = sequence;
		letterCodes = new ArrayList<String>();
		letterSheet = new SpriteSheetOLD(imageName, letterWidth, letterHeight);
		colorizedLetterSheet = new BufferedSpriteSheetOLD(letterSheet);
		color = Color.WHITE;

		MessageScanner scanner = new MessageScanner(letterSequence);
		while (scanner.hasNextLetter())
			letterCodes.add(scanner.nextLetter().getCode());

	}



	// =================== ACCESSORS =================== //

	public Point getLetterSpriteSize() {
		return letterSheet.getSpriteSize();
	}

	public boolean isColorized() {
		return !color.equals(Color.WHITE);
	}

	public SpriteOLD getLetterSprite(String code) {
		int index = letterCodes.indexOf(code);
		if (index >= 0) {
			if (isColorized())
				return colorizedLetterSheet.getSprite(index, 0);
			return letterSheet.getSprite(index, 0);
		}
		return getLetterSprite("?");
	}

	public SpriteOLD[] getTextLetterSprites(String text) {
		ArrayList<Letter> letters = new ArrayList<Letter>();
		MessageScanner scanner = new MessageScanner(text);
		while (scanner.hasNextLetter())
			letters.add(scanner.nextLetter());

		SpriteOLD[] sprites = new SpriteOLD[letters.size()];

		for (int i = 0; i < letters.size(); i++)
			sprites[i] = getLetterSprite(letters.get(i).getCode());

		return sprites;
	}



	// ==================== MUTATORS ==================== //

	public void setColor(Color c) {
		colorizedLetterSheet.replaceColor(color, c);
		color = c;
	}
}
