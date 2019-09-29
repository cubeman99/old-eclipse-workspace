package zelda.game.entity.collectable;

import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;


public class RewardCurrency extends CollectableReward {
	protected Currency currency;
	protected int amount;


	public RewardCurrency(String name, Sprite sprite, int amount,
			Currency currency, String messageText) {
		this(name, sprite, amount, currency, messageText, TYPE_RAISE);
	}

	public RewardCurrency(String name, Sprite sprite, int amount,
			Currency currency, String messageText, int raiseType) {
		super(name, sprite, messageText, raiseType);
		this.currency = currency;
		this.amount   = amount;
	}
	
	public RewardCurrency(RewardCurrency copy) {
		super(copy);
		this.currency = copy.currency;
		this.amount   = copy.amount;
	}

	@Override
	public void collect() {
		if (currency != null)
			currency.give(amount);
	}
	
	@Override
	public RewardCurrency clone() {
		return new RewardCurrency(this);
	}
}
