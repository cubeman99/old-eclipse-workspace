package com.base.game.editor.tools;

import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Rect3f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;
import com.base.engine.core.Mouse;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Brush;
import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Window;
import com.base.game.editor.Editor;
import com.base.game.editor.handle.ScaleHandle;

public class ToolSelection extends EditorTool {
	private Brush selectedBrush;
	private ArrayList<SceneObject> selection;
	private boolean dragging;
	private Vector2f dragOffset;
	private ScaleHandle dragHandle;
	
	private Rect2f selectionArea;
	private Rect3f selectionBox;
	private Rect3f prevSelectionBox;
	private ArrayList<ScaleHandle> scaleHandles;
	
	

	// ================== CONSTRUCTORS ================== //

	public ToolSelection(Editor editor) {
		super(editor, "Selection Tool");
		
		selectionBox  = null;
		dragging      = false;
		dragOffset    = null;
		dragHandle    = null;
		selectedBrush = null;
		selection     = new ArrayList<SceneObject>();
		scaleHandles  = new ArrayList<ScaleHandle>();
		
		scaleHandles.add(new ScaleHandle(0.0f, 0.0f));
		scaleHandles.add(new ScaleHandle(0.5f, 0.0f));
		scaleHandles.add(new ScaleHandle(1.0f, 0.0f));
		scaleHandles.add(new ScaleHandle(1.0f, 0.5f));
		scaleHandles.add(new ScaleHandle(1.0f, 1.0f));
		scaleHandles.add(new ScaleHandle(0.5f, 1.0f));
		scaleHandles.add(new ScaleHandle(0.0f, 1.0f));
		scaleHandles.add(new ScaleHandle(0.0f, 0.5f));
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void drawVertex(Vector2f v, float radius, Vector3f lineColor, Vector3f fillColor) {
		float s = editor.getWorldLength(radius);
		Draw2D.setColor(fillColor);
		Draw2D.fillRect(v.x - s, v.y - s, s * 2, s * 2);
		Draw2D.setColor(lineColor);
		Draw2D.drawRect(v.x - s, v.y - s, s * 2, s * 2);
	}
	
	public void drawScaleHandle(Vector2f v) {
		drawVertex(v, 5, Draw2D.BLACK, Draw2D.WHITE);
	}
	
	public void drawScaleHandle(Vector2f v, Vector3f lineColor, Vector3f fillColor) {
		drawVertex(v, 5, lineColor, fillColor);
	}
	
	public void clearSelection() {
		selection.clear();
		selectionBox = null;
	}
	
	public void addToSelection(Rect2f area) {
		for (Brush brush : editor.getBrushes()) {
			Rect3f box    = brush.getBoundingBox();
			Rect2f bounds = box.swizzle(editor.getAxisX(), editor.getAxisY());
			if (area.contains(bounds)) {
				selection.add(brush);
				if (selectionBox == null)
					selectionBox = new Rect3f(box);
				else
					selectionBox.include(box);
			}
		}
		
		for (SceneObject object : editor.getScene().getChildren()) {
			Rect3f box    = object.getBoundingBox();
			Rect2f bounds = box.swizzle(editor.getAxisX(), editor.getAxisY());
			if (area.contains(bounds)) {
				selection.add(object);
				if (selectionBox == null)
					selectionBox = new Rect3f(box);
				else
					selectionBox.include(box);
			}
		}
		if (selectionBox != null)
			prevSelectionBox = new Rect3f(selectionBox);
	}
	
	public void stopScaling() {
		for (SceneObject object : selection) {
			if (object instanceof Brush) {
				Brush brush = (Brush) object;
				
				for (int i = 0; i < brush.getNumVertices(); i++) {
					Vector3f v = brush.getVertexPosition(i);
					
					Vector3f p = v.minus(prevSelectionBox.getPosition()).dividedBy(prevSelectionBox.getSize());
					v.set(selectionBox.getPosition().plus(p.times(selectionBox.getSize())));
				}
				
	    		brush.recalculate();
			}
			else {
				Vector3f v = object.getTransform().getPosition();
				Vector3f p = v.minus(prevSelectionBox.getPosition()).dividedBy(prevSelectionBox.getSize());
				object.getTransform().getPosition().set(selectionBox.getPosition().plus(p.times(selectionBox.getSize())));
			}
		}
		
		selectionBox.sort();
		prevSelectionBox.set(selectionBox);
	}
	
	public void stopTranslating() {
		Vector3f translation = selectionBox.getPosition()
				.minus(prevSelectionBox.getPosition());
		
		for (SceneObject object : selection) {
			if (object instanceof Brush) {
				Brush brush = (Brush) object;
				
	    		for (int i = 0; i < brush.getNumVertices(); i++) {
	    			Vector3f v = brush.getVertexPosition(i);
	    			
	    			v.add(translation);
	    		}
	    		brush.recalculate();
			}
			else {
				object.getTransform().getPosition().add(translation);
			}
		}
		
	    prevSelectionBox.set(selectionBox);
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update() {
		Vector2f mp = editor.getMousePosition();
		
		
		if (dragging) {
			if (selectionArea != null) {
				selectionArea.setSize(mp.minus(selectionArea.getPosition()));
				
    			if (!Mouse.left.down()) {
    				if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
    					clearSelection();
        			addToSelection(selectionArea);
    				dragging      = false;
    				selectionArea = null;
    			}
			}
			else if (dragHandle != null) {
    			mp = editor.getSnappedToGrid(mp);
    			
    			if (dragHandle.getPosition().x < 0.25f)
    				selectionBox.setComponent(editor.getAxisX() * 4, mp.x); // negative x
    			if (dragHandle.getPosition().x > 0.75f)
    				selectionBox.setComponent(editor.getAxisX() * 2, mp.x); // positive x
    			if (dragHandle.getPosition().y < 0.25f)
    				selectionBox.setComponent(editor.getAxisY() * 4, mp.y); // negative y
    			if (dragHandle.getPosition().y > 0.75f)
    				selectionBox.setComponent(editor.getAxisY() * 2, mp.y); // positive y
    			
    			if (!Mouse.left.down()) {
    				stopScaling();
    				dragging   = false;
    				dragHandle = null;
    			}
			}
			else {
    			mp = editor.getSnappedToGrid(mp.minus(dragOffset));
				selectionBox.getPosition().setComponent(editor.getAxisX(), mp.x);
				selectionBox.getPosition().setComponent(editor.getAxisY(), mp.y);
    			
    			if (!Mouse.left.down()) {
    				stopTranslating();
    				dragging   = false;
    				dragOffset = null;
    			}
			}
		}
		else {
			if (selectionBox != null && Mouse.left.pressed()) {
    			Rect2f bounds = new Rect2f(selectionBox.getPosition().swizzle(editor.getAxisX(), editor.getAxisY()),
    					selectionBox.getSize().swizzle(editor.getAxisX(), editor.getAxisY()));
    			
    			// Check clicking on scale handles.
    			for (ScaleHandle handle : scaleHandles) {
    				Vector2f handlePos = handle.getPosition(bounds);
    				
    				if (mp.distanceTo(handlePos) < editor.getWorldLength(8)) {
						dragging = true;
						dragHandle = handle;
    				}
    			}
    			
    			if (!dragging && bounds.contains(mp)) {
    				dragging = true;
    				dragOffset = mp.minus(bounds.getPosition());
    			}
			}
			
			if (!dragging && Mouse.left.pressed()) {
				dragging = true;
				selectionArea = new Rect2f(mp, new Vector2f(0, 0));
//				
//				selectedBrush = null;
//				selectionBox  = null;
//				
//				for (Brush brush : editor.getBrushes()) {
//					for (int i = 0; i < brush.getNumVertices(); i++) {
//						Vector2f v = brush.getVertexPosition(i).swizzle(editor.getAxisX(), editor.getAxisY());
//
//						if (mp.distanceTo(v) < editor.getWorldLength(8)) {
//							selectedBrush    = brush;
//							selectionBox     = brush.getBoundingBox();
//							prevSelectionBox = new Rect3f(selectionBox);
//						}
//					}
//				}
			}
		}
	}


	@Override
	public void draw() {
		Vector2f mp = editor.getMousePosition();
		
		if (dragging) {

			if (selectionArea != null) {
				Draw2D.setColor(Draw2D.GREEN);
				Draw2D.drawRect(selectionArea);
			}
		}
		
		if (selectionBox != null) {
			Rect2f bounds = selectionBox.swizzle(editor.getAxisX(), editor.getAxisY());
			Draw2D.setColor(Draw2D.RED);
			Draw2D.drawRect(bounds);
			
			// Draw scale handles.
			for (ScaleHandle handle : scaleHandles) {
				Vector2f handlePos = handle.getPosition(bounds);
				boolean hover = false;
				
				if (dragHandle == handle || mp.distanceTo(handlePos) < editor.getWorldLength(8)) {
					hover = true;
				}
				
				drawScaleHandle(handlePos, Draw2D.BLACK, (hover ? Draw2D.YELLOW : Draw2D.WHITE));
			}
		}
	}
}
