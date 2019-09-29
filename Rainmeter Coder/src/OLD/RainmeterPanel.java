package OLD;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;


public class RainmeterPanel extends JPanel {
    public final static boolean shouldFill    = true;
    public final static boolean shouldWeightX = true;
    public final static boolean RIGHT_TO_LEFT = false;
	public JFrame frame;
	
	public RainmeterPanel(JFrame frame) {
		this.frame = frame;
		setOpaque(false);
		setLayout(new BorderLayout());
		
		
		
		
		JSplitPane splitPane = new JSplitPane();
		
		DefaultListModel listModel = new DefaultListModel();
		for (int i = 1; i <= 40; i++) 
			listModel.addElement("Option " + i);
		JList list = new JList(listModel);
//		list.setPreferredSize(new Dimension(400, 0));
//		list.setMaximumSize(new Dimension(400, 0));
//		listScrollPane.setLayout(new BorderLayout());
		
		JPanel panelEdit = new JPanel(new GridLayout());
		
		
		
		JPanel panelDescription = new JPanel();
		panelDescription.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		panelDescription.setLayout(new BoxLayout(panelDescription, BoxLayout.Y_AXIS));
		panelDescription.add(new JLabel("Description:"));
		panelDescription.add(new JLabel("asadsf adsfa sdf"));
		
		panelEdit.setLayout(new BoxLayout(panelEdit, BoxLayout.Y_AXIS));
		panelEdit.add(panelDescription);
		panelEdit.add(new JLabel("Description:"));
		panelEdit.add(new JLabel("asadsf adsfa sdf"));
//		
		JTextField textField = new JTextField("TEXT FIELD!!!", 20);
		textField.setSize(new Dimension(24, 24));
		textField.setMinimumSize(new Dimension(300, 24));
		textField.setMaximumSize(new Dimension(300, 24));
		
		panelEdit.add(textField);
		panelEdit.add(new JButton("Button 1"));
		panelEdit.add(new JButton("Button 2"));
		
		JButton btn = new JButton("Refresh");
//		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelEdit.add(btn);

		
		
		JScrollPane listScrollPane = new JScrollPane(list);
		splitPane.setLeftComponent(listScrollPane);
		splitPane.setRightComponent(new EditPanel());
		add(splitPane);
		
		
		
		createMenuBar();
	}
	
	private JPanel initUI() {
		JPanel pane = new JPanel();
		
		JButton button;
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			//natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
		}

		button = new JButton("Button 1");
		if (shouldWeightX) {
			c.weightx = 0.5;
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(button, c);

		button = new JButton("Button 2");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(button, c);

		button = new JButton("Button 3");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		pane.add(button, c);

		button = new JButton("Long-Named Button 4");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;      //make this component tall
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(button, c);

		button = new JButton("5");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(10,0,0,0);  //top padding
	    c.gridx = 1;       //aligned with button 2
	    c.gridwidth = 2;   //2 columns wide
	    c.gridy = 2;       //third row
	    pane.add(button, c);
	    
	    return pane;
	}
	
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		menuFile.add("Open");
		menuFile.add("Save");
		menuFile.add("Exit");
		menuBar.add(menuFile);

		JMenu menuEdit = new JMenu("Edit");
		menuEdit.add("Open");
		menuEdit.add("Save");
		menuEdit.add("Exit");
		menuBar.add(menuEdit);
		
		frame.setJMenuBar(menuBar);
	}
}
