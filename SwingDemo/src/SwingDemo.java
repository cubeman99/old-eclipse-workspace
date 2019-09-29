import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;


public class SwingDemo extends JPanel {
	public static JFrame frame;
	
	public static void main(String[] args) {
		frame = new JFrame("Java Swing Demo - by David Jordan");
		
		Container c = frame.getContentPane();
		c.add(new SwingDemo());
		
		
		frame.setPreferredSize(new Dimension(480, 480));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setLocationRelativeTo(null);
    	frame.setResizable(true);
    	frame.setVisible(true);
	}
	
	public SwingDemo() {
		setOpaque(false);
		//setPreferredSize(new Dimension(128, 128));
		
		
		//initLookAndFeel("system", "");
//		initLookAndFeel("motif", "");
//		initLookAndFeel("nimbus", "");
//		initLookAndFeel("metal", "ocean");
		
		
		JScrollPane btnpane = new JScrollPane();
		
		GridLayout g = new GridLayout(2, 2);
		g.setHgap(4);
		g.setVgap(4);
		
		setLayout(g);
		
		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		JMenu menu = new JMenu("File");
		menubar.add(menu);
		menubar.add(new JButton("Hello!"));
		menu.add("Open");
		menu.add("Save");
		menu.add("Close");
		menu.add(new JButton("Menu Button Suprise!!"));
		menu.add(new JCheckBox("Check me."));
		menu.add(new JCheckBox("No, check me!"));
		menu.add(new JCheckBox("Don't check me."));
		JMenu menu2 = new JMenu("Scrollbar Suprise");
		menubar.add(menu2);
		menu.setToolTipText("this is a list you dumbass");
		JScrollPane treepane = new JScrollPane(createTree());
		treepane.setMinimumSize(new Dimension(256, 512));
		menu2.add(treepane);
		JMenu menu3 = new JMenu("Help me");
		menu.add(menu3);
		menu3.add("never");
		
		BevelBorder border = new BevelBorder(BevelBorder.LOWERED);
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
		btnPanel.setBorder(new TitledBorder(
				border,
				"Border Title",
				TitledBorder.LEFT,
				TitledBorder.ABOVE_TOP));
		btnPanel.setBorder(new TitledBorder("Border Title"));
		btnPanel.setLayout(new BorderLayout());
		
		btnPanel.add(new JButton("East"), BorderLayout.EAST);
		btnPanel.add(new JButton("West"), BorderLayout.WEST);
		btnPanel.add(new JButton("North"), BorderLayout.NORTH);
		btnPanel.add(new JButton("Center"), BorderLayout.CENTER);
		btnPanel.add(new JButton("South"), BorderLayout.SOUTH);
		
		JScrollPane btnScrollPane = new JScrollPane();
		btnScrollPane.add(btnPanel);
		add(new JScrollPane(btnPanel));
		//ImageIcon img = new ImagesLoader();
		//add(new JScrollPane(new ImageIcon().getImage()));
		JPanel tlbrPanel = new JPanel();
		tlbrPanel.setLayout(new GridLayout(2, 2));

		
		JToolBar tlbr = new JToolBar();
		tlbr.add(new JButton("Button"));
		tlbr.add(new JButton("Button2"));
		tlbr.add(new JTextField("TEXT FIELD!!!", 20));
		tlbrPanel.add(tlbr);
		
		tlbrPanel.add(new JTextField("Hello"));
		
		add(tlbrPanel);
		
		add(new JScrollPane(createTree()));
		
		JPanel chkbxPanel = new JPanel();
		chkbxPanel.add(new JCheckBox("Checkbox", true));
		chkbxPanel.add(new JCheckBox("Checkbox", true));
		chkbxPanel.add(new JCheckBox("Checkbox", true));
		add(chkbxPanel);
	}
	
	public JTree createTree() {
		DefaultMutableTreeNode[] nodes;
		int nodesSize = 100;
		int nodesIndex = 1;
		nodes = new DefaultMutableTreeNode[nodesSize];
		for( int i = 0; i < nodesSize; i += 1 )
			nodes[i] = new DefaultMutableTreeNode("");
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Top");
		nodes[0] = top;
		
		URL url = getClass().getResource("tree.txt");
		
		try {
			FileReader fr = new FileReader("src/tree.txt");
			BufferedReader input =  new BufferedReader(fr);
			
			try {
				String line = input.readLine();
				boolean end = false;
				
				while( line != null ) {
					if( line.charAt(0) == '#' ) {
						line = input.readLine();
						continue;
					}
					
					int i = 0;
					for( ; i < line.length() - 1; i++ ) {
						char linechar = line.charAt(i);
						if( linechar != ' ' ) {
							break;
						}
					}
					DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(line.substring(i));
					
					if( i >= nodesIndex - 1 ) {
						nodes[nodesIndex] = dmtn;
						nodesIndex += 1;
					}
					if( i < nodesIndex - 1 ) {
						for( int j = nodesIndex - 1; j > i; j-- )
							nodes[j - 1].add(nodes[j]);
						nodesIndex = i + 2;
						nodes[i + 1] = dmtn;
					}
					line = input.readLine();
				}
				for( int j = nodesIndex - 1; j > 0; j-- )
					nodes[j - 1].add(nodes[j]);
				
				input.close();
			}
			catch( IOException e ) {
				e.fillInStackTrace();
			}
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
		
		JTree tree = new JTree(nodes[0]);
		return tree;
	}
	
	public boolean initLookAndFeel(String type, String theme) {
		String typ = type.toLowerCase();
		String thm = theme.toLowerCase();
		String lookAndFeel = null;
		
		if( typ.equals("metal") ) {
			lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
		}
		else if( typ.equals("system") ) {
			lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			
		}
		else if( typ.equals("motif") ) {
			 lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		}
		else if( typ.equals("nimbus") ) {
			lookAndFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
		}
		else
			return false;
		
		try {
			UIManager.setLookAndFeel(lookAndFeel);
			if( typ.equals("metal") ) {
				if (thm.equals("defaultmetal"))
					MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
				else if (thm.equals("ocean"))
					MetalLookAndFeel.setCurrentTheme(new OceanTheme());
				//else
					//lookAndFeel.setCurrentTheme(new TestTheme());
				
				UIManager.setLookAndFeel(new MetalLookAndFeel()); 
			}	
        } 
		catch( ClassNotFoundException e ) {}
		catch( UnsupportedLookAndFeelException e ) {}
		catch( Exception e ) {}

		
		return true;
	}
}
