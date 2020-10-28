package com.bws.jdistil.project.fragment.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

import com.bws.jdistil.project.fragment.DefinitionWizardData;
import com.bws.jdistil.project.fragment.RelationshipWizardData;
import com.bws.jdistil.project.fragment.ViewWizardData;
import com.bws.jdistil.project.fragment.data.Attribute;
import com.bws.jdistil.project.util.ResourceFinder;
import com.bws.jdistil.project.util.ResourceReader;
import com.bws.jdistil.project.util.TextConverter;

/**
 * Adds new actions and fields to the application security SQL file.
 * @author Bryan Snipes
 */
public class SecurityInfoUpdater {

  /**
   * Line separator.
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Resource reader.
   */
  private static ResourceReader resourceReader = new ResourceReader();
  
  /**
   * Insert task template.
   */
  private static String insertTaskTemplate = null;
  
  /**
   * Insert action template.
   */
  private static String insertActionTemplate = null;
  
  /**
   * Insert field template.
   */
  private static String insertFieldTemplate = null;
  
  /**
   * Insert field group template.
   */
  private static String insertFieldGroupTemplate = null;
  
  /**
   * Creates a new security information updater.
   */
  public SecurityInfoUpdater() {
    super();
  }
  
	/**
	 * Loads all templates. 
	 * @throws IOException
	 */
  private static void loadTemplates() throws IOException {
  	
    // Load templates
  	if (insertTaskTemplate == null) {
	    insertTaskTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/security/insert-task.txt");
  	}
    
  	if (insertActionTemplate == null) {
	    insertActionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/security/insert-action.txt");
  	}
    
  	if (insertFieldTemplate == null) {
	    insertFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/security/insert-field.txt");
  	}
    
  	if (insertFieldGroupTemplate == null) {
	    insertFieldGroupTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/security/insert-field-group.txt");
  	}
  }
  
  /**
   * Adds new actions and fields to the application security SQL file.
   * @param definitionWizardPage Definition wizard data.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws IOException
   */
  public static void process(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, IProgressMonitor progressMonitor) 
      throws CoreException, IOException, ProcessException {
    
    // Load templates
    loadTemplates();

    // Get the targeted project
    IProject project = definitionWizardData.getProject();

    // Create java project
    IJavaProject javaProject = JavaCore.create(project);
    
    
    // Find field IDs compilation unit
    ICompilationUnit fieldIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "FieldIds.java");
      
    // Verify compilation unit was found
    if (fieldIdsCompilationUnit == null) {
      throw new ProcessException("Error updating security information: FieldIds class not found.");
    }

    // Get field IDs type
    IType fieldIdsType = fieldIdsCompilationUnit.findPrimaryType();
    
    
    // Find action IDs compilation unit
    ICompilationUnit actionIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "ActionIds.java");
      
    // Verify compilation unit was found
    if (actionIdsCompilationUnit == null) {
      throw new ProcessException("Error updating security information: ActionIds class not found.");
    }

    // Get action IDs type
    IType actionIdsType = actionIdsCompilationUnit.findPrimaryType();
    
    
    // Initialize SQL 
    StringBuffer sql = new StringBuffer();

    // Get entity name
    String entityName = definitionWizardData.getEntityName();
    String entityDescription = TextConverter.convertAsSentence(entityName, false);
    
    // Append comment
    sql.append(LINE_SEPARATOR);
    sql.append("--").append(LINE_SEPARATOR);
    sql.append("-- Security configuration for ").append(entityDescription).append(".").append(LINE_SEPARATOR);
    sql.append("--").append(LINE_SEPARATOR);
    
    // Create task name
    String taskName = "Manage " + entityName;
    
    // Create action names
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
    String viewActionId = "VIEW_" + TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String addActionId = "ADD_" + constantEntityName;
    String editActionId = "EDIT_" + constantEntityName;
    String deleteActionId = "DELETE_" + constantEntityName;
    String saveActionId = "SAVE_" + constantEntityName;
    String cancelActionId = "CANCEL_" + constantEntityName;

    // Lookup secure ID's using action names
    String viewActionSecureId = (String)actionIdsType.getField(viewActionId).getConstant();
    String addActionSecureId = (String)actionIdsType.getField(addActionId).getConstant();
    String editActionSecureId = (String)actionIdsType.getField(editActionId).getConstant();
    String deleteActionSecureId = (String)actionIdsType.getField(deleteActionId).getConstant();
    String saveActionSecureId = (String)actionIdsType.getField(saveActionId).getConstant();
    String cancelActionSecureId = (String)actionIdsType.getField(cancelActionId).getConstant();

    // Format for SQL
    viewActionSecureId = viewActionSecureId.replaceAll("\"", "'");
    addActionSecureId = addActionSecureId.replaceAll("\"", "'");
    editActionSecureId = editActionSecureId.replaceAll("\"", "'");
    deleteActionSecureId = deleteActionSecureId.replaceAll("\"", "'");
    saveActionSecureId = saveActionSecureId.replaceAll("\"", "'");
    cancelActionSecureId = cancelActionSecureId.replaceAll("\"", "'");
    
    // Create insert statement for task 
    String insertTask = insertTaskTemplate.replaceAll("TASK-NAME", taskName);

    // Create insert statement for view action 
    String insertViewAction = insertActionTemplate.replaceAll("SECURE-ID", viewActionSecureId);
    insertViewAction = insertViewAction.replaceAll("TASK-NAME", taskName);

    // Create insert statement for add action 
    String insertAddAction = insertActionTemplate.replaceAll("SECURE-ID", addActionSecureId);
    insertAddAction = insertAddAction.replaceAll("TASK-NAME", taskName);

    // Create insert statement for edit action 
    String insertEditAction = insertActionTemplate.replaceAll("SECURE-ID", editActionSecureId);
    insertEditAction = insertEditAction.replaceAll("TASK-NAME", taskName);

    // Create insert statement for delete action 
    String insertDeleteAction = insertActionTemplate.replaceAll("SECURE-ID", deleteActionSecureId);
    insertDeleteAction = insertDeleteAction.replaceAll("TASK-NAME", taskName);

    // Create insert statement for save action 
    String insertSaveAction = insertActionTemplate.replaceAll("SECURE-ID", saveActionSecureId);
    insertSaveAction = insertSaveAction.replaceAll("TASK-NAME", taskName);

    // Create insert statement for cancel action 
    String insertCancelAction = insertActionTemplate.replaceAll("SECURE-ID", cancelActionSecureId);
    insertCancelAction = insertCancelAction.replaceAll("TASK-NAME", taskName);

    // Append SQL statements
    sql.append(insertTask).append(LINE_SEPARATOR);
    sql.append(insertViewAction).append(LINE_SEPARATOR);
    sql.append(insertAddAction).append(LINE_SEPARATOR);
    sql.append(insertEditAction).append(LINE_SEPARATOR);
    sql.append(insertDeleteAction).append(LINE_SEPARATOR);
    sql.append(insertSaveAction).append(LINE_SEPARATOR);
    sql.append(insertCancelAction).append(LINE_SEPARATOR);
    
    if (viewWizardData.getIsPagingEnabled()) {

      // Create paging action names
      String previousPageActionId = "VIEW_" + constantEntityName + "_PREVIOUS_PAGE";
      String selectPageActionId = "VIEW_" + constantEntityName + "_SELECT_PAGE";
      String nextPageActionId = "VIEW_" + constantEntityName + "_NEXT_PAGE";

      // Lookup secure ID's using paging action names
      String previousPageActionSecureId = (String)actionIdsType.getField(previousPageActionId).getConstant();
      String selectPageActionSecureId = (String)actionIdsType.getField(selectPageActionId).getConstant();
      String nextPageActionSecureId = (String)actionIdsType.getField(nextPageActionId).getConstant();

      // Format for SQL
      previousPageActionSecureId = previousPageActionSecureId.replaceAll("\"", "'");
      selectPageActionSecureId = selectPageActionSecureId.replaceAll("\"", "'");
      nextPageActionSecureId = nextPageActionSecureId.replaceAll("\"", "'");

      // Create insert statement for previous page action 
      String insertPreviousPageAction = insertActionTemplate.replaceAll("SECURE-ID", previousPageActionSecureId);
      insertPreviousPageAction = insertPreviousPageAction.replaceAll("TASK-NAME", taskName);

      // Create insert statement for select page action 
      String insertSelectPageAction = insertActionTemplate.replaceAll("SECURE-ID", selectPageActionSecureId);
      insertSelectPageAction = insertSelectPageAction.replaceAll("TASK-NAME", taskName);

      // Create insert statement for next page action 
      String insertNextPageAction = insertActionTemplate.replaceAll("SECURE-ID", nextPageActionSecureId);
      insertNextPageAction = insertNextPageAction.replaceAll("TASK-NAME", taskName);

      // Append paging SQL statements
      sql.append(insertPreviousPageAction).append(LINE_SEPARATOR);
      sql.append(insertSelectPageAction).append(LINE_SEPARATOR);
      sql.append(insertNextPageAction).append(LINE_SEPARATOR);
    }
    
    
    // Create field group name
    String fieldGroupName = entityName;
    
    // Create insert statement for field group 
    String insertFieldGroup = insertFieldGroupTemplate.replaceAll("FIELD-GROUP", fieldGroupName);

    // Append field group SQL statement
    sql.append(insertFieldGroup).append(LINE_SEPARATOR);


    // Get entity attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();

    for (Attribute attribute : attributes) {
      
      // Use attribute name as displayed field name 
    	String fieldName = attribute.getName();

      // Create field ID
      String fieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(attribute.getName());
      
      // Lookup secure ID using field ID
      String fieldSecureId = (String)fieldIdsType.getField(fieldId).getConstant();

      // Format for SQL
      fieldSecureId = fieldSecureId.replaceAll("\"", "'");
      
      // Create insert statement for field 
      String insertField = insertFieldTemplate.replaceAll("FIELD-NAME", fieldName);
      insertField = insertField.replaceAll("SECURE-ID", fieldSecureId);
      insertField = insertField.replaceAll("FIELD-GROUP", fieldGroupName);

      // Append field SQL statement
      sql.append(insertField).append(LINE_SEPARATOR);
    }      

    
    // Get SQL folder
    IFolder folder = project.getFolder(new Path("sql"));

    // Convert SQL into input stream
    ByteArrayInputStream sqlInputStream = new ByteArrayInputStream(sql.toString().getBytes());
    
    // Get application security SQL file and append SQL content
    IFile securitySqlFile = folder.getFile("app-security.sql");
    securitySqlFile.appendContents(sqlInputStream, true, false, progressMonitor);
  }

  /**
   * Adds new fields to the application security SQL file for a fragment relationship.
   * @param relationshipWizardData Relationship wizard data.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws IOException
   */
  public static void process(RelationshipWizardData relationshipWizardData, IProgressMonitor progressMonitor) 
      throws CoreException, IOException, ProcessException {
  
    // Load templates
    loadTemplates();

    // Get the targeted project
    IProject project = relationshipWizardData.getProject();

    // Create java project
    IJavaProject javaProject = JavaCore.create(project);
    
    // Find field IDs compilation unit
    ICompilationUnit fieldIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "FieldIds.java");
      
    // Verify compilation unit was found
    if (fieldIdsCompilationUnit == null) {
      throw new ProcessException("Error updating security information: FieldIds class not found.");
    }

    // Get field IDs type
    IType fieldIdsType = fieldIdsCompilationUnit.findPrimaryType();
    
    
    // Get entity name
    String entity1Name = relationshipWizardData.getDataObject1Name();

    // Get base entity name
    String baseEntity1Name = TextConverter.getBaseClassName(entity1Name);
    
    // Get entity name
    String entity2Name = relationshipWizardData.getDataObject2Name();

    // Get base entity name
    String baseEntity2Name = TextConverter.getBaseClassName(entity2Name);

    
    // Set many to many association indicator
    boolean isManyToManyAssociation = relationshipWizardData.getAssociationType().equals(RelationshipWizardData.MANY_TO_MANY);

    // Add relationship field for entity 1 
    addRelationshipField(baseEntity1Name, baseEntity2Name, project, fieldIdsType, isManyToManyAssociation, progressMonitor);

    if (relationshipWizardData.isBidirectional()) {
    
      // Add relationship field for entity 2 
      addRelationshipField(baseEntity2Name, baseEntity1Name, project, fieldIdsType, isManyToManyAssociation, progressMonitor);
    }    
  }

  /**
   * Adds new fields to the application security SQL file for a fragment relationship.
   * @param primaryEntityName Primary entity name.
   * @param secondaryEntityName Secondary entity name.
   * @param project Project.
   * @param fieldIdsType Field IDs type.
   * @param isManyToManyAssociation Many to many association indicator.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws ProcessException
   * @throws IOException
   */
  private static void addRelationshipField(String primaryEntityName, String secondaryEntityName,
  		IProject project, IType fieldIdsType, boolean isManyToManyAssociation, IProgressMonitor progressMonitor) 
  		throws CoreException, ProcessException, IOException {
    
    // Convert to primary base names
    String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
    String primaryConstantName = TextConverter.convertCommonToConstant(primaryCommonName);

    // Convert to secondary base names
    String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
    String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);
    
    // Initialize SQL 
    StringBuffer sql = new StringBuffer();
      
	  // Create field name 
		String fieldName = secondaryCommonName;
	
	  // Pluralize field name
	  if (isManyToManyAssociation) {
	  	fieldName = TextConverter.convertToPlural(fieldName);
	  }

	  // Create field ID
	  String fieldId = primaryConstantName + "_" + secondaryConstantName + "_" + "ID";

	  // Pluralize field ID
	  if (isManyToManyAssociation) {
	  	fieldId = fieldId + "S";
	  }
	  
	  // Lookup secure ID using field ID
	  String fieldSecureId = (String)fieldIdsType.getField(fieldId).getConstant();
	
	  // Format for SQL
	  fieldSecureId = fieldSecureId.replaceAll("\"", "'");
	  
	  // Create insert statement for field 
	  String insertField = insertFieldTemplate.replaceAll("FIELD-NAME", fieldName);
	  insertField = insertField.replaceAll("SECURE-ID", fieldSecureId);
	  insertField = insertField.replaceAll("FIELD-GROUP", primaryCommonName);
	
	  // Append field SQL statement
	  sql.append(insertField).append(LINE_SEPARATOR);


    // Get SQL folder
    IFolder folder = project.getFolder(new Path("sql"));

    // Convert SQL into input stream
    ByteArrayInputStream sqlInputStream = new ByteArrayInputStream(sql.toString().getBytes());
    
    // Get application security SQL file and append SQL content
    IFile securitySqlFile = folder.getFile("app-security.sql");
    securitySqlFile.appendContents(sqlInputStream, true, false, progressMonitor);
  }

}
