import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.LineEvent;


public class FileHandler {
	private static final String PATH_SKIN =
			"C:/Users/David/Documents/Rainmeter/Skins/cubeman/";
	private static final String FILE_NAME =
			"C:/Users/David/Documents/Rainmeter/Skins/cubeman/variables.var";
	public ArrayList<String> lines;
	public ArrayList<Option> options;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public FileHandler() {
		lines   = new ArrayList<String>();
		options = new ArrayList<Option>();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public ArrayList<Option> getOptions() {
		return options;
	}

	public Option getOption(int index) {
		return options.get(index);
	}
	
	
	// ==================== MUTATORS ==================== //
	
	public void openFile() {
		// Read the lines.
		try {
			Scanner reader = new Scanner(new File(FILE_NAME));
			
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				lines.add(line);
			}
			
			reader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Process the lines to find all options.
		processLines();
	}
	
	public void saveFile() {
//		int index = 0;
//		for (int i = 0; i < lines.size(); i++) {
//			if (options.get(index).getLineIndex() == i) {
//				lines.set(i, options.get(index).getLineString());
//				index++;
//			}
//			System.out.println(lines.get(i));
//		}
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME));
			
			int index = 0;
			for (int i = 0; i < lines.size(); i++) {
				if (options.get(index).getLineIndex() == i) {
					lines.set(i, options.get(index).getLineString());
					index++;
				}
				out.write(lines.get(i));
				out.newLine();
			}
			
			out.close();
			refreshSkins();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void refreshSkins() {
		try {
			Runtime.getRuntime().exec("cmd /c start " + PATH_SKIN + "refresh.bat");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void processLines() {
		for (int i = 0; i < lines.size(); i++) {
			Option opt = processOption(i);
			
			if (opt != null) {
				// Process the title and description of the variable.
				String def   = getLineComment(i - 1);
				String desc  = getLineComment(i - 2);
				String title = getLineComment(i - 3);
				
				if (def != null)
					opt.processDefault(def);
				if (desc != null)
					opt.setDescription(desc);
				if (title != null)
					opt.setVariableTitle(title);
				
				options.add(opt);
			}
		}
	}
	
	private Option processOption(int lineIndex) {
		String line = lines.get(lineIndex);
		
		if (line.length() < 3 || (line.charAt(0) == ';'))
			return null;
		
		String name  = "";
		String value = "";
		boolean readingName = true;
		
		// Parse the variable's name and value.
		for (int i = 0; i < line.length(); i++) {
			if (readingName) {
				if (line.charAt(i) == '=')
					readingName = false;
				else
					name += line.charAt(i);
			}
			else
				value += line.charAt(i);
		}
		
		if (readingName)
			return null;
		
		return new Option(options, lineIndex, name, value);
	}
	
	private String getLineComment(int lineIndex) {
		if (lineIndex < 0)
			return null;
		
		String line = lines.get(lineIndex);
		if (line.length() < 3 || !(line.charAt(0) == ';'))
			return null;
		
		return line.substring(2);
	}
}
