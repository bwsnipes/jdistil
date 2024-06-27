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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.bws.jdistil.builder.data.AssociationType;
import com.bws.jdistil.builder.data.Relationship;

public class RelationshipDialog extends JDialog {

	private static final long serialVersionUID = 4503174984731503181L;
	
	public static final int OK_BUTTON = 1;
	public static final int CANCEL_BUTTON = 2;
	
	private Relationship relationship = null;

	private JComboBox<String> sourceFragmentComboBox = null;
	private JComboBox<String> sourceAttributeComboBox = null;
	private JCheckBox isSourceIncludedInViewCheckBox = null;
	private JComboBox<AssociationType> associationComboBox = null;
	private JCheckBox isBidirectionalCheckBox = null;
	private JComboBox<String> targetFragmentComboBox = null;
	private JComboBox<String> targetAttributeComboBox = null;
	private JCheckBox isTargetRequiredCheckBox = null;
	private JCheckBox isTargetIncludedInViewCheckBox = null;

	private int buttonSelected = CANCEL_BUTTON;
	
	private Set<String> availableRelationshipNames = new HashSet<String>();

	private Map<String, List<String>> availableAttributeNames = new HashMap<String, List<String>>();

	public RelationshipDialog(JFrame parent) {
		super(parent, "Relationship", true);
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
		
		// Create source input components
		JLabel sourceFragmentLabel = new JLabel("Fragment");
		sourceFragmentComboBox = new JComboBox<String>();
		sourceFragmentComboBox.setPreferredSize(new Dimension(300, (int)sourceFragmentComboBox.getPreferredSize().getHeight()));
		
		JLabel sourceAtrributetLabel = new JLabel("Attribute");
		sourceAttributeComboBox = new JComboBox<String>();
		sourceAttributeComboBox.setPreferredSize(new Dimension(300, (int)sourceFragmentComboBox.getPreferredSize().getHeight()));

		JLabel isSourceIncludedInViewLabel = new JLabel("Included in View");
		isSourceIncludedInViewCheckBox = new JCheckBox();

		// Create group panel for source components
		JPanel sourcePanel = new JPanel();
		sourcePanel.setLayout(new GridBagLayout());

		// Get default font for panel component
		Font panelFont = UIManager.getDefaults().getFont("Panel.font");;

		// Set border title for source group panel
		TitledBorder sourceGroupBorder = BorderFactory.createTitledBorder("Source");
		sourceGroupBorder.setTitleFont(new Font(panelFont.getFontName(), panelFont.getStyle(), 14));
		sourcePanel.setBorder(sourceGroupBorder);
		
		// Add text type attribute components to text type group panel
		sourcePanel.add(sourceFragmentLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		sourcePanel.add(sourceFragmentComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  
		sourcePanel.add(sourceAtrributetLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		sourcePanel.add(sourceAttributeComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));

		sourcePanel.add(isSourceIncludedInViewLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		sourcePanel.add(isSourceIncludedInViewCheckBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));

		
		sourceFragmentComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Clear source attribute names
				sourceAttributeComboBox.removeAllItems();
				
				// Get selected source fragment name
				String sourceFragmentName = (String)sourceFragmentComboBox.getSelectedItem();
				
				if (sourceFragmentName != null) {
					
					// Get attribute names associated with the selected fragment
					List<String> attributeNames = availableAttributeNames.get(sourceFragmentName);
					
					if (attributeNames != null) {

						// Add attribute names to component
						for (String attributeName : attributeNames) {
							sourceAttributeComboBox.addItem(attributeName);
						}
					}
				}
				
				sourceAttributeComboBox.setSelectedIndex(-1);
			}
		});
		
		// Create input components for configuration type attribute
		JLabel associationLabel = new JLabel("Association");
		associationComboBox = new JComboBox<AssociationType>(AssociationType.values());
		associationComboBox.setPreferredSize(new Dimension(300, (int)associationComboBox.getPreferredSize().getHeight()));
		
		JLabel isBidirectionalLabel = new JLabel("Bidirectional");
		isBidirectionalCheckBox = new JCheckBox();

		// Create group panel for configuration components
		JPanel configurationPanel = new JPanel();
		configurationPanel.setLayout(new GridBagLayout());
		
		// Set border title for configuration group panel
		TitledBorder configurationGroupBorder = BorderFactory.createTitledBorder("Configuration");
		configurationGroupBorder.setTitleFont(new Font(panelFont.getFontName(), panelFont.getStyle(), 14));
		configurationPanel.setBorder(configurationGroupBorder);
		
		// Add numeric type attribute components to numeric type group panel
		configurationPanel.add(associationLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		configurationPanel.add(associationComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  
		configurationPanel.add(isBidirectionalLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		configurationPanel.add(isBidirectionalCheckBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));

		associationComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Clear and disable bidirectional checkbox
				isBidirectionalCheckBox.setSelected(false);
				isBidirectionalCheckBox.setEnabled(false);
				
				// Get selected association type
				AssociationType associationType = (AssociationType)associationComboBox.getSelectedItem();
				
				// Enable bidirectional checkbox if association is many-to-many
				if (associationType != null && associationType.equals(AssociationType.MANY_TO_MANY)) {
					isBidirectionalCheckBox.setEnabled(true);
				}
			}
		});
		
		isBidirectionalCheckBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
				if (isBidirectionalCheckBox.isSelected()) {
					
					// Enable source UI components
					sourceAttributeComboBox.setEnabled(true);
					isSourceIncludedInViewCheckBox.setEnabled(true);
				}
				else {
					
					sourceAttributeComboBox.setSelectedIndex(-1);
					isSourceIncludedInViewCheckBox.setSelected(false);

					// Disable source UI components
					sourceAttributeComboBox.setEnabled(false);
					isSourceIncludedInViewCheckBox.setEnabled(false);
				}
			}
		});
		
		// Create target input components
		JLabel targetFragmentLabel = new JLabel("Fragment");
		targetFragmentComboBox = new JComboBox<String>();
		targetFragmentComboBox.setPreferredSize(new Dimension(300, (int)targetFragmentComboBox.getPreferredSize().getHeight()));
		
		JLabel targetAtrributetLabel = new JLabel("Attribute");
		targetAttributeComboBox = new JComboBox<String>();
		targetAttributeComboBox.setPreferredSize(new Dimension(300, (int)targetFragmentComboBox.getPreferredSize().getHeight()));

		JLabel isTargetRequiredLabel = new JLabel("Required");
		isTargetRequiredCheckBox = new JCheckBox();

		JLabel isTargetIncludedInViewLabel = new JLabel("Included in View");
		isTargetIncludedInViewCheckBox = new JCheckBox();

		// Create group panel for target components
		JPanel targetPanel = new JPanel();
		targetPanel.setLayout(new GridBagLayout());

		// Set border title for target group panel
		TitledBorder targetGroupBorder = BorderFactory.createTitledBorder("Target");
		targetGroupBorder.setTitleFont(new Font(panelFont.getFontName(), panelFont.getStyle(), 14));
		targetPanel.setBorder(targetGroupBorder);
		
		// Add text type attribute components to text type group panel
		targetPanel.add(targetFragmentLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		targetPanel.add(targetFragmentComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		  
		targetPanel.add(targetAtrributetLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		targetPanel.add(targetAttributeComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));

		targetPanel.add(isTargetRequiredLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		targetPanel.add(isTargetRequiredCheckBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));

		targetPanel.add(isTargetIncludedInViewLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		targetPanel.add(isTargetIncludedInViewCheckBox, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));

		targetFragmentComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Clear target attribute names
				targetAttributeComboBox.removeAllItems();
				
				// Get selected target fragment name
				String targetFragmentName = (String)targetFragmentComboBox.getSelectedItem();
				
				if (targetFragmentName != null) {
					
					// Get attribute names associated with the selected fragment
					List<String> attributeNames = availableAttributeNames.get(targetFragmentName);
					
					if (attributeNames != null) {

						// Add attribute names to component
						for (String attributeName : attributeNames) {
							targetAttributeComboBox.addItem(attributeName);
						}
					}
				}
				
				targetAttributeComboBox.setSelectedIndex(-1);
			}
		});
		
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
		contentPanel.add(sourcePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(configurationPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(targetPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
		contentPanel.add(bottomPanel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 15, 0), 0, 0));
		
		getContentPane().add(contentPanel);
		pack();
   }
	
	public int open(Relationship relationship, Set<String> relationshipNames, List<String> fragmentNames, Map<String, List<String>> attributeNames) {
		
		setData(relationship, relationshipNames, fragmentNames, attributeNames);
		setVisible(true);
		
		return buttonSelected;
	}
	
	public Relationship getData() {
		return relationship;
	}
	
	private void setData(Relationship data, Set<String> relationshipNames, List<String> fragmentNames, Map<String, List<String>> attributeNames) {

		populateReferenceData(relationshipNames, fragmentNames, attributeNames);

		resetData();

		if (data == null) {
			
			// Set working relationship to a new instance
			relationship = new Relationship();
		}
		else {
			
			// Set working relationship using a copy of provide instance
			relationship = data.copy();

			// Populate components using relationship data
			sourceFragmentComboBox.setSelectedItem(relationship.getSourceFragmentName());
			sourceAttributeComboBox.setSelectedItem(relationship.getSourceAttributeName());
			isSourceIncludedInViewCheckBox.setSelected(relationship.getIsSourceIncludedInView());
			associationComboBox.setSelectedItem(relationship.getAssociation());;
			isBidirectionalCheckBox.setSelected(relationship.getIsBidirectional());
			targetFragmentComboBox.setSelectedItem(relationship.getTargetFragmentName());
			targetAttributeComboBox.setSelectedItem(relationship.getTargetAttributeName());
			isTargetRequiredCheckBox.setSelected(relationship.getIsTargetRequired());
			isTargetIncludedInViewCheckBox.setSelected(relationship.getIsTargetIncludedInView());
		}

		// Set focus to first component
		sourceFragmentComboBox.requestFocusInWindow();
	}
	
	private void resetData() {
		
		// Clear current relationship
		this.relationship = null;
		
		// Reset selected button to default
		buttonSelected = CANCEL_BUTTON;
		
		// Reset components to initial state
		sourceFragmentComboBox.setSelectedItem(null);
		sourceAttributeComboBox.setSelectedItem(null);
		isSourceIncludedInViewCheckBox.setSelected(false);
		associationComboBox.setSelectedItem(AssociationType.MANY_TO_ONE);
		isBidirectionalCheckBox.setSelected(false);
		targetFragmentComboBox.setSelectedItem(null);
		targetAttributeComboBox.setSelectedItem(null);
		isTargetRequiredCheckBox.setSelected(false);
		isTargetIncludedInViewCheckBox.setSelected(false);
	}
	
	private void populateReferenceData(Set<String> relationshipNames, List<String> fragmentNames, Map<String, List<String>> attributeNames) {
		
		// Reset available relationship names
		availableRelationshipNames.clear();
		availableRelationshipNames.addAll(relationshipNames);
		
		// Clear fragment values
		sourceFragmentComboBox.removeAllItems();
		targetFragmentComboBox.removeAllItems();
		
		// Populate fragment components with available values
		for (String fragmentName : fragmentNames) {
			sourceFragmentComboBox.addItem(fragmentName);
			targetFragmentComboBox.addItem(fragmentName);
		}

		// Reset available attribute names
		availableAttributeNames.clear();
		availableAttributeNames.putAll(attributeNames);
	}
	
	private boolean saveData() {
		
		boolean isSaved = false;
		
		if (isValidData()) {
			
			// Populate relationship with component data
			relationship.setSourceFragmentName((String)sourceFragmentComboBox.getSelectedItem());
			relationship.setSourceAttributeName((String)sourceAttributeComboBox.getSelectedItem());
			relationship.setIsSourceIncludedInView(isSourceIncludedInViewCheckBox.isSelected());
			relationship.setAssociation((AssociationType)associationComboBox.getSelectedItem());
			relationship.setIsBidirectional(isBidirectionalCheckBox.isSelected());
			relationship.setTargetFragmentName((String)targetFragmentComboBox.getSelectedItem());
			relationship.setTargetAttributeName((String)targetAttributeComboBox.getSelectedItem());
			relationship.setIsTargetRequired(isTargetRequiredCheckBox.isSelected());
			relationship.setIsTargetIncludedInView(isTargetIncludedInViewCheckBox.isSelected());
			
			isSaved = true;
		}
		
		return isSaved;
	}
	
	private boolean isValidData() {
		
		List<String> errorMessages = new ArrayList<String>();
		
		if (sourceFragmentComboBox.getSelectedIndex() == -1) {
			errorMessages.add("Source fragment is a required field.");
		}
		if (isBidirectionalCheckBox.isSelected() && sourceAttributeComboBox.getSelectedIndex() == -1) {
			errorMessages.add("Source attribute is a required field.");
		}
  		if (associationComboBox.getSelectedIndex() == -1) {
			errorMessages.add("Association is a required field.");
		}
		if (targetFragmentComboBox.getSelectedIndex() == -1) {
			errorMessages.add("Target fragment is a required field.");
		}
		if (targetAttributeComboBox.getSelectedIndex() == -1) {
			errorMessages.add("Target attribute is a required field.");
		}

		if (sourceFragmentComboBox.getSelectedIndex() != -1 && targetFragmentComboBox.getSelectedIndex() != -1) {

			// Get source and target fragment names
			String sourceFragmentName = (String)sourceFragmentComboBox.getSelectedItem();
			String targetFragmentName = (String)targetFragmentComboBox.getSelectedItem();
			
			if (sourceFragmentName.equalsIgnoreCase(targetFragmentName)) {
				errorMessages.add("Source fragment and target fragment cannot be the same.");
			}

			// Create reference name
			String referenceName = Relationship.createReferenceName(sourceFragmentName, targetFragmentName);

			if (availableRelationshipNames.contains(referenceName)) {
				errorMessages.add("A relationship already exists with this source fragment and attribute.");
			}
		}
		
		// Display errors
		if (!errorMessages.isEmpty()) {
			
			JOptionPane.showMessageDialog(this, errorMessages.toArray(), "Errors", JOptionPane.WARNING_MESSAGE);
		}
		
		return errorMessages.isEmpty();
	}
	
}
