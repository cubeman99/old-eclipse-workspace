package game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class CardStack {
	public ArrayList<Card> cards = new ArrayList<Card>();
	
	public CardStack() {
		
	}
	
	public int size() {
		return cards.size();
	}
	
	public void clear() {
		cards.clear();
	}
	
	public Card get(int index) {
		return cards.get(index);
	}
	
	public void add(Card card) {
		cards.add(card);
	}
	
	public void add(CardStack stack) {
		for (int i = 0; i < stack.size(); i++) {
			cards.add(stack.get(i));
		}
	}
	
	public void set(CardStack stack) {
		clear();
		add(stack);
	}
	
	public CardStack removeSubStack(int startIndex) {
		CardStack subStack = new CardStack();
		for (int i = startIndex; i < size(); i++) {
			cards.remove(i);
			i -= 1;
		}
		
		return subStack;
	}
	
	public void checkCompletedStacks() {
		int number = -1;
		for (int i = 0; i < size(); i++) {
			int n = get(i).number;
			if (n == 13) {
				number = 13;
			}
			if (number > 0 && n == number - 1) {
				number = n;
				if (number == 1) {
					removeSubStack(i - 12);
				}
			}
		}
	}
	
	public boolean stackIsAppendable(CardStack stack) {
		if (size() == 0)
			return true;
		else if (size() > 0) {
			if (get(size() - 1).number == stack.get(0).number + 1)
				return true;
		}
		return false;
	}
	
	public static boolean checkValidStack(CardStack stack) {
		int number = -1;
		boolean valid = true;
		for (int i = 0; i < stack.size(); i++) {
			if (i == 0 || stack.get(i).number == number - 1) {
				number = stack.get(i).number;
			}
			else {
				valid = false;
				break;
			}
		}
		return valid;
	}
	
	public CardStack getSubStack(int startIndex) {
		CardStack subStack = new CardStack();
		
		for (int i = startIndex; i < size(); i++) {
			subStack.add(cards.get(i));
		}
		
		return subStack;
	}
	
	public void updateGameStack(int x, int y) {
		if (Game.cardGame.draggingGrabStack) {
			Point stackPos = new Point(Mouse.x() - Game.cardGame.grabCardPos.x, Mouse.y() - Game.cardGame.grabCardPos.y);
			int midX = stackPos.x + (Card.DRAW_WIDTH / 2);
			
			if (midX >= x - (CardGame.DRAW_COLUMN_SPACE / 2) && midX < x + Card.DRAW_WIDTH + (CardGame.DRAW_COLUMN_SPACE / 2)) {
				if (!Mouse.left()) {
					// Release drag stack onto this stack if valid
					CardStack grabStack = Game.cardGame.grabStack;
					Game.cardGame.draggingGrabStack = false;
					if (stackIsAppendable(grabStack)) {
						add(grabStack);
						grabStack.clear();
						checkCompletedStacks();
					}
					else {
						Game.cardGame.cardStacks[Game.cardGame.grabStackDefaultIndex].add(grabStack);
						grabStack.clear();
					}
				}
			}
			
			return;
		}
		for (int i = size() - 1; i >= 0; i--) {
			Rectangle cardRect = new Rectangle(x, y + (i * CardGame.DRAW_ROW_SPACE), Card.DRAW_WIDTH, Card.DRAW_HEIGHT);
			if (Mouse.inArea(cardRect)) {
				if (Mouse.leftPressed() && !Game.cardGame.draggingGrabStack) {
					CardStack subStack = getSubStack(i);
					if (checkValidStack(subStack)) {
						// Grab a some cards out of this stack
						Game.cardGame.draggingGrabStack = true;
						CardStack grabStack = Game.cardGame.grabStack;
						Game.cardGame.grabCardPos.setLocation(Mouse.x() - cardRect.getX(), Mouse.y() - cardRect.getY());
						grabStack.set(subStack);
						removeSubStack(i);
						break;
					}
				}
			}
		}
	}
	
	public void draw(Graphics g, int x, int y) {
		for (int i = 0; i < size(); i++) {
			cards.get(i).draw(g, x, y + (i * CardGame.DRAW_ROW_SPACE));
		}
	}
}
