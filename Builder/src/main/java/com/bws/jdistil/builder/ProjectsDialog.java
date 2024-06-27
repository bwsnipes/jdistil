package com.bws.jdistil.builder;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.bws.jdistil.builder.data.DataManager;
import com.bws.jdistil.builder.data.Project;

public class ProjectsDialog extends JDialog {

	private static final long serialVersionUID = 6501394604800214551L;

	public static final int OK_BUTTON = 1;
	public static final int CANCEL_BUTTON = 2;
	
	private Project project = null;
	
	private JComboBox<String> projectComboBox = null;
	
	private int buttonSelected = CANCEL_BUTTON;
	
	public ProjectsDialog(JFrame parent) {
		super(parent, "Open Project", true);
		initialize();
	}
	
	private void initialize(){
		
		// Create input components
		JLabel projectsLabel = new JLabel("Projects");
		projectComboBox = new JComboBox<String>();
		projectComboBox.setPreferredSize(new Dimension(300, (int)projectComboBox.getPreferredSize().getHeight()));
		
		// Create top panel of window layout
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());

		//  Add input components to top panel
		topPanel.add(projectsLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(projectComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		
		// Create ok and cancel buttons
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		
		// Add button listeners
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (saveData()) {
					
					buttonSelected = OK_BUTTON;
					setVisible(false);
				}
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				buttonSelected = CANCEL_BUTTON;
				setVisible(false);
			}
		});

		// Create button panel of window layout
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		
		// Create button spacers
		JLabel leftSpacer = new JLabel();
		JLabel rightSpacer = new JLabel();
		
		bottomPanel.add(leftSpacer, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		bottomPanel.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		bottomPanel.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		bottomPanel.add(rightSpacer, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

		// Create the content panel for dialog
		JPanel contentPanel = new JPanel();
		  
		contentPanel.setLayout(new GridBagLayout());

		// Add top, center and bottom panels of layout to fragment dialog
		contentPanel.add(topPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(bottomPanel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 15, 0), 0, 0));
		
		getContentPane().add(contentPanel);
		pack();
   }
	
	public int open() {
		
		populateReferenceData();
		resetData();
		setVisible(true);
		
		return buttonSelected;
	}
	
	public Project getData() {
		return project;
	}
	
	private void resetData() {
		
		// Clear current project
		this.project = null;
		
		// Reset selected button to default
		buttonSelected = CANCEL_BUTTON;
		
		// Reset components to initial state
		projectComboBox.setSelectedIndex(-1);
	}
	
	private void populateReferenceData() {
		
		// Retrieve project names
		List<String> projectNames = DataManager.getInstance().findProjectNames();
		
		if (projectNames != null) {
			
			// Clear project values
			projectComboBox.removeAllItems();
			
			// Populate project component with available values
			for (String projectName : projectNames) {
				projectComboBox.addItem(projectName);
			}
		}
	}
	
	private boolean saveData() {
		
		boolean isSaved = false;
		
		if (isValidData()) {
			
			String projectName = (String)projectComboBox.getSelectedItem();
			
			project = DataManager.getInstance().getProject(projectName);	
			
			isSaved = true;
		}
		
		return isSaved;
	}
	
	private boolean isValidData() {
		
		List<String> errorMessages = new ArrayList<String>();
		
		if (projectComboBox.getSelectedIndex() == -1) {
			errorMessages.add("Project is a required field.");
		}

		// Display errors
		if (!errorMessages.isEmpty()) {
			
			JOptionPane.showMessageDialog(this, errorMessages.toArray(), "Errors", JOptionPane.WARNING_MESSAGE);
		}
		
		return errorMessages.isEmpty();
	}
}