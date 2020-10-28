package com.bws.jdistil.project.fragment;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.core.runtime.*;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;

import com.bws.jdistil.project.fragment.process.ConfigurationInfoUpdater;
import com.bws.jdistil.project.fragment.process.DataSourceFileCreator;
import com.bws.jdistil.project.fragment.process.JspFileCreator;
import com.bws.jdistil.project.fragment.process.LookupInfoUpdater;
import com.bws.jdistil.project.fragment.process.ProcessFileCreator;
import com.bws.jdistil.project.fragment.process.SecurityInfoUpdater;
import com.bws.jdistil.project.fragment.process.SqlFileCreator;

/**
 * Creates an application fragment in a JDistil project.
 */
public class ApplicationFragmentWizard extends Wizard implements INewWizard, IPageChangedListener {
	
  /**
   * Selected resource in workbench.
   */
  private IStructuredSelection selection;

  /**
   * Definition wizard page.
   */
  private DefinitionWizardPage definitionWizardPage;

  /**
   * View wizard page.
   */
  private ViewWizardPage viewWizardPage;

	/**
	 * Constructor for application fragment wizard.
	 */
	public ApplicationFragmentWizard() {
		super();
		
		// Set window title
		setWindowTitle("Application Fragment");
		
		// Enable use of progress monitor
		setNeedsProgressMonitor(true);
	}
	
  /**
   * Initializes the wizard with references to the workbench and any selected resources.
   * @param workbench Current workbench.
   * @param selection Object selected in workbench.
   * @see org.eclipse.IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection selection) {

    // Set selected resource
    this.selection = selection;
  }

  /**
   * Overridden to register as a page change listener.
   */
  public void setContainer(IWizardContainer wizardContainer) {
    super.setContainer(wizardContainer);
    
    if (wizardContainer != null) {

      // Register as a page change listener
      WizardDialog wizardDialog = (WizardDialog)wizardContainer;
      wizardDialog.addPageChangedListener(this);
    }
  }
  
  /**
   * Handle page change event.
   */
  public void pageChanged(PageChangedEvent event) {
    
    // Get selected page
    Object selectedPage = event.getSelectedPage();
    
    if (selectedPage != null && selectedPage.equals(viewWizardPage)) {
      
      // Get definition wizard data
      DefinitionWizardData definitionWizardData = definitionWizardPage.getData();
      
      // Set defined attributes on view wizard page
      viewWizardPage.populateAttributes(definitionWizardData.getAttributes());
    }
  }
  
  /**
	 * Add wizard pages.
	 */
	public void addPages() {
	  
	  // Create pages
		definitionWizardPage = new DefinitionWizardPage(selection);
    viewWizardPage = new ViewWizardPage(selection);
		
		// Add pages to wizard
		addPage(definitionWizardPage);
    addPage(viewWizardPage);
	}

	/**
	 * Create application fragment resources when the wizard finishes.
	 */
  public boolean performFinish() {
    
    // Initialize return value
    boolean isSuccessful = true;
    
    // Get definition and view page data
    DefinitionWizardData definitionWizardData = definitionWizardPage.getData();
    ViewWizardData viewWizardData = viewWizardPage.getData();
    
    try {
      // Create and execute fragment creator
      FragmentCreator fragmentCreator = new FragmentCreator(definitionWizardData, viewWizardData);
      getContainer().run(true, false, fragmentCreator);
    } 
    catch (InterruptedException interruptedException) {
      
      // Set success indicator
      isSuccessful = false;
    } 
    catch (InvocationTargetException invocationTargetException) {

      // Set success indicator
      isSuccessful = false;

      // Display target exception
      Throwable targetException = invocationTargetException.getTargetException();
      MessageDialog.openError(getShell(), "Error", targetException.getMessage());
    }
    
    return isSuccessful;
  }


/**
 * Runnable process with progress information used to create application fragment 
 * resources using information defined in the wizard pages.	
 */
private class FragmentCreator extends WorkspaceModifyOperation {

  /**
   * Definition wizard data.
   */
  private DefinitionWizardData definitionWizardData = null;
  
  /**
   * View wizard data.
   */
  private ViewWizardData viewWizardData = null;
  
  /**
   * Creates a new fragment creator process.
   */
  public FragmentCreator(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData) {
    super();
    
    this.definitionWizardData = definitionWizardData;
    this.viewWizardData = viewWizardData;
  }
  
  /**
   * Creates application fragment resources using information defined in wizard pages.
   * @param progressMonitor Progress monitor.
   */
  public void execute(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
    
    try {
      // Notify progress monitor of task
      progressMonitor.beginTask("Creating application fragment...", 7);

      
      progressMonitor.subTask("Updating configuration class files...");
      ConfigurationInfoUpdater.process(definitionWizardData, viewWizardData, progressMonitor);
      progressMonitor.worked(1);
      
      
      progressMonitor.subTask("Creating data source class files...");
      DataSourceFileCreator.process(definitionWizardData, viewWizardData, progressMonitor);
      progressMonitor.worked(1);
      
      
      progressMonitor.subTask("Creating process class files...");
      ProcessFileCreator.process(definitionWizardData, viewWizardData, progressMonitor);
      progressMonitor.worked(1);
      
      
      progressMonitor.subTask("Creating JSP files...");
      JspFileCreator.process(definitionWizardData, viewWizardData, progressMonitor);
      progressMonitor.worked(1);

      
      progressMonitor.subTask("Updating Lookup Information...");
      LookupInfoUpdater.process(definitionWizardData, progressMonitor);
      progressMonitor.worked(1);

      
      progressMonitor.subTask("Creating SQL files...");
      SqlFileCreator.process(definitionWizardData, progressMonitor);
      progressMonitor.worked(1);
      

      progressMonitor.subTask("Updating Security Information...");
      SecurityInfoUpdater.process(definitionWizardData, viewWizardData, progressMonitor);
      progressMonitor.worked(1);
    }
    catch (Exception exception) {
      
      // Throw invocation target exception for any encountered exception 
      throw new InvocationTargetException(exception);
    }
    finally {

      // Notify progress monitor of task completion
      progressMonitor.done();
    }
  }
  
}

}