package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;

public class CardGame {
	public static final int CARD_NUMBERS = 13;
	public static final int CARD_SUITS   = 4;
	public static final int CARD_STACKS  = 8;
	public static final int DRAW_STARTX       = 64;
	public static final int DRAW_STARTY       = 64;
	public static final int DRAW_COLUMN_SPACE = 16;
	public static final int DRAW_ROW_SPACE    = 16;

	public CardStack[] cardStacks = new CardStack[CARD_STACKS];
	public CardStack grabStack;
	public int grabStackDefaultIndex = 0;
	public boolean draggingGrabStack = false;
	public Point grabCardPos = new Point();
	
	public Random random = new Random();
	
	
	public CardGame() {
		grabStack = new CardStack();
		
		for (int i = 0; i < CARD_STACKS; i++) {
			cardStacks[i] = new CardStack();
		}
		
		setupGame();
	}
	
	public void clearGame() {
		for (int i = 0; i < CARD_STACKS; i++) {
			cardStacks[i].clear();
		}
	}
	
	public void setupGame() {
		clearGame();
		ArrayList<Card> cardPile = new ArrayList<Card>();
		
		// Fill the card pile with a possible cards
		for (int i = 0; i < CARD_SUITS; i++) {
			for (int j = 1; j <= CARD_NUMBERS; j++) {
				cardPile.add(new Card(0, j));
			}
		}
		
		// pick off cards randomly from the pile to put in the stacks
		int stackIndex = 0;
		while (cardPile.size() > 0) {
			int index = random.nextInt(cardPile.size());
			
			cardStacks[stackIndex].add(cardPile.get(index));
			cardPile.remove(index);
			
			stackIndex += 1;
			if (stackIndex >= CARD_STACKS)
				stackIndex = 0;
		}
		
	}
	
	public void update() {
		if (Keyboard.enter.pressed())
			setupGame();
	}
	
	public void draw(Graphics g) {
		
		// Draw the cards
		for (int i = 0; i < CARD_STACKS; i++) {
			boolean dragPrev = draggingGrabStack;
			cardStacks[i].updateGameStack(DRAW_STARTX + (i * (DRAW_COLUMN_SPACE + Card.DRAW_WIDTH)), DRAW_STARTY);
			if (draggingGrabStack && !dragPrev)
				grabStackDefaultIndex = i;
			cardStacks[i].draw(g, DRAW_STARTX + (i * (DRAW_COLUMN_SPACE + Card.DRAW_WIDTH)), DRAW_STARTY);
		}
		// Draw cards being dragged
		grabStack.draw(g, Mouse.x() - grabCardPos.x, Mouse.y() - grabCardPos.y);
		
	}
}
