package projects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProjectCodeLines {
	public int totalLines;
	public String extension;
	public int count;
	
	
	public ProjectCodeLines() {
		totalLines = 0;
		extension = ".cs";
		//scanFiles(new File("C:/Eclipse Workspace/workspace"));
		//scanFiles(new File("J:/David Homework/Sophomore Simester 1/Game Graphics/PlatformerGameFinal/Platformer/Platformer/Platformer"));
		//scanFiles(new File("J:/David Homework/Sophomore Simester 1/Game Graphics/PlatformerGameFinal/GameCore"));
		//System.out.println("TOTAL LINES: " + totalLines);
		
		int coreLines = scanFiles(new File("J:/David Homework/Sophomore Simester 1/Game Graphics/PlatformerGameFinal/Platformer/Platformer/Platformer"));
		int projLines = scanFiles(new File("J:/David Homework/Sophomore Simester 1/Game Graphics/PlatformerGameFinal/GameCore"));
		System.out.println("Platformer: " + (coreLines + projLines) + " (" + coreLines + " + " + projLines + ")");
		
		coreLines = scanFiles(new File("C:/Users/David/Desktop/Framework/Framework/GameCore"));
		projLines = scanFiles(new File("C:/Users/David/Desktop/Framework/Framework/Game"));
		System.out.println("Framework:  " + (coreLines + projLines) + " (" + coreLines + " + " + projLines + ")");
		
		//System.out.println("Graphics:   " + scanFiles(new File("J:/David Homework/Sophomore Simester 1/Game Graphics")));
		
		extension = ".java";
		System.out.println("Zelda:      " + scanFiles(new File("C:/Eclipse Workspace/workspace/Zelda")));
	}

	public int scanFiles(File directory) {
		count = 0;
		scanFiles(directory, "", 0);
		return count;
	}
	
	private void scanFiles(File directory, String base, int level) {
		for (final File file : directory.listFiles()) {
			String name = base;
			if (!base.isEmpty())
				name += ".";
			name += file.getName();
					
	        if (file.isDirectory()) {
	        	String next = base;
	        	if (next.isEmpty())
	        		next += ".";

	        	handleDirectory(file, name, level);
	        	scanFiles(file, name, level + 1);
	        }
	        else {
	        	handleFile(file, name, level);
	        }
	    }
	}
	
	private void handleDirectory(File dir, String name, int level) {
		String str = "";
		for (int i = 0; i < level; i++)
			str += "  ";
		str += dir.getName();
		//System.out.println(str);
	}
	
	private void handleFile(File file, String name, int level) {
		if (!file.getName().endsWith(extension))
			return;
		String str = "";
		for (int i = 0; i < level; i++)
			str += "  ";
		str += file.getName();

		int lines = 0;
		
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				lines++;
				scanner.nextLine();
			}
			scanner.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		totalLines += lines;
		
		//System.out.println(lines + " - " + file.getName());
		count += lines;
	}
	
	public static void main(String[] args) {
		new ProjectCodeLines();
	}
}
