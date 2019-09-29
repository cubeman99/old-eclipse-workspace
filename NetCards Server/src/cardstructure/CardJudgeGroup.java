package cardstructure;

public class CardJudgeGroup {
	private CardList cards;
	private boolean flipped;
	private String owner;
	
	public CardJudgeGroup(String owner) {
		this.cards   = new CardList();
		this.flipped = false;
		this.owner   = owner;
	}
	
	public void flip() {
		flipped = true;
	}
	
	public boolean getFlipped() {
		return flipped;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public boolean isOwnedBy(String owner) {
		return (this.owner.equals(owner));
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	public Card getCard(int index) {
		return cards.get(index);
	}
	
	public int size() {
		return cards.size();
	}
}
