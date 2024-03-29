

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageLoader {
	public static Image blockOverlay;
	
	public static void loadAllImages() throws IOException {
		blockOverlay = loadImage("blockOverlay.png");
	}
	
	public static Image loadImage(String path) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(Game.class.getResource("images/" + path));
		return (Image) bufferedImage;
	}
	
	public static Image getTile(Image img, int sx, int sy, int width, int height) {
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		newImg.getGraphics().drawImage(img, 0, 0, width, height, sx * width, sy * height, (sx + 1) * width, (sy + 1) * height, null);
		return (Image) newImg;
	}
}