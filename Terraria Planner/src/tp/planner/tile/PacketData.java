package tp.planner.tile;

import tp.common.graphics.Sheet;
import tp.planner.Item;
import tp.planner.ItemData;


public class PacketData {
	public static Sheet sheetArmorLegs;
	public static Sheet sheetArmorBody;
	public static Sheet sheetArmorHead;
	
	
	public static void initialize() {
		sheetArmorLegs = new Sheet("armorLegs", 30, 54, 0);
		sheetArmorBody = new Sheet("armorBody", 30, 54, 0);
		sheetArmorHead = new Sheet("armorHead", 30, 54, 0);
	}
	
	public static TilePacket createNewTilePacket(Tile tile) {
		Item item = tile.getItem();
		TilePacket packet = null;
		
		if (item == ItemData.SIGN)
			packet = new PacketSign();
		else if (item == ItemData.MANNEQUIN)
			packet = new PacketMannequin();
		
		if (packet != null) {
			packet.setTile(tile);
		}
		
		return packet;
	}
}
