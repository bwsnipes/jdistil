package com.bws.jdistil.builder.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.bws.jdistil.builder.data.AssociationType;
import com.bws.jdistil.builder.data.Attribute;
import com.bws.jdistil.builder.data.AttributeType;
import com.bws.jdistil.builder.data.Fragment;
import com.bws.jdistil.builder.data.Project;
import com.bws.jdistil.builder.data.Relationship;
import com.bws.jdistil.builder.generator.util.ResourceReader;
import com.bws.jdistil.builder.generator.util.TextConverter;

public class SqlGenerator {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static ResourceReader resourceReader = new ResourceReader();

	private static String createTableTemplate = null;
	private static String alterTableTemplate = null;
	private static String columnTemplate = null;
	private static String primaryKeyTemplate = null;
	private static String foreignKeyTemplate = null;
	private static String indexTemplate = null;
	private static String associateTableTemplate = null;
	private static String sequenceTemplate = null;

	public SqlGenerator() {
		super();
	}

	private void loadTemplates() throws GeneratorException {

		try {
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
		catch (IOException ioException) {
	
			throw new GeneratorException("Error loading SQL templates: " + ioException.getMessage());
		}
	}

	public void process(Project project, Path sqlPath) throws GeneratorException {
	    
		// Load templates
		loadTemplates();

		// Initialize SQL
		StringBuffer sql = new StringBuffer();

		for (Fragment fragment : project.getFragments()) {
			
			// Get source relationships associated with this fragment 
			List<Relationship> sourceRelationships = project.getSourceFragmentRelationships(fragment.getName());
			
			// Create fragment specific SQL
			createSql(fragment, sourceRelationships, sql);
		}
			
		try {
			// Create app-entity SQL file
			Path appEntityFilePath = sqlPath.resolve("app-entity.sql");
			Files.writeString(appEntityFilePath, sql.toString(), StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating SQL files: " + ioException.getMessage());
		}
	}
	
	private void createSql(Fragment fragment, List<Relationship> sourceRelationships, StringBuffer sql) {
		
		// Get entity name
		String entityName = fragment.getName();

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
		List<Attribute> attributes = fragment.getAttributes();

		for (Attribute attribute : attributes) {

			// Initialize associate attribute indicator
			boolean isAssociateAttribute = false;

			if (attribute.getType().equals(AttributeType.LOOKUP)) {

				// Set associate attribute indicator
				isAssociateAttribute = attribute.getIsLookupMultipleValues();
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
				boolean isBooleanType = attribute.getType().equals(AttributeType.BOOLEAN);

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
		String parentEntityName = fragment.getParentName();

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

			if (attribute.getType().equals(AttributeType.LOOKUP)) {

				if (!attribute.getIsLookupMultipleValues()) {

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

		if (sourceRelationships != null) {
			
			for (Relationship relationship : sourceRelationships) {
				
				// Create SQL based on source relationship
				createSql(relationship, sql);
			}
		}
	}

	private void createSql(Relationship relationship, StringBuffer sql) {

		// Get entity name
		String entity1Name = relationship.getSourceFragmentName();

		// Get base entity name
		String baseEntity1Name = TextConverter.getBaseClassName(entity1Name);

		// Get entity name
		String entity2Name = relationship.getTargetFragmentName();

		// Get base entity name
		String baseEntity2Name = TextConverter.getBaseClassName(entity2Name);

		// Set many to many association indicator
		boolean isManyToManyAssociation = relationship.getAssociation().equals(AssociationType.MANY_TO_MANY);

		if (isManyToManyAssociation) {

			// Add many to many relationship SQL
			addMultipleRelationshipSql(baseEntity1Name, baseEntity2Name, sql);
		} 
		else {

			// Add many to one relationship SQL
			addSingleRelationshipSql(baseEntity1Name, baseEntity2Name, sql);
		}
	}
	
	private void addMultipleRelationshipSql(String primaryEntityName, String secondaryEntityName, StringBuffer sql) {

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

		// Build column definition
		String associateTableDefinition = associateTableTemplate.replaceAll("ASSOCIATE-TABLE-NAME", associateTableName.toLowerCase());
		associateTableDefinition = associateTableDefinition.replaceAll("PARENT-TABLE-NAME", parentTableName);
		associateTableDefinition = associateTableDefinition.replaceAll("PARENT-ID-COLUMN-NAME", parentIdColumnName.toLowerCase());
		associateTableDefinition = associateTableDefinition.replaceAll("DEPENDENT-TABLE-NAME", dependentTableName);
		associateTableDefinition = associateTableDefinition.replaceAll("DEPENDENT-ID-COLUMN-NAME", associateIdColumnName.toLowerCase());

		// Append associate code table definition
		sql.append(associateTableDefinition).append(LINE_SEPARATOR);
	}

	private void addSingleRelationshipSql(String primaryEntityName, String secondaryEntityName, StringBuffer sql) {

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
	}

	private static String getDataType(Attribute attribute) {

		// Initialize return value
		String dataType = null;

		// Get attribute type
		AttributeType type = attribute.getType();

		if (type.equals(AttributeType.TEXT) ||
			type.equals(AttributeType.MEMO) ||
			type.equals(AttributeType.EMAIL) ||
			type.equals(AttributeType.PHONE) ||
			type.equals(AttributeType.POSTAL_CODE)) {

			String maxLength = String.valueOf(attribute.getTextMaxLength());

			// Build data type
			dataType = "VARCHAR(" + maxLength + ")";
		} 
		else if (type.equals(AttributeType.DATE)) {
			dataType = "DATE";
		} 
		else if (type.equals(AttributeType.TIME)) {
			dataType = "TIME";
		} 
		else if (type.equals(AttributeType.BOOLEAN)) {
			dataType = "CHAR(1)";
		} 
		else if (type.equals(AttributeType.LOOKUP)) {
			dataType = "INTEGER";
		} 
		else if (type.equals(AttributeType.NUMERIC)) {

			// Get precision and scale
			Integer precision = attribute.getNumericPrecision();
			Integer scale = attribute.getNumericScale();

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
