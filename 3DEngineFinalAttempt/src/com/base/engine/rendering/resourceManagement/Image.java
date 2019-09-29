package com.base.engine.rendering.resourceManagement;

import java.nio.ByteBuffer;

public class Image {
	private int width;
	private int height;
	private ByteBuffer data;
	
	public Image(ByteBuffer data, int width, int height) {
		this.data   = data;
		this.width  = width;
		this.height = height;
	}
	
	public ByteBuffer getData() {
		return data;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
