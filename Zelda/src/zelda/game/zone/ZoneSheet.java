package zelda.game.zone;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.graphics.ImageSheet;
import zelda.game.world.Frame;


public class ZoneSheet {
	private int index;
	private String name;
	private ImageSheet[] sheets;
	private String[] zoneNames;


	// ================== CONSTRUCTORS ================== //

	public ZoneSheet() {
		this.name = "";
		this.sheets = null;
		this.zoneNames = new String[0];
	}

	public ZoneSheet(String name, String... zoneNames) {
		this.name = name;
		this.zoneNames = zoneNames;
		this.sheets = null;
		for (int i = 0; i < zoneNames.length; i++)
			Resources.zones.addZone(zoneNames[i]);
		Resources.zones.addZoneSheet(this);
	}



	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public ImageSheet getImageSheet(Zone zone) {
		return sheets[zone.getIndex()];
	}

	public ImageSheet getImageSheet(Frame frame) {
		return getImageSheet(frame.getZone());
	}



	// ==================== MUTATORS ==================== //

	public void initialize() {
		ArrayList<Zone> zones = Resources.zones.getZones();

		this.sheets = new ImageSheet[zones.size()];
		int nameIndex = 0;
		ImageSheet defaultSheet = null;

		for (int i = 0; i < zones.size(); i++) {
			sheets[i] = null;
			if (nameIndex < zoneNames.length) {
				if (zones.get(i).getName().equals(zoneNames[nameIndex])) {
					sheets[i] = new ImageSheet(name + zoneNames[nameIndex]
							+ ".png", 16, 16, 1);
					if (name.equals("zoneset"))
						sheets[i].createSubSheet(11, 0, 8, 8);
					nameIndex++;
					if (defaultSheet == null)
						defaultSheet = sheets[i];
				}
			}
		}

		for (int i = 0; i < zones.size(); i++) {
			if (sheets[i] == null)
				sheets[i] = defaultSheet;
		}
	}

	public void initializeGlobal(String name) {
		ArrayList<Zone> zones = Resources.zones.getZones();
		this.name = name;
		this.zoneNames = new String[zones.size()];

		for (int i = 0; i < zones.size(); i++)
			zoneNames[i] = zones.get(i).getName();

		initialize();
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
