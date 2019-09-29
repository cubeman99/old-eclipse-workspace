import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;


public class Game extends JPanel implements Runnable {
	private static final int FPS = 1000 / 120;
	private static Thread thread;
	public static SandControl sand;
	
	private volatile boolean running = false;
	
	public static JColorChooser colorChooser;
	
	public Game() {
		JFrame frame = new JFrame("Sand - By David Jordan");
		
    	Container c = frame.getContentPane();
    	c.setLayout(new BorderLayout());
    	//c.setLayout(new FlowLayout(FlowLayout.LEADING, 4, 4));
    	//c.setLayout(new GridLayout(2, 2));
    	
    	sand = new SandControl();
    	//c.add(sand);
    	JButton buttonColorChoose = new JButton("Change Color");
    	//c.add(buttonColorChoose);
    	
    	colorChooser = new JColorChooser();
    	//colorChooser.setPreferredSize(new Dimension(128, 128));
    	//colorChooser.setMaximumSize(new Dimension(128, 128));
    	
    	JToolBar toolbar1 = new JToolBar(1);
    	toolbar1.add(new JButton("Normal"));
    	toolbar1.addSeparator(new Dimension(8, 8));
    	toolbar1.add(new JButton("Hard"));
    	
    	
    	
    	JToolBar toolbar2 = new JToolBar(1);
    	toolbar2.setPreferredSize(new Dimension(120, 512));
    	//toolbar2.setLayout(new BorderLayout());
    	JScrollPane colorScrollpane = new JScrollPane(colorChooser);
    	
    	colorScrollpane.setPreferredSize(new Dimension(123, 123));
    	//colorChooser.setPreferredSize(new Dimension(123, 123));
    	
    	toolbar2.setFloatable(false);
    	
    	toolbar2.add(colorScrollpane);
    	toolbar2.add(new JButton("Normal"));
    	
    	//toolbar2.
    	
    	//c.add(toolbar2, BorderLayout.EAST);
    	
    	JPanel panelLeft = new JPanel();
    	panelLeft.setLayout(new BorderLayout());
    	panelLeft.add(initToolbar(), BorderLayout.WEST);
    	panelLeft.add(new JScrollPane(sand), BorderLayout.CENTER);
    	
    	JSplitPane splitpane = new JSplitPane(1, panelLeft, toolbar2);
    	splitpane.setDividerSize(4);
    	splitpane.setMaximumSize(new Dimension(256, 64));
    	splitpane.setBorder(new BevelBorder(BevelBorder.RAISED));
    	c.add(splitpane);
    	
    	
    	buttonColorChoose.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Close the Window
				sand.choosing_color = true;
				sand.color_window = new ColorWindow();
				//SandControl.colorSand = Color.blue;
			}
		});
    	
    	
    	frame.setPreferredSize(new Dimension(640, 640));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(true);
    	frame.setVisible(true);
    	
    	//ColorWindow col = new ColorWindow();
    	
    	thread = new Thread(this);
    	thread.start();
	}
	
	public static JToolBar initToolbar() {
		JToolBar toolbar = new JToolBar(1);
		//toolbar.setLayout(new BorderLayout());
		toolbar.add(new JLabel("Tools"));
		
		JButton btn_tool_eraser = new JButton("Eraser");
		btn_tool_eraser.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Close the Window
				sand.setTool(SandControl.TOOL_ERASER);
			}
		});
		
		JButton btn_tool_static = new JButton("Block");
		btn_tool_static.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Close the Window
				sand.setTool(SandControl.TOOL_STATIC);
			}
		});
		
		JButton btn_tool_sand = new JButton("Sand");
		btn_tool_sand.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Close the Window
				sand.setTool(SandControl.TOOL_SAND);
			}
		});
		
		toolbar.add(btn_tool_eraser);
		toolbar.add(btn_tool_static);
		toolbar.add(btn_tool_sand);
		
    	
		return toolbar;
	}
	
	public static void main(String [] args) {
    	new Game();
	}

	public void run() {
		// Remember the starting time
    	long tm = System.currentTimeMillis();
    	
		running = true;
		while( running ) {
			sand.update();
			
			try {
				tm += FPS;
                Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			}
			catch(InterruptedException e) {
				//System.out.println(e);
			}
		}
		System.exit(0);
	} // end of run()
}
