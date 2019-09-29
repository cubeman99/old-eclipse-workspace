import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuBar;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MainFrame extends JPanel {
	public MandelbrotSet mandelbrot;
	public JSpinner spinnerIterations;
	
	public MainFrame() {
		
		
		JFrame frame = new JFrame("Mandelbrot Explorer - By David Jordan");
		
    	Container c = frame.getContentPane();
    	//c.setLayout(new BorderLayout());
    	c.setLayout(new FlowLayout(FlowLayout.LEADING, 4, 4));
    	
    	mandelbrot = new MandelbrotSet();
    	c.add(mandelbrot);
    	spinnerIterations = new JSpinner();
    	spinnerIterations.setValue(100);
    	c.add(spinnerIterations);
    	c.add(new JButton("Generate"));
    	
    	JMenuBar menubar = new JMenuBar();
    	JMenu menuFile = new JMenu("File");
    	menubar.add(menuFile);
    	menuFile.add("Exit").addMouseListener(new MouseListener() {
    		public void mouseClicked(MouseEvent e) {
    		}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
    	});
    	JMenu menuEdit = new JMenu("Edit");
    	menuEdit.add("Properties");
    	menuEdit.add("Hello");
    	menubar.add(menuFile);
    	menubar.add(menuEdit);
    	
    	frame.setJMenuBar(menubar);
    	
    	spinnerIterations.addChangeListener(new ChangeListener() {
    			public void stateChanged(ChangeEvent e) {
    				MandelbrotSet.MaxIterations = (Integer) spinnerIterations.getValue();
    				mandelbrot.fractalCreate();
					mandelbrot.repaint();
    			}
    	});
    	
    	frame.setPreferredSize(new Dimension(1000, 800));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setResizable(true);
    	frame.setVisible(true);
	}
	
	public static void main(String [] args) {
    	new MainFrame();
	}
}
