package zelda.game.zone;

import zelda.common.graphics.ImageSheet;
import zelda.editor.tileSheet.TilesetTemplate;


public class Zone {
	private int index;
	private String name;
	private ImageSheet areaSheet;
	private TilesetTemplate defaultTemplate;



	// ================== CONSTRUCTORS ================== //

	public Zone(String name) {
		this.name            = name;
		this.index           = 0;
		this.defaultTemplate = null;
		this.areaSheet = new ImageSheet("zoneset" + name + ".png", 16, 16, 1);
		this.areaSheet.createSubSheet(11, 0, 8, 8);
	}



	// =================== ACCESSORS =================== //

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public ImageSheet getAreaSheet() {
		return areaSheet;
	}

	public TilesetTemplate getDefaultTemplate() {
		return defaultTemplate;
	}



	// ==================== MUTATORS ==================== //

	public void setIndex(int index) {
		this.index = index;
	}

	public void setDefaultTemplate(TilesetTemplate defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}
}
