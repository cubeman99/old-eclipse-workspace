package zelda.game.zone;

public class ZoneSheetType {
	private int index;
	private String prefix;
	private int seperation;

	public ZoneSheetType(int index, String prefix, int seperation) {
		this.index = index;
		this.prefix = prefix;
		this.seperation = seperation;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getIndex() {
		return index;
	}

	public int getSeperation() {
		return seperation;
	}
}