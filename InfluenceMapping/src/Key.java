
public final class Key {
    public boolean isDown = false;
    public boolean isPressed = false;
    public boolean isReleased = false;

    public Key(Keys k) {
    	k.keyArray.add(this);
    }

    public void tick() {
        isPressed	= false;
        isReleased	= false;
    }

    public void press() {
    	isDown		= true;
    	isPressed	= true;
    }

    public void release() {
    	isDown		= false;
    	isReleased	= true;
    }

    public void clear() {
        isDown		= false;
        isPressed	= false;
        isReleased	= false;
    }
}