package base.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import org.newdawn.slick.opengl.TextureLoader;
import base.engine.Texture;

public class ResourceLoader {
	/*
	public static Texture loadTexture(String fileName) {
		String extension = getFileExtension(fileName);

		try {
			int id = TextureLoader.getTexture(extension, new FileInputStream(new File("./res/textures/" + fileName))).getTextureID();
			
			return new Texture(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
	public static String loadShader(String fileName)
	{
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		
		try
		{
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			String line;
			
			while((line = shaderReader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			
			shaderReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		
		return shaderSource.toString();
	}
	*/
	
	private static String getFileExtension(String fileName) {
		String[] splitArray = fileName.split("\\.");
		return splitArray[splitArray.length - 1];
	}
}
