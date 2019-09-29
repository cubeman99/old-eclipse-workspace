import java.awt.Image;

import javax.swing.ImageIcon;


public class ImageLoader {
	public static Image imageBackdrops 	= loadImage("backdrops.png");
	public static Image imageTerrain 	= loadImage("terrain.png");
	public static Image imagePlayer 	= loadImage("player.png");
	public static Image imageCursor 	= loadImage("cursor.png");
	public static Image imageVines		= loadImage("vines.png");
	public static Image imageTree		= loadImage("tree.png");
	public static Image imageCracks		= loadImage("cracks.png");
	public static Image imageSkygrad	= loadImage("skygrad.png");
	//public static Image imageItems		= loadImage("items.png");
	
	public static Image loadImage(String path) {
		return new ImageIcon(ImageLoader.class.getResource("images/" + path)).getImage();
	}
}
