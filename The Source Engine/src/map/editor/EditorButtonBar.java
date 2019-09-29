package map.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import main.ImageLoader;


public class EditorButtonBar extends JToolBar {
	private static final long serialVersionUID = 1L;
	public static final Dimension SEPARATOR_DIMENSION = new Dimension(10, 22);
	public EditorComponent editorComponent;
	public MapEditor editor;
	
	
	public EditorButtonBar(EditorComponent editorComponent) {
		super();
		this.editorComponent = editorComponent;
		this.editor          = editorComponent.editor;
		
		
		Dimension dim = new Dimension(32, 40);
		
		setBackground(Color.white);
		setOpaque(true);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		setSize(dim);
		setFloatable(false);
		setFocusable(false);


		addSeparator();
		addButton("New", "iconNew");
		addButton("Open...", "iconOpen");
		addSeparator();
		addButton("Save", "iconSave");
		addButton("Save As...", "iconSaveAs");
		addSeparator();
		addButton("Cut", "iconCut");
		addButton("Copy", "iconCopy");
		addButton("Paste", "iconPaste");
		addSeparator();
		addToggleButton("Show Grid|Hide Grid",  "iconGrid");
		addButton("Decrease Grid Scale",  "iconDecreaseGrid");
		addButton("Increase Grid Scale",  "iconIncreaseGrid");
		addSeparator();
		addToggleButton("Run|Stop",  "iconRun", "iconStop");
		
	}

	private void addToggleButton(String name, String imageName) {
		Image img = ImageLoader.loadNewImage(imageName + ".png");
		if (img != null) {
    		addComponent(new JToggleButton(new ImageIcon(img)), name);
		}
	}

	private void addToggleButton(String name, String imageName1, String imageName2) {
		Image img1 = ImageLoader.loadNewImage(imageName1 + ".png");
		Image img2 = ImageLoader.loadNewImage(imageName2 + ".png");
		if (img1 != null && img2 != null) {
			LinkedAction action = addComponent(new JToggleButton(new ImageIcon(img1)), name);
			if (action != null) {
				action.setIcons(new ImageIcon(img1), new ImageIcon(img2));
			}
		}
	}

	private void addButton(String name, String imageName) {
		Image img = ImageLoader.loadNewImage(imageName + ".png");
		if (img != null) {
			addComponent(new JButton(new ImageIcon(img)), name);
		}
	}
	
	private LinkedAction addComponent(AbstractButton button, String name) {
		button.setToolTipText(name);
		button.setOpaque(false);
		button.setFocusable(false);
		
		// Link this button to a menu item:
		LinkedAction action = editorComponent.fileMenu.actionMap.get(name);
		if (action != null) {
			button.setSelected(action.getButtonState());
			action.addLink(button);
			button.addActionListener(action);
			button.setToolTipText(name + (action.shortcutText.equals("") ? "" : " [" + action.shortcutText + "]"));
		}
		
		super.add(button);
		return action;
	}
	
	@Override
	public void addSeparator() {
		super.addSeparator(SEPARATOR_DIMENSION);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = this.getSize();
		g.drawImage(ImageLoader.getImage("buttonBarBackground"), 0, 0, size.width, size.height, this);
	}
}
