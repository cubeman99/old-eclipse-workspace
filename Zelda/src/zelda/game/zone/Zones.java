package zelda.game.zone;

import java.util.ArrayList;


public class Zones {
	private ArrayList<Zone> zones;
	private ArrayList<ZoneSheet> zoneSheets;
	public ZoneSheet areaSheet;



	// ================== CONSTRUCTORS ================== //

	public Zones() {
		zoneSheets = new ArrayList<ZoneSheet>();
		zones = new ArrayList<Zone>();
		areaSheet = new ZoneSheet();
		addZoneSheet(areaSheet);
	}



	// =================== ACCESSORS =================== //

	public Zone getZone(int index) {
		return zones.get(index);
	}

	public ZoneSheet getZoneSheet(int index) {
		return zoneSheets.get(index);
	}

	public Zone getZone(String zoneName) {
		for (int i = 0; i < zones.size(); i++) {
			if (zones.get(i).getName().equals(zoneName))
				return zones.get(i);
		}
		return null;
	}

	public int getNumZones() {
		return zones.size();
	}

	public int getNumZoneSheets() {
		return zoneSheets.size();
	}

	public ArrayList<Zone> getZones() {
		return zones;
	}

	public ArrayList<ZoneSheet> getZoneSheets() {
		return zoneSheets;
	}

	private boolean zoneExists(String name) {
		for (int i = 0; i < zones.size(); i++) {
			if (zones.get(i).getName().equals(name))
				return true;
		}
		return false;
	}



	// ==================== MUTATORS ==================== //

	public void initialize() {
		areaSheet.initializeGlobal("zoneset");

		for (int i = 1; i < zoneSheets.size(); i++)
			zoneSheets.get(i).initialize();
	}

	public void addZone(String name) {
		if (!zoneExists(name)) {
			addZone(new Zone(name));
		}
	}

	public ZoneSheet addZoneSheet(ZoneSheet sheet) {
		sheet.setIndex(zoneSheets.size());
		zoneSheets.add(sheet);
		return sheet;
	}

	private Zone addZone(Zone zone) {
		zone.setIndex(zones.size());
		zones.add(zone);
		return zone;
	}
}
