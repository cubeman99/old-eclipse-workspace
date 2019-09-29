package console;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Locale;
import main.ImageLoader;


public class Console {
	public static final int DEFAULT_WIDTH  = 80;
	public static final int DEFAULT_HEIGHT = 25;
	public static final C16 DEFAULT_COLOR_BACKGROUND = C16.BLACK;
	public static final C16 DEFAULT_COLOR_FOREGROUND = C16.SILVER;
	public static final int CHAR_WIDTH  = 8;
	public static final int CHAR_HEIGHT = 12;
	public static Font FONT       = new Font("Terminal	", Font.PLAIN, 5);
	
	private Image image;
	private Image fontImage;
	private Graphics g;
	private int width;
	private int height;
	private C16 colorFore;
	private C16 colorBack;
	private boolean replaceFore;
	private boolean replaceBack;
	private Point cursorPoint;
	private Char[][] characters;
	
	
	public class Char {
		public char character;
		public C16 colorFore;
		public C16 colorBack;
		
		public Char(char character, C16 colorFore, C16 colorBack) {
			this.character = character;
			this.colorFore = colorFore;
			this.colorBack = colorBack;
		}
		
		public boolean equals(Char c) {
			return (character == c.character && colorFore == c.colorFore && colorBack == c.colorBack);
		}
	}
	
	public Console(int width, int height, C16 colorFore, C16 colorBack) {
		
		
		this.width       = width;
		this.height      = height;
		this.colorFore   = colorFore;
		this.colorBack   = colorBack;
		this.replaceBack = true;
		this.replaceFore = true;
		this.cursorPoint = new Point(0, 0);
		this.characters  = new Char[width][height];
		this.image       = new BufferedImage(width * CHAR_WIDTH, height * CHAR_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.g           = image.getGraphics();
		this.fontImage   = ImageLoader.getImage("fontConsole");
		g.setColor(Color.RED);
		g.setFont(FONT);
		g.fillRect(0, 0, width * CHAR_WIDTH, height * CHAR_HEIGHT);
		clear();
	}
	
	public Console(int width, int height) {
		this(width, height, DEFAULT_COLOR_FOREGROUND, DEFAULT_COLOR_BACKGROUND);
	}
	
	public Console() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public void clear() {
		int i = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				characters[x][y] = new Char((char) 32, colorFore, colorBack);
				//characters[x][y] = new Char((char) (i % 256), colorFore, colorBack);
				i++;
			}
		}
		redraw();
	}
	
	private void redraw() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				drawChar(x, y);
			}
		}
	}
	
	private void setChar(int x, int y, char c) {
		characters[x][y].character = c;
	}
	
	private void drawChar(int x, int y) {
		char c = characters[x][y].character;
		int dx = x * CHAR_WIDTH;
		int dy = y * CHAR_HEIGHT;
		int sx = (c % 16) * CHAR_WIDTH;
		int sy = ((int) (c / 16.0)) * CHAR_HEIGHT;
				
		g.setColor(colorBack.getColor());
		g.fillRect(dx, dy, CHAR_WIDTH, CHAR_HEIGHT);
		g.setColor(colorFore.getColor());
		g.drawImage(fontImage, dx, dy, dx + CHAR_WIDTH, dy + CHAR_HEIGHT,
				sx, sy, sx + CHAR_WIDTH, sy + CHAR_HEIGHT, null);
	}
	
	
	public void setForeground(C16 col) {
		colorFore = col;
	}
	
	public void setBackground(C16 col) {
		colorBack = col;
	}
	
	public void setCursorPos(int x, int y) {
		cursorPoint.setLocation(x % width, y % height);
	}
	
	public void setCursorPos(Point p) {
		setCursorPos(p.x, p.y);
	}
	
	public Point getCursorPos() {
		return cursorPoint;
	}
	
	public Char getChar(int x, int y) {
		return characters[x][y];
	}
	
	public Char getChar(Point p) {
		return characters[p.x][p.y];
	}
	
	public Image getImage() {
		return image;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Point getSize() {
		return new Point(width, height);
	}
	
	////////////////////////////////////////////////////
	// PRINT:

	public void print(boolean value) {
		print("" + value);
	}
	
	public void print(char x) {
		if (!getChar(getCursorPos()).equals(new Char(x, colorFore, colorBack))) {
			setChar(cursorPoint.x, cursorPoint.y, x);
			drawChar(cursorPoint.x, cursorPoint.y);
		}
		setCursorPos(getCursorPos().x + 1, getCursorPos().y);
		if (getCursorPos().x == 0)
			println();
	}
	
	public void print(char[] x) {
		for (int i = 0; i < x.length; i++)
			print(x[i]);
	}
	
	public void print(double x) {
		print("" + x);
	}
	
	public void print(float x) {
		print("" + x);
	}
	
	public void print(int x) {
		print("" + x);
	}
	
	public void print(long x) {
		print("" + x);
	}
	
	public void print(Object x) {
		print("" + x);
	}
	
	public void print(String x) {
		for (int i = 0; i < x.length(); i++)
			print(x.charAt(i));
	}
	
	public void printf(String format, Object... args) {
		print(String.format(format, args));
	}
	
	public void printf(Locale l, String format, Object... args) {
		print(String.format(l, format, args));
	}
	
	// PRINT LINE:
	
	public void println() {
		setCursorPos(0, cursorPoint.y + 1);
	}
	public void println(boolean x) {
		print(x); println();
	}
	
	public void println(char x) {
		print(x); println();
	}
	
	public void println(char[] x) {
		print(x); println();
	}
	
	public void println(double x) {
		print(x); println();
	}
	
	public void println(float x) {
		print(x); println();
	}
	
	public void println(int x) {
		print(x); println();
	}
	
	public void println(long x) {
		print(x); println();
	}

	public void println(Object x) {
		print(x); println();
	}

	public void println(String x) {
		print(x); println();
	}
	////////////////////////////////////////////////////
	
	
	public void setReplaceMode(boolean replaceFore, boolean replaceBack) {
		this.replaceFore = replaceFore;
		this.replaceBack = replaceBack;
	}
}
