package client.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import cardgame.gui.ChatPanel;
import cardgame.gui.PlayerListPanel;
import client.ClientConnection;
import client.Timer;


public class Main implements Runnable {
	public static final int SHUT_DOWN_TIME = 1000;
	public static final int FPS = 60;
	public static boolean running = true;
	public static JFrame frame;
	public static JPanel panel;
	public static Keyboard keyboard;
	
	public static Game game;
	public static ChatPanel chatPanel;
	public static PlayerListPanel playerListPanel;
	
	public static Timer shutDownTimer = new Timer();
	
	public Main() {
		
		game            = new Game();
		chatPanel       = new ChatPanel();
		playerListPanel = new PlayerListPanel();
    	keyboard        = new Keyboard();
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		// Player List Panel:
		JPanel jp1 = new JPanel();
		jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));
		jp1.add(playerListPanel);
		
		// Chat Log Panel
		JPanel jp2 = new JPanel();
		jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS));
		jp2.add(chatPanel);

		rightPanel.add(jp1, BorderLayout.NORTH);
		rightPanel.add(jp2, BorderLayout.SOUTH);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setFocusable(true);
		panel.add(rightPanel, BorderLayout.EAST);
		panel.add(game);
		
		frame = new JFrame("Cards Against Humanity - " + ClientConnection.clientName);
		frame.getContentPane().add(panel);
    	frame.addKeyListener(keyboard);
		frame.setPreferredSize(new Dimension(Game.VIEW_WIDTH + 260, Game.VIEW_HEIGHT + 30));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(false);
    	frame.setVisible(true);
    	frame.requestFocus();

    	// Move the frame to the center of the screen:
    	Dimension screenSize = frame.getToolkit().getScreenSize();
    	frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2), (screenSize.height / 2) - (frame.getHeight() / 2));
    	
    	new Thread(this).start();
	}

	public static void stop() {
		running = false;
	}
	
	public static void beginShutDown() {
		System.out.println("Terminating Client...");
		frame.setVisible(false);
		shutDownTimer.reset();
		shutDownTimer.start();
	}
	
	public void run() {
		// Remember the starting time
    	long time = System.currentTimeMillis();
		running = true;
		
		while (running) {
			if (shutDownTimer.running()) {
				if (shutDownTimer.pastTime(SHUT_DOWN_TIME)) {
					Main.stop();
				}
			}
			else {
    			Keyboard.update();
    			Mouse.update();
    			game.update();
    			game.repaint();
    			playerListPanel.repaint();
			}
			
			try {
				time += 1000 / FPS;
                Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		System.exit(0);
	}
}
