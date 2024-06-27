package com.bws.jdistil.builder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import com.bws.jdistil.builder.data.Attribute;
import com.bws.jdistil.builder.data.AttributeType;
import com.bws.jdistil.builder.data.NumericType;

public class AttributeDialog extends JDialog {

	private static final long serialVersionUID = 5184353095617566852L;

	public static final int OK_BUTTON = 1;
	public static final int CANCEL_BUTTON = 2;
	
	private Attribute attribute = null;
	
	private JTextField nameTextField = null;
	private JComboBox<AttributeType> typeComboBox = null;
	private JCheckBox isRequiredCheckBox = null;
	private JCheckBox isIncludedInSearchFilterCheckBox = null;
	private JCheckBox isIncludedInSearchResultsCheckBox = null;
	private JTextField maxLengthTextField = null;
	private JComboBox<NumericType> numericTypeComboBox = null;
	private JTextField precisionTextField = null;
	private JTextField scaleTextField = null;
	private JComboBox<String> categoryComboBox = null;
	private JCheckBox isMultipleValuesCheckBox = null;
	
	private int buttonSelected = CANCEL_BUTTON;
	
	private List<String> availableAttributeNames = new ArrayList<String>();

	public AttributeDialog(JFrame parent) {
		super(parent, "Attribute", true);
		initialize();
	}
	
	private void initialize(){
		
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
		
		JLabel typeLabel = new JLabel("Type");
		typeComboBox = new JComboBox<AttributeType>(AttributeType.values());
		typeComboBox.setPreferredSize(new Dimension(300, (int)typeComboBox.getPreferredSize().getHeight()));
		
		JLabel isRequiredLabel = new JLabel("Required");
		isRequiredCheckBox = new JCheckBox();
		
		JLabel isIncludedInSearchFilterLabel = new JLabel("Include in Search Filter");
		isIncludedInSearchFilterCheckBox = new JCheckBox();
		
		JLabel isIncludedInResultsFilterLabel = new JLabel("Include in Search Results");
		isIncludedInSearchResultsCheckBox = new JCheckBox();
		
		// Create top panel of window layout
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());

		//  Add input components to top panel
		topPanel.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(nameTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  
		topPanel.add(typeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(typeComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		
		topPanel.add(isRequiredLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(isRequiredCheckBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 3, 0, 5), 0, 0));

		topPanel.add(isIncludedInSearchFilterLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(isIncludedInSearchFilterCheckBox, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 3, 0, 5), 0, 0));

		topPanel.add(isIncludedInResultsFilterLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		topPanel.add(isIncludedInSearchResultsCheckBox, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 3, 0, 5), 0, 0));

		// Add type listener
		typeComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateComponentStates();
			}
		});
		
		// Create input components for text type attribute
		JLabel maxLengthLabel = new JLabel("Max Length");
		maxLengthTextField = new JTextField(10);
		
		// Create group panel for text type attribute components
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridBagLayout());

		// Get default font for panel component
		Font panelFont = UIManager.getDefaults().getFont("Panel.font");;

		// Set border title for text group panel
		TitledBorder textGroupBorder = BorderFactory.createTitledBorder("Text");
		textGroupBorder.setTitleFont(new Font(panelFont.getFontName(), panelFont.getStyle(), 14));
		textPanel.setBorder(textGroupBorder);
		
		// Add text type attribute components to text type group panel
		textPanel.add(maxLengthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		textPanel.add(maxLengthTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  

		// Create input components for numeric type attribute
		JLabel numericTypeLabel = new JLabel("Type");
		numericTypeComboBox = new JComboBox<NumericType>(NumericType.values());
		numericTypeComboBox.setPreferredSize(new Dimension(300, (int)numericTypeComboBox.getPreferredSize().getHeight()));
		
		JLabel precisionLabel = new JLabel("Precision");
		precisionTextField = new JTextField(10);

		JLabel scaleLabel = new JLabel("Scale");
		scaleTextField = new JTextField(10);

		// Create group panel for numeric type attribute components
		JPanel numericPanel = new JPanel();
		numericPanel.setLayout(new GridBagLayout());
		
		// Set border title for numeric group panel
		TitledBorder numericGroupBorder = BorderFactory.createTitledBorder("Numeric");
		numericGroupBorder.setTitleFont(new Font(panelFont.getFontName(), panelFont.getStyle(), 14));
		numericPanel.setBorder(numericGroupBorder);
		
		// Add numeric type attribute components to numeric type group panel
		numericPanel.add(numericTypeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		numericPanel.add(numericTypeComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  
		numericPanel.add(precisionLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		numericPanel.add(precisionTextField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		
		numericPanel.add(scaleLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		numericPanel.add(scaleTextField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 3, 0, 5), 0, 0));

		
		// Create input components for lookup type attribute
		JLabel categoryLabel = new JLabel("Category");
		categoryComboBox = new JComboBox<String>();
		categoryComboBox.setEditable(true);
		categoryComboBox.setPreferredSize(new Dimension(300, (int)categoryComboBox.getPreferredSize().getHeight()));
		
		JLabel isMultipleValuesLabel = new JLabel("Multiple Values");
		isMultipleValuesCheckBox = new JCheckBox();

		// Create group panel for lookup type attribute components
		JPanel lookupPanel = new JPanel();
		lookupPanel.setLayout(new GridBagLayout());
		
		// Set border title for lookup group panel
		TitledBorder lookupGroupBorder = BorderFactory.createTitledBorder("Lookup");
		lookupGroupBorder.setTitleFont(new Font(panelFont.getFontName(), panelFont.getStyle(), 14));
		lookupPanel.setBorder(lookupGroupBorder);
		
		// Add lookup type attribute components to lookup type group panel
		lookupPanel.add(categoryLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		lookupPanel.add(categoryComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  
		lookupPanel.add(isMultipleValuesLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		lookupPanel.add(isMultipleValuesCheckBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
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
		contentPanel.add(textPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(numericPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(lookupPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(bottomPanel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 15, 0), 0, 0));
		
		getContentPane().add(contentPanel);
		pack();
   }
	
	public int open(Attribute attribute, List<String> attributeNames, List<String> categoryNames) {
		
		setData(attribute, attributeNames, categoryNames);
		setVisible(true);
		
		return buttonSelected;
	}
	
	public Attribute getData() {
		return attribute;
	}
	
	private void setData(Attribute data, List<String> attributeNames, List<String> categoryNames) {

		populateReferenceData(attributeNames, categoryNames);

		resetData();

		if (data == null) {
			
			// Set working attribute to a new instance
			attribute = new Attribute();
		}
		else {
			
			// Set working attribute using a copy of provide instance
			attribute = data.copy();

			// Populate components using attribute data
			nameTextField.setText(attribute.getName());
			typeComboBox.setSelectedItem(attribute.getType());
			isRequiredCheckBox.setSelected(attribute.getIsRequired());
			isIncludedInSearchFilterCheckBox.setSelected(attribute.getIsIncludedInSearchFilter());
			isIncludedInSearchResultsCheckBox.setSelected(attribute.getIsIncludedInSearchResults());
			maxLengthTextField.setText(ensureStringValue(attribute.getTextMaxLength()));
			numericTypeComboBox.setSelectedItem(ensureStringValue(attribute.getNumericType()));
			precisionTextField.setText(ensureStringValue(attribute.getNumericPrecision()));
			scaleTextField.setText(ensureStringValue(attribute.getNumericScale()));
			categoryComboBox.setSelectedItem(attribute.getLookupCategory());
			isMultipleValuesCheckBox.setSelected(attribute.getIsLookupMultipleValues());
		}

		// Set focus to first component
		nameTextField.requestFocusInWindow();
	}
	
	private void resetData() {
		
		// Clear current attribute
		this.attribute = null;
		
		// Reset selected button to default
		buttonSelected = CANCEL_BUTTON;
		
		// Reset components to initial state
		nameTextField.setText(null);
		typeComboBox.setSelectedItem(AttributeType.TEXT);
		isRequiredCheckBox.setSelected(false);
		isIncludedInSearchFilterCheckBox.setSelected(false);
		isIncludedInSearchResultsCheckBox.setSelected(false);
		maxLengthTextField.setText(null);
		numericTypeComboBox.setSelectedItem(0);
		precisionTextField.setText(null);
		scaleTextField.setText(null);
		categoryComboBox.setSelectedItem(null);
		isMultipleValuesCheckBox.setSelected(false);
	}
	
	private void populateReferenceData(List<String> attributeNames, List<String> categoryNames) {
		
		// Reset available attribute names
		availableAttributeNames.clear();
		availableAttributeNames.addAll(attributeNames);
		
		// Clear category values
		categoryComboBox.removeAllItems();
		
		// Populate category component with available values
		for (String categoryName : categoryNames) {
			categoryComboBox.addItem(categoryName);
		}
	}
	
	private boolean saveData() {
		
		boolean isSaved = false;
		
		if (isValidData()) {
			
			// Populate attribute with component data
			attribute.setName(nameTextField.getText());
			attribute.setType((AttributeType)typeComboBox.getSelectedItem());
			attribute.setIsRequired(isRequiredCheckBox.isSelected());
			attribute.setIsIncludedInSearchFilter(isIncludedInSearchFilterCheckBox.isSelected());
			attribute.setIsIncludedInSearchResults(isIncludedInSearchResultsCheckBox.isSelected());
			attribute.setTextMaxLength(convertIntegerValue(maxLengthTextField.getText()));
			attribute.setNumericType((NumericType)numericTypeComboBox.getSelectedItem());
			attribute.setNumericPrecision(convertIntegerValue(precisionTextField.getText()));
			attribute.setNumericScale(convertIntegerValue(scaleTextField.getText()));
			attribute.setLookupCategory((String)categoryComboBox.getSelectedItem());
			attribute.setIsLookupMultipleValues(isMultipleValuesCheckBox.isSelected());
			
			isSaved = true;
		}
		
		return isSaved;
	}
	
	private boolean isValidData() {
		
		List<String> errorMessages = new ArrayList<String>();
		
		if (nameTextField.getText() == null || nameTextField.getText().isBlank()) {
			errorMessages.add("Name is a required field.");
		}
		else if (availableAttributeNames.stream().anyMatch(nameTextField.getText()::equalsIgnoreCase)) {
			errorMessages.add("An attribute with this name is already defined.");
		}
		
		if (typeComboBox.getSelectedIndex() == -1) {
			errorMessages.add("Type is a required field.");
		}
		else {
			
			AttributeType type = (AttributeType)typeComboBox.getSelectedItem();
			
			if (type.equals(AttributeType.TEXT)) {
				
				String maxLength = maxLengthTextField.getText();
				
				if (maxLength == null || maxLength.isBlank() || !maxLength.matches("\\d+")) {
					errorMessages.add("Max Length is a required field.");
				}
				else if (!maxLength.matches("\\d+")) {
					errorMessages.add("Max Length must be a valid number.");
				}
			}
			else if (type.equals(AttributeType.NUMERIC)) {
				
				if (numericTypeComboBox.getSelectedIndex() == -1) {
					errorMessages.add("Numeric Type is a required field.");
				}

				String precision = precisionTextField.getText();

				if (precision == null || precision.isBlank() || !precision.matches("\\d+")) {
					errorMessages.add("Precision is a required field.");
				}
				else if (!precision.matches("\\d+")) {
					errorMessages.add("Precision must be a valid number.");
				}

				String scale = scaleTextField.getText();

				if (scale == null || scale.isBlank() || !scale.matches("\\d+")) {
					errorMessages.add("Scale is a required field.");
				}
				else if (!scale.matches("\\d+")) {
					errorMessages.add("Scale must be a valid number.");
				}
			}
			else if (type.equals(AttributeType.LOOKUP)) {
				
				if (categoryComboBox.getSelectedIndex() == -1 && categoryComboBox.getSelectedItem() == null) {
					errorMessages.add("Category is a required field.");
				}
			}
		}

		// Display errors
		if (!errorMessages.isEmpty()) {
			
			JOptionPane.showMessageDialog(this, errorMessages.toArray(), "Errors", JOptionPane.WARNING_MESSAGE);
		}
		
		return errorMessages.isEmpty();
	}
	
	private void updateComponentStates() {
	
		// Clear attribute specific component values
		maxLengthTextField.setText(null);
		numericTypeComboBox.setSelectedIndex(0);
		precisionTextField.setText(null);
		scaleTextField.setText(null);
		categoryComboBox.setSelectedIndex(-1);
		isMultipleValuesCheckBox.setSelected(false);

		// Disable
		maxLengthTextField.setEnabled(false);
		numericTypeComboBox.setEnabled(false);
		precisionTextField.setEnabled(false);
		scaleTextField.setEnabled(false);;
		categoryComboBox.setEnabled(false);;
		isMultipleValuesCheckBox.setEnabled(false);

		AttributeType type = (AttributeType)typeComboBox.getSelectedItem();
		
		if (type != null) {
			
			if (type.equals(AttributeType.TEXT)) {
				maxLengthTextField.setEnabled(true);
			}
			else if (type.equals(AttributeType.NUMERIC)) {
				
				numericTypeComboBox.setEnabled(true);
				precisionTextField.setEnabled(true);
				scaleTextField.setEnabled(true);
			}
			else if (type.equals(AttributeType.LOOKUP)) {
				
				categoryComboBox.setEnabled(true);;
				isMultipleValuesCheckBox.setEnabled(true);
			}
		}
	}
	
	private String ensureStringValue(Object object) {
		
		String value = null;
		
		if (object != null) {
			value = object.toString();
		}
		
		return value;
	}
	private Integer convertIntegerValue(String text) {
		
		Integer value = null;
		
		if (text != null && !text.isBlank()) {
			value = Integer.valueOf(text);
		}
		
		return value;
	}
}
