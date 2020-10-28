package com.bws.jdistil.project.fragment;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.core.runtime.*;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bws.jdistil.project.fragment.process.ConfigurationInfoUpdater;
import com.bws.jdistil.project.fragment.process.DataSourceFileCreator;
import com.bws.jdistil.project.fragment.process.JspFileCreator;
import com.bws.jdistil.project.fragment.process.SecurityInfoUpdater;
import com.bws.jdistil.project.fragment.process.SqlFileCreator;

/**
 * Creates a relationship between two existing fragments in a JDistil project.
 */
public class FragmentRelationshipWizard extends Wizard implements INewWizard {
	
  /**
   * Selected resource in workbench.
   */
  private IStructuredSelection selection;

  /**
   * Relationship wizard page.
   */
  private RelationshipWizardPage relationshipWizardPage;

	/**
	 * Constructor for fragment relationship wizard.
	 */
	public FragmentRelationshipWizard() {
		super();
		
		// Set window title
		setWindowTitle("Fragment Relationship");
		
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
	 * Add wizard pages.
	 */
	public void addPages() {
	  
	  // Create pages
		relationshipWizardPage = new RelationshipWizardPage(selection);
		
		// Add pages to wizard
		addPage(relationshipWizardPage);
	}

	/**
	 * Create application fragment resources when the wizard finishes.
	 */
  public boolean performFinish() {
    
    // Initialize return value
    boolean isSuccessful = true;
    
    // Get relationship page data
    RelationshipWizardData relationshipWizardData = relationshipWizardPage.getData();
    
    try {
      // Create and execute fragment creator
      RelationshipCreator fragmentCreator = new RelationshipCreator(relationshipWizardData);
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
 * Runnable process with progress information used to create fragment relationship 
 * resources using information defined in the wizard pages.	
 */
private class RelationshipCreator extends WorkspaceModifyOperation {

  /**
   * Relationship wizard data.
   */
  private RelationshipWizardData relationshipWizardData = null;
  
  /**
   * Creates a new relationship creator process.
   */
  public RelationshipCreator(RelationshipWizardData relationshipWizardData) {
    super();
    
    this.relationshipWizardData = relationshipWizardData;
  }
  
  /**
   * Creates fragment relationship resources using information defined in wizard pages.
   * @param progressMonitor Progress monitor.
   */
  public void execute(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
  	
    try {
      // Notify progress monitor of task
      progressMonitor.beginTask("Creating fragment relationship...", 7);

      progressMonitor.subTask("Updating configuration class files...");
      ConfigurationInfoUpdater.process(relationshipWizardData, progressMonitor);
      progressMonitor.worked(1);
      
      progressMonitor.subTask("Updating data source class files...");
      DataSourceFileCreator.process(relationshipWizardData, progressMonitor);
      progressMonitor.worked(1);
      
      progressMonitor.subTask("Updating JSP files...");
      JspFileCreator.process(relationshipWizardData, progressMonitor);
      progressMonitor.worked(1);

      progressMonitor.subTask("Updating SQL files...");
      SqlFileCreator.process(relationshipWizardData, progressMonitor);
      progressMonitor.worked(1);
      
      progressMonitor.subTask("Updating Security Information...");
      SecurityInfoUpdater.process(relationshipWizardData, progressMonitor);
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