package simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import common.Draw;
import common.GMath;
import common.Point;
import main.ImageLoader;
import main.Keyboard;
import main.Mouse;
import simulator.gates.*;
import simulator.wires.Wire;

public class SimulatorInterface {
	public WireBoard wireBoard;
	
	public ArrayList<Class<? extends Gate>> toolbarGates;
	public int toolbarIndex;
	public Image toolbarImage   = null;
	public String toolbarName   = "";
	
	public ArrayList<Gate> gateList;
	public ArrayList<Wire> wireList;
	public ArrayList<Selectable> selection;
	
	public Gate selectedGate    = null;
	
	public Point dragOffset     = new Point();
	public Point dragStartPos   = new Point();
	public Gate dragGate        = null;
	public boolean draggingWire = false;
	public Wire dragWire        = null;
	public int dragWireJoints   = 0;
	public boolean toolWire     = false;
	public IOPoint highlightedIOPoint = null;
	
	
	public SimulatorInterface() {
		wireBoard = new WireBoard();
		gateList  = new ArrayList<Gate>();
		wireList  = new ArrayList<Wire>();
		selection = new ArrayList<Selectable>();
		
		toolbarGates = new ArrayList<Class<? extends Gate>>();
		toolbarGates.add(GateOr.class);
		toolbarGates.add(GateAnd.class);
		toolbarGates.add(GateNot.class);
		toolbarName  = getToolbarGate().getName();
		toolbarImage = getToolbarImage();
		
		highlightedIOPoint = null;
	}
	
	public void update() {
		Point ms = Mouse.position();
		
		if (Mouse.wheelUp()) {
			toolbarIndex++;
			if (toolbarIndex >= toolbarGates.size())
				toolbarIndex = 0;
			toolbarName  = getToolbarGate().getName();
			toolbarImage = getToolbarImage();
		}
		if (Mouse.wheelDown()) {
			toolbarIndex--;
			if (toolbarIndex < 0)
				toolbarIndex = toolbarGates.size() - 1;
			toolbarName  = getToolbarGate().getName();
			toolbarImage = getToolbarImage();
		}
		
		if (Keyboard.space.pressed())
			toolWire = !toolWire;

		if (toolWire) {
			highlightedIOPoint = null;
			double minDist = -1;
			for (Gate gate : gateList) {
				for (IOPoint iop : gate.inputs) {
					double d = GMath.distance(new Point(iop.absX(), iop.absY()), ms);
					if (iop.wireLink == null && d < 4 && (d < minDist || minDist < 0)) {
						minDist = d;
						highlightedIOPoint = iop;
					}
				}
				for (IOPoint iop : gate.outputs) {
					double d = GMath.distance(new Point(iop.absX(), iop.absY()), ms);
					if (iop.wireLink == null && d < 4 && (d < minDist || minDist < 0)) {
						minDist = d;
						highlightedIOPoint = iop;
					}
				}
			}
			if (draggingWire) {
				boolean place = Keyboard.enter.pressed();
				dragWire.removeJoints(dragWireJoints);
				
				Point p   = ms;
				int align = Wire.NONE;
				if (highlightedIOPoint != null) {
					p = highlightedIOPoint.getAbsPoint();
					align = highlightedIOPoint.horizontal ? Wire.HORIZONTAL : Wire.VERTICAL;
					//dragWire.setLastJoint(highlightedIOPoint.getAbsPoint());
    				dragWire.output = highlightedIOPoint;
    			}
				else {
					//dragWire.setLastJoint(ms);
					dragWire.output = null;
				}
				
				dragWire.connectToJoint(p, align);
				
				if (Mouse.left.pressed()) {
					if (highlightedIOPoint != null)
						place = true;
					else {
						dragWireJoints += 2;
					}
				}
				
				
				if (place) {
					draggingWire = false;
					wireList.add(dragWire);
					dragWire = null;
				}
				
			}
			else {
				if (Mouse.left.pressed()) {
    				dragStartPos   = ms;
    				draggingWire   = true;
    				dragWire       = new Wire();
    				dragWireJoints = 1;
    				if (highlightedIOPoint != null) {
    					dragStartPos = highlightedIOPoint.getAbsPoint();
    					dragWire.input = highlightedIOPoint;
    				}
    				dragWire.addJoint(dragStartPos);
				}
			}
			
		}
		else {
    		if (dragGate != null) {
    			dragGate.pos.set(ms.minus(dragOffset));
    			if (!Mouse.left.down()) {
    				// Release Gate
    				dragGate = null;
    			}
    		}
    		if (Mouse.left.pressed()) {
    			if (Keyboard.control.down()) {
    				Gate newGate = getNewToolbarInstance();
    				if (newGate != null) {
    					newGate.pos = Mouse.position();
    					gateList.add(newGate);
    				}
    			}
    			else {
    				selectedGate = null;
    				for (Gate gate : gateList) {
    					if (Mouse.inArea(gate.getRect())) {
    						dragGate = gate;
    						if (Keyboard.alt.down()) {
    							Gate newGate = getNewToolbarInstance();
    							if (newGate != null) {
    		    					newGate.pos.set(gate.pos);
    								gateList.add(newGate);
    								dragGate = newGate;
    							}
    						}
    						dragStartPos = dragGate.pos;
    						dragOffset   = new Point(ms.x - dragGate.pos.x, ms.y - dragGate.pos.y);
    						break;
    					}
    				}
    			}
    		}
		}
		
		
		// Update wires:
		for (Wire wire : wireList) {
			wire.update();
		}
	}
	
	public Gate getNewToolbarInstance() {
		Gate newGate = null;
		try {
			newGate = getToolbarGate().newInstance();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return newGate;
	}
	
	public Class<? extends Gate> getToolbarGate() {
		return toolbarGates.get(toolbarIndex);
	}
	
	public Image getToolbarImage() {
		Point ms = Mouse.position();
		String imageName = "ERROR";
		
		
		try {
			//Class<? extends Gate> c = getToolbarGate();
			//Method meth = getToolbarGate().getMethod("getImageName", null);
			imageName = (String) getToolbarGate().getField("IMAGE_NAME").get(null);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return ImageLoader.getImage(imageName);
	}
	
	public void draw(Graphics g) {
		Point ms = Mouse.position();
		Draw.setGraphics(g);
		
		if (toolWire) {
			g.drawString("Wire", 16, 16);
		}
		else {
    		g.drawString(toolbarName, 16, 16);
    		if (toolbarImage != null)
    			g.drawImage(toolbarImage, 16, 24, null);
		}
		
		
		if (dragWire != null)
			dragWire.draw(g);
		
		for (Wire wire : wireList) {
			wire.draw(g);
		}
		
		for (Gate gate : gateList) {
			gate.draw(g);
			if (!toolWire) {
    			if (Mouse.inArea(gate.getRect())) {
    				//gate.drawSelectionBox(g, Color.LIGHT_GRAY);
    			}
    			if (selectedGate == gate) {
    				gate.drawSelectionBox(g, Color.GREEN);
    			}
			}
		}
		
		if (highlightedIOPoint != null) {
			Draw.draw(highlightedIOPoint);
		}
	}
}
