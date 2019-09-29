package cmg.ui;

import java.awt.Graphics;
import projects.towerDefense.GameInstance;
import cmg.graphics.Draw;
import cmg.math.GMath;

public class TestUIRunner extends UIControl {
	public GameInstance game;
	
	public TestUIRunner() {
		super(60);
		game = new GameInstance(null);
	}
	
	@Override
	public void initialize() {
		Window wnd = new Window(366, 456, false);
		
		Button btn1 = new Button("Reset to Defaults", 0);
		Button btn2 = new Button("Done", 1);
		btn1.setRect(20, 100, 120, 30);
		btn2.setRect(20, 150, 80, 30);
		Label lbl1 = new Label("New to Battle.net?");
		lbl1.setPosition(20, 50);
		Checkbox cbx1 = new Checkbox(true, "Launch Battle.net when you start your computer");
		cbx1.setPosition(20, 200);
		
		wnd.add(btn1);
		wnd.add(btn2);
		wnd.add(lbl1);
		wnd.add(cbx1);
		
		
		addWindow(wnd);
		
		
	}

	@Override
	public void update() {
		game.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		game.draw();
	}
	
	public static void main(String[] args) {
		new TestUIRunner();
	}
}
