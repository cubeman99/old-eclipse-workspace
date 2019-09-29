package tp.planner.tile;

import java.awt.Graphics;
import java.awt.Image;
import tp.common.GMath;
import tp.common.Point;
import tp.common.graphics.Sheet;

public class PacketMannequin extends TilePacket {
	private static final Point ARMOR_OFFSET = new Point(4, 12);
	private int[] armorIndeces;
	private Sheet[] armorSheets;
	
	public PacketMannequin() {
		super();
		armorSheets  = new Sheet[] {PacketData.sheetArmorLegs, PacketData.sheetArmorBody, PacketData.sheetArmorHead};
		armorIndeces = new int[3];

		armorIndeces[0] = GMath.random.nextInt(22);
		armorIndeces[1] = GMath.random.nextInt(23);
		armorIndeces[2] = GMath.random.nextInt(42);
	}
	
	public PacketMannequin(PacketMannequin copy) {
		super();
		armorSheets  = new Sheet[] {PacketData.sheetArmorLegs, PacketData.sheetArmorBody, PacketData.sheetArmorHead};
		armorIndeces = new int[3];
		for (int i = 0; i < 3; i++)
			armorIndeces[i] = copy.armorIndeces[i];
	}
	
	private void drawArmorPart(Graphics g, int index, int partX, int partY) {
		int sPartX = (tile.getRandomSubimage() == 0 ? 1 - partX : partX);
		Point pos  = tile.getPosition();
		Image img  = armorSheets[index].getImage();
		Point sp   = new Point(Math.max(0, (sPartX * 16) + ARMOR_OFFSET.x), (partY * 16) + ARMOR_OFFSET.y);
		int spX2   = Math.min(40, (sPartX * 16) + ARMOR_OFFSET.x + 16);
		sp.add(armorIndeces[index] * 40, 0);
		spX2 += armorIndeces[index] * 40;
		Point off  = new Point(partX * 16, partY * 16);
		Point dp = new Point((pos.x * 16) + off.x, (pos.y * 16) + off.y);
		
		int dx1 = dp.x;
		int dx2 = dp.x + 16;
		if (tile.getRandomSubimage() == 0) {
			dx1 += 16;
			dx2 -= 16;
		}
		
		g.drawImage(img, dx1, dp.y, dx2, dp.y + 16, sp.x, sp.y, spX2, sp.y + 16, null);
	}
	
	@Override
	public void draw(Graphics g, int partX, int partY) {
		for (int i = 0; i < 3; i++)
			drawArmorPart(g, i, partX, partY);
	}
	
	@Override
	public TilePacket getCopy(Tile t) {
		PacketMannequin p = new PacketMannequin(this);
		p.tile = t;
		return p;
	}
}
