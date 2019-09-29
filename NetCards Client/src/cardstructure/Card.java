package cardstructure;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.StringTokenizer;

import client.main.GMath;
import client.main.Game;
import client.main.ImageLoader;
import client.main.Mouse;


public class Card {
	public static final Font CARD_BACK_FONT       = new Font(Font.SANS_SERIF, Font.BOLD, 25);
	public static final String[] DRAW_BACK_STRING = new String[] {"Cards", "Against", "Humanity"};
	public static final Font CARD_FONT            = new Font(Font.SANS_SERIF, Font.BOLD, 11);
	public static final int DRAW_WIDTH            = 140;
	public static final int DRAW_HEIGHT           = (int) ((double) DRAW_WIDTH * 1.4d); // 140 --> 196
	public static final int DRAW_PADDING          = 14;
	public static final int LINE_SPACING          = 14;
	public static final int BACK_LINE_SPACING     = 24;
	
	
	public static final boolean DEBUG_DRAW_GUIDELINES = false;

	public static final double DEFAULT_MOVE_SPEED = 50;
	
	private String text;
	private ArrayList<String> lines;
	private int blanks;
	public int x;
	public int y;
	public int index;
	
	public double moveSpeed = DEFAULT_MOVE_SPEED;
	public Point movePoint  = null;
	
	
	public Card(String text, int blanks) {
		this.text   = text;
		this.blanks = blanks;
		this.x      = 0;
		this.y      = 0;
		this.index  = 0;
		compileLines();
	}
	
	public Card(String text) {
		this(text, 1);
	}
	
	public Card() {
		this("", 1);
	}
	
	public void setPosition(Point pos) {
		this.x = pos.x;
		this.y = pos.y;
	}
	
	public int getBlanks() {
		return blanks;
	}
	
	public String getText() {
		return text;
	}
	
	public String toString() {
		return text;
	}
	
	
	/////////////////////  - CLIENT METHODS -  /////////////////////
	
	public Point getPoint() {
		return new Point(x, y);
	}
	
	public void setMovePosition(Point pos) {
		this.movePoint = new Point(pos);
	}
	
	public void update() {
		if (movePoint != null) {
			double distance = GMath.distance(getPoint(), movePoint);
			if (distance > 3) {
				double direction = GMath.direction(getPoint(), movePoint);
				x += GMath.lengthDirPoint(Math.min(moveSpeed, distance + 2), direction).x;
				y += GMath.lengthDirPoint(Math.min(moveSpeed, distance + 2), direction).y;
			}
			else {
				x = movePoint.x;
				y = movePoint.y;
				movePoint = null;
				moveSpeed = DEFAULT_MOVE_SPEED;
			}
		}
	}
	
	private void compileLines() {
		// Compile the text to add line breaks in order for
		// the text to fit on the card
		this.lines = new ArrayList<String>();
		int innerWidth = getInnerWidth();
		
		StringTokenizer tokenizer = new StringTokenizer(text);
		Graphics g = Game.getBufferGraphics();
		g.setFont(CARD_FONT);
		String str = "";
		
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			if (getTextWidth(g, str + token) < innerWidth || str.equals("")) {
				str += token + " ";
			}
			else {
				// Text goes out of bounds, add a line break
				lines.add(str);
				str = "" + token + " ";
			}
		}
		if (!str.equals(""))
			lines.add(str);
	}
	
	public boolean isMoving() {
		return (movePoint != null);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, DRAW_WIDTH, DRAW_HEIGHT);
	}
	
	private int getTextWidth(Graphics g, String str) {
		FontMetrics metrics = g.getFontMetrics();
		return metrics.stringWidth(str);
	}
	
	private static int getInnerWidth() {
		return (DRAW_WIDTH - (2 * DRAW_PADDING));
	}
	
	private static int getInnerHeight() {
		return (DRAW_HEIGHT - (2 * DRAW_PADDING));
	}
	
	public boolean mouseInArea(int x, int y) {
		return Mouse.inArea(new Rectangle(x, y, DRAW_WIDTH, DRAW_HEIGHT));
	}
	
	public void drawWhiteFront(Graphics g, int x, int y) {
		Image image = ImageLoader.getImage("cardWhite");
		g.drawImage(image, x, y, null);
		drawCardFront(g, x, y, Color.WHITE, Color.BLACK);
	}
	
	public void drawBlackFront(Graphics g, int x, int y) {
		Image image = ImageLoader.getImage("cardBlack");
		g.drawImage(image, x, y, null);
		drawCardFront(g, x, y, Color.BLACK, Color.WHITE);
	}
	
	public void drawWhiteBack(Graphics g, int x, int y) {
		Image image = ImageLoader.getImage("cardWhite");
		g.drawImage(image, x, y, null);
		drawCardBack(g, x, y, Color.WHITE, Color.BLACK);
	}
	
	public void drawBlackBack(Graphics g, int x, int y) {
		Image image = ImageLoader.getImage("cardBlack");
		g.drawImage(image, x, y, null);
		drawCardBack(g, x, y, Color.BLACK, Color.WHITE);
	}
	
	public void drawCardBase(Graphics g, int x, int y, Color col) {
		if (DEBUG_DRAW_GUIDELINES) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawRect(x + DRAW_PADDING, y + DRAW_PADDING, getInnerWidth(), getInnerHeight());
		}
		
		Image image = ImageLoader.getImage("cardWhite");
		g.drawImage(image, x, y, null);
	}
	
	public void drawCardFront(Graphics g, int x, int y, Color background, Color foreground) {
		//drawCardBase(g, x, y, background);
		
		g.setColor(foreground);
		g.setFont(CARD_FONT);
		for (int i = 0; i < lines.size(); i++) {
			g.setColor(foreground);
			int dx = x + DRAW_PADDING;
			int dy = y + DRAW_PADDING + (LINE_SPACING * i) + (int) ((double) LINE_SPACING * 0.6d);
			g.drawString(lines.get(i), dx, dy);
			
			if (DEBUG_DRAW_GUIDELINES) {
				g.setColor(Color.RED);
				g.drawLine(dx, dy, dx + getTextWidth(g, lines.get(i)), dy);
			}
		}
	}
	
	public void drawCardBack(Graphics g, int x, int y, Color background, Color foreground) {
		//drawCardBase(g, x, y, background);
		
		g.setColor(foreground);
		g.setFont(CARD_BACK_FONT);
		for (int i = 0; i < DRAW_BACK_STRING.length; i++) {
			int dx = x + DRAW_PADDING;
			int dy = y + DRAW_PADDING + (BACK_LINE_SPACING * i) + (int) ((double) BACK_LINE_SPACING * 0.9d);
			g.drawString(DRAW_BACK_STRING[i], dx, dy);
		}
	}
}
