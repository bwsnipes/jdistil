package com.bws.jdistil.project.fragment.process;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
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
import com.bws.jdistil.project.fragment.data.NumericAttribute;
import com.bws.jdistil.project.util.ResourceFinder;
import com.bws.jdistil.project.util.ResourceReader;
import com.bws.jdistil.project.util.TextConverter;

/**
 * Creates data source class files supporting the new entity defined in the application fragment wizard.
 * @author Bryan Snipes
 */
public class DataSourceFileCreator {

  /**
   * Line separator.updateDataManager
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Bindings variable text.
   */
  private static final String BINDINGS_VARIABLE_TEXT = "List<AssociateBinding> associateBindings = new ArrayList<AssociateBinding>();";
  		
  /**
   * Resource reader.
   */
  private static ResourceReader resourceReader = new ResourceReader();

  /**
   * Data object template.
   */
  private static String dataObjectTemplate = null;

  /**
   * Attribute statement template.
   */
  private static String attributeStatementTemplate = null;

  /**
   * Property statement template.
   */
  private static String propertyStatementTemplate = null;

  /**
   * Property getter statement template.
   */
  private static String propertyGetterStatementTemplate = null;

  /**
   * Property setter statement template.
   */
  private static String propertySetterStatementTemplate = null;

  /**
   * List type attribute statement template.
   */
  private static String listAttributeStatementTemplate = null;

  /**
   * List type property statement template.
   */
  private static String listPropertyStatementTemplate = null;

  /**
   * List type property getter statement template.
   */
  private static String listPropertyGetterStatementTemplate = null;

  /**
   * List type property setter template.
   */
  private static String listPropertySetterStatementTemplate = null;
  
  /**
   * Data manager template.
   */
  private static String dataManagerTemplate = null;
  
  /**
   * Column binding statement template.
   */
  private static String columnBindingStatementTemplate = null;
  
  /**
   * Column binding reference statement template.
   */
  private static String columnBindingReferenceStatementTemplate = null;
  
  /**
   * Associate bindings template.
   */
  private static String associateBindingsTemplate = null;
  
  /**
   * Associate binding statement template.
   */
  private static String associateBindingStatementTemplate = null;
  
  /**
   * Column binding variable template.
   */
  private static String columnBindingVariableTemplate = null;
  
  /**
   * Creates a new data source file creator.
   */
  public DataSourceFileCreator() {
    super();
  }
  
	/**
	 * Loads all templates. 
	 * @throws IOException
	 */
  private static void loadTemplates() throws IOException {
  	
    // Load templates
  	if (dataObjectTemplate == null) {
      dataObjectTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/data-object.txt");
  	}

  	if (attributeStatementTemplate == null) {
	  	attributeStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/attribute-statement.txt");
  	}

  	if (propertyStatementTemplate == null) {
	    propertyStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/property-statement.txt");
  	}

  	if (propertyGetterStatementTemplate == null) {
	    propertyGetterStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/property-getter-statement.txt");
  	}

  	if (propertySetterStatementTemplate == null) {
	    propertySetterStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/property-setter-statement.txt");
  	}

  	if (listAttributeStatementTemplate == null) {
	    listAttributeStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/list-attribute-statement.txt");
  	}

  	if (listPropertyStatementTemplate == null) {
	    listPropertyStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/list-property-statement.txt");
  	}

  	if (listPropertyGetterStatementTemplate == null) {
	    listPropertyGetterStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/list-property-getter-statement.txt");
  	}

  	if (listPropertySetterStatementTemplate == null) {
	    listPropertySetterStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/list-property-setter-statement.txt");
  	}

  	if (dataManagerTemplate == null) {
  		dataManagerTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/data-manager.txt");
  	}

  	if (columnBindingStatementTemplate == null) {
  		columnBindingStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/column-binding-statement.txt");
  	}

  	if (columnBindingReferenceStatementTemplate == null) {
  		columnBindingReferenceStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/column-binding-reference-statement.txt");
  	}

  	if (associateBindingsTemplate == null) {
  		associateBindingsTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/associate-bindings.txt");
  	}

  	if (associateBindingStatementTemplate == null) {
  		associateBindingStatementTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/associate-binding-statement.txt");
  	}
    
  	if (columnBindingVariableTemplate == null) {
  		columnBindingVariableTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/datasource/column-binding-variable.txt");
  	}
  }
  
  /**
   * Creates data source class files for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  public static void process(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, IProgressMonitor progressMonitor) 
      throws CoreException, IOException {
    
  	// Load templates
  	loadTemplates();
  	
    // Get the targeted project
    IProject project = definitionWizardData.getProject();
    
    // Create java project
    IJavaProject javaProject = JavaCore.create(project);

    // Get parent entity name
    String parentEntityName = getParentEntityName(definitionWizardData);

    // Create data object classes
    createDataObject(definitionWizardData, viewWizardData, javaProject, parentEntityName, progressMonitor);
    createDataManager(definitionWizardData, viewWizardData, javaProject, parentEntityName, progressMonitor);
  }
  
  /**
   * Updates data source class files for a fragment relationship.
   * @param relationshipWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  public static void process(RelationshipWizardData relationshipWizardData, IProgressMonitor progressMonitor) 
      throws CoreException, ProcessException, IOException {
    
  	// Load templates
  	loadTemplates();
  	
    // Get the targeted project
    IProject project = relationshipWizardData.getProject();
    
    // Create java project
    IJavaProject javaProject = JavaCore.create(project);

    // Update data object classes
    updateDataObjects(relationshipWizardData, javaProject, progressMonitor);
    updateDataManagers(relationshipWizardData, javaProject, progressMonitor);
  }
  
  /**
   * Creates a data object class file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param javaProject Java project.
   * @param parentEntityName Parent entity name.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void createDataObject(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
      IJavaProject javaProject, String parentEntityName, IProgressMonitor progressMonitor) 
      throws CoreException, IOException {
    
    // Initialize attribute statements and property statements
    StringBuffer attributeStatements = new StringBuffer();
    StringBuffer propertyStatements = new StringBuffer();
    
    // Initialize date and list required indicators
    boolean dateTypeRequired = false;
    boolean listTypeRequired = false;
    
    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    if (!attributes.isEmpty()) {
      
      for (Attribute attribute : attributes) {

        // Add line separator for correct spacing
        attributeStatements.append(LINE_SEPARATOR);
        propertyStatements.append(LINE_SEPARATOR);
        
        // Initialize attribute and property statements
        String attributeStatement = attributeStatementTemplate;
        String propertyStatement = propertyStatementTemplate;
        
        // Get attribute properties
        String name = attribute.getName();
        String type = attribute.getType();
        String javaType = attribute.getJavaType();
        
        if (type.equals(AttributeTypes.DATE) || type.equals(AttributeTypes.TIME)) {
        	
          // Set date required indicator
          dateTypeRequired = true;
        }
        else if (type.equals(AttributeTypes.LOOKUP)) {
          
          // Cast to lookup attribute
          LookupAttribute lookupAttribute = (LookupAttribute)attribute;
          
          if (lookupAttribute.getMultipleValues()) {
            
            // Set list type required indicator
            listTypeRequired = true;
            
            // Set attribute and property statements supporting list type
            attributeStatement = listAttributeStatementTemplate;
            propertyStatement = listPropertyStatementTemplate;
          }
        }
        
        // Create statement variables
        String attributeName = TextConverter.convertCommonToCamel(name, true);
        String attributeDescription = TextConverter.convertAsSentence(name, true);
        String propertyName = TextConverter.convertCommonToCamel(name, false);
        String propertyDescription = TextConverter.convertAsSentence(name, false);
        
        // Create attribute statement
        attributeStatement = attributeStatement.replaceAll("ATTRIBUTE-DESCRIPTION", attributeDescription);
        attributeStatement = attributeStatement.replaceAll("ATTRIBUTE-TYPE", javaType);
        attributeStatement = attributeStatement.replaceAll("ATTRIBUTE-NAME", attributeName);
        
        // Add attribute statement
        attributeStatements.append(attributeStatement).append(LINE_SEPARATOR);

        // Create property statement
        propertyStatement = propertyStatement.replaceAll("ATTRIBUTE-DESCRIPTION", attributeDescription);
        propertyStatement = propertyStatement.replaceAll("ATTRIBUTE-TYPE", javaType);
        propertyStatement = propertyStatement.replaceAll("ATTRIBUTE-NAME", attributeName);
        propertyStatement = propertyStatement.replaceAll("PROPERTY-DESCRIPTION", propertyDescription);
        propertyStatement = propertyStatement.replaceAll("PROPERTY-NAME", propertyName);
       
        // Add property statement
        propertyStatements.append(propertyStatement).append(LINE_SEPARATOR);
      }
    }
    
   
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Create parent entity field ID
      String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      
      // Create statement variables
      String attributeName = TextConverter.convertCommonToCamel(parentEntityCommonName, true) + "Id";
      String attributeDescription = TextConverter.convertAsSentence(parentEntityCommonName, true) + " ID";
      String propertyName = TextConverter.convertCommonToCamel(parentEntityCommonName, false) + "Id";
      String propertyDescription = TextConverter.convertAsSentence(parentEntityCommonName, false) + " ID";
      
      // Create attribute statement
      String attributeStatement = attributeStatementTemplate.replaceAll("ATTRIBUTE-DESCRIPTION", attributeDescription);
      attributeStatement = attributeStatement.replaceAll("ATTRIBUTE-TYPE", "Integer");
      attributeStatement = attributeStatement.replaceAll("ATTRIBUTE-NAME", attributeName);
      
      // Add attribute statement
      attributeStatements.append(attributeStatement).append(LINE_SEPARATOR);

      // Create property statement
      String propertyStatement = propertyStatementTemplate.replaceAll("ATTRIBUTE-DESCRIPTION", attributeDescription);
      propertyStatement = propertyStatement.replaceAll("ATTRIBUTE-TYPE", "Integer");
      propertyStatement = propertyStatement.replaceAll("ATTRIBUTE-NAME", attributeName);
      propertyStatement = propertyStatement.replaceAll("PROPERTY-DESCRIPTION", propertyDescription);
      propertyStatement = propertyStatement.replaceAll("PROPERTY-NAME", propertyName);
     
      // Add property statement
      propertyStatements.append(propertyStatement).append(LINE_SEPARATOR);
    }    
    
    
    // Get package name and entity name
    String packageName = definitionWizardData.getPackageName();
    String entityName = definitionWizardData.getEntityName();
    
    // Create template variables
    String dataObjectName = TextConverter.convertAsSentence(entityName, false);
    String dataObjectClassName = TextConverter.convertCommonToCamel(entityName, false);
    
    // Set data object variables
    String dataObject = dataObjectTemplate;
    dataObject = dataObject.replaceAll("PACKAGE-NAME", packageName);
    dataObject = dataObject.replaceAll("DATA-OBJECT-NAME", dataObjectName);
    dataObject = dataObject.replaceAll("DATA-OBJECT-CLASS-NAME", dataObjectClassName);
    dataObject = dataObject.replaceAll("ATTRIBUTES", attributeStatements.toString());
    dataObject = dataObject.replaceAll("PROPERTIES", propertyStatements.toString());
    
    // Initialize imports
    StringBuffer imports = new StringBuffer("");
    
    // Add conditional imports
    if (dateTypeRequired) {
      imports.append("import java.util.Date;").append(LINE_SEPARATOR);
    }
    if (listTypeRequired) {
      imports.append("import java.util.Collections;").append(LINE_SEPARATOR);
      imports.append("import java.util.ArrayList;").append(LINE_SEPARATOR);
      imports.append("import java.util.List;").append(LINE_SEPARATOR);
    }

    // Set imports
    dataObject = dataObject.replaceAll("IMPORTS", imports.toString());

    // Create data object file name
    String dataObjectFileName = dataObjectClassName + ".java";

    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = ResourceFinder.findSourcePackageFragmentRoot(javaProject);
    
    // Create and open package fragment
    IPackageFragment packageFragment = sourcePackageFragmentRoot.createPackageFragment(packageName, true, progressMonitor);
    packageFragment.open(progressMonitor);
    
    // Create data object
    packageFragment.createCompilationUnit(dataObjectFileName, dataObject, true, progressMonitor);
  }
  
  /**
   * Update data object class files for a fragment relationship.
   * @param relationshipWizardData Relationship wizard data.
   * @param javaProject Java project.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void updateDataObjects(RelationshipWizardData relationshipWizardData, IJavaProject javaProject, IProgressMonitor progressMonitor) 
      throws CoreException, ProcessException, IOException {
    
    // Get entity name
    String entity1Name = relationshipWizardData.getDataObject1Name();

    // Get base package and entity name
    String basePackage1Name = TextConverter.getPackageName(entity1Name);
    String baseEntity1Name = TextConverter.getBaseClassName(entity1Name);
    
    // Get entity name
    String entity2Name = relationshipWizardData.getDataObject2Name();

    // Get base package and entity name
    String basePackage2Name = TextConverter.getPackageName(entity2Name);
    String baseEntity2Name = TextConverter.getBaseClassName(entity2Name);
    
    boolean isManyToManyAssociation = relationshipWizardData.getAssociationType().equals(RelationshipWizardData.MANY_TO_MANY);
    
  	// Update data object for entity 1
    updateDataObject(basePackage1Name, baseEntity1Name, baseEntity2Name, isManyToManyAssociation, javaProject, progressMonitor);
    
    if (relationshipWizardData.isBidirectional()) {

    	// Update data object for entity 2
      updateDataObject(basePackage2Name, baseEntity2Name, baseEntity1Name, isManyToManyAssociation, javaProject, progressMonitor);
    }
  }
  
  /**
   * Update a data object class file for a fragment relationship.
   * @param primaryPackageName Primary package name.
   * @param primaryEntityName Primary entity name.
   * @param secondaryEntityName Secondary entity name.
   * @param isManyToManyAssociation Many to many association indicator.
   * @param javaProject Java project.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void updateDataObject(String primaryPackageName, String primaryEntityName, String secondaryEntityName, boolean isManyToManyAssociation, 
  		IJavaProject javaProject, IProgressMonitor progressMonitor) 
      throws CoreException, ProcessException, IOException {
    
    // Convert to primary base names
    String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
    String primaryUpperCaseCamel = TextConverter.convertCommonToCamel(primaryCommonName, false);

    // Convert to secondary base names
    String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
    String secondaryLowerCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, true);
    String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, false);
    
    // Get package name and class name
    String className = primaryUpperCaseCamel + ".java";
  	
    // Get property name
    String property1Name = secondaryUpperCaseCamel + "Id";
    
    // Get attribute name
    String attribute1Name = secondaryLowerCaseCamel + "Id";
    
    // Get attribute description
    String attribute1Description = secondaryCommonName.toLowerCase() + " ID";
    
    // Initialize statements
    String attributeStatement = attributeStatementTemplate;
    String propertyGetterStatement = propertyGetterStatementTemplate;
    String propertySetterStatement = propertySetterStatementTemplate;
    
    if (isManyToManyAssociation) {
    	
      // Pluralize names
      property1Name = property1Name + "s";
      attribute1Name = attribute1Name + "s";
      attribute1Description = attribute1Description + "s";

      // Use list statement templates
      attributeStatement = listAttributeStatementTemplate;
      propertyGetterStatement = listPropertyGetterStatementTemplate;
      propertySetterStatement = listPropertySetterStatementTemplate;
    }
    
    // Create attribute statement
    attributeStatement = attributeStatement.replaceAll("ATTRIBUTE-DESCRIPTION", attribute1Description);
    attributeStatement = attributeStatement.replaceAll("ATTRIBUTE-TYPE", "Integer");
    attributeStatement = attributeStatement.replaceAll("ATTRIBUTE-NAME", attribute1Name);
    
    // Create property getter statement
    propertyGetterStatement = propertyGetterStatement.replaceAll("ATTRIBUTE-DESCRIPTION", attribute1Description);
    propertyGetterStatement = propertyGetterStatement.replaceAll("ATTRIBUTE-TYPE", "Integer");
    propertyGetterStatement = propertyGetterStatement.replaceAll("ATTRIBUTE-NAME", attribute1Name);
    propertyGetterStatement = propertyGetterStatement.replaceAll("PROPERTY-NAME", property1Name);

    // Create property setter statement
    propertySetterStatement = propertySetterStatement.replaceAll("ATTRIBUTE-DESCRIPTION", attribute1Description);
    propertySetterStatement = propertySetterStatement.replaceAll("ATTRIBUTE-TYPE", "Integer");
    propertySetterStatement = propertySetterStatement.replaceAll("ATTRIBUTE-NAME", attribute1Name);
    propertySetterStatement = propertySetterStatement.replaceAll("PROPERTY-NAME", property1Name);


    // Find primary compilation unit
    ICompilationUnit entityCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, primaryPackageName, className);
    
    // Verify compilation unit was found
    if (entityCompilationUnit == null) {
      throw new ProcessException("Error updating data object information: " + primaryPackageName + "." + className + " not found.");
    }
    
    if (isManyToManyAssociation) {
    	
      // Attempt to get required import declarations
      IImportDeclaration arrayListImport = entityCompilationUnit.getImport("java.util.ArrayList");
      IImportDeclaration collectionsImport = entityCompilationUnit.getImport("java.util.Collections");
      IImportDeclaration listImport = entityCompilationUnit.getImport("java.util.List");
      
      // Create imports if not already defined
      if (arrayListImport == null || !arrayListImport.exists()) {
      	entityCompilationUnit.createImport("java.util.ArrayList", null, progressMonitor);
      }

      if (collectionsImport == null || !collectionsImport.exists()) {
      	entityCompilationUnit.createImport("java.util.Collections", null, progressMonitor);
      }
      
      if (listImport == null || !listImport.exists()) {
      	entityCompilationUnit.createImport("java.util.List", null, progressMonitor);
      }
    }

    // Get entity type
    IType entityType = entityCompilationUnit.findPrimaryType();
    
    // Create attribute
    entityType.createField(attributeStatement, null, true, progressMonitor);
    
    // Create getter and setter methods
    entityType.createMethod(propertyGetterStatement, null, true, progressMonitor);
    entityType.createMethod(propertySetterStatement, null, true, progressMonitor);
  }
  
  /**
   * Creates a data manager class file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param javaProject Java project.
   * @param parentEntityName Parent entity name.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void createDataManager(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
      IJavaProject javaProject, String parentEntityName, IProgressMonitor progressMonitor) 
      throws CoreException, IOException {
    
    // Initialize column binding and associate binding statements
    StringBuffer columnBindingStatements = new StringBuffer();
    StringBuffer associateBindingStatements = new StringBuffer();
    
    // Get package name and entity name
    String packageName = definitionWizardData.getPackageName();
    String entityName = definitionWizardData.getEntityName();
    
    // Create template variables
    String entityClassName = TextConverter.convertCommonToCamel(entityName, false);
    String managerName = TextConverter.convertAsSentence(entityName, true);
    String managerClassName = entityClassName + "Manager";
    String tableName = TextConverter.convertCommonToConstant(entityName);
    String idColumnName = tableName + "_ID";

    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    if (!attributes.isEmpty()) {
      
      for (Attribute attribute : definitionWizardData.getAttributes()) {

      	// Set lookup attribute indicator
      	boolean isLookupAttribute = attribute.getType().equals(AttributeTypes.LOOKUP);
      	
        // Initialize associate attribute indicator
        boolean isAssociateAttribute = false;
        
        if (isLookupAttribute) {
          
          // Cast to lookup attribute
          LookupAttribute lookupAttribute = (LookupAttribute)attribute;
          
          // Set associate attribute indicator
          isAssociateAttribute = lookupAttribute.getMultipleValues();
        }
        
        if (isAssociateAttribute) {
          
          // Get attribute name
          String attributeName = attribute.getName();
          
          // Create column binding statement variables
          String propertyName = TextConverter.convertCommonToCamel(attributeName, false);
          String associateTableName = tableName + "_" + TextConverter.convertCommonToConstant(attributeName);

          if (isLookupAttribute) {

            // Create column binding statement
            String associateBindingStatement = associateBindingStatementTemplate.replaceAll("PROPERTY-NAME", propertyName);
            associateBindingStatement = associateBindingStatement.replaceAll("ASSOCIATE-TABLE-NAME", associateTableName.toLowerCase());
            associateBindingStatement = associateBindingStatement.replaceAll("ASSOCIATE-COLUMN-BINDING-NAME", "codeIdColumnBinding");
            associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-TABLE-NAME", "bws_code");
            associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-ID-COLUMN-NAME", "code_id");
            associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-COLUMN-NAME", "name");
            
            // Add attribute statement
            associateBindingStatements.append(associateBindingStatement);
          }
        }
        else {
          
          // Get attribute name
          String name = attribute.getName();
          
          // Create column binding statement variables
          String columnType = getColumnType(attribute);
          String columnName = TextConverter.convertCommonToConstant(name);
          String propertyName = TextConverter.convertCommonToCamel(name, false);
          
          if (isLookupAttribute) {
          	
            // Create column binding statement with reference support
            String columnBindingStatement = columnBindingReferenceStatementTemplate.replaceAll("PRIMARY-COLUMN-NAME", columnName.toLowerCase());
            columnBindingStatement = columnBindingStatement.replaceAll("COLUMN-TYPE", columnType);
            columnBindingStatement = columnBindingStatement.replaceAll("IS-UNIQUE-MEMBER", "false");
            columnBindingStatement = columnBindingStatement.replaceAll("IS-VIRTUAL-DELETE", "false");
            columnBindingStatement = columnBindingStatement.replaceAll("PROPERTY-NAME", propertyName);
            columnBindingStatement = columnBindingStatement.replaceAll("REFERENCE-TABLE-NAME", "bws_code");
            columnBindingStatement = columnBindingStatement.replaceAll("REFERENCE-ID-COLUMN-NAME", "code_id");
            columnBindingStatement = columnBindingStatement.replaceAll("REFERENCE-COLUMN-NAME", "name");

            // Add attribute statement
            columnBindingStatements.append(columnBindingStatement);
          }
          else {
          	
            // Create column binding statement
            String columnBindingStatement = columnBindingStatementTemplate.replaceAll("PRIMARY-COLUMN-NAME", columnName.toLowerCase());
            columnBindingStatement = columnBindingStatement.replaceAll("COLUMN-TYPE", columnType);
            columnBindingStatement = columnBindingStatement.replaceAll("IS-UNIQUE-MEMBER", "false");
            columnBindingStatement = columnBindingStatement.replaceAll("IS-VIRTUAL-DELETE", "false");
            columnBindingStatement = columnBindingStatement.replaceAll("PROPERTY-NAME", propertyName);

            // Add attribute statement
            columnBindingStatements.append(columnBindingStatement);
          }
        }
      }
    }
    
    
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Create column name
      String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      String parentEntityConstantName = TextConverter.convertCommonToConstant(parentEntityCommonName);
      String columnName = parentEntityConstantName + "_ID";
      
      // Create property name
      String commonColumnName = TextConverter.convertConstantToCommon(columnName);
      String propertyName = TextConverter.convertCommonToCamel(commonColumnName, false);
      
      // Create column binding statement
      String columnBindingStatement = columnBindingStatementTemplate.replaceAll("PRIMARY-COLUMN-NAME", columnName.toLowerCase());
      columnBindingStatement = columnBindingStatement.replaceAll("COLUMN-TYPE", "INTEGER");
      columnBindingStatement = columnBindingStatement.replaceAll("IS-UNIQUE-MEMBER", "false");
      columnBindingStatement = columnBindingStatement.replaceAll("IS-VIRTUAL-DELETE", "false");
      columnBindingStatement = columnBindingStatement.replaceAll("PROPERTY-NAME", propertyName);

      // Add attribute statement
      columnBindingStatements.append(columnBindingStatement);
    }

    
    // Initialize associate bindings data
    String associateBindings = "";
    
    if (associateBindingStatements.length() > 0) {
      
      // Set associate bindings
      associateBindings = associateBindingsTemplate.replaceAll("ASSOCIATE-BINDING-STATEMENTS", associateBindingStatements.toString());
    }
    
    // Set data manager variables
    String dataManager = dataManagerTemplate;
    dataManager = dataManager.replaceAll("PACKAGE-NAME", packageName);
    dataManager = dataManager.replaceAll("ENTITY-NAME", entityName.toLowerCase());
    dataManager = dataManager.replaceAll("ENTITY-CLASS-NAME", entityClassName);
    dataManager = dataManager.replaceAll("MANAGER-NAME", managerName);
    dataManager = dataManager.replaceAll("MANAGER-CLASS-NAME", managerClassName);
    dataManager = dataManager.replaceAll("TABLE-NAME", tableName.toLowerCase());
    dataManager = dataManager.replaceAll("ID-COLUMN-NAME", idColumnName.toLowerCase());
    dataManager = dataManager.replaceAll("COLUMN-BINDING-STATEMENTS", columnBindingStatements.toString());
    dataManager = dataManager.replaceAll("ASSOCIATE-BINDING-STATEMENTS", associateBindings);
    
    // Create data manager file name
    String dataManagerFileName = managerClassName + ".java";

    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = ResourceFinder.findSourcePackageFragmentRoot(javaProject);
    
    // Create and open package fragment
    IPackageFragment packageFragment = sourcePackageFragmentRoot.createPackageFragment(packageName, true, progressMonitor);
    packageFragment.open(progressMonitor);
    
    // Create data manager
    packageFragment.createCompilationUnit(dataManagerFileName, dataManager, true, progressMonitor);
  }

  /**
   * Update data manager class files for a fragment relationship.
   * @param relationshipWizardData Relationship wizard data.
   * @param javaProject Java project.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void updateDataManagers(RelationshipWizardData relationshipWizardData, IJavaProject javaProject, IProgressMonitor progressMonitor) 
      throws CoreException, ProcessException, IOException {
    
    // Get entity name
    String entity1Name = relationshipWizardData.getDataObject1Name();

    // Get base package and entity name
    String basePackage1Name = TextConverter.getPackageName(entity1Name);
    String baseEntity1Name = TextConverter.getBaseClassName(entity1Name);
    
    // Get entity attribute name
    String entity1AttributeName = relationshipWizardData.getAttribute1Name();

    // Get entity name
    String entity2Name = relationshipWizardData.getDataObject2Name();

    // Get base package and entity name
    String basePackage2Name = TextConverter.getPackageName(entity2Name);
    String baseEntity2Name = TextConverter.getBaseClassName(entity2Name);
    
    // Get entity attribute name
    String entity2AttributeName = relationshipWizardData.getAttribute2Name();
    
    boolean isManyToManyAssociation = relationshipWizardData.getAssociationType().equals(RelationshipWizardData.MANY_TO_MANY);

    if (isManyToManyAssociation) {
    	
      // Update data manager for entity 1
      updateDataManager(basePackage1Name, baseEntity1Name, baseEntity2Name, entity2AttributeName, true, javaProject, progressMonitor);
      
      if (relationshipWizardData.isBidirectional()) {

      	// Update data manager for entity 2
        updateDataManager(basePackage2Name, baseEntity2Name, baseEntity1Name, entity1AttributeName, false, javaProject, progressMonitor);
      }  	
    }
    else {

    	// Update data manager for entity 2
      updateDataManager(basePackage1Name, baseEntity1Name, baseEntity2Name, entity2AttributeName, javaProject, progressMonitor);
    }
  }
  
  /**
   * Update a data manager class file for a fragment relationship.
   * @param primaryPackageName Primary package name.
   * @param primaryEntityName Primary entity name.
   * @param secondaryEntityName Secondary entity name.
   * @param secondaryEntityAttributeName Secondary entity attribute name.
   * @param isPrimary Primary entity indicator.
   * @param javaProject Java project.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void updateDataManager(String primaryPackageName, String primaryEntityName, String secondaryEntityName, 
  		String secondaryEntityAttributeName, boolean isPrimary, IJavaProject javaProject, IProgressMonitor progressMonitor) 
      throws CoreException, ProcessException, IOException {
  	
    // Convert to primary base names
    String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
    String primaryConstant = TextConverter.convertCommonToConstant(primaryCommonName);
    String primaryUpperCaseCamel = TextConverter.convertCommonToCamel(primaryCommonName, false);

    // Convert to secondary base names
    String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
    String secondaryConstant = TextConverter.convertCommonToConstant(secondaryCommonName);
    String secondaryLowerCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, true);
    String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, false);
    
    // Create template variables
    String idVariableName = secondaryLowerCaseCamel + "IdColumnBinding";
    String idColumnName = secondaryLowerCaseCamel + "_id";
    String propertyName = secondaryUpperCaseCamel + "Ids";
    String referenceColumnName = TextConverter.convertCommonToConstant(secondaryEntityAttributeName);
    
    // Initialize table name
    String tableName = null;

    // Build table name
    if (isPrimary) {
    	tableName = primaryConstant + "_" + secondaryConstant;
    }
    else {
    	tableName = secondaryConstant + "_" + primaryConstant;
    }
    
    // Create bindings statements
    StringBuffer bindingStatements = new StringBuffer(LINE_SEPARATOR).append(LINE_SEPARATOR);
    
    // Create ID column variable
    String columnBindingVariable = columnBindingVariableTemplate.replaceAll("ID-COLUMN-DESC", secondaryCommonName);
    columnBindingVariable = columnBindingVariable.replaceAll("ID-VARIABLE-NAME", idVariableName);
    columnBindingVariable = columnBindingVariable.replaceAll("ID-COLUMN-NAME", idColumnName);

    // Update binding statements
    bindingStatements.append(columnBindingVariable).append(LINE_SEPARATOR);
    
    // Create column binding statement
    String associateBindingStatement = associateBindingStatementTemplate.replaceAll("PROPERTY-NAME", propertyName);
    associateBindingStatement = associateBindingStatement.replaceAll("ASSOCIATE-TABLE-NAME", tableName.toLowerCase());
    associateBindingStatement = associateBindingStatement.replaceAll("ASSOCIATE-COLUMN-BINDING-NAME", idVariableName);
    associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-TABLE-NAME", secondaryConstant.toLowerCase());
    associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-ID-COLUMN-NAME", idColumnName);
    associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-COLUMN-NAME", referenceColumnName.toLowerCase());
    
    // Update binding statements
    bindingStatements.append(associateBindingStatement).append(LINE_SEPARATOR);
    
  	
    // Get package name and class name
    String className = primaryUpperCaseCamel + "Manager.java";

    // Find data manager compilation unit
    ICompilationUnit dataManagerCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, primaryPackageName, className);
      
    // Verify compilation unit was found
    if (dataManagerCompilationUnit == null) {
      throw new ProcessException("Error updating " + primaryCommonName + " information: " + className + " class not found.");
    }

  	
    // Get data manager type
    IType dataManagerType = dataManagerCompilationUnit.findPrimaryType();
    
    // Get create data object binding method from data manager type
    IMethod createDataObjectBindingMethod = dataManagerType.getMethod("createDataObjectBinding", null);

    // Get source range
    ISourceRange sourceRange = createDataObjectBindingMethod.getSourceRange();
    
    // Get method source
    String methodSource = createDataObjectBindingMethod.getSource();
    
    // Get position of bindings variable
    int bindingVariablePosition = methodSource.lastIndexOf(BINDINGS_VARIABLE_TEXT);
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bindingVariablePosition + BINDINGS_VARIABLE_TEXT.length();

    // Get working copy
    ICompilationUnit workingCopy = dataManagerCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, bindingStatements.toString());

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
   * Update a data manager class file for a fragment relationship.
   * @param primaryPackageName Primary package name.
   * @param primaryEntityName Primary entity name.
   * @param secondaryEntityName Secondary entity name.
   * @param secondaryEntityAttributeName Secondary entity attribute name.
   * @param javaProject Java project.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void updateDataManager(String primaryPackageName, String primaryEntityName, String secondaryEntityName, 
  		String secondaryEntityAttributeName, IJavaProject javaProject, IProgressMonitor progressMonitor) 
      throws CoreException, ProcessException, IOException {
  	
    // Convert to primary base names
    String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
    String primaryUpperCaseCamel = TextConverter.convertCommonToCamel(primaryCommonName, false);

    // Convert to secondary base names
    String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
    String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);
    String secondaryLowerCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, true);
    String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, false);
    
    // Create template variables
    String idColumnName = secondaryLowerCaseCamel + "_id";
    String propertyName = secondaryUpperCaseCamel + "Id";
    String referenceColumnName = TextConverter.convertCommonToConstant(secondaryEntityAttributeName);
    
    // Create bindings statements
    StringBuffer bindingStatements = new StringBuffer(LINE_SEPARATOR).append(LINE_SEPARATOR);
    
    // Create column binding statement
    String columnBindingStatement = columnBindingStatementTemplate.replaceAll("PRIMARY-COLUMN-NAME", idColumnName);
    columnBindingStatement = columnBindingStatement.replaceAll("COLUMN-TYPE", "INTEGER");
    columnBindingStatement = columnBindingStatement.replaceAll("IS-UNIQUE-MEMBER", "false");
    columnBindingStatement = columnBindingStatement.replaceAll("IS-VIRTUAL-DELETE", "false");
    columnBindingStatement = columnBindingStatement.replaceAll("PROPERTY-NAME", propertyName);
    columnBindingStatement = columnBindingStatement.replaceAll("REFERENCE-TABLE-NAME", secondaryConstantName.toLowerCase());
    columnBindingStatement = columnBindingStatement.replaceAll("REFERENCE-ID-COLUMN-NAME", idColumnName);
    columnBindingStatement = columnBindingStatement.replaceAll("REFERENCE-COLUMN-NAME", referenceColumnName.toLowerCase());
    
    // Update binding statements
    bindingStatements.append(columnBindingStatement).append(LINE_SEPARATOR);
    

    // Get package name and class name
    String className = primaryUpperCaseCamel + "Manager.java";

    // Find data manager compilation unit
    ICompilationUnit dataManagerCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, primaryPackageName, className);
      
    // Verify compilation unit was found
    if (dataManagerCompilationUnit == null) {
      throw new ProcessException("Error updating " + primaryCommonName + " information: " + className + " class not found.");
    }

  	
    // Get data manager type
    IType dataManagerType = dataManagerCompilationUnit.findPrimaryType();
    
    // Get create data object binding method from data manager type
    IMethod createDataObjectBindingMethod = dataManagerType.getMethod("createDataObjectBinding", null);

    // Get source range
    ISourceRange sourceRange = createDataObjectBindingMethod.getSourceRange();
    
    // Get method source
    String methodSource = createDataObjectBindingMethod.getSource();
    
    // Get position of bindings variable
    int bindingVariablePosition = methodSource.lastIndexOf(BINDINGS_VARIABLE_TEXT);
    
    // Calculate edit position
    int editPosition = sourceRange.getOffset() + bindingVariablePosition + BINDINGS_VARIABLE_TEXT.length();

    // Get working copy
    ICompilationUnit workingCopy = dataManagerCompilationUnit.getWorkingCopy(progressMonitor);
    
    try {
      // Create insert edit containing new method text
      InsertEdit insertEdit = new InsertEdit(editPosition, bindingStatements.toString());

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
   * Returns the parent entity name.
   * @param definitionWizardData Definition wizard data.
   * @return String Parent entity name.
   */
  private static String getParentEntityName(DefinitionWizardData definitionWizardData) {
    
    // Get parent entity name
    String parentEntityName = definitionWizardData.getParentEntityName();
    
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Get index of last package separator
      int index = parentEntityName.lastIndexOf(".");
      
      // Remove package from parent entity name
      if (index > 0) {
        parentEntityName = parentEntityName.substring(index + 1);
      }
    }
    
    return parentEntityName;
  }
  
  /**
   * Returns a column type value based on a specified attribute.
   * @param attribute Attribute.
   * @return String Column type.
   */
  private static String getColumnType(Attribute attribute) {

    // Initialize return value
    String columnType = null;
    
    // Get attribute type
    String type = attribute.getType();
    
    if (type.equals(AttributeTypes.TEXT) || 
    		type.equals(AttributeTypes.MEMO) || 
    		type.equals(AttributeTypes.EMAIL) || 
    		type.equals(AttributeTypes.PHONE_NUMBER) || 
    		type.equals(AttributeTypes.POSTAL_CODE)) {
    	
      columnType = "STRING";
    }
    else if (type.equals(AttributeTypes.DATE)) {
      columnType = "DATE";
    }
    else if (type.equals(AttributeTypes.TIME)) {
      columnType = "TIME";
    }
    else if (type.equals(AttributeTypes.BOOLEAN)) {
      columnType = "BOOLEAN";
    }
    else if (type.equals(AttributeTypes.LOOKUP)) {
      columnType = "INTEGER";
    }
    else if (type.equals(AttributeTypes.NUMERIC)) {
      
      // Cast to numeric attribute
      NumericAttribute numericAttribute = (NumericAttribute)attribute;
      
      // Get scale
      Integer scale = numericAttribute.getScale();
      
      // Set type based on specified scale
      if (scale.intValue() == 0) {
        columnType = "INTEGER";
      }
      else {
        columnType = "DOUBLE";
      }
    }

    return columnType;
  }
  
}
