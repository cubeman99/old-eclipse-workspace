package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import cmg.main.Keyboard;
import cmg.math.geometry.Vector;
import game.network.Message;
import game.network.Server;
import game.network.User;
import game.network.UserListener;

public class SlimeBallServer extends SlimeBall implements UserListener {
	private Server server;
	private HashMap<User, Slime> slimeUsers;

	
	
	// ================== CONSTRUCTORS ================== //

	public SlimeBallServer(Server server) {
		super();
		this.server = server;
		server.setUserListener(this);
		slimeUsers = new HashMap<User, Slime>();
	}
	
	public Slime getSlime(User user) {
		return slimeUsers.get(user);
	}

	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update() {
		ball.update();
		
		if (Keyboard.restart.pressed()) {
			ball.getPosition().set(Settings.VIEW_WIDTH / 4.0, Settings.FLOOR_Y - Settings.BALL_SERVE_HEIGHT);
			ball.getVelocity().zero();
		}
		
		Message msg = new Message(Settings.MSG_GAME_UPDATE);
		msg.writeDouble(ball.getPosition().x);
		msg.writeDouble(ball.getPosition().y);
		
		for (User user : server.getUsers()) {
			Slime slime = getSlime(user);
			
			if (slime != null) {
    			msg.writeInt(user.getID());
    			msg.writeDouble(slime.getPosition().x);
    			msg.writeDouble(slime.getPosition().y);
			}
		}
		
		server.broadcastMessage(msg);
	}

	@Override
	public void onJoin(User user) {
		Slime slime = new Slime(this, "David", teams[0], Color.RED);
		slimes.add(slime);
		slimeUsers.put(user, slime);
	}

	@Override
	public void onLeave(User user) {
		slimes.remove(getSlime(user));
		slimeUsers.remove(user);
		
		Message msg = new Message(Settings.MSG_REMOVE_PLAYER);
		msg.writeInt(user.getID());
		server.broadcastMessage(msg);
	}

	@Override
	public void handleMessage(Message msg, User user) {
		
		if (msg.getType() == Settings.MSG_PLAYER_JOIN) {
			Slime slime = getSlime(user);
			slime.setName(msg.readString());
			slime.setTeam(teams[msg.readInt()]);
			slime.setColor(new Color(msg.readInt()));

			
			Message msgAdd = new Message(Settings.MSG_ADD_PLAYER);
			msgAdd.writeInt(user.getID());
			msgAdd.writeString(slime.getName());
			msgAdd.writeInt(slime.getTeamIndex());
			msgAdd.writeInt(slime.getColor().getRGB());
			
			for (User u : server.getUsers()) {
				if (u != user) {
					u.sendMessage(msgAdd);
					
					Slime addSlime = getSlime(user);
					Message msgAdd2 = new Message(Settings.MSG_ADD_PLAYER);
					msgAdd2.writeInt(u.getID());
					msgAdd2.writeString(addSlime.getName());
					msgAdd2.writeInt(addSlime.getTeamIndex());
					msgAdd2.writeInt(addSlime.getColor().getRGB());
					user.sendMessage(msgAdd2);
				}
			}
		}
		if (msg.getType() == Settings.MSG_PLAYER_UPDATE) {
			Slime slime = getSlime(user);
			slime.getPosition().set(msg.readDouble(), msg.readDouble());
			slime.getVelocity().set(msg.readDouble(), msg.readDouble());
		}
	}
}
