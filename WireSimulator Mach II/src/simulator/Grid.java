package simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import main.GMath;
import main.Keyboard;
import main.Mouse;

public class Grid {
	public static final int TILE_SIZE = 16;
	public int width;
	public int height;
	public GridObject[][] tiles;
	public boolean tickCheck = false;
	
	public boolean draggingWire   = false;
	public Point dragWirePosition = new Point(0, 0);
	
	public ArrayList<WireGroup> wireGroups;
	public ArrayList<LogicGate> logicGates;
	public ArrayList<Class> toolbarTypes;
	public int toolbarIndex;
	
	public ArrayList<ArrayList<LogicGate>> logicGateSequence;
	public ArrayList<PowerSource> powerSources;
	public ArrayList<WireGroup> sourceWires;
	
	public boolean running = false;
	
	public Grid(int width, int height) {
		this.width  = width;
		this.height = height;
		
		tiles             = new GridObject[width][height];
		wireGroups        = new ArrayList<WireGroup>();
		logicGates        = new ArrayList<LogicGate>();
		toolbarTypes      = new ArrayList<Class>();
		sourceWires       = new ArrayList<WireGroup>();
		powerSources      = new ArrayList<PowerSource>();
		logicGateSequence = new ArrayList<ArrayList<LogicGate>>();
		toolbarIndex      = 0;
		
		toolbarAddType(GateNot.class);
		toolbarAddType(GateAnd.class);
		toolbarAddType(GateOr.class);
		toolbarAddType(SourceLever.class);
		toolbarAddType(SourceTrue.class);
		toolbarAddType(SourceFalse.class);
		toolbarAddType(SourceExtender.class);
	}
	
	public void toolbarAddType(Class type) {
		toolbarTypes.add(type);
	}
	
	public GridObject getNewToolbarObject() {
		GridObject object = null;
		
		try {
			object = (GridObject) toolbarTypes.get(toolbarIndex).newInstance();
		}
		catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return object;
	}
	
	public void clearGrid() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = new GridObject(x, y);
			}
		}
	}
	
	public Point getBoundedMouseTilePos() {
		int x = (int) (Mouse.x() / TILE_SIZE);
		int y = (int) (Mouse.y() / TILE_SIZE);
		return new Point(Math.max(Math.min(x, width - 1), 0), Math.max(Math.min(y, height - 1), 0));
	}
	
	public Point getMouseTilePos() {
		int x = (int) (Mouse.x() / TILE_SIZE);
		int y = (int) (Mouse.y() / TILE_SIZE);
		return new Point(x, y);
	}
	
	public boolean inBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}
	
	public void deleteTile(int x, int y) {
		tiles[x][y] = new GridObject(x, y);
	}

	public void dragWireStart(Point p) {
		// Start Dragging wire:
		dragWirePosition = new Point(p);
		draggingWire = true;
	}
	
	public LogicGate getLogicGateTile(Point p) {
		return getLogicGateTile(p.x, p.y);
	}
	public LogicGate getLogicGateTile(int x, int y) {
		if (!inBounds(x, y))
			return null;
		if (tiles[x][y] instanceof LogicGate) {
			return (LogicGate) tiles[x][y];
		}
		return null;
	}
	
	public PowerSource getSourceTile(Point p) {
		return getSourceTile(p.x, p.y);
	}
	public PowerSource getSourceTile(int x, int y) {
		if (!inBounds(x, y))
			return null;
		if (tiles[x][y] instanceof PowerSource) {
			return (PowerSource) tiles[x][y];
		}
		return null;
	}
	
	public Wire getWireTile(Point p) {
		return getWireTile(p.x, p.y);
	}
	public Wire getWireTile(int x, int y) {
		if (!inBounds(x, y))
			return null;
		if (tiles[x][y] instanceof Wire) {
			return (Wire) tiles[x][y];
		}
		return null;
	}
	
	public void dragWirePlace(Point p) {
		// Place a wire
		draggingWire = false;
		
		int dir = (int) GMath.dirSimp(Math.round(GMath.direction(dragWirePosition, p) / 90)) * 90;
		dir = (int) GMath.dirSimp(360 - dir);
		int len = (int) GMath.distance(dragWirePosition, p);
		
		for (int i = 0; i <= len; i++) {
			int x = (int) (dragWirePosition.x + Math.round(GMath.lenDirX(i, dir)));
			int y = (int) (dragWirePosition.y - Math.round(GMath.lenDirY(i, dir)));
			if (!inBounds(x, y))
				break;
			
			boolean[] dirs = new boolean[4];
			for (int j = 0; j < 4; j++)
				dirs[j] = false;
			
			if(i < len)
				dirs[dir / 90] = true;
            if(i > 0)
            	dirs[(int) (GMath.dirSimp(dir + 180) / 90)] = true;
            
			if (tiles[x][y] instanceof Wire) {
				// Change an Existing Wire
				Wire wire = (Wire) tiles[x][y];
				for (int j = 0; j < 4; j++)
					wire.directions[j] = wire.directions[j] || dirs[j];
			}
			else {
				// Create a new Wire
				tiles[x][y] = new Wire(x, y, dirs);
			}
		}
	}
	
	private void fillWire(Point p) {
		ArrayList<Point> nodes = new ArrayList<Point>();
		Point pos = new Point(p);
		Wire test = getWireTile(p);
		if (test == null)
			return;
		if (test.wireGroup != null)
			return;
		
		WireGroup group = new WireGroup();
		test.wireGroup = group;
		group.addTile(pos);
		wireGroups.add(group);
		
		while (true) {
			Point nextPos = new Point(pos);
			boolean expanded = false;
			for (int i = 0; i < 4; i++) {
				Point checkPos = GMath.add(pos, GMath.dirGet(i));
				
				// Check for Connecting Wires:
				Wire checkWire = getWireTile(checkPos);
				if (checkWire != null) {
					int otheri = i + 2;
					if (otheri >= 4)
						otheri -= 4;
					if (checkWire.directions[otheri] && checkWire.wireGroup == null) {
						if (expanded) {
							nodes.add(checkPos);
						}
						else {
							nextPos = new Point(checkPos);
						}
						expanded = true;
						group.addTile(checkPos);
						checkWire.wireGroup = group;
					}
				}
				
				
				LogicGate gate = getLogicGateTile(checkPos);
				if (gate != null) {
					
				}
				
				// Check for Connecting Power Sources:
				PowerSource source = getSourceTile(checkPos);
				if (source != null) {
					group.addSource(source);
				}
			}

			// Check for Connecting Logic Gates:
			for (LogicGate gate : logicGates) {
				LogicConnectionPoint lcp1 = gate.findInputPoint(pos);
				if (lcp1 != null) {
					lcp1.linkWire(group);
					group.addOutput(lcp1);
				}
				LogicConnectionPoint lcp2 = gate.findOutputPoint(pos);
				if (lcp2 != null) {
					lcp2.linkWire(group);
				}
			}
			
			
			
			if (!expanded && nodes.size() > 0) {
				nextPos = nodes.get(nodes.size() - 1);
				nodes.remove(nodes.size() - 1);
			}
			if (nextPos.x == pos.x && nextPos.y == pos.y)
				break;
			
			pos = nextPos;
		}
	}
	
	private void resetWireGroups() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Wire wire = getWireTile(x, y);
				if (wire != null) {
					wire.wireGroup = null;
				}
			}
		}
		logicGates.clear();
		wireGroups.clear();
	}
	
	private void groupWires() {
		// First, reset all wire groups
		resetWireGroups();
		
		// Second, Create a list of all Logic Gates
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Point pos = new Point(x, y);
				LogicGate gate = getLogicGateTile(pos);
				if (gate != null) {
					logicGates.add(gate);
				}
			}
		}
		
		// Finally, group/link wires up
		boolean done = false;
		while (!done) {
			done = true;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					Point point = new Point(x, y);
					Wire wire = getWireTile(point);
					if (wire != null) {
						// Check for linked wire groups
						if (wire.wireGroup == null) {
							fillWire(point);
							done = false;
						}
					}
				}
			}
		}
	}
	
	private void fillSourceWires() {
		sourceWires.clear();
		for (WireGroup group : wireGroups) {
			if (group.hasPowerSource()) {
				sourceWires.add(group);
			}
		}
	}
	
	private ArrayList<LogicGate> compileFromWireGroup(WireGroup group, int compileIndex) {
		ArrayList<LogicGate> gateList = new ArrayList<LogicGate>();
		
//		System.out.println("compiling from wire... " + group.getOutputs().size());
		
		// Cycle through this wire group's outputs:
		for (LogicConnectionPoint wireOutput : group.getOutputs()) {
			LogicGate gate = wireOutput.getLogicGate();
			boolean gateIsReady = true;
			
			// Check if this gate has familiar inputs
//			System.out.println(compileIndex + ": Checking " + gate.getClass().getName() + "...");
			for (LogicConnectionPoint gateInput : gate.inputs) {
				if (gateInput == wireOutput) {
					if (gateInput.sequenceIndex >= 0) {
//						System.out.println("Repeat Gate Found!");
						gateIsReady = false;
						break;
					}
					else {
						// Set this input's sequence index making it familiar
						gateInput.sequenceIndex = compileIndex;
					}
				}
				else if (gateInput.sequenceIndex < 0) {
					// This gate contains an unknown wire
					gateIsReady = false;
//					System.out.println("Unknown input found");
				}
			}
			
			if (gateIsReady) {
				// Add the gate to the current sequence array
				gateList.add(gate);
//				System.out.println("Adding gate to list.");
			}
		}
		
		return gateList;
	}
	private void compileGrid() {
		System.out.println("Computing Grid...");
		
		logicGateSequence.clear();
		groupWires();
		fillSourceWires();
		
		for (LogicGate  gate : logicGates) {
			for (LogicConnectionPoint lcp : gate.outputs)
				lcp.sequenceIndex = -1;
			for (LogicConnectionPoint lcp : gate.inputs)
				lcp.sequenceIndex = -1;
		}
		
//		System.out.println("Total Wires:  " + wireGroups.size());
//		System.out.println("Source Wires: " + sourceWires.size());
//		System.out.println("Total Gates:  " + logicGates.size());
		
		ArrayList<LogicGate> previousSequenceArray = new ArrayList<LogicGate>();
		ArrayList<LogicGate> currentSequenceArray  = new ArrayList<LogicGate>();
		int compileIndex      = 0;
		boolean foundNewGates = false;
		
		// Cycle through all Source Wires first:
		for (WireGroup group : sourceWires) {
			currentSequenceArray.addAll(compileFromWireGroup(group, compileIndex));
		}
    	if (currentSequenceArray.size() > 0) {
//    		System.out.println("Source Wires extended...");
    		logicGateSequence.add(currentSequenceArray);
    		previousSequenceArray = currentSequenceArray;
    		currentSequenceArray  = new ArrayList<LogicGate>();
    		compileIndex         += 1;
    		foundNewGates         = true;
		}
		
		
		// Branch off new logic gates until no new indexes
		while (foundNewGates) {
			foundNewGates = false;
			
    		// Cycle through previous indexed logic gates:
    		for (LogicGate previousGate : previousSequenceArray) {
    			
    			// Cycle through this gate's output wires and compile from them:
    			for (LogicConnectionPoint gateOutput : previousGate.outputs) {
    				WireGroup group = gateOutput.getWireGroup(wireGroups);
    				if (group != null) {
    					currentSequenceArray.addAll(compileFromWireGroup(group, compileIndex));
    				}
    				else {
//    					System.out.println("Null wiregroup");
    				}
    			}
    		}
    		
    		foundNewGates = (currentSequenceArray.size() > 0);
    		
    		if (foundNewGates) {
        		// Add the sequence array to the compilation and continue to the next index:
        		logicGateSequence.add(currentSequenceArray);
        		previousSequenceArray = currentSequenceArray;
        		currentSequenceArray  = new ArrayList<LogicGate>();
        		compileIndex         += 1;
    		}
		}
	}
	
	////////////////////////
	//  UPDATE THE GRID!  //
	////////////////////////
	
	public void update() {
		Point ms = getBoundedMouseTilePos();
		
		if (Keyboard.left.pressed()) {
			toolbarIndex += 1;
			if (toolbarIndex >= toolbarTypes.size())
				toolbarIndex = 0;
		}
		if (Keyboard.right.pressed()) {
			toolbarIndex -= 1;
			if (toolbarIndex < 0)
				toolbarIndex = toolbarTypes.size() - 1;
		}
		
		if (running) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (tiles[x][y] != null)
						tiles[x][y].update();
				}
			}
			tickSimulation();
			
			if (Keyboard.enter.pressed()) {
				running = false;
			}
		}
		else {
    		if (Keyboard.enter.pressed()) {
    			compileGrid();
    			
    			for (int i = 0; i < logicGateSequence.size(); i++) {
    				ArrayList<LogicGate> sequenceLevel = logicGateSequence.get(i);
    				System.out.println("Level " + i + ":");
    				for (LogicGate gate : sequenceLevel) {
    					System.out.println("  " + gate.getClass().getName());
    				}
    			}
    			
    			running = true;
    		}
    		
    		if (Keyboard.space.down()) {
    			// Tick simulator:
    			tickSimulation();
    		}
    		if (Keyboard.up.pressed()) {
    			// Tick simulator:
    			System.out.println("SIMULATOR TICK:");
    			tickSimulation();
    		}
    		
    		if (draggingWire) {
    			if (!Mouse.left()) {
    				dragWirePlace(ms);
    			}
    		}
    		else {
    			if (Mouse.leftPressed()) {
    				if (Keyboard.control.down()) {
    					GridObject object = getNewToolbarObject();
    					object.x = ms.x;
    					object.y = ms.y;
    					tiles[ms.x][ms.y] = object;
    				}
    				else if (Keyboard.shift.down()) {
    					tiles[ms.x][ms.y] = new SourceConstant(ms.x, ms.y);
    				}
    				else {
    					dragWireStart(ms);
    				}
    			}
    			if (Mouse.rightPressed()) {
    				// Delete Wire:
    				deleteTile(ms.x, ms.y);
    				for (int i = 0; i < 4; i++) {
    					int x = ms.x + (int)  GMath.lenDirX(1, i * 90);
    					int y = ms.y + (int) -GMath.lenDirY(1, i * 90);
    					
    					if (inBounds(x, y)) {
    						if (tiles[x][y] instanceof Wire) {
    							Wire wire = (Wire) tiles[x][y];
    							int ii = i + 2;
    							if (ii >= 4)
    								ii -= 4;
    							wire.directions[ii] = false;
    						}
    					}
    				}
    			}
    		}
		}
	}
	
	public void tickSimulation() {
		// 1.) Update source wire groups:
		for (WireGroup source : sourceWires) {
			source.tickAsSource();
		}
		
		// 2.) Update all logic gates:
		for (ArrayList<LogicGate> sequenceLevel : logicGateSequence) {
			for (LogicGate gate : sequenceLevel) {
				gate.updateInputs();
				gate.compute();
				gate.updateOutputs();
			}
		}
	}
	
	public void draw(Graphics g) {
		Point ms = getBoundedMouseTilePos();
		int ts = Grid.TILE_SIZE;
		
		// Draw highlighted tile
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(ms.x * ts, ms.y * ts, ts, ts);
		
		// Draw the grid overlay
		int gs = TILE_SIZE;
		g.setColor(Color.LIGHT_GRAY);
		for (int x = 0; x < width; x++) {
			g.drawLine(x * gs, 0, x * gs, height * gs);
		}
		for (int y = 0; y < height; y++) {
			g.drawLine(0, y * gs, width * gs, y * gs);
		}
		
		// Draw Dragging overlay
		if (draggingWire) {
			g.setColor(Color.RED);
			Point dwp = dragWirePosition;
			g.drawLine((dwp.x * ts) + (ts / 2), (dwp.y * ts) + (ts / 2), (ms.x * ts) + (ts / 2), (ms.y * ts) + (ts / 2));
		}
		
		// Draw Wire Groups
		if (Keyboard.control.down()) {
			g.setColor(Color.MAGENTA);
			for (int i = 0; i < logicGateSequence.size(); i++) {
				ArrayList<LogicGate> sequenceLevel = logicGateSequence.get(i);
				for (LogicGate gate : sequenceLevel) {
					g.drawString("" + i, gate.x * TILE_SIZE, (gate.y * TILE_SIZE) + 4);
				}
			}
		}
		
		
		// Draw all objects on the grid
		tickCheck = !tickCheck;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (tiles[x][y].tickCheck != tickCheck) {
					tiles[x][y].draw(g);
					tiles[x][y].tickCheck = tickCheck;
				}
			}
		}
		
		if (!running) {
			g.setColor(Color.BLACK);
			g.drawString("Tool Index: " + toolbarIndex, 32, 32);
			g.drawString(toolbarTypes.get(toolbarIndex).getName(), 32, 52);
		}
		else {
			g.setColor(Color.RED);
			g.drawString("[RUNNING]", 32, 32);
		}
	}
}
