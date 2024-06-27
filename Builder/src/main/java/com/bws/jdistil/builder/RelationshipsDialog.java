package com.bws.jdistil.builder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.bws.jdistil.builder.data.Project;
import com.bws.jdistil.builder.data.Relationship;

public class RelationshipsDialog extends JDialog {

	private static final long serialVersionUID = -6837746223877966980L;

	private Project project = null;
	
	private JTable relationshipsTable = null;
	
	private JButton addButton = null;
	private JButton updateButton = null;
	private JButton deleteButton = null;

	private RelationshipTableModel relationshipsTableModel = new RelationshipTableModel();

	private RelationshipDialog relationshipDialog = null;
	
	private List<String> availableFragmentNames = new ArrayList<String>();
	
	private Set<String> availableRelationshipNames = new HashSet<String>();
	private Map<String, List<String>> availableAttributeNames = new HashMap<String, List<String>>();
	
	public RelationshipsDialog(JFrame parent) {
		super(parent, "Relationships", true);
		initialize(parent);
	}
	
	private void initialize(JFrame parent){
		
		// Create dialog
		relationshipDialog = new RelationshipDialog(parent);
		
		// Disable default closing of window
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Handle system exit when window is closed
        addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent windowEvent){
				
				saveData();
				setVisible(false);
			}        
		});
		
		// Create relationships table
		relationshipsTable = new JTable(relationshipsTableModel);
		relationshipsTable.setRowSelectionAllowed(true);
		relationshipsTable.setFillsViewportHeight(true);
		relationshipsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		relationshipsTable.getColumnModel().getColumn(0).setMinWidth(300);
		relationshipsTable.getColumnModel().getColumn(1).setMinWidth(300);
		relationshipsTable.getColumnModel().getColumn(2).setMinWidth(100);
		relationshipsTable.getColumnModel().getColumn(3).setMinWidth(200);
		relationshipsTable.getColumnModel().getColumn(4).setMinWidth(100);
		relationshipsTable.getColumnModel().getColumn(5).setMinWidth(300);
		relationshipsTable.getColumnModel().getColumn(6).setMinWidth(300);
		relationshipsTable.getColumnModel().getColumn(7).setMinWidth(100);
		relationshipsTable.getColumnModel().getColumn(8).setMinWidth(100);
		JScrollPane scrollPane = new JScrollPane(relationshipsTable);
		
		// Add listener to handle column resizing based on parent size
		relationshipsTable.getParent().addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(final ComponentEvent e) {
		        if (relationshipsTable.getPreferredSize().width < relationshipsTable.getParent().getWidth()) {
		        	relationshipsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		        } 
		        else {
		        	relationshipsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		        }
		    }
		});

		// Create button panel of window layout
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		 
		// Create buttons used to edit fragment data
		addButton = new JButton("Add...");
		updateButton = new JButton("Update...");
		deleteButton = new JButton("Delete");
		
		// Add button listeners
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Update reference data
				updateReferenceData(null);
				
				if (relationshipDialog.open(null, availableRelationshipNames, availableFragmentNames, availableAttributeNames) == RelationshipDialog.OK_BUTTON) {
					
					// Get new relationship
					Relationship relationship = relationshipDialog.getData();
					
					// Add relationship to table data
					relationshipsTableModel.addRowData(relationship);
				}
			}
		});
		
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Get selected row
				int row = relationshipsTable.getSelectedRow();
				
				// Get selected relationship
				Relationship relationship = relationshipsTableModel.getRowData(row);

				// Update reference data
				updateReferenceData(relationship);
				
				if (relationshipDialog.open(relationship, availableRelationshipNames, availableFragmentNames, availableAttributeNames) == RelationshipDialog.OK_BUTTON) {
					
					// Get update relationship
					Relationship updatedRelationship = relationshipDialog.getData();
					
					// Update table data with updated relationship
					relationshipsTableModel.setRowData(row, updatedRelationship);
				}
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (JOptionPane.showConfirmDialog(deleteButton.getParent(), "Are you sure you want to delete the selected relationship?", 
						"Delete Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					relationshipsTableModel.deleteRowData(relationshipsTable.getSelectedRows());
				}
			}
		});
		
		// Add listener to handle enabling and disabling buttons based on rows selected
		relationshipsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				updateButtonStates();
			}
			
		});
		
		// Create button spacer
		JLabel buttonSpacer = new JLabel();
		buttonSpacer.setPreferredSize(new Dimension(5, 25));
		
		// Add buttons to button panel
		buttonPanel.add(addButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(updateButton, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(deleteButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		
		// Create center panel of window layout using a titled border
		JPanel centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(1200, 600));
		centerPanel.setLayout(new GridBagLayout());

		// Get default font for panel component
		Font panelFont = UIManager.getDefaults().getFont("Panel.font");;

		// Set border title
		TitledBorder border = BorderFactory.createTitledBorder("Relationships");
		border.setTitleFont(new Font(panelFont.getFontName(), panelFont.getStyle(), 16));
		centerPanel.setBorder(border);
		
		// Add fragment table and button panel to center panel of layout
		centerPanel.add(scrollPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
		centerPanel.add(buttonPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// Create close button
		JButton closeButton = new JButton("Close");
		
		// Add button listeners
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				saveData();
				setVisible(false);
			}
		});

		// Create buttom panel of window layout
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		
		// Create button spacers
		JLabel leftSpacer = new JLabel();
		JLabel rightSpacer = new JLabel();
		
		bottomPanel.add(leftSpacer, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		bottomPanel.add(closeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		bottomPanel.add(rightSpacer, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

		// Create the content panel for dialog
		JPanel contentPanel = new JPanel();
		  
		contentPanel.setLayout(new GridBagLayout());

		// Add top, center and bottom panels of layout to fragment dialog
		contentPanel.add(centerPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(bottomPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 15, 0), 0, 0));
		
		getContentPane().add(contentPanel);
		pack();
   }
	
	private void updateButtonStates() {
		
		// Get total number of rows selected
		int totalRowsSelected = relationshipsTable.getSelectionModel().getSelectedItemsCount();
		
		// Update button states based on number of rows selected
		updateButton.setEnabled(totalRowsSelected == 1);
		deleteButton.setEnabled(totalRowsSelected >= 1);
	}
	
	private void updateReferenceData(Relationship excludedRelationship) {

		// Clear existing values
		availableRelationshipNames.clear();
		
		for (Relationship relationship : relationshipsTableModel.getRowData()) {
			
			if (excludedRelationship == null  || !excludedRelationship.equals(relationship)) {
				
				// Add relationship to available relationship names
				addAvailableRelationship(relationship);
			}
		}
	}
	
	public void open(Project project, List<String> fragmentNames, Map<String, List<String>> attributeNames) {
		
		setData(project, fragmentNames, attributeNames);
		updateButtonStates();
		setVisible(true);
	}
	
	private void setData(Project data, List<String> fragmentNames, Map<String, List<String>> attributeNames) {

		populateReferenceData(data, fragmentNames, attributeNames);
		
		resetData();

		// Populate relationships table data
		relationshipsTableModel.addRowData(data.getRelationships());

		project = data;
	}
	
	private void resetData() {
		
		// Clear relationships table data
		relationshipsTableModel.clearData();
	}
	
	private void populateReferenceData(Project project, List<String> fragmentNames, Map<String, List<String>> attributeNames) {
		
		// Reset available fragment names
		availableFragmentNames.clear();
		availableFragmentNames.addAll(fragmentNames);
		
		// Reset available attribute names
		availableAttributeNames.clear();
		availableAttributeNames.putAll(attributeNames);
		
		// Clear available relationship names
		availableRelationshipNames.clear();
		
		if (project != null && project.getRelationships() != null) {
		
			for (Relationship relationship : project.getRelationships()) {
				
				// Add relationship to available relationships lookup
				addAvailableRelationship(relationship);
			}
		}		
	}
	
	private void addAvailableRelationship(Relationship relationship) {
		
		if (relationship != null) {
			
			// Get source and target fragment names
			String sourceFragmentName = relationship.getSourceFragmentName();
			String targetFragmentName = relationship.getTargetFragmentName();
			
			// Add both sides of relationship to available relationships lookup
			availableRelationshipNames.add(Relationship.createReferenceName(sourceFragmentName, targetFragmentName));
			availableRelationshipNames.add(Relationship.createReferenceName(targetFragmentName, sourceFragmentName));
		}		
	}
	
	private void saveData() {
		
		project.setRelationships(relationshipsTableModel.getRowData());
	}


private class RelationshipTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -2426428307608749406L;

	private String[] columnNames = {"<html><center>&nbsp;<br>Source<br>Fragment<br>&nbsp;</center></html>", "<html><center>Source<br>Attribute</center></html>", "<html><center>Source<br>Included In View</center></html>", "Association", "Bidirectional", "<html><center>Target<br>Fragment</center></html>", "<html><center>Target<br>Attribute</center></html>", "<html><center>Target<br>Required</center></html>", "<html><center>Target<br>Included In View</center></html>"};
    private List<Relationship> data = new ArrayList<Relationship>();

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class<?> getColumnClass(int col) {
        return String.class;
    }

    public boolean isCellEditable(int row, int col) {
    	return false;
    }

    public void clearData() {
    	
    	data.clear();
    	
    	fireTableDataChanged();
    }
    
    private List<Relationship> getRowData() {
    	
    	return new ArrayList<Relationship>(data);
    }
    
   public Relationship getRowData(int row) {
    
	   Relationship relationship = null;
    	
    	if (row >= 0 && row < data.size()) {
    		
    		relationship = data.get(row);
    	}
    	
    	return relationship;
    }
    
    public void addRowData(Relationship relationship) {
    	
    	if (relationship != null) {
    		
    		data.add(relationship);

        	fireTableDataChanged();
    	}
    }
    
    public void addRowData(List<Relationship> relationships) {
    	
    	if (relationships != null) {
    		
    		data.addAll(relationships);

        	fireTableDataChanged();
    	}
    }
    
    public void setRowData(int row, Relationship relationship) {
    	
    	if (row >= 0 && row < data.size() && relationship != null) {
    		
    		data.set(row, relationship);

        	fireTableDataChanged();
    	}
    }
    
    public void deleteRowData(int[] rows) {
    	
    	if (rows != null) {
    		
    		List<Integer> sortedRows = Arrays.stream(rows).boxed().sorted().toList().reversed();
    		
    		for (Integer row : sortedRows) {
    			
    	    	if (row >= 0 && row < data.size()) {
    	    		
    	    		data.remove(row.intValue());
    	    	}
    		}
    		
        	fireTableDataChanged();
    	}
    }

    public Object getValueAt(int row, int col) {
    	
    	String value = null;
    	
    	Relationship relationship = data.get(row);
    	
    	switch(col) {
    	case 0:
    		value = relationship.getSourceFragmentName();
    		break;
    	case 1:
    		value = relationship.getSourceAttributeName();
    		break;
    	case 2:
    		value = relationship.getIsSourceIncludedInView() ? "Yes" : "No";
    		break;
    	case 3:
    		value = relationship.getAssociation().toString();
    		break;
    	case 4:
    		value = relationship.getIsBidirectional() ? "Yes" : "No";
    		break;
    	case 5:
    		value = relationship.getTargetFragmentName();
    		break;
    	case 6:
    		value = relationship.getTargetAttributeName();
    		break;
    	case 7:
    		value = relationship.getIsTargetRequired() ? "Yes" : "No";
    		break;
    	case 8:
    		value = relationship.getIsTargetIncludedInView() ? "Yes" : "No";
    		break;
    	}
    	
        return value;
    }

}
	
}
