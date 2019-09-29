package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import com.sun.imageio.plugins.jpeg.JPEG;

public class JuliaSetFractal {
	public int canvasWidth;
	public int canvasHeight;
	public Image image;
	public ComplexNumber minPoint;
	public ComplexNumber maxPoint;
	public ComplexNumber complexParameter;
	public int maxIterations;
	public int threshhold;
	public Color fillColor = new Color(0, 0, 0);
	
	private Color[][] pixels;
	
	
	public static Random random = new Random();
	
	public JuliaSetFractal() {
		canvasWidth      = 1600; // 4
		canvasHeight     = 1200; // 3
		minPoint         = new ComplexNumber(-1.7333, 1.3);
		maxPoint         = new ComplexNumber(1.7333, -1.3);
		maxIterations    = 2000;
		threshhold       = 1000;

		//setZoom(1.3);
		complexParameter = new ComplexNumber(-0.4, 0.6);
		complexParameter = new ComplexNumber(-0.8, 0.156);
		complexParameter = new ComplexNumber(0.285, 0.01);
		complexParameter = new ComplexNumber(-0.8, 0.156);
		
		image  = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
		pixels = new Color[canvasWidth][canvasHeight];
	}
	
	public void setZoom(double zoom) {
		minPoint = new ComplexNumber(-zoom, zoom);
		maxPoint = new ComplexNumber(zoom, -zoom);
	}
	
	public void createFractal() {
		ComplexNumber c = complexParameter;
		
		for (int x = 0; x < canvasWidth; x++) {
			for (int y = 0; y < canvasHeight; y++) {
				ComplexNumber z = getComplexPoint(x, y);
				boolean inside  = true;
				
				int interation = 0;
				for (int i = 0; i < maxIterations; i++) {
					interation = i;
					ComplexNumber z2 = GMath.sqr(z);
					
					if (z2.real + z2.imag > threshhold) {
						inside = false;
		                break;
		            }
					
					// f(z) = z^2 + c
					z.real = z2.real + c.real;
					z.imag = z2.imag + c.imag;
					if (random.nextInt(100) == -9) {
    					z.real -= random.nextDouble() / 10.0d;
    					z.imag -= random.nextDouble() / 10.0d;
					}
//					z.real = z2.real - c.real;
//					z.imag = z2.imag - c.imag;
					//z = GMath.sqr(z);
				}
				
				pixels[x][y] = fillColor;
				
				if (!inside) {
					pixels[x][y] = computeColor(interation);
				}
			}
		}
	}
	
	public void createImage() {
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);
		
		for (int x = 0; x < canvasWidth; x++) {
			for (int y = 0; y < canvasHeight; y++) {
				g.setColor(pixels[x][y]);
				g.drawRect(x, y, 0, 0);
			}
		}
		
		saveImage();
	}
	
	public void saveImage() {

		try{  
			//BufferedImage bi = new BufferedImage(700, 700, BufferedImage.TYPE_INT_RGB);  
			File outputfile = new File("src/image.png");  
			ImageIO.write((RenderedImage) image, "png", outputfile);  
		}
		catch (IOException e) {  
			System.out.println("Error saving image: " + e);
		}
		
		System.out.println("Image saved.");
	}
	
	public Color computeColor(int iteration) {
		double amount = ((double) iteration) / ((double) maxIterations);
		
		int r = (int) (amount * 255.0d);
		int g = (int) (amount * 255.0d);
		int b = (int) (amount * 255.0d);
		
		Color col = new Color(r, g, b);
		
		int xx = (int) ((double) iteration * 4.0d);
		int v = xx % 255;
		
		v = iteration % 255;
		
		col = new Color(v, v, v);
		
		return col;
	}
	
	public ComplexNumber getComplexPoint(int dx, int dy) {
		ComplexNumber z = new ComplexNumber(minPoint.real, minPoint.imag);
		z.real += ((double) dx / (double) canvasWidth)  * (maxPoint.real - minPoint.real);
		z.imag += ((double) dy / (double) canvasHeight) * (maxPoint.imag - minPoint.imag);
		
		return z;
	}
	
}
