package com.bws.jdistil.project.fragment;

import com.bws.jdistil.project.fragment.data.Attribute;
import com.bws.jdistil.project.util.ResourceFinder;
import com.bws.jdistil.project.validation.PackageNameValidator;
import com.bws.jdistil.project.validation.ResourceNameValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * Captures an application fragment definition. 
 */
public class DefinitionWizardPage extends WizardPage {
	
  /**
   * Project selected in workbench.
   */
  private IProject selectedProject;

  /**
   * List of projects containing the JDistil fragment.
   */
  private Combo projectCombo = null;
	
  /**
   * Package name.
   */
  private Text packageText = null;

  /**
   * Package button.
   */
  private Button packageButton = null;

  /**
   * Entity name.
   */
  private Text nameText = null;

  /**
   * List of parent entities.
   */
  private Combo parentCombo = null;

  /**
   * Attribute table.
   */
  private Table attributeTable = null;
  
  /**
   * Add attribute button.
   */
  private Button addAttributeButton = null;

  /**
   * Edit attribute button.
   */
  private Button editAttributeButton = null;

  /**
   * Delete attribute button.
   */
  private Button deleteAttributeButton = null;

  /**
   * Package name validator.
   */
  private PackageNameValidator packageNameValidator = new PackageNameValidator();

  /**
   * Resource name validator.
   */
  private ResourceNameValidator resourceNameValidator = new ResourceNameValidator();

  /**
   * Map of category names keyed by category ID. Category names are specific to 
   * the selected project and are used to provide choices in attribute dialog.
   */
  private Map<Integer, String> categories = new HashMap<Integer, String>();
  
  /**
	 * Creates a new definition wizard page.
	 * @param selection Object selected in workbench.
	 */
	public DefinitionWizardPage(IStructuredSelection selection) {
		super("Definition");
		
		if (selection != null) {
		  
		  // Get first element in selection
	    Object element = selection.getFirstElement();
	    
	    if (element instanceof IJavaProject) {
	    
	      // Cast to java project
	      IJavaProject javaProject = (IJavaProject)element;
	      
	      // Set selected project
	      selectedProject = javaProject.getProject();
	    }
		}
		
		// Set title and description
		setTitle("Definition");
		setDescription("Captures an application fragment definition.");

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
    composite.setLayout(new GridLayout(3, false));
		
    // Create project label
    Label projectLabel = new Label(composite, SWT.NONE);
    projectLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    projectLabel.setText("Project:");

    // Create project list
    projectCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
    projectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    
    // Create label for column spacer
    Label spacer1Label = new Label(composite, SWT.NONE);
    spacer1Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
		// Create package label
    Label packageLabel = new Label(composite, SWT.NONE);
    packageLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    packageLabel.setText("Package:");

    // Create package text
    packageText = new Text(composite, SWT.BORDER);
    packageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    packageText.addModifyListener(new PackageTextModifyListener(this));
    
    // Create package button
    packageButton = new Button(composite, SWT.PUSH);
    packageButton.setText("Browse...");
    GridData packageButtonGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    packageButtonGridData.widthHint = 75;
    packageButton.setLayoutData(packageButtonGridData);
    packageButton.addSelectionListener(new PackageButtonSelectionListener(this));

    // Create name label
    Label nameLabel = new Label(composite, SWT.NONE);
    nameLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    nameLabel.setText("Name:");

    // Create name text
    nameText = new Text(composite, SWT.BORDER);
    nameText.setTextLimit(30);
    nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    nameText.addModifyListener(new NameTextModifyListener(this));
		
    // Create label for column spacer
    Label spacer2Label = new Label(composite, SWT.NONE);
    spacer2Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
    // Create parent label
    Label parentLabel = new Label(composite, SWT.NONE);
    parentLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    parentLabel.setText("Parent:");

    // Create parent list
    parentCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
    parentCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    // Create label for column spacer
    Label spacer3Label = new Label(composite, SWT.NONE);
    spacer3Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
    // Create attribute group
    Group attributeGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
    attributeGroup.setText("Attributes");
    attributeGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 3));
    attributeGroup.setLayout(new GridLayout(3, false));
    
    // Create attribute table
    attributeTable = new Table(attributeGroup, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
    attributeTable.setHeaderVisible(true);
    TableColumn nameColumn = new TableColumn(attributeTable, SWT.LEFT);
    nameColumn.setText("Name");
    nameColumn.setWidth(100);
    TableColumn typeColumn = new TableColumn(attributeTable, SWT.LEFT);
    typeColumn.setText("Type");
    typeColumn.setWidth(100);
    TableColumn requiredColumn = new TableColumn(attributeTable, SWT.CENTER);
    requiredColumn.setText("Required");
    requiredColumn.setWidth(80);
    attributeTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 3));
    attributeTable.addControlListener(new AttributeTableControlListener(this));
    attributeTable.addSelectionListener(new AttributeTableSelectionListener(this));
    
    // Create add attribute button
    addAttributeButton = new Button(attributeGroup, SWT.PUSH);
    addAttributeButton.setText("Add...");
    GridData addAttributeButtonGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
    addAttributeButtonGridData.widthHint = 75;
    addAttributeButton.setLayoutData(addAttributeButtonGridData);
    addAttributeButton.addSelectionListener(new AddAttributeButtonSelectionListener(this));

    // Create edit attribute button
    editAttributeButton = new Button(attributeGroup, SWT.PUSH);
    editAttributeButton.setText("Edit...");
    GridData editAttributeButtonGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
    editAttributeButtonGridData.widthHint = 75;
    editAttributeButton.setLayoutData(editAttributeButtonGridData);
    editAttributeButton.addSelectionListener(new EditAttributeButtonSelectionListener(this));

    // Create delete attribute button
    deleteAttributeButton = new Button(attributeGroup, SWT.PUSH);
    deleteAttributeButton.setText("Delete");
    GridData deleteAttributeButtonGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
    deleteAttributeButtonGridData.widthHint = 75;
    deleteAttributeButton.setLayoutData(deleteAttributeButtonGridData);
    deleteAttributeButton.addSelectionListener(new DeleteAttributeButtonSelectionListener(this));
    
	  // Populate reference data
	  populateProjects();
	  populateParents();
	  populateCategoryNames();
	  
	  // Register project combo listener after reference data is loaded
    projectCombo.addSelectionListener(new ProjectComboSelectionListener(this));
	  
    // Update attribute button status
    updateAttributeButtonStatus();    

    // Pack composite to preferred size
    composite.pack();
    
    // Set composite to page
		setControl(composite);
	}

  /**
   * Populates the project combo with projects containing the Jdistil project facet.
   */
  private void populateProjects() {
    
    // Get JDistil project facet
    IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet("com.bws.jdistil.project.facet");

    try {
      // Get all projects with the JDistil project facet
      Set<IFacetedProject> facetedProjects = ProjectFacetsManager.getFacetedProjects(projectFacet);
      
      if (facetedProjects != null) {
        
        // Create project data
        Map<String, IProject> projectData = new HashMap<String, IProject>();
        
        // Initialize current index and selected project index
        int currentIndex = 0;
        int selectedProjectIndex = 0;
        
        for (IFacetedProject facetedProject : facetedProjects) {
          
          // Get next project
          IProject project = facetedProject.getProject();
          
          // Set selected project index
          if (selectedProject != null && selectedProject.equals(project)) {
            selectedProjectIndex = currentIndex;
          }
          
          // Add project to lookup
          projectData.put(project.getName(), project);
  
          // Add project name to list
          projectCombo.add(project.getName());
          
          // Increment current index
          currentIndex++;
        }
        
        // Add project data to list
        projectCombo.setData(projectData);
        
        // Set selected project
        projectCombo.select(selectedProjectIndex);
      }
    }
    catch (CoreException coreException) {
      
      // Display error message
      MessageDialog.openError(getShell(), "Error", coreException.getMessage());
    }

  }

  /**
   * Populates the parents combo with the names of classes implementing 
   * the DataObject interface in the currently selected project.
   */
  @SuppressWarnings("unchecked")
  private void populateParents() {
    
    // Get currently selected project
    int selectedIndex = projectCombo.getSelectionIndex();
    
    if (selectedIndex >= 0) {
      
      // Clear parent combo
      parentCombo.removeAll();
      
      // Get currently selected project name
      String projectName = projectCombo.getItem(selectedIndex);
      
      // Get project data and lookup selected project
      Map<String, IProject> projectData = (Map<String, IProject>)projectCombo.getData();
      IProject project = projectData.get(projectName);
      
      // Create java project
      IJavaProject javaProject = JavaCore.create(project);

      // Initialize source package fragment root
      IPackageFragmentRoot sourcePackageFragmentRoot = null;
      
      try {
        // Get all package fragment roots
        IPackageFragmentRoot[] packageFragmentRoots = javaProject.getAllPackageFragmentRoots();
        
        // Find source package fragment root
        for (IPackageFragmentRoot packageFragmentRoot : packageFragmentRoots) {
          if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
            sourcePackageFragmentRoot = packageFragmentRoot;
            break;
          }
        }
        
        if (sourcePackageFragmentRoot != null) {
          
          // Get data object type
          IType dataObjectType = javaProject.findType("com.bws.jdistil.core.datasource.DataObject");

          // Create region containing source package fragment root
          IRegion region = JavaCore.newRegion();
          region.add(sourcePackageFragmentRoot);

          // Retrieve type hierarchy
          ITypeHierarchy typeHierarchy = javaProject.newTypeHierarchy(dataObjectType, region, null);

          // Get all types implementing data manager interface
          IType[] types = typeHierarchy.getAllClasses();
          
          if (types != null) {
            
          	// Create list of parent class names
          	List<String> parentClassNames = new ArrayList<String>();
          	
            for (IType type : types) {
              
              // Add source types only
              if (!type.isBinary()) {
              	parentClassNames.add(type.getFullyQualifiedName());
              }
            }
            
            // Sort parent class names
            Collections.sort(parentClassNames);
            
            // Add parent class names to combo
            for (String parentClassName : parentClassNames) {
              parentCombo.add(parentClassName);
            }
          }
        }
      }
      catch (CoreException coreException) {
        
        // Display error message
        MessageDialog.openError(getShell(), "Error", coreException.getMessage());
      }
    }
  }

  /**
   * Populates a map of category names keyed by category ID by retrieving 
   * all categories from the project specific categories class. 
   */
  @SuppressWarnings("unchecked")
  private void populateCategoryNames() {
    
    // Clear existing categories
    categories.clear();
    
    // Get currently selected project
    int selectedIndex = projectCombo.getSelectionIndex();
    
    if (selectedIndex >= 0) {
      
      // Get currently selected project name
      String projectName = projectCombo.getItem(selectedIndex);
      
      // Get project data and lookup selected project
      Map<String, IProject> projectData = (Map<String, IProject>)projectCombo.getData();
      IProject project = projectData.get(projectName);
      
      // Create java project
      IJavaProject javaProject = JavaCore.create(project);
      
      try {
        // Find category IDs compilation unit
        ICompilationUnit categoryIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "CategoryIds.java");
        
        if (categoryIdsCompilationUnit != null) {
          
          // Get category IDs type
          IType type = categoryIdsCompilationUnit.getType("CategoryIds");
          
          if (type != null) {
            
            // Get all declared fields
            IField[] fields = type.getFields();
            
            for (IField field : fields) {
              
              // Get category ID and category name
              Integer categoryId = (Integer)field.getConstant();
              String categoryName = field.getElementName();
              
              // Add category map
              categories.put(categoryId, categoryName);
            }
          }
        }
      }
      catch (CoreException coreException) {
        
        // Display error message
        MessageDialog.openError(getShell(), "Error", coreException.getMessage());
      }
    }
  }

  /**
   * Displays the package selection dialog.
   */
  @SuppressWarnings("unchecked")
  private void choosePackage() {
    
    // Get currently selected project
    int selectedIndex = projectCombo.getSelectionIndex();
    
    if (selectedIndex >= 0) {
      
      // Get currently selected project name
      String projectName = projectCombo.getItem(selectedIndex);
      
      // Get project data and lookup selected project
      Map<String, IProject> projectData = (Map<String, IProject>)projectCombo.getData();
      IProject project = projectData.get(projectName);
      
      // Create java project
      IJavaProject javaProject = JavaCore.create(project);
      
      try {
        // Initialize source package fragment root
        IPackageFragmentRoot sourcePackageFragmentRoot = null;
        
        // Get all package fragment roots
        IPackageFragmentRoot[] packageFragmentRoots = javaProject.getAllPackageFragmentRoots();
        
        // Find source package fragment root
        for (IPackageFragmentRoot packageFragmentRoot : packageFragmentRoots) {
          if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
            sourcePackageFragmentRoot = packageFragmentRoot;
            break;
          }
        }

        if (sourcePackageFragmentRoot != null) {
          
          // Get packages
          IJavaElement[] packages = sourcePackageFragmentRoot.getChildren();

          // Create package selection dialog
          ElementListSelectionDialog packageSelectionDialog = new ElementListSelectionDialog(getShell(), new JavaElementLabelProvider());
          packageSelectionDialog.setMultipleSelection(false);
          packageSelectionDialog.setTitle("Package Selection");
          packageSelectionDialog.setBlockOnOpen(true);
          packageSelectionDialog.setElements(packages);
          packageSelectionDialog.setHelpAvailable(false);
          packageSelectionDialog.setMessage("Select a package for fragment classes.");
          
          // Open dialog and get selected button
          int selectedButton = packageSelectionDialog.open();
          
          if (selectedButton == Window.OK) {
            
            // Get selected item
            IJavaElement selectedItem = (IJavaElement)packageSelectionDialog.getFirstResult();
            
            if (selectedItem != null) {

              // Set package text and data
              packageText.setText(selectedItem.getElementName());
              packageText.setData(selectedItem);
              
              // Validate definition wizard page
              validate();
            }
          }
        }
      }
      catch (JavaModelException javaModelException) {
        
        // Display error message
        MessageDialog.openError(getShell(), "Error", javaModelException.getMessage());
      }
    }
  }
  
  /**
   * Sets enabled status of attribute buttons based on row selection in attribute table.
   */
  private void updateAttributeButtonStatus() {
    
    // Get total selected rows
    int selectionCount = attributeTable.getSelectionCount();
    
    // Update button status
    if (selectionCount == 0) {
      editAttributeButton.setEnabled(false);
      deleteAttributeButton.setEnabled(false);
    }
    else if (selectionCount == 1) {
      editAttributeButton.setEnabled(true);
      deleteAttributeButton.setEnabled(true);
    }
    else if (selectionCount > 1) {
      editAttributeButton.setEnabled(false);
      deleteAttributeButton.setEnabled(true);
    }
  }
  
  /**
   * Adds an attribute using the attribute dialog.
   */
  private void addAttribute() {
    
    // Create attribute dialog
    AttributeDialog attributeDialog = new AttributeDialog(getShell(), categories);
    
    if (attributeDialog.open() == SWT.OK) {
      
      // Get attribute
      Attribute attribute = attributeDialog.getAttribute();

      if (attribute != null) {
        
        // Add table item
        TableItem tableItem = new TableItem(attributeTable, SWT.NONE);
        
        // Update table item
        tableItem.setText(0, attribute.getName());
        tableItem.setText(1, attribute.getTypeDescription());
        tableItem.setText(2, attribute.getIsRequired() ? "Yes" : "No");
        
        // Set table item data
        tableItem.setData(attribute);
        
        // Update attribute button status
        updateAttributeButtonStatus();
        
        // Validate definition wizard page
        validate();
      }
    }
  }
  
  /**
   * Edits an attribute using the attribute dialog.
   */
  private void editAttribute() {
    
    // Get selected index
    int selectedIndex = attributeTable.getSelectionIndex();
    
    if (selectedIndex >= 0) {

      // Get selected table item
      TableItem tableItem = attributeTable.getItem(selectedIndex);
      
      // Get attribute data
      Attribute attribute = (Attribute)tableItem.getData();
      
      // Create attribute dialog
      AttributeDialog attributeDialog = new AttributeDialog(getShell(), categories);
      
      // Set attribute data
      attributeDialog.setAttribute(attribute);
      
      if (attributeDialog.open() == SWT.OK) {

        // Get attribute
        attribute = attributeDialog.getAttribute();

        // Update table item
        tableItem.setText(0, attribute.getName());
        tableItem.setText(1, attribute.getTypeDescription());
        tableItem.setText(2, attribute.getIsRequired() ? "Yes" : "No");

        // Set table item data
        tableItem.setData(attribute);
        
        // Update attribute button status
        updateAttributeButtonStatus();    
        
        // Validate definition wizard page
        validate();
      }
    }
    
  }
  
  /**
   * Deletes an attribute using the attribute dialog.
   */
  private void deleteAttribute() {

    // Create confirmation messasge box
    MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
    messageBox.setText("Delete Confirmation");
    messageBox.setMessage("Delete the selected attributes?");
    
    if (messageBox.open() == SWT.YES) {
      
      // Get selected indices
      int[] selectedIndices = attributeTable.getSelectionIndices();
      
      // Remove attributes
      if (selectedIndices != null && selectedIndices.length > 0) {
        attributeTable.remove(selectedIndices);
      }

      // Update attribute button status
      updateAttributeButtonStatus();
      
      // Validate definition wizard page
      validate();
    }
  }
  
  /**
   * Resizes attribute table columns.
   */
  private void resizeAttributeColumns() {
    
    // Get client area of table
    Rectangle clientArea = attributeTable.getClientArea();
    
    // Calculate column width for first two columns
    int columnWidth = (clientArea.width - 80) / 2;
    
    // Set column widths
    attributeTable.getColumn(0).setWidth(columnWidth);
    attributeTable.getColumn(1).setWidth(columnWidth);
    attributeTable.getColumn(2).setWidth(80);
  }
  
  /**
   * Validates the definition page.
   */
  private void validate() {
    
    if (projectCombo.getSelectionIndex() < 0) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Project is a required field.");
    }
    else if (!packageNameValidator.isValid("Package", packageText.getText())) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage(packageNameValidator.getErrorMessage());
    }
    else if (!resourceNameValidator.isValid("Name", nameText.getText())) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage(resourceNameValidator.getErrorMessage());
    }
    else if (attributeTable.getItemCount() == 0) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("At least one attribute is required.");
    }
    else {
      
      // Set page as complete and clear error message
      setPageComplete(true);
      setErrorMessage(null);
    }
  }
  
  /**
   * Returns the selected project.
   * @return IProject Project.
   */
  @SuppressWarnings("unchecked")
  public IProject getProject() {
    
    // Initialize return value
    IProject project = null;
    
    // Get currently selected project
    int selectedIndex = projectCombo.getSelectionIndex();
    
    if (selectedIndex >= 0) {
      
      // Get currently selected project name
      String projectName = projectCombo.getItem(selectedIndex);
      
      // Get project data and lookup selected project
      Map<String, IProject> projectData = (Map<String, IProject>)projectCombo.getData();
      project = projectData.get(projectName);
    }

    return project;
  }

  /**
   * Returns the package name.
   * @return String Package name.
   */
  private String getPackageName() {
    return packageText.getText();
  }

  /**
   * Returns the entity name.
   * @return String Entity name.
   */
  private String getEntityName() {
    return nameText.getText();
  }

  /**
   * Returns the parent project.
   * @return IProject Parent project.
   */
  private String getParentEntityName() {
    
    // Initialize return value
    String parentEntityName = null;
    
    // Get selection index
    int selectedIndex = parentCombo.getSelectionIndex();
    
    // Get parent entity name
    if (selectedIndex >= 0) {
      parentEntityName = parentCombo.getItem(selectedIndex);
    }

    return parentEntityName;
  }

  /**
   * Returns a list of defined attributes.
   * @return List List of defined attributes.
   */
  private java.util.List<Attribute> getAttributes() {
    
    // Initialize return value
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    
    // Get all table items
    TableItem[] items = attributeTable.getItems();
    
    if (items != null) {
      
      for (TableItem item : items) {
        
        // Get attribute
        Attribute attribute = (Attribute)item.getData();
        
        // Add attribute to attributes collection
        attributes.add(attribute);
      }
    }
    
    return attributes;
  }

  /**
   * Returns all definition wizard page data.
   * @return DefinitionWizardData Definition wizard page data.
   */
  public DefinitionWizardData getData() {
  
    // Create data object
    DefinitionWizardData definitionWizardData = new DefinitionWizardData();
    
    // Populate data object
    definitionWizardData.setProject(getProject());
    definitionWizardData.setEntityName(getEntityName());
    definitionWizardData.setPackageName(getPackageName());
    definitionWizardData.setParentEntityName(getParentEntityName());
    definitionWizardData.setAttributes(getAttributes());
    
    return definitionWizardData;
  }
  
/**
 * Selection listener used to respond to changes in project combo selection.
 */
private class ProjectComboSelectionListener implements SelectionListener {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new project combo selection listener using a definition wizard page.
   * @param definitionWizardPage Definition wizard page.
   */
  public ProjectComboSelectionListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }
  
  /**
   * Handles default project combo selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles project combo selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
    // Update parents combo
    definitionWizardPage.populateParents();
    definitionWizardPage.populateCategoryNames();
    
    // Validate definition wizard page
    definitionWizardPage.validate();
  }
  
}

/**
 * Modify listener used to respond to changes in package text.
 */
public class PackageTextModifyListener implements ModifyListener {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new package text modify listener.
   * @param definitionWizardPage Definition wizard page.
   */
  public PackageTextModifyListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }
  
  /**
   * Handles package text modify events.
   * @param modifyEvent Modify event.
   */
  public void modifyText(ModifyEvent modifyEvent) {

    // Validate definition wizard page
    definitionWizardPage.validate();
  }

}

/**
 * Modify listener used to respond to changes in name text.
 */
public class NameTextModifyListener implements ModifyListener {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new name text modify listener.
   * @param definitionWizardPage Definition wizard page.
   */
  public NameTextModifyListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }
  
  /**
   * Handles name text modify events.
   * @param modifyEvent Modify event.
   */
  public void modifyText(ModifyEvent modifyEvent) {

    // Validate definition wizard page
    definitionWizardPage.validate();
  }

}

/**
 * Selection listener used to respond to package button selection.
 */
private class PackageButtonSelectionListener implements SelectionListener {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new package button selection listener using a definition wizard page.
   * @param definitionWizardPage Definition wizard page.
   */
  public PackageButtonSelectionListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }
  
  /**
   * Handles default package button selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles package button selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
    // Update parents combo
    definitionWizardPage.choosePackage();
  }
  
}

/**
 * Control listener used to respond to attribute table resize events.
 */
private class AttributeTableControlListener extends ControlAdapter {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new attribute table control listener using a definition wizard page.
   * @param definitionWizardPage Definition wizard page.
   */
  public AttributeTableControlListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }

  /**
   * Responds to resize events.
   */
  public void controlResized(ControlEvent controlEvent) {
    definitionWizardPage.resizeAttributeColumns();
  }
  
}

/**
 * Selection listener used to respond to attribute table selection.
 */
private class AttributeTableSelectionListener implements SelectionListener {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new attribute table selection listener using a definition wizard page.
   * @param definitionWizardPage Definition wizard page.
   */
  public AttributeTableSelectionListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }
  
  /**
   * Handles default package button selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles package button selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
    // Update parents combo
    definitionWizardPage.updateAttributeButtonStatus();
  }
  
}

/**
 * Selection listener used to respond to add attribute button selection.
 */
private class AddAttributeButtonSelectionListener implements SelectionListener {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new add attribute selection listener using a definition wizard page.
   * @param definitionWizardPage Definition wizard page.
   */
  public AddAttributeButtonSelectionListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }
  
  /**
   * Handles default package button selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles package button selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
    // Update parents combo
    definitionWizardPage.addAttribute();
  }
  
}

/**
 * Selection listener used to respond to edit attribute button selection.
 */
private class EditAttributeButtonSelectionListener implements SelectionListener {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new edit attribute selection listener using a definition wizard page.
   * @param definitionWizardPage Definition wizard page.
   */
  public EditAttributeButtonSelectionListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }
  
  /**
   * Handles default package button selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles package button selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
    // Update parents combo
    definitionWizardPage.editAttribute();
  }
  
}

/**
 * Selection listener used to respond to delete attribute button selection.
 */
private class DeleteAttributeButtonSelectionListener implements SelectionListener {

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage = null;
  
  /**
   * Creates a new delete attribute selection listener using a definition wizard page.
   * @param definitionWizardPage Definition wizard page.
   */
  public DeleteAttributeButtonSelectionListener(DefinitionWizardPage definitionWizardPage) {
    super();
    this.definitionWizardPage = definitionWizardPage;
  }
  
  /**
   * Handles default package button selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles package button selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
    // Update parents combo
    definitionWizardPage.deleteAttribute();

    // Validate definition wizard page
    definitionWizardPage.validate();
  }
  
}

}