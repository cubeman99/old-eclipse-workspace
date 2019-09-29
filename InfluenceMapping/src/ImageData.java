

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageData {
	public static Image pickupHealth;
	public static Image pickupAmmo;
	
	public static void loadAllImages() throws IOException {
		pickupHealth	= loadImage("pickup_health.png");
		pickupAmmo		= loadImage("pickup_ammo.png");
	}
	public static Image loadImage(String path) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(Game.class.getResource("images/" + path));
		return (Image) bufferedImage;
	}
}