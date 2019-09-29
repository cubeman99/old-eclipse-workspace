package base.engine.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import org.newdawn.slick.opengl.TextureLoader;
import base.engine.rendering.Texture;

public class ResourceLoader {
	private static String getFileExtension(String fileName) {
		String[] splitArray = fileName.split("\\.");
		return splitArray[splitArray.length - 1];
	}
}
