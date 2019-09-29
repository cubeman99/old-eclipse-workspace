package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LookAndFeel {
	public static final int LAF_METAL     = 0;
	public static final int LAF_NIMBUS    = 1;
	public static final int LAF_CDE_MOTIF = 2;
	public static final int LAF_SYSTEM    = 3;
	public static final int LAF_WINDOWS   = 4;
	
	
	/** Set the Look and Feel of the frame with a look and feel constant. **/
	public static void set(int lookAndFeelIndex) {
		if (lookAndFeelIndex == LAF_METAL)
			setLAF("javax.swing.plaf.metal.MetalLookAndFeel");
		else if (lookAndFeelIndex == LAF_NIMBUS)
			setLAF("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		else if (lookAndFeelIndex == LAF_CDE_MOTIF)
			setLAF("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		else if (lookAndFeelIndex == LAF_WINDOWS)
			setLAF("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		else if (lookAndFeelIndex == LAF_SYSTEM)
			setLAF(UIManager.getSystemLookAndFeelClassName());
	}
	
	/** Set the look and feel with a string of the path. **/
	private static void setLAF(String path) {
		try {
			UIManager.setLookAndFeel(path);
			SwingUtilities.updateComponentTreeUI(Main.frame);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
