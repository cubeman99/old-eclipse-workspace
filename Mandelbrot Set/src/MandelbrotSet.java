import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class MandelbrotSet extends JComponent {
	private static final int PWIDTH  = 600; // size of the panel
	private static final int PHEIGHT = (int)(PWIDTH * (2.4f / 3.0f));
	private static final int FPS     = 1000 / 60;
	
	private static boolean running;
	private static boolean fractal_redraw = true;
	public static Vector mouseVec;
	public static int mouseX;
	public static int mouseY;
	
	public static Image buffer;
	
	public static boolean mouseDragging = false;
	public static int mouseDragX = 0;
	public static int mouseDragY = 0;
	public static boolean mousePressed = false;
	public static boolean mouseDown = false;
	
	public static double MinRe = -2.0;
	public static double MaxRe = 1.0;
	public static double MinIm = -1.2;
	public static double MaxIm = 1.2;
	public static double Re_factor = (MaxRe - MinRe) / (PWIDTH - 1);
	public static double Im_factor = (MaxIm - MinIm) / (PHEIGHT - 1);
	public static int MaxIterations = 100;
	public static int power = 2;
	
	public static ArrayList<Color> colorList;
	public static ArrayList<ArrayList<Color>> pixels;
	
	public MandelbrotSet() {
		setLayout(new BorderLayout());
		setOpaque(false);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

		installListeners();
		
		buffer = new BufferedImage(PWIDTH, PHEIGHT, BufferedImage.TYPE_INT_RGB);
		
		colorList = new ArrayList<Color>();
		
		colorScheme1();
		
		pixels = new ArrayList<ArrayList<Color>>();
		
		for( int y = 0; y < PHEIGHT; y += 1 ) {
			ArrayList<Color> row = new ArrayList<Color>();
			for( int x = 0; x < PWIDTH; x += 1 ) {
				row.add(new Color(0, 0, 0));
			}
			pixels.add(row);
		}
		
		//paint(getGraphics());
		
		Graphics g = buffer.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, PWIDTH, PHEIGHT);
	}
	
	public void colorScheme1() {
		colorList.add(new Color( 12,   0,  64)); // dark blue
		colorList.add(new Color( 28,  93, 197)); // light blue
		colorList.add(new Color(255, 255, 255)); // white
		colorList.add(new Color(252, 193,  15)); // light orange
		colorList.add(new Color(191,  76,  13)); // dark orange
		colorList.add(new Color( 12,   0,  64)); // dark blue
	}

	public void colorScheme2() {
		colorList.add(new Color(  0,   0,  0)); // black
		colorList.add(new Color(255,   0,  0)); // red
		colorList.add(new Color(255, 128,  0)); // orange
		colorList.add(new Color(255, 255,  0)); // yellow
		colorList.add(new Color(255, 128,  0)); // orange
		colorList.add(new Color(255,   0,  0)); // red
		colorList.add(new Color(  0,   0,  0)); // black
	}
	
	private void installListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseVec = new Vector(e.getX(), e.getY());
				mousePressed = true;
				mouseDragging = true;
				mouseDragX = (int) mouseVec.getX();
				mouseDragY = (int) mouseVec.getY();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				mouseVec = new Vector(e.getX(), e.getY());
				int size = 100;
				if( e.getButton() == MouseEvent.BUTTON1 )
					size = -size;
				mouseDragX = (int) mouseVec.getX() - (size / 2);
				mouseDragY = (int) mouseVec.getY() - (size / 2);
				mouseX = (int) mouseVec.getX() + (size / 2);
				mouseY = (int) mouseVec.getY() + (size / 2);
				if( mouseDragging ) {
					mouseDragging = false;
					
					int x1, y1, x2, y2;
					x1 = Math.min(mouseDragX, mouseX);
					y1 = (int)Math.min((float)mouseDragY, mouseDragY + ((float)(mouseX - mouseDragX) * ((float)PHEIGHT / (float)PWIDTH)));
					x2 = Math.max(mouseDragX, mouseX);
					y2 = (int)Math.max((float)mouseDragY, mouseDragY + ((float)(mouseX - mouseDragX) * ((float)PHEIGHT / (float)PWIDTH)));
					
					double tempMinRe = MinRe;
					double tempMaxIm = MaxIm;
					
					MinRe = tempMinRe + (x1 * Re_factor);
					MaxRe = tempMinRe + (x2 * Re_factor);
					MinIm = tempMaxIm - (y2 * Im_factor);
					MaxIm = tempMaxIm - (y1 * Im_factor);
					
					Re_factor = (MaxRe - MinRe) / (PWIDTH - 1);
					Im_factor = (MaxIm - MinIm) / (PHEIGHT - 1);
					
					fractal_redraw = false;
					
					fractalCreate();
					repaint();
				}
			}
		});
	}
	
	public void gameUpdate() {
		updateMousePosition();
		if( mousePressed ) {
			mouseDragging = true;
			mouseDragX = mouseX;
			mouseDragY = mouseY;
		}
	}
	
	public void setPixel(int x, int y, Color c) {
		pixels.get(y).set(x, c);
	}
	
	public Color getPixel(int x, int y) {
		return( pixels.get(y).get(x) );
	}
	
	public void updateMousePosition() {
		mouseVec = new Vector(MouseInfo.getPointerInfo().getLocation());
		Vector winVec = new Vector(this.getLocation());
		mouseVec.sub(winVec);
		mouseVec.set(mouseVec);
		mouseVec.sub(4, 26);
		mouseX = (int) mouseVec.getX();
		mouseY = (int) mouseVec.getY();
	}
	
	public Color mergeColor(Color c1, Color c2, double amount) {
		int r, g, b;
		r = c1.getRed() + (int)(amount * (double)(c2.getRed() - c1.getRed()));
		g = c1.getGreen() + (int)(amount * (double)(c2.getGreen() - c1.getGreen()));
		b = c1.getBlue() + (int)(amount * (double)(c2.getBlue() - c1.getBlue()));
		return new Color(r, g, b);
	}
	
	public void fractalCreate() {
		int ImageWidth   = PWIDTH;
		int ImageHeight  = PHEIGHT;
		
		Graphics g = buffer.getGraphics();

		ComplexNumber c = new ComplexNumber();
		
		for( int y = 0; y < ImageHeight; ++y ) {
		    c.setImag(MaxIm - (y * Im_factor));
		    
		    for( int x = 0; x < ImageWidth; ++x ) {
		        c.setReal(MinRe + (x * Re_factor));
		        ComplexNumber Z = new ComplexNumber(c);
		        
		        boolean isInside = true;
		        int n = 0;
		        int I = 0;
		        for( ; n < MaxIterations; ++n ) {
		            ComplexNumber sqrZ = new ComplexNumber(Z);
		            sqrZ.real = Math.pow(sqrZ.real, power);
		            sqrZ.imag = Math.pow(sqrZ.imag, power);
		            
		            if( sqrZ.real + sqrZ.imag > 4 ) {
		                isInside = false;
		                break;
		            }
		            Z.setImag((2 * Z.real * Z.imag) + c.imag);
		            Z.setReal((sqrZ.real - sqrZ.imag) + c.real);
		        }
		        
		        // Draw a dot
		        Color col = new Color(0, 0, 0);
		        
		        if( !isInside ) {
		        	double i      = (double) n;
		        	double mi     = (double) MaxIterations;
		        	double cN     = (double) (colorList.size() - 1);
		        	double index  = (int)(i / (mi / cN));
		            double amount = (i - ((mi / cN) * index)) / (mi / cN);
		            col = mergeColor(colorList.get((int)index), colorList.get((int)(index + 1)), amount);
		        }
		        g.setColor(col);
	        	g.drawRect(x, y, 0, 0);
		    }
		}
	}
	
	public void paintComponent(Graphics g) {
		if( fractal_redraw ) {
			fractal_redraw = false;
			fractalCreate();
		}
		/*
		for( int y = 0; y < PHEIGHT; y += 1 ) {
			for( int x = 0; x < PWIDTH; x += 1 ) {
				g.setColor(getPixel(x, y));
				g.drawRect(x, y, 0, 0);
			}
		}*/
		
		g.drawImage(buffer, 0, 0, null);
		
		if( mouseDragging ) {
			int x1, y1, x2, y2;
			x1 = Math.min(mouseDragX, mouseX);
			y1 = (int)Math.min((float)mouseDragY, mouseDragY + ((float)(mouseX - mouseDragX) * ((float)PHEIGHT / (float)PWIDTH)));
			x2 = Math.max(mouseDragX, mouseX);
			y2 = (int)Math.max((float)mouseDragY, mouseDragY + ((float)(mouseX - mouseDragX) * ((float)PHEIGHT / (float)PWIDTH)));
			
			g.setColor(Color.white);
			g.drawRect(x1, y1, x2 - x1, y2 - y1);
			
			if( !mouseDown ) {
				mouseDragging = false;
				
				double tempMinRe = MinRe;
				double tempMaxRe = MaxRe;
				double tempMinIm = MinIm;
				double tempMaxIm = MaxIm;
				
				MinRe = tempMinRe + (x1 * Re_factor);
				MaxRe = tempMinRe + (x2 * Re_factor);
				MinIm = tempMaxIm - (y2 * Im_factor);
				MaxIm = tempMaxIm - (y1 * Im_factor);
				
				Re_factor = (MaxRe - MinRe) / (PWIDTH - 1);
				Im_factor = (MaxIm - MinIm) / (PHEIGHT - 1);
				
				fractal_redraw = true;
			}
		}
	}
}
