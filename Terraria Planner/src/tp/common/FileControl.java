package tp.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import tp.main.Keyboard;
import tp.main.Mouse;
import tp.planner.Control;
import tp.planner.Item;
import tp.planner.ItemData;

public class FileControl {
	public static File WORKING_DIRECTORY;
	public static File SAVE_DIRECTORY;
	public static File CURRENT_FILE;
	private static Item[] template;
	private static Control control;
	private static final String WINDOW_CAPTION = " - Terraria Planner";
	
	
	public static void initialize(Control control) {
		FileControl.control = control;
		
		try {
			WORKING_DIRECTORY = new File(FileControl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		CURRENT_FILE   = null; // The current file being edited.
		SAVE_DIRECTORY = new File(WORKING_DIRECTORY.getPath());
		
		loadConfig();
		loadConversionTemplate();
	}
	
	private static void saveConfig() {
		File file = new File("config.txt");
		
	}
	
	private static void loadConfig() {
		File file = new File("config.txt");
		
		if (!file.exists())
			return;
		
		try {
			Scanner reader = new Scanner(new File("config.txt"));
			
			SAVE_DIRECTORY = new File(reader.nextLine());
			
			reader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadConversionTemplate() {
		try {
			Scanner reader = new Scanner(new File("saveConversionTemplate.txt"));
			template = new Item[250];
			for (int i = 0; i < template.length; i++)
				template[i] = null;
			
			while (reader.hasNext()) {
				int index = reader.nextInt();
				reader.nextLine();
				String name = reader.nextLine();
				template[index] = ItemData.find(name);
			}
			
			reader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public static Item getConvertedItem(int itemOldIndex) {
		if (template == null)
			loadConversionTemplate();
		
		if (itemOldIndex >= template.length) {
			System.out.println("Item #" + itemOldIndex + " is out of bounds!");
			return null;
		}
		if (template[itemOldIndex] == null)
			System.out.println("Cant find item #" + itemOldIndex);
		
		return template[itemOldIndex];
	}
	
	private static void setCurrentFile(File file) {
		CURRENT_FILE = file;
		if (file == null)
			control.runner.setTitle("[untitled]" + WINDOW_CAPTION);
		else
			control.runner.setTitle(CURRENT_FILE.getName() + WINDOW_CAPTION);
	}
	
	public static void newFile() {
		control.clearWorld();
		setCurrentFile(null);
	}

	public static void openFile() {
		JFileChooser fc = new JFileChooser(FileControl.SAVE_DIRECTORY);
		Mouse.clear();
		Keyboard.clear();
		int option = fc.showOpenDialog(control.runner.getFrame());
		
        if (option == JFileChooser.APPROVE_OPTION) {
        	setCurrentFile(fc.getSelectedFile());
            control.world.loadFromFile(CURRENT_FILE);
        }
	}
	
	public static void saveFile() {
		if (CURRENT_FILE == null)
			saveFileAs();
		else
            control.world.saveToFile(CURRENT_FILE);
	}
	
	public static void saveFileAs() {
		JFileChooser fc = new JFileChooser(FileControl.SAVE_DIRECTORY);
		Mouse.clear();
		Keyboard.clear();
		int option = fc.showSaveDialog(control.runner.getFrame());
		
        if (option == JFileChooser.APPROVE_OPTION) {
        	setCurrentFile(fc.getSelectedFile());
            control.world.saveToFile(CURRENT_FILE);
        }
	}
	
	public static void saveImage() {
		JFileChooser fc = new JFileChooser(FileControl.SAVE_DIRECTORY);
		Mouse.clear();
		Keyboard.clear();
		int option = fc.showSaveDialog(control.runner.getFrame());
		
        if (option == JFileChooser.APPROVE_OPTION) {
        	File file = fc.getSelectedFile();
            
            try {
            	// TODO
            	BufferedImage bi = (BufferedImage) control.world.getCanvas().getImage();
                ImageIO.write(bi, "png", file);
            }
            catch (IOException e) {
            	e.printStackTrace();
            }
        }
	}
}
