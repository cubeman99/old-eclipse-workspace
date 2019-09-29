package base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	private int id;

	public Texture(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void setId(int id) {
		this.id = id;
	}
}
