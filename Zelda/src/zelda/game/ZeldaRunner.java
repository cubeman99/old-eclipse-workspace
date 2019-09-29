package zelda.game;

import java.awt.Graphics;
import java.io.File;
import zelda.common.graphics.Draw;
import zelda.editor.Editor;
import zelda.game.control.GameInstance;
import zelda.game.world.World;
import zelda.main.GameRunner;


public class ZeldaRunner extends GameRunner {
	public GameInstance control;

	public ZeldaRunner() {
		super(60, 160, 144, CanvasMode.MODE_STRETCH);
		super.resizeWindow(160 * 4, 144 * 4);
		super.setTitle("Zelda - David Jordan");
	}

	@Override
	public void initialize() {
		control = new GameInstance(this);
	}

	@Override
	public void update() {
		control.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		Draw.setRunner(this);
		control.draw(g);
	}

	public static void main(String[] args) {
		//String fileName = "Level 4 - Skull Dungeon.zwd";
		String fileName = "Mine Cart Testing Zone.zwd";
		//String fileName = "testingWorld.zwd";

		Editor editor = new Editor(null);
		editor.loadWorld(new File(fileName));
		ZeldaRunner zr = new ZeldaRunner();
		GameInstance game = zr.control;
		game.setWorld(editor.getWorld());
		//Draw.setRunner(zr);
	}
}
