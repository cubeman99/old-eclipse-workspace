package OLD;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Java Swing Demo - by David Jordan");
		LookAndFeel.setFrame(frame);
		
		Container c = frame.getContentPane();
		c.add(new RainmeterPanel(frame));
		
		frame.setPreferredSize(new Dimension(480, 480));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.pack();
    	frame.setLocationRelativeTo(null);
    	frame.setResizable(true);
    	frame.setVisible(true);
		
//    	LookAndFeel.set(LookAndFeel.LAF_SYSTEM);
    	
		/*
		Scanner in = new Scanner(System.in);
		Control ctrl = new Control();
		
		ctrl.read();
//		ctrl.print();
		
		System.out.println("Enter color: ");
		String color = in.nextLine();
		
		ctrl.setKey("Variables", "COLOR_NORMAL", color);
		ctrl.save();
		ctrl.refreshSkins();
		*/
	}
}
