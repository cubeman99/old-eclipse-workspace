package map.editor.tools;

import javax.swing.ImageIcon;
import common.Vector;
import main.ImageLoader;
import map.Map;
import map.editor.MapEditor;


public abstract class Tool {
	public static final double DISTANCE_SELECT_HANDLE = 6.0;
	public static final double DISTANCE_SELECT_EDGE   = 3.0;
	public static final double DISTANCE_SELECT_CENTER = 5.0;
	
	public MapEditor editor;
	public Map map;
	public String name;
	public ImageIcon icon;
	public Vector mousePosition;
	public Vector mouseGridPosition;
	public Vector mousePositionLast;
	public Vector mouseGridPositionLast;
	
	
	
	public Tool() {}
	
	public Tool(MapEditor editor, String name, String imageName) {
		this.editor = editor;
		this.map    = editor.map;
		this.name   = name;
		this.icon   = null;
		if (ImageLoader.getImage(imageName) != null)
			this.icon = new ImageIcon(ImageLoader.getImage(imageName));
		else
			System.out.println("Failed to get image: " + imageName);
	}
	
	/** Call when the tool is selected. **/
	public abstract void initialize();
	
	/** Call when the tool is no longer selected. **/
	public abstract void terminate();
	
	/** Return whether this tool is busy and shouldn't be interrupted. **/
	public abstract boolean isBusy();
	
	/** Update the tool. **/
	public abstract void update();
	
	/** Handle drawing functions. **/
	public abstract void draw();
	
	
	public boolean equals(Tool t) {
		return (this.name.equals(t.name));
	}
}
