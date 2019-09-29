package com.base.engine.rendering.resourceManagement;

import java.io.File;

public class ResourceManager {
	public static final String ROOT_DIRECTORY = "./";
	
	public static String RESOURCE_DIRECTORY = "res/";
	public static String TEXTURE_DIRECTORY  = "textures/";
	public static String BITMAP_DIRECTORY   = "bitmaps/";
	public static String FONT_DIRECTORY     = "fonts/";
	public static String MODEL_DIRECTORY    = "models/";
	public static String SOUND_DIRECTORY    = "sounds/";
	public static String SHADER_DIRECTORY   = "shaders/";
	
	
	
	public static String getPath(String fileName, String relativeDirectory) {
		return (ROOT_DIRECTORY + RESOURCE_DIRECTORY + relativeDirectory + fileName);
	}
	
	public static File createFile(String fileName, String relativeDirectory) {
		return new File(getPath(fileName, relativeDirectory));
	}
} 
