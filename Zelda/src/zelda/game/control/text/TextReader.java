package zelda.game.control.text;

import java.awt.Color;
import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.game.control.GameInstance;
import zelda.game.player.Player;
import zelda.main.Keyboard;


public class TextReader {
	private static final int LINE_LENGTH = 16;
	private static final Point[] BOX_POSITIONS = {new Point(8, 8),
			new Point(8, 80)};
	private static final Point POS_LINE1 = new Point(8, 4);
	private static final Point POS_LINE2 = new Point(8, 20);
	private static final Point POS_TRANSITION = new Point(8, 12);
	private static final int MAX_NUM_QUESTIONS = 4;

	private GameInstance game;
	private ArrayList<TextLine> lines;
	private boolean reading;
	private Message message;
	private int lineIndex;
	private int topLineIndex;
	private Point boxPos;
	private int transitionTimer;
	private boolean firstStep;
	private Sprite spriteArrow;
	private int[] optionPositions;
	private int numOptions;
	private int optionIndex;
	private boolean askingQuestion;
	private int wordIndex;


	// ================== CONSTRUCTORS ================== //

	public TextReader(GameInstance game) {
		this.game = game;
		lines = new ArrayList<TextReader.TextLine>();
		lineIndex = 0;
		topLineIndex = 0;
		reading = false;
		message = null;
		boxPos = new Point(8, 80);
		transitionTimer = 0;
		firstStep = true;
		optionPositions = new int[MAX_NUM_QUESTIONS];
		numOptions = 0;
		optionIndex = 0;
		askingQuestion = false;
		spriteArrow = new Sprite(Resources.SHEET_MENU_SMALL, new Animation()
				.addFrame(16, -1, -1).addFrame(16, 4, 5));
	}



	// =================== ACCESSORS =================== //

	public boolean isReading() {
		return reading;
	}



	// ==================== MUTATORS ==================== //

	public void readMessage(Message msg, Player player) {
		message = msg;
		reading = true;
		firstStep = true;
		lineIndex = 0;
		topLineIndex = 0;
		lines.clear();
		double py = player.getPosition().y
				- player.getGame().getView().getPosition().y;
		if (py < ((Settings.VIEW_SIZE.y - 16) / 2) + 1)
			boxPos.set(BOX_POSITIONS[1]);
		else
			boxPos.set(BOX_POSITIONS[0]);


		// Scan text.
		MessageScanner scanner = new MessageScanner(msg);
		LetterString lineStr = new LetterString();

		while (scanner.hasNextWord()) {
			LetterString word = scanner.nextWord();

			if (word.isLineBreak()) {
				lines.add(new TextLine(lineStr));
				lineStr.clear();
			}
			else if (lineStr.length() + word.length() >= LINE_LENGTH) {
				lines.add(new TextLine(lineStr));
				lineStr.clear();
				lineStr.addLetters(word);
			}
			else {
				if (lineStr.length() > 0)
					lineStr.addLetter(" ");
				lineStr.addLetters(word);
			}
		}
		if (lineStr.length() > 0)
			lines.add(new TextLine(lineStr));
		if (new LetterString(msg.getText()).length() == 0)
			lines.add(new TextLine().complete());
		
		wordIndex = 0;

		// Make question text.
		optionIndex = 0;
		askingQuestion = false;
		numOptions = msg.getOptions().length;
		LetterString[] opts = new LetterString[numOptions];
		for (int i = 0; i < numOptions; i++)
			opts[i] = new LetterString(msg.getOptions()[i]);

		if (opts.length > 0) {
			optionPositions[0] = 2;
			optionPositions[1] = optionPositions[0] + opts[0].length() + 2;

			LetterString str = new LetterString();
			for (int i = 0; i < optionPositions[0]; i++)
				str.addLetter(" ");
			str.addLetters(opts[0]);
			if (numOptions > 1) {
				for (int i = str.length(); i < optionPositions[1]; i++)
					str.addLetter(" ");
				str.addLetters(opts[1]);
			}
			lines.add(new TextLine(str));
		}
		if (opts.length > 2) {
			optionPositions[2] = 2;
			optionPositions[3] = optionPositions[2] + opts[2].length() + 2;

			LetterString str = new LetterString();
			for (int i = 0; i < optionPositions[2]; i++)
				str.addLetter(" ");
			str.addLetters(opts[2]);
			if (numOptions > 3) {
				for (int i = str.length(); i < optionPositions[3]; i++)
					str.addLetter(" ");
				str.addLetters(opts[3]);
			}
			lines.add(new TextLine(str));
		}
	}

	public void update() {

		if (reading) {
			if (transitionTimer > 0) {
				transitionTimer--;
			}
			else if (lineIndex + 1 >= lines.size()
					&& lines.get(lines.size() - 1).isLoaded()) {
				if (numOptions > 0) {
					askingQuestion = true;

					if (Keyboard.right.pressed())
						optionIndex = (optionIndex + 1) % numOptions;
					else if (Keyboard.left.pressed())
						optionIndex = (optionIndex + numOptions - 1)
								% numOptions;
					else if ((Keyboard.up.pressed() || Keyboard.down.pressed())
							&& numOptions > 2) {
						if (numOptions > 3)
							optionIndex = (optionIndex + 2) % numOptions;
						else if (optionIndex % 2 == 0)
							optionIndex = (optionIndex == 0 ? 2 : 0);
						else
							optionIndex = 0;
					}
					else if (Keyboard.b.pressed())
						optionIndex = numOptions - 1;

					for (int i = 0; i < numOptions; i++) {
						int x = optionPositions[i] - 1;
						int y = (numOptions > 2 ? i / 2 : 1);
						TextLine tl = lines.get(lines.size() - 2 + y);
						tl.setLetter(x, (i == optionIndex ? "<trir>" : " "));
					}
				}

				if ((Keyboard.anyKey.pressed() && !askingQuestion)
						|| Keyboard.a.pressed())
				{
					reading = false;
					
					if (optionIndex == 0)
						message.option1();
					else if (optionIndex == 1)
						message.option2();
					else if (optionIndex == 2)
						message.option3();
					else if (optionIndex == 3)
						message.option4();
				}
			}
			else if (lines.get(lineIndex).isLoaded()) {
				if (topLineIndex % 2 == 1 || lineIndex == 0) {
					lineIndex++;
					if (lineIndex > topLineIndex + 1
							&& lineIndex + 1 <= lines.size()) {
						topLineIndex++;
						transitionTimer = 2;
					}
				}
				else {
					spriteArrow.update();

					if ((Keyboard.a.pressed() || Keyboard.b.pressed())
							&& lineIndex + 1 < lines.size()) {
						spriteArrow.setFrameIndex(0);
						lineIndex++;
						Sounds.TEXT_CONTINUE.play();
						if (lineIndex > topLineIndex + 1) {
							topLineIndex++;
							transitionTimer = 2;
						}
					}
				}
			}
			else {
				TextLine line = lines.get(lineIndex);
				int index = lines.get(lineIndex).getLoadedIndex();
				boolean isLetter = !line.getLetter(index).getCode().equals(" ");
				if (isLetter)
					wordIndex++;
				if (Sounds.TEXT_CONTINUE.isPlaying() && index < 7)
					wordIndex = 0;
				else if (isLetter && wordIndex % 4 == 0)
					Sounds.TEXT_READ.play();
				
				if (!firstStep && (Keyboard.a.pressed() || Keyboard.b.pressed()))
					line.completeLoading();
				else
					line.loadLetters();
			}

			firstStep = false;
		}
	}

	private void drawLine(int lineIndex, Point pos) {
		LetterString str = lines.get(lineIndex).getLetterString();
		Draw.drawText(str, boxPos.plus(pos), Resources.FONT_TEXT, new Color(
				248, 208, 136));
	}

	public void draw() {
		if (reading) {
			Resources.FONT_TEXT.setColor(new Color(248, 208, 136));
			Draw.setViewPosition(new Vector(0, -16));

			// TODO
			Draw.setColor(Color.BLACK);
			Draw.fillRect(new Rectangle(boxPos, new Point(144, 40)));

			drawLine(topLineIndex, (transitionTimer > 0 ? POS_TRANSITION
					: POS_LINE1));

			if (topLineIndex + 1 < lines.size() && transitionTimer == 0) {
				drawLine(topLineIndex + 1, POS_LINE2);
			}

			Draw.drawSprite(spriteArrow, boxPos.plus(136, 32));
		}
	}

	private class TextLine {
		private Letter[] letters;
		private int numLetters;
		private double letterIndex;

		public TextLine() {
			letters = new Letter[LINE_LENGTH];
			numLetters = 0;
			letterIndex = 0;
		}

		public TextLine(LetterString str) {
			letters = new Letter[LINE_LENGTH];
			numLetters = Math.min(letters.length, str.length());
			letterIndex = 0;
			for (int i = 0; i < letters.length; i++) {
				if (i < numLetters)
					letters[i] = str.getLetter(i);
				else
					letters[i] = new Letter(" ");
			}
		}

		public int getLength() {
			return numLetters;
		}

		public LetterString getLetterString() {
			return new LetterString(letters).substring(0, (int) letterIndex);
		}

		public Letter getLetter(int index) {
			return letters[index];
		}

		public boolean isLoaded() {
			return (letterIndex >= numLetters);
		}

		public int getLoadedIndex() {
			return (int) letterIndex;
		}

		public void loadLetters() {
			letterIndex += 0.5;
		}

		public void completeLoading() {
			letterIndex = numLetters;
		}

		public void addLetter(Letter l) {
			letters[numLetters++] = l;
		}

		public void setLetter(int index, String code) {
			letters[index] = new Letter(code);
		}

		public TextLine complete() {
			for (int i = numLetters; i < letters.length; i++)
				letters[i] = new Letter(" ");
			return this;
		}

		@Override
		public String toString() {
			String str = "";
			for (int i = 0; i < letters.length; i++) {
				str += letters[i].getCode();
			}
			return str;
		}
	}
}
