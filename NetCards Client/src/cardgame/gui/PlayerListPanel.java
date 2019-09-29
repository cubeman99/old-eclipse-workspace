package cardgame.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import client.main.ImageLoader;
import cardgame.CardGame;

public class PlayerListPanel extends JComponent {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH  = 252;
	public static final int HEIGHT = 320;
	public static final int LIST_SPACING = 32;
	
	public Image buffer;
	
	public PlayerListPanel() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
	}
	
	
	private void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		for (int i = 0; i < CardGame.playerHolders.size(); i++) {
			PlayerHolder player = CardGame.playerHolders.get(i);
			int dx              = 0;
			int dy              = (i * LIST_SPACING);
			String text         = player.getName();
			g.setColor(GUILayout.COLOR_PLAYERLIST_BACKGROUND);
			
			if (CardGame.judgeName.equals(player.getName())) {
				g.setColor(GUILayout.COLOR_BACKGROUND_JUDGE);
				text += " - Judge";
			}
			else if (player.getReady()) {
				g.setColor(GUILayout.COLOR_BACKGROUND_READY);
			}
			
			g.fillRect(dx, dy, WIDTH, LIST_SPACING);
			
			g.setColor(Color.BLACK);
			g.drawString(text, dx + 40, dy + (LIST_SPACING / 2) + 6);
			
			String scoreStr = "";
			if (player.getScore() < 10)
				scoreStr += "0";
			scoreStr += player.getScore();
			g.drawString(scoreStr, dx + 9, dy + (LIST_SPACING / 2) + 6);
			
			g.drawImage(ImageLoader.getImage("playerHolderSlot"), dx, dy, null);
//			g.drawRect(dx, dy, WIDTH - 1, LIST_SPACING - 1);
//			g.drawLine(dx + 31, dy, dx + 31, dy + LIST_SPACING - 1);
		}
	}
	
	public void paintComponent(Graphics g) {
		Graphics bufferGraphics = buffer.getGraphics();
		render(bufferGraphics);
		bufferGraphics.dispose();
		g.drawImage(buffer, 0, 0, null);
	}
}
