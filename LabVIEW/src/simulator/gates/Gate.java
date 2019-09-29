package simulator.gates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import common.Draw;
import common.Point;
import main.ImageLoader;
import simulator.IOPoint;
import simulator.Selectable;
import simulator.datatypes.Type;

public abstract class Gate extends Selectable {
	public Point size;
	public Image image;
	public ArrayList<IOPoint> inputs;
	public ArrayList<IOPoint> outputs;
	public Point pos;
	
	public Gate(int x, int y) {
		this.pos     = new Point(x, y);
		this.inputs  = new ArrayList<IOPoint>();
		this.outputs = new ArrayList<IOPoint>();
		createImage(getImage());
	}
	
	private void createImage(Image image) {
		this.image  = image;
		this.size   = new Point(image.getWidth(null), image.getHeight(null));
	}
	
	protected void addInputPoint(int x, int y, boolean horizontal, Type dataType) {
		inputs.add(new IOPoint(this, x, y, horizontal, Type.BOOLEAN));
	}
	
	protected void addOutputPoint(int x, int y, boolean horizontal, Type dataType) {
		outputs.add(new IOPoint(this, x, y, horizontal, dataType));
	}
	
	public Image getImage() {
		return ImageLoader.getImage(getImageName());
	}
	
	public Rectangle getRect() {
		return new Rectangle(pos.x, pos.y, size.x, size.y);
	}
	
	@Override
	public Point getPosition() {
		return pos;
	}
	
	public Point getCenter() {
		return new Point(pos.x / 2, pos.y / 2);
	}

	@Override
	public Point getSize() {
		return size;
	}

	////////////////////////////////////
	//  DATA TYPE RETRIEVAL METHODS:  //
	////////////////////////////////////
	protected boolean getBool(int index) {
		if (inputs.size() > index)
			return inputs.get(index).dataType.getBoolean();
		return false;
	}
	
	protected byte getByte(int index) {
		if (inputs.size() > index)
			return inputs.get(index).dataType.getByte();
		return 0;
	}
	
	protected int getInteger(int index) {
		if (inputs.size() > index)
			return inputs.get(index).dataType.getInteger();
		return 0;
	}
	protected int getInt(int index)
		{return getInteger(index);}

	
	protected double getDouble(int index) {
		if (inputs.size() > index)
			return inputs.get(index).dataType.getDouble();
		return 0.0d;
	}
	protected double getDbl(int index)
		{return getDouble(index);}

	
	protected String getString(int index) {
		if (inputs.size() > index)
			return inputs.get(index).dataType.getString();
		return "";
	}
	protected String getSt(int index)
		{return getString(index);}
	
	////////////////////////////////////
    //   DATA TYPE SETTING METHODS:   //
    ////////////////////////////////////
    protected void setBoolean(int index, boolean value) {
    	if (inputs.size() > index)
    		inputs.get(index).dataType.setBoolean(value);
    }
    protected void setBool(int index, boolean value)
		{setBoolean(index, value);}

    protected void setByte(int index, byte value) {
    	if (inputs.size() > index)
    		inputs.get(index).dataType.setByte(value);
    }

    protected void setInteger(int index, int value) {
    	if (inputs.size() > index)
    		inputs.get(index).dataType.setInteger(value);
    }
    protected void setInt(int index, int value)
    	{setInteger(index, value);}


    protected void setDouble(int index, double value) {
    	if (inputs.size() > index)
    		inputs.get(index).dataType.setDouble(value);
    }
    protected void setDbl(int index, double value)
    	{setDouble(index, value);}


    protected void setString(int index, String value) {
    	if (inputs.size() > index)
    		inputs.get(index).dataType.setString(value);
    }
    protected void setStr(int index, String value)
    	{setString(index, value);}
    ////////////////////////////////////
	
   // public static String getImageName() {System.out.println("Default getName");return "";}
    
    public abstract String getImageName();
	public abstract void compute();
	
	
	public void drawSelectionBox(Graphics g, Color color) {
		Rectangle r = getRect();
		r.grow(4, 4);
		g.setColor(color);
		g.drawRect(r.x, r.y, r.width, r.height);
		
		for (IOPoint iop : inputs) {
			Draw.draw(iop);
		}
		for (IOPoint iop : outputs) {
			Draw.draw(iop);
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, pos.x, pos.y, null);
	}
}
