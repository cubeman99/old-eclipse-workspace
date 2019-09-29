package map.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import common.GMath;
import common.transform.MirrorTransformation;
import main.LookAndFeel;
import main.Main;


public class EditorFileMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private static final int MAX_RECENT_FILES = 10;
	
	public HashMap<String, LinkedAction> actionMap;
	public EditorComponent editorComponent;
	public MapEditor editor;
	
	public JMenuItem itemTestRun;
	
	private JMenu file, edit, map, view, help;
	private JMenu menuRecentFiles;
	private JMenu menuTransform;
	private JMenu menuLookAndFeel;
	private ButtonGroup lookAndFeelButtonGroup;
	
	private JMenuItem itemClearRecentFiles;
	private ArrayList<File> recentFiles;
	
	
	public EditorFileMenu(EditorComponent editorComp) {
		super();
		this.editorComponent = editorComp;
		this.editor          = editorComp.editor;
		this.actionMap       = new HashMap<String, LinkedAction>();
		this.recentFiles     = new ArrayList<File>();
		
		file = addMenu("File", KeyEvent.VK_F);
		edit = addMenu("Edit", KeyEvent.VK_E);
		map  = addMenu("Map",  KeyEvent.VK_M);
		view = addMenu("View", KeyEvent.VK_V);
		help = addMenu("Help", KeyEvent.VK_H);
		
		
		
		// =========== FILE =========== //
		
		// NEW
		addMenuButton(file, "New", KeyEvent.VK_N, ActionEvent.CTRL_MASK, new LinkedAction() {
			public void onPress(boolean state) {
				editor.fileNew();
			}
		});
		
		// OPEN...
		addMenuButton(file, "Open...", KeyEvent.VK_O, ActionEvent.CTRL_MASK, new LinkedAction() {
			public void onPress(boolean state) {
				editor.fileOpen();
				editorComponent.refreshWindowTitle();
			}
		});
		
		menuRecentFiles = addSubMenu(file, "Recent Files");
		file.addSeparator();
		
		itemClearRecentFiles = addMenuButton(menuRecentFiles, "Clear Recent Files", 0, 0, new LinkedAction() {
			public void onPress(boolean state) {
				recentFiles.clear();
				refreshRecentFiles();
			}
		});
		
		// SAVE
		addMenuButton(file, "Save", KeyEvent.VK_S, ActionEvent.CTRL_MASK, new LinkedAction() {
			public void onPress(boolean state) {
				editor.fileSave();
				editorComponent.refreshWindowTitle();
			}
		});
		
		// SAVE AS...
		addMenuButton(file, "Save As...", KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK, new LinkedAction() {
			public void onPress(boolean state) {
				editor.fileSaveAs();
				editorComponent.refreshWindowTitle();
			}
		});
		
		file.addSeparator();
		
		// EXIT
		addMenuButton(file, "Exit", KeyEvent.VK_ESCAPE, ActionEvent.SHIFT_MASK, new LinkedAction(true) {
			public void onPress(boolean state) {
				Main.stop();
			}
		});
		
		
		
		// =========== EDIT =========== //
		
		// CUT
		addMenuButton(edit, "Cut", KeyEvent.VK_X, ActionEvent.CTRL_MASK, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				editor.toolSelection.saveToClipBoard();
				editor.toolSelection.deleteSelection();
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});
		
		// COPY
		addMenuButton(edit, "Copy", KeyEvent.VK_C, ActionEvent.CTRL_MASK, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				editor.toolSelection.saveToClipBoard();
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});
		
		// PASTE
		addMenuButton(edit, "Paste", KeyEvent.VK_V, ActionEvent.CTRL_MASK, new LinkedAction() {
			public void onPress(boolean state) {
				editor.setTool(editor.toolSelection);
				editor.toolSelection.loadFromClipBoard();
			}
			public boolean isEnabled() {
				return (editor.toolSelection.clipBoard.size() > 0);
			}
		});
		
		edit.addSeparator();
		
		menuTransform = addSubMenu(edit, "Transform");
		
		// ROTATE 90° CLOCKWISE
		addMenuButton(menuTransform, "Rotate 90° Clockwise", 0, 0, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				editor.toolSelection.rotateSelection(-GMath.HALF_PI);
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});
		
		// ROTATE 90° COUNTER CLOCKWISE
		addMenuButton(menuTransform, "Rotate 90° Counter-Clockwise", 0, 0, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				editor.toolSelection.rotateSelection(GMath.HALF_PI);
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});
		
		// ROTATE 180°
		addMenuButton(menuTransform, "Rotate 180°", 0, 0, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				editor.toolSelection.rotateSelection(GMath.PI);
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});
		
		// ROTATE...
		addMenuButton(menuTransform, "Rotate...", 0, 0, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				// TODO
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});
		
		menuTransform.addSeparator();
		
		// FLIP HORIZONTAL
		addMenuButton(menuTransform, "Flip Horizontal", 0, 0, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				editor.toolSelection.transformSelection(new MirrorTransformation(
					editor.toolSelection.selectionBox.getCenter().x,
					editor.toolSelection.selectionBox.y1(),
					editor.toolSelection.selectionBox.getCenter().x,
					editor.toolSelection.selectionBox.y2()
				));
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});

		// FLIP VERTICAL
		addMenuButton(menuTransform, "Flip Vertical", 0, 0, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				editor.toolSelection.transformSelection(new MirrorTransformation(
					editor.toolSelection.selectionBox.x1(),
					editor.toolSelection.selectionBox.getCenter().y,
					editor.toolSelection.selectionBox.x2(),
					editor.toolSelection.selectionBox.getCenter().y
				));
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});
		
		edit.addSeparator();
		
		// DELETE SELECTION
		addMenuButton(edit, "Delete Selection", KeyEvent.VK_DELETE, 0, new LinkedAction() {
			public void onPress(boolean state) {
				if (editor.getTool() == editor.toolSelection)
					editor.toolSelection.deleteSelection();
				else
					editor.toolVertex.deleteSelection();
			}
			public boolean isEnabled() {
				return ((editor.getTool() == editor.toolSelection && 
						 editor.toolSelection.selection.size() > 0) || 
						(editor.getTool() == editor.toolVertex &&
						 editor.toolVertex.selectedIndexes.size() > 0));
			}
		});
		
		// SELECT ALL
		addMenuButton(edit, "Select All", KeyEvent.VK_A, ActionEvent.CTRL_MASK, new LinkedAction() {
			public void onPress(boolean state) {
				if (editor.getTool() == editor.toolVertex)
					editor.toolVertex.selectAll();
				else {
    				editor.setTool(editor.toolSelection);
    				editor.toolSelection.selectAll();
				}
			}
		});

		// DESELECT
		addMenuButton(edit, "Deselect", KeyEvent.VK_D, ActionEvent.CTRL_MASK, new LinkedAction(editor.toolSelection) {
			public void onPress(boolean state) {
				editor.toolSelection.clearSelection();
			}
			public boolean isEnabled() {
				return (editor.toolSelection.selection.size() > 0);
			}
		});
		
		
		// =========== MAP =========== //
		
		// RUN / STOP
		itemTestRun = addMenuCheckBox(map, "Run|Stop", false, KeyEvent.VK_F5, 0, new LinkedAction(true) {
			public void onPress(boolean state) {
				if (state)
					editorComponent.startTestRun();
				else
					editorComponent.stopTestRun();
			}
		});
		
		
		
		// =========== VIEW =========== //
		
		// SHOW GRID / HIDE GRID
		addMenuCheckBox(view, "Show Grid|Hide Grid", true, KeyEvent.VK_G, 0, new LinkedAction() {
			public void onPress(boolean state) {
				editor.showGrid = state;
			}
		});
		
		// DECREASE GRID SCALE
		addMenuButton(view, "Decrease Grid Scale", KeyEvent.VK_OPEN_BRACKET, 0, new LinkedAction() {
			public void onPress(boolean state) {
				editor.decreaseGridScale();
			}
			public boolean isEnabled() {
				return editor.showGrid;
			}
		}.setUseableWhileBusy(true));

		// INCREASE GRID SCALE
		addMenuButton(view, "Increase Grid Scale", KeyEvent.VK_CLOSE_BRACKET, 0, new LinkedAction() {
			public void onPress(boolean state) {
				editor.increaseGridScale();
			}
			public boolean isEnabled() {
				return editor.showGrid;
			}
		}.setUseableWhileBusy(true));
		
		view.addSeparator();
		
		
		
		// =========== HELP =========== //
		
		// CONTENTS
		addMenuButton(help, "Contents", KeyEvent.VK_F1, 0, new LinkedAction() {
			public void onPress(boolean state) {
				// TODO
			}
		});
		
		
		
		menuLookAndFeel = new JMenu("Look and Feel");
		lookAndFeelButtonGroup = new ButtonGroup();
		view.add(menuLookAndFeel);
		addLookAndFeelButton("Metal", LookAndFeel.LAF_METAL);
		addLookAndFeelButton("Nimbus", LookAndFeel.LAF_NIMBUS);
		addLookAndFeelButton("CDE/Motif", LookAndFeel.LAF_CDE_MOTIF);
		addLookAndFeelButton("Windows", LookAndFeel.LAF_SYSTEM).setSelected(true);
		addLookAndFeelButton("Windows Classic", LookAndFeel.LAF_WINDOWS);
		
		
		refreshRecentFiles();
//		JCheckBoxMenuItem viewGrid = new JCheckBoxMenuItem("Show Grid|Hide Grid");
//		addMenuItem(view, viewGrid);
	}
	
	private JMenuItem addMenuCheckBox(JMenu menu, String name, boolean initialState, int keyBinding, int keyMask, LinkedAction action) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
		item.setSelected(initialState);
		return addMenuItem(menu, item, keyBinding, keyMask, action);
	}
	
	private JMenuItem addMenuRadioButton(JMenu menu, String name, int keyBinding, int keyMask, LinkedAction action) {
		return addMenuItem(menu, new JRadioButtonMenuItem(name), keyBinding, keyMask, action);
	} 
	
	private JMenuItem addMenuButton(JMenu menu, String name, int keyBinding, int keyMask, LinkedAction action) {
		JMenuItem item = new JMenuItem(name);
		return addMenuItem(menu, item, keyBinding, keyMask, action);
	}
	
	private JMenuItem addMenuItem(JMenu menu, JMenuItem menuItem, int keyBinding, int keyMask, LinkedAction action) {
		menu.add(menuItem);
		if (keyBinding != 0) {
			menuItem.setAccelerator(KeyStroke.getKeyStroke(keyBinding, keyMask));
		}
		if (action != null) {
			action.editor = editor;
			action.addLink(menuItem);
			menuItem.addActionListener(action);
			actionMap.put(menuItem.getText(), action);
			action.setText(menuItem.getText());
			if (keyBinding != 0)
				action.buildShortcutText(menuItem.getAccelerator().toString());
		}
		
		return menuItem;
	}
	
	/** Add a menu inside a menu. **/
	public JMenu addSubMenu(JMenu menu, String name) {
		JMenu subMenu = new JMenu(name);
		menu.add(subMenu);
		return subMenu;
	}
	
	/** Add a new menu to the menu bar, returning the created menu. **/
	public JMenu addMenu(String name, int mnemonic) {
		JMenu newMenu = new JMenu(name);
		newMenu.setFocusable(false);
		newMenu.setMnemonic(mnemonic);
		super.add(newMenu);
		return newMenu;
	}
	
	/** Add a new file to the recent files list. **/
	public void addRecentFile(File file) {
		recentFiles.add(0, file);
		if (recentFiles.size() == MAX_RECENT_FILES + 1)
			recentFiles.remove(MAX_RECENT_FILES - 1);
		for (int i = 1; i < recentFiles.size(); i++) {
			if (file.equals(recentFiles.get(i)))
				recentFiles.remove(i--);
		}
		refreshRecentFiles();
	}
	
	/** Refresh the recent files menu. **/
	private void refreshRecentFiles() {
		menuRecentFiles.removeAll();
		for (int i = 0; i < recentFiles.size(); i++) {
			final File file = recentFiles.get(i);
			JMenuItem item = new JMenuItem((i + 1) + ". " + file.getName());
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					editor.fileOpen(file);
				}
			});
			menuRecentFiles.add(item);
		}
		
		if (recentFiles.size() > 0) {
			menuRecentFiles.addSeparator();
			menuRecentFiles.setEnabled(true);
			menuRecentFiles.add(itemClearRecentFiles);
		}
		else
			menuRecentFiles.setEnabled(false);
	}
	
	/** Add a look and feel button. **/
	private JRadioButtonMenuItem addLookAndFeelButton(String name, final int lookAndFeelIndex) {
		JRadioButtonMenuItem laf  = new JRadioButtonMenuItem(name);
		laf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LookAndFeel.set(lookAndFeelIndex);
			}
		});
		lookAndFeelButtonGroup.add(laf);
		menuLookAndFeel.add(laf);
		return laf;
	}
}
