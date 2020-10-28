package com.bws.jdistil.project.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bws.jdistil.project.fragment.data.Attribute;
import com.bws.jdistil.project.fragment.data.AttributeTypes;
import com.bws.jdistil.project.fragment.data.BooleanAttribute;
import com.bws.jdistil.project.fragment.data.DateAttribute;
import com.bws.jdistil.project.fragment.data.EmailAttribute;
import com.bws.jdistil.project.fragment.data.LookupAttribute;
import com.bws.jdistil.project.fragment.data.MemoAttribute;
import com.bws.jdistil.project.fragment.data.NumericAttribute;
import com.bws.jdistil.project.fragment.data.NumericValueTypes;
import com.bws.jdistil.project.fragment.data.PhoneNumberAttribute;
import com.bws.jdistil.project.fragment.data.PostalCodeAttribute;
import com.bws.jdistil.project.fragment.data.TextAttribute;
import com.bws.jdistil.project.fragment.data.TimeAttribute;
import com.bws.jdistil.project.util.TextConverter;
import com.bws.jdistil.project.validation.ResourceNameValidator;

/**
 * Dialog used to add and edit attribute data.
 */
public class AttributeDialog extends Dialog {
  
  /**
   * Attribute data.
   */
  private Attribute attribute;

  /**
   * Attribute name text.
   */
  private Text nameText;
  
  /**
   * Attribute type label.
   */
  private Label typeLabel;
  
  /**
   * Attribute type combo.
   */
  private Combo typeCombo;
  
  /**
   * Required indicator label.
   */
  private Label requiredLabel;

  /**
   * Required indicator button.
   */
  private Button requiredButton;
  
  /**
   * Max length label.
   */
  private Label maxLengthLabel;
  
  /**
   * Max length text.
   */
  private Text maxLengthText;
  
  /**
   * Numeric value type.
   */
  private Label valueTypeLabel;
  
  /**
   * Numeric value type combo.
   */
  private Combo valueTypeCombo;
  
  /**
   * Numeric precision label.
   */
  private Label precisionLabel;
  
  /**
   * Numeric precision text.
   */
  private Text precisionText;
  
  /**
   * Numeric scale label.
   */
  private Label scaleLabel;
  
  /**
   * Numeric scale text.
   */
  private Text scaleText;
  
  /**
   * Lookup category label.
   */
  private Label categoryLabel;
  
  /**
   * Lookup category combo.
   */
  private Combo categoryCombo;
  
  /**
   * Multiple values label.
   */
  private Label multipleValuesLabel;
  
  /**
   * Multiple values button.
   */
  private Button multipleValuesButton;
  
  /**
   * Selected dialog button.
   */
  private int selectedButton = SWT.CANCEL;
  
  /**
   * Map of category IDs keyed by category names.
   */
  private Map<String, Integer> categoryLookup = new HashMap<String, Integer>();
  
  /**
   * Resource name validator.
   */
  private ResourceNameValidator resourceNameValidator = new ResourceNameValidator();

  /**
   * Creates a new attribute dialog using a parent shell.
   * @param parent Parent of this dialog.
   * @param categories Map of category names keyed by category ID.
   */
  public AttributeDialog(Shell parent, Map<Integer, String> categories) {
    super(parent);
    
    if (categories != null) {
      
      for (Map.Entry<Integer, String> category : categories.entrySet()) {
        
        // Get category ID and formatted category name
        Integer categoryId = category.getKey();
        String categoryName = TextConverter.convertConstantToCommon(category.getValue());
        
        // Add category data to lookup
        this.categoryLookup.put(categoryName, categoryId);
      }
    }
  }

  /**
   * Creates the attribute dialog controls.
   * @param shell Dialog window.
   */
  private void createContents(final Shell shell) {
    
    // Set layout
    shell.setLayout(new GridLayout(2, false));

    // Create name label
    Label nameLabel = new Label(shell, SWT.NONE);
    GridData nameLabelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    //nameLabelGridData.widthHint = 30;
    nameLabel.setLayoutData(nameLabelGridData);
    nameLabel.setText("Name:");

    // Create name text
    nameText = new Text(shell, SWT.BORDER);
    nameText.setTextLimit(30);
    nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    
    // Create type label
    typeLabel = new Label(shell, SWT.NONE);
    typeLabel.setText("Type:");
    GridData typeLabelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    //typeLabelGridData.widthHint = 30;
    typeLabel.setLayoutData(typeLabelGridData);

    // Create type list
    typeCombo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
    typeCombo.add(AttributeTypes.TEXT);
    typeCombo.add(AttributeTypes.NUMERIC);
    typeCombo.add(AttributeTypes.BOOLEAN);
    typeCombo.add(AttributeTypes.DATE);
    typeCombo.add(AttributeTypes.TIME);
    typeCombo.add(AttributeTypes.MEMO);
    typeCombo.add(AttributeTypes.LOOKUP);
    typeCombo.add(AttributeTypes.EMAIL);
    typeCombo.add(AttributeTypes.PHONE_NUMBER);
    typeCombo.add(AttributeTypes.POSTAL_CODE);
    GridData typeGridData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
    typeGridData.widthHint = 100;
    typeCombo.setLayoutData(typeGridData);
    typeCombo.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        
        // Update dialog view
        updateDialogView();
      }
    });
    
    // Create type label
    requiredLabel = new Label(shell, SWT.NONE);
    requiredLabel.setText("Required:");
    GridData requiredLabelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    requiredLabel.setLayoutData(requiredLabelGridData);

    // Create required button
    requiredButton = new Button(shell, SWT.CHECK);
    requiredButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
    
    // Create text attribute group
    Group textGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
    textGroup.setText("Text");
    textGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    textGroup.setLayout(new GridLayout(2, false));

    // Create max length label
    maxLengthLabel = new Label(textGroup, SWT.NONE);
    maxLengthLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    maxLengthLabel.setText("Max Length:");

    // Create max length text
    maxLengthText = new Text(textGroup, SWT.BORDER);
    GridData maxLengthTextGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    maxLengthTextGridData.widthHint = 20;
    maxLengthText.setLayoutData(maxLengthTextGridData);

    // Create numeric attribute group
    Group numericGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
    numericGroup.setText("Numeric");
    numericGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    numericGroup.setLayout(new GridLayout(2, false));

    // Create value type label
    valueTypeLabel = new Label(numericGroup, SWT.NONE);
    valueTypeLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    valueTypeLabel.setText("Type:");
    
    // Create value type combo
    valueTypeCombo = new Combo(numericGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
    valueTypeCombo.add(NumericValueTypes.ANY);
    valueTypeCombo.add(NumericValueTypes.POSITIVE);
    valueTypeCombo.add(NumericValueTypes.NEGATIVE);
    GridData valueTypeGridData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
    valueTypeGridData.widthHint = 100;
    valueTypeCombo.setLayoutData(valueTypeGridData);
    
    // Create precision label
    precisionLabel = new Label(numericGroup, SWT.NONE);
    precisionLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    precisionLabel.setText("Precision:");

    // Create precision text
    precisionText = new Text(numericGroup, SWT.BORDER);
    GridData precisionTextGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    precisionTextGridData.widthHint = 20;
    precisionText.setLayoutData(precisionTextGridData);

    // Create scale label
    scaleLabel = new Label(numericGroup, SWT.NONE);
    scaleLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    scaleLabel.setText("Scale:");

    // Create scale text
    scaleText = new Text(numericGroup, SWT.BORDER);
    GridData scaleTextGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    scaleTextGridData.widthHint = 20;
    scaleText.setLayoutData(scaleTextGridData);

    // Create lookup attribute group
    Group lookupGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
    lookupGroup.setText("Lookup");
    lookupGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    lookupGroup.setLayout(new GridLayout(2, false));

    // Create category label
    categoryLabel = new Label(lookupGroup, SWT.NONE);
    categoryLabel.setText("Category:");
    categoryLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

    // Create category list
    categoryCombo = new Combo(lookupGroup, SWT.DROP_DOWN);
    GridData categoryGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    categoryGridData.widthHint = 100;
    categoryCombo.setLayoutData(categoryGridData);
    
    if (categoryLookup != null) {

      // Populate category combo with category names
      for (String categoryName : categoryLookup.keySet()) {
        categoryCombo.add(categoryName);
      }
    }
    
    // Create multiple values label
    multipleValuesLabel = new Label(lookupGroup, SWT.NONE);
    multipleValuesLabel.setText("Multiple Values:");
    GridData multipleLabelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    multipleValuesLabel.setLayoutData(multipleLabelGridData);

    // Create multiple values button
    multipleValuesButton = new Button(lookupGroup, SWT.CHECK);
    multipleValuesButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
    
    // Create button composite
    Composite buttonComposite = new Composite(shell, SWT.NONE);
    buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    buttonComposite.setLayout(new GridLayout(2, true));
    
    // Create OK button
    Button okButton = new Button(buttonComposite, SWT.PUSH);
    okButton.setText("OK");
    GridData okGridData = new GridData(SWT.END, SWT.CENTER, true, false);
    okGridData.widthHint = 75;
    okButton.setLayoutData(okGridData);

    // Set OK button selection listener
    okButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        
        if (validate(shell)) {

          // Populate attribute
          populateAttribute();
          
          // Set selected button
          selectedButton = SWT.OK;
          
          // Close dialog
          shell.dispose();
        }
      }
    });

    // Create cancel button
    Button cancelButton = new Button(buttonComposite, SWT.PUSH);
    cancelButton.setText("Cancel");
    GridData cancelGridData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
    cancelGridData.widthHint = 75;
    cancelButton.setLayoutData(cancelGridData);

    // Set cancel button selection listener
    cancelButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {

        // Set selected button
        selectedButton = SWT.CANCEL;

        // Close dialog
        shell.dispose();
      }
    });
    
    // Populate dialog with attribute data
    populateDialogView();
    
    // Update dialog view
    updateDialogView();

    // Set OK button as default
    shell.setDefaultButton(okButton);
  }
  
  /**
   * Opens the attribute dialog and returns the selected button.
   * @return int Selected button.
   */
  public int open() {

    // Get parent
    Shell parent = getParent();
    
    // Create the dialog window
    Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE);
    createContents(shell);
    shell.setText("Attribute");
    shell.setMinimumSize(300, SWT.DEFAULT);
    shell.pack();

    // Get parent and shell sizes
    Rectangle parentSize = parent.getBounds();
    Rectangle shellSize = shell.getBounds();

    // Calculate coordinates for centering shell
    int locationX = (parentSize.width - shellSize.width) / 2 + parentSize.x;
    int locationY = (parentSize.height - shellSize.height) / 2 + parentSize.y;

    // Center shell
    shell.setLocation(new Point(locationX, locationY));

    // Open dialog
    shell.open();
    
    // Get the display
    Display display = getParent().getDisplay();
    
    // Loop until shell is disposed
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    
    return selectedButton;
  }

  /**
   * Populates the dialog view using the attribute property.
   */
  private void populateDialogView() {
  
    if (attribute != null) {
      
      // Set name text
      if (attribute.getName() != null) {
        nameText.setText(attribute.getName());
      }

      // Set required button
      requiredButton.setSelection(attribute.getIsRequired());

      // Get attribute type
      String type = attribute.getType();
      
      if (type != null) {

        // Get selected type
        int typeIndex = typeCombo.indexOf(type);
        
        if (typeIndex >= 0) {
          
          // Set type
          typeCombo.select(typeIndex);
          
          if (type.equals(AttributeTypes.TEXT)) {

            // Cast to text attribute
            TextAttribute textAttribute = (TextAttribute)attribute;
            
            // Set max length
            if (textAttribute.getMaxLength() != null) {
              maxLengthText.setText(String.valueOf(textAttribute.getMaxLength()));
            }
          }
          else if (type.equals(AttributeTypes.MEMO)) {

            // Cast to memo attribute
            MemoAttribute memoAttribute = (MemoAttribute)attribute;
            
            // Set max length
            if (memoAttribute.getMaxLength() != null) {
              maxLengthText.setText(String.valueOf(memoAttribute.getMaxLength()));
            }
          }
          else if (type.equals(AttributeTypes.NUMERIC)) {

            // Cast to numeric attribute
            NumericAttribute numericAttribute = (NumericAttribute)attribute;
            
            // Set value type
            if (numericAttribute.getValueType() != null) {

              // Attempt to select value type index
              int valueTypeIndex = valueTypeCombo.indexOf(numericAttribute.getValueType());
              
              if (valueTypeIndex >= 0) {
                valueTypeCombo.select(valueTypeIndex);
              }
            }

            // Set precision
            if (numericAttribute.getPrecision() != null) {
              precisionText.setText(String.valueOf(numericAttribute.getPrecision()));
            }

            // Set scale
            if (numericAttribute.getScale() != null) {
              scaleText.setText(String.valueOf(numericAttribute.getScale()));
            }
          }
          else if (type.equals(AttributeTypes.LOOKUP)) {

            // Cast to lookup attribute
            LookupAttribute lookupAttribute = (LookupAttribute)attribute;
            
            // Attempt to select category index
            int categoryIndex = categoryCombo.indexOf(lookupAttribute.getCategoryName());
            
            // Set category
            if (categoryIndex >= 0) {
              categoryCombo.select(categoryIndex);
            }
            else {
              categoryCombo.setText(lookupAttribute.getCategoryName());
            }
            
            multipleValuesButton.setSelection(lookupAttribute.getMultipleValues());
          }
        }
      }
    }
  }
  
  /**
   * Updates the dialog view based on the selected type.
   */
  private void updateDialogView() {
    
    // Hide all type specific controls
    requiredLabel.setEnabled(true);
    requiredButton.setEnabled(true);
    maxLengthLabel.setEnabled(false);
    maxLengthText.setEnabled(false);
    valueTypeLabel.setEnabled(false);
    valueTypeCombo.setEnabled(false);
    precisionLabel.setEnabled(false);
    precisionText.setEnabled(false);
    scaleLabel.setEnabled(false);
    scaleText.setEnabled(false);
    categoryLabel.setEnabled(false);
    categoryCombo.setEnabled(false);
    multipleValuesLabel.setEnabled(false);
    multipleValuesButton.setEnabled(false);
    
    // Get selected
    int selectedIndex = typeCombo.getSelectionIndex();
    
    if (selectedIndex >= 0) {
      
      // Get selected type
      String type = typeCombo.getItem(selectedIndex);
      
      // Display type specific controls
      if (type.equals(AttributeTypes.TEXT) ||
      		type.equals(AttributeTypes.MEMO)) {
      	
        maxLengthLabel.setEnabled(true);
        maxLengthText.setEnabled(true);
      }
      else if (type.equals(AttributeTypes.NUMERIC)) {
      	
        valueTypeLabel.setEnabled(true);
        valueTypeCombo.setEnabled(true);
        precisionLabel.setEnabled(true);
        precisionText.setEnabled(true);
        scaleLabel.setEnabled(true);
        scaleText.setEnabled(true);
      }
      else if (type.equals(AttributeTypes.LOOKUP)) {
      	
        categoryLabel.setEnabled(true);
        categoryCombo.setEnabled(true);
        multipleValuesLabel.setEnabled(true);
        multipleValuesButton.setEnabled(true);
      }
      else if (type.equals(AttributeTypes.BOOLEAN)) {
      	
        requiredLabel.setEnabled(false);
        requiredButton.setEnabled(false);
        requiredButton.setSelection(false);
      }
    }
  }
  
  /**
   * Validates the dialog information.
   * @param parent Parent shell.
   * @return boolean Validation indicator.
   */
  private boolean validate(Shell parent) {
    
    // Initialize return value
    boolean isValid = true;
    
    // Initialize errors
    List<String> errors = new ArrayList<String>();
    
    if (!resourceNameValidator.isValid("Name", nameText.getText())) {
      errors.add(resourceNameValidator.getErrorMessage());
    }
    if (typeCombo.getSelectionIndex() < 0) {
      errors.add("Type is a required field.");
    }
    else {
      
      // Get selected
      int selectedIndex = typeCombo.getSelectionIndex();
      
      if (selectedIndex >= 0) {
        
        // Get selected type
        String type = typeCombo.getItem(selectedIndex);
        
        if (type.equals(AttributeTypes.TEXT) ||
        		type.equals(AttributeTypes.MEMO)) {

          // Validate text properties
          if (isEmpty(maxLengthText.getText())) {
            errors.add("Max length is a required field.");
          }
        }
        else if (type.equals(AttributeTypes.NUMERIC)) {

          // Validate text properties
          if (valueTypeCombo.getSelectionIndex() < 0) {
            errors.add("Type is a required field.");
          }
          if (isEmpty(precisionText.getText())) {
            errors.add("Precision is a required field.");
          }
          if (isEmpty(scaleText.getText())) {
            errors.add("Scale is a required field.");
          }
        }
        else if (type.equals(AttributeTypes.LOOKUP)) {

          // Validate text properties
          if (isEmpty(categoryCombo.getText()) && categoryCombo.getSelectionIndex() < 0) {
            errors.add("Category is a required field.");
          }
        }
      }
    }

    if (!errors.isEmpty()) {

      // Set valid indicator
      isValid = false;
      
      // Initialize message
      StringBuffer message = new StringBuffer();
      
      for (String error : errors) {
        
        // Add line separator if not first error
        if (message.length() > 0) {
          message.append("\r\n");
        }
        
        // Add error to message
        message.append(error);
      }
      
      // Create error message dialog
      MessageBox messageBox = new MessageBox(parent, SWT.ICON_ERROR | SWT.OK);
      messageBox.setText("Validation Errors");
      messageBox.setMessage(message.toString());
      
      // Display error message dialog
      messageBox.open();
    }
    
    return isValid;
  }
  
  /**
   * Populates the attribute object.
   */
  private void populateAttribute() {
    
    // Get selected
    int selectedIndex = typeCombo.getSelectionIndex();
    
    if (selectedIndex >= 0) {
      
      // Get selected type
      String type = typeCombo.getItem(selectedIndex);
      
      // Create attribute based on type
      if (type.equals(AttributeTypes.TEXT)) {

        // Create text attribute
        TextAttribute textAttribute = new TextAttribute();
        textAttribute.setName(nameText.getText());
        textAttribute.setIsRequired(requiredButton.getSelection());
        textAttribute.setMaxLength(Integer.valueOf(maxLengthText.getText()));
        
        // Set attribute property
        attribute = textAttribute;
      }
      else if (type.equals(AttributeTypes.MEMO)) {

        // Create memo attribute
        MemoAttribute memoAttribute = new MemoAttribute();
        memoAttribute.setName(nameText.getText());
        memoAttribute.setIsRequired(requiredButton.getSelection());
        memoAttribute.setMaxLength(Integer.valueOf(maxLengthText.getText()));
        
        // Set attribute property
        attribute = memoAttribute;
      }
      else if (type.equals(AttributeTypes.EMAIL)) {

        // Create email attribute
        EmailAttribute emailAttribute = new EmailAttribute();
        emailAttribute.setName(nameText.getText());
        emailAttribute.setIsRequired(requiredButton.getSelection());
        
        // Set attribute property
        attribute = emailAttribute;
      }
      else if (type.equals(AttributeTypes.PHONE_NUMBER)) {

        // Create phone number attribute
        PhoneNumberAttribute phoneNumberAttribute = new PhoneNumberAttribute();
        phoneNumberAttribute.setName(nameText.getText());
        phoneNumberAttribute.setIsRequired(requiredButton.getSelection());
        
        // Set attribute property
        attribute = phoneNumberAttribute;
      }
      else if (type.equals(AttributeTypes.POSTAL_CODE)) {

        // Create postal code attribute
        PostalCodeAttribute postalCodeAttribute = new PostalCodeAttribute();
        postalCodeAttribute.setName(nameText.getText());
        postalCodeAttribute.setIsRequired(requiredButton.getSelection());
        
        // Set attribute property
        attribute = postalCodeAttribute;
      }
      else if (type.equals(AttributeTypes.NUMERIC)) {

        // Create numeric attribute
        NumericAttribute numericAttribute = new NumericAttribute();
        numericAttribute.setName(nameText.getText());
        numericAttribute.setIsRequired(requiredButton.getSelection());
        numericAttribute.setValueType(valueTypeCombo.getText());
        numericAttribute.setPrecision(Integer.valueOf(precisionText.getText()));
        numericAttribute.setScale(Integer.valueOf(scaleText.getText()));
        
        // Set attribute property
        attribute = numericAttribute;
      }
      else if (type.equals(AttributeTypes.DATE)) {

        // Create date attribute
        DateAttribute dateAttribute = new DateAttribute();
        dateAttribute.setName(nameText.getText());
        dateAttribute.setIsRequired(requiredButton.getSelection());

        // Set attribute property
        attribute = dateAttribute;
      }
      else if (type.equals(AttributeTypes.TIME)) {

        // Create time attribute
        TimeAttribute timeAttribute = new TimeAttribute();
        timeAttribute.setName(nameText.getText());
        timeAttribute.setIsRequired(requiredButton.getSelection());

        // Set attribute property
        attribute = timeAttribute;
      }
      else if (type.equals(AttributeTypes.BOOLEAN)) {

        // Create boolean attribute
        BooleanAttribute booleanAttribute = new BooleanAttribute();
        booleanAttribute.setName(nameText.getText());
        booleanAttribute.setIsRequired(requiredButton.getSelection());

        // Set attribute property
        attribute = booleanAttribute;
      }
      else if (type.equals(AttributeTypes.LOOKUP)) {

        // Initialize category ID and name
        Integer categoryId = null;
        String categoryName = null;
        
        // Get selected category index
        int categoryIndex = categoryCombo.getSelectionIndex();
        
        if (categoryIndex == -1) {
          
          // Get new category name
          categoryName = categoryCombo.getText();
        }
        else {
          
          // Get existing category name
          categoryName = categoryCombo.getItem(categoryIndex);
          
          // Lookup category ID
          categoryId = categoryLookup.get(categoryName);
        }
        
        // Create lookup attribute
        LookupAttribute lookupAttribute = new LookupAttribute();
        lookupAttribute.setName(nameText.getText());
        lookupAttribute.setIsRequired(requiredButton.getSelection());
        lookupAttribute.setCategoryId(categoryId);
        lookupAttribute.setCategoryName(categoryName);
        lookupAttribute.setMultipleValues(multipleValuesButton.getSelection());

        // Set attribute property
        attribute = lookupAttribute;
      }
    }
  }

  /**
   * Returns a value indicating whether or not a text value is null or empty.
   * @param text Text value.
   * @return boolean Empty text indicator.
   */
  private boolean isEmpty(String text) {
    return text == null || text.trim().length() == 0;
  }
  
  /**
   * Returns the attribute data.
   * @return Attribute Attribute object.
   */
  public Attribute getAttribute() {
    return attribute;
  }

  /**
   * Sets the attribute data.
   * @param attribute Attribute object.
   */
  public void setAttribute(Attribute attribute) {
    this.attribute = attribute;
  }

}