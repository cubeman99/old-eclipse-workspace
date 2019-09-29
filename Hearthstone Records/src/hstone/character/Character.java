package hstone.character;

import java.awt.Color;

public class Character {
	private String title;
	private String name;
	private Color color;

	
	
	// ================== CONSTRUCTORS ================== //
	
	public Character(String title, String name, Color color) {
		this.title = title;
		this.name  = name;
		this.color = color;
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Color getColor() {
		return color;
	}
	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}
	
	
	
	// ==================== MUTATORS ==================== //

}
