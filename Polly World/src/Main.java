import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class Main implements Runnable {
	public static final int FPS = 60;
	private static Thread thread;
	
	public static float fpsCurrent = 0;
	public static float fpsAverage = 0;
	
	public static PollyWorld game;
	public static boolean running = true;
	
	public static long step = 0;
	
	public static JFrame frame;
	
	public Main() {
		frame = new JFrame("Polly World Simulation - By David Jordan");
		
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		
		game = new PollyWorld();
		c.add(new JScrollPane(game));
		
		
		frame.setPreferredSize(new Dimension(580, 580));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(true);
    	frame.setVisible(true);
    	
    	new Thread(this).start();
	}
	
	public static void main(String [] args) {
    	new Main();
	}
	
	public static void gameEnd() {
		running = false;
	}
	
	public void run() {
		// Remember the starting time
    	long tm = System.currentTimeMillis();
    	
		running = true;
		
		long time1 = tm;
		long time2;
		
		// Keep a running average of fps values
		int fpsValuesCount = 20;
		float[] fpsValues = new float[fpsValuesCount];
		float fpsValuesTotal = 0.0f;
		int fpsValuesCountTotal = 0;
		
		for( int i = 0; i < fpsValuesCount; i++ )
			fpsValues[i] = 0.0f;
		
		while( running ) {
			game.update();
			
			try {
				tm += 1000 / FPS;
				time2 = System.currentTimeMillis();
				
				// Shift fps values up
				for( int i = fpsValuesCount - 1; i > 0; i-- )
					fpsValues[i] = fpsValues[i - 1];
				
				fpsValues[0] = 1.0f / ((float)(time2 - time1) / 1000.0f);
				fpsValuesTotal += fpsValues[0];
				fpsValuesCountTotal += 1;
				
				// Get the average
				float avg = 0;
				for( int i = 0; i < fpsValuesCount; i++ )
					avg += fpsValues[i];
				avg /= fpsValuesCount;
				fpsCurrent = avg;
				
				fpsAverage = fpsValuesTotal / (float)fpsValuesCountTotal;
				
				time1 = time2;
                Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			}
			catch(InterruptedException e) {}
			
			step += 1;
		}
		System.exit(0);
	}
}
