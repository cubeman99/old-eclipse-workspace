package game;


import java.awt.Color;
import java.awt.Graphics;

public class Card {
	public static final int DRAW_WIDTH  = 48;
	public static final int DRAW_HEIGHT = 64;
	public int number;
	public int suit;

	
	public Card(int suit, int number) {
		this.suit   = suit;
		this.number = number;
	}
	/*
	public enum Suit {
		CLUBS(Color.BLACK), SPADES(Color.BLACK), DIAMONDS(Color.RED), HEARTS(Color.RED);
		
		private Suit(Color color) {
			this.color = color;
		}
		
		public Color getColor() {
			return color;
		}
		
		public enum Color {RED, BLACK}
		
		private Color color;
	}*/
	
	
	public String getNumberChar() {
		if (number == 1)
			return "A";
		else if (number == 11)
			return "J";
		else if (number == 12)
			return "Q";
		else if (number == 13)
			return "K";
		return ("" + number);
	}
	
	public String getSuitChar() {
		if (suit == 0)
			return "C";
		else if (suit == 1)
			return "S";
		else if (suit == 2)
			return "D";
		else if (suit == 3)
			return "H";
		return "?";
	}
	
	public Color getSuitColor() {
		if (suit < 2)
			return Color.BLACK;
		return Color.RED;
	}
	
	public void draw(Graphics g, int x, int y) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, DRAW_WIDTH, DRAW_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, DRAW_WIDTH, DRAW_HEIGHT);
		
		g.setColor(getSuitColor());
		g.drawString(getNumberChar(), x + 2, y + 12);
		g.drawString(getSuitChar(), x + DRAW_WIDTH - 10, y + 12);
	}
}
