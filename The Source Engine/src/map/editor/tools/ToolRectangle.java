package map.editor.tools;

import java.awt.Color;
import common.Draw;
import common.GMath;
import common.Vector;
import common.shape.Polygon;
import common.shape.Rectangle;
import main.Keyboard;
import main.Mouse;
import map.editor.MapEditor;


/** This tool is used to edit vertex positions. **/
public class ToolRectangle extends Tool {
	public Vector dragPoint;
	public boolean dragging;
	

	public ToolRectangle() {}
	
	public ToolRectangle(MapEditor editor) {
		super(editor, "Rectangle tool", "rectangle");
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
				Rectangle r = new Rectangle(dragPoint, mouseGridPosition);
				if (GMath.distance(dragPoint, mouseGridPosition) > GMath.EPSILON) {
					// Create Rectangle:
					Polygon p = r.toPolygon();
					map.addWall(p);
					editor.selectPolygon(p);
				}
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
    		Polygon p = new Rectangle(dragPoint, mouseGridPosition).toPolygon();
    		
    		Draw.setColor(Color.RED);
    		Draw.drawPolygon(p);
    		
    		Draw.setColor(Color.WHITE);
    		for (int j = 0; j < p.vertexCount(); j++)
    			editor.drawVertex(p.getVertex(j), 5);
		}
	}
	
}
