package com.bws.jdistil.builder.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import com.bws.jdistil.builder.data.AssociationType;
import com.bws.jdistil.builder.data.Attribute;
import com.bws.jdistil.builder.data.Fragment;
import com.bws.jdistil.builder.data.Project;
import com.bws.jdistil.builder.data.Relationship;
import com.bws.jdistil.builder.generator.util.ResourceReader;
import com.bws.jdistil.builder.generator.util.TextConverter;

public class SecurityInfoGenerator {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static ResourceReader resourceReader = new ResourceReader();

	private static String insertTaskTemplate = null;
	private static String insertActionTemplate = null;
	private static String insertFieldTemplate = null;
	private static String insertFieldGroupTemplate = null;

	public SecurityInfoGenerator() {
		super();
	}

	private static void loadTemplates() throws GeneratorException {

		try {
			// Load templates
			if (insertTaskTemplate == null) {
				insertTaskTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/security/insert-task.txt");
			}

			if (insertActionTemplate == null) {
				insertActionTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/security/insert-action.txt");
			}

			if (insertFieldTemplate == null) {
				insertFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/security/insert-field.txt");
			}

			if (insertFieldGroupTemplate == null) {
				insertFieldGroupTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/security/insert-field-group.txt");
			}
		}
		catch (IOException ioException) {
	
			throw new GeneratorException("Error loading lookup info templates: " + ioException.getMessage());
		}
	}

	public void process(Project project, Path sqlPath, Map<String, String> fieldConstants, Map<String, String> actionConstants) throws GeneratorException {
	    
		// Load templates
		loadTemplates();

		// Initialize SQL
		StringBuffer sql = new StringBuffer();

		for (Fragment fragment : project.getFragments()) {
			
			// Get source relationships associated with this fragment 
			List<Relationship> sourceRelationships = project.getSourceFragmentRelationships(fragment.getName());
			
			// Create fragment specific SQL
			createSecuritySql(fragment, sourceRelationships, fieldConstants, actionConstants, sql);
		}
			
		try {
			// Create app-security SQL file
			Path appSecurityFilePath = sqlPath.resolve("app-security.sql");
			Files.writeString(appSecurityFilePath, sql.toString(), StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating security info SQL files: " + ioException.getMessage());
		}
	}
	
	public void createSecuritySql(Fragment fragment, List<Relationship> sourceRelationships, 
			Map<String, String> fieldConstants, Map<String, String> actionConstants, StringBuffer sql) throws GeneratorException {
	
		// Get entity name
		String entityName = fragment.getName();
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
		String viewActionSecureId = actionConstants.get(viewActionId);
		String addActionSecureId = actionConstants.get(addActionId);
		String editActionSecureId = actionConstants.get(editActionId);
		String deleteActionSecureId = actionConstants.get(deleteActionId);
		String saveActionSecureId = actionConstants.get(saveActionId);
		String cancelActionSecureId = actionConstants.get(cancelActionId);

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

		if (fragment.getIsPaginationSupported()) {

			// Create paging action names
			String previousPageActionId = "VIEW_" + constantEntityName + "_PREVIOUS_PAGE";
			String selectPageActionId = "VIEW_" + constantEntityName + "_SELECT_PAGE";
			String nextPageActionId = "VIEW_" + constantEntityName + "_NEXT_PAGE";

			// Lookup secure ID's using paging action names
			String previousPageActionSecureId = actionConstants.get(previousPageActionId);
			String selectPageActionSecureId = actionConstants.get(selectPageActionId);
			String nextPageActionSecureId = actionConstants.get(nextPageActionId);

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
		List<Attribute> attributes = fragment.getAttributes();

		for (Attribute attribute : attributes) {

			// Use attribute name as displayed field name
			String fieldName = attribute.getName();

			// Create field ID
			String fieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(attribute.getName());

			// Lookup secure ID using field ID
			String fieldSecureId = fieldConstants.get(fieldId);

			// Create insert statement for field
			String insertField = insertFieldTemplate.replaceAll("FIELD-NAME", fieldName);
			insertField = insertField.replaceAll("SECURE-ID", fieldSecureId);
			insertField = insertField.replaceAll("FIELD-GROUP", fieldGroupName);

			// Append field SQL statement
			sql.append(insertField).append(LINE_SEPARATOR);
		}
		
		if (sourceRelationships != null) {
			
			for (Relationship relationship : sourceRelationships) {
				
				createSecuritySql(relationship, fieldConstants, sql);
			}
		}
	}

	private void createSecuritySql(Relationship relationship, Map<String, String> fieldConstants, StringBuffer sql) {

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

		// Add relationship field for entity 1
		addRelationshipField(baseEntity1Name, baseEntity2Name, fieldConstants, isManyToManyAssociation, sql);

		if (relationship.getIsBidirectional()) {

			// Add relationship field for entity 2
			addRelationshipField(baseEntity2Name, baseEntity1Name, fieldConstants, isManyToManyAssociation, sql);
		}
	}

	private void addRelationshipField(String primaryEntityName, String secondaryEntityName, 
			Map<String, String> fieldConstants, boolean isManyToManyAssociation, StringBuffer sql) {

		// Convert to primary base names
		String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
		String primaryConstantName = TextConverter.convertCommonToConstant(primaryCommonName);

		// Convert to secondary base names
		String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
		String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);

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
		String fieldSecureId = fieldConstants.get(fieldId);

		// Format for SQL
		fieldSecureId = fieldSecureId.replaceAll("\"", "'");

		// Create insert statement for field
		String insertField = insertFieldTemplate.replaceAll("FIELD-NAME", fieldName);
		insertField = insertField.replaceAll("SECURE-ID", fieldSecureId);
		insertField = insertField.replaceAll("FIELD-GROUP", primaryCommonName);

		// Append field SQL statement
		sql.append(insertField).append(LINE_SEPARATOR);
	}
}
