
import java.util.*;

public class Keys {
	public ArrayList<Key> keyArray = new ArrayList<Key>();

    public Key up				= new Key(this);
    public Key down				= new Key(this);
    public Key left				= new Key(this);
    public Key right			= new Key(this);
    public Key reload			= new Key(this);
    public Key use				= new Key(this);
    public Key switchWeapons	= new Key(this);

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
