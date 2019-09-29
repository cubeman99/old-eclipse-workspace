package com.base.engine.rendering;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.base.engine.rendering.resourceManagement.ResourceManager;

public class Bitmap {
	private int width;
	private int height;
	private int[] pixels;

	
	
	// ================== CONSTRUCTORS ================== //

	public Bitmap(String fileName) {
		try {
			BufferedImage image = ImageIO.read(ResourceManager.createFile(fileName, ResourceManager.BITMAP_DIRECTORY));
			width  = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Bitmap(int width, int height) {
		this.width  = width;
		this.height = height;
		this.pixels = new int[width * height];
	}
	
	
	
	// =================== ACCESSORS =================== //

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getPixel(int x, int y) {
		return pixels[(y * width) + x];
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	
	// ==================== MUTATORS ==================== //
	
	public Bitmap flipHorizontal() {
		for (int x = 0; x < width / 2; x++) {
			for (int y = 0; y < height; y++) {
				int temp = getPixel(x, y);
				setPixel(x, y, getPixel(width - x - 1, y));
				setPixel(width - x - 1, y, temp);
			}
		}
		return this;
	}
	
	public Bitmap flipVertical() {
		for (int y = 0; y < height / 2; y++) {
			for (int x = 0; x < width; x++) {
				int temp = getPixel(x, y);
				setPixel(x, y, getPixel(x, height - y - 1));
				setPixel(x, height - y - 1, temp);
			}
		}
		return this;
	}
	
	public void setPixel(int x, int y, int value) {
		pixels[(y * width) + x] = value;
	}
}
