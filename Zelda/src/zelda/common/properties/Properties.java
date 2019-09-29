package zelda.common.properties;

import java.io.Serializable;
import java.util.ArrayList;
import zelda.game.control.script.Script;
import zelda.game.world.Frame;


/**
 * Properties.
 * 
 * @author David Jordan
 */
public class Properties implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Property> properties;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Properties() {
		properties = new ArrayList<Property>();
	}

	public Properties(Properties copy) {
		properties = new ArrayList<Property>();
		for (int i = 0; i < copy.properties.size(); i++)
			properties.add(new Property(copy.properties.get(i)));
	}



	// =================== ACCESSORS =================== //
	
	public Property getProperty(int index) {
		return properties.get(index);
	}
	
	public int getNumProperties() {
		return properties.size();
	}
	
	public Property getProperty(String propertyName) {
		for (int i = 0; i < properties.size(); i++) {
			if (properties.get(i).getName().equals(propertyName))
				return properties.get(i);
		}
		return null;
	}
	
	public boolean exists(String propertyName) {
		for (int i = 0; i < properties.size(); i++) {
			if (properties.get(i).getName().equals(propertyName))
				return true;
		}
		return false;
	}

	public String get(String propertyName) {
		return get(propertyName, "");
	}
	
	public String get(String propertyName, String defaultValue) {
		Property p = getProperty(propertyName);
		if (p != null)
			return p.getString();
		return defaultValue;
	}

	public boolean getBoolean(String propertyName) {
		return getBoolean(propertyName, false);
	}

	public boolean getBoolean(String propertyName, boolean defaultValue) {
		Property p = getProperty(propertyName);
		if (p != null && !p.isNull())
			return p.getBoolean();
		return defaultValue;
	}
	

	public int getInt(String propertyName) {
		return getInt(propertyName, 0);
	}

	public int getInt(String propertyName, int defaultValue) {
		Property p = getProperty(propertyName);
		if (p != null && !p.isNull())
			return p.getInt();
		return defaultValue;
	}

	public double getDouble(String propertyName) {
		return getDouble(propertyName, 0);
	}
	
	public double getDouble(String propertyName, double defaultValue) {
		Property p = getProperty(propertyName);
		if (p != null && !p.isNull())
			return p.getDouble();
		return defaultValue;
	}
	
	public boolean isNull(String propertyName) {
		Property p = getProperty(propertyName);
		if (p != null)
			return p.isNull();
		return true;
	}

	public boolean nullOrEquals(String propertyName, Object testValue) {
		return (isNull(propertyName) || get(propertyName, "").equals(
				testValue.toString()));
	}

	public boolean notNullAndEquals(String propertyName, Object testValue) {
		return (!isNull(propertyName) && get(propertyName, "").equals(
				testValue.toString()));
	}


	public boolean existEquals(String propertyName, Object testValue) {
		return (exists(propertyName) && get(propertyName, "").equals(
				testValue.toString()));
	}

	public boolean existNotEquals(String propertyName, Object testValue) {
		return (exists(propertyName) && !get(propertyName, "").equals(
				testValue.toString()));
	}

	public ArrayList<Property> getPropertiesList() {
		return properties;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void set(Properties other) {
		for (int i = 0; i < other.properties.size(); i++)
			set(other.properties.get(i));
	}

	public void set(Property p) {
		set(p.getName(), p.getString());
	}
	
	public void set(String name, Object value) {
		Property p = getProperty(name);
		if (p == null)
			properties.add(new Property(name, value));
		else
			p.set(value);
	}

	public void define(String name, Object value) {
		if (!exists(name))
			properties.add(new Property(name, value));
	}
	
	public String script(String name, PropertyHolder holder, Frame frame) {
		if (exists(name))
			return Script.execute(name, get(name, ""), holder, frame);
		return "";
	}
}
