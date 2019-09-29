package cardstructure;

import cardgame.GameRules;

public class CardHand {
	private CardList cards;

	
	public CardHand() {
		cards = new CardList();
	}
	
	public void drawCards(CardDeck deck) {
		for (int i = 0; i < GameRules.HAND_SIZE; i++) {
			drawCard(deck);
		}
	}
	
	private void drawCard(CardDeck deck) {
		cards.add(deck.drawCard());
	}
}
