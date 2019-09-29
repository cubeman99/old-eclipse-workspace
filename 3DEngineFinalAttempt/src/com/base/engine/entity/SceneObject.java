package com.base.engine.entity;

import java.util.ArrayList;
import com.base.engine.audio.SoundSource;
import com.base.engine.common.Rect3f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Transform;
import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class SceneObject implements SoundSource {
	private SceneObject parent;
	private ArrayList<SceneObject> children;
	private Transform transform;
	private boolean attatched;
	private boolean destroyed;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public SceneObject() {
		parent    = null;
		children  = new ArrayList<SceneObject>();
		transform = new Transform();
		attatched = false;
		destroyed = false;
	}
	
	

	// =================== ACCESSORS =================== //

	public boolean isDestroyed() {
		return destroyed;
	}
	
	public boolean isAttatched() {
		return attatched;
	}
	
	public Rect3f getBoundingBox() {
		float size = 1;
		return new Rect3f(getTransform().getPosition().minus(size / 2), new Vector3f(size));
	}
	
	public Transform getTransform() {
		if (attatched && parent != null)
			return parent.getTransform();
		return transform;
	}
	
	public SceneObject getParent() {
		return parent;
	}
	
	public ArrayList<SceneObject> getChildren() {
		return children;
	}
	
	

	// ==================== MUTATORS ==================== //

	public void addAttatchment(SceneObject attatchment) {
		attatchment.attatch(this);
//		child.setEngine(engine);
		//child.getTransform().setParent(transform);
	}
	
	public void addChild(SceneObject child) {
		children.add(child);
		child.getTransform().setParent(transform);
		child.setParent(this);
//		child.setEngine(engine);
	}
	
	public void removeChild(SceneObject child) {
		children.remove(child);
		child.getTransform().setParent(null);
		child.setParent(null);
	}
	
	public void attatch(SceneObject base) {
		if (parent != null)
			detatch();
		base.children.add(this);
		attatched = true;
		parent    = base;
	}
	
	public void detatch() {
		if (parent != null) {
    		parent.children.remove(this);
    		attatched = false;
    		parent    = null;
		}
	}
	
	public final void destroy() {
		destroyed = true;
//		onDestroy(); TODO
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	public void setParent(SceneObject parent) {
		this.parent = parent;
	}
	
	public void update(float delta) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).update(delta);
			if (children.get(i).isDestroyed())
				children.remove(i--);
		}
	}
	
	public void render(Shader shader, RenderingEngine renderingEngine) {
		for (int i = 0; i < children.size(); i++)
			children.get(i).render(shader, renderingEngine);
	}



	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public Vector3f getSoundPosition() {
		return getTransform().getTransformedPosition();
	}

	@Override
	public Vector3f getSoundVelocity() {
		return new Vector3f();
	}
}
