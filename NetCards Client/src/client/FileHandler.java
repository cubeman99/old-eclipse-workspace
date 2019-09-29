package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FileHandler {
	public static final String PROPERTIES_FILE_NAME = "properties.properties";
	
	private static Properties propertiesFile = new Properties();
	
	public static void loadProperties() {
		try {
			propertiesFile.load(new FileInputStream(PROPERTIES_FILE_NAME));
		}
		catch (IOException e) {
			System.out.println("Error loading properties file: " + e);
		}
	}
	
	public static void saveProperties() {
		try {
			propertiesFile.store(new FileOutputStream(PROPERTIES_FILE_NAME), null);
		}
		catch (FileNotFoundException e) {
			System.out.println("Properties file not found: " + e);
		}
		catch (IOException e) {
			System.out.println("Error saving properties file: " + e);
		}
	}

	public static String getProperty(String key) {
		return propertiesFile.getProperty(key, "");
	}
	
	public static void setProperty(String key, String value) {
		propertiesFile.setProperty(key, value);
	}
}
