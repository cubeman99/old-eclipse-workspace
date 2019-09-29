

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageLoader {
	public static Image gateOr;
	public static Image gateInvert;
	public static Image gateLightOff;
	public static Image gateLightOn;
	
	public static void loadAllImages() throws IOException {
		gateOr 			= loadImage("gateOr.png");
		gateInvert 		= loadImage("gateInvert.png");
		gateLightOff 	= loadImage("gateLightOff.png");
		gateLightOn 	= loadImage("gateLightOn.png");
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