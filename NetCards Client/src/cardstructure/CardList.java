package cardstructure;
import java.util.ArrayList;
import java.util.Random;


public class CardList {
	private static Random random = new Random();
	private ArrayList<Card> cards;
	
	public CardList() {
		cards = new ArrayList<Card>();
	}
	
	public CardList(CardList template) {
		this();
		for (int i = 0; i < template.size(); i++) {
			cards.add(template.get(i));
		}
	}
	
	public void shuffle() {
		ArrayList<Card> shuffledCards = new ArrayList<Card>();
		
		while (size() > 0) {
			int index = random.nextInt(size());
			shuffledCards.add(get(index));
		}
		cards = shuffledCards;
	}

	public void add(Card card) {
		cards.add(card);
	}

	public void add(String str) {
		cards.add(new Card(str));
	}
	
	public Card get(int index) {
		return cards.get(index);
	}
	
	public void remove(int index) {
		cards.remove(index);
	}
	
	public void remove(Card card) {
		cards.remove(card);
	}
	
	
	public boolean contains(Card card) {
		return cards.contains(card);
	}
	
	public CardList getCopy() {
		return new CardList(this);
	}
	
	public int size() {
		return cards.size();
	}
	
	public void clear() {
		cards.clear();
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public void print() {
		System.out.println();
		for (int i = 0; i < size(); i++) {
			System.out.println(get(i));
		}
	}
}
