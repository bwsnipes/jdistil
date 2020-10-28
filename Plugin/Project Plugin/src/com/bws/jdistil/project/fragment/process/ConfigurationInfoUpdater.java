package com.bws.jdistil.project.fragment.process;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.text.edits.InsertEdit;
  

import com.bws.jdistil.project.fragment.DefinitionWizardData;
import com.bws.jdistil.project.fragment.RelationshipWizardData;
import com.bws.jdistil.project.fragment.ViewWizardData;
import com.bws.jdistil.project.fragment.data.Attribute;
import com.bws.jdistil.project.fragment.data.AttributeTypes;
import com.bws.jdistil.project.fragment.data.LookupAttribute;
import com.bws.jdistil.project.fragment.data.MemoAttribute;
import com.bws.jdistil.project.fragment.data.NumericAttribute;
import com.bws.jdistil.project.util.ResourceFinder;
import com.bws.jdistil.project.util.ResourceReader;
import com.bws.jdistil.project.util.TextConverter;

/**
 * Updates all configuration class files.
 * @author Bryan Snipes
 */
public class ConfigurationInfoUpdater {

  /**
   * Line separator.
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Field value prefix.
   */
  private static final String FIELD_PREFIX = "F";

  /**
   * Action value prefix.
   */
  private static final String ACTION_PREFIX = "A";

  /**
   * Page value prefix.
   */
  private static final String PAGE_PREFIX = "P";

  /**
   * Resource reader.
   */
  private static ResourceReader resourceReader = new ResourceReader();
  
  /**
   * Configuration template.
   */
  private static String configurationTemplate = null;
  
  /**
   * Constant template.
   */
  private static String constantTemplate = null;
  
  /**
   * Field template.
   */
  private static String fieldTemplate = null;
  
  /**
   * Add number rule template.
   */
  private static String addNumberRuleTemplate = null;
  
  /**
   * Add max length rule template.
   */
  private static String addMaxLengthRuleTemplate = null;
  
  /**
   * Add converter rule template.
   */
  private static String addConverterRuleTemplate = null;
  
  /**
   * Add email rule template.
   */
  private static String addEmailRuleTemplate = null;
  
  /**
   * Add phone number rule template.
   */
  private static String addPhoneNumberRuleTemplate = null;
  
  /**
   * Add postal code rule template.
   */
  private static String addPostalCodeRuleTemplate = null;
  
  /**
   * Add field to fields template.
   */
  private static String addFieldToFieldsTemplate = null;
  
  /**
   * Action template.
   */
  private static String actionTemplate = null;
  
  /**
   * Add processor factory template.
   */
  private static String addProcessorFactoryTemplate = null;
  
  /**
   * Add field to action template.
   */
  private static String addFieldToActionTemplate = null;
  
  /**
   * Add action to actions template.
   */
  private static String addActionToActionsTemplate = null;
  
  /**
   * Page template.
   */
  private static String pageTemplate = null;
  
  /**
   * Add page to pages template.
   */
  private static String addPageToPagesTemplate = null;
  
  /**
   * Object binding template.
   */
  private static String objectBindingTemplate = null;
  
  /**
   * Add field to bindings template.
   */
  private static String addFieldToBindingTemplate = null;
  
  /**
   * Add collection field to bindings template.
   */
  private static String addCollectionFieldToBindingTemplate = null;
  
  /**
   * Add binding to bindings template.
   */
  private static String addBindingToBindingsTemplate = null;
  
  /**
   * Creates a new configuration information updater.
   */
  public ConfigurationInfoUpdater() {
    super();
  }
  
	/**
	 * Loads all templates. 
	 * @throws IOException
	 */
  private static void loadTemplates() throws IOException {
  	
    // Load templates
  	if (configurationTemplate == null) {
  		configurationTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/configuration.txt");
  	}

  	if (constantTemplate == null) {
      constantTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/constant.txt");
  	}

  	if (fieldTemplate == null) {
  		fieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/field.txt");
  	}

  	if (addFieldToFieldsTemplate == null) {
  		addFieldToFieldsTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-field-to-fields.txt");
  	}

  	if (addNumberRuleTemplate == null) {
  		addNumberRuleTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-number-rule.txt");
  	}

  	if (addMaxLengthRuleTemplate == null) {
  		addMaxLengthRuleTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-max-length-rule.txt");
  	}

  	if (addConverterRuleTemplate == null) {
  		addConverterRuleTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-converter-rule.txt");
  	}

  	if (addEmailRuleTemplate == null) {
  		addEmailRuleTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-email-rule.txt");
  	}

  	if (addPhoneNumberRuleTemplate == null) {
  		addPhoneNumberRuleTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-phone-number-rule.txt");
  	}

  	if (addPostalCodeRuleTemplate == null) {
  		addPostalCodeRuleTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-postal-code-rule.txt");
  	}

  	if (actionTemplate == null) {
  		actionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/action.txt");
  	}

  	if (addProcessorFactoryTemplate == null) {
  		addProcessorFactoryTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-processor-factory.txt");
  	}

  	if (addFieldToActionTemplate == null) {
  		addFieldToActionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-field-to-action.txt");
  	}

  	if (addActionToActionsTemplate == null) {
  		addActionToActionsTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-action-to-actions.txt");
  	}

  	if (pageTemplate == null) {
  		pageTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/page.txt");
  	}

  	if (addPageToPagesTemplate == null) {
  		addPageToPagesTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-page-to-pages.txt");
  	}

  	if (objectBindingTemplate == null) {
  		objectBindingTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/object-binding.txt");
  	}

  	if (addFieldToBindingTemplate == null) {
  		addFieldToBindingTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-field-to-binding.txt");
  	}

  	if (addCollectionFieldToBindingTemplate == null) {
  		addCollectionFieldToBindingTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-collection-field-to-binding.txt");
  	}

  	if (addBindingToBindingsTemplate == null) {
  		addBindingToBindingsTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/configuration/add-binding-to-bindings.txt");
  	}

  }
  
  /**
   * Updates all configuration class files.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
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
    
    // Create fragment configuration
    ICompilationUnit fragmentConfigurationUnit = createFragmentConfiguration(javaProject, definitionWizardData, progressMonitor);
    
    // Add fragment configuration to application configuration
    addFragmentConfiguration(javaProject, definitionWizardData, progressMonitor);
    
    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    // Create attribute lookup
    Map<String, Attribute> attributeLookup = createAttributeLookup(attributes);
    
    // Update specific configuration information
    updateFieldConfiguration(javaProject, definitionWizardData, viewWizardData, attributeLookup, fragmentConfigurationUnit, progressMonitor);
    updateActionConfiguration(javaProject, definitionWizardData, viewWizardData, attributeLookup, fragmentConfigurationUnit, progressMonitor);
    updatePageConfiguration(javaProject, definitionWizardData, viewWizardData, attributeLookup, fragmentConfigurationUnit, progressMonitor);
    updateAttributeConfiguration(javaProject, definitionWizardData, viewWizardData, attributeLookup, fragmentConfigurationUnit, progressMonitor);
    updateObjectBindingConfiguration(javaProject, definitionWizardData, viewWizardData, attributeLookup, fragmentConfigurationUnit, progressMonitor);
  }

  /**
   * Updates configuration class files for a fragment relationship.
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
    
    // Update specific configuration information
    updateFieldConfiguration(javaProject, relationshipWizardData, progressMonitor);
    updateActionConfiguration(javaProject, relationshipWizardData, progressMonitor);
    updateObjectBindingConfiguration(javaProject, relationshipWizardData, progressMonitor);
  }

  private static ICompilationUnit createFragmentConfiguration(IJavaProject javaProject, DefinitionWizardData definitionWizardData, IProgressMonitor progressMonitor) 
  			throws CoreException, IOException, ProcessException {
  	
    // Find configuration package
    IPackageFragment configurationPackage = ResourceFinder.findPackageFragment(javaProject, "configuration");

    // Verify compilation unit was found
    if (configurationPackage == null || !configurationPackage.exists()) {
      throw new ProcessException("Error updating configuration information: Configuration package not found.");
    }

    // Get configuration package name
    String configurationPackageName = configurationPackage.getElementName();
    
    // Get package and entity names
    String entityPackageName = definitionWizardData.getPackageName();
    String entityName = TextConverter.convertCommonToCamel(definitionWizardData.getEntityName(), false);
    
    // Create template variables
    String entityClassName = entityName + "Configuration";
    
    // Create configuration 
    String configuration = configurationTemplate.replaceAll("CONFIG-PACKAGE-NAME", configurationPackageName);
    configuration = configuration.replaceAll("ENTITY-PACKAGE-NAME", entityPackageName);
    configuration = configuration.replaceAll("ENTITY-NAME", entityName);

    // Create configuration file name
    String configurationFileName = entityClassName + ".java";

    // Retrieve fragments package fragment
    IPackageFragment fragmentsPackage = ResourceFinder.findPackageFragment(javaProject, "configuration.fragments");
    
    // Verify package was found
    if (fragmentsPackage == null) {
      throw new ProcessException("Error updating configuration information: Fragment configuration package not found.");
    }

    // Open fragments package
    fragmentsPackage.open(progressMonitor);
    
    // Create configuration class
    ICompilationUnit fragmentConfigurationCompilationUnit = fragmentsPackage.createCompilationUnit(configurationFileName, configuration, true, progressMonitor);
    
    return fragmentConfigurationCompilationUnit;
  }

  private static void addFragmentConfiguration(IJavaProject javaProject, DefinitionWizardData definitionWizardData, IProgressMonitor progressMonitor) 
			throws CoreException, IOException, ProcessException {
		
	  // Get entity name
	  String entityName = definitionWizardData.getEntityName();
	  
	  // Create template variables
	  String entityClassName = TextConverter.convertCommonToCamel(entityName, false) + "Configuration";
	
	  // Create fragment reference statement
	  String fragmentReferenceStatement = "  fragmentConfigurations.add(new " + entityClassName + "());" + LINE_SEPARATOR;
	
	  // Find configuration compilation unit
	  ICompilationUnit configurationCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "Configuration.java");
	    
	  // Verify compilation unit was found
	  if (configurationCompilationUnit == null || !configurationCompilationUnit.exists()) {
	    throw new ProcessException("Error updating configuration information: Configuration class not found.");
	  }
	
	  // Get configuration type
	  IType configurationType = configurationCompilationUnit.findPrimaryType();
	  
	  // Get constructor method from configuration type
	  IMethod constructorMethod = configurationType.getMethod("Configuration", new String[] {});
	
	  // Get source range
	  ISourceRange sourceRange = constructorMethod.getSourceRange();
	  
	  // Get method source
	  String methodSource = constructorMethod.getSource();
	  
	  // Get position of last brace
	  int bracePosition = methodSource.lastIndexOf("}");
	  
	  // Calculate edit position
	  int editPosition = sourceRange.getOffset() + bracePosition;
	
	  // Get working copy
	  ICompilationUnit workingCopy = configurationCompilationUnit.getWorkingCopy(progressMonitor);
	  
	  try {
	    // Create insert edit containing new method text
	    InsertEdit insertEdit = new InsertEdit(editPosition, fragmentReferenceStatement);
	
	    // Apply text edit
	    workingCopy.applyTextEdit(insertEdit, progressMonitor);
	    workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, progressMonitor);
	    workingCopy.commitWorkingCopy(true, progressMonitor);
	  }
	  finally {
	    
	    // Discard working copy
	    workingCopy.discardWorkingCopy();
	  }
	}
  
  /**
   * Updates field configuration information. 
   * @param javaProject Java project.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param attributeLookup Map of attributes keyed by attribute name.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updateFieldConfiguration(IJavaProject javaProject, DefinitionWizardData definitionWizardData, 
      ViewWizardData viewWizardData, Map<String, Attribute> attributeLookup, ICompilationUnit configurationCompilationUnit, 
      IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Find field IDs compilation unit
    ICompilationUnit fieldIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "FieldIds.java");
      
    // Verify compilation unit was found
    if (fieldIdsCompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: FieldIds class not found.");
    }

    // Create configuration text
    StringBuffer configurationText = new StringBuffer();

    // Get field IDs type
    IType fieldIdsType = fieldIdsCompilationUnit.findPrimaryType();
    
    // Initialize field index
    int fieldIndex = 1;
    
    // Get all fields
    IField[] fields = fieldIdsType.getFields();
    
    if (fields != null) {

      // Set field index if fields exist
    	fieldIndex = fields.length + 1;
    }
    
    // Get entity name
    String entityName = definitionWizardData.getEntityName();

    // Create constant entity name
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);

    // Create ID and version field IDs
    String idFieldId = constantEntityName + "_ID";
    String versionFieldId = constantEntityName + "_VERSION";
  
    // Create field ID constants
    createConstant(fieldIdsType, idFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
    createConstant(fieldIdsType, versionFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
    
    // Create ID field and version field statements
    String idFieldStatements = createIdFieldStatements(idFieldId);
    String versionFieldStatements = createVersionFieldStatements(versionFieldId);
    
    // Append ID field and version field statements
    configurationText.append(idFieldStatements);
    configurationText.append(versionFieldStatements);

    
    // Create sort field field ID
    String sortFieldFieldId = constantEntityName + "_SORT_FIELD";
    
    // Create sort field field ID constant
    createConstant(fieldIdsType, sortFieldFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
    
    // Create sort field field label
    String sortFieldFieldLabel = TextConverter.convertConstantToCommon("SORT_FIELD");
    
    // Create sort field field statements
    String sortFieldFieldStatements = createStringFieldStatements(sortFieldFieldId, sortFieldFieldLabel);
    
    // Append sort field field statements
    configurationText.append(sortFieldFieldStatements);


    // Create sort direction field ID
    String sortDirectionFieldId = constantEntityName + "_SORT_DIRECTION";
    
    // Create sort direction field ID constant
    createConstant(fieldIdsType, sortDirectionFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
    
    // Create sort direction field label
    String sortDirectionFieldLabel = TextConverter.convertConstantToCommon("SORT_DIRECTION");
    
    // Create sort direction field statements
    String sortDirectionFieldStatements = createStringFieldStatements(sortDirectionFieldId, sortDirectionFieldLabel);
    
    // Append sort direction field statements
    configurationText.append(sortDirectionFieldStatements);


    if (viewWizardData.isPagingEnabled) {
  		
      // Create current page number field ID
      String currentPageNumberFieldId = constantEntityName + "_CURRENT_PAGE_NUMBER";
      
      // Create current page number field ID constant
      createConstant(fieldIdsType, currentPageNumberFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
      
      // Create current page number label
      String currentPageNumberLabel = TextConverter.convertConstantToCommon("CURRENT_PAGE_NUMBER");
      
      // Create current page number field statements
      String currentPageNumberFieldStatements = createStringFieldStatements(currentPageNumberFieldId, currentPageNumberLabel);
      
      // Append current page number field statements
      configurationText.append(currentPageNumberFieldStatements);


      // Create selected page number field ID
      String selectedPageNumberFieldId = constantEntityName + "_SELECTED_PAGE_NUMBER";
      
      // Create selected page number field ID constant
      createConstant(fieldIdsType, selectedPageNumberFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
      
      // Create selected page number label
      String selectedPageNumberLabel = TextConverter.convertConstantToCommon("SELECTED_PAGE_NUMBER");
      
      // Create selected page number field statements
      String selectedPageNumberFieldStatements = createStringFieldStatements(selectedPageNumberFieldId, selectedPageNumberLabel);
      
      // Append selected page number field statements
      configurationText.append(selectedPageNumberFieldStatements);
  	}
  	
    
    // Get filter attribute names
    String[] filterAttributeNames = viewWizardData.getFilterAttributeNames();
    
    if (filterAttributeNames != null && filterAttributeNames.length > 0) {
    	
      // Create group state field ID
      String groupStateFieldId = constantEntityName + "_GROUP_STATE";
      
      // Create group state field ID constant
      createConstant(fieldIdsType, groupStateFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
      
      // Create group state label
      String groupStateLabel = TextConverter.convertConstantToCommon("GROUP_STATE");
      
      // Create group state field statements
      String groupStateFieldStatements = createStringFieldStatements(groupStateFieldId, groupStateLabel);
      
      // Append group state field statements
      configurationText.append(groupStateFieldStatements);


      for (String filterAttributeName : filterAttributeNames) {
        
        // Create constant name
        String constantName = TextConverter.convertCommonToConstant(filterAttributeName);

        // Create filter field ID
        String filterFieldId = constantEntityName + "_" + constantName + "_FILTER";
        
        // Create filter field name
        String filterFieldName = TextConverter.convertConstantToCommon(constantName);

        // Create filter field ID constant
        createConstant(fieldIdsType, filterFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
        
        // Lookup filter attribute
        Attribute filterAttribute = attributeLookup.get(filterAttributeName);

        // Create filter field statements
        String filterFieldStatements = createFieldStatements(filterFieldId, filterFieldName, filterAttribute, true);
        
        // Append filter field statements
        configurationText.append(filterFieldStatements);
        
        
        // Get filter attribute type
        String filterAttributeType = filterAttribute.getType();
        
        if (filterAttributeType.equalsIgnoreCase(AttributeTypes.DATE) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.EMAIL) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.MEMO) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.NUMERIC) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.POSTAL_CODE) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.TEXT) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.TIME)) {
        	
          // Create operator field ID
          String operatorFieldId = constantEntityName + "_" + constantName + "_FILTER_OPERATOR";
          
          // Create operator field ID constant
          createConstant(fieldIdsType, operatorFieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);
          
          // Create operator field statements
          String operatorFieldStatements = createOperatorFieldStatements(operatorFieldId);
          
          // Append operator field statements
          configurationText.append(operatorFieldStatements);
        }
      }
    }
    
    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    for (Attribute attribute : attributes) {
      
      // Create constant name
      String constantName = TextConverter.convertCommonToConstant(attribute.getName());
      
      // Create field ID
      String fieldId = constantEntityName + "_" + constantName;
      
      // Create field name
      String fieldName = TextConverter.convertConstantToCommon(constantName);

      // Create field ID constant
      createConstant(fieldIdsType, fieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);

      // Create field statements
      String fieldStatements = createFieldStatements(fieldId, fieldName, attribute, false);
      
      // Append field statements
      configurationText.append(fieldStatements);
    }
    
    // Add spacing to support brace indentation
    configurationText.append("  ");
    
    // Get configuration type
    IType configurationType = configurationCompilationUnit.findPrimaryType();
    
    // Get register fields method from configuration type
    IMethod registerFieldsMethod = configurationType.getMethod("registerFields", new String[] {"QSet<QField;>;"});

    // Get source range
    ISourceRange sourceRange = registerFieldsMethod.getSourceRange();
    
    // Get method source
    String methodSource = registerFieldsMethod.getSource();
    
    // Get position of last brace
    int bracePosition = methodSource.lastIndexOf("}");
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bracePosition;

    // Get working copy
    ICompilationUnit workingCopy = configurationCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, configurationText.toString());

      // Apply text edit
      workingCopy.applyTextEdit(insertEdit, progressMonitor);
      workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, progressMonitor);
      workingCopy.commitWorkingCopy(true, progressMonitor);
    }
    finally {
      
      // Discard working copy
      workingCopy.discardWorkingCopy();
    }
  }

  /**
   * Updates field configuration information for a fragment relationship. 
   * @param javaProject Java project.
   * @param relationshipWizardData Relationship wizard data.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updateFieldConfiguration(IJavaProject javaProject, RelationshipWizardData relationshipWizardData, IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Find field IDs compilation unit
    ICompilationUnit fieldIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "FieldIds.java");
      
    // Verify compilation unit was found
    if (fieldIdsCompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: FieldIds class not found.");
    }

    // Get field IDs type
    IType fieldIdsType = fieldIdsCompilationUnit.findPrimaryType();
    
    // Initialize field index
    int fieldIndex = 1;
    
    // Get all fields
    IField[] fields = fieldIdsType.getFields();
    
    if (fields != null) {

      // Set field index if fields exist
    	fieldIndex = fields.length + 1;
    }
    
    // Get entity name
    String entity1Name = relationshipWizardData.getDataObject1Name();

    // Get base entity name
    String baseEntity1Name = TextConverter.getBaseClassName(entity1Name);
    
    // Get entity configuration names
    String entity1ConfigurationName = baseEntity1Name + "Configuration";
    String entity1ConfigurationClassName = entity1ConfigurationName + ".java";
    
    // Find entity compilation unit
    ICompilationUnit entity1CompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration.fragments", entity1ConfigurationClassName);
      
    // Verify compilation unit was found
    if (entity1CompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: " + entity1ConfigurationName + " class not found.");
    }
    
    // Get entity name
    String entity2Name = relationshipWizardData.getDataObject2Name();

    // Get base entity name
    String baseEntity2Name = TextConverter.getBaseClassName(entity2Name);
    
    // Get entity configuration names
    String entity2ConfigurationName = baseEntity2Name + "Configuration";
    String entity2ConfigurationClassName = entity2ConfigurationName + ".java";
    
    // Find entity compilation unit
    ICompilationUnit entity2CompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration.fragments", entity2ConfigurationClassName);
      
    // Verify compilation unit was found
    if (entity2CompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: " + entity2ConfigurationName + " class not found.");
    }

    // Set many to many association indicator
    boolean isManyToManyAssociation = relationshipWizardData.getAssociationType().equals(RelationshipWizardData.MANY_TO_MANY);
    
    // Update field configuration for entity 1
    updateFieldConfiguration(baseEntity1Name, baseEntity2Name, fieldIdsType, fieldIndex++, isManyToManyAssociation, entity1CompilationUnit, progressMonitor);
    
    if (relationshipWizardData.isBidirectional()) {

      // Update field configuration for entity 2 if bidirectional configuration
    	updateFieldConfiguration(baseEntity2Name, baseEntity1Name, fieldIdsType, fieldIndex++, isManyToManyAssociation, entity2CompilationUnit, progressMonitor);
    }
  }

  /**
   * Updates field configuration information for one side of a fragment relationship. 
   * @param primaryBaseEntityName Primary base entity name.
   * @param secondaryBaseEntityName Secondary base entity name.
   * @param fieldIdsType Fields IDs type.
   * @param fieldIndex Field index.
   * @param isManyToManyAssociation Many to many association indicator.
   * @param primaryEntityCompilationUnit Primary entity compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws ProcessException
   */
  private static void updateFieldConfiguration(String primaryBaseEntityName, String secondaryBaseEntityName, IType fieldIdsType, int  fieldIndex, 
  		boolean isManyToManyAssociation, ICompilationUnit primaryEntityCompilationUnit, IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Get common entity names
    String primaryCommonEntityName = TextConverter.convertCamelToCommon(primaryBaseEntityName);
    String secondaryCommonEntityName = TextConverter.convertCamelToCommon(secondaryBaseEntityName);

    // Get constant entity names
    String primaryConstantEntityName = TextConverter.convertCommonToConstant(primaryCommonEntityName);
    String secondaryConstantEntityName = TextConverter.convertCommonToConstant(secondaryCommonEntityName);

    // Initialize field label
    String fieldLabel = secondaryCommonEntityName;
    
    // Get plural version of field label
    if (isManyToManyAssociation) {
    	TextConverter.convertToPlural(secondaryCommonEntityName);
    }

    // Create configuration text
    StringBuffer configurationText = new StringBuffer();

    // Create field ID
    String fieldId = primaryConstantEntityName + "_" + secondaryConstantEntityName + "_ID";
    
    // Pluralize field ID
    if (isManyToManyAssociation) {
    	fieldId = fieldId + "S";
    }
    
    // Create field ID constant
    createConstant(fieldIdsType, fieldId, FIELD_PREFIX, fieldIndex++, progressMonitor);

    // Create field statements
    String fieldStatements = createIntegerFieldStatements(fieldId, fieldLabel);
    
    // Append field statements
    configurationText.append(fieldStatements);
    
    // Add spacing to support brace indentation
    configurationText.append("  ");
    
    // Get configuration type
    IType configurationType = primaryEntityCompilationUnit.findPrimaryType();
    
    // Get register fields method from configuration type
    IMethod registerFieldsMethod = configurationType.getMethod("registerFields", new String[] {"QSet<QField;>;"});

    // Get source range
    ISourceRange sourceRange = registerFieldsMethod.getSourceRange();
    
    // Get method source
    String methodSource = registerFieldsMethod.getSource();
    
    // Get position of last brace
    int bracePosition = methodSource.lastIndexOf("}");
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bracePosition;

    // Get working copy
    ICompilationUnit workingCopy = primaryEntityCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, configurationText.toString());

      // Apply text edit
      workingCopy.applyTextEdit(insertEdit, progressMonitor);
      workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, progressMonitor);
      workingCopy.commitWorkingCopy(true, progressMonitor);
    }
    finally {
      
      // Discard working copy
      workingCopy.discardWorkingCopy();
    }
  }

  /**
   * Updates action configuration information. 
   * @param javaProject Java project.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param attributeLookup Map of attributes keyed by attribute name.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updateActionConfiguration(IJavaProject javaProject, DefinitionWizardData definitionWizardData, 
      ViewWizardData viewWizardData, Map<String, Attribute> attributeLookup, ICompilationUnit configurationCompilationUnit, 
      IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Find action IDs compilation unit
    ICompilationUnit actionIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "ActionIds.java");
      
    // Verify compilation unit was found
    if (actionIdsCompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: ActionIds class not found.");
    }

    // Create configuration text
    StringBuffer configurationText = new StringBuffer();

    // Get action IDs type
    IType actionIdsType = actionIdsCompilationUnit.findPrimaryType();
    
    // Initialize action index
    int actionIndex = 1;
    
    // Get all fields
    IField[] fields = actionIdsType.getFields();
    
    if (fields != null) {

      // Set action index if fields exist
    	actionIndex = fields.length + 1;
    }
    
    // Get entity name
    String entityName = definitionWizardData.getEntityName();

    // Create constant entity name
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);

    // Create ID and version field IDs
    String idFieldId = constantEntityName + "_ID";
    String versionFieldId = constantEntityName + "_VERSION";
    
    // Create action IDs
    String viewActionId = "VIEW_" + TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String addActionId = "ADD_" + constantEntityName;
    String editActionId = "EDIT_" + constantEntityName;
    String deleteActionId = "DELETE_" + constantEntityName;
    String saveActionId = "SAVE_" + constantEntityName;
    String cancelActionId = "CANCEL_" + constantEntityName;
    String selectActionId = "SELECT_" + TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String selectAddActionId = "SELECT_" + constantEntityName + "_ADD";
    String selectRemoveActionId = "SELECT_" + constantEntityName + "_REMOVE";
    String selectCloseActionId = "SELECT_" + constantEntityName + "_CLOSE";

    // Create action ID constants
    createConstant(actionIdsType, viewActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, addActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, editActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, deleteActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, saveActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, cancelActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, selectActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, selectAddActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, selectRemoveActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    createConstant(actionIdsType, selectCloseActionId, ACTION_PREFIX, actionIndex++, progressMonitor);
    
    // Get parent ID field ID
    String parentIdFieldId = getParentIdFieldId(definitionWizardData);
    
    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    // Get filter attribute names
    String[] filterAttributeNames = viewWizardData.getFilterAttributeNames();
    
    // Get paging indicator
    boolean isPagingEnabled = viewWizardData.getIsPagingEnabled();
    
    // Create view action class name
    String viewCommonName = TextConverter.convertConstantToCommon(viewActionId);
    String viewActionClassName = TextConverter.convertCommonToCamel(viewCommonName, false);

    // Create select action class name
    String selectCommonName = TextConverter.convertConstantToCommon(selectActionId);
    String selectActionClassName = TextConverter.convertCommonToCamel(selectCommonName, false);

    // Create action statements
    String viewActionStatements = createViewActionStatements(viewActionId, viewActionClassName, "Apply", constantEntityName, filterAttributeNames, attributeLookup, parentIdFieldId, isPagingEnabled, false);
    String addActionStatements = createAddActionStatements(addActionId, editActionId);
    String editActionStatements = createEditActionStatements(editActionId, idFieldId, parentIdFieldId);
    String deleteActionStatements = createDeleteActionStatements(deleteActionId, idFieldId);
    String saveActionStatements = createSaveActionStatements(saveActionId, constantEntityName, idFieldId, versionFieldId, parentIdFieldId, attributes);
    String cancelActionStatements = createCancelActionStatements(cancelActionId, viewActionId);
    
    // Create selection based action statements
    String selectActionStatements = createViewActionStatements(selectActionId, selectActionClassName, "Apply", constantEntityName, filterAttributeNames, attributeLookup, null, isPagingEnabled, true);
    String selectAddActionStatements = createViewActionStatements(selectAddActionId, selectActionClassName, "Add", constantEntityName, filterAttributeNames, attributeLookup, null, isPagingEnabled, true);
    String selectRemoveActionStatements = createViewActionStatements(selectRemoveActionId, selectActionClassName, "Remove", constantEntityName, filterAttributeNames, attributeLookup, null, isPagingEnabled, true);
    String selectCloseActionStatements = createViewActionStatements(selectCloseActionId, selectActionClassName, "Close", constantEntityName, filterAttributeNames, attributeLookup, null, isPagingEnabled, true);
    
    // Append action statements
    configurationText.append(viewActionStatements);
    configurationText.append(addActionStatements);
    configurationText.append(editActionStatements);
    configurationText.append(deleteActionStatements);
    configurationText.append(saveActionStatements);
    configurationText.append(cancelActionStatements);

    // Append selection based action statements
    configurationText.append(selectActionStatements);
    configurationText.append(selectAddActionStatements);
    configurationText.append(selectRemoveActionStatements);
    configurationText.append(selectCloseActionStatements);

    if (isPagingEnabled) {

      // Create paging action names
      String viewPreviousPageActionName = "VIEW_" + constantEntityName + "_PREVIOUS_PAGE";
      String viewSelectPageActionName = "VIEW_" + constantEntityName + "_SELECT_PAGE";
      String viewNextPageActionName = "VIEW_" + constantEntityName + "_NEXT_PAGE";

      // Create selection based paging action names
      String selectPreviousPageActionName = "SELECT_" + constantEntityName + "_PREVIOUS_PAGE";
      String selectSelectPageActionName = "SELECT_" + constantEntityName + "_SELECT_PAGE";
      String selectNextPageActionName = "SELECT_" + constantEntityName + "_NEXT_PAGE";
      
      // Create paging action ID constants
      createConstant(actionIdsType, viewPreviousPageActionName, ACTION_PREFIX, actionIndex++, progressMonitor);
      createConstant(actionIdsType, viewSelectPageActionName, ACTION_PREFIX, actionIndex++, progressMonitor);
      createConstant(actionIdsType, viewNextPageActionName, ACTION_PREFIX, actionIndex++, progressMonitor);
      
      // Create selection based paging action ID constants
      createConstant(actionIdsType, selectPreviousPageActionName, ACTION_PREFIX, actionIndex++, progressMonitor);
      createConstant(actionIdsType, selectSelectPageActionName, ACTION_PREFIX, actionIndex++, progressMonitor);
      createConstant(actionIdsType, selectNextPageActionName, ACTION_PREFIX, actionIndex++, progressMonitor);

      // Create paging action statements
      String viewPreviousPageActionStatements = createPagingActionStatements(viewActionId, viewPreviousPageActionName, constantEntityName, filterAttributeNames, attributeLookup, parentIdFieldId, "Previous Page", false);
      String viewSelectPageActionStatements = createPagingActionStatements(viewActionId, viewSelectPageActionName, constantEntityName, filterAttributeNames, attributeLookup, parentIdFieldId, "Select Page", false);
      String viewNextPageActionStatements = createPagingActionStatements(viewActionId, viewNextPageActionName, constantEntityName, filterAttributeNames, attributeLookup, parentIdFieldId, "Next Page", false);

      // Create selection based paging action statements
      String selectPreviousPageActionStatements = createPagingActionStatements(selectActionId, selectPreviousPageActionName, constantEntityName, filterAttributeNames, attributeLookup, null, "Previous Page", true);
      String selectSelectPageActionStatements = createPagingActionStatements(selectActionId, selectSelectPageActionName, constantEntityName, filterAttributeNames, attributeLookup, null, "Select Page", true);
      String selectNextPageActionStatements = createPagingActionStatements(selectActionId, selectNextPageActionName, constantEntityName, filterAttributeNames, attributeLookup, null, "Next Page", true);

      // Append paging action statements
      configurationText.append(viewPreviousPageActionStatements);
      configurationText.append(viewSelectPageActionStatements);
      configurationText.append(viewNextPageActionStatements);

      // Append selection based paging action statements
      configurationText.append(selectPreviousPageActionStatements);
      configurationText.append(selectSelectPageActionStatements);
      configurationText.append(selectNextPageActionStatements);
    }

    // Add spacing to support brace indentation
    configurationText.append("  ");
    
    // Get configuration type
    IType configurationType = configurationCompilationUnit.findPrimaryType();
    
    // Get register actions method from configuration type
    IMethod registerActionsMethod = configurationType.getMethod("registerActions", new String[] {"QSet<QAction;>;"});
    
    // Get source range
    ISourceRange sourceRange = registerActionsMethod.getSourceRange();
    
    // Get method source
    String methodSource = registerActionsMethod.getSource();
    
    // Get position of last brace
    int bracePosition = methodSource.lastIndexOf("}");
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bracePosition;

    // Get working copy
    ICompilationUnit workingCopy = configurationCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, configurationText.toString());

      // Apply text edit
      workingCopy.applyTextEdit(insertEdit, progressMonitor);
      workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, progressMonitor);
      workingCopy.commitWorkingCopy(true, progressMonitor);
    }
    finally {
      
      // Discard working copy
      workingCopy.discardWorkingCopy();
    }
  }

  /**
   * Updates action configuration information for a fragment relationship. 
   * @param javaProject Java project.
   * @param relationshipWizardData Relationship wizard data.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updateActionConfiguration(IJavaProject javaProject, RelationshipWizardData relationshipWizardData, IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Get entity name
    String entity1Name = relationshipWizardData.getDataObject1Name();

    // Get base entity name
    String baseEntity1Name = TextConverter.getBaseClassName(entity1Name);
    
    // Get entity configuration names
    String entity1ConfigurationName = baseEntity1Name + "Configuration";
    String entity1ConfigurationClassName = entity1ConfigurationName + ".java";
    
    // Find entity compilation unit
    ICompilationUnit entity1CompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration.fragments", entity1ConfigurationClassName);
      
    // Verify compilation unit was found
    if (entity1CompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: " + entity1ConfigurationName + " class not found.");
    }
    
    // Get entity name
    String entity2Name = relationshipWizardData.getDataObject2Name();

    // Get base entity name
    String baseEntity2Name = TextConverter.getBaseClassName(entity2Name);
    
    // Get entity configuration names
    String entity2ConfigurationName = baseEntity2Name + "Configuration";
    String entity2ConfigurationClassName = entity2ConfigurationName + ".java";
    
    // Find entity compilation unit
    ICompilationUnit entity2CompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration.fragments", entity2ConfigurationClassName);
      
    // Verify compilation unit was found
    if (entity2CompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: " + entity2ConfigurationName + " class not found.");
    }

    // Set many to many association indicator
    boolean isManyToManyAssociation = relationshipWizardData.getAssociationType().equals(RelationshipWizardData.MANY_TO_MANY);
    
    // Set required field indicator
    boolean isRequired = relationshipWizardData.isAttribute2Required();
    
    // Update action configuration for entity 1
    updateActionConfiguration(baseEntity1Name, baseEntity2Name, isRequired, isManyToManyAssociation, entity1CompilationUnit, progressMonitor);
    
    if (relationshipWizardData.isBidirectional()) {
    	
      // Update action configuration for entity 1
      updateActionConfiguration(baseEntity2Name, baseEntity1Name, false, isManyToManyAssociation, entity2CompilationUnit, progressMonitor);
    }
  }

  /**
   * Updates action configuration information for one side of a fragment relationship. 
   * @param primaryBaseEntityName Primary base entity name.
   * @param secondaryBaseEntityName Secondary base entity name.
   * @param isRequired Required field indicator.
   * @param isManyToManyAssociation Many to many association indicator.
   * @param primaryEntityCompilationUnit Primary entity compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws ProcessException
   */
  private static void updateActionConfiguration(String primaryBaseEntityName, String secondaryBaseEntityName, boolean isRequired, 
  		boolean isManyToManyAssociation, ICompilationUnit primaryEntityCompilationUnit, IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Get common entity names
    String primaryCommonEntityName = TextConverter.convertCamelToCommon(primaryBaseEntityName);
    String secondaryCommonEntityName = TextConverter.convertCamelToCommon(secondaryBaseEntityName);

    // Get constant entity names
    String primaryConstantEntityName = TextConverter.convertCommonToConstant(primaryCommonEntityName);
    String secondaryConstantEntityName = TextConverter.convertCommonToConstant(secondaryCommonEntityName);

    // Create template variables
    String saveActionId = "SAVE_" + primaryConstantEntityName;
    String commonName = TextConverter.convertConstantToCommon(saveActionId);
    String actionName = TextConverter.convertCommonToCamel(commonName, true);
    
    // Create field ID
    String fieldId = primaryConstantEntityName + "_" + secondaryConstantEntityName + "_ID";
    
    // Pluralize field ID 
    if (isManyToManyAssociation) {
    	fieldId = fieldId + "S";
    }
    
    // Set required field value
    String isRequiredField = isRequired ? "true" : "false";
    
    // Create sort field to action statement
    String sortFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
    sortFieldToAction = sortFieldToAction.replaceAll("FIELD-ID", fieldId);
    sortFieldToAction = sortFieldToAction.replaceAll("IS-REQUIRED", isRequiredField);
    
    // Create action to actions statement
    String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);
    
    // Create configuration text
    StringBuffer configurationText = new StringBuffer();

    // Append field to action statement
    configurationText.append(sortFieldToAction);
    configurationText.append(LINE_SEPARATOR);
    configurationText.append("  ");
    
    // Get configuration type
    IType configurationType = primaryEntityCompilationUnit.findPrimaryType();
    
    // Get register fields method from configuration type
    IMethod registerFieldsMethod = configurationType.getMethod("registerActions", new String[] {"QSet<QAction;>;"});

    // Get source range
    ISourceRange sourceRange = registerFieldsMethod.getSourceRange();
    
    // Get method source
    String methodSource = registerFieldsMethod.getSource();
    
    // Get position of target statement
    int bracePosition = methodSource.lastIndexOf(actionToActions);
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bracePosition;

    // Get working copy
    ICompilationUnit workingCopy = primaryEntityCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, configurationText.toString());

      // Apply text edit
      workingCopy.applyTextEdit(insertEdit, progressMonitor);
      workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, progressMonitor);
      workingCopy.commitWorkingCopy(true, progressMonitor);
    }
    finally {
      
      // Discard working copy
      workingCopy.discardWorkingCopy();
    }
  }

  /**
   * Updates page configuration information. 
   * @param javaProject Java project.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param attributeLookup Map of attributes keyed by attribute name.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updatePageConfiguration(IJavaProject javaProject, DefinitionWizardData definitionWizardData, 
      ViewWizardData viewWizardData, Map<String, Attribute> attributeLookup, ICompilationUnit configurationCompilationUnit, 
      IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Find page IDs compilation unit
    ICompilationUnit pageIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "PageIds.java");
      
    // Verify compilation unit was found
    if (pageIdsCompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: PageIds class not found.");
    }

    // Get page IDs type
    IType pageIdsType = pageIdsCompilationUnit.findPrimaryType();
    
    // Initialize page index
    int pageIndex = 1;
    
    // Get all fields
    IField[] fields = pageIdsType.getFields();
    
    if (fields != null) {

      // Set page index if fields exist
    	pageIndex = fields.length + 1;
    }
    
    // Get entity name
    String entityName = definitionWizardData.getEntityName();

    // Create constant entity name
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);

    // Create page names
    String viewPageName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String editPageName = constantEntityName;
    String selectPageName = constantEntityName + "_SELECTION";
  
    // Create page ID constants
    createConstant(pageIdsType, viewPageName, PAGE_PREFIX, pageIndex++, progressMonitor);
    createConstant(pageIdsType, editPageName, PAGE_PREFIX, pageIndex++, progressMonitor);
    createConstant(pageIdsType, selectPageName, PAGE_PREFIX, pageIndex++, progressMonitor);
    
    // Create directory name
    String directoryName = TextConverter.convertCommonToCamel(entityName, true);

    // Create page statements
    String viewPageStatements = createPageStatements(directoryName, viewPageName);
    String editPageStatements = createPageStatements(directoryName, editPageName);
    String selectPageStatements = createPageStatements(directoryName, selectPageName);
    
    // Create configuration text
    StringBuffer configurationText = new StringBuffer();

    // Append page statements
    configurationText.append(viewPageStatements);
    configurationText.append(editPageStatements);
    configurationText.append(selectPageStatements);
    
    // Add spacing to support brace indentation
    configurationText.append("  ");
    
    // Get configuration type
    IType configurationType = configurationCompilationUnit.findPrimaryType();
    
    // Get register pages method from configuration type
    IMethod registerActionsMethod = configurationType.getMethod("registerPages", new String[] {"QSet<QPage;>;"});
    
    // Get source range
    ISourceRange sourceRange = registerActionsMethod.getSourceRange();
    
    // Get method source
    String methodSource = registerActionsMethod.getSource();
    
    // Get position of last brace
    int bracePosition = methodSource.lastIndexOf("}");
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bracePosition;

    // Get working copy
    ICompilationUnit workingCopy = configurationCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, configurationText.toString());

      // Apply text edit
      workingCopy.applyTextEdit(insertEdit, progressMonitor);
      workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, progressMonitor);
      workingCopy.commitWorkingCopy(true, progressMonitor);
    }
    finally {
      
      // Discard working copy
      workingCopy.discardWorkingCopy();
    }
  }

  /**
   * Updates attribute configuration information. 
   * @param javaProject Java project.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param attributeLookup Map of attributes keyed by attribute name.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updateAttributeConfiguration(IJavaProject javaProject, DefinitionWizardData definitionWizardData, 
      ViewWizardData viewWizardData, Map<String, Attribute> attributeLookup, ICompilationUnit configurationCompilationUnit, 
      IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Find attribute names compilation unit
    ICompilationUnit attributeNamesCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "AttributeNames.java");
      
    // Verify compilation unit was found
    if (attributeNamesCompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: AttributeNames class not found.");
    }

    // Get attribute names type
    IType attributeNamesType = attributeNamesCompilationUnit.findPrimaryType();
    
    // Get entity name
    String entityName = definitionWizardData.getEntityName();

    // Create constant entity name
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);

    // Create attribute names
    String attributeName = constantEntityName;
    String attributesName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String selectedAttributesName = "SELECTED_" + attributesName;
  
    // Create attribute name constants
    createConstant(attributeNamesType, attributeName, progressMonitor);
    createConstant(attributeNamesType, attributesName, progressMonitor);
    createConstant(attributeNamesType, selectedAttributesName, progressMonitor);
  }

  /**
   * Updates object binding configuration information. 
   * @param javaProject Java project.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param attributeLookup Map of attributes keyed by attribute name.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updateObjectBindingConfiguration(IJavaProject javaProject, DefinitionWizardData definitionWizardData, 
      ViewWizardData viewWizardData, Map<String, Attribute> attributeLookup, ICompilationUnit configurationCompilationUnit, 
      IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Get entity name
    String entityName = definitionWizardData.getEntityName();

    // Get parent ID field ID
    String parentIdFieldId = getParentIdFieldId(definitionWizardData);
    
    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();

    // Create object binding statements
    String objectBindingStatements = createObjectBindingStatements(entityName, parentIdFieldId, attributes);
    
    // Create configuration text
    StringBuffer configurationText = new StringBuffer();

    // Append object binding statements
    configurationText.append(objectBindingStatements);
    
    // Add spacing to support brace indentation
    configurationText.append("  ");
    
    // Get configuration type
    IType configurationType = configurationCompilationUnit.findPrimaryType();
    
    // Get register object bindings method from configuration type
    IMethod registerActionsMethod = configurationType.getMethod("registerObjectBindings", new String[] {"QSet<QObjectBinding;>;"});
    
    // Get source range
    ISourceRange sourceRange = registerActionsMethod.getSourceRange();
    
    // Get method source
    String methodSource = registerActionsMethod.getSource();
    
    // Get position of last brace
    int bracePosition = methodSource.lastIndexOf("}");
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bracePosition;

    // Get working copy
    ICompilationUnit workingCopy = configurationCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, configurationText.toString());

      // Apply text edit
      workingCopy.applyTextEdit(insertEdit, progressMonitor);
      workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, progressMonitor);
      workingCopy.commitWorkingCopy(true, progressMonitor);
    }
    finally {
      
      // Discard working copy
      workingCopy.discardWorkingCopy();
    }
  }

  /**
   * Updates object binding configuration information for a fragment relationship. 
   * @param javaProject Java project.
   * @param relationshipWizardData Relationship wizard data.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updateObjectBindingConfiguration(IJavaProject javaProject, RelationshipWizardData relationshipWizardData, IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Get entity name
    String entity1Name = relationshipWizardData.getDataObject1Name();

    // Get base entity name
    String baseEntity1Name = TextConverter.getBaseClassName(entity1Name);
    
    // Get entity configuration names
    String entity1ConfigurationName = baseEntity1Name + "Configuration";
    String entity1ConfigurationClassName = entity1ConfigurationName + ".java";
    
    // Get entity name
    String entity2Name = relationshipWizardData.getDataObject2Name();

    // Get base entity name
    String baseEntity2Name = TextConverter.getBaseClassName(entity2Name);
    
    // Get entity configuration names
    String entity2ConfigurationName = baseEntity2Name + "Configuration";
    String entity2ConfigurationClassName = entity2ConfigurationName + ".java";
    
    // Find entity compilation unit
    ICompilationUnit entity1CompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration.fragments", entity1ConfigurationClassName);
      
    // Verify compilation unit was found
    if (entity1CompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: " + entity1ConfigurationName + " class not found.");
    }
    
    // Find entity compilation unit
    ICompilationUnit entity2CompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration.fragments", entity2ConfigurationClassName);
      
    // Verify compilation unit was found
    if (entity2CompilationUnit == null) {
      throw new ProcessException("Error updating configuration information: " + entity2ConfigurationName + " class not found.");
    }
    
    // Set many to many association indicator
    boolean isManyToManyAssociation = relationshipWizardData.getAssociationType().equals(RelationshipWizardData.MANY_TO_MANY);
    
    // Update object binding for entity 1
    updateObjectBindingConfiguration(javaProject, baseEntity1Name, baseEntity2Name, isManyToManyAssociation, entity1CompilationUnit, progressMonitor);
    
    if (relationshipWizardData.isBidirectional()) {
    	
      // Update object binding for entity 2
      updateObjectBindingConfiguration(javaProject, baseEntity2Name, baseEntity1Name, isManyToManyAssociation, entity2CompilationUnit, progressMonitor);
    }
  }

  /**
   * Updates object binding configuration information for a fragment relationship. 
   * @param javaProject Java project.
   * @param primaryEntityName Primary entity name.
   * @param secondaryEntityName Secondary entity name.
   * @param isManyToManyAssociation Many to many association indicator.
   * @param configurationCompilationUnit Configuration compilation unit.
   * @param progressMonitor Progress monitor.
   * @throws CoreException 
   * @throws ProcessException
   */
  private static void updateObjectBindingConfiguration(IJavaProject javaProject, String primaryEntityName, String secondaryEntityName, 
      boolean isManyToManyAssociation, ICompilationUnit configurationCompilationUnit, IProgressMonitor progressMonitor)
      throws CoreException, ProcessException {
    
    // Create configuration text
    StringBuffer configurationText = new StringBuffer();

    // Get common entity names
    String primaryCommonEntityName = TextConverter.convertCamelToCommon(primaryEntityName);
    String secondaryCommonEntityName = TextConverter.convertCamelToCommon(secondaryEntityName);

    // Get primary and secondary constant entity names
    String primaryConstantEntityName = TextConverter.convertCommonToConstant(primaryCommonEntityName);
    String secondaryConstantEntityName = TextConverter.convertCommonToConstant(secondaryCommonEntityName);

    // Create template variables
    String bindingName = TextConverter.convertCommonToCamel(primaryCommonEntityName, true);
    String fieldId = primaryConstantEntityName + "_" + secondaryConstantEntityName + "_ID";
    String propertyName = TextConverter.convertCommonToCamel(secondaryCommonEntityName, false) + "Id";
    
    // Pluralize field ID and property name
    if (isManyToManyAssociation) {
    	fieldId = fieldId + "S";
    	propertyName = propertyName + "s";
    }
    
    // Create field to binding statement
    String fieldToBinding = addCollectionFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
    fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", fieldId);
    fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", propertyName);
    fieldToBinding = fieldToBinding.replaceAll("IS-COLLECTION", String.valueOf(isManyToManyAssociation));

    // Append field to binding statement
    configurationText.append(fieldToBinding).append(LINE_SEPARATOR);

    // Add spacing to support brace indentation
    configurationText.append("  ");
    
    // Get configuration type
    IType configurationType = configurationCompilationUnit.findPrimaryType();
    
    // Get register object bindings method from configuration type
    IMethod registerActionsMethod = configurationType.getMethod("registerObjectBindings", new String[] {"QSet<QObjectBinding;>;"});
    
    // Get source range
    ISourceRange sourceRange = registerActionsMethod.getSourceRange();
    
    // Get method source
    String methodSource = registerActionsMethod.getSource();
    
    // Create binding to bindings statement
    String bindingToBindings = addBindingToBindingsTemplate.replaceAll("BINDING-NAME", bindingName);

    // Get position of target statement
    int bracePosition = methodSource.lastIndexOf(bindingToBindings);
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bracePosition;

    // Get working copy
    ICompilationUnit workingCopy = configurationCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, configurationText.toString());

      // Apply text edit
      workingCopy.applyTextEdit(insertEdit, progressMonitor);
      workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, progressMonitor);
      workingCopy.commitWorkingCopy(true, progressMonitor);
    }
    finally {
      
      // Discard working copy
      workingCopy.discardWorkingCopy();
    }
  }

  /**
   * Creates a string constant using a class type, constant name, value prefix and value index.
   * @param type Class type.
   * @param constantName Constant name.
   * @param valuePrefix Constant value prefix.
   * @param valueIndex Constant value index.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private static void createConstant(IType type, String constantName, String valuePrefix, int valueIndex, IProgressMonitor progressMonitor) 
      throws CoreException {
    
  	// Create constant value
  	String constantValue = valuePrefix + String.valueOf(valueIndex);
  	
    // Create field contents
    String contents = constantTemplate.replaceAll("CONSTANT-NAME", constantName);
    contents = contents.replaceAll("CONSTANT-VALUE", constantValue);
    
    // Create constant field
    type.createField(contents, null, true, progressMonitor);
  }

  /**
   * Creates a string constant using a class type and constant name.
   * @param type Class type.
   * @param constantName Constant name.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private static void createConstant(IType type, String constantName, IProgressMonitor progressMonitor) 
      throws CoreException {
    
    // Create field contents
    String contents = constantTemplate.replaceAll("CONSTANT-NAME", constantName);
    contents = contents.replaceAll("CONSTANT-VALUE", constantName);
    
    // Create constant field
    type.createField(contents, null, true, progressMonitor);
  }

  /**
   * Creates and returns field statements for an ID field of type integer.
   * @param fieldId Field ID.
   * @return String Field statements.
   */
  private static String createIdFieldStatements(String fieldId) {
    
    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);
    
    // Create field name
    String commonName = TextConverter.convertConstantToCommon(fieldId);
    String fieldName = TextConverter.convertCommonToCamel(commonName, true);
    
    // Create field statement
    String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
    field = field.replaceAll("FIELD-ID", fieldId);
    field = field.replaceAll("FIELD-TYPE", "INTEGER");
    field = field.replaceAll("FIELD-LABEL", "ID");
    field = field.replaceAll("FIELD-CONVERTER", "NumberConverter.getInstance()");
    
    // Append field statement
    statements.append(field).append(LINE_SEPARATOR);
    
    // Create add field statement
    String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

    // Append add field statement
    statements.append(addField).append(LINE_SEPARATOR);
    
    return statements.toString();
  }
  
  /**
   * Creates and returns field statements for a version field of type integer.
   * @param fieldId Field ID.
   * @return String Field statements.
   */
  private static String createVersionFieldStatements(String fieldId) {
    
    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);
    
    // Create field name
    String commonName = TextConverter.convertConstantToCommon(fieldId);
    String fieldName = TextConverter.convertCommonToCamel(commonName, true);
    
    // Create field statement
    String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
    field = field.replaceAll("FIELD-ID", fieldId);
    field = field.replaceAll("FIELD-TYPE", "LONG");
    field = field.replaceAll("FIELD-LABEL", "Version");
    field = field.replaceAll("FIELD-CONVERTER", "NumberConverter.getInstance()");
    
    // Append field statement
    statements.append(field).append(LINE_SEPARATOR);
    
    // Create add field statement
    String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

    // Append add field statement
    statements.append(addField).append(LINE_SEPARATOR);
    
    return statements.toString();
  }
  
  /**
   * Creates and returns field statements for a given field ID and attribute.
   * @param fieldId Field ID.
   * @param fieldLabel Field label.
   * @param attribute Attribute.
   * @param isFilterField Filter field indicator.
   * @return String Field statements.
   */
  private static String createFieldStatements(String fieldId, String fieldLabel, Attribute attribute, boolean isFilterField) {
    
    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);
    
    // Create field name
    String commonName = TextConverter.convertConstantToCommon(fieldId);
    String fieldName = TextConverter.convertCommonToCamel(commonName, true);
    
    // Set field type and field converter using attribute data
    String fieldType = getFieldType(attribute);
    String fieldConverter = getFieldConverter(attribute);
    
    // Create field statement
    String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
    field = field.replaceAll("FIELD-ID", fieldId);
    field = field.replaceAll("FIELD-TYPE", fieldType);
    field = field.replaceAll("FIELD-LABEL", fieldLabel);
    field = field.replaceAll("FIELD-CONVERTER", fieldConverter);
    
    // Append field statement
    statements.append(field).append(LINE_SEPARATOR);
    
    if (attribute.getType().equals(AttributeTypes.NUMERIC)) {

      // Cast to numeric attribute
      NumericAttribute numericAttribute = (NumericAttribute)attribute;
      
      // Get value type, precision and scale
      String valueType = numericAttribute.getValueType();
      String precision = String.valueOf(numericAttribute.getPrecision());
      String scale = String.valueOf(numericAttribute.getScale());
      
      // Create number rule
      String numberRule = addNumberRuleTemplate.replaceAll("FIELD-NAME", fieldName);
      numberRule = numberRule.replaceAll("VALUE-TYPE", valueType.toUpperCase());
      numberRule = numberRule.replaceAll("PRECISION", precision);
      numberRule = numberRule.replaceAll("SCALE", scale);

      // Append field statement
      statements.append(numberRule).append(LINE_SEPARATOR);
    }
    else if (attribute.getType().equals(AttributeTypes.DATE) || attribute.getType().equals(AttributeTypes.TIME)) {

      // Create converter rule
      String converterRule = addConverterRuleTemplate.replaceAll("FIELD-NAME", fieldName);

      // Append field statement
      statements.append(converterRule).append(LINE_SEPARATOR);
    }
    else if (attribute.getType().equals(AttributeTypes.MEMO)) {

      // Cast to memo attribute
      MemoAttribute memoAttribute = (MemoAttribute)attribute;
      
      // Get max length
      Integer maxLength = memoAttribute.getMaxLength();

      // Create max length rule
      String maxLengthRule = addMaxLengthRuleTemplate.replaceAll("FIELD-NAME", fieldName);
      maxLengthRule = maxLengthRule.replaceAll("MAX-LENGTH", maxLength.toString());

      // Append field statement
      statements.append(maxLengthRule).append(LINE_SEPARATOR);
    }
    else if (!isFilterField && attribute.getType().equals(AttributeTypes.EMAIL)) {

      // Create email rule
      String emailRule = addEmailRuleTemplate.replaceAll("FIELD-NAME", fieldName);

      // Append field statement
      statements.append(emailRule).append(LINE_SEPARATOR);
    }
    else if (!isFilterField && attribute.getType().equals(AttributeTypes.PHONE_NUMBER)) {

      // Create phone number rule
      String phoneNumberRule = addPhoneNumberRuleTemplate.replaceAll("FIELD-NAME", fieldName);

      // Append field statement
      statements.append(phoneNumberRule).append(LINE_SEPARATOR);
    }
    else if (!isFilterField && attribute.getType().equals(AttributeTypes.POSTAL_CODE)) {

      // Create postal code rule
      String postalCodeRule = addPostalCodeRuleTemplate.replaceAll("FIELD-NAME", fieldName);

      // Append field statement
      statements.append(postalCodeRule).append(LINE_SEPARATOR);
    }
    
    // Create add field statement
    String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

    // Append add field statement
    statements.append(addField).append(LINE_SEPARATOR);
    
    return statements.toString();
  }
  
  /**
   * Creates and returns string field statements for a given field ID.
   * @param fieldId Field ID.
   * @param fieldLabel Field label.
   * @return String Field statements.
   */
  private static String createStringFieldStatements(String fieldId, String fieldLabel) {
    
    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);
    
    // Create field name
    String commonName = TextConverter.convertConstantToCommon(fieldId);
    String fieldName = TextConverter.convertCommonToCamel(commonName, true);
    
    // Create field statement
    String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
    field = field.replaceAll("FIELD-ID", fieldId);
    field = field.replaceAll("FIELD-TYPE", "STRING");
    field = field.replaceAll("FIELD-LABEL", fieldLabel);
    field = field.replaceAll("FIELD-CONVERTER", "null");
    
    // Append field statement
    statements.append(field).append(LINE_SEPARATOR);
    
    // Create add field statement
    String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

    // Append add field statement
    statements.append(addField).append(LINE_SEPARATOR);
    
    return statements.toString();
  }
  
  /**
   * Creates and returns integer field statements for a given field ID.
   * @param fieldId Field ID.
   * @param fieldLabel Field label.
   * @return String Field statements.
   */
  private static String createIntegerFieldStatements(String fieldId, String fieldLabel) {
    
    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);
    
    // Create field name
    String commonName = TextConverter.convertConstantToCommon(fieldId);
    String fieldName = TextConverter.convertCommonToCamel(commonName, true);
    
    // Create field statement
    String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
    field = field.replaceAll("FIELD-ID", fieldId);
    field = field.replaceAll("FIELD-TYPE", "INTEGER");
    field = field.replaceAll("FIELD-LABEL", fieldLabel);
    field = field.replaceAll("FIELD-CONVERTER", "NumberConverter.getInstance()");
    
    // Append field statement
    statements.append(field).append(LINE_SEPARATOR);
    
    // Create add field statement
    String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

    // Append add field statement
    statements.append(addField).append(LINE_SEPARATOR);
    
    return statements.toString();
  }
  
  /**
   * Creates and returns operator field statements for a given field ID.
   * @param fieldId Field ID.
   * @return String Field statements.
   */
  private static String createOperatorFieldStatements(String fieldId) {
    
    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);
    
    // Create field name
    String commonName = TextConverter.convertConstantToCommon(fieldId);
    String fieldName = TextConverter.convertCommonToCamel(commonName, true);
    
    // Create field statement
    String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
    field = field.replaceAll("FIELD-ID", fieldId);
    field = field.replaceAll("FIELD-TYPE", "STRING");
    field = field.replaceAll("FIELD-LABEL", "Operator");
    field = field.replaceAll("FIELD-CONVERTER", "null");
    
    // Append field statement
    statements.append(field).append(LINE_SEPARATOR);
    
    // Create add field statement
    String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

    // Append add field statement
    statements.append(addField).append(LINE_SEPARATOR);
    
    return statements.toString();
  }
  
  /**
   * Creates and returns statements supporting the view action.
   * @param viewActionId View action ID.
   * @param viewActionClassName View action class name.
   * @param actionLabel Action label.
   * @param constantEntityName Entity name in constant format.
   * @param filterAttributeNames Array of filter attribute names.
   * @param attributeLookup Map of attributes keyed by attribute name.
   * @param parentIdFieldId Parent entity ID field ID.
   * @param isPagingEnabled Paging indicator.
   * @param isSelectEntityAction Select entity action indicator.
   * @return String Action statements.
   */
  private static String createViewActionStatements(String viewActionId, String viewActionClassName, String actionLabel, String constantEntityName, String[] filterAttributeNames, 
  		Map<String, Attribute> attributeLookup, String parentIdFieldId, boolean isPagingEnabled, boolean isSelectEntityAction) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables
    String commonName = TextConverter.convertConstantToCommon(viewActionId);
    String actionName = TextConverter.convertCommonToCamel(commonName, true);
    
    
    // Create action statement
    String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
    action = action.replaceAll("ACTION-ID", viewActionId);
    action = action.replaceAll("ACTION-LABEL", actionLabel);
    
    // Append action statement
    statements.append(action).append(LINE_SEPARATOR);
    
    
    // Create processor factory statement
    String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
    processorFactory = processorFactory.replaceAll("ACTION-CLASS", viewActionClassName);
    
    // Append processor factory statement
    statements.append(processorFactory).append(LINE_SEPARATOR);
    
    
    // Create and append action field statements
    String actionFieldStatements = createActionFieldStatements(actionName, constantEntityName, filterAttributeNames, attributeLookup, parentIdFieldId, isPagingEnabled, isSelectEntityAction);
    statements.append(actionFieldStatements);
    
    
    // Create action to actions statement
    String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);
    
    // Append action to actions statement
    statements.append(actionToActions).append(LINE_SEPARATOR);
    
    return statements.toString();
  }

  /**
   * Creates and returns action field statements.
   * @param actionName Action name.
   * @param constantEntityName Entity name in constant format.
   * @param filterAttributeNames Array of filter attribute names.
   * @param attributeLookup Map of attributes keyed by attribute name.
   * @param parentIdFieldId Parent entity ID field ID.
   * @param isPagingEnabled Paging indicator.
   * @param isSelectEntityAction Select entity action indicator.
   * @return String Action field statements.
   */
  private static String createActionFieldStatements(String actionName, String constantEntityName, String[] filterAttributeNames, 
  		Map<String, Attribute> attributeLookup, String parentIdFieldId, boolean isPagingEnabled, boolean isSelectEntityAction) {
  	
    // Initialize statements
    StringBuffer statements = new StringBuffer();

    if (parentIdFieldId != null) {
    	
      // Create parent ID field to action statement
      String parentIdFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      parentIdFieldToAction = parentIdFieldToAction.replaceAll("FIELD-ID", parentIdFieldId);
      parentIdFieldToAction = parentIdFieldToAction.replaceAll("IS-REQUIRED", "true");
      
      // Append parent ID field to action statement
      statements.append(parentIdFieldToAction).append(LINE_SEPARATOR);
    }
    
    if (isSelectEntityAction) {
    	
      // Create parent field ID to action statement
      String parentFieldIdToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      parentFieldIdToAction = parentFieldIdToAction.replaceAll("FIELD-ID", "PARENT_FIELD_ID");
      parentFieldIdToAction = parentFieldIdToAction.replaceAll("IS-REQUIRED", "true");
      
      // Append parent field ID to action statement
      statements.append(parentFieldIdToAction).append(LINE_SEPARATOR);

      // Create parent action ID to action statement
      String parentDataObjectClassToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      parentDataObjectClassToAction = parentDataObjectClassToAction.replaceAll("FIELD-ID", "PARENT_ACTION_ID");
      parentDataObjectClassToAction = parentDataObjectClassToAction.replaceAll("IS-REQUIRED", "true");
      
      // Append parent action ID to action statement
      statements.append(parentDataObjectClassToAction).append(LINE_SEPARATOR);

      // Create parent page ID to action statement
      String parentPageIdToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      parentPageIdToAction = parentPageIdToAction.replaceAll("FIELD-ID", "PARENT_PAGE_ID");
      parentPageIdToAction = parentPageIdToAction.replaceAll("IS-REQUIRED", "true");
      
      // Append parent page ID to action statement
      statements.append(parentPageIdToAction).append(LINE_SEPARATOR);
    }
    
    if (filterAttributeNames != null && filterAttributeNames.length > 0) {
      
      for (String filterAttributeName : filterAttributeNames) {
        
        // Create filter field ID
        String filterFieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttributeName) + "_FILTER";

        // Create filter field to action statement
        String filterFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
        filterFieldToAction = filterFieldToAction.replaceAll("FIELD-ID", filterFieldId);
        filterFieldToAction = filterFieldToAction.replaceAll("IS-REQUIRED", "false");
        
        // Append filter field to action statement
        statements.append(filterFieldToAction).append(LINE_SEPARATOR);


        // Lookup filter attribute
        Attribute filterAttribute = attributeLookup.get(filterAttributeName);
        
        // Get filter attribute type
        String filterAttributeType = filterAttribute.getType();
        
        if (filterAttributeType.equalsIgnoreCase(AttributeTypes.DATE) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.EMAIL) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.MEMO) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.NUMERIC) ||
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.POSTAL_CODE) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.TEXT) || 
        		filterAttributeType.equalsIgnoreCase(AttributeTypes.TIME)) {
        	
          // Create operator field ID
          String operatorFieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttributeName) + "_FILTER_OPERATOR";

          // Create operator field to action statement
          String operatorFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
          operatorFieldToAction = operatorFieldToAction.replaceAll("FIELD-ID", operatorFieldId);
          operatorFieldToAction = operatorFieldToAction.replaceAll("IS-REQUIRED", "false");
          
          // Append operator field to action statement
          statements.append(operatorFieldToAction).append(LINE_SEPARATOR);
        }
      }
      
      // Create group state field name
      String groupStateFieldName = constantEntityName + "_GROUP_STATE";
      
      // Create group state field to action statement
      String filterToggleToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      filterToggleToAction = filterToggleToAction.replaceAll("FIELD-ID", groupStateFieldName);
      filterToggleToAction = filterToggleToAction.replaceAll("IS-REQUIRED", "false");
      
      // Append group state field to action statement
      statements.append(filterToggleToAction).append(LINE_SEPARATOR);
    }
    
    if (isPagingEnabled) {
    	
      // Create current page number field name
      String currentPageNumberFieldName = constantEntityName + "_CURRENT_PAGE_NUMBER";
      
      // Create current page number to action statement
      String currentPageNumberToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      currentPageNumberToAction = currentPageNumberToAction.replaceAll("FIELD-ID", currentPageNumberFieldName);
      currentPageNumberToAction = currentPageNumberToAction.replaceAll("IS-REQUIRED", "false");
      
      // Append current page number to action statement
      statements.append(currentPageNumberToAction).append(LINE_SEPARATOR);

      // Create selected page number field name
      String selectedPageNumberFieldName = constantEntityName + "_SELECTED_PAGE_NUMBER";
      
      // Create selected page number to action statement
      String selectedPageNumberToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      selectedPageNumberToAction = selectedPageNumberToAction.replaceAll("FIELD-ID", selectedPageNumberFieldName);
      selectedPageNumberToAction = selectedPageNumberToAction.replaceAll("IS-REQUIRED", "false");
      
      // Append selected page number to action statement
      statements.append(selectedPageNumberToAction).append(LINE_SEPARATOR);
    }
    
    // Create sort field field name
    String sortFieldFieldName = constantEntityName + "_SORT_FIELD";
    
    // Create sort field to action statement
    String sortFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
    sortFieldToAction = sortFieldToAction.replaceAll("FIELD-ID", sortFieldFieldName);
    sortFieldToAction = sortFieldToAction.replaceAll("IS-REQUIRED", "false");
    
    // Append sort field to action statement
    statements.append(sortFieldToAction).append(LINE_SEPARATOR);

    // Create sort direction field name
    String sortDirectionFieldName = constantEntityName + "_SORT_DIRECTION";
    
    // Create sort direction to action statement
    String sortDirectionToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
    sortDirectionToAction = sortDirectionToAction.replaceAll("FIELD-ID", sortDirectionFieldName);
    sortDirectionToAction = sortDirectionToAction.replaceAll("IS-REQUIRED", "false");
    
    // Append sort field to action statement
    statements.append(sortDirectionToAction).append(LINE_SEPARATOR);

    return statements.toString();
  }
  
  /**
   * Creates and returns statements supporting a paging action.
   * @param viewActionId View action ID.
   * @param pagingActionId Paging action ID.
   * @param constantEntityName Entity name in constant format.
   * @param attributeLookup Map of attributes keyed by attribute name.
   * @param parentIdFieldId Parent entity ID field ID.
   * @param label Action label.
   * @param isSelectEntityAction Select entity action indicator.
   * @return String Action statements.
   */
  private static String createPagingActionStatements(String viewActionId, String pagingActionId, String constantEntityName, 
  		String[] filterAttributeNames, Map<String, Attribute> attributeLookup, String parentIdFieldId, String label, boolean isSelectEntityAction) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables
    String commonPagingName = TextConverter.convertConstantToCommon(pagingActionId);
    String actionName = TextConverter.convertCommonToCamel(commonPagingName, true);
    String commonViewName = TextConverter.convertConstantToCommon(viewActionId);
    String actionClass = TextConverter.convertCommonToCamel(commonViewName, false);
    
    // Create action statement
    String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
    action = action.replaceAll("ACTION-ID", pagingActionId);
    action = action.replaceAll("ACTION-LABEL", label);
    
    // Append action statement
    statements.append(action).append(LINE_SEPARATOR);
    
    
    // Create processor factory statement
    String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
    processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);
    
    // Append processor factory statement
    statements.append(processorFactory).append(LINE_SEPARATOR);
    

    // Create and append action field statements
    String actionFieldStatements = createActionFieldStatements(actionName, constantEntityName, filterAttributeNames, attributeLookup, parentIdFieldId, true, isSelectEntityAction);
    statements.append(actionFieldStatements);

    
    // Create action to actions statement
    String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);
    
    // Append action to actions statement
    statements.append(actionToActions).append(LINE_SEPARATOR);
    
    return statements.toString();
  }

  /**
   * Creates and returns statements supporting the add action.
   * @param addActionId Add action ID.
   * @param editActionId Edit action ID.
   * @return String Action statements.
   */
  private static String createAddActionStatements(String addActionId, String editActionId) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables based on add action ID
    String addCommonName = TextConverter.convertConstantToCommon(addActionId);
    String actionName = TextConverter.convertCommonToCamel(addCommonName, true);
    
    // Create template variables based on edit action ID
    String editCommonName = TextConverter.convertConstantToCommon(editActionId);
    String actionClass = TextConverter.convertCommonToCamel(editCommonName, false);
    
    
    // Create action statement
    String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
    action = action.replaceAll("ACTION-ID", addActionId);
    action = action.replaceAll("ACTION-LABEL", "Add");
    
    // Append action statement
    statements.append(action).append(LINE_SEPARATOR);
    
    
    // Create processor factory statement
    String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
    processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);
    
    // Append processor factory statement
    statements.append(processorFactory).append(LINE_SEPARATOR);
    
    
    // Create action to actions statement
    String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);
    
    // Append action to actions statement
    statements.append(actionToActions).append(LINE_SEPARATOR);
    
    return statements.toString();
  }

  /**
   * Creates and returns statements supporting the edit action.
   * @param editActionId Edit action ID.
   * @param idFieldId Entity ID field ID.
   * @param parentIdFieldId Parent entity ID field ID.
   * @return String Action statements.
   */
  private static String createEditActionStatements(String editActionId, String idFieldId, String parentIdFieldId) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables
    String commonName = TextConverter.convertConstantToCommon(editActionId);
    String actionName = TextConverter.convertCommonToCamel(commonName, true);
    String actionClass = TextConverter.convertCommonToCamel(commonName, false);
    
    
    // Create action statement
    String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
    action = action.replaceAll("ACTION-ID", editActionId);
    action = action.replaceAll("ACTION-LABEL", "Edit");
    
    // Append action statement
    statements.append(action).append(LINE_SEPARATOR);
    
    
    // Create processor factory statement
    String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
    processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);
    
    // Append processor factory statement
    statements.append(processorFactory).append(LINE_SEPARATOR);
    

    // Create ID field to action statement
    String idFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
    idFieldToAction = idFieldToAction.replaceAll("FIELD-ID", idFieldId);
    idFieldToAction = idFieldToAction.replaceAll("IS-REQUIRED", "true");
    
    // Append ID field to action statement
    statements.append(idFieldToAction).append(LINE_SEPARATOR);

    
    if (parentIdFieldId != null && parentIdFieldId.trim().length() > 0) {
      
      // Create parent ID field to action statement
      String parentIdFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      parentIdFieldToAction = parentIdFieldToAction.replaceAll("FIELD-ID", parentIdFieldId);
      parentIdFieldToAction = parentIdFieldToAction.replaceAll("IS-REQUIRED", "true");
      
      // Append parent ID field to action statement
      statements.append(parentIdFieldToAction).append(LINE_SEPARATOR);
    }
    
    
    // Create action to actions statement
    String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);
    
    // Append action to actions statement
    statements.append(actionToActions).append(LINE_SEPARATOR);
    
    return statements.toString();
  }

  /**
   * Creates and returns statements supporting the delete action.
   * @param deleteActionId Delete action ID.
   * @param idFieldId Entity ID field ID.
   * @return String Action statements.
   */
  private static String createDeleteActionStatements(String deleteActionId, String idFieldId) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables
    String commonName = TextConverter.convertConstantToCommon(deleteActionId);
    String actionName = TextConverter.convertCommonToCamel(commonName, true);
    String actionClass = TextConverter.convertCommonToCamel(commonName, false);
    
    
    // Create action statement
    String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
    action = action.replaceAll("ACTION-ID", deleteActionId);
    action = action.replaceAll("ACTION-LABEL", "Delete");
    
    // Append action statement
    statements.append(action).append(LINE_SEPARATOR);
    
    
    // Create processor factory statement
    String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
    processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);
    
    // Append processor factory statement
    statements.append(processorFactory).append(LINE_SEPARATOR);
    

    // Create ID field to action statement
    String idFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
    idFieldToAction = idFieldToAction.replaceAll("FIELD-ID", idFieldId);
    idFieldToAction = idFieldToAction.replaceAll("IS-REQUIRED", "true");
    
    // Append ID field to action statement
    statements.append(idFieldToAction).append(LINE_SEPARATOR);

    
    // Create action to actions statement
    String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);
    
    // Append action to actions statement
    statements.append(actionToActions).append(LINE_SEPARATOR);
    
    return statements.toString();
  }

  /**
   * Creates and returns statements supporting the save action.
   * @param saveActionId Save action ID.
   * @param constantEntityName Entity name constant format.
   * @param idFieldId Entity ID field ID.
   * @param versionFieldId Entity version field ID.
   * @param parentIdFieldId Parent entity ID field ID.
   * @param attributes Collection of attributes.
   * @return String Action statements.
   */
  private static String createSaveActionStatements(String saveActionId, String constantEntityName,
      String idFieldId, String versionFieldId, String parentIdFieldId, Collection<Attribute> attributes) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables
    String commonName = TextConverter.convertConstantToCommon(saveActionId);
    String actionName = TextConverter.convertCommonToCamel(commonName, true);
    String actionClass = TextConverter.convertCommonToCamel(commonName, false);
    
    
    // Create action statement
    String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
    action = action.replaceAll("ACTION-ID", saveActionId);
    action = action.replaceAll("ACTION-LABEL", "Save");
    
    // Append action statement
    statements.append(action).append(LINE_SEPARATOR);
    
    
    // Create processor factory statement
    String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
    processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);
    
    // Append processor factory statement
    statements.append(processorFactory).append(LINE_SEPARATOR);
    

    // Create ID field to action statement
    String idFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
    idFieldToAction = idFieldToAction.replaceAll("FIELD-ID", idFieldId);
    idFieldToAction = idFieldToAction.replaceAll("IS-REQUIRED", "false");
    
    // Append ID field to action statement
    statements.append(idFieldToAction).append(LINE_SEPARATOR);

    // Create version field to action statement
    String versionFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
    versionFieldToAction = versionFieldToAction.replaceAll("FIELD-ID", versionFieldId);
    versionFieldToAction = versionFieldToAction.replaceAll("IS-REQUIRED", "false");
    
    // Append version field to action statement
    statements.append(versionFieldToAction).append(LINE_SEPARATOR);

    
    if (parentIdFieldId != null && parentIdFieldId.trim().length() > 0) {
      
      // Create parent ID field to action statement
      String parentIdFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
      parentIdFieldToAction = parentIdFieldToAction.replaceAll("FIELD-ID", parentIdFieldId);
      parentIdFieldToAction = parentIdFieldToAction.replaceAll("IS-REQUIRED", "true");
      
      // Append parent ID field to action statement
      statements.append(parentIdFieldToAction).append(LINE_SEPARATOR);
    }

    if (attributes != null) {
      
      for (Attribute attribute : attributes) {
        
        // Create field ID
        String fieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(attribute.getName());
        
        // Set required indicator
        String isRequired = attribute.getIsRequired() ? "true" : "false";
        
        // Create attribute field to action statement
        String attributeFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
        attributeFieldToAction = attributeFieldToAction.replaceAll("FIELD-ID", fieldId);
        attributeFieldToAction = attributeFieldToAction.replaceAll("IS-REQUIRED", isRequired);
        
        // Append attribute field to action statement
        statements.append(attributeFieldToAction).append(LINE_SEPARATOR);
      }
    }
    
    // Create action to actions statement
    String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);
    
    // Append action to actions statement
    statements.append(actionToActions).append(LINE_SEPARATOR);
    
    return statements.toString();
  }

  /**
   * Creates and returns statements supporting the cancel action.
   * @param cancelActionId Cancel action ID.
   * @param viewActionId View action ID.
   * @return String Action statements.
   */
  private static String createCancelActionStatements(String cancelActionId, String viewActionId) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables based on cancel action ID
    String cancelCommonName = TextConverter.convertConstantToCommon(cancelActionId);
    String actionName = TextConverter.convertCommonToCamel(cancelCommonName, true);

    // Create template variables based on view action ID
    String viewCommonName = TextConverter.convertConstantToCommon(viewActionId);
    String actionClass = TextConverter.convertCommonToCamel(viewCommonName, false);
  
    // Create action statement
    String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
    action = action.replaceAll("ACTION-ID", cancelActionId);
    action = action.replaceAll("ACTION-LABEL", "Cancel");
    
    // Append action statement
    statements.append(action).append(LINE_SEPARATOR);
    
    
    // Create processor factory statement
    String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
    processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);
    
    // Append processor factory statement
    statements.append(processorFactory).append(LINE_SEPARATOR);
    
    
    // Create action to actions statement
    String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);
    
    // Append action to actions statement
    statements.append(actionToActions).append(LINE_SEPARATOR);
    
    return statements.toString();
  }
  /**
   * Creates and returns statements supporting a page.
   * @param directoryName Directory name.
   * @param pageId Page ID.
   * @return String Page statements.
   */
  private static String createPageStatements(String directoryName, String pageId) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables
    String commonName = TextConverter.convertConstantToCommon(pageId);
    String pageName = TextConverter.convertCommonToCamel(commonName, true);
    String properName = TextConverter.convertCommonToCamel(commonName, false);
    
    // Build full path name
    String fullPathName = directoryName + "/" + properName;
    
    // Create action statement
    String action = pageTemplate.replaceAll("PAGE-NAME", pageName);
    action = action.replaceAll("PAGE-ID", pageId);
    action = action.replaceAll("FILE-NAME", fullPathName);
    action = action.replaceAll("DEFAULT-DESCRIPTION", commonName);
    
    // Append action statement
    statements.append(action).append(LINE_SEPARATOR);
    
    // Create page to pages statement
    String pageToPages = addPageToPagesTemplate.replaceAll("PAGE-NAME", pageName);
    
    // Append page to pages statement
    statements.append(pageToPages).append(LINE_SEPARATOR);
    
    return statements.toString();
  }

  /**
   * Creates and returns statements supporting an object binding.
   * @param entityName Entity name.
   * @param parentIdFieldId Parent entity ID field ID.
   * @param attributes Collection of attributes.
   * @return String Object binding statements.
   */
  private static String createObjectBindingStatements(String entityName, String parentIdFieldId, Collection<Attribute> attributes) {

    // Initialize statements
    StringBuffer statements = new StringBuffer(LINE_SEPARATOR);

    // Create template variables
    String bindingName = TextConverter.convertCommonToCamel(entityName, true);
    String bindingClassName = TextConverter.convertCommonToCamel(entityName, false);

    // Create object binding statement
    String objectBinding = objectBindingTemplate.replaceAll("BINDING-NAME", bindingName);
    objectBinding = objectBinding.replaceAll("BINDING-CLASS", bindingClassName);
    
    // Append action statement
    statements.append(objectBinding).append(LINE_SEPARATOR);
    
    // Create constant entity name
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);


    // Create ID field ID
    String idFieldId = constantEntityName + "_ID";
    
    // Create field to binding statement for ID field
    String fieldToBinding = addFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
    fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", idFieldId);
    fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", "Id");

    // Append field to binding statement
    statements.append(fieldToBinding).append(LINE_SEPARATOR);
    
    
    // Create version field ID
    String versionFieldId = constantEntityName + "_VERSION";
    
    // Create field to binding statement for ID field
    fieldToBinding = addFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
    fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", versionFieldId);
    fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", "Version");

    // Append field to binding statement
    statements.append(fieldToBinding).append(LINE_SEPARATOR);

    
    if (parentIdFieldId != null && parentIdFieldId.trim().length() > 0) {
      
      // Create parent entity ID property name
      String commonParentEntityName = TextConverter.convertConstantToCommon(parentIdFieldId);
      String parentEntityPropertyName = TextConverter.convertCommonToCamel(commonParentEntityName, false);
      
      // Create field to binding statement for ID field
      fieldToBinding = addFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
      fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", parentIdFieldId);
      fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", parentEntityPropertyName);

      // Append field to binding statement
      statements.append(fieldToBinding).append(LINE_SEPARATOR);
    }
    
    for (Attribute attribute : attributes) {
      
      // Use attribute name as common name
      String commonName = attribute.getName();
      
      // Create template variables
      String constantAttributeName = TextConverter.convertCommonToConstant(commonName);
      String fieldId = constantEntityName + "_" + constantAttributeName;
      String propertyName = TextConverter.convertCommonToCamel(commonName, false);
      
      // Initialize associate attribute indicator
      boolean isAssociateAttribute = false;
      
      if (attribute.getType().equals(AttributeTypes.LOOKUP)) {
        
        // Cast to lookup attribute
        LookupAttribute lookupAttribute = (LookupAttribute)attribute;
        
        // Set associate attribute indicator
        isAssociateAttribute = lookupAttribute.getMultipleValues();
      }
      
      if (isAssociateAttribute) {
      	
        // Create field to binding statement
        fieldToBinding = addCollectionFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
        fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", fieldId);
        fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", propertyName);
        fieldToBinding = fieldToBinding.replaceAll("IS-COLLECTION", "true");
      }
      else {
      	
        // Create field to binding statement
        fieldToBinding = addFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
        fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", fieldId);
        fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", propertyName);
        fieldToBinding = fieldToBinding.replaceAll("IS-COLLECTION", "true");
      }

      // Append field to binding statement
      statements.append(fieldToBinding).append(LINE_SEPARATOR);
    }
    
    // Create binding to bindings statement
    String bindingToBindings = addBindingToBindingsTemplate.replaceAll("BINDING-NAME", bindingName);
    
    // Append binding to bindings statement
    statements.append(bindingToBindings).append(LINE_SEPARATOR);
    
    return statements.toString();
  }

  /**
   * Returns the parent entity ID.
   * @param definitionWizardData Definition wizard data.
   * @return String Parent entity ID.
   */
  private static String getParentIdFieldId(DefinitionWizardData definitionWizardData) {
    
    // Initialize return value
    String parentId = null;
    
    // Get parent entity name
    String parentEntityName = definitionWizardData.getParentEntityName();
    
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Get index of last package separator
      int index = parentEntityName.lastIndexOf(".");
      
      // Remove package from parent entity name
      if (index > 0) {
        parentEntityName = parentEntityName.substring(index + 1);
      }
      
      // Create constant parent entity name
      String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      String parentEntityConstantName = TextConverter.convertCommonToConstant(parentEntityCommonName);

      // Create parent ID
      parentId = parentEntityConstantName + "_ID";
    }
    
    return parentId;
  }
  
  /**
   * Returns a field type value for a given attribute.
   * @param attribute Attribute.
   * @return String Field type.
   */
  private static String getFieldType(Attribute attribute) {

    // Initialize return value
    String fieldType = null;
    
    // Get attribute type
    String type = attribute.getType();
    
    if (type.equals(AttributeTypes.TEXT) ||
    		type.equals(AttributeTypes.MEMO) || 
    		type.equals(AttributeTypes.EMAIL) || 
    		type.equals(AttributeTypes.PHONE_NUMBER)  || 
    		type.equals(AttributeTypes.POSTAL_CODE)) {
    	
      fieldType = "STRING";
    }
    else if (type.equals(AttributeTypes.DATE) || type.equals(AttributeTypes.TIME)) {
      fieldType = "DATE";
    }
    else if (type.equals(AttributeTypes.BOOLEAN)) {
      fieldType = "BOOLEAN";
    }
    else if (type.equals(AttributeTypes.LOOKUP)) {
      fieldType = "INTEGER";
    }
    else if (type.equals(AttributeTypes.NUMERIC)) {
      
      // Cast to numeric attribute
      NumericAttribute numericAttribute = (NumericAttribute)attribute;
      
      // Get scale
      Integer scale = numericAttribute.getScale();
      
      // Set type based on specified scale
      if (scale.intValue() == 0) {
        fieldType = "INTEGER";
      }
      else {
        fieldType = "DOUBLE";
      }
    }

    return fieldType;
  }
  
  /**
   * Returns a field converter for a given attribute.
   * @param attribute Attribute.
   * @return String Field converter.
   */
  private static String getFieldConverter(Attribute attribute) {
  
    // Initialize return value
    String fieldConverter = "null";
    
    // Get attribute type
    String type = attribute.getType();
    
    if (type.equals(AttributeTypes.BOOLEAN)) {
      fieldConverter = "BooleanConverter.getInstance()";
    }
    else if (type.equals(AttributeTypes.DATE)) {
      fieldConverter = "DateConverter.getInstance()";
    }
    else if (type.equals(AttributeTypes.TIME)) {
      fieldConverter = "TimeConverter.getInstance()";
    }
    else if (type.equals(AttributeTypes.LOOKUP)) {
      fieldConverter = "NumberConverter.getInstance()";
    }
    else if (type.equals(AttributeTypes.NUMERIC)) {

      // Cast to numeric attribute
      NumericAttribute numericAttribute = (NumericAttribute)attribute;
      
      // Get scale
      Integer scale = numericAttribute.getScale();
    	
      // Set type of numeric converter based on scale
      if (scale == null || scale.intValue() == 0) {
      	
      	fieldConverter = "NumberConverter.getInstance()";
      }
      else {
      	
      	fieldConverter = "DecimalConverter.getInstance()";
      }
    }
    
    return fieldConverter;
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
