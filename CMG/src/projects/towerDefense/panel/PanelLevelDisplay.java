package projects.towerDefense.panel;

import projects.towerDefense.GameInstance;

public class PanelLevelDisplay extends Panel {

	public PanelLevelDisplay(GameInstance game) {
		super(game);
		
	}

	@Override
	public void draw() {
		super.draw();
		
		game.getLevel().draw();
	}
}
