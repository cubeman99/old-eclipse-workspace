import java.awt.Image;

import javax.swing.ImageIcon;


public class ImageLoader {
	public static Image imagePolly 	= loadImage("Polly.png");
	public static Image imagePlant 	= loadImage("Plant.png");
	
	public static Image loadImage(String path) {
		return new ImageIcon(ImageLoader.class.getResource("images/" + path)).getImage();
	}
}
