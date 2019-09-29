package game.network;

public interface UserListener {
	public void onJoin(User user);
	public void onLeave(User user);
	public void handleMessage(Message msg, User user);
}
