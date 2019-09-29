package game;

import java.awt.Color;
import java.util.HashMap;
import game.network.Client;
import game.network.Message;
import game.network.MessageHandler;
import game.network.Server;
import game.network.User;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Rectangle;
import cmg.math.geometry.Vector;

public class SlimeBallClient extends SlimeBall implements MessageHandler {
	private static final int PLAYING        = 0;
	private static final int CHOOSING_TEAM  = 1;
	private static final int CHOOSING_COLOR = 2;
	private Client client;
	private HashMap<Integer, Slime> slimeUsers;
	private int gameState;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public SlimeBallClient(Client client) {
		super();
		this.client = client;
		slimeUsers  = new HashMap<Integer, Slime>();
		
		slimes.add(new Slime(this, "David", teams[1], Color.GREEN));
		player = slimes.get(0);
		
		client.setMessageHandler(this);
		
		gameState = CHOOSING_TEAM;
	}

	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update() {
		if (gameState == PLAYING) {
			player.update();
			
			Message msg = new Message(Settings.MSG_PLAYER_UPDATE, 8 * 4);
			msg.writeDouble(player.getPosition().x);
			msg.writeDouble(player.getPosition().y);
			msg.writeDouble(player.getVelocity().x);
			msg.writeDouble(player.getVelocity().y);
			client.sendMessage(msg);
		}
		else if (gameState == CHOOSING_COLOR) {
			
		}
		else if (gameState == CHOOSING_TEAM) {
			
		}
	}

	@Override
	public void handleMessage(Message msg) {
		if (msg.getType() == Settings.MSG_ADD_PLAYER) {
			int id      = msg.readInt();
			String name = msg.readString();
			int team    = msg.readInt();
			int rgb     = msg.readInt();
			Color color = new Color(rgb);
			
			Slime slime = new Slime(this, name, teams[team], color);
			slimes.add(slime);
			slimeUsers.put(id, slime);
			
			System.out.println("ADD SLIME: " + slime);
		}
		else if (msg.getType() == Settings.MSG_REMOVE_PLAYER) {
			int id =  msg.readInt();
			slimes.remove(slimeUsers.get(id));
		}
		else if (msg.getType() == Settings.MSG_GAME_UPDATE) {
			ball.getPosition().set(msg.readDouble(), msg.readDouble());
			
			while (msg.hasNext()) {
				int id      = msg.readInt();
				Slime slime = slimeUsers.get(id);
				Vector pos  = new Vector(msg.readDouble(), msg.readDouble());
				if (slime != player && slime != null)
					slime.getPosition().set(pos);
			}
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		
		if (gameState == CHOOSING_TEAM) {
			for (Team team : teams) {
    			Rectangle box = new Rectangle(0, 0, 248, 56);
    			Vector center = team.getViewRect().getCenter();
    			box.centerAt(center);
    
    			//setFont(12);
    			Draw.setColor(Color.WHITE);
    			Draw.fill(box);
    			
    			Draw.setColor(Color.BLUE);
    			Draw.drawString("Click here to join this team!", center, Draw.CENTER, Draw.MIDDLE);
    			Vector ms = Mouse.getVector();
    			
    			if (box.contains(ms)) {
    				if (Mouse.left.pressed()) {
    					player.setTeam(team);
    					gameState = PLAYING;
    					
    					Message msg = new Message(Settings.MSG_PLAYER_JOIN);
    					msg.writeString("Username");
    					msg.writeInt(player.getTeamIndex());
    					msg.writeInt(player.getColor().getRGB());
    					client.sendMessage(msg);
    				}
    			}
			}
		}
	}
}
