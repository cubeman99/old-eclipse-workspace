package zelda.game.entity.collectable;

import zelda.common.Resources;
import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;


public class RewardHeartPiece extends CollectableReward {
	protected Currency currency;
	protected int amount;
	
	public RewardHeartPiece() {
		super("heart_piece", new Sprite(Resources.SHEET_ICONS_LARGE, 4, 4),
				"You got a <red>Piece of Heart<red>!<n>Collect four in all"
				+ " to get an extra <red>Heart Container<red>! Check them"
				+ " on the Item Screen.", TYPE_TWO_HAND);
	}

	@Override
	public void collect() {
		Currency currency = game.getPlayer().currencyHeartPieces;
		currency.give(1);

		// Four Pieces of Hearts!
		if (currency.full()) {
			currency.deplete();
			game.getPlayer().currencyHearts.fill(game.getPlayer()
					.currencyHearts.getMax() + 4);
		}
	}
	
	@Override
	public RewardHeartPiece clone() {
		return new RewardHeartPiece();
	}
}
