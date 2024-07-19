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

public class DataSourceGenerator {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static ResourceReader resourceReader = new ResourceReader();

	private static String dataObjectTemplate = null;
	private static String attributeStatementTemplate = null;
	private static String propertyStatementTemplate = null;
	private static String propertyGetterStatementTemplate = null;
	private static String propertySetterStatementTemplate = null;
	private static String listAttributeStatementTemplate = null;
	private static String listPropertyStatementTemplate = null;
	private static String listPropertyGetterStatementTemplate = null;
	private static String listPropertySetterStatementTemplate = null;
	private static String dataManagerTemplate = null;
	private static String columnBindingStatementTemplate = null;
	private static String columnBindingReferenceStatementTemplate = null;
	private static String associateBindingsTemplate = null;
	private static String associateBindingStatementTemplate = null;
	private static String columnBindingVariableTemplate = null;

	public DataSourceGenerator() {
		super();
	}

	private void loadTemplates() throws GeneratorException {

		try {
			// Load templates
			if (dataObjectTemplate == null) {
				dataObjectTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/data-object.txt");
			}

			if (attributeStatementTemplate == null) {
				attributeStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/attribute-statement.txt");
			}

			if (propertyStatementTemplate == null) {
				propertyStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/property-statement.txt");
			}

			if (propertyGetterStatementTemplate == null) {
				propertyGetterStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/property-getter-statement.txt");
			}

			if (propertySetterStatementTemplate == null) {
				propertySetterStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/property-setter-statement.txt");
			}

			if (listAttributeStatementTemplate == null) {
				listAttributeStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/list-attribute-statement.txt");
			}

			if (listPropertyStatementTemplate == null) {
				listPropertyStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/list-property-statement.txt");
			}

			if (listPropertyGetterStatementTemplate == null) {
				listPropertyGetterStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/list-property-getter-statement.txt");
			}

			if (listPropertySetterStatementTemplate == null) {
				listPropertySetterStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/list-property-setter-statement.txt");
			}

			if (dataManagerTemplate == null) {
				dataManagerTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/data-manager.txt");
			}

			if (columnBindingStatementTemplate == null) {
				columnBindingStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/column-binding-statement.txt");
			}

			if (columnBindingReferenceStatementTemplate == null) {
				columnBindingReferenceStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/column-binding-reference-statement.txt");
			}

			if (associateBindingsTemplate == null) {
				associateBindingsTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/associate-bindings.txt");
			}

			if (associateBindingStatementTemplate == null) {
				associateBindingStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/associate-binding-statement.txt");
			}

			if (columnBindingVariableTemplate == null) {
				columnBindingVariableTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/datasource/column-binding-variable.txt");
			}
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error loading data source templates: " + ioException.getMessage());
		}
	}

	public void process(Project project, String basePackageName, Path basePackagePath) throws GeneratorException {

		// Load templates
		loadTemplates();

		for (Fragment fragment : project.getFragments()) {
			
			try {
				// Create fragment package name
				String subPackageName = TextConverter.convertCommonToCamel(fragment.getName(), true);
				String fragmentPackageName = basePackageName + "." + subPackageName;
				
				// Create fragment package path
				Path fragmentPackagePath = basePackagePath.resolve(subPackageName);
				
				// Create fragment package directories
				Files.createDirectories(fragmentPackagePath);

				// Get all relationships associated with this fragment 
				List<Relationship> sourceRelationships = project.getSourceFragmentRelationships(fragment.getName());
				List<Relationship> targetRelationships = project.getTargetFragmentRelationships(fragment.getName());
				
				// Create data object class
				createDataObject(fragment, sourceRelationships, targetRelationships, basePackageName, fragmentPackageName, fragmentPackagePath);

				// Create data manage class
				createDataManager(fragment, sourceRelationships, targetRelationships, basePackageName, fragmentPackageName, fragmentPackagePath);
			}
			catch (IOException ioException) {

				throw new GeneratorException("Error creating data source class files: " + ioException.getMessage());
			}
		}
	}

	private void createDataObject(Fragment fragment, List<Relationship> sourceRelationships, 
			List<Relationship> targetRelationships, String basePackageName, String fragmentPackageName, Path fragmentPackagePath) 
			throws GeneratorException {

		// Initialize attribute statements and property statements
		StringBuffer attributeStatements = new StringBuffer();
		StringBuffer propertyStatements = new StringBuffer();

		// Initialize date and list required indicators
		boolean dateTypeRequired = false;
		boolean listTypeRequired = false;

		// Get attributes
		List<Attribute> attributes = fragment.getAttributes();

		if (!attributes.isEmpty()) {

			for (Attribute attribute : attributes) {

				// Add line separator for correct spacing
				attributeStatements.append("LINE_SEPARATOR");
				propertyStatements.append("LINE_SEPARATOR");

				// Initialize attribute and property statements
				String attributeStatement = attributeStatementTemplate;
				String propertyStatement = propertyStatementTemplate;

				// Get attribute properties
				String name = attribute.getName();
				AttributeType type = attribute.getType();
				String javaType = determineJavaType(attribute);

				if (type.equals(AttributeType.DATE) || type.equals(AttributeType.TIME)) {

					// Set date required indicator
					dateTypeRequired = true;
				}
				else if (type.equals(AttributeType.LOOKUP)) {

					if (attribute.getIsLookupMultipleValues()) {

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

		// Get parent name
		String parentName = fragment.getParentName();

		if (parentName != null && parentName.trim().length() > 0) {

			// Create statement variables
			String attributeName = TextConverter.convertCommonToCamel(parentName, true) + "Id";
			String attributeDescription = TextConverter.convertAsSentence(parentName, true) + " ID";
			String propertyName = TextConverter.convertCommonToCamel(parentName, false) + "Id";
			String propertyDescription = TextConverter.convertAsSentence(parentName, false) + " ID";

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

		// Initialize imports
		StringBuffer importStatements = new StringBuffer("");

		// Add conditional imports
		if (dateTypeRequired) {
			importStatements.append("import java.util.Date;").append(LINE_SEPARATOR);
		}
		if (listTypeRequired) {
			importStatements.append("import java.util.Collections;").append(LINE_SEPARATOR);
			importStatements.append("import java.util.ArrayList;").append(LINE_SEPARATOR);
			importStatements.append("import java.util.List;").append(LINE_SEPARATOR);
		}

		if (sourceRelationships != null) {
			
			for (Relationship relationship : sourceRelationships) {
				
				// Update data object elements based on source relationship
				updateDataObject(relationship, basePackageName, importStatements, attributeStatements, propertyStatements, true);
			}
		}
		
		if (targetRelationships != null) {
			
			for (Relationship relationship : targetRelationships) {
				
				if (relationship.getIsBidirectional()) {
					
					// Update data object elements based on target relationship
					updateDataObject(relationship, basePackageName, importStatements, attributeStatements, propertyStatements, false);
				}
			}
		}
		
		// Get fragment name
		String fragmentName = fragment.getName();
		
		// Create sub-package and package name
		String subPackageName = TextConverter.convertCommonToCamel(fragmentName, true);
		String packageName = basePackageName + "." + subPackageName;
		
		// Create data object class name and description
		String dataObjectClassName = TextConverter.convertCommonToCamel(fragmentName, false);
		String dataObjectDescription = TextConverter.convertAsSentence(fragmentName, false);

		// Set data object variables
		String dataObjectContent = dataObjectTemplate;
		dataObjectContent = dataObjectContent.replaceAll("PACKAGE-NAME", packageName);
		dataObjectContent = dataObjectContent.replaceAll("DATA-OBJECT-NAME", dataObjectDescription);
		dataObjectContent = dataObjectContent.replaceAll("DATA-OBJECT-CLASS-NAME", dataObjectClassName);
		dataObjectContent = dataObjectContent.replaceAll("IMPORTS", importStatements.toString());
		dataObjectContent = dataObjectContent.replaceAll("ATTRIBUTES", attributeStatements.toString());
		dataObjectContent = dataObjectContent.replaceAll("PROPERTIES", propertyStatements.toString());

		try {
			// Create data object file name
			String dataObjectFileName = dataObjectClassName + ".java";

			// Create data object file
			Path dataObjectPath = fragmentPackagePath.resolve(dataObjectFileName);
			Files.writeString(dataObjectPath, dataObjectContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating data object class file: " + ioException.getMessage());
		}
	}

	private void updateDataObject(Relationship relationship, String basePackageName, StringBuffer importStatements, 
			StringBuffer attributeStatements, StringBuffer propertyStatements, boolean isSourceRelationship) {

		// Get source data object name
		String sourceDataObjectName = relationship.getSourceFragmentName();

		// Get source sub-package and package names
		String sourceSubPackageName = TextConverter.convertCommonToCamel(sourceDataObjectName, true);
		String sourcePackageName = basePackageName + "." + sourceSubPackageName;

		// Get target data object name
		String targetDataObjectName = relationship.getTargetFragmentName();

		// Get target sub-package and package names
		String targetSubPackageName = TextConverter.convertCommonToCamel(targetDataObjectName, true);
		String targetPackageName = basePackageName + "." + targetSubPackageName;

		// Set many-to-many indicator
		boolean isManyToManyAssociation = relationship.getAssociation().equals(AssociationType.MANY_TO_MANY);

		if (isSourceRelationship) {

			// Update data object for source relationship
			updateDataObject(sourcePackageName, sourceDataObjectName, targetDataObjectName, isManyToManyAssociation, 
					importStatements, attributeStatements, propertyStatements);
		}
		else {
			
			// Update data object for bi-directional target relationship
			updateDataObject(targetPackageName, targetDataObjectName, sourceDataObjectName, isManyToManyAssociation, 
					importStatements, attributeStatements, propertyStatements);
		}
	}

	private void updateDataObject(String primaryPackageName, String primaryDataObjectName, String secondaryDataObjectName, 
			boolean isManyToManyAssociation, StringBuffer importStatements, StringBuffer attributeStatements, StringBuffer propertyStatements) {

		// Convert to secondary base names
		String secondaryLowerCaseCamel = TextConverter.convertCommonToCamel(secondaryDataObjectName, true);
		String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryDataObjectName, false);

		// Get property name
		String property1Name = secondaryUpperCaseCamel + "Id";

		// Get attribute name
		String attribute1Name = secondaryLowerCaseCamel + "Id";

		// Get attribute description
		String attribute1Description = secondaryDataObjectName.toLowerCase() + " ID";

		// Initialize statements
		String attributeStatement = attributeStatementTemplate;
		String propertyGetterStatement = propertyGetterStatementTemplate;
		String propertySetterStatement = propertySetterStatementTemplate;

		if (isManyToManyAssociation) {

			if (importStatements.indexOf("java.util.Collections") == -1) {
				
				// Add collection classes to import statements if not already present
				importStatements.append("import java.util.Collections;").append(LINE_SEPARATOR);
				importStatements.append("import java.util.ArrayList;").append(LINE_SEPARATOR);
				importStatements.append("import java.util.List;").append(LINE_SEPARATOR);
			}

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

		// Add attribute statement
		attributeStatements.append(attributeStatement).append(LINE_SEPARATOR);

		// Create property getter statement
		propertyGetterStatement = propertyGetterStatement.replaceAll("ATTRIBUTE-DESCRIPTION", attribute1Description);
		propertyGetterStatement = propertyGetterStatement.replaceAll("ATTRIBUTE-TYPE", "Integer");
		propertyGetterStatement = propertyGetterStatement.replaceAll("ATTRIBUTE-NAME", attribute1Name);
		propertyGetterStatement = propertyGetterStatement.replaceAll("PROPERTY-NAME", property1Name);

		// Add property getter statement
		propertyStatements.append(propertyGetterStatement).append(LINE_SEPARATOR);
		
		// Create property setter statement
		propertySetterStatement = propertySetterStatement.replaceAll("ATTRIBUTE-DESCRIPTION", attribute1Description);
		propertySetterStatement = propertySetterStatement.replaceAll("ATTRIBUTE-TYPE", "Integer");
		propertySetterStatement = propertySetterStatement.replaceAll("ATTRIBUTE-NAME", attribute1Name);
		propertySetterStatement = propertySetterStatement.replaceAll("PROPERTY-NAME", property1Name);

		// Add property setter statement
		propertyStatements.append(propertySetterStatement).append(LINE_SEPARATOR);
	}

	private void createDataManager(Fragment fragment, List<Relationship> sourceRelationships, List<Relationship> targetRelationships, 
			String basePackageName, String fragmentPackageName, Path fragmentPackagePath) throws GeneratorException {
		
		// Initialize column binding and associate binding statements
		StringBuffer columnBindingStatements = new StringBuffer();
		StringBuffer associateBindingStatements = new StringBuffer();

		// Get fragment name
		String fragmentName = fragment.getName();

		// Get package name and entity name
		String dataManagerSubPackageName = TextConverter.convertCommonToCamel(fragmentName, true);
		String dataManagerPackageName = basePackageName + "." + dataManagerSubPackageName;

		// Create template variables
		String dataObjectClassName = TextConverter.convertCommonToCamel(fragmentName, false);
		String dataManagerName = TextConverter.convertAsSentence(fragmentName, true);
		String dataManagerClassName = dataObjectClassName + "Manager";
		String tableName = TextConverter.convertCommonToConstant(fragmentName);
		String idColumnName = tableName + "_ID";

		// Get attributes
		List<Attribute> attributes = fragment.getAttributes();

		if (!attributes.isEmpty()) {

			for (Attribute attribute : attributes) {

				// Set lookup attribute indicator
				boolean isLookupAttribute = attribute.getType().equals(AttributeType.LOOKUP);

				// Initialize associate attribute indicator
				boolean isAssociateAttribute = false;

				if (isLookupAttribute) {

					// Set associate attribute indicator
					isAssociateAttribute = attribute.getIsLookupMultipleValues();
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
		
		// Get parent name
		String parentName = fragment.getParentName();
		
		if (parentName != null && parentName.trim().length() > 0) {

			// Create column name
			String parentConstantName = TextConverter.convertCommonToConstant(parentName);
			String columnName = parentConstantName + "_ID";
			
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

		if (sourceRelationships != null) {
			
			for (Relationship relationship : sourceRelationships) {
				
				// Update data manager based on source relationship
				updateDataManager(relationship, basePackageName, associateBindingStatements, true);
			}
		}
		
		if (targetRelationships != null) {
			
			for (Relationship relationship : targetRelationships) {
				
				if (relationship.getIsBidirectional()) {
					
					// Update data manager based on target relationship
					updateDataManager(relationship, basePackageName, associateBindingStatements, false);
				}
			}
		}
		
		// Initialize associate bindings data
		String associateBindings = "";

		if (associateBindingStatements.length() > 0) {

			// Set associate bindings
			associateBindings = associateBindingsTemplate.replaceAll("ASSOCIATE-BINDING-STATEMENTS", associateBindingStatements.toString());
		}

		// Set data manager variables
		String dataManager = dataManagerTemplate;
		dataManager = dataManager.replaceAll("PACKAGE-NAME", dataManagerPackageName);
		dataManager = dataManager.replaceAll("ENTITY-NAME", fragmentName.toLowerCase());
		dataManager = dataManager.replaceAll("ENTITY-CLASS-NAME", dataObjectClassName);
		dataManager = dataManager.replaceAll("MANAGER-NAME", dataManagerName);
		dataManager = dataManager.replaceAll("MANAGER-CLASS-NAME", dataManagerClassName);
		dataManager = dataManager.replaceAll("TABLE-NAME", tableName.toLowerCase());
		dataManager = dataManager.replaceAll("ID-COLUMN-NAME", idColumnName.toLowerCase());
		dataManager = dataManager.replaceAll("COLUMN-BINDING-STATEMENTS", columnBindingStatements.toString());
		dataManager = dataManager.replaceAll("ASSOCIATE-BINDING-STATEMENTS", associateBindings);

		try {
			// Create data manager file name
			String dataManagerFileName = dataManagerClassName + ".java";

			// Create data object file
			Path dataManagerPath = fragmentPackagePath.resolve(dataManagerFileName);
			Files.writeString(dataManagerPath, dataManager, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating data manager class file: " + ioException.getMessage());
		}
	}

	private void updateDataManager(Relationship relationship, String basePackageName, 
			StringBuffer associateBindingStatements, boolean isSourceRelationship) {

		// Get source data object name
		String sourceDataObjectName = relationship.getSourceFragmentName();

		// Get source sub-package and package names
		String sourceSubPackageName = TextConverter.convertCommonToCamel(sourceDataObjectName, true);
		String sourcePackageName = basePackageName + "." + sourceSubPackageName;

		// Get source attribute name
		String sourceAttributeName = relationship.getSourceAttributeName();

		// Get target data object name
		String targetDataObjectName = relationship.getTargetFragmentName();

		// Get target sub-package and package names
		String targetSubPackageName = TextConverter.convertCommonToCamel(targetDataObjectName, true);
		String targetPackageName = basePackageName + "." + targetSubPackageName;

		// Get target attribute name
		String targetAttributeName = relationship.getTargetAttributeName();

		// Get many-to-many association indicator
		boolean isManyToManyAssociation = relationship.getAssociation().equals(AssociationType.MANY_TO_MANY);
		
		if (isSourceRelationship) {
			
			if (isManyToManyAssociation) {

				// Update source data manager
				updateDataManager(sourcePackageName, sourceDataObjectName, targetDataObjectName, targetAttributeName, true, associateBindingStatements);
			} 
			else {

				// Update target data manager
				updateDataManager(targetDataObjectName, targetAttributeName, associateBindingStatements);
			}
		}
		else {
			
			// Update target data manager
			updateDataManager(targetPackageName, targetDataObjectName, sourceDataObjectName, sourceAttributeName, false, associateBindingStatements);	
		}
	}

	private void updateDataManager(String primaryPackageName, String primaryDataObjectName,	
			String secondaryDataObjectName, String secondaryAttributeName, 
			boolean isPrimary, StringBuffer associateBindingStatements) {

		// Convert to primary base names
		String primaryConstant = TextConverter.convertCommonToConstant(primaryDataObjectName);

		// Convert to secondary base names
		String secondaryConstant = TextConverter.convertCommonToConstant(secondaryDataObjectName);
		String secondaryLowerCaseCamel = TextConverter.convertCommonToCamel(secondaryDataObjectName, true);
		String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryDataObjectName, false);
		String secondartyDescription = TextConverter.convertAsSentence(secondaryDataObjectName, false);

		// Create template variables
		String idVariableName = secondaryLowerCaseCamel + "IdColumnBinding";
		String idColumnName = secondaryLowerCaseCamel + "_id";
		String propertyName = secondaryUpperCaseCamel + "Ids";
		String referenceColumnName = TextConverter.convertCommonToConstant(secondaryAttributeName);

		// Initialize table name
		String tableName = null;

		// Build table name
		if (isPrimary) {
			tableName = primaryConstant + "_" + secondaryConstant;
		}
		else {
			tableName = secondaryConstant + "_" + primaryConstant;
		}

		// Append line separators to bindings statements
		associateBindingStatements.append(LINE_SEPARATOR).append(LINE_SEPARATOR);

		// Create ID column variable
		String columnBindingVariable = columnBindingVariableTemplate.replaceAll("ID-COLUMN-DESC", secondartyDescription);
		columnBindingVariable = columnBindingVariable.replaceAll("ID-VARIABLE-NAME", idVariableName);
		columnBindingVariable = columnBindingVariable.replaceAll("ID-COLUMN-NAME", idColumnName);

		// Update binding statements
		associateBindingStatements.append(columnBindingVariable).append(LINE_SEPARATOR);

		// Create column binding statement
		String associateBindingStatement = associateBindingStatementTemplate.replaceAll("PROPERTY-NAME", propertyName);
		associateBindingStatement = associateBindingStatement.replaceAll("ASSOCIATE-TABLE-NAME", tableName.toLowerCase());
		associateBindingStatement = associateBindingStatement.replaceAll("ASSOCIATE-COLUMN-BINDING-NAME", idVariableName);
		associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-TABLE-NAME", secondaryConstant.toLowerCase());
		associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-ID-COLUMN-NAME", idColumnName);
		associateBindingStatement = associateBindingStatement.replaceAll("REFERENCE-COLUMN-NAME", referenceColumnName.toLowerCase());

		// Update binding statements
		associateBindingStatements.append(associateBindingStatement).append(LINE_SEPARATOR);
	}

	private void updateDataManager(String secondaryDataObjectName, String targetAttributeName, StringBuffer associateBindingStatements) {

		// Convert to secondary base names
		String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryDataObjectName);
		String secondaryLowerCaseCamel = TextConverter.convertCommonToCamel(secondaryDataObjectName, true);
		String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryDataObjectName, false);

		// Create template variables
		String idColumnName = secondaryLowerCaseCamel + "_id";
		String propertyName = secondaryUpperCaseCamel + "Id";
		String referenceColumnName = TextConverter.convertCommonToConstant(targetAttributeName);

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
	}

	private String determineJavaType(Attribute attribute) {

		// Initialize return value
		String javaType = "String";

		AttributeType attributeType = attribute.getType();
		
		if (attributeType.equals(AttributeType.BOOLEAN)) {
			
			javaType = "Boolean";
		} 
		else if (attributeType.equals(AttributeType.DATE)) {
			
			javaType = "Date";
		}
		else if (attributeType.equals(AttributeType.TIME)) {
			
			javaType = "Date";
		}
		else if (attributeType.equals(AttributeType.LOOKUP)) {
			
			javaType = "Integer";
		}
		else if (attributeType.equals(AttributeType.NUMERIC)) {

			// Get scale
			Integer scale = attribute.getNumericScale();

			// Set type of numeric converter based on scale
			if (scale == null || scale.intValue() == 0) {

				javaType = "Integer";
			} 
			else {

				javaType = "Double";
			}
		}

		return javaType;
	}

	private String getColumnType(Attribute attribute) {

		// Initialize return value
		String columnType = "STRING";

		// Get attribute type
		AttributeType type = attribute.getType();

		if (type.equals(AttributeType.DATE)) {
			
			columnType = "DATE";
		} 
		else if (type.equals(AttributeType.TIME)) {
			
			columnType = "TIME";
		} 
		else if (type.equals(AttributeType.BOOLEAN)) {
			
			columnType = "BOOLEAN";
		} 
		else if (type.equals(AttributeType.LOOKUP)) {
			
			columnType = "INTEGER";
		} 
		else if (type.equals(AttributeType.NUMERIC)) {

			// Get scale
			Integer scale = attribute.getNumericScale();

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
