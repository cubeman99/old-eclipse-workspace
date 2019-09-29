package cardgame.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import client.ClientConnection;
import client.Message;

public class ChatPanel extends JComponent {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH  = 250;
	public static final int HEIGHT = 380;
	
	private JPanel inputPanel;
	private JScrollPane scrollPane;
	private JTextArea chatTextArea;
	private JTextField chatInputBox;
	private JButton chatInputButton;
	
	public ChatPanel() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		inputPanel = new JPanel();
		inputPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		chatTextArea = new JTextArea();
		chatTextArea.setLayout(new BorderLayout());
		chatTextArea.setEditable(false);
		chatTextArea.setLineWrap(true);
		chatTextArea.setWrapStyleWord(true);
		scrollPane = new JScrollPane(chatTextArea);
		
		chatTextArea.setFont(new Font("MS Sans Serif", Font.PLAIN, 12));
		
		chatInputBox = new JTextField(20);
		chatInputButton = new JButton("Send");
		inputPanel.add(chatInputBox);
		inputPanel.add(chatInputButton);

		class ChatInputListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				sendChatMessage();
			}
		}
		class ChatKeyListener implements KeyListener {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					sendChatMessage();
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		}

		ActionListener aListener = new ChatInputListener();
		KeyListener kListener    = new ChatKeyListener();
		chatInputButton.addActionListener(aListener);
		chatInputBox.addKeyListener(kListener);
		
		this.add(scrollPane);
		this.add(inputPanel, BorderLayout.SOUTH);
	}
	
	private void sendChatMessage() {
		if (!chatInputBox.getText().equals("")) {
    		ClientConnection.sendMessage(new Message("chatmessage", chatInputBox.getText()));
    		chatInputBox.setText("");
		}
	}
	
	public void printChatMessage(String text) {
		chatTextArea.append(text + "\n");
		chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
	}
}
