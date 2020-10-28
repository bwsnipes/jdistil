package com.bws.jdistil.project.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import com.bws.jdistil.project.util.TextConverter;

/**
 * Captures an fragment relationship. 
 */
public class RelationshipWizardPage extends WizardPage {
	
  /**
   * Project selected in workbench.
   */
  private IProject selectedProject;

  /**
   * List of projects containing the JDistil fragment.
   */
  private Combo projectCombo = null;
	
  /**
   * List of source data objects.
   */
  private Combo dataObject1Combo = null;

  /**
   * List of source data object attributes.
   */
  private Combo attribute1Combo = null;

  /**
   * Include source attribute in view indicator.
   */
  private Button attribute1IncludeInViewCheckbox = null;
  
  /**
   * List of association types.
   */
  private Combo associationTypeCombo = null;

  /**
   * Bidirectional indicator.
   */
  private Button bidirectionalCheckbox = null;

  /**
   * List of target data objects.
   */
  private Combo dataObject2Combo = null;

  /**
   * List of target data object attributes.
   */
  private Combo attribute2Combo = null;

  /**
   * Required target attribute indicator.
   */
  private Button attribute2RequiredCheckbox = null;
  
  /**
   * Include target attribute in view indicator.
   */
  private Button attribute2IncludeInViewCheckbox = null;
  
  /**
	 * Creates a new relationship wizard page.
	 * @param selection Object selected in workbench.
	 */
	public RelationshipWizardPage(IStructuredSelection selection) {
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
		setDescription("Captures a relationship definition between two application fragments.");

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
    GridLayout gridLayout = new GridLayout(3, false);
    gridLayout.verticalSpacing = 20;
	  composite.setLayout(gridLayout);
		
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
    
    // Create fragment 1 group
    Group fragment1Group = new Group(composite, SWT.SHADOW_ETCHED_IN);
    fragment1Group.setText("Source Fragment");
    fragment1Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 2));
    fragment1Group.setLayout(new GridLayout(3, false));
    
    // Create data object 1 label
    Label dataObject1Label = new Label(fragment1Group, SWT.NONE);
    dataObject1Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    dataObject1Label.setText("Data Object:");

    // Create data object 1 list
    dataObject1Combo = new Combo(fragment1Group, SWT.DROP_DOWN | SWT.READ_ONLY);
    dataObject1Combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    // Create label for column spacer
    Label spacer2Label = new Label(fragment1Group, SWT.NONE);
    spacer2Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
    // Create attribute 1 label
    Label attribute1Label = new Label(fragment1Group, SWT.NONE);
    attribute1Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    attribute1Label.setText("UI Attribute:");

    // Create attribute 1 list
    attribute1Combo = new Combo(fragment1Group, SWT.DROP_DOWN | SWT.READ_ONLY);
    attribute1Combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    attribute1Combo.setEnabled(true);
    
    // Create label for column spacer
    Label spacer3Label = new Label(fragment1Group, SWT.NONE);
    spacer3Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

    // Create attribute 1 include in view label
    Label attribute1IncludeInViewLabel = new Label(fragment1Group, SWT.NONE);
    attribute1IncludeInViewLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    attribute1IncludeInViewLabel.setText("Include in View:");

    // Create attribute 1 include in view check box
    attribute1IncludeInViewCheckbox = new Button(fragment1Group, SWT.CHECK);
    attribute1IncludeInViewCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    attribute1IncludeInViewCheckbox.setEnabled(false);
    
    // Create label for column spacer
    Label spacer4Label = new Label(fragment1Group, SWT.NONE);
    spacer4Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
    // Create configuration group
    Group configurationGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
    configurationGroup.setText("Configuration");
    configurationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 2));
    configurationGroup.setLayout(new GridLayout(3, false));
    
    // Create association label
    Label associationLabel = new Label(configurationGroup, SWT.NONE);
    associationLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    associationLabel.setText("Association:");

    // Create association list
    associationTypeCombo = new Combo(configurationGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
    associationTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    // Create label for column spacer
    Label spacer5Label = new Label(configurationGroup, SWT.NONE);
    spacer5Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
    // Create bidirectional label
    Label bidirectionalLabel = new Label(configurationGroup, SWT.NONE);
    bidirectionalLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    bidirectionalLabel.setText("Bidirectional:");

    // Create bidirectional check box
    bidirectionalCheckbox = new Button(configurationGroup, SWT.CHECK);
    bidirectionalCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

    // Create label for column spacer
    Label spacer6Label = new Label(configurationGroup, SWT.NONE);
    spacer6Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
    // Create fragment 2 group
    Group fragment2Group = new Group(composite, SWT.SHADOW_ETCHED_IN);
    fragment2Group.setText("Target Fragment");
    fragment2Group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 2));
    fragment2Group.setLayout(new GridLayout(3, false));
    
    // Create data object 2 label
    Label dataObject2Label = new Label(fragment2Group, SWT.NONE);
    dataObject2Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    dataObject2Label.setText("Data Object:");

    // Create data object 2 list
    dataObject2Combo = new Combo(fragment2Group, SWT.DROP_DOWN | SWT.READ_ONLY);
    dataObject2Combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    // Create label for column spacer
    Label spacer7Label = new Label(fragment2Group, SWT.NONE);
    spacer7Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
    // Create attribute 2 label
    Label attribute2Label = new Label(fragment2Group, SWT.NONE);
    attribute2Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    attribute2Label.setText("UI Attribute:");

    // Create attribute 2 list
    attribute2Combo = new Combo(fragment2Group, SWT.DROP_DOWN | SWT.READ_ONLY);
    attribute2Combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    
    // Create label for column spacer
    Label spacer8Label = new Label(fragment2Group, SWT.NONE);
    spacer8Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    
    // Create attribute 2 required label
    Label attribute2RequiredLabel = new Label(fragment2Group, SWT.NONE);
    attribute2RequiredLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    attribute2RequiredLabel.setText("Required:");

    // Create attribute 2 required check box
    attribute2RequiredCheckbox = new Button(fragment2Group, SWT.CHECK);
    attribute2RequiredCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

    // Create label for column spacer
    Label spacer9Label = new Label(fragment2Group, SWT.NONE);
    spacer9Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

    // Create attribute 2 include in view label
    Label attribute2IncludeInViewLabel = new Label(fragment2Group, SWT.NONE);
    attribute2IncludeInViewLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    attribute2IncludeInViewLabel.setText("Include in View:");

    // Create attribute 2 include in view check box
    attribute2IncludeInViewCheckbox = new Button(fragment2Group, SWT.CHECK);
    attribute2IncludeInViewCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

    // Create label for column spacer
    Label spacer10Label = new Label(fragment2Group, SWT.NONE);
    spacer10Label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

	  // Populate reference data
	  populateProjects();
	  populateDataObjects();
	  populateAssociationTypes();
	  
	  // Register project combo listener after reference data is loaded
    projectCombo.addSelectionListener(new ProjectComboSelectionListener(this));
    
    // Add data object selection listeners
    dataObject1Combo.addSelectionListener(new DataObjectComboSelectionListener(this, attribute1Combo));
    dataObject2Combo.addSelectionListener(new DataObjectComboSelectionListener(this, attribute2Combo));
	  
    // Add configuration listeners
    associationTypeCombo.addSelectionListener(new AssociationTypeComboSelectionListener(this));
    bidirectionalCheckbox.addSelectionListener(new BidirectionalCheckboxSelectionListener(this));
    
    // Add attribute selection listeners
    AttributeComboSelectionListener attributeComboSelectionListener = new AttributeComboSelectionListener(this);
    attribute1Combo.addSelectionListener(attributeComboSelectionListener);
    attribute2Combo.addSelectionListener(attributeComboSelectionListener);
    
    // Pack composite to preferred size
    composite.pack();
    
    // Set composite to page
		setControl(composite);
	}

	private void populateAssociationTypes() {
	
		associationTypeCombo.add(RelationshipWizardData.MANY_TO_ONE);
		associationTypeCombo.add(RelationshipWizardData.MANY_TO_MANY);
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
   * Populates the data object combos with the names of classes implementing 
   * the DataObject interface in the currently selected project.
   */
  @SuppressWarnings("unchecked")
  private void populateDataObjects() {
    
    // Clear data object combos
    dataObject1Combo.removeAll();
    dataObject2Combo.removeAll();
    
    // Clear attribute combos
    attribute1Combo.removeAll();
    attribute2Combo.removeAll();

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
            
          	// Create list of data object names
          	List<String> dataObjectNames = new ArrayList<String>();
          	
            for (IType type : types) {
            	
              // Add source types only
              if (!type.isBinary()) {
              	dataObjectNames.add(type.getFullyQualifiedName());
              }
            }

            // Sort data object names
            Collections.sort(dataObjectNames);
            
            // Add data object names to combos
            for (String dataObjectName : dataObjectNames) {
              dataObject1Combo.add(dataObjectName);
              dataObject2Combo.add(dataObjectName);
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
   * Populates the attribute combos with the attribute names from the classes implementing 
   * the DataObject interface in the currently selected project.
   */
  @SuppressWarnings("unchecked")
  private void populateAttributes(Combo dataObjectCombo, Combo attributeCombo) {
    
    // Clear existing values
  	attributeCombo.removeAll();
    
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
          
        	String dataObjectClassName = dataObjectCombo.getText();
        	
          // Get data object type
          IType dataObjectType = javaProject.findType(dataObjectClassName);

          if (dataObjectType != null) {
          	
          	// Get fields
            IField[] fields = dataObjectType.getFields();
            
            if (fields != null) {
            	
            	for (IField field : fields) {
            		
            		// Get formatted attribute name
            		String attributeName = TextConverter.convertCamelToCommon(field.getElementName());
            		
            		// Add attribute name to combo
            		attributeCombo.add(attributeName);
            	}
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
   * Enables and disables fields based on selected values.
   */
  private void updateFields() {
  	
  	boolean isManyToManyAssociation = false;
  	
  	// Get selected index
  	int index = associationTypeCombo.getSelectionIndex();
  	
  	if (index >= 0) {
  		
  		// Check for many to many association type
    	String association = associationTypeCombo.getItem(index);
    	isManyToManyAssociation = association != null && association.equals(RelationshipWizardData.MANY_TO_MANY);
  	}

  	if (isManyToManyAssociation) {
  		
  		bidirectionalCheckbox.setEnabled(true);
  	}
  	else {
  		
  		bidirectionalCheckbox.setEnabled(false);
  		bidirectionalCheckbox.setSelection(false);
  	}
  	
  	// Get bidirectional indicator and source attribute selection
  	boolean isBidirectional = bidirectionalCheckbox.getSelection();
  	
  	if (isBidirectional) {
  		
  		attribute1Combo.setEnabled(true);
  		attribute1IncludeInViewCheckbox.setEnabled(true);
  	}
  	else {
  		
  		attribute1Combo.setEnabled(false);
  		attribute1Combo.select(-1);
  		attribute1IncludeInViewCheckbox.setEnabled(false);
  		attribute1IncludeInViewCheckbox.setSelection(false);
  	}
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
    else if (dataObject1Combo.getSelectionIndex() < 0) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Source fragment data object is a required field.");
    }
    else if (associationTypeCombo.getSelectionIndex() < 0) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Association is a required field.");
    }
    else if (isSourceAttributeError()) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Source fragment attribute is a required field.");
    }
    else if (dataObject2Combo.getSelectionIndex() < 0) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Target fragment data object is a required field.");
    }
    else if (attribute2Combo.getSelectionIndex() < 0) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Target fragment attribute is a required field.");
    }
    else if (dataObject1Combo.getSelectionIndex() == dataObject2Combo.getSelectionIndex()) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Source and target data objects must be different.");
    }
    else if (isAlreadyDefined()) {
    	
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage("Fragment relationship between source and target data objects already exists.");
    }
    else {
      
      // Set page as complete and clear error message
      setPageComplete(true);
      setErrorMessage(null);
    }
  }
  
  private boolean isSourceAttributeError() {
  	
  	// Initialize return value
  	boolean isError = false;
  	
  	// Get selected index
  	int index = associationTypeCombo.getSelectionIndex();
  	
  	if (index >= 0) {
  		
  		// Check for many to many association type
    	String association = associationTypeCombo.getItem(index);
    	boolean isManyToManyAssociation = association != null && association.equals(RelationshipWizardData.MANY_TO_MANY);
    	
    	// Get bidirectional indicator and source attribute selection
    	boolean isBidirectional = bidirectionalCheckbox.getSelection();
    	boolean isSourceAttributeSelected = attribute1Combo.getSelectionIndex() >= 0;
    	
    	isError = isManyToManyAssociation && isBidirectional && !isSourceAttributeSelected;
  	}

  	return isError;
  }
  
  /**
   * Returns a value indicating whether or not a fragment relationship already exists for the two selected data objects. 
   * @return boolean Existing fragment relationship indicator.
   */
  private boolean isAlreadyDefined() {
  
  	// Initialize return value
  	boolean isAlreadyDefined = false;
  	
    // Get currently selected project
    int selectedIndex = projectCombo.getSelectionIndex();
    
    if (selectedIndex >= 0) {
      
      // Get currently selected project name
      String projectName = projectCombo.getItem(selectedIndex);
      
      // Get project data and lookup selected project
      @SuppressWarnings("unchecked")
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
          
        	// Get selected data object class names
        	String dataObject1ClassName = dataObject1Combo.getText();
        	String dataObject2ClassName = dataObject2Combo.getText();
        	
          // Get data object types
          IType dataObject1Type = javaProject.findType(dataObject1ClassName);
          IType dataObject2Type = javaProject.findType(dataObject2ClassName);

          if (dataObject1Type != null && dataObject2Type != null) {
          	
          	// Get fields
            IField[] fields = dataObject1Type.getFields();
            
            if (fields != null) {
            	
            	// Build target field name using second data object class name
            	String targetFieldName = TextConverter.getBaseClassName(dataObject2ClassName) + "Ids";
            	
            	for (IField field : fields) {
            		
            		// Get next field name
            		String fieldName = field.getElementName();
            		
            		if (fieldName.equalsIgnoreCase(targetFieldName)) {
            			
            			isAlreadyDefined = true;
            			break;
            		}
            	}
            }
          }
        }
      }
      catch (CoreException coreException) {
        
        // Display error message
        MessageDialog.openError(getShell(), "Error", coreException.getMessage());
      }
    }
    
  	return isAlreadyDefined;
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
   * Returns the data object 1 name.
   * @return String Data object 1 name.
   */
  private String getDataObject1Name() {
    
    // Initialize return value
    String dataObjectName = null;
    
    // Get selection index
    int selectedIndex = dataObject1Combo.getSelectionIndex();
    
    // Get data object name
    if (selectedIndex >= 0) {
    	dataObjectName = dataObject1Combo.getItem(selectedIndex);
    }

    return dataObjectName;
  }

  /**
   * Returns the attribute 1 name.
   * @return String Attribute 1 name.
   */
  private String getAttribute1Name() {
    
    // Initialize return value
    String attributeName = null;
    
    // Get selection index
    int selectedIndex = attribute1Combo.getSelectionIndex();
    
    // Get attribute name
    if (selectedIndex >= 0) {
    	attributeName = attribute1Combo.getItem(selectedIndex);
    }

    return attributeName;
  }

  /**
   * Returns the attribute 1 include in view indicator.
   * @return boolean Attribute 1 include in view indicator.
   */
  private boolean getAttribute1IncludeInView() {
    
    return attribute1IncludeInViewCheckbox.getSelection();
  }

  /**
   * Returns the association type.
   * @return String Association type.
   */
  private String getAssociationType() {
    
    // Initialize return value
    String associationType = null;
    
    // Get selection index
    int selectedIndex = associationTypeCombo.getSelectionIndex();
    
    // Get association type
    if (selectedIndex >= 0) {
    	associationType = associationTypeCombo.getItem(selectedIndex);
    }

    return associationType;
  }

  /**
   * Returns the bidirectional indicator.
   * @return boolean Bidirectional indicator.
   */
  private boolean getBidirectional() {
    
    return bidirectionalCheckbox.getSelection();
  }

  /**
   * Returns the data object 2 name.
   * @return String Data object 2 name.
   */
  private String getDataObject2Name() {
    
    // Initialize return value
    String dataObjectName = null;
    
    // Get selection index
    int selectedIndex = dataObject2Combo.getSelectionIndex();
    
    // Get data object name
    if (selectedIndex >= 0) {
    	dataObjectName = dataObject2Combo.getItem(selectedIndex);
    }

    return dataObjectName;
  }

  /**
   * Returns the attribute 2 name.
   * @return String Attribute 2 name.
   */
  private String getAttribute2Name() {
    
    // Initialize return value
    String attributeName = null;
    
    // Get selection index
    int selectedIndex = attribute2Combo.getSelectionIndex();
    
    // Get attribute name
    if (selectedIndex >= 0) {
    	attributeName = attribute2Combo.getItem(selectedIndex);
    }

    return attributeName;
  }

  /**
   * Returns the attribute 2 required indicator.
   * @return boolean Attribute 2 required indicator.
   */
  private boolean getAttribute2Required() {
    
    return attribute2RequiredCheckbox.getSelection();
  }

  /**
   * Returns the attribute 2 include in view indicator.
   * @return boolean Attribute 2 include in view indicator.
   */
  private boolean getAttribute2IncludeInView() {
    
    return attribute2IncludeInViewCheckbox.getSelection();
  }

  /**
   * Returns all relationship wizard page data.
   * @return RelationshipWizardData Relationship wizard page data.
   */
  public RelationshipWizardData getData() {
  
    // Create wizard data
    RelationshipWizardData relationshipWizardData = new RelationshipWizardData();
    
    // Populate data object
    relationshipWizardData.setProject(getProject());
    relationshipWizardData.setDataObject1Name(getDataObject1Name());
    relationshipWizardData.setAttribute1Name(getAttribute1Name());
    relationshipWizardData.setAttribute1IncludeInView(getAttribute1IncludeInView());
    relationshipWizardData.setAssociationType(getAssociationType());
    relationshipWizardData.setBidirectional(getBidirectional());
    relationshipWizardData.setDataObject2Name(getDataObject2Name());
    relationshipWizardData.setAttribute2Name(getAttribute2Name());
    relationshipWizardData.setAttribute2Required(getAttribute2Required());
    relationshipWizardData.setAttribute2IncludeInView(getAttribute2IncludeInView());
    
    return relationshipWizardData;
  }
  
/**
 * Selection listener used to respond to changes in project combo selection.
 */
private class ProjectComboSelectionListener implements SelectionListener {

  /**
   * Relationship wizard page.
   */
  private RelationshipWizardPage relationshipWizardPage = null;
  
  /**
   * Creates a new project combo selection listener using a relationship wizard page.
   * @param relationshipWizardPage Relationship wizard page.
   */
  public ProjectComboSelectionListener(RelationshipWizardPage relationshipWizardPage) {
    super();
    this.relationshipWizardPage = relationshipWizardPage;
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
    
    // Update data object combos
    relationshipWizardPage.populateDataObjects();
    
    // Validate relationship wizard page
    relationshipWizardPage.validate();
  }
  
}

/**
 * Selection listener used to respond to changes in data object combo selection.
 */
private class DataObjectComboSelectionListener implements SelectionListener {

  /**
   * Relationship wizard page.
   */
  private RelationshipWizardPage relationshipWizardPage = null;
  
  /**
   * Attribute combo.
   */
  private Combo attributeCombo = null;
  
  /**
   * Creates a new data object combo selection listener using a relationship wizard page.
   * @param relationshipWizardPage Relationship wizard page.
   */
  public DataObjectComboSelectionListener(RelationshipWizardPage relationshipWizardPage, Combo attributeCombo) {
    super();
    
    this.relationshipWizardPage = relationshipWizardPage;
    this.attributeCombo = attributeCombo;
  }
  
  /**
   * Handles default data object combo selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles data object combo selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
  	// Cast source to combo
  	Combo dataObjectCombo = (Combo)selectionEvent.getSource();
  	
    // Update attribute combos
    relationshipWizardPage.populateAttributes(dataObjectCombo, attributeCombo);
    
    // Validate relationship wizard page
    relationshipWizardPage.validate();
  }
  
}

/**
 * Selection listener used to respond to changes in attribute combo selection.
 */
private class AttributeComboSelectionListener implements SelectionListener {

  /**
   * Relationship wizard page.
   */
  private RelationshipWizardPage relationshipWizardPage = null;
  
  /**
   * Creates a new attribute combo selection listener using a relationship wizard page.
   * @param relationshipWizardPage Relationship wizard page.
   */
  public AttributeComboSelectionListener(RelationshipWizardPage relationshipWizardPage) {
    super();
    this.relationshipWizardPage = relationshipWizardPage;
  }
  
  /**
   * Handles default attribute combo selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles attribute combo selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
    // Validate page
    relationshipWizardPage.validate();
  }
  
}

/**
 * Selection listener used to respond to changes in association type combo selection.
 */
private class AssociationTypeComboSelectionListener implements SelectionListener {

  /**
   * Relationship wizard page.
   */
  private RelationshipWizardPage relationshipWizardPage = null;
  
  /**
   * Creates a new association type combo selection listener using a relationship wizard page.
   * @param relationshipWizardPage Relationship wizard page.
   */
  public AssociationTypeComboSelectionListener(RelationshipWizardPage relationshipWizardPage) {
    super();
    this.relationshipWizardPage = relationshipWizardPage;
  }
  
  /**
   * Handles default association type combo selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles association type combo selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
  	// Update fields
  	relationshipWizardPage.updateFields();

  	// Validate page
    relationshipWizardPage.validate();
  }
  
}

/**
 * Selection listener used to respond to changes in bidirectional checkbox combo selection.
 */
private class BidirectionalCheckboxSelectionListener implements SelectionListener {

  /**
   * Relationship wizard page.
   */
  private RelationshipWizardPage relationshipWizardPage = null;
  
  /**
   * Creates a new bidirectional checkbox selection listener using a relationship wizard page.
   * @param relationshipWizardPage Relationship wizard page.
   */
  public BidirectionalCheckboxSelectionListener(RelationshipWizardPage relationshipWizardPage) {
    super();
    this.relationshipWizardPage = relationshipWizardPage;
  }
  
  /**
   * Handles default bidirectional checkbox selection event.
   */
  public void widgetDefaultSelected(SelectionEvent selectionEvent) {
    widgetSelected(selectionEvent);
  }

  /**
   * Handles bidirectional checkbox selection event.
   */
  public void widgetSelected(SelectionEvent selectionEvent) {
    
  	// Update fields
  	relationshipWizardPage.updateFields();

  	// Validate page
    relationshipWizardPage.validate();
  }
  
}

}