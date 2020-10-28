package com.bws.jdistil.project.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.bws.jdistil.project.fragment.data.Attribute;

/**
 * Captures an application fragment view. 
 */
public class ViewWizardPage extends WizardPage {
	
  /**
   * Enable button.
   */
  private Button enableSupportButton = null;

  /**
   * Page size text.
   */
  private Text pageSizeText = null;

  /**
   * List of available table filter attributes.
   */
  private List availableFilterList = null;
	
  /**
   * List of selected table filter attributes.
   */
  private List selectedFilterList = null;
  
  /**
   * Add filter button.
   */
  private Button addFilterButton = null;

  /**
   * Remove filter button.
   */
  private Button removeFilterButton = null;

  /**
   * Add all filter button.
   */
  private Button addAllFilterButton = null;

  /**
   * Remove all filter button.
   */
  private Button removeAllFilterButton = null;

  /**
   * Move filter up button.
   */
  private Button moveFilterUpButton = null;

  /**
   * Move filter down button.
   */
  private Button moveFilterDownButton = null;

  /**
   * List of available table column attributes.
   */
  private List availableColumnList = null;
  
  /**
   * List of selected table column attributes.
   */
  private List selectedColumnList = null;
  
  /**
   * Add column button.
   */
  private Button addColumnButton = null;

  /**
   * Remove column button.
   */
  private Button removeColumnButton = null;

  /**
   * Add all column button.
   */
  private Button addAllColumnButton = null;

  /**
   * Remove all column button.
   */
  private Button removeAllColumnButton = null;

  /**
   * Move column up button.
   */
  private Button moveColumnUpButton = null;

  /**
   * Move column down button.
   */
  private Button moveColumnDownButton = null;

  /**
	 * Creates a new view wizard page.
	 * @param selection Object selected in workbench.
	 */
	public ViewWizardPage(IStructuredSelection selection) {
		super("View");
		
		// Set title and description
		setTitle("View");
		setDescription("Captures an application fragment view.");

    // Start with page incomplete
    setPageComplete(false);
	}

	/**
   * Create the wizard page.
   * @param composite Parent composite.
	 */
	public void createControl(Composite parent) {

    // Create containing composite and set layout
	  Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));
		
    // Create pagination group
    Group paginationGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
    paginationGroup.setText("Pagination");
    paginationGroup.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    paginationGroup.setLayout(new GridLayout(5, false));

    // Create enable support label
    Label enableSupportLabel = new Label(paginationGroup, SWT.NONE);
    enableSupportLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    enableSupportLabel.setText("Enable:");

    // Create enable support button
    enableSupportButton = new Button(paginationGroup, SWT.CHECK);
    enableSupportButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.END, false, false));
    enableSupportButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        
        if (enableSupportButton.getSelection()) {
          pageSizeText.setEnabled(true);
        }
        else {
          pageSizeText.setEnabled(true);
          pageSizeText.setText("");
        }
        
        // Validate page
        validate();
      }
    });
    
    // Create label for column spacer
    Label spacerLabel = new Label(paginationGroup, SWT.NONE);
    GridData spacerGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    spacerGridData.widthHint = 10;
    spacerLabel.setLayoutData(spacerGridData);

    // Create page size label
    Label pageSizeLabel = new Label(paginationGroup, SWT.NONE);
    pageSizeLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    pageSizeLabel.setText("Page Size:");

    // Create page size text
    pageSizeText = new Text(paginationGroup, SWT.BORDER);
    pageSizeText.setEnabled(false);
    GridData pageSizeTextGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    pageSizeTextGridData.widthHint = 20;
    pageSizeText.setLayoutData(pageSizeTextGridData);
    pageSizeText.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent event) {

        // Validate page
        validate();
      }
    });

    // Create table filter group
    Group filterGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
    filterGroup.setText("Table Filters");
    filterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    filterGroup.setLayout(new GridLayout(4, false));
    
    // Create available filter label
    Label availableFilterLabel = new Label(filterGroup, SWT.NONE);
    availableFilterLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    availableFilterLabel.setText("Available");

    // Create label for column spacer
    Label spacer1Label = new Label(filterGroup, SWT.NONE);
    spacer1Label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

    // Create selected filter label
    Label selectedFilterLabel = new Label(filterGroup, SWT.NONE);
    selectedFilterLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    selectedFilterLabel.setText("Selected");

    // Create label for column spacer
    Label spacer2Label = new Label(filterGroup, SWT.NONE);
    spacer2Label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

    // Create available filter list
    availableFilterList = new List(filterGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    availableFilterList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    availableFilterList.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        updateFilterButtonStatus();
      }
    });
    
    // Create move filter button composite
    Composite moveFilterButtonComposite = new Composite(filterGroup, SWT.NONE);
    moveFilterButtonComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true));
    moveFilterButtonComposite.setLayout(new GridLayout(1, false));
    
    // Create add filter button
    addFilterButton = new Button(moveFilterButtonComposite, SWT.PUSH);
    addFilterButton.setText(">");
    GridData addFilterButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    addFilterButtonGridData.widthHint = 50;
    addFilterButton.setLayoutData(addFilterButtonGridData);
    addFilterButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        moveItems(availableFilterList, selectedFilterList, false);
        updateFilterButtonStatus();
      }
    });
    
    // Create remove filter button
    removeFilterButton = new Button(moveFilterButtonComposite, SWT.PUSH);
    removeFilterButton.setText("<");
    GridData removeFilterButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    removeFilterButtonGridData.widthHint = 50;
    removeFilterButton.setLayoutData(removeFilterButtonGridData);
    removeFilterButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        moveItems(selectedFilterList, availableFilterList, false);
        updateFilterButtonStatus();
      }
    });

    // Create add all filter button
    addAllFilterButton = new Button(moveFilterButtonComposite, SWT.PUSH);
    addAllFilterButton.setText(">>");
    GridData addAllFilterButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    addAllFilterButtonGridData.widthHint = 50;
    addAllFilterButton.setLayoutData(addAllFilterButtonGridData);
    addAllFilterButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        moveItems(availableFilterList, selectedFilterList, true);
        updateFilterButtonStatus();
      }
    });
    
    // Create remove all filter button
    removeAllFilterButton = new Button(moveFilterButtonComposite, SWT.PUSH);
    removeAllFilterButton.setText("<<");
    GridData removeAllFilterButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    removeAllFilterButtonGridData.widthHint = 50;
    removeAllFilterButton.setLayoutData(removeAllFilterButtonGridData);
    removeAllFilterButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        moveItems(selectedFilterList, availableFilterList, true);
        updateFilterButtonStatus();
      }
    });

    // Create selected filter list
    selectedFilterList = new List(filterGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    selectedFilterList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    selectedFilterList.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        updateFilterButtonStatus();
      }
    });

    // Create order filter button composite
    Composite orderFilterButtonComposite = new Composite(filterGroup, SWT.NONE);
    orderFilterButtonComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true));
    orderFilterButtonComposite.setLayout(new GridLayout(1, false));
    
    // Create move filter up button
    moveFilterUpButton = new Button(orderFilterButtonComposite, SWT.PUSH);
    moveFilterUpButton.setText("Up");
    GridData moveFilterUpButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    moveFilterUpButtonGridData.widthHint = 50;
    moveFilterUpButton.setLayoutData(moveFilterUpButtonGridData);
    moveFilterUpButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        orderItems(selectedFilterList, true);
        updateFilterButtonStatus();
      }
    });
    
    // Create move filter down button
    moveFilterDownButton = new Button(orderFilterButtonComposite, SWT.PUSH);
    moveFilterDownButton.setText("Down");
    GridData moveFilterDownButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    moveFilterDownButtonGridData.widthHint = 50;
    moveFilterDownButton.setLayoutData(moveFilterDownButtonGridData);
    moveFilterDownButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        orderItems(selectedFilterList, false);
        updateFilterButtonStatus();
      }
    });
    
    // Create table column group
    Group columnGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
    columnGroup.setText("Table Columns");
    columnGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    columnGroup.setLayout(new GridLayout(4, false));

    // Create available column label
    Label availableColumnLabel = new Label(columnGroup, SWT.NONE);
    availableColumnLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    availableColumnLabel.setText("Available");

    // Create label for column spacer
    Label spacer3Label = new Label(columnGroup, SWT.NONE);
    spacer3Label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

    // Create selected column label
    Label selectedColumnLabel = new Label(columnGroup, SWT.NONE);
    selectedColumnLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    selectedColumnLabel.setText("Selected");

    // Create label for column spacer
    Label spacer4Label = new Label(columnGroup, SWT.NONE);
    spacer4Label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

    // Create available column list
    availableColumnList = new List(columnGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    availableColumnList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    availableColumnList.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        updateColumnButtonStatus();
      }
    });
    
    Composite columnButtonComposite = new Composite(columnGroup, SWT.NONE);
    columnButtonComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true));
    columnButtonComposite.setLayout(new GridLayout(1, false));
    
    // Create add column button
    addColumnButton = new Button(columnButtonComposite, SWT.PUSH);
    addColumnButton.setText(">");
    GridData addColumnButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    addColumnButtonGridData.widthHint = 50;
    addColumnButton.setLayoutData(addColumnButtonGridData);
    addColumnButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        moveItems(availableColumnList, selectedColumnList, false);
        updateColumnButtonStatus();
        validate();
      }
    });
    
    // Create remove column button
    removeColumnButton = new Button(columnButtonComposite, SWT.PUSH);
    removeColumnButton.setText("<");
    GridData removeColumnButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    removeColumnButtonGridData.widthHint = 50;
    removeColumnButton.setLayoutData(removeColumnButtonGridData);
    removeColumnButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        moveItems(selectedColumnList, availableColumnList, false);
        updateColumnButtonStatus();
        validate();
      }
    });

    // Create add all column button
    addAllColumnButton = new Button(columnButtonComposite, SWT.PUSH);
    addAllColumnButton.setText(">>");
    GridData addAllColumnButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    addAllColumnButtonGridData.widthHint = 50;
    addAllColumnButton.setLayoutData(addAllColumnButtonGridData);
    addAllColumnButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        moveItems(availableColumnList, selectedColumnList, true);
        updateColumnButtonStatus();
        validate();
      }
    });
    
    // Create remove all column button
    removeAllColumnButton = new Button(columnButtonComposite, SWT.PUSH);
    removeAllColumnButton.setText("<<");
    GridData removeAllColumnButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, true);
    removeAllColumnButtonGridData.widthHint = 50;
    removeAllColumnButton.setLayoutData(removeAllColumnButtonGridData);
    removeAllColumnButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        moveItems(selectedColumnList, availableColumnList, true);
        updateColumnButtonStatus();
        validate();
      }
    });

    // Create selected column list
    selectedColumnList = new List(columnGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    selectedColumnList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    selectedColumnList.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        updateColumnButtonStatus();
      }
    });
    
    // Create order column button composite
    Composite orderColumnButtonComposite = new Composite(columnGroup, SWT.NONE);
    orderColumnButtonComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true));
    orderColumnButtonComposite.setLayout(new GridLayout(1, false));
    
    // Create move column up button
    moveColumnUpButton = new Button(orderColumnButtonComposite, SWT.PUSH);
    moveColumnUpButton.setText("Up");
    GridData moveColumnUpButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    moveColumnUpButtonGridData.widthHint = 50;
    moveColumnUpButton.setLayoutData(moveColumnUpButtonGridData);
    moveColumnUpButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        orderItems(selectedColumnList, true);
        updateColumnButtonStatus();
      }
    });
    
    // Create move column down button
    moveColumnDownButton = new Button(orderColumnButtonComposite, SWT.PUSH);
    moveColumnDownButton.setText("Down");
    GridData moveColumnDownButtonGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    moveColumnDownButtonGridData.widthHint = 50;
    moveColumnDownButton.setLayoutData(moveColumnDownButtonGridData);
    moveColumnDownButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        orderItems(selectedColumnList, false);
        updateColumnButtonStatus();
      }
    });
    
    // Update button statuses
    updateFilterButtonStatus();
    updateColumnButtonStatus();
    
    // Validate to set message
    validate();

    // Pack composite to preferred size
    composite.pack();
    
    // Set composite to page
		setControl(composite);
	}

	/**
	 * Populates the available filters and available column lists
	 * using a list of attributes.
	 * @param attributes List of attributes.
	 */
	public void populateAttributes(java.util.List<Attribute> attributes) {

	  if (attributes != null) {

	    // Initialize filter and column lists
      initializeAttributeList(attributes, availableFilterList, selectedFilterList);
	    initializeAttributeList(attributes, availableColumnList, selectedColumnList);

	    // Update button statuses
	    updateFilterButtonStatus();
	    updateColumnButtonStatus();
	  }
	}
	
  /**
   * Initializes source and target lists using a list of attributes.
   * @param attributes List of attributes.
   * @param sourceList Source list.
   * @param targetList Target list.
   */
  private void initializeAttributeList(java.util.List<Attribute> attributes, List sourceList, List targetList) {
    
    // Initialize target items collection
    Collection<String> targetItems = new HashSet<String>();
    
    if (targetList.getItemCount() > 0) {
      
      // Get target items
      String[] items = targetList.getItems();

      // Add target item to current target items collection
      for (String item : items) {
        targetItems.add(item);
      }
    }
    
    // Clear source and target lists
    sourceList.removeAll();
    targetList.removeAll();
    
    if (attributes != null && !attributes.isEmpty()) {

      // Initialize source attribute and target attribute names
      ArrayList<String> sourceAttributeNames = new ArrayList<String>();
      ArrayList<String> targetAttributeNames = new ArrayList<String>();
      
      for (Attribute attribute : attributes) {
        
        // Get attribute name
        String attributeName = attribute.getName();
        
        // Add to appropriate attribute name list
        if (targetItems.contains(attributeName)) {
          targetAttributeNames.add(attributeName);
        }
        else {
          sourceAttributeNames.add(attributeName);
        }
      }
      
      // Populate source list
      for (String attributeName : sourceAttributeNames) {
        sourceList.add(attributeName);
      }

      // Populate target list
      for (String attributeName : targetAttributeNames) {
        targetList.add(attributeName);
      }
    }
  }
  
  /**
	 * Moves items from one list to another.
	 * @param sourceList Source list.
	 * @param targetList Target list.
	 * @param moveAll Indicates whether all items should be moved instead of just the selected items.
	 */
	private void moveItems(List sourceList, List targetList, boolean moveAll) {
	  
    // Create items list
    ArrayList<String> items = new ArrayList<String>();
    
	  // Get all target items
	  String[] targetItems = targetList.getItems();
	  
    if (targetItems != null) {

      // Add target items
      for (String item : targetItems) {
        items.add(item);
      }
      
      // Remove items from target list
      targetList.removeAll();
    }

	  // Initialize source items
	  String[] sourceItems = null;
	  
	  if (moveAll) {

	    // Get all source items
	    sourceItems = sourceList.getItems();

	    // Remove all source items
	    sourceList.removeAll();
	  }
	  else {

      // Get selected source items
	    sourceItems = sourceList.getSelection();
	    
	    // Remove selected source items
	    sourceList.remove(sourceList.getSelectionIndices());
	  }
	  
    if (sourceItems != null) {

      // Add source items
      for (String item : sourceItems) {
        items.add(item);
      }
    }
    
    // Add items to target list
    for (String item : items) {
      targetList.add(item);
    }
	}

  /**
   * Moves items in a list up or down.
   * @param sourceList Source list.
   * @param isMoveUp Indicates which direction to move items.
   */
  private void orderItems(List sourceList, boolean isMoveUp) {

    // Get selected source items
    String[] selectedItems = sourceList.getSelection();

    if (selectedItems != null && selectedItems.length > 0) {

      // Create selection lookup
      Collection<String> selectionLookup = Arrays.asList(selectedItems);

      // Get items from source list
      java.util.List<String> items = Arrays.asList(sourceList.getItems());

      // Work with an inverted list if moving items down
      if (!isMoveUp){
        Collections.reverse(items);
      }
      
      // Initialize ordered items
      ArrayList<String> orderedItems = new ArrayList<String>(items.size());
      
      for (String item : items) {
        
        if (orderedItems.isEmpty() || !selectionLookup.contains(item)) {
          
          // Adds as next item in ordered list if not selected
          orderedItems.add(item);
        }
        else {
          
          // Get index of last ordered item
          int lastItemIndex = orderedItems.size() - 1;
          
          // Add to pending items to be added later
          orderedItems.add(lastItemIndex, item);
        }
      }
      
      // Reverse items after reordering when moving items down
      if (!isMoveUp){
        Collections.reverse(orderedItems);
      }
      
      // Set reordered items on source list
      sourceList.setItems(orderedItems.toArray(new String[orderedItems.size()]));
      
      for (int index = 0; index < orderedItems.size(); index++) {
        
        // Get next item
        String item = orderedItems.get(index);
        
        // Remark selected items
        if (selectionLookup.contains(item)) {
          sourceList.select(index);
        }
      }
    }
  }

	/**
	 * Updates the enable status of all filter buttons.
	 */
	private void updateFilterButtonStatus() {
	  
    addFilterButton.setEnabled(availableFilterList.getSelectionCount() > 0);
    removeFilterButton.setEnabled(selectedFilterList.getSelectionCount() > 0);
    addAllFilterButton.setEnabled(availableFilterList.getItemCount() > 0);
    removeAllFilterButton.setEnabled(selectedFilterList.getItemCount() > 0);

    moveFilterUpButton.setEnabled(selectedFilterList.getSelectionCount() > 0);
    moveFilterDownButton.setEnabled(selectedFilterList.getSelectionCount() > 0);
	}
	
  /**
   * Updates the enable status of all column buttons.
   */
  private void updateColumnButtonStatus() {
    
    addAllColumnButton.setEnabled(availableColumnList.getItemCount() > 0);
    addColumnButton.setEnabled(availableColumnList.getSelectionCount() > 0);
    removeAllColumnButton.setEnabled(selectedColumnList.getItemCount() > 0);
    removeColumnButton.setEnabled(selectedColumnList.getSelectionCount() > 0);

    moveColumnUpButton.setEnabled(selectedColumnList.getSelectionCount() > 0);
    moveColumnDownButton.setEnabled(selectedColumnList.getSelectionCount() > 0);
  }
  
  /**
   * Validates the view page.
   */
  private void validate() {
    
    // Get page size
    String pageSize = pageSizeText.getText();
    
    if (enableSupportButton.getSelection() && (pageSize == null || pageSize.trim().length() == 0)) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Page size is required.");
    }
    else if (selectedColumnList.getItemCount() == 0) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("At least one table column is required.");
    }
    else {
      
      // Set page as complete and clear error message
      setPageComplete(true);
      setErrorMessage(null);
    }
  }

  /**
   * Returns the paging enabled indicator.
   * @return boolean Paging enabled indicator.
   */
  private boolean getIsPagingEnabled() {
    return enableSupportButton.getSelection();
  }

  /**
   * Returns the page size.
   * @return String Page size.
   */
  private String getPageSize() {
    return pageSizeText.getText();
  }

  /**
   * Returns the filter attribute names.
   * @return String[] Filter attribute names.
   */
  private String[] getFilterAttributeNames() {
    return selectedFilterList.getItems();
  }
  
  /**
   * Returns the column attribute names.
   * @return String[] Column attribute names.
   */
  private String[] getColumnAttributeNames() {
    return selectedColumnList.getItems();
  }
  
  /**
   * Returns all view wizard page data.
   * @return ViewWizardData View wizard page data.
   */
  public ViewWizardData getData() {
  
    // Create data object
    ViewWizardData viewWizardData = new ViewWizardData();
    
    // Populate data object
    viewWizardData.setPagingEnabled(getIsPagingEnabled());
    viewWizardData.setPageSize(getPageSize());
    viewWizardData.setFilterAttributeNames(getFilterAttributeNames());
    viewWizardData.setColumnAttributeNames(getColumnAttributeNames());
    
    return viewWizardData;
  }
  
}