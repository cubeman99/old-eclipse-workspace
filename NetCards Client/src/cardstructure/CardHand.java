package cardstructure;

import client.Message;
import cardgame.CardGame;
import cardgame.GameRules;

public class CardHand {
	private CardList cards;
	
	public CardHand() {
		this.cards = new CardList();
	}
	
	public void requestNeededCards() {
		int neededCards = GameRules.HAND_SIZE - size();
		if (neededCards > 0) {
			// Send a request to the server to draw more cards
			CardGame.sendMessage(new Message("requestcards", neededCards));
		}
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	public Card getCard(int index) {
		return cards.get(index);
	}
	
	public CardList getCards() {
		return cards;
	}
	
	public void clear() {
		cards.clear();
	}
	
	public int size() {
		return cards.size();
	}
}
