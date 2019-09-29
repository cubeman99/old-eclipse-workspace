package map.editor.tools;

import java.awt.Color;
import common.Draw;
import common.GMath;
import common.Vector;
import common.shape.Line;
import main.Keyboard;
import main.Mouse;
import map.editor.MapEditor;


/** This tool is used to edit vertex positions. **/
public class ToolLine extends Tool {
	public Vector dragPoint;
	public boolean dragging;
	

	public ToolLine() {}
	
	public ToolLine(MapEditor editor) {
		super(editor, "Line tool", "line");
	}

	@Override
	public void initialize() {
		dragPoint = null;
		dragging  = false;
	}

	@Override
	public void terminate() {
		
	}

	@Override
	public boolean isBusy() {
		return dragging;
	}

	@Override
	public void update() {
		if (dragging) {
			if (!Mouse.left.down()) {
				dragging = false;
				Line l = new Line(dragPoint, mouseGridPosition);
				if (GMath.distance(dragPoint, mouseGridPosition) > GMath.EPSILON)
					map.addWall(l);
			}
			else if (Keyboard.escape.pressed()) {
				// Cancel dragging:
				dragging = false;
			}
		}
		else {
			if (Mouse.left.pressed()) {
				dragPoint = new Vector(mouseGridPosition);
				dragging  = true;
				editor.madeChange();
			}
		}
	}

	@Override
	public void draw() {
		if (dragging) {
    		Draw.setColor(Color.RED);
    		Draw.drawLine(dragPoint, mouseGridPosition);
    		
    		Draw.setColor(Color.WHITE);
    		editor.drawVertex(dragPoint, 5);
    		editor.drawVertex(mouseGridPosition, 5);
		}
	}
	
}
