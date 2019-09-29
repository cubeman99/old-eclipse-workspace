package OLD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Control {
	private static final String PATH_SKIN    = "C:/Users/David/Documents/Rainmeter/Skins/cubeman/";
	private static final String PATH_DOCKBAR = PATH_SKIN + "top/";
	
	private ArrayList<String> lines;
	
	
	public Control() {
		lines = new ArrayList<String>();
	}
	
	public void refreshSkins() {
		try {
			Runtime.getRuntime().exec("cmd /c start " + PATH_SKIN + "refresh.bat");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void read() {
		try {
			Scanner reader = new Scanner(new File(PATH_DOCKBAR + "topNEW.ini"));
			
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				lines.add(line);
			}
			
			reader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(PATH_DOCKBAR + "topNEW.ini"));
			
			for (int i = 0; i < lines.size(); i++) {
				out.write(lines.get(i));
				out.newLine();
			}
			
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void print() {
		for (int i = 0; i < lines.size(); i++) {
			String section = getLineSection(lines.get(i));
			String key     = getLineKey(lines.get(i));
			if (section != null)
				System.out.println("\n[" + section + "]");
			if (key != null)
				System.out.println(key);
		}
	}
	
	public boolean setKey(String section, String key, String value) {
//		int secLineIndex = getSectionLine(section);
//		if (secLineIndex < 0) {
//			System.out.println("The section \'" + key + "\' does not exist.");
//			return false;
//		}
		
		int lineIndex = getKeyLine(section, key);
		if (lineIndex < 0) {
			System.out.println("The key \'" + key + "\' does not exist.");
			return false;
		}
		
		String line = key + "=" + value;
		lines.set(lineIndex, line);
		return true;
	}
	
	private int getKeyLine(String section, String key) {
		int sectionLineIndex = getSectionLine(section);
		if (sectionLineIndex < 0)
			return -1;
		
		for (int i = sectionLineIndex + 1; i < lines.size(); i++) {
			String line = lines.get(i);
			String testKey = getLineKey(line);
			
			if (testKey != null && testKey.equals(key))
				return i;
		}
		
		return -1;
	}
	
	private int getSectionLine(String section) {
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String testSection = getLineSection(line);
			
			if (testSection != null && testSection.equals(section))
				return i;
		}
		
		return -1;
	}
	
	private String getLineSection(String line) {
		if (line.length() < 2)
			return null;
		if (line.charAt(0) != '[')
			return null;
		
		String section = "";
		for (int i = 1; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == ']')
				return section;
			section += c;
		}
		
		return null;
	}
	
	private String getLineKey(String line) {
		if (line.length() < 3)
			return null;
		if (line.charAt(0) == '#' || line.charAt(0) == ';' || line.charAt(0) == '[')
			return null;
		
		String key = "";
		for (int i = 0; i < line.length(); i++) {
			String c = line.substring(i, i + 1);
			if (c.equals("=") || c.equals(" "))
				return key;
			key += c;
		}
		
		return null;
	}
}
