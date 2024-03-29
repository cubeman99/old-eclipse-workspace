
import java.awt.event.KeyEvent;
import java.util.*;

public class Keys {
	public ArrayList<Key> keyArray = new ArrayList<Key>();

    public Key up				= new Key(this);
    public Key down				= new Key(this);
    public Key left				= new Key(this);
    public Key right			= new Key(this);

    public Key flipR			= new Key(this);
    public Key flipL			= new Key(this);
    public Key instant			= new Key(this);
   
    public Key restart			= new Key(this);
    public Key escape			= new Key(this);

    public void tick() {
        for (Key key : keyArray)
            key.tick();
    }

    public void clear() {
        for (Key key : keyArray)
            key.clear();
    }

    public List<Key> getArray() {
        return keyArray;
    }
}
