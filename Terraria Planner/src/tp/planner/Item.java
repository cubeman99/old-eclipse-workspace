package tp.planner;

import java.awt.Image;
import tp.common.Point;
import tp.main.ImageLoader;


public class Item {
	public static final int TYPE_OBJECT = 0;
	public static final int TYPE_WALL   = 1;
	public static final int TYPE_LIQUID = 2;
	public static final int TYPE_WIRE   = 3;
	
	private String name;
	private Point size;
	private Point offset;
	private boolean flippable;
	private Image image;
	private ConnectionScheme connectionScheme;
	private int index;
	private int type;
	private int randSubimageCount;
	private boolean extendsUp;
	private boolean extendsDown;
	private boolean horizontalSubimageStrip;
	private Item wallObjectItem;
	private Point extendedOffset;
	private Point extendedSize;
	private Item parentItem;
	private boolean solid;
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create an item with from its own image. **/
	public Item(int index, String name, int width, int height, boolean flippable, ConnectionScheme scheme, int gridIndex) {
		this(index, name, name, width, height, 0, 0, flippable, scheme, gridIndex);
	}

	/** Create an item with another item's image. **/
	public Item(int index, String name, String sourceName, int width, int height, int offsetX, int offsetY, boolean flippable, ConnectionScheme scheme, int gridIndex) {
		this.index                   = index;
		this.name                    = name;
		this.size                    = new Point(width, height);
		this.offset                  = new Point(offsetX, offsetY);
		this.flippable               = flippable;
		this.image                   = ImageLoader.loadImage(sourceName + ".png");
		this.connectionScheme        = scheme;
		this.type                    = gridIndex;
		this.extendsUp               = false;
		this.extendsDown             = false;
		this.randSubimageCount       = 0;
		this.horizontalSubimageStrip = true;
		this.wallObjectItem          = null;
		this.extendedOffset          = new Point(0, 0);
		this.extendedSize            = new Point(size);
		this.parentItem              = null;
		this.solid                   = (scheme != null && scheme != ConnectionScheme.BLOCK);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public String getName() {
		return name;
	}
	
	public Point getSize() {
		return size;
	}
	
	public int getWidth() {
		return size.x;
	}

	public int getHeight() {
		return size.y;
	}
	
	public boolean isFlippable() {
		return flippable;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Point getOffset() {
		return offset;
	}
	
	public ConnectionScheme getConnectionScheme() {
		return connectionScheme;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Grid getGrid(Control control) {
		return control.world.getGrid(type);
	}
	
	public int getGridIndex() {
		return type;
	}
	
	public boolean isGridType(int type) {
		return (this.type == type);
	}
	
	public boolean extendsDown() {
		return extendsDown;
	}
	
	public boolean extendsUp() {
		return extendsUp;
	}
	
	public int getRandomSubimageCount() {
		return randSubimageCount;
	}
	
	public boolean isHorizontalSubimageStrip() {
		return horizontalSubimageStrip;
	}
	
	public Item getWallObjectItem() {
		return wallObjectItem;
	}
	
	public Point getExtendedOffset() {
		return extendedOffset;
	}
	
	public Point getExtendedSize() {
		return extendedSize;
	}
	
	public Item getParentItem() {
		return parentItem;
	}
	
	public Item getBaseItem() {
		return (parentItem == null ? this : parentItem);
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setToVerticalSubimageStrip() {
		horizontalSubimageStrip = false;
	}
	
	public void setExtension(int extendedX, int extendedY, int extendedWidth, int extendedHeight) {
		this.extendedOffset.set(extendedX, extendedY);
		this.extendedSize.set(extendedWidth, extendedHeight);
	}
	
	public void setToExtendsUp() {
		extendsUp = true;
		setExtension(0, -1, size.x, size.y + 1);
	}
	
	public void setToExtendsDown() {
		extendsDown = true;
		setExtension(0, 0, size.x, size.y + 1);
	}
	
	public void setRandomSubimageCount(int randSubimageCount) {
		this.randSubimageCount = randSubimageCount;
	}
	
	public void setWallObjectItem(Item wallObjectItem) {
		this.wallObjectItem = wallObjectItem;
		if (wallObjectItem != null)
			wallObjectItem.wallObjectItem = this;
	}
	
	public void setParentItem(Item parentItem) {
		this.parentItem = parentItem;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
}
