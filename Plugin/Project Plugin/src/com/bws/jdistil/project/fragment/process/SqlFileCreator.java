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

import com.bws.jdistil.project.fragment.DefinitionWizardData;
import com.bws.jdistil.project.fragment.RelationshipWizardData;
import com.bws.jdistil.project.fragment.data.Attribute;
import com.bws.jdistil.project.fragment.data.AttributeTypes;
import com.bws.jdistil.project.fragment.data.EmailAttribute;
import com.bws.jdistil.project.fragment.data.LookupAttribute;
import com.bws.jdistil.project.fragment.data.MemoAttribute;
import com.bws.jdistil.project.fragment.data.NumericAttribute;
import com.bws.jdistil.project.fragment.data.PhoneNumberAttribute;
import com.bws.jdistil.project.fragment.data.PostalCodeAttribute;
import com.bws.jdistil.project.fragment.data.TextAttribute;
import com.bws.jdistil.project.util.ResourceReader;
import com.bws.jdistil.project.util.TextConverter;

/**
 * Creates SQL files supporting the new entity defined in the application fragment wizard.
 * @author Bryan Snipes
 */
public class SqlFileCreator {

  /**
   * Line separator.
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Resource reader.
   */
  private static ResourceReader resourceReader = new ResourceReader();

  /**
   * Create table template.
   */
  private static String createTableTemplate = null;

  /**
   * Alter table template.
   */
  private static String alterTableTemplate = null;

  /**
   * Column template.
   */
  private static String columnTemplate = null;

  /**
   * Primary key template.
   */
  private static String primaryKeyTemplate = null;

  /**
   * Foreign key template.
   */
  private static String foreignKeyTemplate = null;

  /**
   * Index template.
   */
  private static String indexTemplate = null;

  /**
   * Associate table template.
   */
  private static String associateTableTemplate = null;
  
  /**
   * Sequence template.
   */
  private static String sequenceTemplate = null;

  /**
   * Creates a new SQL file creator.
   */
  public SqlFileCreator() {
    super();
  }
  
	/**
	 * Loads all templates. 
	 * @throws IOException
	 */
  private static void loadTemplates() throws IOException {

  	if (createTableTemplate == null) {
	    createTableTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/sql/create-table.txt");
  	}
  	
  	if (alterTableTemplate == null) {
  		alterTableTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/sql/alter-table.txt");
  	}
  	
  	if (columnTemplate == null) {
	    columnTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/sql/column.txt");
  	}
  	
  	if (primaryKeyTemplate == null) {
	    primaryKeyTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/sql/primary-key.txt");
  	}
  	
  	if (foreignKeyTemplate == null) {
	    foreignKeyTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/sql/foreign-key.txt");
  	}

  	if (indexTemplate == null) {
  		indexTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/sql/index.txt");
  	}
  	
  	if (associateTableTemplate == null) {
	    associateTableTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/sql/associate-table.txt");
  	}

  	if (sequenceTemplate == null) {
  		sequenceTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/sql/sequence.txt");
  	}
  }
  
  /**
   * Creates SQL files supporting the new entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws IOException
   */
  public static void process(DefinitionWizardData definitionWizardData, IProgressMonitor progressMonitor) 
      throws CoreException, IOException {
    
    // Load templates
    loadTemplates();

    // Get the targeted project
    IProject project = definitionWizardData.getProject();
    
    // Get SQL folder
    IFolder folder = project.getFolder(new Path("sql"));

    // Initialize SQL 
    StringBuffer sql = new StringBuffer();
    
    // Get entity name
    String entityName = definitionWizardData.getEntityName();
    
    // Create table name
    String tableName = TextConverter.convertCommonToConstant(entityName);
    
    // Create ID column name
    String idColumnName = tableName + "_ID";
    
    // Initialize column definitions and associate code table definitions
    StringBuffer columnDefinitions = new StringBuffer();
    StringBuffer associateCodeTableDefinitions = new StringBuffer();
    
    // Build primary key column definition
    String columnDefinition = columnTemplate.replaceAll("COLUMN-NAME", idColumnName.toLowerCase());
    columnDefinition = columnDefinition.replaceAll("DATA-TYPE", "INTEGER");
    columnDefinition = columnDefinition.replaceAll("CONSTRAINT", "NOT NULL");
    columnDefinition = columnDefinition + ",";
    
    // Append primary key column definition
    columnDefinitions.append(columnDefinition).append(LINE_SEPARATOR);
    
    // Get entity attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    for (Attribute attribute : attributes) {
      
      // Initialize associate attribute indicator
      boolean isAssociateAttribute = false;
      
      if (attribute.getType().equals(AttributeTypes.LOOKUP)) {
        
        // Cast to lookup attribute
        LookupAttribute lookupAttribute = (LookupAttribute)attribute;
        
        // Set associate attribute indicator
        isAssociateAttribute = lookupAttribute.getMultipleValues();
      }
      
      if (isAssociateAttribute) {

        // Get attribute name
        String attributeName = TextConverter.convertCommonToConstant(attribute.getName());
        
        // Build associate table name
        String associateTableName = tableName + "_" + attributeName;
        
        // Build associative table
        String associateCodeTableDefinition = associateTableTemplate.replaceAll("ASSOCIATE-TABLE-NAME", associateTableName.toLowerCase());
        associateCodeTableDefinition = associateCodeTableDefinition.replaceAll("PARENT-TABLE-NAME", tableName.toLowerCase());
        associateCodeTableDefinition = associateCodeTableDefinition.replaceAll("PARENT-ID-COLUMN-NAME", idColumnName.toLowerCase());
        associateCodeTableDefinition = associateCodeTableDefinition.replaceAll("DEPENDENT-TABLE-NAME", "bws_code");
        associateCodeTableDefinition = associateCodeTableDefinition.replaceAll("DEPENDENT-ID-COLUMN-NAME", "code_id");
        
        // Append associate code table definition
        associateCodeTableDefinitions.append(associateCodeTableDefinition).append(LINE_SEPARATOR);
      }
      else {
        
      	// Set boolean indicator
      	boolean isBooleanType = attribute.getJavaType().equals(AttributeTypes.BOOLEAN);
      	
        // Get column attributes
        String columnName = TextConverter.convertCommonToConstant(attribute.getName());
        String dataType = getDataType(attribute);
        String constraint = attribute.getIsRequired() || isBooleanType ? "NOT NULL" : "NULL";
        
        // Build column definition
        columnDefinition = columnTemplate.replaceAll("COLUMN-NAME", columnName.toLowerCase());
        columnDefinition = columnDefinition.replaceAll("DATA-TYPE", dataType);
        columnDefinition = columnDefinition.replaceAll("CONSTRAINT", constraint);
        columnDefinition = columnDefinition + ",";
        
        // Append column definition
        columnDefinitions.append(columnDefinition).append(LINE_SEPARATOR);
      }
    }
    

    // Get parent entity name
    String parentEntityName = getParentEntityName(definitionWizardData);
    
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Get parent table name
      String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      String parentTableName = TextConverter.convertCommonToConstant(parentEntityCommonName);
      
      // Create parent ID column name
      String parentIdColumnName = parentTableName + "_ID";
      
      // Build version column definition
      columnDefinition = columnTemplate.replaceAll("COLUMN-NAME", parentIdColumnName.toLowerCase());
      columnDefinition = columnDefinition.replaceAll("DATA-TYPE", "INTEGER");
      columnDefinition = columnDefinition.replaceAll("CONSTRAINT", "NOT NULL");
      columnDefinition = columnDefinition + ",";
      
      // Append version column definition
      columnDefinitions.append(columnDefinition).append(LINE_SEPARATOR);
    }
    

    // Build deleted column definition
    columnDefinition = columnTemplate.replaceAll("COLUMN-NAME", "is_deleted");
    columnDefinition = columnDefinition.replaceAll("DATA-TYPE", "CHAR");
    columnDefinition = columnDefinition.replaceAll("CONSTRAINT", "NOT NULL");
    columnDefinition = columnDefinition + ",";
    
    // Append deleted column definition
    columnDefinitions.append(columnDefinition).append(LINE_SEPARATOR);


    // Build version column definition
    columnDefinition = columnTemplate.replaceAll("COLUMN-NAME", "version");
    columnDefinition = columnDefinition.replaceAll("DATA-TYPE", "INTEGER");
    columnDefinition = columnDefinition.replaceAll("CONSTRAINT", "NOT NULL");
    columnDefinition = columnDefinition + ",";
    
    // Append version column definition
    columnDefinitions.append(columnDefinition).append(LINE_SEPARATOR);

    
    // Build domain ID column definition
    columnDefinition = columnTemplate.replaceAll("COLUMN-NAME", "domain_id");
    columnDefinition = columnDefinition.replaceAll("DATA-TYPE", "INTEGER");
    columnDefinition = columnDefinition.replaceAll("CONSTRAINT", "NOT NULL");
    
    // Append domain ID column definition
    columnDefinitions.append(columnDefinition);

    
    // Build table definition
    String tableDefinition = createTableTemplate.replaceAll("TABLE-NAME", tableName.toLowerCase());
    tableDefinition = tableDefinition.replaceAll("COLUMN-DEFINITIONS", columnDefinitions.toString());

    // Append comment
    sql.append(LINE_SEPARATOR);
    sql.append("--").append(LINE_SEPARATOR);
    sql.append("-- ").append(entityName).append(" table.").append(LINE_SEPARATOR);
    sql.append("--").append(LINE_SEPARATOR);

    // Append table definition to SQL
    sql.append(tableDefinition).append(LINE_SEPARATOR);
    
    // Create PK constraint name
    String pkConstraintName = "PK_" + tableName;

    // Build primary key definition
    String primaryKeyDefinition = primaryKeyTemplate.replaceAll("TABLE-NAME", tableName.toLowerCase());
    primaryKeyDefinition = primaryKeyDefinition.replaceAll("CONSTRAINT-NAME", pkConstraintName.toLowerCase());
    primaryKeyDefinition = primaryKeyDefinition.replaceAll("COLUMN-NAMES", idColumnName.toLowerCase());
    
    // Append primary key definition to SQL
    sql.append(LINE_SEPARATOR).append(primaryKeyDefinition).append(LINE_SEPARATOR);
    
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Get parent table name
      String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      String parentTableName = TextConverter.convertCommonToConstant(parentEntityCommonName);
      
      // Create parent ID column name
      String parentIdColumnName = parentTableName + "_ID";
      
      // Create FK constraint name
      String fkConstraintName = "FK_" + tableName;

      // Build foreign key definition
      String foreignKeyDefinition = foreignKeyTemplate.replaceAll("TARGET-TABLE-NAME", tableName.toLowerCase());
      foreignKeyDefinition = foreignKeyDefinition.replaceAll("CONSTRAINT-NAME", fkConstraintName.toLowerCase());
      foreignKeyDefinition = foreignKeyDefinition.replaceAll("TARGET-COLUMN-NAMES", parentIdColumnName.toLowerCase());
      foreignKeyDefinition = foreignKeyDefinition.replaceAll("REFERENCED-TABLE-NAME", parentTableName.toLowerCase());
      foreignKeyDefinition = foreignKeyDefinition.replaceAll("REFERENCED-COLUMN-NAMES", parentIdColumnName.toLowerCase());
      
      // Append parent foreign key definition to SQL
      sql.append(LINE_SEPARATOR).append(foreignKeyDefinition).append(LINE_SEPARATOR);
    }
    
    for (Attribute attribute : attributes) {
      
      if (attribute.getType().equals(AttributeTypes.LOOKUP)) {
        
        // Cast to lookup attribute
        LookupAttribute lookupAttribute = (LookupAttribute)attribute;
          
        if (!lookupAttribute.getMultipleValues()) {
          
          // Get column name
          String columnName = TextConverter.convertCommonToConstant(attribute.getName());
          
          // Create FK constraint name
          String fkConstraintName = "FK_CODE_" + tableName + "_" + columnName;
          
          // Build foreign key definition
          String foreignKeyDefinition = foreignKeyTemplate.replaceAll("TARGET-TABLE-NAME", tableName.toLowerCase());
          foreignKeyDefinition = foreignKeyDefinition.replaceAll("CONSTRAINT-NAME", fkConstraintName.toLowerCase());
          foreignKeyDefinition = foreignKeyDefinition.replaceAll("TARGET-COLUMN-NAMES", columnName.toLowerCase());
          foreignKeyDefinition = foreignKeyDefinition.replaceAll("REFERENCED-TABLE-NAME", "bws_code");
          foreignKeyDefinition = foreignKeyDefinition.replaceAll("REFERENCED-COLUMN-NAMES", "code_id");

          // Append lookup foreign key definition to SQL
          sql.append(LINE_SEPARATOR).append(foreignKeyDefinition).append(LINE_SEPARATOR);
        }
      }
    }
    
    // Append associate code table definitions
    if (associateCodeTableDefinitions.length() > 0) {
      sql.append(associateCodeTableDefinitions.toString());
    }
    
    
    // Build domain index name
    String domainIndexName = "idx_" + tableName.toLowerCase() + "_1";
    
    // Build domain index definition
    String domainIndexDefinition = indexTemplate.replaceAll("INDEX-NAME", domainIndexName);
    domainIndexDefinition = domainIndexDefinition.replaceAll("TABLE-NAME", tableName.toLowerCase());
    domainIndexDefinition = domainIndexDefinition.replaceAll("COLUMN-NAME", "domain_id");
    
    // Append domain index definition to SQL
    sql.append(LINE_SEPARATOR).append(domainIndexDefinition).append(LINE_SEPARATOR);
    
    
    // Build sequence insert statement
    String sequenceInsertStatement = sequenceTemplate.replaceAll("TABLE-NAME", tableName.toLowerCase());
    sequenceInsertStatement = sequenceInsertStatement.replaceAll("COLUMN-NAME", idColumnName);
    
    // Append sequence insert statement to SQL
    sql.append(LINE_SEPARATOR).append(sequenceInsertStatement).append(LINE_SEPARATOR);
    
    
    // Convert SQL into input stream
    ByteArrayInputStream sqlInputStream = new ByteArrayInputStream(sql.toString().getBytes());
    
    // Get application entity SQL file and append SQL content
    IFile applicationEntitySqlFile = folder.getFile("app-entity.sql");
    applicationEntitySqlFile.appendContents(sqlInputStream, true, false, progressMonitor);
  }

  /**
   * Updates SQL files for a fragment relationship.
   * @param relationshipWizardData Definition wizard data.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws IOException
   */
  public static void process(RelationshipWizardData relationshipWizardData, IProgressMonitor progressMonitor) 
      throws CoreException, IOException {
    
    // Load templates
    loadTemplates();

    // Get the targeted project
    IProject project = relationshipWizardData.getProject();
    
    // Get SQL folder
    IFolder folder = project.getFolder(new Path("sql"));

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

    if (isManyToManyAssociation) {
    	
      // Add many to many relationship SQL
      addMultipleRelationshipSql(baseEntity1Name, baseEntity2Name, folder, progressMonitor);
    }
    else {
    	
      // Add many to one relationship SQL
      addSingleRelationshipSql(baseEntity1Name, baseEntity2Name, folder, progressMonitor);
    }
  }

  /**
   * Updates SQL files for a many to many fragment relationship.
   * @param primaryEntityName Primary entity name.
   * @param secondaryEntityName Secondary entity name.
   * @param project Project.
   * @param fieldIdsType Field IDs type.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws ProcessException
   * @throws IOException
   */
  private static void addMultipleRelationshipSql(String primaryEntityName, String secondaryEntityName, IFolder folder, IProgressMonitor progressMonitor) 
  		throws CoreException {

    // Convert to primary base names
    String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
    String primaryConstantName = TextConverter.convertCommonToConstant(primaryCommonName);

    // Convert to secondary base names
    String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
    String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);

    // Create table names
    String parentTableName = primaryConstantName.toLowerCase();
    String dependentTableName = secondaryConstantName.toLowerCase();
    String associateTableName = primaryConstantName + "_" + secondaryConstantName;
    
    // Create column names
    String parentIdColumnName = parentTableName + "_" + "id";
    String associateIdColumnName = dependentTableName + "_" + "id";
    
    // Initialize SQL 
    StringBuffer sql = new StringBuffer();
    
	  // Build column definition
	  String associateTableDefinition = associateTableTemplate.replaceAll("ASSOCIATE-TABLE-NAME", associateTableName.toLowerCase());
	  associateTableDefinition = associateTableDefinition.replaceAll("PARENT-TABLE-NAME", parentTableName);
	  associateTableDefinition = associateTableDefinition.replaceAll("PARENT-ID-COLUMN-NAME", parentIdColumnName.toLowerCase());
	  associateTableDefinition = associateTableDefinition.replaceAll("DEPENDENT-TABLE-NAME", dependentTableName);
	  associateTableDefinition = associateTableDefinition.replaceAll("DEPENDENT-ID-COLUMN-NAME", associateIdColumnName.toLowerCase());
        
	  // Append associate code table definition
	  sql.append(associateTableDefinition).append(LINE_SEPARATOR);
    
    // Convert SQL into input stream
    ByteArrayInputStream sqlInputStream = new ByteArrayInputStream(sql.toString().getBytes());
    
    // Get application entity SQL file and append SQL content
    IFile applicationEntitySqlFile = folder.getFile("app-entity.sql");
    applicationEntitySqlFile.appendContents(sqlInputStream, true, false, progressMonitor);
  }
  
  /**
   * Updates SQL files for a many to one fragment relationship.
   * @param primaryEntityName Primary entity name.
   * @param secondaryEntityName Secondary entity name.
   * @param project Project.
   * @param fieldIdsType Field IDs type.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws ProcessException
   * @throws IOException
   */
  private static void addSingleRelationshipSql(String primaryEntityName, String secondaryEntityName, IFolder folder, IProgressMonitor progressMonitor) 
  		throws CoreException {

    // Convert to primary base names
    String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
    String primaryConstantName = TextConverter.convertCommonToConstant(primaryCommonName);

    // Convert to secondary base names
    String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
    String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);

    // Create table names
    String parentTableName = primaryConstantName.toLowerCase();
    String dependentTableName = secondaryConstantName.toLowerCase();
    
    // Create column names
    String associateIdColumnName = dependentTableName + "_" + "id";
    
    // Initialize SQL 
    StringBuffer sql = new StringBuffer();
    
	  // Build column definition
	  String columnDefinition = columnTemplate.replaceAll("COLUMN-NAME", associateIdColumnName.toLowerCase());
	  columnDefinition = columnDefinition.replaceAll("DATA-TYPE", "INTEGER");
	  columnDefinition = columnDefinition.replaceAll("CONSTRAINT", "NULL");

	  // Build alter table definition
	  String alterTableDefinition = alterTableTemplate.replaceAll("TABLE-NAME", parentTableName.toLowerCase());
	  alterTableDefinition = alterTableDefinition.replaceAll("COLUMN-DEFINITIONS", columnDefinition);
        
	  // Append alter table definition
	  sql.append(alterTableDefinition).append(LINE_SEPARATOR);
    
    // Create FK constraint name
    String fkConstraintName = "fk_" + parentTableName + "_" + dependentTableName;

    // Build foreign key definition
    String foreignKeyDefinition = foreignKeyTemplate.replaceAll("TARGET-TABLE-NAME", parentTableName.toLowerCase());
    foreignKeyDefinition = foreignKeyDefinition.replaceAll("CONSTRAINT-NAME", fkConstraintName.toLowerCase());
    foreignKeyDefinition = foreignKeyDefinition.replaceAll("TARGET-COLUMN-NAMES", associateIdColumnName.toLowerCase());
    foreignKeyDefinition = foreignKeyDefinition.replaceAll("REFERENCED-TABLE-NAME", dependentTableName.toLowerCase());
    foreignKeyDefinition = foreignKeyDefinition.replaceAll("REFERENCED-COLUMN-NAMES", associateIdColumnName.toLowerCase());
    
    // Append foreign key definition to SQL
    sql.append(LINE_SEPARATOR).append(foreignKeyDefinition).append(LINE_SEPARATOR);
	  
	  // Convert SQL into input stream
    ByteArrayInputStream sqlInputStream = new ByteArrayInputStream(sql.toString().getBytes());
    
    // Get application entity SQL file and append SQL content
    IFile applicationEntitySqlFile = folder.getFile("app-entity.sql");
    applicationEntitySqlFile.appendContents(sqlInputStream, true, false, progressMonitor);
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
   * Returns a data type for a given attribute.
   * @param attribute Attribute.
   * @return String Data type.
   */
  private static String getDataType(Attribute attribute) {
  
    // Initialize return value
    String dataType = null;
    
    // Get attribute type
    String type = attribute.getType();
    
    if (type.equals(AttributeTypes.TEXT)) {
      
      // Cast to text attribute
      TextAttribute textAttribute = (TextAttribute)attribute;
      
      // Get max length
      Integer maxLength = textAttribute.getMaxLength();
      
      // Build data type
      dataType = "VARCHAR(" + maxLength + ")";
    }
    else if (type.equals(AttributeTypes.MEMO)) {
      
      // Cast to memo attribute
      MemoAttribute memoAttribute = (MemoAttribute)attribute;
      
      // Get max length
      Integer maxLength = memoAttribute.getMaxLength();
      
      // Build data type
      dataType = "VARCHAR(" + maxLength + ")";
    }
    else if (type.equals(AttributeTypes.EMAIL)) {
      
      // Cast to email attribute
      EmailAttribute emailAttribute = (EmailAttribute)attribute;
      
      // Get max length
      Integer maxLength = emailAttribute.getMaxLength();
      
      // Build data type
      dataType = "VARCHAR(" + maxLength + ")";
    }
    else if (type.equals(AttributeTypes.PHONE_NUMBER)) {
      
      // Cast to phone number attribute
      PhoneNumberAttribute phoneNumberAttribute = (PhoneNumberAttribute)attribute;
      
      // Get max length
      Integer maxLength = phoneNumberAttribute.getMaxLength();
      
      // Build data type
      dataType = "VARCHAR(" + maxLength + ")";
    }
    else if (type.equals(AttributeTypes.POSTAL_CODE)) {
      
      // Cast to postal code attribute
      PostalCodeAttribute postalCodeAttribute = (PostalCodeAttribute)attribute;
      
      // Get max length
      Integer maxLength = postalCodeAttribute.getMaxLength();
      
      // Build data type
      dataType = "VARCHAR(" + maxLength + ")";
    }
    else if (type.equals(AttributeTypes.DATE)) {
      dataType = "DATE";
    }
    else if (type.equals(AttributeTypes.TIME)) {
      dataType = "TIME";
    }
    else if (type.equals(AttributeTypes.BOOLEAN)) {
      dataType = "CHAR(1)";
    }
    else if (type.equals(AttributeTypes.LOOKUP)) {
      dataType = "INTEGER";
    }
    else if (type.equals(AttributeTypes.NUMERIC)) {

      // Cast to numeric attribute
      NumericAttribute numericAttribute = (NumericAttribute)attribute;
      
      // Get precision and scale
      Integer precision = numericAttribute.getPrecision();
      Integer scale = numericAttribute.getScale();
      
      if (scale > 0) {
      	
        // Build data type
        dataType = "NUMERIC(" + precision + ", " + scale + ")";
      }
      else {
      	
        // Build data type
        dataType = "INTEGER";
      }
    }
    
    return dataType;
  }
  
}
