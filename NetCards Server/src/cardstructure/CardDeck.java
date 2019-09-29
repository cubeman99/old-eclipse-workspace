package cardstructure;


public class CardDeck {
	private CardList cards;
	
	public CardDeck() {
		this.cards = new CardList();
	}
	
	public CardDeck(CardDeck template) {
		this.cards = template.getCards().getCopy();
	}
	
	public void shuffle() {
		cards.shuffle();
	}
	
	public Card drawCard() {
		if (size() > 0) {
			Card topCard = getTopCard();
			removeTopCard();
			return topCard;
		}
		return null;
	}
	
	public CardList getCards() {
		return cards;
	}
	
	public int size() {
		return cards.size();
	}

	private void removeCard(int index) {
		cards.remove(index);
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	public void printCards() {
		cards.print();
	}
	
	private Card getTopCard() {
		if (size() > 0)
			return getCard(size() - 1);
		return null;
	}
	
	private void removeTopCard() {
		if (size() > 0)
			removeCard(size() - 1);
	}
	
	private Card getCard(int index) {
		if (cards.size() > index)
			return cards.get(index);
		return null;
	}
}
