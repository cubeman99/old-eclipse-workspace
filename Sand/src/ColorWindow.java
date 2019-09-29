import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class ColorWindow {
	public JFrame frame;
	public JButton buttonOK;
	public JButton buttonCancel;
	public JColorChooser colorChooser;
	public JPanel panelButtons;
	
	public boolean closed = false;
	
	public ColorWindow() {
		// Initiate Components
		frame        = new JFrame("Choose a color");
		
		colorChooser = new JColorChooser();
		
		buttonOK     = new JButton("OK");
		buttonOK.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Close the Window
				closed = true;
				frame.dispose();
			}
		});
		
    	buttonCancel = new JButton("Cancel");
    	buttonCancel.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Close the Window
				closed = true;
				frame.dispose();
			}
		});
    	
    	
    	
    	panelButtons = new JPanel();
    	//panelButtons.setLayout(new BorderLayout());
    	panelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING, 8, 8));
    	panelButtons.add(buttonOK, BorderLayout.WEST);
    	panelButtons.add(buttonCancel, BorderLayout.EAST);
    	
    	// Fill Frame with content
    	Container c = frame.getContentPane();
    	c.setLayout(new BorderLayout());
    	
    	
    	c.add(colorChooser, BorderLayout.NORTH);
    	c.add(panelButtons, BorderLayout.SOUTH);
    	
    	frame.pack();
    	frame.setResizable(false);
    	frame.setVisible(true);
	}
	
	public Color getColor() {
		Color col = colorChooser.getColor();
		if( closed ) {
			//frame.dispose();
		}
		if( col == null )
			return Color.white;
		return col;
	}
	
	/*public Color waitUntilClose() {
		while( true ) {
			Color col = colorChooser.getColor();
			if( )
			return col;
		}
	}*/
}
