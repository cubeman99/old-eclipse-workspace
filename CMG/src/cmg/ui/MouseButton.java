package cmg.ui;

public class MouseButton {
	protected int buttonIndex;
	protected boolean rawDown;
	protected boolean down;
	protected boolean downPrev;
	protected boolean rawClicked;
	protected boolean clicked;

	public MouseButton(int buttonIndex) {
		this.buttonIndex = buttonIndex;
		this.rawDown     = false;
		this.down        = false;
		this.downPrev    = false;
		this.rawClicked  = false;
		this.clicked     = false;
	}

	/** Check if the mouse button is down. **/
	public boolean down() {
		return down;
	}

	/** Check if the mouse button was pressed. **/
	public boolean pressed() {
		return (down && !downPrev);
	}

	/** Check if the mouse button was released. **/
	public boolean released() {
		return (!down && downPrev);
	}

	/**
	 * Check if the mouse button was clicked (pressed & released without
	 * movement).
	 **/
	public boolean clicked() {
		return clicked;
	}
}
