package OLD;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class EditPanel extends JComponent {
	
	public EditPanel() {
		initUI();
	}
	
	private void initUI() {
		setLayout(new GridBagLayout());
		
		JPanel gridPanel = new JPanel(new GridLayout(1, 2, 8, 8));
		
		gridPanel.add(new JButton("asd"));
		gridPanel.add(new JButton("sfhsf"));

		addComp(new JTextField(), 0, 0, 5, 1);
		addComp(gridPanel, 0, 1, 5, 1);
		
		addComp(new SquareButton("A"), 0, 2, 1, 1);
		addComp(new SquareButton("B"), 1, 2, 1, 1);
		addComp(new SquareButton("C"), 2, 2, 1, 1);
		addComp(new SquareButton("D"), 3, 2, 1, 1);
		addComp(new SquareButton("E"), 4, 2, 1, 1);
		
		addComp(new SquareButton("F"), 0, 3, 1, 1);
		addComp(new SquareButton("G"), 1, 3, 1, 1);
		addComp(new SquareButton("H"), 2, 3, 1, 1);
		addComp(new SquareButton("I"), 3, 3, 1, 1);
		addComp(new SquareButton("J"), 4, 3, 1, 1);
		
		addComp(new SquareButton("K"), 0, 4, 1, 1);
		addComp(new SquareButton("L"), 1, 4, 1, 1);
		addComp(new SquareButton("M"), 2, 4, 1, 1);
		addComp(new SquareButton("N"), 3, 4, 1, 1);
		addComp(new SquareButton("O"), 4, 4, 1, 1);
		
		resetFocusable(this);
		setFocusable(true);
	}
	
	private static void resetFocusable(Component component) {
		component.setFocusable(false);
		
		if (component instanceof Container) {
			for (Component c : ((Container) component).getComponents()) {
				resetFocusable(c);
			}
		}
	}

	private void addComp(Component comp, int gridX, int gridY, int gridWidth, int gridHeight) {
		add(comp, new GridBagConstraints(gridX, gridY, gridWidth, gridHeight,
				1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(4, 4, 4, 4), 0, 0));
	}
	
	private static class SquareButton extends JButton {
		private SquareButton(String text) {
			super(text);
			setMargin(new Insets(2, 0, 2, 0));
		}
		
		@Override
		public Dimension getMinimumSize() {
			Dimension result = super.getMinimumSize();
			
			if (result.width < result.height) {
				result.width = result.height;
			}
			
			return result;
		}
		
		@Override
		public Dimension getPreferredSize() {
			return getMinimumSize();
		}
	}
}
