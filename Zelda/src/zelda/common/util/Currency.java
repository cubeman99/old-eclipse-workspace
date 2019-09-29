package zelda.common.util;

/**
 * Used to store info about an amount of something and can be added to or taken
 * from that also has a max-amount and a name. <br>
 * <br>
 * 
 * Some examples of currencies include Rupees, Keys, Bombs, Seeds, Hearts,
 * Heart-pieces, etc.
 * 
 * @author David Jordan
 */
public class Currency {
	private String name;
	private int amountMax;
	private int amount;



	// ================== CONSTRUCTORS ================== //

	public Currency(String name, int amountMax) {
		this.name = name;
		this.amount = amountMax;
		this.amountMax = amountMax;
	}

	public Currency(String name, int amount, int amountMax) {
		this.name = name;
		this.amount = amount;
		this.amountMax = amountMax;
	}

	public Currency(Currency copy) {
		this.name = copy.name;
		this.amount = copy.amount;
		this.amountMax = copy.amountMax;
	}



	// =================== ACCESSORS =================== //

	public int get() {
		return amount;
	}

	public int getMax() {
		return amountMax;
	}

	public String getName() {
		return name;
	}

	public double getPercentage() {
		return (amount / (double) amountMax);
	}

	public String getFormattedString() {
		return getFormattedString(amount);
	}

	public String getFormattedString(int amount) {
		int amountPlaces = 0;
		int places = 0;
		if (amountMax > 0)
			places = (int) Math.log10(amountMax);
		if (amount > 0)
			amountPlaces = (int) Math.log10(amount);
		String str = "" + amount;
		for (int i = 0; i < places - amountPlaces; i++)
			str = "0" + str;
		return str;
	}

	public boolean has(int amount) {
		return (this.amount >= amount);
	}

	public boolean full() {
		return (amount == amountMax);
	}

	public boolean empty() {
		return (amount == 0);
	}



	// ==================== MUTATORS ==================== //

	public void set(int amount, int amountMax) {
		this.amountMax = amountMax;
		this.amount = Math.max(0, Math.min(amountMax, amount));
	}

	public void set(int amount) {
		this.amount = Math.max(0, Math.min(amountMax, amount));
	}

	public void setMax(int amountMax) {
		this.amountMax = amountMax;
		if (amount > amountMax)
			amount = amountMax;
	}

	public void give(int amount) {
		this.amount += amount;
		if (this.amount > amountMax)
			this.amount = amountMax;
		if (this.amount < 0)
			this.amount = 0;
	}

	public void take(int amount) {
		this.amount -= amount;
		if (this.amount > amountMax)
			this.amount = amountMax;
		if (this.amount < 0)
			this.amount = 0;
	}

	public void fill(int amountMax) {
		this.amount = amountMax;
		this.amountMax = amountMax;
	}

	public void fill() {
		amount = amountMax;
	}

	public void deplete() {
		amount = 0;
	}
}
