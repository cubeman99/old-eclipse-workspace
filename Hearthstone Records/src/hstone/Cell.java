package hstone;

import java.awt.Color;

public class Cell {
	private String text;
	private Color textColor;
	private Color backgroundColor;
	private boolean bold;
	
	

	// ================== CONSTRUCTORS ================== //

	public Cell() {
		this.bold            = false;
		this.text            = "";
		this.textColor       = Color.BLACK;
		this.backgroundColor = Color.WHITE;
	}
	
	public Cell(String text) {
		this.bold            = false;
		this.text            = text;
		this.textColor       = Color.BLACK;
		this.backgroundColor = Color.WHITE;
	}
	
	public Cell(String text, Color backgroundColor) {
		this.bold            = false;
		this.text            = text;
		this.textColor       = Color.BLACK;
		this.backgroundColor = backgroundColor;
	}
	
	public Cell(String text, Color backgroundColor, Color textColor) {
		this.bold            = false;
		this.text            = text;
		this.textColor       = textColor;
		this.backgroundColor = backgroundColor;
	}

	

	// =================== ACCESSORS =================== //
	
	public boolean isBold() {
		return bold;
	}
	
	public String getText() {
		return text;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
