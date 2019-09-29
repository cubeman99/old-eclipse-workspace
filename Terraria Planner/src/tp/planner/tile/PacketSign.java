package tp.planner.tile;

public class PacketSign extends TilePacket {
	private String text;
	
	public PacketSign() {
		super();
		text = "";
	}
	
	public PacketSign(PacketSign copy) {
		super();
		this.text = copy.getText();
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public TilePacket getCopy(Tile t) {
		this.tile = t;
		return new PacketSign(this);
	}
}
