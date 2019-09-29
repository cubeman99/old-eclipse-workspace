package common;

import java.awt.Graphics;
import java.awt.Graphics2D;


public class HUD {
	private static Graphics2D graphics = null;
	
	public static void setGraphics(Graphics g) {
		HUD.graphics = (Graphics2D) g;
	}
	
	public static Graphics2D getGraphics() {
		return graphics;
	}
}
