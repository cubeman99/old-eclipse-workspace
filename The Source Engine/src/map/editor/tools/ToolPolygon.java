package map.editor.tools;

import java.awt.Color;
import common.Draw;
import common.GMath;
import common.shape.Polygon;
import main.Keyboard;
import main.Mouse;
import map.editor.MapEditor;


/** This tool is used to edit vertex positions. **/
public class ToolPolygon extends Tool {
	public Polygon dragPolygon;
	public boolean dragging;
	

	public ToolPolygon() {}
	
	public ToolPolygon(MapEditor editor) {
		super(editor, "Polygon tool", "polygon");
	}

	@Override
	public void initialize() {
		dragPolygon = null;
		dragging    = false;
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
			if (Mouse.left.pressed()) {
				if (mouseGridPosition.distanceTo(dragPolygon.getVertex(0)) < 0.01) {
					dragPolygon.setClosed(true);
					map.addWall(dragPolygon);
					dragging = false;
					editor.selectPolygon(dragPolygon);
				}
				else if (mouseGridPosition.distanceTo(dragPolygon.getVertex(dragPolygon.vertexCount() - 1)) > GMath.EPSILON) {
					dragPolygon.addVertex(mouseGridPosition);
				}
			}
			else if (Keyboard.enter.pressed()) {
				dragging = false;
				map.addWall(dragPolygon);
			}
			else if (Keyboard.escape.pressed()) {
				// Cancel polygon:
				dragging    = false;
				dragPolygon = null;
			}
		}
		else {
			if (Mouse.left.pressed()) {
				dragPolygon = new Polygon(mouseGridPosition);
				dragging    = true;
				dragPolygon.setClosed(false);
				editor.madeChange();
			}
		}
	}

	@Override
	public void draw() {
		if (dragging) {
    		Draw.setColor(Color.RED);

			if (mouseGridPosition.distanceTo(dragPolygon.getVertex(0)) < 0.01)
				Draw.setColor(Color.GREEN);
    		
    		Draw.drawPolygon(dragPolygon);
    		Draw.drawLine(mouseGridPosition, dragPolygon.getVertex(dragPolygon.vertexCount() - 1));

			Draw.setColor(Color.WHITE);
			editor.drawVertex(mouseGridPosition, 5);
    		for (int i = 0; i < dragPolygon.vertexCount(); i++)
    			editor.drawVertex(dragPolygon.getVertex(i), 5);
		}
	}
	
}
