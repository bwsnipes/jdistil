package com.bws.jdistil.project.fragment.process;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import com.bws.jdistil.project.fragment.DefinitionWizardData;
import com.bws.jdistil.project.fragment.ViewWizardData;
import com.bws.jdistil.project.fragment.data.Attribute;
import com.bws.jdistil.project.fragment.data.AttributeTypes;
import com.bws.jdistil.project.util.ResourceFinder;
import com.bws.jdistil.project.util.ResourceReader;
import com.bws.jdistil.project.util.TextConverter;

/**
 * Creates process class files supporting the new entity defined in the application fragment wizard.
 * @author Bryan Snipes
 */
public class ProcessFileCreator {

  /**
   * Line separator.
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Creates a new process file creator.
   */
  public ProcessFileCreator() {
    super();
  }
  
  /**
   * Creates process class files for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  public static void process(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Get the targeted project
    IProject project = definitionWizardData.getProject();
    
    // Create java project
    IJavaProject javaProject = JavaCore.create(project);

    // Create resource reader
    ResourceReader resourceReader = new ResourceReader();
    
    // Initialize configuration package name
    String configurationPackageName = null;
    
    // Find configuration compilation unit
    IPackageFragment configurationPackageFragment = ResourceFinder.findPackageFragment(javaProject, "configuration");
    
    if (configurationPackageFragment != null) {
      configurationPackageName = configurationPackageFragment.getElementName();
    }
    else {

      // Throw error message
      throw new ProcessException("Error creating process files: Unable to locate configuration package.");
    }

    // Create process classes
    createViewEntitiesProcess(definitionWizardData, viewWizardData, javaProject, resourceReader, configurationPackageName, progressMonitor);
    createSelectEntitiesProcess(definitionWizardData, viewWizardData, javaProject, resourceReader, configurationPackageName, progressMonitor);
    createDeleteEntityProcess(definitionWizardData, viewWizardData, javaProject, resourceReader, configurationPackageName, progressMonitor);
    createEditEntityProcess(definitionWizardData, viewWizardData, javaProject, resourceReader, configurationPackageName, progressMonitor);
    createSaveEntityProcess(definitionWizardData, viewWizardData, javaProject, resourceReader, configurationPackageName, progressMonitor);
  }
  
  /**
   * Creates a view entities process class file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  private static void createViewEntitiesProcess(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
      IJavaProject javaProject, ResourceReader resourceReader, String configurationPackageName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Initialize view entities template
    String viewEntitiesTemplate = null;
    
    // Select view entities template based on paging
    if (viewWizardData.getIsPagingEnabled()) {
      viewEntitiesTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/view-entities-paging.txt");
    }
    else {
      viewEntitiesTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/view-entities.txt");
    }
    
    // Add configuration package name
    String viewEntities = viewEntitiesTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
    
    // Get package name and entity name
    String packageName = definitionWizardData.getPackageName();
    String entityName = definitionWizardData.getEntityName();
    
    // Create camel case based resource names
    String camelCaseEntityName = TextConverter.convertCommonToCamel(entityName, false);
    String viewClassName = "View" + TextConverter.convertToPlural(camelCaseEntityName);
    String managerClassName = camelCaseEntityName + "Manager";
    
    // Create constant case based resource names
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
    String attributeName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String pageName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
    
    // Set template variables
    viewEntities = viewEntities.replaceAll("PACKAGE-NAME", packageName);
    viewEntities = viewEntities.replaceAll("ENTITY-NAME", camelCaseEntityName);
    viewEntities = viewEntities.replaceAll("VIEW-CLASS-NAME", viewClassName);
    viewEntities = viewEntities.replaceAll("MANAGER-CLASS-NAME", managerClassName);
    viewEntities = viewEntities.replaceAll("ATTRIBUTE-NAME", attributeName);
    viewEntities = viewEntities.replaceAll("PAGE-NAME", pageName);
    
    // Set paging specific variables
    if (viewWizardData.getIsPagingEnabled()) {

      // Create constant case based resource names
      String viewActionName = "VIEW_" + TextConverter.convertToPlural(constantEntityName).toUpperCase();
      String deleteActionName = "DELETE_" + constantEntityName;
      String saveActionName = "SAVE_" + constantEntityName;
      String cancelActionName = "CANCEL_" + constantEntityName;
      String pagingActionName = "VIEW_" + constantEntityName;
      String currentPageNumberFieldName = constantEntityName + "_CURRENT_PAGE_NUMBER";
      String selectedPageNumberFieldName = constantEntityName + "_SELECTED_PAGE_NUMBER";
      String previousPageActionName = pagingActionName + "_PREVIOUS_PAGE";
      String nextPageActionName = pagingActionName + "_NEXT_PAGE";
      String selectPageActionName = pagingActionName + "_SELECT_PAGE";

      // Set template variables
      viewEntities = viewEntities.replaceAll("VIEW-ACTION-NAME", viewActionName);
      viewEntities = viewEntities.replaceAll("DELETE-ACTION-NAME", deleteActionName);
      viewEntities = viewEntities.replaceAll("SAVE-ACTION-NAME", saveActionName);
      viewEntities = viewEntities.replaceAll("CANCEL-ACTION-NAME", cancelActionName);
      viewEntities = viewEntities.replaceAll("CURRENT-PAGE-NUMBER-FIELD-NAME", currentPageNumberFieldName);
      viewEntities = viewEntities.replaceAll("SELECTED-PAGE-NUMBER-FIELD-NAME", selectedPageNumberFieldName);
      viewEntities = viewEntities.replaceAll("PREVIOUS-PAGE-ACTION-NAME", previousPageActionName);
      viewEntities = viewEntities.replaceAll("NEXT-PAGE-ACTION-NAME", nextPageActionName);
      viewEntities = viewEntities.replaceAll("SELECT-PAGE-ACTION-NAME", selectPageActionName);
      viewEntities = viewEntities.replaceAll("PAGE-SIZE", viewWizardData.getPageSize());
    }
    
    // Get parent entity ID field name
    String parentIdFieldName = getParentIdFieldName(definitionWizardData);

    // Get filter attribute names
    String[] filterAttributeNames = viewWizardData.getFilterAttributeNames();
    
    if ((parentIdFieldName == null || parentIdFieldName.trim().length() == 0) &&
        (filterAttributeNames == null || filterAttributeNames.length == 0)) {
      
      // Clear filter criteria definition
      viewEntities = viewEntities.replaceAll("FILTER-CRITERIA-DEFINITION", "");
    }
    else {
      
      // Get view templates
      String filterCriteriaStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/filter-criteria-statement.txt");

      // Initialize definition template
      String filterCriteriaDefinitionTemplate = null;
      
      // Set definition template
      if (viewWizardData.getIsPagingEnabled()) {
      	
        filterCriteriaDefinitionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/filter-criteria-definition-paging.txt");
      }
      else {
      	
        filterCriteriaDefinitionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/filter-criteria-definition.txt");
      }
      
      
      // Create sort field and sort direction field names
      String sortFieldFieldName = constantEntityName + "_SORT_FIELD";
      String sortDirectionFieldName = constantEntityName + "_SORT_DIRECTION";
    	
      // Set sort field and sort direction field names
      filterCriteriaDefinitionTemplate = filterCriteriaDefinitionTemplate.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
      filterCriteriaDefinitionTemplate = filterCriteriaDefinitionTemplate.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);

      
      // Initialize filter criteria statements
      StringBuffer filterCriteriaStatements = new StringBuffer();
      
      if (parentIdFieldName != null && parentIdFieldName.trim().length() > 0) {
        
        // Create filter criteria statement
        String filterCriteriaStatement = filterCriteriaStatementTemplate.replaceAll("FIELD-ID", parentIdFieldName);
        filterCriteriaStatement = filterCriteriaStatement.replaceAll("DEFAULT-ID", "null");
        filterCriteriaStatement = filterCriteriaStatement.replaceAll("OPERATOR-ID", "EQUALS");
        filterCriteriaStatement = filterCriteriaStatement.replaceAll("FILTER-ID", parentIdFieldName);
        
        // Add filter criteria statement
        filterCriteriaStatements.append(filterCriteriaStatement).append(LINE_SEPARATOR);
      }
      
      if (filterAttributeNames != null) {
        
        // Get attributes
        Collection<Attribute> attributes = definitionWizardData.getAttributes();
        
        // Create attribute lookup
        Map<String, Attribute> attributeLookup = createAttributeLookup(attributes);
        
        for (String filterAttributeName : filterAttributeNames) {
          
        	// Lookup filter attribute
        	Attribute filterAttribute = attributeLookup.get(filterAttributeName);
        	
          // Get filter attribute type
          String filterAttributeType = filterAttribute.getType();
          
          // Create filter field IDs
          String fieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttributeName);
          String filterFieldId = fieldId + "_FILTER";
          
          // Initialize default operator field ID 
          String defaultOperatorFieldId = "null";
          
          if (filterAttributeType.equalsIgnoreCase(AttributeTypes.TEXT) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.MEMO) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.EMAIL) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.POSTAL_CODE) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.NUMERIC) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.DATE) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.TIME)) {
          	
          	// Set operator field ID 
            defaultOperatorFieldId = "FieldIds." + fieldId + "_FILTER_OPERATOR";
          }
          
          // Initialize operator ID 
          String operatorId = "EQUALS";
          
          if (filterAttributeType.equalsIgnoreCase(AttributeTypes.TEXT) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.MEMO) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.EMAIL) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER)||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.POSTAL_CODE)) {
          	
          	// Set operator field ID 
          	operatorId = "CONTAINS";
          }
          
          // Create filter criteria statement
          String filterCriteriaStatement = filterCriteriaStatementTemplate.replaceAll("FIELD-ID", fieldId);
          filterCriteriaStatement = filterCriteriaStatement.replaceAll("DEFAULT-ID", defaultOperatorFieldId);
          filterCriteriaStatement = filterCriteriaStatement.replaceAll("OPERATOR-ID", operatorId);
          filterCriteriaStatement = filterCriteriaStatement.replaceAll("FILTER-ID", filterFieldId);
          
          // Add filter criteria statement
          filterCriteriaStatements.append(filterCriteriaStatement).append(LINE_SEPARATOR);
        }
      }
      
      // Create filter criteria definition
      String filterCriteriaDefinition = filterCriteriaDefinitionTemplate.replaceAll("FILTER-CRITERIA-STATEMENTS", filterCriteriaStatements.toString());

      // Add filter criteria definition
      viewEntities = viewEntities.replaceAll("FILTER-CRITERIA-DEFINITION", filterCriteriaDefinition);
    }
    
    // Create view entities process file name
    String viewEntitiesProcessFileName = viewClassName + ".java";

    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = ResourceFinder.findSourcePackageFragmentRoot(javaProject);
    
    // Create and open package fragment
    IPackageFragment packageFragment = sourcePackageFragmentRoot.createPackageFragment(packageName, true, progressMonitor);
    packageFragment.open(progressMonitor);
    
    // Create view entities process file
    packageFragment.createCompilationUnit(viewEntitiesProcessFileName, viewEntities, true, progressMonitor);
  }
  
  /**
   * Creates a select entities process class file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  private static void createSelectEntitiesProcess(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
      IJavaProject javaProject, ResourceReader resourceReader, String configurationPackageName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Initialize select entities template
    String selectEntitiesTemplate = null;
    
    // Select select entities template based on paging
    if (viewWizardData.getIsPagingEnabled()) {
      selectEntitiesTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/select-entities-paging.txt");
    }
    else {
      selectEntitiesTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/select-entities.txt");
    }
    
    // Add configuration package name
    String selectEntities = selectEntitiesTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
    
    // Get package name and entity name
    String packageName = definitionWizardData.getPackageName();
    String entityName = definitionWizardData.getEntityName();
    
    // Create camel case based resource names
    String camelCaseEntityName = TextConverter.convertCommonToCamel(entityName, false);
    String selectClassName = "Select" + TextConverter.convertToPlural(camelCaseEntityName);
    String managerClassName = camelCaseEntityName + "Manager";
    
    // Create constant case based resource names
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
    String attributeName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String pageName = constantEntityName + "_SELECTION";
    
    String associateAttributeName = "SELECTED_" + attributeName;
    String associateId = constantEntityName + "_ID";
    String addAssociateId = "SELECT_" + constantEntityName + "_ADD";
    String removeAssociateId = "SELECT_" + constantEntityName + "_REMOVE";
    String closeAssociateId = "SELECT_" + constantEntityName + "_CLOSE";

    // Set template variables
    selectEntities = selectEntities.replaceAll("PACKAGE-NAME", packageName);
    selectEntities = selectEntities.replaceAll("ENTITY-NAME", camelCaseEntityName);
    selectEntities = selectEntities.replaceAll("SELECT-CLASS-NAME", selectClassName);
    selectEntities = selectEntities.replaceAll("MANAGER-CLASS-NAME", managerClassName);
    selectEntities = selectEntities.replaceAll("PRIMARY-ATTRIBUTE-NAME", attributeName);
    selectEntities = selectEntities.replaceAll("PAGE-NAME", pageName);
    selectEntities = selectEntities.replaceAll("ASSOCIATE-ATTRIBUTE-NAME", associateAttributeName);
    selectEntities = selectEntities.replaceAll("SELECTED-ASSOCIATE-ID", associateId);
    selectEntities = selectEntities.replaceAll("ADD-ASSOCIATE-ID", addAssociateId);
    selectEntities = selectEntities.replaceAll("REMOVE-ASSOCIATE-ID", removeAssociateId);
    selectEntities = selectEntities.replaceAll("CLOSE-ASSOCIATE-ID", closeAssociateId);
    
    // Set paging specific variables
    if (viewWizardData.getIsPagingEnabled()) {

      // Create paging action names
      String selectActionName = "SELECT_" + attributeName;
      String basePagingActionName = "SELECT_" + constantEntityName;
      String currentPageNumberFieldName = constantEntityName + "_CURRENT_PAGE_NUMBER";
      String selectedPageNumberFieldName = constantEntityName + "_SELECTED_PAGE_NUMBER";
      String previousPageActionName = basePagingActionName + "_PREVIOUS_PAGE";
      String nextPageActionName = basePagingActionName + "_NEXT_PAGE";
      String selectPageActionName = basePagingActionName + "_SELECT_PAGE";

      // Set template variables
      selectEntities = selectEntities.replaceAll("SELECT-ACTION-NAME", selectActionName);
      selectEntities = selectEntities.replaceAll("CURRENT-PAGE-NUMBER-FIELD-NAME", currentPageNumberFieldName);
      selectEntities = selectEntities.replaceAll("SELECTED-PAGE-NUMBER-FIELD-NAME", selectedPageNumberFieldName);
      selectEntities = selectEntities.replaceAll("PREVIOUS-PAGE-ACTION-NAME", previousPageActionName);
      selectEntities = selectEntities.replaceAll("NEXT-PAGE-ACTION-NAME", nextPageActionName);
      selectEntities = selectEntities.replaceAll("SELECT-PAGE-ACTION-NAME", selectPageActionName);
      selectEntities = selectEntities.replaceAll("PAGE-SIZE", viewWizardData.getPageSize());
    }
    
    // Get parent entity ID field name
    String parentIdFieldName = getParentIdFieldName(definitionWizardData);

    // Get filter attribute names
    String[] filterAttributeNames = viewWizardData.getFilterAttributeNames();
    
    if ((parentIdFieldName == null || parentIdFieldName.trim().length() == 0) &&
        (filterAttributeNames == null || filterAttributeNames.length == 0)) {
      
      // Clear filter criteria definition
      selectEntities = selectEntities.replaceAll("FILTER-CRITERIA-DEFINITION", "");
    }
    else {
      
      // Get view templates
      String filterCriteriaStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/filter-criteria-statement.txt");

      // Initialize definition template
      String filterCriteriaDefinitionTemplate = null;
      
      // Set definition template
      if (viewWizardData.getIsPagingEnabled()) {

        filterCriteriaDefinitionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/filter-criteria-definition-paging.txt");
      }
      else {
        
      	filterCriteriaDefinitionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/filter-criteria-definition.txt");
      }
      
      
      // Create sort field and sort direction field names
      String sortFieldFieldName = constantEntityName + "_SORT_FIELD";
      String sortDirectionFieldName = constantEntityName + "_SORT_DIRECTION";
    	
      // Set sort field and sort direction field names
      filterCriteriaDefinitionTemplate = filterCriteriaDefinitionTemplate.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
      filterCriteriaDefinitionTemplate = filterCriteriaDefinitionTemplate.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);


      // Initialize filter criteria statements
      StringBuffer filterCriteriaStatements = new StringBuffer();
      
      if (parentIdFieldName != null && parentIdFieldName.trim().length() > 0) {
        
        // Create filter criteria statement
        String filterCriteriaStatement = filterCriteriaStatementTemplate.replaceAll("FIELD-ID", parentIdFieldName);
        filterCriteriaStatement = filterCriteriaStatement.replaceAll("DEFAULT-ID", "null");
        filterCriteriaStatement = filterCriteriaStatement.replaceAll("OPERATOR-ID", "EQUALS");
        filterCriteriaStatement = filterCriteriaStatement.replaceAll("FILTER-ID", parentIdFieldName);
        
        // Add filter criteria statement
        filterCriteriaStatements.append(filterCriteriaStatement).append(LINE_SEPARATOR);
      }
      
      if (filterAttributeNames != null) {
        
        // Get attributes
        Collection<Attribute> attributes = definitionWizardData.getAttributes();
        
        // Create attribute lookup
        Map<String, Attribute> attributeLookup = createAttributeLookup(attributes);
        
        for (String filterAttributeName : filterAttributeNames) {
          
        	// Lookup filter attribute
        	Attribute filterAttribute = attributeLookup.get(filterAttributeName);
        	
          // Get filter attribute type
          String filterAttributeType = filterAttribute.getType();
          
          // Create filter field IDs
          String fieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttributeName);
          String filterFieldId = fieldId + "_FILTER";
          
          // Initialize default operator field ID 
          String defaultOperatorFieldId = "null";
          
          if (filterAttributeType.equalsIgnoreCase(AttributeTypes.TEXT) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.MEMO) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.EMAIL) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.POSTAL_CODE) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.NUMERIC) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.DATE) || 
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.TIME)) {
          	
          	// Set operator field ID 
            defaultOperatorFieldId = "FieldIds." + fieldId + "_FILTER_OPERATOR";
          }
          
          // Initialize operator ID 
          String operatorId = "EQUALS";
          
          if (filterAttributeType.equalsIgnoreCase(AttributeTypes.TEXT) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.MEMO) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.EMAIL) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER) ||
          		filterAttributeType.equalsIgnoreCase(AttributeTypes.POSTAL_CODE)) {
          	
          	// Set operator field ID 
          	operatorId = "CONTAINS";
          }
          
          // Create filter criteria statement
          String filterCriteriaStatement = filterCriteriaStatementTemplate.replaceAll("FIELD-ID", fieldId);
          filterCriteriaStatement = filterCriteriaStatement.replaceAll("DEFAULT-ID", defaultOperatorFieldId);
          filterCriteriaStatement = filterCriteriaStatement.replaceAll("OPERATOR-ID", operatorId);
          filterCriteriaStatement = filterCriteriaStatement.replaceAll("FILTER-ID", filterFieldId);
          
          // Add filter criteria statement
          filterCriteriaStatements.append(filterCriteriaStatement).append(LINE_SEPARATOR);
        }
      }
      
      // Create filter criteria definition
      String filterCriteriaDefinition = filterCriteriaDefinitionTemplate.replaceAll("FILTER-CRITERIA-STATEMENTS", filterCriteriaStatements.toString());

      // Add filter criteria definition
      selectEntities = selectEntities.replaceAll("FILTER-CRITERIA-DEFINITION", filterCriteriaDefinition);
    }
    
    // Create view entities process file name
    String viewEntitiesProcessFileName = selectClassName + ".java";

    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = ResourceFinder.findSourcePackageFragmentRoot(javaProject);
    
    // Create and open package fragment
    IPackageFragment packageFragment = sourcePackageFragmentRoot.createPackageFragment(packageName, true, progressMonitor);
    packageFragment.open(progressMonitor);
    
    // Create view entities process file
    packageFragment.createCompilationUnit(viewEntitiesProcessFileName, selectEntities, true, progressMonitor);
  }
  
  /**
   * Creates a delete entity process class file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  private static void createDeleteEntityProcess(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
      IJavaProject javaProject, ResourceReader resourceReader, String configurationPackageName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Get delete entity template
    String deleteEntityTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/delete-entity.txt");
    
    // Add configuration package name
    String deleteEntity = deleteEntityTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
    
    // Get package name and entity name
    String packageName = definitionWizardData.getPackageName();
    String entityName = definitionWizardData.getEntityName();
    
    // Create camel case based resource names
    String camelCaseEntityName = TextConverter.convertCommonToCamel(entityName, false);
    String deleteClassName = "Delete" + camelCaseEntityName;
    String viewClassName = "View" + TextConverter.convertToPlural(camelCaseEntityName);
    String managerClassName = camelCaseEntityName + "Manager";
    
    // Create constant case based resource names
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
    String fieldName = constantEntityName + "_ID";
    
    // Set template variables
    deleteEntity = deleteEntity.replaceAll("PACKAGE-NAME", packageName);
    deleteEntity = deleteEntity.replaceAll("ENTITY-NAME", camelCaseEntityName);
    deleteEntity = deleteEntity.replaceAll("DELETE-CLASS-NAME", deleteClassName);
    deleteEntity = deleteEntity.replaceAll("VIEW-CLASS-NAME", viewClassName);
    deleteEntity = deleteEntity.replaceAll("MANAGER-CLASS-NAME", managerClassName);
    deleteEntity = deleteEntity.replaceAll("FIELD-NAME", fieldName);

    // Create delete entity process file name
    String deleteEntityProcessFileName = deleteClassName + ".java";

    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = ResourceFinder.findSourcePackageFragmentRoot(javaProject);
    
    // Create and open package fragment
    IPackageFragment packageFragment = sourcePackageFragmentRoot.createPackageFragment(packageName, true, progressMonitor);
    packageFragment.open(progressMonitor);
    
    // Create delete entity process file
    packageFragment.createCompilationUnit(deleteEntityProcessFileName, deleteEntity, true, progressMonitor);
  }
  
  /**
   * Creates an edit entity process class file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  private static void createEditEntityProcess(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
      IJavaProject javaProject, ResourceReader resourceReader, String configurationPackageName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Get edit entity template
    String editEntityTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/edit-entity.txt");
    
    // Add configuration package name
    String editEntity = editEntityTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
    
    // Get package name and entity name
    String packageName = definitionWizardData.getPackageName();
    String entityName = definitionWizardData.getEntityName();
    
    // Create camel case based resource names
    String camelCaseEntityName = TextConverter.convertCommonToCamel(entityName, false);
    String editClassName = "Edit" + camelCaseEntityName;
    String viewClassName = "View" + TextConverter.convertToPlural(camelCaseEntityName);
    String entityClassName = camelCaseEntityName;
    String managerClassName = camelCaseEntityName + "Manager";
    
    // Create constant case based resource names
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
    String fieldName = constantEntityName + "_ID";
    String attributeName = constantEntityName;
    String pageName = constantEntityName;
    
    // Set template variables
    editEntity = editEntity.replaceAll("PACKAGE-NAME", packageName);
    editEntity = editEntity.replaceAll("ENTITY-NAME", camelCaseEntityName);
    editEntity = editEntity.replaceAll("EDIT-CLASS-NAME", editClassName);
    editEntity = editEntity.replaceAll("ENTITY-CLASS-NAME", entityClassName);
    editEntity = editEntity.replaceAll("MANAGER-CLASS-NAME", managerClassName);
    editEntity = editEntity.replaceAll("FIELD-NAME", fieldName);
    editEntity = editEntity.replaceAll("ATTRIBUTE-NAME", attributeName);
    editEntity = editEntity.replaceAll("PAGE-NAME", pageName);
    editEntity = editEntity.replaceAll("VIEW-CLASS-NAME", viewClassName);

    // Create edit entity process file name
    String editEntityProcessFileName = editClassName + ".java";

    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = ResourceFinder.findSourcePackageFragmentRoot(javaProject);
    
    // Create and open package fragment
    IPackageFragment packageFragment = sourcePackageFragmentRoot.createPackageFragment(packageName, true, progressMonitor);
    packageFragment.open(progressMonitor);
    
    // Create edit entity process file
    packageFragment.createCompilationUnit(editEntityProcessFileName, editEntity, true, progressMonitor);
  }
  
  /**
   * Creates a save entity process class file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  private static void createSaveEntityProcess(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
      IJavaProject javaProject, ResourceReader resourceReader, String configurationPackageName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Get save entity template
    String saveEntityTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/process/save-entity.txt");
    
    // Add configuration package name
    String saveEntity = saveEntityTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
    
    // Get package name and entity name
    String packageName = definitionWizardData.getPackageName();
    String entityName = definitionWizardData.getEntityName();
    
    // Create camel case based resource names
    String camelCaseEntityName = TextConverter.convertCommonToCamel(entityName, false);
    String saveClassName = "Save" + camelCaseEntityName;
    String viewClassName = "View" + TextConverter.convertToPlural(camelCaseEntityName);
    String editClassName = "Edit" + camelCaseEntityName;
    String entityClassName = camelCaseEntityName;
    String managerClassName = camelCaseEntityName + "Manager";
    
    // Create constant case based resource names
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
    String fieldName = constantEntityName + "_ID";
    String attributeName = constantEntityName;
    
    // Set template variables
    saveEntity = saveEntity.replaceAll("PACKAGE-NAME", packageName);
    saveEntity = saveEntity.replaceAll("ENTITY-NAME", camelCaseEntityName);
    saveEntity = saveEntity.replaceAll("SAVE-CLASS-NAME", saveClassName);
    saveEntity = saveEntity.replaceAll("ENTITY-CLASS-NAME", entityClassName);
    saveEntity = saveEntity.replaceAll("MANAGER-CLASS-NAME", managerClassName);
    saveEntity = saveEntity.replaceAll("FIELD-NAME", fieldName);
    saveEntity = saveEntity.replaceAll("ATTRIBUTE-NAME", attributeName);
    saveEntity = saveEntity.replaceAll("VIEW-CLASS-NAME", viewClassName);
    saveEntity = saveEntity.replaceAll("EDIT-CLASS-NAME", editClassName);

    // Create save entity process file name
    String saveEntityProcessFileName = saveClassName + ".java";

    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = ResourceFinder.findSourcePackageFragmentRoot(javaProject);
    
    // Create and open package fragment
    IPackageFragment packageFragment = sourcePackageFragmentRoot.createPackageFragment(packageName, true, progressMonitor);
    packageFragment.open(progressMonitor);
    
    // Create save entity process file
    packageFragment.createCompilationUnit(saveEntityProcessFileName, saveEntity, true, progressMonitor);
  }
  
  /**
   * Returns the parent entity ID field name.
   * @param definitionWizardData Definition wizard data.
   * @return String Parent entity ID field name.
   */
  private static String getParentIdFieldName(DefinitionWizardData definitionWizardData) {
    
    // Initialize return value
    String parentIdFieldName = null;
    
    // Get parent entity name
    String parentEntityName = definitionWizardData.getParentEntityName();
    
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Get index of last package separator
      int index = parentEntityName.lastIndexOf(".");
      
      // Remove package from parent entity name
      if (index > 0) {
        parentEntityName = parentEntityName.substring(index + 1);
      }

      // Get parent entity ID field name
      String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      String parentEntityConstantName = TextConverter.convertCommonToConstant(parentEntityCommonName);
      parentIdFieldName = parentEntityConstantName + "_ID";
    }
    
    return parentIdFieldName;
  }
  
  /**
   * Creates and returns an attribute lookup keyed by attribute name.
   * @param attributes Collection of attributes.
   * @return Map Map of attributes keyed by attribute name.
   */
  private static Map<String, Attribute> createAttributeLookup(Collection<Attribute> attributes) {
  
    // Initialize return value
    Map<String, Attribute> attributeLookup = new HashMap<String, Attribute>();
    
    // Populate lookup
    for (Attribute attribute : attributes) {
      attributeLookup.put(attribute.getName(), attribute);
    }
    
    return attributeLookup;
  }
  
}
