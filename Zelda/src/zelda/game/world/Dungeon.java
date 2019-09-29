package zelda.game.world;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;


/**
 * Dungeon.
 * 
 * @author David Jordan
 */
public class Dungeon implements PropertyHolder {
	private Properties properties;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Dungeon() {
		properties = new Properties();
	}
	
	public Dungeon(String name) {
		this();
		
		properties.define("name",     name);
		properties.define("keys",     0);
		properties.define("boss_key", false);
		properties.define("compas",   false);
		properties.define("map",      false);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public String getName() {
		return properties.get("name", "NULL");
	}
	
	public int getNumSmallKeys() {
		return properties.getInt("keys", 0);
	}
	
	public boolean hasSmallKey() {
		return (getNumSmallKeys() > 0);
	}
	
	public boolean hasCompass() {
		return hasItem("compass");
	}
	
	public boolean hasMap() {
		return hasItem("map");
	}
	
	public boolean hasBossKey() {
		return hasItem("boss_key");
	}
	
	public boolean hasItem(String itemName) {
		return properties.getBoolean(itemName, false);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void useSmallKey() {
		if (hasSmallKey()) {
    		int keys = properties.getInt("keys", 0) - 1;
    		properties.set("keys", keys);
		}
	}
	
	public void addSmallKey() {
		int keys = properties.getInt("keys", 0) + 1;
		keys = Math.min(9, keys);
		properties.set("keys", keys);
	}
	
	public void acquireItem(String itemName) {
		if (itemName.equals("key"))
			addSmallKey();
		else
			properties.set(itemName, true);
	}
	
	public void load(ObjectInputStream in) throws IOException, ClassNotFoundException {
		properties = (Properties) in.readObject();
	}
	
	public void save(ObjectOutputStream out) throws IOException {
		out.writeObject(properties);
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public void onChangeProperty(Property p) {
		// TODO Auto-generated method stub
	}
}
