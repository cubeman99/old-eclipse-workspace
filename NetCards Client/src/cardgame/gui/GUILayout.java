package cardgame.gui;

import java.awt.Color;
import java.awt.Point;
import client.main.Game;

import cardstructure.Card;

public class GUILayout {
	// Category Position:
	public static final Point CATEGORYCARD_POS = new Point(32, 32);
	public static final Point CATEGORYCARD_CENTERED_POS = new Point((Game.VIEW_WIDTH / 2) - (Card.DRAW_WIDTH / 2), 32);
	
	// Submission Position:
	public static final Point SUBMISSION_POS     = new Point(CATEGORYCARD_POS.x + Card.DRAW_WIDTH + 32, CATEGORYCARD_POS.y);
	public static final int SUBMISSION_PADDING   = 4;
	
	// Hand Drawing:
	public static final Point HAND_POS           = new Point(CATEGORYCARD_POS.x, CATEGORYCARD_POS.y + Card.DRAW_HEIGHT + 48);
	public static final int HAND_CARD_XPADDING   = 4;
	public static final int HAND_CARD_YPADDING   = 4;
	public static final int HAND_CARDS_PER_ROW   = 5;

	// Judgement Drawing:
	public static final Point JUDGE_CATEGORYCARD_POS = new Point(32, 220);
	public static final int JUDGE_CARD_XOFFSET       = 32;
	public static final int JUDGE_CARD_XPADDING      = 4;
	public static final int JUDGE_CARD_YPADDING      = 8;
	
	
	// Background Colors:
	public static final Color COLOR_PLAYERLIST_BACKGROUND = new Color(250, 245, 220);
	public static final Color COLOR_BACKGROUND_NORMAL     = new Color(153, 204, 255);
	public static final Color COLOR_BACKGROUND_READY      = new Color(192, 245, 156);
	public static final Color COLOR_BACKGROUND_JUDGE      = new Color(220, 175, 146);
}
