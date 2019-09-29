package zelda.editor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.properties.Property;
import zelda.common.util.InvalidInputException;
import zelda.editor.gui.Button;
import zelda.editor.gui.Panel;
import zelda.game.world.Frame;
import zelda.game.world.Level;
import zelda.game.world.World;
import zelda.game.zone.Zone;
import zelda.main.Keyboard;
import zelda.main.Mouse;


public class PanelButtons extends Panel {
	private ArrayList<Button> buttons;
	private JFileChooser fileChooser;

	public Button buttonNew;
	public Button buttonSave;
	public Button buttonSaveAs;
	public Button buttonOpen;
	public Button buttonAddLevel;
	public Button buttonDeleteLevel;
	public Button buttonRenameLevel;
	public Button buttonResizeLevel;
	public Button buttonShowFrameBorders;
	public Button buttonChangeZone;
	public Button buttonShowGrid;
	public Button buttonTestRun;
	public Button buttonClear;
	public Button buttonShowObjects;
	public Button buttonInfo;
	public Button buttonChangeTileset;
	public Button buttonAddProperty;



	// ================== CONSTRUCTORS ================== //

	public PanelButtons(Editor editor) {
		super(editor);
		draggable = false;
		backgroundColor = new Color(224, 224, 224);
		buttons = new ArrayList<Button>();


		buttonNew = addButton("New World", 1, 1).setHotkeys(KeyEvent.VK_N,
				Keyboard.control);
		buttonSave = addButton("Save World", 2, 1).setHotkeys(KeyEvent.VK_S,
				Keyboard.control);
		buttonSaveAs = addButton("Save World As", 3, 1).setHotkeys(
				KeyEvent.VK_S, Keyboard.control, Keyboard.shift);
		buttonOpen = addButton("Open World", 4, 1).setHotkeys(KeyEvent.VK_O,
				Keyboard.control);

		buttonAddLevel = addButton("Add Level", 0, 2);
		buttonDeleteLevel = addButton("Delete Level", 7, 1);
		buttonRenameLevel = addButton("Rename Level", 6, 1).setHotkeys(
				KeyEvent.VK_F2);

		buttonResizeLevel = addButton("Resize Level", 3, 0).setHotkeys(
				KeyEvent.VK_R, Keyboard.control);

		buttonClear = addButton("Clear Level", 7, 0);
		buttonChangeZone = addButton("Change Zone", 6, 0);

		buttonShowFrameBorders = addToggleButton("Show Frame Borders", 4, 0)
				.setHotkeys(KeyEvent.VK_F, Keyboard.control).toggle();
		buttonShowGrid = addToggleButton("Show Grid", 5, 0).setHotkeys(
				KeyEvent.VK_G, Keyboard.control);
		buttonShowObjects = addToggleButton("Show Objects", 1, 2).setHotkeys(
				KeyEvent.VK_H, Keyboard.control).toggle();

		buttonChangeTileset = addButton("Change Tileset", 7, 2).setHotkeys(
				KeyEvent.VK_F3);
		buttonAddProperty = addButton("Add Property", 0, 3).setHotkeys(
				KeyEvent.VK_F4);

		buttonTestRun = addButton("Test Run", 0, 1).setHotkeys(KeyEvent.VK_F5);
		buttonInfo = addButton("Help", 5, 1).setHotkeys(KeyEvent.VK_F1);



		fileChooser = new JFileChooser() {
			private static final long serialVersionUID = 1L;

			public boolean accept(File f) {
				return (f.isDirectory() || f.getName().endsWith(".zwd"));
			}
		};
		fileChooser.setCurrentDirectory(new File(
				"C:/Eclipse Workspace/workspace/Zelda"));
	}



	// =================== ACCESSORS =================== //



	// ==================== MUTATORS ==================== //

	private void performButtonActions() {
		World world = editor.getWorld();
		Level level = world.getCurrentLevel();

		// NEW WORLD:
		if (buttonNew.isPressed()) {
			if (!editor.hasMadeChanges() || promptSaveChanges()) {
				// TODO: New World!
			}
		}

		// SAVE WORLD:
		else if (buttonSave.isPressed()) {
			saveWorld();
		}

		// SAVE WORLD AS:
		else if (buttonSaveAs.isPressed()) {
			saveWorldAs();
		}

		// OPEN WORLD:
		else if (buttonOpen.isPressed()) {
			if (!editor.hasMadeChanges() || promptSaveChanges()) {
				Keyboard.clear();
				Mouse.clear();
				if (fileChooser.showOpenDialog(editor.getRunner().getFrame()) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					editor.loadWorld(file);
					editor.setMadeChanges(false);
					editor.setFile(file);
				}
			}
		}

		// ADD LEVEL:
		else if (buttonAddLevel.isPressed()) {
			try {
				JTextField fieldName = new JTextField("newlevel");
				SpinnerNumberModel fieldWidth = new SpinnerNumberModel(10, 1,
						100, 1);
				SpinnerNumberModel fieldHeight = new SpinnerNumberModel(10, 1,
						100, 1);
				JComboBox fieldFrameSize = new JComboBox(new Object[] {"Small",
						"Large"});
				JComboBox fieldZone = new JComboBox();

				ArrayList<Zone> zones = Resources.zones.getZones();
				for (int i = 0; i < zones.size(); i++) {
					fieldZone.addItem(zones.get(i).getName());
				}

				final JComponent[] inputs = new JComponent[] {
						new JLabel("Level Name"), fieldName,
						new JLabel("Width"), new JSpinner(fieldWidth),
						new JLabel("Height"), new JSpinner(fieldHeight),
						new JLabel("Frame Size"), fieldFrameSize,
						new JLabel("Zone"), fieldZone};

				int opt = JOptionPane.showOptionDialog(null, inputs,
						"New Level", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, new Object[] {
								"Finish", "Cancel"}, "Cancel");

				if (opt == 0) {
					Point frameSize = fieldFrameSize.getSelectedIndex() == 0 ? Frame.SIZE_SMALL
							: Frame.SIZE_LARGE;
					Level newLevel = world.addLevel(fieldName.getText(),
							fieldWidth.getNumber().intValue(), fieldHeight
									.getNumber().intValue(), frameSize);
					Zone zone = Resources.zones.getZone((String) fieldZone
							.getSelectedItem());
					editor.tileset.setZone(zone);
					newLevel.setZone(zone);

					editor.fillNewLevel(newLevel);
					editor.getWorld().setCurrentLevel(newLevel);
				}
			}
			catch (InvalidInputException e) {
			}
		}

		// DELETE LEVEL:
		else if (buttonDeleteLevel.isPressed()) {
			if (world.getNumLevels() > 1) {
				Keyboard.clear();
				Mouse.clear();
				int n = JOptionPane.showOptionDialog(
						editor.getRunner().getFrame(),
						"Are you sure you want to delete the level "
								+ level.getName() + "?", "Delete Level",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, new Object[] {
								"Yes", "No"}, "No");
				if (n == JOptionPane.YES_OPTION) {
					world.removeLevel(level);
					world.setCurrentLevel(world.getLevel(0));
				}
			}
			else {
				showWarning("You must have at least one level!", "Delete Level");
			}
		}

		// RENAME LEVEL:
		else if (buttonRenameLevel.isPressed()) {
			try {
				String name = promptString(buttonRenameLevel,
						"Enter the new name of the level.", level.getName());
				level.setName(name);
			}
			catch (InvalidInputException e) {
			}
		}

		// RESIZE LEVEL:
		else if (buttonResizeLevel.isPressed()) {
			try {
				int width = promptInt(buttonResizeLevel,
						"Enter the new number of frames per column.",
						level.getSize().x);
				int height = promptInt(buttonResizeLevel,
						"Enter the new number of frames per row.",
						level.getSize().y);
				editor.resizeLevel(level, width, height);
			}
			catch (InvalidInputException e) {
			}
		}

		// CHANGE ZONE:
		else if (buttonChangeZone.isPressed()) {
			ArrayList<Zone> zones = Resources.zones.getZones();
			Object[] options = new Object[zones.size()];
			for (int i = 0; i < zones.size(); i++) {
				options[i] = zones.get(i).getName();
			}
			Keyboard.clear();
			Mouse.clear();
			String str = (String) JOptionPane.showInputDialog(editor
					.getRunner().getFrame(), "Choose a zone type.",
					buttonChangeZone.getText(), JOptionPane.PLAIN_MESSAGE,
					null, options, level.getCurrentFrame().getZone().getName());

			if (str != null) {
				for (int i = 0; i < zones.size(); i++) {
					if (zones.get(i).getName().equals(str)) {
						level.setZone(zones.get(i));
						editor.tileset.setZone(zones.get(i));
						break;
					}
				}
			}
		}

		// CLEAR LEVEL:
		else if (buttonClear.isPressed()) {
			Keyboard.clear();
			Mouse.clear();
			int n = JOptionPane.showOptionDialog(editor.getRunner().getFrame(),
					"Are you sure you want to clear the level?", "Clear Level",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, new Object[] {"Yes", "No"}, "No");
			if (n == JOptionPane.YES_OPTION)
				editor.fillNewLevel(level);
		}

		// CHANGE TILESET:
		else if (buttonChangeTileset.isPressed()) {
			editor.tileset.changeTileset();
		}

		// ADD PROPERTY:
		else if (buttonAddProperty.isPressed()) {
			JTextField fieldName = new JTextField("");
			JTextField fieldValue = new JTextField("");
			final JComponent[] inputs = new JComponent[] {
					new JLabel("Property Name"), fieldName,
					new JLabel("Property Value"), fieldValue,};
			int opt = JOptionPane.showOptionDialog(null, inputs,
					"Add Property", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, new Object[] {"Finish",
							"Cancel"}, "Cancel");

			if (opt == 0) {
				if (!fieldName.getText().equals("")) {
					Property p = editor.panelProperties.addProperty(
							fieldName.getText(), fieldValue.getText());
					editor.getSelection().onChangeProperty(p);
				}
			}
		}

		// TEST RUN:
		else if (buttonTestRun.isPressed()) {
			try {
				// TODO: ask for save.
				if (world.getNumLevels() < 1) {
					throw new InvalidInputException(
							"You must have at least one level to begin testing.",
							"Test Run");
				}
				editor.testRun();
			}
			catch (InvalidInputException e) {
				showError(e);
			}
		}
	}

	private boolean promptSaveChanges() {
		Keyboard.clear();
		Mouse.clear();
		int n = promptQuestion("Save changes to " + editor.getFileDisplayName()
				+ "?", "Save As", "Yes");
		if (n == JOptionPane.YES_OPTION)
			return saveWorld();
		return (n == JOptionPane.NO_OPTION);
	}

	private boolean saveWorld() {
		if (editor.getFile() != null) {
			editor.saveWorld(editor.getFile());
			editor.setMadeChanges(false);
			return true;
		}

		return saveWorldAs();
	}

	private boolean saveWorldAs() {
		if (editor.getFile() == null)
			fileChooser.setSelectedFile(new File("untitled.zwd"));
		if (fileChooser.showSaveDialog(editor.getRunner().getFrame()) != JFileChooser.APPROVE_OPTION)
			return false;

		File file = fileChooser.getSelectedFile();
		String fileName = file.getAbsolutePath();
		if (!file.getName().endsWith(".zwd"))
			fileName += ".zwd";
		editor.saveWorld(new File(fileName));
		editor.setMadeChanges(false);
		editor.setFile(file);

		return true;
	}

	private void showError(InvalidInputException e) {
		if (e.errorMessage != null && e.errorTitle != null)
			showError(e.errorMessage, e.errorTitle);
	}

	private void showError(String message, String title) {
		Keyboard.clear();
		Mouse.clear();
		JOptionPane.showMessageDialog(editor.getRunner().getFrame(), message,
				title, JOptionPane.ERROR_MESSAGE);
	}

	private void showWarning(String message, String title) {
		Keyboard.clear();
		Mouse.clear();
		JOptionPane.showMessageDialog(editor.getRunner().getFrame(), message,
				title, JOptionPane.WARNING_MESSAGE);
	}

	private void showMessage(String message, String title) {
		Keyboard.clear();
		Mouse.clear();
		JOptionPane.showMessageDialog(editor.getRunner().getFrame(), message,
				title, JOptionPane.INFORMATION_MESSAGE);
	}

	private int promptQuestion(String message, String title) {
		return promptQuestion(message, title, "Yes");
	}

	private int promptQuestion(String message, String title,
			String defaultOption) {
		Keyboard.clear();
		Mouse.clear();
		int n = JOptionPane.showOptionDialog(editor.getRunner().getFrame(),
				message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Yes", "No"},
				defaultOption);
		return n;
	}

	private String promptString(Button button, String message, String defInput)
			throws InvalidInputException {
		Keyboard.clear();
		Mouse.clear();
		String input = (String) JOptionPane.showInputDialog(editor.getRunner()
				.getFrame(), message, button.getText(),
				JOptionPane.PLAIN_MESSAGE, null, null, defInput);
		if (input == null)
			throw new InvalidInputException();
		return input;
	}

	private int promptInt(Button button, String message, int defInput)
			throws InvalidInputException {
		Keyboard.clear();
		Mouse.clear();
		String input = (String) JOptionPane.showInputDialog(editor.getRunner()
				.getFrame(), message, button.getText(),
				JOptionPane.PLAIN_MESSAGE, null, null, defInput);
		if (input == null)
			throw new InvalidInputException();
		return Integer.parseInt(input);
	}

	private Button addButton(String text, int sourceX, int sourceY) {
		Button b = new Button(this, text, new Point(sourceX, sourceY),
				new Point(4 + (28 * buttons.size()), 4));
		buttons.add(b);
		return b;
	}

	private Button addToggleButton(String text, int sourceX, int sourceY) {
		Button b = addButton(text, sourceX, sourceY);
		b.setToggles(true);
		return b;
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update() {
		super.update();

		for (Button b : buttons)
			b.update();

		performButtonActions();
	}

	@Override
	public void draw() {
		for (Button b : buttons)
			b.draw();
	}
}
