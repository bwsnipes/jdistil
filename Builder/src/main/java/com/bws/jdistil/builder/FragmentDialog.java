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
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.bws.jdistil.builder.data.Attribute;
import com.bws.jdistil.builder.data.AttributeType;
import com.bws.jdistil.builder.data.Fragment;

public class FragmentDialog extends JDialog {

	private static final long serialVersionUID = -6359188224210147292L;

	public static final int OK_BUTTON = 1;
	public static final int CANCEL_BUTTON = 2;
	
	private Fragment fragment = null;

	private JTextField nameTextField = null;
	private JComboBox<String> parentComboBox = null;
	private JCheckBox isPaginationCheckBox = null;
	private JTextField pageSizeTextField = null;
	private JTable attributesTable = null;
	
	private JButton addButton = null;
	private JButton updateButton = null;
	private JButton deleteButton = null;
	private JButton upButton = null;
	private JButton downButton = null;
	
	private AttributeTableModel attributesTableModel = new AttributeTableModel();
	
	private AttributeDialog attributeDialog = null;
	
	private int buttonSelected = CANCEL_BUTTON;
	
	private List<String> availableFragmentNames = new ArrayList<String>();
	private List<String> availableCategoryNames = new ArrayList<String>();
	private List<String> availableAttributeNames = new ArrayList<String>();

	public FragmentDialog(JFrame parent) {
		super(parent, "Fragment", true);
		initialize(parent);
	}
	
	private void initialize(JFrame parent){
		
		// Create dialog
		attributeDialog = new AttributeDialog(parent);

		// Disable default closing of window
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// Handle system exit when window is closed
        addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent windowEvent){

				buttonSelected = CANCEL_BUTTON;
				setVisible(false);
			}        
		});
		
		// Create input components
		JLabel nameLabel = new JLabel("Name");
		nameTextField = new JTextField(20);
		
		JLabel parentLabel = new JLabel("Parent");
		parentComboBox = new JComboBox<String>();
		parentComboBox.setPreferredSize(new Dimension(300, (int)parentComboBox.getPreferredSize().getHeight()));
		
		JLabel isPaginationLabel = new JLabel("Pagination");
		isPaginationCheckBox = new JCheckBox();
		
		JLabel pageSizeLabel = new JLabel("Page Size");
		pageSizeTextField = new JTextField(5);
		  
		// Create top panel of window layout
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());

		//  Add input components to top panel
		topPanel.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(nameTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  
		topPanel.add(parentLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(parentComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		
		topPanel.add(isPaginationLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(isPaginationCheckBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 3, 0, 5), 0, 0));
		
		topPanel.add(pageSizeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(pageSizeTextField, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		
		// Create fragments table
		attributesTable = new JTable(attributesTableModel);
		attributesTable.setRowSelectionAllowed(true);
		attributesTable.setFillsViewportHeight(true);
		attributesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		attributesTable.getColumnModel().getColumn(0).setMinWidth(300);
		attributesTable.getColumnModel().getColumn(1).setMinWidth(300);
		attributesTable.getColumnModel().getColumn(2).setMinWidth(100);
		attributesTable.getColumnModel().getColumn(3).setMinWidth(100);
		attributesTable.getColumnModel().getColumn(4).setMinWidth(100);
		JScrollPane scrollPane = new JScrollPane(attributesTable);
		
		// Add listener to handle column resizing based on parent size
		attributesTable.getParent().addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(final ComponentEvent e) {
		        if (attributesTable.getPreferredSize().width < attributesTable.getParent().getWidth()) {
		        	attributesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		        } 
		        else {
		        	attributesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
		upButton = new JButton("Up");
		downButton = new JButton("Down");
		
		// Add button listeners
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Update reference data
				updateReferenceData(null);
				
				if (attributeDialog.open(null, availableAttributeNames, availableCategoryNames) == AttributeDialog.OK_BUTTON) {
					
					// Get new attribute
					Attribute attribute = attributeDialog.getData();
					
					// Add attribute to table data
					attributesTableModel.addRowData(attribute);
				}
			}
		});
		
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Get selected row
				int row = attributesTable.getSelectedRow();
				
				// Get selected attribute
				Attribute attribute = attributesTableModel.getRowData(row);

				// Update reference data
				updateReferenceData(attribute.getName());
				
				if (attributeDialog.open(attribute, availableAttributeNames, availableCategoryNames) == AttributeDialog.OK_BUTTON) {
					
					// Get update attribute
					Attribute updatedAttribute = attributeDialog.getData();
					
					// Update table data with updated attribute
					attributesTableModel.setRowData(row, updatedAttribute);
				}
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (JOptionPane.showConfirmDialog(deleteButton.getParent(), "Are you sure you want to delete the selected attributes?", 
						"Delete Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					attributesTableModel.deleteRowData(attributesTable.getSelectedRows());
				}
			}
		});
		
		upButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				attributesTableModel.moveRowUp(attributesTable.getSelectedRow());
			}
		});
		
		downButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				attributesTableModel.moveRowDown(attributesTable.getSelectedRow());
			}
		});
		
		// Add listener to handle enabling and disabling buttons based on rows selected
		attributesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

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
		buttonPanel.add(buttonSpacer, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(upButton, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, 
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(downButton, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, 
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
		
		// Create center panel of window layout using a titled border
		JPanel centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(1200, 600));
		centerPanel.setLayout(new GridBagLayout());

		// Get default font for panel component
		Font panelFont = UIManager.getDefaults().getFont("Panel.font");;

		// Set border title
		TitledBorder border = BorderFactory.createTitledBorder("Attributes");
		border.setTitleFont(new Font(panelFont.getFontName(), panelFont.getStyle(), 16));
		centerPanel.setBorder(border);
		
		// Add fragment table and button panel to center panel of layout
		centerPanel.add(scrollPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
		centerPanel.add(buttonPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		  
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

		// Create buttom panel of window layout
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
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(centerPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(bottomPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 15, 0), 0, 0));
		
		getContentPane().add(contentPanel);
		pack();
	}
	
	private void updateButtonStates() {
		
		// Get total number of rows selected
		int totalRowsSelected = attributesTable.getSelectionModel().getSelectedItemsCount();
		
		
		// Update button states based on number of rows selected
		updateButton.setEnabled(totalRowsSelected == 1);
		deleteButton.setEnabled(totalRowsSelected >= 1);
		upButton.setEnabled(totalRowsSelected == 1);
		downButton.setEnabled(totalRowsSelected == 1);
	}
	
	private void updateReferenceData(String excludedAttributeName) {

		// Clear existing values
		availableAttributeNames.clear();
		
		for (Attribute attribute : attributesTableModel.getRowData()) {
			
			// Get attribute name
			String attributeName = attribute.getName();
			
			if (excludedAttributeName == null  || !excludedAttributeName.equalsIgnoreCase(attributeName)) {
				
				// Add attribute name to available list
				availableAttributeNames.add(attribute.getName());
			}
			
			// Get category name
			String categoryName = attribute.getLookupCategory();
			
			if (categoryName != null && !availableCategoryNames.contains(categoryName)) {
				
				// Add any category name defined during the editing of this fragment to available list
				availableAttributeNames.add(categoryName);
			}
		}
	}
	
	public int open(Fragment fragment, List<String> fragmentNames, List<String> categoryNames) {
		
		setData(fragment, fragmentNames, categoryNames);
		updateButtonStates();
		setVisible(true);
		
		return buttonSelected;
	}
	
	public Fragment getData() {
		return fragment;
	}
	
	private void setData(Fragment data, List<String> fragmentNames, List<String> categoryNames) {

		populateReferenceData(fragmentNames, categoryNames);
		
		resetData();

		if (data == null) {
			
			// Set working fragment to a new instance
			fragment = new Fragment();
		}
		else {
			
			// Set working fragment using a copy of provide instance
			fragment = data.copy();

			// Populate components using fragment data
			nameTextField.setText(fragment.getName());
			parentComboBox.setSelectedItem(fragment.getParentName());
			isPaginationCheckBox.setSelected(fragment.getIsPaginationSupported());
			pageSizeTextField.setText(fragment.getPageSize() == null ? "" : fragment.getPageSize().toString());
			
			// Populate attribute table data
			attributesTableModel.addRowData(fragment.getAttributes());
		}

		// Set focus to first component
		nameTextField.requestFocusInWindow();
	}
	
	private void resetData() {
		
		// Clear current fragment
		this.fragment = null;
		
		// Reset selected button to default
		buttonSelected = CANCEL_BUTTON;
		
		// Reset components to initial state
		nameTextField.setText(null);
		parentComboBox.setSelectedIndex(-1);
		isPaginationCheckBox.setSelected(false);
		pageSizeTextField.setText(null);
		
		// Clear attribute table data
		attributesTableModel.clearData();
	}
	
	private void populateReferenceData(List<String> fragmentNames, List<String> categoryNames) {
		
		// Reset available fragment names
		availableFragmentNames.clear();
		availableFragmentNames.addAll(fragmentNames);
		
		// Reset available category names
		availableCategoryNames.clear();
		availableCategoryNames.addAll(categoryNames);
		
		// Clear parent fragment values
		parentComboBox.removeAllItems();
		
		// Populate parent fragment component with available values
		for (String fragmentName : fragmentNames) {
			parentComboBox.addItem(fragmentName);
		}
	}
	
	private boolean saveData() {
		
		boolean isSaved = false;
		
		if (isValidData()) {
			
			// Populate fragment with component data
			fragment.setName(nameTextField.getText());
			fragment.setParentName((String)parentComboBox.getSelectedItem());
			fragment.setIsPaginationSupported(isPaginationCheckBox.isSelected());
			fragment.setPageSize(convertIntegerValue(pageSizeTextField.getText()));
			
			// Populate fragment attributes using attribute table data
			fragment.setAttributes(attributesTableModel.getRowData());
			
			isSaved = true;
		}
		
		return isSaved;
	}
	
	private boolean isValidData() {
		
		List<String> errorMessages = new ArrayList<String>();
		
		if (nameTextField.getText() == null || nameTextField.getText().isBlank()) {
			errorMessages.add("Name is a required field.");
		}
		else if (availableFragmentNames.stream().anyMatch(nameTextField.getText()::equalsIgnoreCase)) {
			errorMessages.add("A fragment with this name is already defined.");
		}
		
		
		if (isPaginationCheckBox.isSelected()) {
			
			String pageSize = pageSizeTextField.getText();
			
			if (pageSize == null || pageSize.isBlank() || !pageSize.matches("\\d+")) {
				errorMessages.add("Page Size is a required field.");
			}
			else if (!pageSize.matches("\\d+")) {
				errorMessages.add("Page Size must be a valid number.");
			}
		}
		
		if (attributesTableModel.getRowCount() == 0) {
			errorMessages.add("At least one attribute must be defined.");
		}
		
		// Display errors
		if (!errorMessages.isEmpty()) {
			
			JOptionPane.showMessageDialog(this, errorMessages.toArray(), "Errors", JOptionPane.WARNING_MESSAGE);
		}
		
		return errorMessages.isEmpty();
	}
	
	private Integer convertIntegerValue(String text) {
		
		Integer value = null;
		
		if (text != null && !text.isBlank()) {
			value = Integer.valueOf(text);
		}
		
		return value;
	}

	
private class AttributeTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -4335357911581020384L;

	private String[] columnNames = {"<html><center>&nbsp;<br>Name<br>&nbsp;</center></html>", "Type", "Required", "<html><center>Included in<br>Search Filter</center></html>", "<html><center>Included in<br>Search Results</center></html>"};
    private List<Attribute> data = new ArrayList<Attribute>();

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
    
    private List<Attribute> getRowData() {
    	
    	return new ArrayList<Attribute>(data);
    }
    
   public Attribute getRowData(int row) {
    
    	Attribute attribute = null;
    	
    	if (row >= 0 && row < data.size()) {
    		
    		attribute = data.get(row);
    	}
    	
    	return attribute;
    }
    
    public void addRowData(Attribute attribute) {
    	
    	if (attribute != null) {
    		
    		data.add(attribute);

        	fireTableDataChanged();
    	}
    }
    
    public void addRowData(List<Attribute> attributes) {
    	
    	if (attributes != null) {
    		
    		data.addAll(attributes);

        	fireTableDataChanged();
    	}
    }
    
    public void setRowData(int row, Attribute attribute) {
    	
    	if (row >= 0 && row < data.size() && attribute != null) {
    		
    		data.set(row, attribute);

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
    
    public void moveRowUp(int row) {
    	
		if (row > 0 && row < data.size()) {
			
			int newRow = row - 1;
			
			Collections.swap(data, newRow, row);

			fireTableDataChanged();
			
			attributesTable.getSelectionModel().clearSelection();
			attributesTable.getSelectionModel().setSelectionInterval(newRow, newRow);
		}
    }

    public void moveRowDown(int row) {
    	
		if (row >= 0 && row < data.size() - 1) {
			
			int newRow = row + 1;
			
			Collections.swap(data, newRow, row);

			fireTableDataChanged();
			
			attributesTable.getSelectionModel().clearSelection();
			attributesTable.getSelectionModel().setSelectionInterval(newRow, newRow);
		}
    }

    public Object getValueAt(int row, int col) {
    	
    	String value = null;
    	
    	Attribute attribute = data.get(row);
    	
    	switch (col) {
    		case 0: 
    			value = attribute.getName();
     			break;
    		case 1: 
        		value = attribute.getType().toString();
 
       			if (attribute.getType().equals(AttributeType.TEXT)) {
    				value = value + " (" + attribute.getTextMaxLength() + ")";
       			}
       			else if (attribute.getType().equals(AttributeType.NUMERIC)) {
    				value = value + " (" + attribute.getNumericType() + ", " + attribute.getNumericPrecision() + ", " + attribute.getNumericScale() + ")";
       			}
       			else if (attribute.getType().equals(AttributeType.LOOKUP)) {
    				value = value + " (" + attribute.getLookupCategory() + ")";
       			}
        		break;
    		case 2: 
        		value = attribute.getIsRequired() ? "Yes" : "No";
    			break;
    		case 3: 
        		value = attribute.getIsRequired() ? "Yes" : "No";
    			break;
    		case 4: 
        		value = attribute.getIsIncludedInSearchFilter() ? "Yes" : "No";
    			break;
    		case 5: 
        		value = attribute.getIsIncludedInSearchResults() ? "Yes" : "No";
    			break;
    	}
    	
        return value;
    }

}

	
}
