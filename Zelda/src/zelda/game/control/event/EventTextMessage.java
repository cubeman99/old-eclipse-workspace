package zelda.game.control.event;

import zelda.common.graphics.Draw;
import zelda.game.control.text.Message;
import zelda.game.control.text.TextReader;


public class EventTextMessage extends Event {
	private Message message;
	private TextReader textReader;

	public EventTextMessage(String text) {
		message = new Message(text);
	}

	public EventTextMessage(Message msg) {
		message = msg;
	}

	public void setMessage(Message msg) {
		message = msg;
	}

	public void setMessage(String txt) {
		message = new Message(txt);
	}

	@Override
	public void begin() {
		super.begin();
		textReader = new TextReader(game);
		textReader.readMessage(message, game.getPlayer());
	}

	@Override
	public void update() {
		super.update();
		textReader.update();
		if (!textReader.isReading())
			end();
	}

	@Override
	public void draw() {
		Draw.setViewPosition(0, -16);
		textReader.draw();
	}
}
