package simulator;

import common.Point;
import simulator.datatypes.DataType;
import simulator.datatypes.Type;
import simulator.datatypes.TypeBoolean;
import simulator.gates.Gate;
import simulator.wires.Wire;

public class IOPoint {
	public Point offset;
	public Type type;
	public DataType dataType;
	public Wire wireLink;
	public Gate gateLink;
	public boolean horizontal; // true = horizontal, false = vertical
	
	public IOPoint(Gate gateLink, int x, int y, boolean horizontal, Type type) {
		this.gateLink   = gateLink;
		this.offset     = new Point(x, y);
		this.type       = type;
		this.horizontal = horizontal;
		
		if (type == Type.BOOLEAN)
			this.dataType = new TypeBoolean();
		else if (type == Type.BYTE)
			;//this.dataType = new TypeByte();
		else if (type == Type.INTEGER)
			;//this.dataType = new TypeInteger();
		else if (type == Type.DOUBLE)
			;//this.dataType = new TypeDouble();
		else if (type == Type.STRING)
			;//this.dataType = new TypeString();
	}
	
	public Point getAbsPoint() {
		return new Point(gateLink.pos.x + offset.x, gateLink.pos.y + offset.y);
	}
	
	public void linkWire(Wire link) {
		this.wireLink = link;
	}
	
	public void unlinkWire(Wire link) {
		this.wireLink = null;
	}
	
	public int absX() {
		return (gateLink.pos.x + offset.x);
	}
	
	public int absY() {
		return (gateLink.pos.y + offset.y);
	}
}
