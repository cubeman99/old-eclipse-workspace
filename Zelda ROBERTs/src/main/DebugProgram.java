package main;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * This class sets up a windows and starts a thread to update and render
 * the Game.
 * @author	Robert Jordan
 * @author	David Jordan
 * @see
 * {@linkplain Game}
 */
public class DebugProgram {

	
	// ===================== Entry Point ======================
	
	/** The entry point for the program. */
	public static void main(String [] args) {
		boolean oldMethod = false;
		ArrayList<String> files = new ArrayList<String>();
		if (oldMethod)
			files.addAll(ResourceLoader.getResources("", "", ".asc", false));
		else {
			files.addAll(ResourceLoader.getResources("", "", ".mp3", false));
			files.addAll(ResourceLoader.getResources("", "", ".MP3", false));
			files.addAll(ResourceLoader.getResources("", "", ".wav", false));
			files.addAll(ResourceLoader.getResources("", "", ".WAV", false));
			files.addAll(ResourceLoader.getResources("", "", ".aiff", false));
			files.addAll(ResourceLoader.getResources("", "", ".AIFF", false));
		}

		int numFiles = 0;
		for (int i = 0; i < files.size(); i++) {
			String file = files.get(i);
			boolean fileAlreadyFound = false;
			for (int j = 0; j < i; j++) {
				if (file.equals(files.get(j)))
					fileAlreadyFound = true;
			}
			if (!fileAlreadyFound) {
				numFiles++;
			}
		}
		File outFile = null;
		OutputStream outStream = null;
		//DataOutputStream out = null;
		BufferedWriter out = null;
		if (!oldMethod) {
			outFile = new File("playlist.txt");
			try {
				if (!outFile.exists())
					outFile.createNewFile();
				outStream = new FileOutputStream(outFile);
				//out = new DataOutputStream(outStream);
				out = new BufferedWriter(new OutputStreamWriter(outStream));
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Found " + String.valueOf(numFiles) + " files.");
		for (int i = 0; i < files.size(); i++) {
			String file = files.get(i);
			boolean fileAlreadyFound = false;
			for (int j = 0; j < i; j++) {
				if (file.equals(files.get(j)))
					fileAlreadyFound = true;
			}
			if (!fileAlreadyFound) {
				if (oldMethod) {
					loadLevel(file);
				}
				else {
					try {
						String dir = System.getProperty("user.dir");
						String finalFile = file.substring(dir.length() + 1, file.length());
						System.out.println(finalFile);
						out.write(finalFile);
						out.newLine();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished");
	}
	
	private static void loadLevel(String path) {

		try {
			int width, height;
			char[][] chars1, chars2;
			char[][] fcolors1, fcolors2;
			char[][] bcolors1, bcolors2;
			InputStream stream = new FileInputStream(path);
			System.out.println("- " + path);
			DataInputStream in = new DataInputStream(stream);
			
			in.skipBytes(19);
			width = in.readUnsignedByte();
			in.skipBytes(1);
			height = in.readUnsignedByte();
			in.skipBytes(1);
			
			chars1 = new char[width][height];
			chars2 = new char[width][height];
			fcolors1 = new char[width][height];
			fcolors2 = new char[width][height];
			bcolors1 = new char[width][height];
			bcolors2 = new char[width][height];
			
			for( int j = 0; j < height; j++ ) {
				for( int i = 0; i < width; i++ ) {
					chars1[i][j] = (char)in.readUnsignedByte();
					char color = (char)in.readUnsignedByte();
					
					fcolors1[i][j] = (char)(color & 0xf);
					bcolors1[i][j] = (char)((color >> 4) & 0xf);
					
					in.skipBytes(1);
				}
			}
			for( int j = 0; j < height; j++ ) {
				for( int i = 0; i < width; i++ ) {
					chars2[i][j] = (char)in.readUnsignedByte();
					char color = (char)in.readUnsignedByte();
					
					fcolors2[i][j] = (char)(color & 0xf);
					bcolors2[i][j] = (char)((color >> 4) & 0xf);
					
					in.skipBytes(1);
				}
			}
			in.close();
			
			String outPath = path.substring(0, path.length() - 4) + ".txt";
			File outFile = new File(outPath);
			if (outFile.exists() || outFile.createNewFile()) {
			
				OutputStream outStream = new FileOutputStream(outFile);
				DataOutputStream out = new DataOutputStream(outStream);
				
				writeString(out, "levels.push(new LevelData(");
				writeString(out, String.valueOf(width));
				writeString(out, ", ");
				writeString(out, String.valueOf(height));
				writeString(out, ", ");
				writeString(out, "[");
				for (int i = 0; i < width; i++) {
					writeString(out, "[");
					for (int j = 0; j < height; j++) {
						writeString(out, "[");
						writeString(out, String.valueOf((int)chars1[i][j]));
						writeString(out, ",");
						writeString(out, String.valueOf((int)fcolors1[i][j]));
						writeString(out, ",");
						writeString(out, String.valueOf((int)bcolors1[i][j]));
						writeString(out, "]");
						if (j + 1 < height)
							writeString(out, ",");
							
					}
					writeString(out, "]");
					if (i + 1 < height)
						writeString(out, ",");
				}
				writeString(out, "]");
				writeString(out, ", ");
				writeString(out, "[");
				for (int i = 0; i < width; i++) {
					writeString(out, "[");
					for (int j = 0; j < height; j++) {
						writeString(out, "[");
						writeString(out, String.valueOf((int)chars2[i][j]));
						writeString(out, ",");
						writeString(out, String.valueOf((int)fcolors2[i][j]));
						writeString(out, ",");
						writeString(out, String.valueOf((int)bcolors2[i][j]));
						writeString(out, "]");
						if (j + 1 < height)
							writeString(out, ",");
							
					}
					writeString(out, "]");
					if (i + 1 < height)
						writeString(out, ",");
				}
				writeString(out, "]");
				writeString(out, "));");
			}
			else {
				System.out.println("Failed to create output file of : " + outPath);
			}
			
		}
		catch (FileNotFoundException e) {
			System.out.println("Failed to find: " + path);
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("Failed to convert: " + path);
			e.printStackTrace();
		}
	}
	
	private static void writeString(DataOutputStream out, String string) throws IOException {
		out.write(string.getBytes());
	}
}