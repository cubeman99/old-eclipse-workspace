package com.base.game.wolf3d.tile;

public class PlayerStart extends Tile {

	public PlayerStart() {
		super("Player Start", false);
		
	}

	@Override
	public PlayerStart clone() {
		return new PlayerStart();
	}
}
