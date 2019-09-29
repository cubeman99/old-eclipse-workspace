import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class FrameUI extends JFrame {
	private FileHandler fileHandler;
	
	private JButton buttonApply;
	private JButton buttonChooseColor;
	private JButton buttonDefault;
	private JTextField colorDisplayField;
	private JMenu jMenu1;
	private JMenu jMenu2;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;
	private JMenuItem jMenuItem1;
	private JScrollPane scrollPaneDescription;
	private JTextArea labelDescription;
	private JLabel labelValue;
	private JLabel labelVariable;
	private JList list;
	private DefaultListModel listModel;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JPanel panelDescription;
	private JPanel panelEdit;
	private JPanel panelValue;
	private JPanel panelVariable;
	private JScrollPane scrollPaneList;
	private JSplitPane splitPane;
	private JTextField valueInput; 

	
	public static void main(String args[]) {
		// Set the Look and Feel.
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Metal".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(FrameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(FrameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(FrameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(FrameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		// Create and display the form.
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new FrameUI().setVisible(true);
			}
		});
	}
	
	public FrameUI() {
		initComponents();
		fileHandler = new FileHandler();
		fileHandler.openFile();
		
		for (Option opt : fileHandler.getOptions()) {
			listModel.addElement(opt.getVariableTitle());
		}
		
		list.setSelectedIndex(0);
	}

	private void initComponents() {
		jMenu1 = new JMenu();
		jMenuBar1 = new JMenuBar();
		jMenu2 = new JMenu();
		jMenu3 = new JMenu();
		splitPane = new JSplitPane();
		panelEdit = new JPanel();
		panelVariable = new JPanel();
		labelVariable = new JLabel();
		panelDescription = new JPanel();
		labelDescription = new JTextArea();
		scrollPaneDescription = new JScrollPane();
		panelValue = new JPanel();
		labelValue = new JLabel();
		valueInput = new JTextField();
		colorDisplayField = new JTextField();
		buttonChooseColor = new JButton();
		buttonDefault = new JButton();
		buttonApply = new JButton();
		scrollPaneList = new JScrollPane();
		list = new JList();
		menuBar = new JMenuBar();
		menuFile = new JMenu();
		jMenuItem1 = new JMenuItem();

		jMenu1.setText("jMenu1");

		jMenu2.setText("File");
		jMenuBar1.add(jMenu2);

		jMenu3.setText("Edit");
		jMenuBar1.add(jMenu3);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("cube_man Skin Options - David Jordan");

		splitPane.setDividerLocation(160);
		splitPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		splitPane.setPreferredSize(new java.awt.Dimension(596, 436));

		panelVariable.setBorder(BorderFactory.createTitledBorder("Variable"));

		labelVariable.setText("COLOR_TEXT");

		GroupLayout panelVariableLayout = new GroupLayout(panelVariable);
		panelVariable.setLayout(panelVariableLayout);
		
		panelVariableLayout.setHorizontalGroup(
			panelVariableLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(panelVariableLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(labelVariable)
        			.addContainerGap())
		);
		panelVariableLayout.setVerticalGroup(
			panelVariableLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(panelVariableLayout.createSequentialGroup()
    				.addContainerGap()
        			.addComponent(labelVariable)
        			.addContainerGap())
		);
		
		panelDescription.setBorder(BorderFactory.createTitledBorder("Description"));
		
		labelDescription.setEditable(false);
		labelDescription.setFont(labelVariable.getFont());// new java.awt.Font("Tahoma", 0, 11)); // NOI18N
		labelDescription.setLineWrap(true);
        labelDescription.setRows(1);
        labelDescription.setText("");
        labelDescription.setWrapStyleWord(true);
        labelDescription.setFocusable(false);
//        labelDescription.setBa
        scrollPaneDescription.setViewportView(labelDescription);
		
		
        GroupLayout panelDescriptionLayout = new GroupLayout(panelDescription);
        panelDescription.setLayout(panelDescriptionLayout);
        
        panelDescriptionLayout.setHorizontalGroup(
            panelDescriptionLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, panelDescriptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneDescription)
                .addContainerGap())
        );
        panelDescriptionLayout.setVerticalGroup(
            panelDescriptionLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, panelDescriptionLayout.createSequentialGroup()
            	.addContainerGap()
                .addComponent(scrollPaneDescription, GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

		panelValue.setBorder(BorderFactory.createTitledBorder("Value"));

		labelValue.setText("255, 128, 0, 255");
		
		valueInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				valueInputActionPerformed(evt);
			}
		});

		colorDisplayField.setEditable(false);
		colorDisplayField.setBackground(new java.awt.Color(153, 255, 51));

		buttonChooseColor.setText("Choose Color");
		buttonChooseColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonChooseColorActionPerformed(evt);
			}
		});
		
		buttonDefault.setText("Set to Default");
		buttonDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonDefaultActionPerformed(evt);
			}
		});

		buttonApply.setText("Apply");
		buttonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonApplyActionPerformed(evt);
			}
		});
		
		GroupLayout panelValueLayout = new GroupLayout(panelValue);
		panelValue.setLayout(panelValueLayout);
		
		
		panelValueLayout.setHorizontalGroup(
    		panelValueLayout.createSequentialGroup()
    			.addContainerGap()
    			.addGroup(panelValueLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
    				.addGroup(GroupLayout.Alignment.TRAILING, panelValueLayout.createSequentialGroup()
    					.addComponent(buttonApply)
    					.addPreferredGap(ComponentPlacement.RELATED, 229, Short.MAX_VALUE)
    					.addComponent(buttonDefault)
    				)
    				.addComponent(buttonChooseColor, GroupLayout.Alignment.TRAILING)
    				.addComponent(colorDisplayField)
    				.addComponent(valueInput)
    				.addComponent(labelValue)
    			)
    			.addContainerGap()
		);
		
		panelValueLayout.setVerticalGroup(
			panelValueLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(panelValueLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(labelValue)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(valueInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(colorDisplayField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(buttonChooseColor)
				.addContainerGap(144, Short.MAX_VALUE)
			)
			
			.addGroup(GroupLayout.Alignment.TRAILING, panelValueLayout.createSequentialGroup()
				.addGroup(panelValueLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(buttonDefault)
					.addComponent(buttonApply)
				)
				.addContainerGap()
			)
		);
		

		GroupLayout panelEditLayout = new GroupLayout(panelEdit);
		panelEdit.setLayout(panelEditLayout);
		panelEditLayout.setHorizontalGroup(
				panelEditLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, panelEditLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(panelEditLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(panelValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelDescription, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelVariable, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap())
				);
		panelEditLayout.setVerticalGroup(
				panelEditLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, panelEditLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(panelVariable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(panelDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(panelValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addContainerGap())
				);

		splitPane.setRightComponent(panelEdit);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setMaximumSize(new java.awt.Dimension(100, 80));
		list.setMinimumSize(new java.awt.Dimension(100, 80));
		list.setPreferredSize(new java.awt.Dimension(100, 80));
		scrollPaneList.setViewportView(list);
		
		listModel = new DefaultListModel();
		list.setModel(listModel);
		list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                listValueChanged(evt);
            }
        });

		splitPane.setLeftComponent(scrollPaneList);

		menuFile.setText("File");

		jMenuItem1.setText("Exit");
		menuFile.add(jMenuItem1);

		menuBar.add(menuFile);

		setJMenuBar(menuBar);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(splitPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(splitPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
				);

		pack();
	}     

	private void listValueChanged(ListSelectionEvent evt) {
		if (evt.getValueIsAdjusting())
			return;
		
		refreshEditPanel();
	}
	
	private void refreshEditPanel() {
		Option opt = getOption();
		
		
		labelVariable.setText(opt.getVariableTitle());
		labelDescription.setText(opt.getDescription());
		labelValue.setText(opt.getValue());
		valueInput.setText(opt.getValue());
		
		if (opt.getColor() != null)
			colorDisplayField.setBackground(opt.getColor());
		
		colorDisplayField.setVisible(opt.isColor());
		buttonChooseColor.setVisible(opt.isColor());
	}
	
	private void buttonApplyActionPerformed(ActionEvent evt) {
		fileHandler.saveFile();
	}

	private void valueInputActionPerformed(ActionEvent evt) {
		labelValue.setText(valueInput.getText());
		getOption().setValue(valueInput.getText());
		refreshEditPanel();
		
	}   
	
	private void buttonDefaultActionPerformed(ActionEvent evt) {
		getOption().setToDefault();
		refreshEditPanel();
	}

	private void buttonChooseColorActionPerformed(ActionEvent evt) {
		JColorChooser cc = new JColorChooser();

		Color col = JColorChooser.showDialog(this, "Choose Color",
				colorDisplayField.getBackground());

		if (col != null) {
			colorDisplayField.setBackground(col);
			getOption().setColor(col);
			refreshEditPanel();
		}
	}   
	
	private Option getOption() {
		return fileHandler.getOption(list.getSelectionModel()
				.getMinSelectionIndex());
	}
}
