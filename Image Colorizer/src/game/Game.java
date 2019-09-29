package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;


public class Game extends JComponent {
	private static final long serialVersionUID = 1L;

	public static int VIEW_WIDTH  = 640;
	public static int VIEW_HEIGHT = 480;
	public static Image buffer;
	public static Mouse mouse;
    public static Image img;
    public static Image testImage;
    public static Image testImageColorized;
    
    
    
	public Game() {
		buffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setFocusable(true);
		
		// Install Mouse and Keyboard, Load images, and initialize the game
		mouse = new Mouse();
		addMouseListener(mouse);
		addKeyListener(new Keyboard());
		ImageLoader.loadAllImages();
        initializeGame();
	}
	
	public Image colorizeImage(Image image, Color color) {
		BufferedImage img = loadImage(image);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color c1 = new Color(img.getRGB(x, y));

	            int r = c1.getRed();
	            int g = c1.getGreen();
	            int b = c1.getBlue();
	            //r = 255;

				float[] hsb = new float[3];
				float[] hsb2 = new float[3];
	            Color.RGBtoHSB(r, g, b, hsb);
	            Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb2);
	            hsb[0]  = hsb2[0]; // set the hue to 0
	            hsb[1]  = hsb2[1];
	            hsb[2] *= hsb2[2];
	            
	            //img.setRGB(x, y, new Color(r, g, b).getRGB());
	            img.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
			}
		}
		return img;
	}
	
	public BufferedImage loadImage(Image image) {
	    // Create empty BufferedImage, sized to Image
	    BufferedImage buffImage = 
	      new BufferedImage(
	        image.getWidth(null), 
	        image.getHeight(null), 
	        BufferedImage.TYPE_INT_ARGB);

	    // Draw Image into BufferedImage
	    Graphics g = buffImage.getGraphics();
	    g.drawImage(image, 0, 0, null);
	    return buffImage;
	}
	
	public void initializeGame() {
		// INITIALIZE DIFFERENT CLASSES
		// ...
		testImage = ImageLoader.getImage("test");
		testImageColorized = colorizeImage(testImage, new Color(128, 128, 128));
	}
	
	
	public void update() {
		// UPDATE EVENTS HERE
		// ...
		
		
		
		
		if (Keyboard.escape.down())
			Main.stop();
	}
	
	public void render(Graphics g) {
		// Draw a black background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		g.setColor(Color.BLACK);

		// DRAW EVENTS HERE
		// ...
		
		g.drawImage(testImage, 16, 16, null);
		g.drawImage(testImageColorized, 320, 16, null);
	}
	
	public void paintComponent(Graphics g) {
		Graphics bufferGraphics = buffer.getGraphics();
		render(bufferGraphics);
		bufferGraphics.dispose();
		g.drawImage(buffer, 0, 0, null);
	}
}
