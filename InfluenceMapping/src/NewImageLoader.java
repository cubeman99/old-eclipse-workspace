import java.awt.Image;

import javax.swing.ImageIcon;


public class NewImageLoader {
	
	public static Image loadImage(String path) {
		Image img = new ImageIcon(NewImageLoader.class.getResource(path)).getImage();
		System.out.println(path + " (Loaded!)");
		return img;
	}
}
