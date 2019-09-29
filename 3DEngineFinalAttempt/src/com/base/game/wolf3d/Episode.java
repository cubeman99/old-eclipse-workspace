package com.base.game.wolf3d;

import java.util.ArrayList;

public class Episode {
	private String fileName;
	private ArrayList<String> levelNames;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Episode(String fileName, int numLevels) {
		this.fileName = fileName;
		levelNames = new ArrayList<String>();
		
		for (int i = 0; i < numLevels; i++) {
			levelNames.add(fileName + "L" + (i + 1) + ".lvl");
		}
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Level createLevel(Game game, int levelIndex) {
		Level level = new Level(game);
		level.load(levelNames.get(levelIndex));
		return level;
	}
	
	public String getLevelFileName(int levelIndex) {
		return levelNames.get(levelIndex);
	}
}
