package com.bws.jdistil.builder.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.bws.jdistil.builder.data.Attribute;
import com.bws.jdistil.builder.data.AttributeType;
import com.bws.jdistil.builder.data.Fragment;
import com.bws.jdistil.builder.data.Project;
import com.bws.jdistil.builder.generator.util.ResourceReader;
import com.bws.jdistil.builder.generator.util.TextConverter;

public class ProcessGenerator {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static String viewEntitiesTemplate = null;
	private static String viewEntitiesPagingTemplate = null;
	private static String selectEntitiesTemplate = null;
	private static String selectEntitiesPagingTemplate = null;
	private static String filterCriteriaDefinitionTemplate = null;
	private static String filterCriteriaDefinitionPagingTemplate = null;
	private static String filterCriteriaStatementTemplate = null;
	private static String saveEntityTemplate = null;
	private static String editEntityTemplate = null;
	private static String deleteEntityTemplate = null;
	
	private ResourceReader resourceReader = new ResourceReader();

	public ProcessGenerator() {
		super();
	}

	private void loadTemplates() throws GeneratorException {
		
		try {
			if (viewEntitiesTemplate == null) {
				viewEntitiesTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/view-entities.txt");
			}
			
			if (viewEntitiesPagingTemplate == null) {
				viewEntitiesPagingTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/view-entities-paging.txt");
			}
			
			if (selectEntitiesTemplate == null) {
				selectEntitiesTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/select-entities.txt");
			}
			
			if (selectEntitiesPagingTemplate == null) {
				selectEntitiesPagingTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/select-entities-paging.txt");
			}
			
			if (filterCriteriaDefinitionTemplate == null) {
				filterCriteriaDefinitionTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/filter-criteria-definition.txt");
			}
			
			if (filterCriteriaDefinitionPagingTemplate == null) {
				filterCriteriaDefinitionPagingTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/filter-criteria-definition-paging.txt");
			}
			
			if (filterCriteriaStatementTemplate == null) {
				filterCriteriaStatementTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/filter-criteria-statement.txt");
			}
			
			if (saveEntityTemplate == null) {
				saveEntityTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/save-entity.txt");
			}
			
			if (editEntityTemplate == null) {
				editEntityTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/edit-entity.txt");
			}
			
			if (deleteEntityTemplate == null) {
				deleteEntityTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/process/delete-entity.txt");
			}
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error loading process templates: " + ioException.getMessage());
		}
	}
	
	public void process(Project project, String basePackageName, Path basePackagePath, String configurationPackageName) throws GeneratorException {
    
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

			    // Create process classes
			    createViewEntitiesProcess(fragment, configurationPackageName, fragmentPackageName, fragmentPackagePath);
			    createSelectEntitiesProcess(fragment, configurationPackageName, fragmentPackageName, fragmentPackagePath);
			    createDeleteEntityProcess(fragment, configurationPackageName, fragmentPackageName, fragmentPackagePath);
			    createEditEntityProcess(fragment, configurationPackageName, fragmentPackageName, fragmentPackagePath);
			    createSaveEntityProcess(fragment, configurationPackageName, fragmentPackageName, fragmentPackagePath);
			}
			catch (IOException ioException) {

				throw new GeneratorException("Error creating process class files: " + ioException.getMessage());
			}
		}
	}

	private void createViewEntitiesProcess(Fragment fragment, String configurationPackageName, String fragmentPackageName, Path fragmentPackagePath) 
			throws GeneratorException {

		try {
			// Initialize view entities
			String viewEntities = null;
	
			// Select view entities template based on paging
			if (fragment.getIsPaginationSupported()) {
				
				viewEntities = viewEntitiesPagingTemplate;
			} 
			else {
				
				viewEntities = viewEntitiesTemplate;
			}
	
			// Add configuration package name
			viewEntities = viewEntities.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
	
			// Get fragment name
			String fragmentName = fragment.getName();
			
			// Create camel case based resource names
			String camelCaseEntityName = TextConverter.convertCommonToCamel(fragmentName, false);
			String viewClassName = "View" + TextConverter.convertToPlural(camelCaseEntityName);
			String managerClassName = camelCaseEntityName + "Manager";
	
			// Create constant case based resource names
			String constantEntityName = TextConverter.convertCommonToConstant(fragmentName);
			String attributeName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
			String pageName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
	
			// Get page size
			String pageSize = fragment.getPageSize() == null ? "0" : fragment.getPageSize().toString();
			
			// Set template variables
			viewEntities = viewEntities.replaceAll("PACKAGE-NAME", fragmentPackageName);
			viewEntities = viewEntities.replaceAll("ENTITY-NAME", camelCaseEntityName);
			viewEntities = viewEntities.replaceAll("VIEW-CLASS-NAME", viewClassName);
			viewEntities = viewEntities.replaceAll("MANAGER-CLASS-NAME", managerClassName);
			viewEntities = viewEntities.replaceAll("ATTRIBUTE-NAME", attributeName);
			viewEntities = viewEntities.replaceAll("PAGE-NAME", pageName);
	
			// Set paging specific variables
			if (fragment.getIsPaginationSupported()) {
	
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
				viewEntities = viewEntities.replaceAll("PAGE-SIZE", pageSize);
			}
	
			// Get parent entity ID field name
			String parentIdFieldName = getParentIdFieldName(fragment.getParentName());
	
			// Get filter attributes
			List<Attribute> filterAttributes = fragment.getSearchFilterAttributes();

			if ((parentIdFieldName == null || parentIdFieldName.trim().length() == 0) && (filterAttributes == null || filterAttributes.isEmpty())) {
	
				// Clear filter criteria definition
				viewEntities = viewEntities.replaceAll("FILTER-CRITERIA-DEFINITION", "");
			} 
			else {
	
				// Initialize definition template
				String filterCriteriaDefinition = null;
	
				// Set definition template
				if (fragment.getIsPaginationSupported()) {
	
					filterCriteriaDefinition = filterCriteriaDefinitionPagingTemplate;
				} 
				else {
	
					filterCriteriaDefinition = filterCriteriaDefinitionTemplate;
				}
	
				// Create sort field and sort direction field names
				String sortFieldFieldName = constantEntityName + "_SORT_FIELD";
				String sortDirectionFieldName = constantEntityName + "_SORT_DIRECTION";
	
				// Set sort field and sort direction field names
				filterCriteriaDefinition = filterCriteriaDefinition.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
				filterCriteriaDefinition = filterCriteriaDefinition.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);
	
				// Initialize filter criteria statements
				StringBuffer filterCriteriaStatements = new StringBuffer();
	
				if (parentIdFieldName != null && parentIdFieldName.trim().length() > 0) {
	
					// Create filter criteria statement
					String filterCriteriaStatement = filterCriteriaStatementTemplate;
					filterCriteriaStatement = filterCriteriaStatement.replaceAll("FIELD-ID", parentIdFieldName);
					filterCriteriaStatement = filterCriteriaStatement.replaceAll("DEFAULT-ID", "null");
					filterCriteriaStatement = filterCriteriaStatement.replaceAll("OPERATOR-ID", "EQUALS");
					filterCriteriaStatement = filterCriteriaStatement.replaceAll("FILTER-ID", parentIdFieldName);
	
					// Add filter criteria statement
					filterCriteriaStatements.append(filterCriteriaStatement).append(LINE_SEPARATOR);
				}
	
				if (filterAttributes != null) {
	
					for (Attribute filterAttribute : filterAttributes) {
	
						// Get filter attribute type
						AttributeType attributeType = filterAttribute.getType();
	
						// Create filter field IDs
						String fieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttribute.getName());
						String filterFieldId = fieldId + "_FILTER";
	
						// Initialize default operator field ID
						String defaultOperatorFieldId = "null";
	
						if (attributeType.equals(AttributeType.TEXT) || 
								attributeType.equals(AttributeType.MEMO) || 
								attributeType.equals(AttributeType.EMAIL) || 
								attributeType.equals(AttributeType.PHONE) ||
								attributeType.equals(AttributeType.POSTAL_CODE) ||
								attributeType.equals(AttributeType.NUMERIC) ||
								attributeType.equals(AttributeType.DATE) ||
								attributeType.equals(AttributeType.TIME)) {
	
							// Set operator field ID
							defaultOperatorFieldId = "FieldIds." + fieldId + "_FILTER_OPERATOR";
						}
	
						// Initialize operator ID
						String operatorId = "EQUALS";
	
						if (attributeType.equals(AttributeType.TEXT)||
								attributeType.equals(AttributeType.MEMO) ||
								attributeType.equals(AttributeType.EMAIL) ||
								attributeType.equals(AttributeType.PHONE) ||
								attributeType.equals(AttributeType.POSTAL_CODE)) {
	
							// Set operator field ID
							operatorId = "CONTAINS";
						}
	
						// Create filter criteria statement
						String filterCriteriaStatement = filterCriteriaStatementTemplate;
						filterCriteriaStatement = filterCriteriaStatement.replaceAll("FIELD-ID", fieldId);
						filterCriteriaStatement = filterCriteriaStatement.replaceAll("DEFAULT-ID", defaultOperatorFieldId);
						filterCriteriaStatement = filterCriteriaStatement.replaceAll("OPERATOR-ID", operatorId);
						filterCriteriaStatement = filterCriteriaStatement.replaceAll("FILTER-ID", filterFieldId);
	
						// Add filter criteria statement
						filterCriteriaStatements.append(filterCriteriaStatement).append(LINE_SEPARATOR);
					}
				}
	
				// Create filter criteria definition
				filterCriteriaDefinition = filterCriteriaDefinition.replaceAll("FILTER-CRITERIA-STATEMENTS", filterCriteriaStatements.toString());
	
				// Add filter criteria definition
				viewEntities = viewEntities.replaceAll("FILTER-CRITERIA-DEFINITION", filterCriteriaDefinition);
			}

			// Create view process file name
			String viewProcessFileName = viewClassName + ".java";

			// Create view process file
			Path viewProcessFilePath = fragmentPackagePath.resolve(viewProcessFileName);
			Files.writeString(viewProcessFilePath, viewEntities, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating view process class file: " + ioException.getMessage());
		}
	}

	private void createSelectEntitiesProcess(Fragment fragment, String configurationPackageName, String fragmentPackageName, Path fragmentPackagePath) 
			throws GeneratorException {

		try {
			// Initialize select entities template
			String selectEntities = null;

			// Select select entities template based on paging
			if (fragment.getIsPaginationSupported()) {
				
				selectEntities = selectEntitiesPagingTemplate;
			} 
			else {
				
				selectEntities = selectEntitiesTemplate;
			}

			// Add configuration package name
			selectEntities = selectEntities.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);

			// Get fragment name
			String fragmentName = fragment.getName();
			
			// Create camel case based resource names
			String camelCaseEntityName = TextConverter.convertCommonToCamel(fragmentName, false);
			String selectClassName = "Select" + TextConverter.convertToPlural(camelCaseEntityName);
			String managerClassName = camelCaseEntityName + "Manager";

			// Create constant case based resource names
			String constantEntityName = TextConverter.convertCommonToConstant(fragmentName);
			String attributeName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
			String pageName = constantEntityName + "_SELECTION";

			String associateAttributeName = "SELECTED_" + attributeName;
			String associateId = constantEntityName + "_ID";
			String addAssociateId = "SELECT_" + constantEntityName + "_ADD";
			String removeAssociateId = "SELECT_" + constantEntityName + "_REMOVE";
			String closeAssociateId = "SELECT_" + constantEntityName + "_CLOSE";

			// Get page size
			String pageSize = fragment.getPageSize() == null ? "0" : fragment.getPageSize().toString();
			
			// Set template variables
			selectEntities = selectEntities.replaceAll("PACKAGE-NAME", fragmentPackageName);
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
			if (fragment.getIsPaginationSupported()) {

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
				selectEntities = selectEntities.replaceAll("PAGE-SIZE", pageSize);
			}

			// Get parent entity ID field name
			String parentIdFieldName = getParentIdFieldName(fragment.getParentName());
	
			// Get filter attributes
			List<Attribute> filterAttributes = fragment.getSearchFilterAttributes();

			if ((parentIdFieldName == null || parentIdFieldName.trim().length() == 0) && (filterAttributes == null || filterAttributes.isEmpty())) {

				// Clear filter criteria definition
				selectEntities = selectEntities.replaceAll("FILTER-CRITERIA-DEFINITION", "");
			} 
			else {

				// Initialize definition template
				String filterCriteriaDefinition = null;

				// Set definition template
				if (fragment.getIsPaginationSupported()) {

					filterCriteriaDefinition = filterCriteriaDefinitionPagingTemplate;
				} 
				else {

					filterCriteriaDefinition = filterCriteriaDefinitionTemplate;
				}

				// Create sort field and sort direction field names
				String sortFieldFieldName = constantEntityName + "_SORT_FIELD";
				String sortDirectionFieldName = constantEntityName + "_SORT_DIRECTION";

				// Set sort field and sort direction field names
				filterCriteriaDefinition = filterCriteriaDefinition.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
				filterCriteriaDefinition = filterCriteriaDefinition.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);

				// Initialize filter criteria statements
				StringBuffer filterCriteriaStatements = new StringBuffer();

				if (parentIdFieldName != null && parentIdFieldName.trim().length() > 0) {

					// Create filter criteria statement
					String filterCriteriaStatement = filterCriteriaStatementTemplate;
					filterCriteriaStatement = filterCriteriaStatement.replaceAll("FIELD-ID", parentIdFieldName);
					filterCriteriaStatement = filterCriteriaStatement.replaceAll("DEFAULT-ID", "null");
					filterCriteriaStatement = filterCriteriaStatement.replaceAll("OPERATOR-ID", "EQUALS");
					filterCriteriaStatement = filterCriteriaStatement.replaceAll("FILTER-ID", parentIdFieldName);

					// Add filter criteria statement
					filterCriteriaStatements.append(filterCriteriaStatement).append(LINE_SEPARATOR);
				}

				if (filterAttributes != null) {
					
					for (Attribute filterAttribute : filterAttributes) {
	
						// Get filter attribute type
						AttributeType attributeType = filterAttribute.getType();

						// Create filter field IDs
						String fieldId = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttribute.getName());
						String filterFieldId = fieldId + "_FILTER";

						// Initialize default operator field ID
						String defaultOperatorFieldId = "null";

						if (attributeType.equals(AttributeType.TEXT) ||
								attributeType.equals(AttributeType.MEMO) ||
								attributeType.equals(AttributeType.EMAIL) ||
								attributeType.equals(AttributeType.PHONE) ||
								attributeType.equals(AttributeType.POSTAL_CODE) ||
								attributeType.equals(AttributeType.NUMERIC) ||
								attributeType.equals(AttributeType.DATE) ||
								attributeType.equals(AttributeType.TIME)) {

							// Set operator field ID
							defaultOperatorFieldId = "FieldIds." + fieldId + "_FILTER_OPERATOR";
						}

						// Initialize operator ID
						String operatorId = "EQUALS";

						if (attributeType.equals(AttributeType.TEXT) ||
								attributeType.equals(AttributeType.MEMO) ||
								attributeType.equals(AttributeType.EMAIL) ||
								attributeType.equals(AttributeType.PHONE) ||
								attributeType.equals(AttributeType.POSTAL_CODE)) {

							// Set operator field ID
							operatorId = "CONTAINS";
						}

						// Create filter criteria statement
						String filterCriteriaStatement = filterCriteriaStatementTemplate;
						filterCriteriaStatement = filterCriteriaStatement.replaceAll("FIELD-ID", fieldId);
						filterCriteriaStatement = filterCriteriaStatement.replaceAll("DEFAULT-ID", defaultOperatorFieldId);
						filterCriteriaStatement = filterCriteriaStatement.replaceAll("OPERATOR-ID", operatorId);
						filterCriteriaStatement = filterCriteriaStatement.replaceAll("FILTER-ID", filterFieldId);

						// Add filter criteria statement
						filterCriteriaStatements.append(filterCriteriaStatement).append(LINE_SEPARATOR);
					}
				}

				// Create filter criteria definition
				filterCriteriaDefinition = filterCriteriaDefinition.replaceAll("FILTER-CRITERIA-STATEMENTS", filterCriteriaStatements.toString());

				// Add filter criteria definition
				selectEntities = selectEntities.replaceAll("FILTER-CRITERIA-DEFINITION", filterCriteriaDefinition);
			}

			// Create select entities process file name
			String selectProcessFileName = selectClassName + ".java";

			// Create select entities process file
			Path selectProcessFilePath = fragmentPackagePath.resolve(selectProcessFileName);
			Files.writeString(selectProcessFilePath, selectEntities, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {
	
			throw new GeneratorException("Error creating select process class file: " + ioException.getMessage());
		}
	}

	private void createDeleteEntityProcess(Fragment fragment, String configurationPackageName, String fragmentPackageName, Path fragmentPackagePath) 
			throws GeneratorException {

		try {
			// Add configuration package name
			String deleteEntity = deleteEntityTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
	
			// Get fragment name
			String fragmentName = fragment.getName();
			
			// Create camel case based resource names
			String camelCaseEntityName = TextConverter.convertCommonToCamel(fragmentName, false);
			String deleteClassName = "Delete" + camelCaseEntityName;
			String viewClassName = "View" + TextConverter.convertToPlural(camelCaseEntityName);
			String managerClassName = camelCaseEntityName + "Manager";
	
			// Create constant case based resource names
			String constantEntityName = TextConverter.convertCommonToConstant(fragmentName);
			String fieldName = constantEntityName + "_ID";
	
			// Set template variables
			deleteEntity = deleteEntity.replaceAll("PACKAGE-NAME", fragmentPackageName);
			deleteEntity = deleteEntity.replaceAll("ENTITY-NAME", camelCaseEntityName);
			deleteEntity = deleteEntity.replaceAll("DELETE-CLASS-NAME", deleteClassName);
			deleteEntity = deleteEntity.replaceAll("VIEW-CLASS-NAME", viewClassName);
			deleteEntity = deleteEntity.replaceAll("MANAGER-CLASS-NAME", managerClassName);
			deleteEntity = deleteEntity.replaceAll("FIELD-NAME", fieldName);
	
			// Create delete entity process file name
			String deleteProcessFileName = deleteClassName + ".java";

			// Create delete entity process file
			Path deleteProcessFilePath = fragmentPackagePath.resolve(deleteProcessFileName);
			Files.writeString(deleteProcessFilePath, deleteEntity, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {
	
			throw new GeneratorException("Error creating delete process class file: " + ioException.getMessage());
		}
	}

	private void createEditEntityProcess(Fragment fragment, String configurationPackageName, String fragmentPackageName, Path fragmentPackagePath) 
			throws GeneratorException {

		try {
			// Add configuration package name
			String editEntity = editEntityTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
	
			// Get fragment name
			String fragmentName = fragment.getName();
			
			// Create camel case based resource names
			String camelCaseEntityName = TextConverter.convertCommonToCamel(fragmentName, false);
			String editClassName = "Edit" + camelCaseEntityName;
			String viewClassName = "View" + TextConverter.convertToPlural(camelCaseEntityName);
			String entityClassName = camelCaseEntityName;
			String managerClassName = camelCaseEntityName + "Manager";
	
			// Create constant case based resource names
			String constantEntityName = TextConverter.convertCommonToConstant(fragmentName);
			String fieldName = constantEntityName + "_ID";
			String attributeName = constantEntityName;
			String pageName = constantEntityName;
	
			// Set template variables
			editEntity = editEntity.replaceAll("PACKAGE-NAME", fragmentPackageName);
			editEntity = editEntity.replaceAll("ENTITY-NAME", camelCaseEntityName);
			editEntity = editEntity.replaceAll("EDIT-CLASS-NAME", editClassName);
			editEntity = editEntity.replaceAll("ENTITY-CLASS-NAME", entityClassName);
			editEntity = editEntity.replaceAll("MANAGER-CLASS-NAME", managerClassName);
			editEntity = editEntity.replaceAll("FIELD-NAME", fieldName);
			editEntity = editEntity.replaceAll("ATTRIBUTE-NAME", attributeName);
			editEntity = editEntity.replaceAll("PAGE-NAME", pageName);
			editEntity = editEntity.replaceAll("VIEW-CLASS-NAME", viewClassName);
	
			// Create edit entity process file name
			String editProcessFileName = editClassName + ".java";

			// Create edit entity process file
			Path editProcessFilePath = fragmentPackagePath.resolve(editProcessFileName);
			Files.writeString(editProcessFilePath, editEntity, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {
	
			throw new GeneratorException("Error creating edit process class file: " + ioException.getMessage());
		}
	}

	private void createSaveEntityProcess(Fragment fragment, String configurationPackageName, String fragmentPackageName, Path fragmentPackagePath) 
			throws GeneratorException {

		try {
			// Add configuration package name
			String saveEntity = saveEntityTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);
	
			// Get fragment name
			String fragmentName = fragment.getName();
			
			// Create camel case based resource names
			String camelCaseEntityName = TextConverter.convertCommonToCamel(fragmentName, false);
			String saveClassName = "Save" + camelCaseEntityName;
			String viewClassName = "View" + TextConverter.convertToPlural(camelCaseEntityName);
			String editClassName = "Edit" + camelCaseEntityName;
			String entityClassName = camelCaseEntityName;
			String managerClassName = camelCaseEntityName + "Manager";
	
			// Create constant case based resource names
			String constantEntityName = TextConverter.convertCommonToConstant(fragmentName);
			String fieldName = constantEntityName + "_ID";
			String attributeName = constantEntityName;
	
			// Set template variables
			saveEntity = saveEntity.replaceAll("PACKAGE-NAME", fragmentPackageName);
			saveEntity = saveEntity.replaceAll("ENTITY-NAME", camelCaseEntityName);
			saveEntity = saveEntity.replaceAll("SAVE-CLASS-NAME", saveClassName);
			saveEntity = saveEntity.replaceAll("ENTITY-CLASS-NAME", entityClassName);
			saveEntity = saveEntity.replaceAll("MANAGER-CLASS-NAME", managerClassName);
			saveEntity = saveEntity.replaceAll("FIELD-NAME", fieldName);
			saveEntity = saveEntity.replaceAll("ATTRIBUTE-NAME", attributeName);
			saveEntity = saveEntity.replaceAll("VIEW-CLASS-NAME", viewClassName);
			saveEntity = saveEntity.replaceAll("EDIT-CLASS-NAME", editClassName);
	
			// Create save entity process file name
			String saveProcessFileName = saveClassName + ".java";

			// Create save entity process file
			Path saveProcessFilePath = fragmentPackagePath.resolve(saveProcessFileName);
			Files.writeString(saveProcessFilePath, saveEntity, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {
	
			throw new GeneratorException("Error creating save process class file: " + ioException.getMessage());
		}
	}

	private String getParentIdFieldName(String parentName) {

		// Initialize return value
		String parentIdFieldName = null;

		if (parentName != null && parentName.trim().length() > 0) {

			// Create column name
			String parentConstantName = TextConverter.convertCommonToConstant(parentName);
			parentIdFieldName = parentConstantName + "_ID";
		}

		return parentIdFieldName;
	}
}
