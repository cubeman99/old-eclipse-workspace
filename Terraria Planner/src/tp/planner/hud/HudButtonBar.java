package tp.planner.hud;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import tp.common.FileControl;
import tp.common.Point;
import tp.common.graphics.Draw;
import tp.common.graphics.Sheet;
import tp.main.ImageLoader;
import tp.main.Mouse;


public class HudButtonBar extends HudPanel {
	private Image imageBar;
	private Sheet sheetbuttonIcons;
	private int buttonSep;
	private ArrayList<Button> buttons;
	
	
	public HudButtonBar(HUD hud) {
		super(hud);
		imageBar         = ImageLoader.getImage("hudButtonPanel");
		sheetbuttonIcons = new Sheet("hudButtonIcons", 32, 32, 2);
		buttons  = new ArrayList<Button>();
		
		createButtons();
		
		buttonSep = 38;
		position.set(0, 0);
		size.set(0, HUD.BAR_THICKNESS);
		
		for (Button b : buttons)
			b.setControl(control);
	}
	
	private void createButtons() {
		
		buttons.add(new Button("New", "ctrl + n", KeyEvent.VK_N, KeyEvent.VK_CONTROL) {
    		public void onPress() {
    			FileControl.newFile();
    		}
		});
		
		buttons.add(new Button("Open", "ctrl + o", KeyEvent.VK_O, KeyEvent.VK_CONTROL) {
    		public void onPress() {
    			FileControl.openFile();
    		}
		});
		
		buttons.add(new Button("Save", "ctrl + s", KeyEvent.VK_S, KeyEvent.VK_CONTROL) {
    		public void onPress() {
    			FileControl.saveFile();
    		}
		});
		
		buttons.add(new Button("Save As", "ctrl + a", KeyEvent.VK_A, KeyEvent.VK_CONTROL) {
    		public void onPress() {
    			FileControl.saveFileAs();
    		}
		});
		
		buttons.add(new Button("Copy", "ctrl + c", KeyEvent.VK_C, KeyEvent.VK_CONTROL) {
    		public void onPress() {
    			
    		}
		});
		
		buttons.add(new Button("Paste", "ctrl + v", KeyEvent.VK_V, KeyEvent.VK_CONTROL) {
    		public void onPress() {
    			
    		}
		});
		
		buttons.add(new Button("Save Picture") {
    		public void onPress() {
    			FileControl.saveImage();
    		}
		});
		
		buttons.add(new Button("Resize World") {
    		public void onPress() {
    			
    		}
		});
		
		buttons.add(new Button("Set Background", 4) {
    		public void onPress() {
    			this.control.setBackdropIndex(getToggleIndex());
    		}
		});
		
		buttons.add(new Button("Grid", 2) {
    		public void onPress() {
    			this.control.setShowGrid(getToggleIndex() == 1);
    		}
		});
		
		buttons.add(new Button("Help") {
    		public void onPress() {
    			
    		}
		});
	}
	
	@Override
	public void update() {
		for (Button b : buttons) {
			if (b.isKeyPressed())
				b.press();
		}
	}
	
	@Override
	public void draw() {
		size.x = runner.getViewWidth();
		
		for (int x = 0; x < runner.getViewWidth(); x += 200)
			hud.graphics.drawImage(imageBar, x, 0, null);
		
		
		for (int i = 0; i < buttons.size(); i++) {
			Point dp = position.plus((size.y / 2) + (i * buttonSep), size.y / 2);
			Button b = buttons.get(i);
			
			Draw.drawSpriteCentered(sheetbuttonIcons, dp.x, dp.y, i, b.getToggleIndex());
			
			if (hud.mouseOverArea(dp.x, dp.y, buttonSep)) {
				hud.drawButtonOverlay(dp.x, dp.y, Mouse.left.down());
				if (Mouse.left.pressed()) {
					buttons.get(i).press();
				}
			}
		}
		
		// TODO: Buttons!
	}
}
