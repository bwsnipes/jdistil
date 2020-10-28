package com.bws.jdistil.project.facet;

import com.bws.jdistil.project.validation.PackageNameValidator;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;

/**
 * Wizard page used to capture JDistil project properties.
 */
public class ProjectPropertiesWizardPage extends AbstractFacetWizardPage {

  /**
   * Project configuration.
   */
	private ProjectConfiguration projectConfiguration = null;
	
	/**
	 * Base package text component.
	 */
	private Text basePackageText = null;
	
	/**
	 * Package name validator.
	 */
	private PackageNameValidator packageNameValidator = new PackageNameValidator();
	
	/**
	 * Creates a new project properties page.
	 */
	public ProjectPropertiesWizardPage() {
		super("com.bws.core.jdistil.project.facet");

		// Set title and description
    setTitle("JDistil Project");
    setDescription("Configures a dynamic web project with JDistil resources.");
    
    // Start with page incomplete
    setPageComplete(false);
    setMessage("Enter base package name used for project configuration.");
	}

	/**
	 * Create the project properties wizard page.
	 * @param composite - Parent composite.
	 */
	public void createControl(Composite parent) {
	  
	  // Create containing composite and set layout
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(2, false));

    // Create base package label component
    Label basePackageLabel = new Label(composite, SWT.NONE);
    basePackageLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
    basePackageLabel.setText("Base Package:");

    // Create base package text component
    basePackageText = new Text(composite, SWT.BORDER);
    basePackageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    basePackageText.addModifyListener(new PackageTextModifyListener(this));
    
    // Get base package name
    String basePackageName = projectConfiguration.getBasePackageName();
    
    // Set base package field
    if (basePackageName != null) {
      basePackageText.setText(basePackageName);
    }
    
    // Set containing composite control
    setControl(composite);
	}
	
	/**
	 * Transfer UI data to project configuration.
	 */
	public void transferStateToConfig() {
	  projectConfiguration.setBasePackageName(basePackageText.getText());
	}

  /**
   * Sets the project configuration.
   * @param projectConfiguration Project configuration.
   */
  public void setConfig(Object projectConfiguration) {
    this.projectConfiguration = (ProjectConfiguration)projectConfiguration;
  }

  /**
   * Validates the project properties page.
   */
  private void validate() {
    
    if (!packageNameValidator.isValid("Base Package", basePackageText.getText())) {
      
      // Set page as incomplete and error message
      setPageComplete(false);
      setErrorMessage(packageNameValidator.getErrorMessage());
    }
    else {
      
      // Set page as complete and clear error message
      setPageComplete(true);
      setErrorMessage(null);
    }
  }


/**
 * Modify listener used to respond to changes in package text.
 */
public class PackageTextModifyListener implements ModifyListener {

  /**
   * Project properties wizard page.
   */
  private ProjectPropertiesWizardPage projectPropertiesWizardPage = null;
  
  /**
   * Creates a new package text modify listener.
   * @param projectPropertiesPage Project properties page.
   */
  public PackageTextModifyListener(ProjectPropertiesWizardPage projectPropertiesWizardPage) {
    super();
    this.projectPropertiesWizardPage = projectPropertiesWizardPage;
  }
  
  /**
   * Handles package text modify events.
   * @param modifyEvent Modify event.
   */
  public void modifyText(ModifyEvent modifyEvent) {

    // Validate project properties page
    projectPropertiesWizardPage.validate();
  }

}
  
}
