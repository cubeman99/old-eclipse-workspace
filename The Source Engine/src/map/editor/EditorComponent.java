package map.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import common.Draw;
import common.HUD;
import control.Control;
import main.GamePanel;
import main.GameRunnerOLD;
import main.Keyboard;
import main.Main;


public class EditorComponent extends JComponent implements GameRunnerOLD {
	private static final long serialVersionUID = 1L;
	public static final Dimension STATUS_BAR_SEPERATOR_DIMENSION = new Dimension(16, 16);
	public static final String EDITOR_NAME = "Map Editor";
	
	public Control control;
	public MapEditor editor;
	public EditorFileMenu fileMenu;
	public EditorButtonBar buttonBar;
	public JToolBar toolbar;
	public JToolBar statusBar;
	public JLabel statusTool;
	public JLabel statusFPS;
	
	public boolean testRunning;
	
	public GamePanel gamePanel;
	public boolean initialized = false;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public EditorComponent() {
		control     = new Control();
		editor      = new MapEditor(control.map, this);
		testRunning = false;
		gamePanel   = new GamePanel();
		
		initialize(Main.frame);
		editor.setToolIndex(0);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return whether the map is being tested. **/
	public boolean isTestRunning() {
		return testRunning;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Initialize the UI for the editor. **/
	public void initialize(JFrame frame) {
		toolbar = new JToolBar();
		toolbar.setPreferredSize(new Dimension(48, 48));
		toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
		toolbar.setOrientation(JToolBar.VERTICAL);
		toolbar.add(new JLabel("Tools"));
		toolbar.addSeparator(new Dimension(40, 8));
		toolbar.setFloatable(false);
		
		statusBar = new JToolBar();
		statusBar.setFloatable(false);
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		statusBar.setPreferredSize(new Dimension(20, 20));
//		statusTool = new JLabel(editor.toolbar.getTool().name);
		statusTool = new JLabel("");
		statusTool.setHorizontalAlignment(SwingConstants.RIGHT);
		statusFPS = new JLabel("FPS: " + 62.3);
		
		statusFPS.setHorizontalAlignment(SwingConstants.RIGHT);
		statusBar.addSeparator(STATUS_BAR_SEPERATOR_DIMENSION);
		statusBar.add(statusTool);
		statusBar.addSeparator(STATUS_BAR_SEPERATOR_DIMENSION);
		statusBar.add(statusFPS);
		
		fileMenu  = new EditorFileMenu(this);
		buttonBar = new EditorButtonBar(this);

		JPanel drawPanelHolder = new JPanel();
		drawPanelHolder.setLayout(new BorderLayout());
		drawPanelHolder.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		drawPanelHolder.add(gamePanel.getPanel());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
//		frame.setJMenuBar(fileMenu);
//		frame.add(fileMenu, BorderLayout.NORTH);
		frame.setTitle("Map Editor");
		panel.add(buttonBar, BorderLayout.NORTH);
		panel.add(statusBar, BorderLayout.SOUTH);
		panel.add(toolbar, BorderLayout.WEST);
		panel.add(drawPanelHolder, BorderLayout.CENTER);
		
		frame.add(fileMenu, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		
		editor.toolbar.addToolsToToolbar(toolbar);
		for (LinkedAction action : fileMenu.actionMap.values())
			action.unifyLinks();
		
		refreshWindowTitle();
	}
	
	public void refreshWindowTitle() {
		String title = EDITOR_NAME + " - ";
		if (editor.currentFile != null)
			title += editor.currentFile.getName();
		else
			title += "<untitled>";
		if (editor.changesMade)
			title += "*";
		Main.frame.setTitle(title);
	}
	
	/** Start a test run. **/
	public void startTestRun() {
		testRunning = true;
		editor.control.testRun();
	}
	
	/** Stop the test run. **/
	public void stopTestRun() {
		testRunning = false;
		editor.control.clearAllEntities();
		editor.control.physics.clearBodies();
	}
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	public void update() {
		if (testRunning && Keyboard.escape.pressed()) {
//			stopTestRun();
			fileMenu.itemTestRun.doClick();
		}
		
		gamePanel.update();
		control.update();
		if (!testRunning)
			editor.update();
		
		
		DecimalFormat format = new DecimalFormat("0.00");
		statusFPS.setText("FPS: " + format.format(Main.averageFPS));
		
		Collection<LinkedAction> actionList = fileMenu.actionMap.values();
		for (LinkedAction action : actionList) {
			action.refreshEnabled();
		}
	}
	
	public void render(Graphics g) {	
		control.draw(g);
		if (!testRunning)
			editor.draw();
	}

	@Override
	public void draw() {
		gamePanel.drawBackground(Color.BLACK);
		Draw.setGraphics(gamePanel.getGraphics());
		HUD.setGraphics(gamePanel.getGraphics());
		render(gamePanel.getGraphics());
		gamePanel.repaint();
	}
	
	@Override
	public void initialize() {
		
	}
}
