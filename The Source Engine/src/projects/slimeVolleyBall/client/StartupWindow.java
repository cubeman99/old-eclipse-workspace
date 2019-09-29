package projects.slimeVolleyBall.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


public class StartupWindow {
	public JFrame frame;
	public JTextField fieldIP;
	public JTextField fieldUsername;
	public JLabel labelIP;
	public JLabel labelUsername;
	public JButton buttonLogin;
	
	
	public StartupWindow() {
		setLookAndFeel();
		
		String fontName = "MS Sans Serif";
		Font fontTitle = new Font(fontName, Font.BOLD, 16);
		Font font1     = new Font(fontName, Font.PLAIN, 14);
		
		// Create Main Components:
		JPanel panel      = new JPanel();
		JPanel inputPanel = new JPanel();
		fieldIP           = new JTextField("66.189.88.77:25565", 20);
//		fieldIP           = new JTextField("66.189.36.38:25565", 20);
		fieldUsername     = new JTextField("User", 20);
		buttonLogin       = new JButton("Login");
		labelIP           = new JLabel("IP Adress:");
		labelUsername     = new JLabel("Username:");
		
		JLabel labelTitle = new JLabel("Slime Volleyball");
		labelTitle.setFont(fontTitle);
		labelIP.setFont(font1);
		labelUsername.setFont(font1);
		buttonLogin.setFont(font1);
		fieldIP.setFont(font1);
		fieldUsername.setFont(font1);
		
		GroupLayout layout = new GroupLayout(inputPanel);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
			layout.createSequentialGroup()
    			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
    				.addComponent(labelIP)
    				.addComponent(labelUsername)
    			)
    			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
    				.addComponent(fieldIP)
    				.addComponent(fieldUsername)
    			)
			);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(labelIP)
					.addComponent(fieldIP)
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(labelUsername)
					.addComponent(fieldUsername)
				)
			);
		
		
		buttonLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{pressLoginButton();}
			});
		fieldUsername.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					pressLoginButton();
				}
			}
			public void keyReleased(KeyEvent arg0) {}
			public void keyTyped(KeyEvent arg0) {}
			});
		
		labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		inputPanel.setLayout(layout);
		inputPanel.add(labelIP);
		inputPanel.add(fieldIP);
		inputPanel.add(labelUsername);
		inputPanel.add(fieldUsername);
		inputPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(labelTitle);
		panel.add(inputPanel);
		panel.add(buttonLogin);
		
		frame = new JFrame("Slime Volleyball Launcher");
		frame.getContentPane().add(panel);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(false);
    	frame.setVisible(true);
    	
    	fieldUsername.requestFocus();
    	
    	// Move the frame to the center of the screen:
    	Dimension screenSize = frame.getToolkit().getScreenSize();
    	frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2), (screenSize.height / 2) - (frame.getHeight() / 2));

	}
	
	private void pressLoginButton() {
		labelIP.setForeground(Color.BLACK);
		labelUsername.setForeground(Color.BLACK);
		
		try {
    		String username      = fieldUsername.getText();
    		String ip            = fieldIP.getText();
    		String serverName    = "";
    		String serverPort    = "";
    		boolean readingName  = true;
    		
    		for (int i = 0; i < ip.length(); i++) {
    			char c = ip.charAt(i);
    			
    			if (c == ':')
    				readingName = false;
    			else if (readingName)
    				serverName += c;
    			else
    				serverPort += c;
    		}
    		
    		int port = Integer.parseInt(serverPort);
    		
    		if (username.trim().length() == 0) {
    			System.out.println("Error, username is bad!");
    			labelUsername.setForeground(Color.RED);
    			return;
    		}
    		
    		ClientConnection.clientName = fieldUsername.getText();
    		ClientConnection.connectTCPIP(serverName, port);
    		
    		frame.setVisible(false);
		}
		catch (NumberFormatException e) {
			labelIP.setForeground(Color.RED);
		}
	}
	

	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
