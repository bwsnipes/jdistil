package com.bws.jdistil.builder.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bws.jdistil.builder.data.AssociationType;
import com.bws.jdistil.builder.data.Attribute;
import com.bws.jdistil.builder.data.AttributeType;
import com.bws.jdistil.builder.data.Fragment;
import com.bws.jdistil.builder.data.Project;
import com.bws.jdistil.builder.data.Relationship;
import com.bws.jdistil.builder.generator.util.ResourceReader;
import com.bws.jdistil.builder.generator.util.TextConverter;

public class ConfigurationGenerator {
	
	private static final String FIELD_PREFIX = "F";
	private static final String ACTION_PREFIX = "A";
	private static final String PAGE_PREFIX = "P";
	
	private static ResourceReader resourceReader = new ResourceReader();
	
	private static String configurationTemplate = null;
	private static String constantTemplate = null;
	private static String fieldTemplate = null;
	private static String addNumberRuleTemplate = null;
	private static String addMaxLengthRuleTemplate = null;
	private static String addConverterRuleTemplate = null;
	private static String addEmailRuleTemplate = null;
	private static String addPhoneNumberRuleTemplate = null;
	private static String addPostalCodeRuleTemplate = null;
	private static String addFieldToFieldsTemplate = null;
	private static String actionTemplate = null;
	private static String addProcessorFactoryTemplate = null;
	private static String addFieldToActionTemplate = null;
	private static String addActionToActionsTemplate = null;
	private static String pageTemplate = null;
	private static String addPageToPagesTemplate = null;
	private static String objectBindingTemplate = null;
	private static String addFieldToBindingTemplate = null;
	private static String addCollectionFieldToBindingTemplate = null;
	private static String addBindingToBindingsTemplate = null;

	public ConfigurationGenerator() {
		super();
	}

	private void loadTemplates() throws GeneratorException {
		
		try {
			// Load templates
			if (configurationTemplate == null) {
				configurationTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/configuration.txt");
			}

			if (constantTemplate == null) {
				constantTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/constant.txt");
			}

			if (fieldTemplate == null) {
				fieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/field.txt");
			}

			if (addFieldToFieldsTemplate == null) {
				addFieldToFieldsTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-field-to-fields.txt");
			}

			if (addNumberRuleTemplate == null) {
				addNumberRuleTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-number-rule.txt");
			}

			if (addMaxLengthRuleTemplate == null) {
				addMaxLengthRuleTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-max-length-rule.txt");
			}

			if (addConverterRuleTemplate == null) {
				addConverterRuleTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-converter-rule.txt");
			}

			if (addEmailRuleTemplate == null) {
				addEmailRuleTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-email-rule.txt");
			}

			if (addPhoneNumberRuleTemplate == null) {
				addPhoneNumberRuleTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-phone-number-rule.txt");
			}

			if (addPostalCodeRuleTemplate == null) {
				addPostalCodeRuleTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-postal-code-rule.txt");
			}

			if (actionTemplate == null) {
				actionTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/action.txt");
			}

			if (addProcessorFactoryTemplate == null) {
				addProcessorFactoryTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-processor-factory.txt");
			}

			if (addFieldToActionTemplate == null) {
				addFieldToActionTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-field-to-action.txt");
			}

			if (addActionToActionsTemplate == null) {
				addActionToActionsTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-action-to-actions.txt");
			}

			if (pageTemplate == null) {
				pageTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/page.txt");
			}

			if (addPageToPagesTemplate == null) {
				addPageToPagesTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-page-to-pages.txt");
			}

			if (objectBindingTemplate == null) {
				objectBindingTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/object-binding.txt");
			}

			if (addFieldToBindingTemplate == null) {
				addFieldToBindingTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-field-to-binding.txt");
			}

			if (addCollectionFieldToBindingTemplate == null) {
				addCollectionFieldToBindingTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-collection-field-to-binding.txt");
			}

			if (addBindingToBindingsTemplate == null) {
				addBindingToBindingsTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/add-binding-to-bindings.txt");
			}
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error loading configuration templates: " + ioException.getMessage());
		}
	}

	public void process(Project project, String basePackageName, String configurationPackageName, Path configurationPackagePath,
			Map<String, String> fieldConstants, Map<String, String> actionConstants, Map<String, String> pageConstants,
			Map<String, String> categoryConstants) throws GeneratorException {

		// Load templates
		loadTemplates();
		
		// Create app configuration writers
		AppConfigurationWriters appWriters = new AppConfigurationWriters();
		
		// Initialize constant ID indexes
		int fieldIndex = 1;
		int actionIndex = 1;
		int pageIndex = 1;
		int categoryIndex = 1;
		
		try {
			// Build fragment configuration package path
			Path fragmentConfigurationPackagePath = configurationPackagePath.resolve("fragments");
			
			// Create fragment configuration package directories
			Files.createDirectories(fragmentConfigurationPackagePath);
			
			// Get fragment configuration package name
			String fragmentConfigurationPackageName = configurationPackageName.concat(".fragments");
			
			for (Fragment fragment : project.getFragments()) {

				// Create fragment writers
				FragmentConfigurationWriters fragmentWriters = new FragmentConfigurationWriters();
				
				try {
					// Update fragment configuration in application configuration
					updateFragmentConfiguration(fragment, appWriters);

					// Update fragment specific configuration information (Indexed constants)
					fieldIndex = updateFieldConfiguration(fragment, fieldIndex, appWriters, fragmentWriters, fieldConstants);
					actionIndex = updateActionConfiguration(fragment, actionIndex, appWriters, fragmentWriters, actionConstants);
					pageIndex = updatePageConfiguration(fragment, pageIndex, appWriters, fragmentWriters, pageConstants);
					categoryIndex = updateCategoryConfiguration(fragment, categoryIndex, appWriters, categoryConstants);
					
					// Update fragment specific configuration information (Non-indexed constants)
					updateAttributeConfiguration(fragment, appWriters);
					updateObjectBindingConfiguration(fragment, fragmentWriters);
					
					// Get all relationships associated with this fragment 
					List<Relationship> fragmentRelationships = project.getSourceFragmentRelationships(fragment.getName());
					
					if (fragmentRelationships != null) {
						
						for (Relationship relationship : fragmentRelationships) {
							
							// Update relationship specific configuration information (Indexed constants)
							fieldIndex = updateFieldConfiguration(relationship, fieldIndex, appWriters, fragmentWriters, fieldConstants);
							
							// Update relationship specific configuration information (Non-indexed constants)
							updateActionConfiguration(relationship, fragmentWriters);
							updateObjectBindingConfiguration(relationship, fragmentWriters);
						}
					}
					
					// Create fragment configuration file
					createFragmentConfiguration(fragment, fragmentConfigurationPackageName, fragmentConfigurationPackagePath, fragmentWriters);
				}
				finally {
					
					// Close fragment writers
					fragmentWriters.close();
				}
			}
			
			// Create application configuration file
			createApplicationConfiguration(basePackageName, configurationPackageName, configurationPackagePath, appWriters);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error generating configuration artifacts: " + ioException.getMessage());
		}
		finally {
			
			// Close configuration writer
			appWriters.close();
		}
	}

	private void createFragmentConfiguration(Fragment fragment,	String fragmentConfigurationPackageName, 
			Path fragmentConfigurationPackagePath, FragmentConfigurationWriters fragmentWriters) throws GeneratorException {
		
		try {
			// Get fragment name
			String fragmentName = fragment.getName();
			
			// Create fragment package name
			String fragmentPackageName = fragmentConfigurationPackageName + "." + TextConverter.convertCommonToCamel(fragmentName, true);
			
			// Create fragment class name
			String fragmentClassName = TextConverter.convertCommonToCamel(fragmentName, false);

			// Create fragment configuration
			String fragmentConfigurationContent = configurationTemplate.replaceAll("CONFIG-PACKAGE-NAME", fragmentConfigurationPackageName);
			fragmentConfigurationContent = fragmentConfigurationContent.replaceAll("ENTITY-PACKAGE-NAME", fragmentPackageName);
			fragmentConfigurationContent = fragmentConfigurationContent.replaceAll("ENTITY-NAME", fragmentClassName);

			// Write registration statements
			fragmentConfigurationContent = fragmentConfigurationContent.replaceAll("REGISTERED-FIELDS", fragmentWriters.getFieldStatements());
			fragmentConfigurationContent = fragmentConfigurationContent.replaceAll("REGISTERED-ACTIONS", fragmentWriters.getActionStatements());
			fragmentConfigurationContent = fragmentConfigurationContent.replaceAll("REGISTERED-PAGES", fragmentWriters.getPageStatements());
			fragmentConfigurationContent = fragmentConfigurationContent.replaceAll("REGISTERED-OBJECT-BINDINGS", fragmentWriters.getObjectBindingStatements());
			
			// Create fragment configuration file
			String fragmentConfigurationFileName = fragmentClassName + ".java";
			
			// Create fragment configuration file
			Path fragmentConfigurationPath = fragmentConfigurationPackagePath.resolve(fragmentConfigurationFileName);
			Files.writeString(fragmentConfigurationPath, fragmentConfigurationContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating fragment configuration class file: " + ioException.getMessage());
		}
	}

	private void createApplicationConfiguration(String basePackageName, String configurationPackageName, 
			Path configurationPackagePath, AppConfigurationWriters writers) throws GeneratorException {
		
		try {
			// Get configuration file content
			String configurationContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/Configuration.txt");

			// Update configuration file content
			configurationContent = configurationContent.replaceAll("BASE_PACKAGE_NAME", basePackageName);
			configurationContent = configurationContent.replaceAll("FRAGMENT-CONFIGURATION-STATEMENTS", writers.getFragmentStatements());

			// Create configuration file
			Path configurationPath = configurationPackagePath.resolve("Configuration.java");
			Files.writeString(configurationPath, configurationContent, StandardOpenOption.CREATE_NEW);

		
			// Get field IDs file content
			String fieldIdsContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/FieldIds.txt");

			// Update field IDs content
			fieldIdsContent = fieldIdsContent.replaceAll("PACKAGE_NAME", configurationPackageName);
			fieldIdsContent = fieldIdsContent.replaceAll("FIELD-ID-CONSTANTS", writers.getFieldConstants());

			// Create field IDs file
			Path fieldIdsPath = configurationPackagePath.resolve("FieldIds.java");
			Files.writeString(fieldIdsPath, fieldIdsContent, StandardOpenOption.CREATE_NEW);

			
			// Get action IDs file content
			String actionIdsContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/ActionIds.txt");
			
			// Update action IDs file content
			actionIdsContent = actionIdsContent.replaceAll("PACKAGE_NAME", configurationPackageName);
			actionIdsContent = actionIdsContent.replaceAll("ACTION-ID-CONSTANTS", writers.getActionConstants());

			// Create view home file
			Path actionIdsPath = configurationPackagePath.resolve("ActionIds.java");
			Files.writeString(actionIdsPath, actionIdsContent, StandardOpenOption.CREATE_NEW);

			
			// Get page IDs file content
			String pageIdsContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/PageIds.txt");

			// Update page IDs content
			pageIdsContent = pageIdsContent.replaceAll("PACKAGE_NAME", configurationPackageName);
			pageIdsContent = pageIdsContent.replaceAll("PAGE-ID-CONSTANTS", writers.getPageConstants());

			// Create page IDs file
			Path pageIdsPath = configurationPackagePath.resolve("PageIds.java");
			Files.writeString(pageIdsPath, pageIdsContent, StandardOpenOption.CREATE_NEW);


			// Get attribute names file content
			String attributeNamesContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/AttributeNames.txt");

			// Update attribute names file content
			attributeNamesContent = attributeNamesContent.replaceAll("PACKAGE_NAME", configurationPackageName);
			attributeNamesContent = attributeNamesContent.replaceAll("ATTRIBUTE-NAME-CONSTANTS", writers.getAttributeConstants());

			// Create attribute names file
			Path attributeNamesPath = configurationPackagePath.resolve("AttributeNames.java");
			Files.writeString(attributeNamesPath, attributeNamesContent, StandardOpenOption.CREATE_NEW);

		
			// Get category IDs file content
			String categoryIdsContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/CategoryIds.txt");

			// Update category IDs content
			categoryIdsContent = categoryIdsContent.replaceAll("PACKAGE_NAME", configurationPackageName);
			categoryIdsContent = categoryIdsContent.replaceAll("CATEGORY-ID-CONSTANTS", writers.getCategoryConstants());
			
			// Create category IDs file
			Path categoryIdsPath = configurationPackagePath.resolve("CategoryIds.java");
			Files.writeString(categoryIdsPath, categoryIdsContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating application configuration files: " + ioException.getMessage());
		}
	}
	
	private void updateFragmentConfiguration(Fragment fragment, AppConfigurationWriters appWriters) {

		// Get fragment name
		String fragmentName = fragment.getName();

		// Create fragment specific configuration class name
		String fragmentConfigurationClassName = TextConverter.convertCommonToCamel(fragmentName, false)	+ "Configuration";

		// Create fragment configuration reference statement
		String fragmentReferenceStatement = "  fragmentConfigurations.add(new " + fragmentConfigurationClassName + "());";

		// Write fragment statement
		appWriters.getFragmentStatementWriter().println(fragmentReferenceStatement);
	}

	private int updateFieldConfiguration(Fragment fragment, int fieldIndex, AppConfigurationWriters appWriters, 
			FragmentConfigurationWriters fragmentWriters, Map<String, String> fieldConstants) {
		
		// Get fragment name
		String fragmentName = fragment.getName();

		// Create constant fragment name
		String constantFragmentName = TextConverter.convertCommonToConstant(fragmentName);

		// Create ID and version field IDs
		String idFieldId = constantFragmentName + "_ID";
		String versionFieldId = constantFragmentName + "_VERSION";

		// Create field ID constants
		createConstant(idFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);
		createConstant(versionFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

		// Create ID field and version field statements
		createIdFieldStatements(idFieldId, fragmentWriters.getFieldStatementWriter());
		createVersionFieldStatements(versionFieldId, fragmentWriters.getFieldStatementWriter());

		
		// Create sort field field ID
		String sortFieldFieldId = constantFragmentName + "_SORT_FIELD";

		// Create sort field field ID constant
		createConstant(sortFieldFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

		// Create sort field field label
		String sortFieldFieldLabel = TextConverter.convertConstantToCommon("SORT_FIELD");

		// Create sort field field statements
		createStringFieldStatements(sortFieldFieldId, sortFieldFieldLabel, fragmentWriters.getFieldStatementWriter());


		// Create sort direction field ID
		String sortDirectionFieldId = constantFragmentName + "_SORT_DIRECTION";

		// Create sort direction field ID constant
		createConstant(sortDirectionFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

		// Create sort direction field label
		String sortDirectionFieldLabel = TextConverter.convertConstantToCommon("SORT_DIRECTION");

		// Create sort direction field statements
		createStringFieldStatements(sortDirectionFieldId, sortDirectionFieldLabel, fragmentWriters.getFieldStatementWriter());

		
		if (fragment.getIsPaginationSupported()) {

			// Create current page number field ID
			String currentPageNumberFieldId = constantFragmentName + "_CURRENT_PAGE_NUMBER";

			// Create current page number field ID constant
			createConstant(currentPageNumberFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

			
			// Create current page number label
			String currentPageNumberLabel = TextConverter.convertConstantToCommon("CURRENT_PAGE_NUMBER");

			// Create current page number field statements
			createStringFieldStatements(currentPageNumberFieldId, currentPageNumberLabel, fragmentWriters.getFieldStatementWriter());


			// Create selected page number field ID
			String selectedPageNumberFieldId = constantFragmentName + "_SELECTED_PAGE_NUMBER";

			// Create selected page number field ID constant
			createConstant(selectedPageNumberFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

			// Create selected page number label
			String selectedPageNumberLabel = TextConverter.convertConstantToCommon("SELECTED_PAGE_NUMBER");

			// Create selected page number field statements
			createStringFieldStatements(selectedPageNumberFieldId, selectedPageNumberLabel, fragmentWriters.getFieldStatementWriter());
		}

		// Get filter attribute names
		List<Attribute> searchFilterAttributes = fragment.getSearchFilterAttributes();

		if (searchFilterAttributes != null && !searchFilterAttributes.isEmpty()) {

			// Create group state field ID
			String groupStateFieldId = constantFragmentName + "_GROUP_STATE";

			// Create group state field ID constant
			createConstant(groupStateFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

			// Create group state label
			String groupStateLabel = TextConverter.convertConstantToCommon("GROUP_STATE");

			// Create group state field statements
			createStringFieldStatements(groupStateFieldId, groupStateLabel, fragmentWriters.getFieldStatementWriter());

			for (Attribute searchFilterAttribute : searchFilterAttributes) {

				// Get attribute name
				String filterAttributeName = searchFilterAttribute.getName();
				
				// Create constant name
				String constantName = TextConverter.convertCommonToConstant(filterAttributeName);

				// Create filter field ID
				String filterFieldId = constantFragmentName + "_" + constantName + "_FILTER";

				// Create filter field name
				String filterFieldName = TextConverter.convertConstantToCommon(constantName);

				// Create filter field ID constant
				createConstant(filterFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

				// Create filter field statements
				createFieldStatements(filterFieldId, filterFieldName, searchFilterAttribute, true, fragmentWriters.getFieldStatementWriter());

				// Get filter attribute type
				AttributeType filterAttributeType = searchFilterAttribute.getType();

				if (filterAttributeType.equals(AttributeType.DATE)
						|| filterAttributeType.equals(AttributeType.EMAIL)
						|| filterAttributeType.equals(AttributeType.MEMO)
						|| filterAttributeType.equals(AttributeType.NUMERIC)
						|| filterAttributeType.equals(AttributeType.PHONE)
						|| filterAttributeType.equals(AttributeType.POSTAL_CODE)
						|| filterAttributeType.equals(AttributeType.TEXT)
						|| filterAttributeType.equals(AttributeType.TIME)) {

					// Create operator field ID
					String operatorFieldId = constantFragmentName + "_" + constantName + "_FILTER_OPERATOR";

					// Create operator field ID constant
					createConstant(operatorFieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

					// Create operator field statements
					createOperatorFieldStatements(operatorFieldId, fragmentWriters.getFieldStatementWriter());
				}
			}
		}

		for (Attribute attribute : fragment.getAttributes()) {

			// Create constant name
			String constantName = TextConverter.convertCommonToConstant(attribute.getName());

			// Create field ID
			String fieldId = constantFragmentName + "_" + constantName;

			// Create field name
			String fieldName = TextConverter.convertConstantToCommon(constantName);

			// Create field ID constant
			createConstant(fieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

			// Create field statements
			createFieldStatements(fieldId, fieldName, attribute, false, fragmentWriters.getFieldStatementWriter());
		}
		
		return fieldIndex;
	}

	private int updateFieldConfiguration(Relationship relationship, int fieldIndex, 
			AppConfigurationWriters appWriters, FragmentConfigurationWriters fragmentWriters, Map<String, String> fieldConstants) {

		// Get source and target fragment names
		String sourceFragmentName = relationship.getSourceFragmentName();
		String targetFragmentName = relationship.getTargetFragmentName();
		
		// Get many-to=many relationship indicator
		boolean isManyToManyAssociation = relationship.getAssociation().equals(AssociationType.MANY_TO_MANY);

		// Update field configuration for source fragment
		updateFieldConfiguration(sourceFragmentName, targetFragmentName, fieldIndex++, isManyToManyAssociation, 
				appWriters, fragmentWriters, fieldConstants);

		if (relationship.getIsBidirectional()) {

			// Update field configuration for target fragment if relationship is bidirectional
			updateFieldConfiguration(targetFragmentName, sourceFragmentName, fieldIndex++, isManyToManyAssociation, 
					appWriters, fragmentWriters, fieldConstants);
		}
		
		return fieldIndex;
	}

	private int updateFieldConfiguration(String primaryFragmentName, String secondaryFragmentName, int fieldIndex, boolean isManyToManyAssociation, 
			AppConfigurationWriters appWriters, FragmentConfigurationWriters fragmentWriters, Map<String, String> fieldConstants) {

		// Get constant fragment names
		String primaryConstantEntityName = TextConverter.convertCommonToConstant(primaryFragmentName);
		String secondaryConstantEntityName = TextConverter.convertCommonToConstant(secondaryFragmentName);

		// Initialize field label
		String fieldLabel = secondaryFragmentName;

		// Create field ID
		String fieldId = primaryConstantEntityName + "_" + secondaryConstantEntityName + "_ID";

		// Pluralize field ID
		if (isManyToManyAssociation) {
			fieldId = fieldId + "S";
		}

		// Create field ID constant
		createConstant(fieldId, FIELD_PREFIX, fieldIndex++, appWriters.getFieldConstantWriter(), fieldConstants);

		// Create field statements
		createIntegerFieldStatements(fieldId, fieldLabel, fragmentWriters.getFieldStatementWriter());
		
		return fieldIndex;
	}

	private int updateActionConfiguration(Fragment fragment, int actionIndex, AppConfigurationWriters appWriters, 
			FragmentConfigurationWriters fragmentWriters, Map<String, String> actionConstants) {

		// Get fragment name
		String fragmentName = fragment.getName();

		// Create constant fragment name
		String constantFragmentName = TextConverter.convertCommonToConstant(fragmentName);

		// Create ID and version field IDs
		String idFieldId = constantFragmentName + "_ID";
		String versionFieldId = constantFragmentName + "_VERSION";

		// Create action IDs
		String viewActionId = "VIEW_" + TextConverter.convertToPlural(constantFragmentName).toUpperCase();
		String addActionId = "ADD_" + constantFragmentName;
		String editActionId = "EDIT_" + constantFragmentName;
		String deleteActionId = "DELETE_" + constantFragmentName;
		String saveActionId = "SAVE_" + constantFragmentName;
		String cancelActionId = "CANCEL_" + constantFragmentName;
		String selectActionId = "SELECT_" + TextConverter.convertToPlural(constantFragmentName).toUpperCase();
		String selectAddActionId = "SELECT_" + constantFragmentName + "_ADD";
		String selectRemoveActionId = "SELECT_" + constantFragmentName + "_REMOVE";
		String selectCloseActionId = "SELECT_" + constantFragmentName + "_CLOSE";

		// Create action ID constants
		createConstant(viewActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(addActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(editActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(deleteActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(saveActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(cancelActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(selectActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(selectAddActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(selectRemoveActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
		createConstant(selectCloseActionId, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);

		// Get parent ID field ID
		String parentIdFieldId = getParentIdFieldId(fragment);

		// Get attributes
		List<Attribute> attributes = fragment.getAttributes();

		// Get filter attribute names
		List<Attribute> filterAttributes = fragment.getSearchFilterAttributes();

		// Get paging indicator
		boolean isPaginationSupported = fragment.getIsPaginationSupported();

		// Create view action class name
		String viewCommonName = TextConverter.convertConstantToCommon(viewActionId);
		String viewActionClassName = TextConverter.convertCommonToCamel(viewCommonName, false);

		// Create select action class name
		String selectCommonName = TextConverter.convertConstantToCommon(selectActionId);
		String selectActionClassName = TextConverter.convertCommonToCamel(selectCommonName, false);

		// Create action statements
		createViewActionStatements(viewActionId, viewActionClassName, "Apply", constantFragmentName, filterAttributes, parentIdFieldId, isPaginationSupported, false, fragmentWriters.getActionStatementWriter());
		createAddActionStatements(addActionId, editActionId, fragmentWriters.getActionStatementWriter());
		createEditActionStatements(editActionId, idFieldId, parentIdFieldId, fragmentWriters.getActionStatementWriter());
		createDeleteActionStatements(deleteActionId, idFieldId, fragmentWriters.getActionStatementWriter());
		createSaveActionStatements(saveActionId, constantFragmentName, idFieldId, versionFieldId, parentIdFieldId, attributes, fragmentWriters.getActionStatementWriter());
		createCancelActionStatements(cancelActionId, viewActionId, fragmentWriters.getActionStatementWriter());

		// Create selection based action statements
		createViewActionStatements(selectActionId, selectActionClassName, "Apply", constantFragmentName, filterAttributes, null, isPaginationSupported, true, fragmentWriters.getActionStatementWriter());
		createViewActionStatements(selectAddActionId, selectActionClassName, "Add", constantFragmentName, filterAttributes, null, isPaginationSupported, true, fragmentWriters.getActionStatementWriter());
		createViewActionStatements(selectRemoveActionId, selectActionClassName, "Remove", constantFragmentName, filterAttributes, null, isPaginationSupported, true, fragmentWriters.getActionStatementWriter());
		createViewActionStatements(selectCloseActionId, selectActionClassName, "Close", constantFragmentName, filterAttributes, null, isPaginationSupported, true, fragmentWriters.getActionStatementWriter());

		if (isPaginationSupported) {

			// Create paging action names
			String viewPreviousPageActionName = "VIEW_" + constantFragmentName + "_PREVIOUS_PAGE";
			String viewSelectPageActionName = "VIEW_" + constantFragmentName + "_SELECT_PAGE";
			String viewNextPageActionName = "VIEW_" + constantFragmentName + "_NEXT_PAGE";

			// Create selection based paging action names
			String selectPreviousPageActionName = "SELECT_" + constantFragmentName + "_PREVIOUS_PAGE";
			String selectSelectPageActionName = "SELECT_" + constantFragmentName + "_SELECT_PAGE";
			String selectNextPageActionName = "SELECT_" + constantFragmentName + "_NEXT_PAGE";

			// Create paging action ID constants
			createConstant(viewPreviousPageActionName, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
			createConstant(viewSelectPageActionName, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
			createConstant(viewNextPageActionName, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);

			// Create selection based paging action ID constants
			createConstant(selectPreviousPageActionName, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
			createConstant(selectSelectPageActionName, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);
			createConstant(selectNextPageActionName, ACTION_PREFIX, actionIndex++, appWriters.getActionConstantWriter(), actionConstants);

			// Create paging action statements
			createPagingActionStatements(viewActionId, viewPreviousPageActionName, constantFragmentName, filterAttributes, parentIdFieldId, "Previous Page", false, fragmentWriters.getActionStatementWriter());
			createPagingActionStatements(viewActionId, viewSelectPageActionName, constantFragmentName, filterAttributes, parentIdFieldId, "Select Page", false, fragmentWriters.getActionStatementWriter());
			createPagingActionStatements(viewActionId, viewNextPageActionName, constantFragmentName, filterAttributes, parentIdFieldId, "Next Page", false, fragmentWriters.getActionStatementWriter());

			// Create selection based paging action statements
			createPagingActionStatements(selectActionId, selectPreviousPageActionName, constantFragmentName, filterAttributes, null, "Previous Page", true, fragmentWriters.getActionStatementWriter());
			createPagingActionStatements(selectActionId, selectSelectPageActionName, constantFragmentName, filterAttributes, null, "Select Page", true, fragmentWriters.getActionStatementWriter());
			createPagingActionStatements(selectActionId, selectNextPageActionName, constantFragmentName, filterAttributes, null, "Next Page", true, fragmentWriters.getActionStatementWriter());
		}
		
		return actionIndex;
	}

	private void updateActionConfiguration(Relationship relationship, FragmentConfigurationWriters fragmentWriters) {

		// Get source and target fragment names
		String sourceFragmentName = relationship.getSourceFragmentName();
		String targetFragmentName = relationship.getTargetFragmentName();
		
		// Set required field indicator
		boolean isRequired = relationship.getIsTargetRequired();

		// Set many-to-many association indicator
		boolean isManyToManyAssociation = relationship.getAssociation().equals(AssociationType.MANY_TO_MANY);

		// Update action configuration for target fragment
		updateActionConfiguration(sourceFragmentName, targetFragmentName, isRequired, isManyToManyAssociation, fragmentWriters.getActionStatementWriter());

		if (relationship.getIsBidirectional()) {

			// Update action configuration for source fragment
			updateActionConfiguration(targetFragmentName, sourceFragmentName, false, isManyToManyAssociation, fragmentWriters.getActionStatementWriter());
		}
	}

	private void updateActionConfiguration(String primaryFragmentName, String secondaryFragmentName,
			boolean isRequired, boolean isManyToManyAssociation, PrintWriter actionStatementWriter) {

		// Get constant fragment names
		String primaryConstantFragmentName = TextConverter.convertCommonToConstant(primaryFragmentName);
		String secondaryConstantFragmentName = TextConverter.convertCommonToConstant(secondaryFragmentName);

		// Create template variables
		String saveActionId = "SAVE_" + primaryConstantFragmentName;
		String commonName = TextConverter.convertConstantToCommon(saveActionId);
		String actionName = TextConverter.convertCommonToCamel(commonName, true);

		// Create field ID
		String fieldId = primaryConstantFragmentName + "_" + secondaryConstantFragmentName + "_ID";

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

		// Write field to action statement
		actionStatementWriter.println(sortFieldToAction);
	}

	private int updatePageConfiguration(Fragment fragment, int pageIndex, AppConfigurationWriters appWriters, 
			FragmentConfigurationWriters fragmentWriters, Map<String, String> pageConstants) {

		// Get fragment name
		String fragmentName = fragment.getName();

		// Create constant fragment name
		String constantFragmentName = TextConverter.convertCommonToConstant(fragmentName);

		// Create page names
		String viewPageName = TextConverter.convertToPlural(constantFragmentName).toUpperCase();
		String editPageName = constantFragmentName;
		String selectPageName = constantFragmentName + "_SELECTION";

		// Create page ID constants
		createConstant(viewPageName, PAGE_PREFIX, pageIndex++, appWriters.getPageConstantWriter(), pageConstants);
		createConstant(editPageName, PAGE_PREFIX, pageIndex++, appWriters.getPageConstantWriter(), pageConstants);
		createConstant(selectPageName, PAGE_PREFIX, pageIndex++, appWriters.getPageConstantWriter(), pageConstants);

		// Create directory name
		String directoryName = TextConverter.convertCommonToCamel(fragmentName, true);

		// Create page statements
		createPageStatements(directoryName, viewPageName, fragmentWriters.getPageStatementWriter());
		createPageStatements(directoryName, editPageName, fragmentWriters.getPageStatementWriter());
		createPageStatements(directoryName, selectPageName, fragmentWriters.getPageStatementWriter());
		
		return pageIndex;
	}

	private int updateCategoryConfiguration(Fragment fragment, int categoryIndex, AppConfigurationWriters appWriters, 
			Map<String, String> categoryConstants) {

		// Create lookup categories set to keep track of processed items
		Set<String> lookupCategories = new HashSet<String>();
		
		for (Attribute attribute : fragment.getAttributes()) {
			
			if (attribute.getType().equals(AttributeType.LOOKUP)) {
				
				// Get lookup category
				String lookupCategory = attribute.getLookupCategory();
				
				if (!lookupCategories.contains(lookupCategory)) {
					
					// Get category ID and name
					String categoryId = String.valueOf(categoryIndex++);
					String categoryName = TextConverter.convertCommonToConstant(lookupCategory);
					
					// Add constant to reference map
					categoryConstants.put(categoryName, categoryId);
					
					// Create category content
					String categoryContent = "   public static final int " + categoryName + " = " + categoryId + ";";
					
					// Write category constant
					appWriters.getCategoryConstantWriter().println(categoryContent);
					
					// Add to lookup category to set to avoid duplicate processing
					lookupCategories.add(lookupCategory);
				}
			}
		}
		
		return categoryIndex;
	}

	private void updateAttributeConfiguration(Fragment fragment, AppConfigurationWriters appWriters) {
		
		// Get fragment name
		String fragmentName = fragment.getName();

		// Create constant fragment name
		String constantFragmentEntityName = TextConverter.convertCommonToConstant(fragmentName);

		// Create attribute names
		String attributeName = constantFragmentEntityName;
		String attributesName = TextConverter.convertToPlural(constantFragmentEntityName).toUpperCase();
		String selectedAttributesName = "SELECTED_" + attributesName;

		// Create attribute name constants
		createConstant(attributeName, appWriters.getAttributeConstantWriter());
		createConstant(attributesName, appWriters.getAttributeConstantWriter());
		createConstant(selectedAttributesName, appWriters.getAttributeConstantWriter());
	}

	private void updateObjectBindingConfiguration(Fragment fragment, FragmentConfigurationWriters fragmentWriters) {

		// Get fragment name
		String fragmentName = fragment.getName();

		// Get parent ID field ID
		String parentIdFieldId = getParentIdFieldId(fragment);

		// Get attributes
		List<Attribute> attributes = fragment.getAttributes();

		// Create object binding statements
		createObjectBindingStatements(fragmentName, parentIdFieldId, attributes, fragmentWriters.getObjectBindingStatementWriter());
	}

	private void updateObjectBindingConfiguration(Relationship relationship, FragmentConfigurationWriters fragmentWriters) {

		// Get source and target fragment names
		String sourceFragmentName = relationship.getSourceFragmentName();
		String targetFragmentName = relationship.getTargetFragmentName();
		
		// Set many-to-many association indicator
		boolean isManyToManyAssociation = relationship.getAssociation().equals(AssociationType.MANY_TO_MANY);

		// Update object binding for source fragment
		updateObjectBindingConfiguration(sourceFragmentName, targetFragmentName, isManyToManyAssociation, fragmentWriters.getObjectBindingStatementWriter());

		if (relationship.getIsBidirectional()) {

			// Update object binding for target fragment
			updateObjectBindingConfiguration(targetFragmentName, sourceFragmentName, isManyToManyAssociation, fragmentWriters.getObjectBindingStatementWriter());
		}
	}

	private void updateObjectBindingConfiguration(String primaryFragmentName, String secondaryFragmentName, 
			boolean isManyToManyAssociation, PrintWriter objectBindingStatementWriter) {

		// Get primary and secondary constant entity names
		String primaryConstantEntityName = TextConverter.convertCommonToConstant(primaryFragmentName);
		String secondaryConstantEntityName = TextConverter.convertCommonToConstant(secondaryFragmentName);

		// Create template variables
		String bindingName = TextConverter.convertCommonToCamel(primaryFragmentName, true);
		String fieldId = primaryConstantEntityName + "_" + secondaryConstantEntityName + "_ID";
		String propertyName = TextConverter.convertCommonToCamel(secondaryFragmentName, false) + "Id";

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

		// Write field to binding statement
		objectBindingStatementWriter.println(fieldToBinding);
	}

	private void createConstant(String constantName, String valuePrefix, int valueIndex, 
			PrintWriter constantWriter, Map<String, String> fieldConstants) {

		// Create constant value
		String constantValue = valuePrefix + String.valueOf(valueIndex);

		// Add constant to reference map
		fieldConstants.put(constantName, constantValue);
		
		// Create field contents
		String contents = constantTemplate.replaceAll("CONSTANT-NAME", constantName);
		contents = contents.replaceAll("CONSTANT-VALUE", constantValue);

		// Print contents using writer
		constantWriter.println(contents);
	}

	private void createConstant(String constantName, PrintWriter constantWriter) {
		
		// Create field contents
		String contents = constantTemplate.replaceAll("CONSTANT-NAME", constantName);
		contents = contents.replaceAll("CONSTANT-VALUE", constantName);

		// Print contents using writer
		constantWriter.println(contents);
	}

	private void createIdFieldStatements(String fieldId, PrintWriter statementsWriter) {

		// Create field name
		String commonName = TextConverter.convertConstantToCommon(fieldId);
		String fieldName = TextConverter.convertCommonToCamel(commonName, true);

		// Create field statement
		String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
		field = field.replaceAll("FIELD-ID", fieldId);
		field = field.replaceAll("FIELD-TYPE", "INTEGER");
		field = field.replaceAll("FIELD-LABEL", "ID");
		field = field.replaceAll("FIELD-CONVERTER", "NumberConverter.getInstance()");

		// Write field statement
		statementsWriter.println(field);

		// Create add field statement
		String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

		// Write add field statement
		statementsWriter.println(addField);
	}

	private void createVersionFieldStatements(String fieldId, PrintWriter fieldStatementWrite) {

		// Create field name
		String commonName = TextConverter.convertConstantToCommon(fieldId);
		String fieldName = TextConverter.convertCommonToCamel(commonName, true);

		// Create field statement
		String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
		field = field.replaceAll("FIELD-ID", fieldId);
		field = field.replaceAll("FIELD-TYPE", "LONG");
		field = field.replaceAll("FIELD-LABEL", "Version");
		field = field.replaceAll("FIELD-CONVERTER", "NumberConverter.getInstance()");

		// Write field statement
		fieldStatementWrite.print(field);

		// Create add field statement
		String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

		// Write add field statement
		fieldStatementWrite.println(addField);
	}

	private void createFieldStatements(String fieldId, String fieldLabel, Attribute attribute, boolean isFilterField, PrintWriter fieldStatementWriter) {

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

		// Write field statement
		fieldStatementWriter.println(field);

		if (attribute.getType().equals(AttributeType.NUMERIC)) {

			// Get value type, precision and scale
			String valueType = attribute.getNumericType().toString();
			String precision = String.valueOf(attribute.getNumericPrecision());
			String scale = String.valueOf(attribute.getNumericScale());

			// Create number rule
			String numberRule = addNumberRuleTemplate.replaceAll("FIELD-NAME", fieldName);
			numberRule = numberRule.replaceAll("VALUE-TYPE", valueType.toUpperCase());
			numberRule = numberRule.replaceAll("PRECISION", precision);
			numberRule = numberRule.replaceAll("SCALE", scale);

			// Write field statement
			fieldStatementWriter.println(numberRule);
		} 
		else if (attribute.getType().equals(AttributeType.DATE) || attribute.getType().equals(AttributeType.TIME)) {

			// Create converter rule
			String converterRule = addConverterRuleTemplate.replaceAll("FIELD-NAME", fieldName);

			// Write field statement
			fieldStatementWriter.println(converterRule);
		} 
		else if (attribute.getType().equals(AttributeType.MEMO)) {

			// Get max length
			Integer maxLength = attribute.getTextMaxLength();

			// Create max length rule
			String maxLengthRule = addMaxLengthRuleTemplate.replaceAll("FIELD-NAME", fieldName);
			maxLengthRule = maxLengthRule.replaceAll("MAX-LENGTH", maxLength.toString());

			// Write field statement
			fieldStatementWriter.println(maxLengthRule);
		} 
		else if (!isFilterField && attribute.getType().equals(AttributeType.EMAIL)) {

			// Create email rule
			String emailRule = addEmailRuleTemplate.replaceAll("FIELD-NAME", fieldName);

			// Write field statement
			fieldStatementWriter.println(emailRule);
		} 
		else if (!isFilterField && attribute.getType().equals(AttributeType.PHONE)) {

			// Create phone number rule
			String phoneNumberRule = addPhoneNumberRuleTemplate.replaceAll("FIELD-NAME", fieldName);

			// Write field statement
			fieldStatementWriter.println(phoneNumberRule);
		} 
		else if (!isFilterField && attribute.getType().equals(AttributeType.POSTAL_CODE)) {

			// Create postal code rule
			String postalCodeRule = addPostalCodeRuleTemplate.replaceAll("FIELD-NAME", fieldName);

			// Write field statement
			fieldStatementWriter.println(postalCodeRule);
		}

		// Create add field statement
		String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

		// Write field statement
		fieldStatementWriter.println(addField);
	}

	private void createStringFieldStatements(String fieldId, String fieldLabel, PrintWriter fieldStatementWriter) {

		// Create field name
		String commonName = TextConverter.convertConstantToCommon(fieldId);
		String fieldName = TextConverter.convertCommonToCamel(commonName, true);

		// Create field statement
		String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
		field = field.replaceAll("FIELD-ID", fieldId);
		field = field.replaceAll("FIELD-TYPE", "STRING");
		field = field.replaceAll("FIELD-LABEL", fieldLabel);
		field = field.replaceAll("FIELD-CONVERTER", "null");

		// Write field statement
		fieldStatementWriter.println(field);

		// Create add field statement
		String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

		// Write add field statement
		fieldStatementWriter.println(addField);
	}

	private void createIntegerFieldStatements(String fieldId, String fieldLabel, PrintWriter fieldStatementWriter) {

		// Create field name
		String commonName = TextConverter.convertConstantToCommon(fieldId);
		String fieldName = TextConverter.convertCommonToCamel(commonName, true);

		// Create field statement
		String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
		field = field.replaceAll("FIELD-ID", fieldId);
		field = field.replaceAll("FIELD-TYPE", "INTEGER");
		field = field.replaceAll("FIELD-LABEL", fieldLabel);
		field = field.replaceAll("FIELD-CONVERTER", "NumberConverter.getInstance()");

		// Write field statement
		fieldStatementWriter.println(field);

		// Create add field statement
		String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

		// Write add field statement
		fieldStatementWriter.println(addField);
	}

	private void createOperatorFieldStatements(String fieldId, PrintWriter fieldStatementWriter) {

		// Create field name
		String commonName = TextConverter.convertConstantToCommon(fieldId);
		String fieldName = TextConverter.convertCommonToCamel(commonName, true);

		// Create field statement
		String field = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
		field = field.replaceAll("FIELD-ID", fieldId);
		field = field.replaceAll("FIELD-TYPE", "STRING");
		field = field.replaceAll("FIELD-LABEL", "Operator");
		field = field.replaceAll("FIELD-CONVERTER", "null");

		// Write field statement
		fieldStatementWriter.println(field);

		// Create add field statement
		String addField = addFieldToFieldsTemplate.replaceAll("FIELD-NAME", fieldName);

		// Write add field statement
		fieldStatementWriter.println(addField);
	}

	private void createViewActionStatements(String viewActionId, String viewActionClassName, String actionLabel, 
			String constantFragmentName, List<Attribute> filterAttributes, String parentIdFieldId, boolean isPagingEnabled,
			boolean isSelectEntityAction, PrintWriter actionStatementWriter) {

		// Create template variables
		String commonName = TextConverter.convertConstantToCommon(viewActionId);
		String actionName = TextConverter.convertCommonToCamel(commonName, true);

		// Create action statement
		String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
		action = action.replaceAll("ACTION-ID", viewActionId);
		action = action.replaceAll("ACTION-LABEL", actionLabel);

		// Write action statement
		actionStatementWriter.println(action);

		// Create processor factory statement
		String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
		processorFactory = processorFactory.replaceAll("ACTION-CLASS", viewActionClassName);

		// Write processor factory statement
		actionStatementWriter.println(processorFactory);

		// Create and append action field statements
		createActionFieldStatements(actionName, constantFragmentName, filterAttributes, parentIdFieldId, isPagingEnabled, isSelectEntityAction, actionStatementWriter);

		// Create action to actions statement
		String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);

		// Write action to actions statement
		actionStatementWriter.println(actionToActions);
	}

	private void createActionFieldStatements(String actionName, String constantFragmentName, List<Attribute> filterAttributes, 
			String parentIdFieldId, boolean isPagingEnabled, boolean isSelectEntityAction, PrintWriter actionStatementWriter) {

		if (parentIdFieldId != null) {

			// Create parent ID field to action statement
			String parentIdFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			parentIdFieldToAction = parentIdFieldToAction.replaceAll("FIELD-ID", parentIdFieldId);
			parentIdFieldToAction = parentIdFieldToAction.replaceAll("IS-REQUIRED", "true");

			// Write parent ID field to action statement
			actionStatementWriter.println(parentIdFieldToAction);
		}

		if (isSelectEntityAction) {

			// Create parent field ID to action statement
			String parentFieldIdToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			parentFieldIdToAction = parentFieldIdToAction.replaceAll("FIELD-ID", "PARENT_FIELD_ID");
			parentFieldIdToAction = parentFieldIdToAction.replaceAll("IS-REQUIRED", "true");

			// Write parent field ID to action statement
			actionStatementWriter.println(parentFieldIdToAction);

			// Create parent action ID to action statement
			String parentDataObjectClassToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			parentDataObjectClassToAction = parentDataObjectClassToAction.replaceAll("FIELD-ID", "PARENT_ACTION_ID");
			parentDataObjectClassToAction = parentDataObjectClassToAction.replaceAll("IS-REQUIRED", "true");

			// Write parent action ID to action statement
			actionStatementWriter.println(parentDataObjectClassToAction);

			// Create parent page ID to action statement
			String parentPageIdToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			parentPageIdToAction = parentPageIdToAction.replaceAll("FIELD-ID", "PARENT_PAGE_ID");
			parentPageIdToAction = parentPageIdToAction.replaceAll("IS-REQUIRED", "true");

			// Write parent page ID to action statement
			actionStatementWriter.println(parentPageIdToAction);
		}

		if (filterAttributes != null && !filterAttributes.isEmpty()) {

			for (Attribute filterAttribute : filterAttributes) {

				// Get filter attribute name
				String filterAttributeName = filterAttribute.getName();
				
				// Create filter field ID
				String filterFieldId = constantFragmentName + "_" + TextConverter.convertCommonToConstant(filterAttributeName) + "_FILTER";

				// Create filter field to action statement
				String filterFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
				filterFieldToAction = filterFieldToAction.replaceAll("FIELD-ID", filterFieldId);
				filterFieldToAction = filterFieldToAction.replaceAll("IS-REQUIRED", "false");

				// Write filter field to action statement
				actionStatementWriter.println(filterFieldToAction);

				// Get filter attribute type
				AttributeType filterAttributeType = filterAttribute.getType();

				if (filterAttributeType.equals(AttributeType.DATE)
						|| filterAttributeType.equals(AttributeType.EMAIL)
						|| filterAttributeType.equals(AttributeType.MEMO)
						|| filterAttributeType.equals(AttributeType.NUMERIC)
						|| filterAttributeType.equals(AttributeType.PHONE)
						|| filterAttributeType.equals(AttributeType.POSTAL_CODE)
						|| filterAttributeType.equals(AttributeType.TEXT)
						|| filterAttributeType.equals(AttributeType.TIME)) {

					// Create operator field ID
					String operatorFieldId = constantFragmentName + "_" + TextConverter.convertCommonToConstant(filterAttributeName) + "_FILTER_OPERATOR";

					// Create operator field to action statement
					String operatorFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
					operatorFieldToAction = operatorFieldToAction.replaceAll("FIELD-ID", operatorFieldId);
					operatorFieldToAction = operatorFieldToAction.replaceAll("IS-REQUIRED", "false");

					// Write operator field to action statement
					actionStatementWriter.println(operatorFieldToAction);
				}
			}

			// Create group state field name
			String groupStateFieldName = constantFragmentName + "_GROUP_STATE";

			// Create group state field to action statement
			String filterToggleToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			filterToggleToAction = filterToggleToAction.replaceAll("FIELD-ID", groupStateFieldName);
			filterToggleToAction = filterToggleToAction.replaceAll("IS-REQUIRED", "false");

			// Write group state field to action statement
			actionStatementWriter.println(filterToggleToAction);
		}

		if (isPagingEnabled) {

			// Create current page number field name
			String currentPageNumberFieldName = constantFragmentName + "_CURRENT_PAGE_NUMBER";

			// Create current page number to action statement
			String currentPageNumberToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			currentPageNumberToAction = currentPageNumberToAction.replaceAll("FIELD-ID", currentPageNumberFieldName);
			currentPageNumberToAction = currentPageNumberToAction.replaceAll("IS-REQUIRED", "false");

			// Write current page number to action statement
			actionStatementWriter.println(currentPageNumberToAction);

			// Create selected page number field name
			String selectedPageNumberFieldName = constantFragmentName + "_SELECTED_PAGE_NUMBER";

			// Create selected page number to action statement
			String selectedPageNumberToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			selectedPageNumberToAction = selectedPageNumberToAction.replaceAll("FIELD-ID", selectedPageNumberFieldName);
			selectedPageNumberToAction = selectedPageNumberToAction.replaceAll("IS-REQUIRED", "false");

			// Write selected page number to action statement
			actionStatementWriter.println(selectedPageNumberToAction);
		}

		// Create sort field field name
		String sortFieldFieldName = constantFragmentName + "_SORT_FIELD";

		// Create sort field to action statement
		String sortFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
		sortFieldToAction = sortFieldToAction.replaceAll("FIELD-ID", sortFieldFieldName);
		sortFieldToAction = sortFieldToAction.replaceAll("IS-REQUIRED", "false");

		// Write sort field to action statement
		actionStatementWriter.println(sortFieldToAction);

		// Create sort direction field name
		String sortDirectionFieldName = constantFragmentName + "_SORT_DIRECTION";

		// Create sort direction to action statement
		String sortDirectionToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
		sortDirectionToAction = sortDirectionToAction.replaceAll("FIELD-ID", sortDirectionFieldName);
		sortDirectionToAction = sortDirectionToAction.replaceAll("IS-REQUIRED", "false");

		// Write sort field to action statement
		actionStatementWriter.println(sortDirectionToAction);
	}

	private void createPagingActionStatements(String viewActionId, String pagingActionId, String constantEntityName, 
			List<Attribute> filterAttributes, String parentIdFieldId, String label, boolean isSelectEntityAction,
			PrintWriter actionStatementWriter) {

		// Create template variables
		String commonPagingName = TextConverter.convertConstantToCommon(pagingActionId);
		String actionName = TextConverter.convertCommonToCamel(commonPagingName, true);
		String commonViewName = TextConverter.convertConstantToCommon(viewActionId);
		String actionClass = TextConverter.convertCommonToCamel(commonViewName, false);

		// Create action statement
		String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
		action = action.replaceAll("ACTION-ID", pagingActionId);
		action = action.replaceAll("ACTION-LABEL", label);

		// Write action statement
		actionStatementWriter.println(action);

		// Create processor factory statement
		String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
		processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);

		// Write processor factory statement
		actionStatementWriter.println(processorFactory);

		// Create and append action field statements
		createActionFieldStatements(actionName, constantEntityName, filterAttributes, parentIdFieldId, true, isSelectEntityAction, actionStatementWriter);

		// Create action to actions statement
		String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);

		// Write action to actions statement
		actionStatementWriter.println(actionToActions);
	}

	private void createAddActionStatements(String addActionId, String editActionId, PrintWriter actionStatementWriter) {

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

		// Write action statement
		actionStatementWriter.println(action);

		// Create processor factory statement
		String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
		processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);

		// Write processor factory statement
		actionStatementWriter.println(processorFactory);

		// Create action to actions statement
		String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);

		// Write action to actions statement
		actionStatementWriter.println(actionToActions);
	}

	private void createEditActionStatements(String editActionId, String idFieldId, String parentIdFieldId, PrintWriter actionStatementWriter) {

		// Create template variables
		String commonName = TextConverter.convertConstantToCommon(editActionId);
		String actionName = TextConverter.convertCommonToCamel(commonName, true);
		String actionClass = TextConverter.convertCommonToCamel(commonName, false);

		// Create action statement
		String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
		action = action.replaceAll("ACTION-ID", editActionId);
		action = action.replaceAll("ACTION-LABEL", "Edit");

		// Write action statement
		actionStatementWriter.println(action);

		// Create processor factory statement
		String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
		processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);

		// Write processor factory statement
		actionStatementWriter.println(processorFactory);

		// Create ID field to action statement
		String idFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
		idFieldToAction = idFieldToAction.replaceAll("FIELD-ID", idFieldId);
		idFieldToAction = idFieldToAction.replaceAll("IS-REQUIRED", "true");

		// Write ID field to action statement
		actionStatementWriter.println(idFieldToAction);

		if (parentIdFieldId != null && parentIdFieldId.trim().length() > 0) {

			// Create parent ID field to action statement
			String parentIdFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			parentIdFieldToAction = parentIdFieldToAction.replaceAll("FIELD-ID", parentIdFieldId);
			parentIdFieldToAction = parentIdFieldToAction.replaceAll("IS-REQUIRED", "true");

			// Write parent ID field to action statement
			actionStatementWriter.println(parentIdFieldToAction);
		}

		// Create action to actions statement
		String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);

		// Write action to actions statement
		actionStatementWriter.println(actionToActions);
	}

	private void createDeleteActionStatements(String deleteActionId, String idFieldId, PrintWriter actionStatementWriter) {

		// Create template variables
		String commonName = TextConverter.convertConstantToCommon(deleteActionId);
		String actionName = TextConverter.convertCommonToCamel(commonName, true);
		String actionClass = TextConverter.convertCommonToCamel(commonName, false);

		// Create action statement
		String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
		action = action.replaceAll("ACTION-ID", deleteActionId);
		action = action.replaceAll("ACTION-LABEL", "Delete");

		// Write action statement
		actionStatementWriter.println(action);

		// Create processor factory statement
		String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
		processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);

		// Write processor factory statement
		actionStatementWriter.println(processorFactory);

		// Create ID field to action statement
		String idFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
		idFieldToAction = idFieldToAction.replaceAll("FIELD-ID", idFieldId);
		idFieldToAction = idFieldToAction.replaceAll("IS-REQUIRED", "true");

		// Write ID field to action statement
		actionStatementWriter.println(idFieldToAction);

		// Create action to actions statement
		String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);

		// Write action to actions statement
		actionStatementWriter.println(actionToActions);
	}

	private void createSaveActionStatements(String saveActionId, String constantEntityName, String idFieldId,
			String versionFieldId, String parentIdFieldId, List<Attribute> attributes, PrintWriter actionStatementWriter) {

		// Create template variables
		String commonName = TextConverter.convertConstantToCommon(saveActionId);
		String actionName = TextConverter.convertCommonToCamel(commonName, true);
		String actionClass = TextConverter.convertCommonToCamel(commonName, false);

		// Create action statement
		String action = actionTemplate.replaceAll("ACTION-NAME", actionName);
		action = action.replaceAll("ACTION-ID", saveActionId);
		action = action.replaceAll("ACTION-LABEL", "Save");

		// Write action statement
		actionStatementWriter.println(action);

		// Create processor factory statement
		String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
		processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);

		// Write processor factory statement
		actionStatementWriter.println(processorFactory);

		// Create ID field to action statement
		String idFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
		idFieldToAction = idFieldToAction.replaceAll("FIELD-ID", idFieldId);
		idFieldToAction = idFieldToAction.replaceAll("IS-REQUIRED", "false");

		// Write ID field to action statement
		actionStatementWriter.println(idFieldToAction);

		// Create version field to action statement
		String versionFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
		versionFieldToAction = versionFieldToAction.replaceAll("FIELD-ID", versionFieldId);
		versionFieldToAction = versionFieldToAction.replaceAll("IS-REQUIRED", "false");

		// Write version field to action statement
		actionStatementWriter.println(versionFieldToAction);

		if (parentIdFieldId != null && parentIdFieldId.trim().length() > 0) {

			// Create parent ID field to action statement
			String parentIdFieldToAction = addFieldToActionTemplate.replaceAll("ACTION-NAME", actionName);
			parentIdFieldToAction = parentIdFieldToAction.replaceAll("FIELD-ID", parentIdFieldId);
			parentIdFieldToAction = parentIdFieldToAction.replaceAll("IS-REQUIRED", "true");

			// Write parent ID field to action statement
			actionStatementWriter.println(parentIdFieldToAction);
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

				// Write attribute field to action statement
				actionStatementWriter.println(attributeFieldToAction);
			}
		}

		// Create action to actions statement
		String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);

		// Write action to actions statement
		actionStatementWriter.println(actionToActions);
	}

	private void createCancelActionStatements(String cancelActionId, String viewActionId, PrintWriter actionStatementWriter) {

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

		// Write action statement
		actionStatementWriter.println(action);

		// Create processor factory statement
		String processorFactory = addProcessorFactoryTemplate.replaceAll("ACTION-NAME", actionName);
		processorFactory = processorFactory.replaceAll("ACTION-CLASS", actionClass);

		// Write processor factory statement
		actionStatementWriter.println(processorFactory);

		// Create action to actions statement
		String actionToActions = addActionToActionsTemplate.replaceAll("ACTION-NAME", actionName);

		// Write action to actions statement
		actionStatementWriter.println(actionToActions);
	}

	private void createPageStatements(String directoryName, String pageId, PrintWriter pageStatementWriter) {

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

		// Write action statement
		pageStatementWriter.println(action);

		// Create page to pages statement
		String pageToPages = addPageToPagesTemplate.replaceAll("PAGE-NAME", pageName);

		// Write page to pages statement
		pageStatementWriter.println(pageToPages);
	}

	private void createObjectBindingStatements(String fragmentName, String parentIdFieldId, List<Attribute> attributes, PrintWriter objectBindingStatementWriter) {

		// Create template variables
		String bindingName = TextConverter.convertCommonToCamel(fragmentName, true);
		String bindingClassName = TextConverter.convertCommonToCamel(fragmentName, false);

		// Create object binding statement
		String objectBinding = objectBindingTemplate.replaceAll("BINDING-NAME", bindingName);
		objectBinding = objectBinding.replaceAll("BINDING-CLASS", bindingClassName);

		// Write action statement
		objectBindingStatementWriter.println(objectBinding);

		// Create constant entity name
		String constantEntityName = TextConverter.convertCommonToConstant(fragmentName);

		// Create ID field ID
		String idFieldId = constantEntityName + "_ID";

		// Create field to binding statement for ID field
		String fieldToBinding = addFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
		fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", idFieldId);
		fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", "Id");

		// Write field to binding statement
		objectBindingStatementWriter.println(fieldToBinding);

		// Create version field ID
		String versionFieldId = constantEntityName + "_VERSION";

		// Create field to binding statement for ID field
		fieldToBinding = addFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
		fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", versionFieldId);
		fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", "Version");

		// Write field to binding statement
		objectBindingStatementWriter.println(fieldToBinding);

		if (parentIdFieldId != null && parentIdFieldId.trim().length() > 0) {

			// Create parent entity ID property name
			String commonParentEntityName = TextConverter.convertConstantToCommon(parentIdFieldId);
			String parentEntityPropertyName = TextConverter.convertCommonToCamel(commonParentEntityName, false);

			// Create field to binding statement for ID field
			fieldToBinding = addFieldToBindingTemplate.replaceAll("BINDING-NAME", bindingName);
			fieldToBinding = fieldToBinding.replaceAll("FIELD-ID", parentIdFieldId);
			fieldToBinding = fieldToBinding.replaceAll("PROPERTY-NAME", parentEntityPropertyName);

			// Write field to binding statement
			objectBindingStatementWriter.println(fieldToBinding);
		}

		for (Attribute attribute : attributes) {

			// Use attribute name as common name
			String commonName = attribute.getName();

			// Create template variables
			String constantAttributeName = TextConverter.convertCommonToConstant(commonName);
			String fieldId = constantEntityName + "_" + constantAttributeName;
			String propertyName = TextConverter.convertCommonToCamel(commonName, false);

			// Initialize associate attribute indicator
			boolean isAssociateAttribute = attribute.getIsLookupMultipleValues();

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

			// Write field to binding statement
			objectBindingStatementWriter.println(fieldToBinding);
		}

		// Create binding to bindings statement
		String bindingToBindings = addBindingToBindingsTemplate.replaceAll("BINDING-NAME", bindingName);

		// Write binding to bindings statement
		objectBindingStatementWriter.println(bindingToBindings);
	}

	private String getParentIdFieldId(Fragment fragment) {

		// Initialize return value
		String parentId = null;

		// Get parent fragment name
		String parentFragmentName = fragment.getParentName();

		if (parentFragmentName != null && parentFragmentName.trim().length() > 0) {

			// Create constant parent entity name
			String parentFragmentCommonName = TextConverter.convertCamelToCommon(parentFragmentName);
			String parentFragmentConstantName = TextConverter.convertCommonToConstant(parentFragmentCommonName);

			// Create parent ID
			parentId = parentFragmentConstantName + "_ID";
		}

		return parentId;
	}

	private String getFieldType(Attribute attribute) {

		// Initialize return value
		String fieldType = null;

		// Get attribute type
		AttributeType type = attribute.getType();

		if (type.equals(AttributeType.TEXT) || type.equals(AttributeType.MEMO) || type.equals(AttributeType.EMAIL) || 
			type.equals(AttributeType.PHONE) || type.equals(AttributeType.POSTAL_CODE)) {

			fieldType = "STRING";
		} 
		else if (type.equals(AttributeType.DATE) || type.equals(AttributeType.TIME)) {
			
			fieldType = "DATE";
		}
		else if (type.equals(AttributeType.BOOLEAN)) {
			
			fieldType = "BOOLEAN";
		}
		else if (type.equals(AttributeType.LOOKUP)) {
			
			fieldType = "INTEGER";
		}
		else if (type.equals(AttributeType.NUMERIC)) {

			// Get scale
			Integer scale = attribute.getNumericScale();

			// Set type based on specified scale
			if (scale == null || scale.intValue() == 0) {
				
				fieldType = "INTEGER";
			} 
			else {
				
				fieldType = "DOUBLE";
			}
		}

		return fieldType;
	}

	private String getFieldConverter(Attribute attribute) {

		// Initialize return value
		String fieldConverter = "null";

		// Get attribute type
		AttributeType type = attribute.getType();

		if (type.equals(AttributeType.BOOLEAN)) {
			
			fieldConverter = "BooleanConverter.getInstance()";
		} 
		else if (type.equals(AttributeType.DATE)) {
			
			fieldConverter = "DateConverter.getInstance()";
		}
		else if (type.equals(AttributeType.TIME)) {
			
			fieldConverter = "TimeConverter.getInstance()";
		}
		else if (type.equals(AttributeType.LOOKUP)) {
			
			fieldConverter = "NumberConverter.getInstance()";
		}
		else if (type.equals(AttributeType.NUMERIC)) {

			// Get scale
			Integer scale = attribute.getNumericScale();

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

private class AppConfigurationWriters {
	
	private StringWriter fragmentStatementStringWriter= null;
	private StringWriter fieldConstantStringWriter= null;
	private StringWriter actionConstantStringWriter= null;
	private StringWriter pageConstantStringWriter= null;
	private StringWriter attributeConstantStringWriter= null;
	private StringWriter categoryConstantStringWriter= null;

	private PrintWriter fragmentStatementPrintWriter = null;
	private PrintWriter fieldConstantPrintWriter = null;
	private PrintWriter actionConstantPrintWriter = null;
	private PrintWriter pageConstantPrintWriter = null;
	private PrintWriter attributeConstantPrintWriter = null;
	private PrintWriter categoryConstantPrintWriter = null;
	
	public AppConfigurationWriters() {
		super();
		
		// Create configuration string writers
		fragmentStatementStringWriter = new StringWriter();
		fieldConstantStringWriter = new StringWriter();
		actionConstantStringWriter = new StringWriter();
		pageConstantStringWriter = new StringWriter();
		attributeConstantStringWriter = new StringWriter();
		categoryConstantStringWriter = new StringWriter();

		// Create configuration print writers
		fragmentStatementPrintWriter = new PrintWriter(fragmentStatementStringWriter, true);
		fieldConstantPrintWriter = new PrintWriter(fieldConstantStringWriter, true);
		actionConstantPrintWriter = new PrintWriter(actionConstantStringWriter, true);
		pageConstantPrintWriter = new PrintWriter(pageConstantStringWriter, true);
		attributeConstantPrintWriter = new PrintWriter(attributeConstantStringWriter, true);
		categoryConstantPrintWriter = new PrintWriter(categoryConstantStringWriter, true);
	}

	public void close() {

		fragmentStatementPrintWriter.close();
		fieldConstantPrintWriter.close();
		actionConstantPrintWriter.close();
		pageConstantPrintWriter.close();
		attributeConstantPrintWriter.close();
		categoryConstantPrintWriter.close();
	}
	
	public String getFragmentStatements() {
		return fragmentStatementStringWriter.toString();
	}

	public String getFieldConstants() {
		return fieldConstantStringWriter.toString();
	}

	public String getActionConstants() {
		return actionConstantStringWriter.toString();
	}

	public String getPageConstants() {
		return pageConstantStringWriter.toString();
	}

	public String getAttributeConstants() {
		return attributeConstantStringWriter.toString();
	}

	public String getCategoryConstants() {
		return categoryConstantStringWriter.toString();
	}

	public PrintWriter getFragmentStatementWriter() {
		return fragmentStatementPrintWriter;
	}

	public PrintWriter getFieldConstantWriter() {
		return fieldConstantPrintWriter;
	}

	public PrintWriter getActionConstantWriter() {
		return actionConstantPrintWriter;
	}

	public PrintWriter getPageConstantWriter() {
		return pageConstantPrintWriter;
	}

	public PrintWriter getAttributeConstantWriter() {
		return attributeConstantPrintWriter;
	}

	public PrintWriter getCategoryConstantWriter() {
		return categoryConstantPrintWriter;
	}
}

private class FragmentConfigurationWriters {
	
	private StringWriter fieldStatementStringWriter= null;
	private StringWriter actionStatementStringWriter= null;
	private StringWriter pageStatementStringWriter= null;
	private StringWriter objectBindingStatementStringWriter= null;

	private PrintWriter fieldStatementPrintWriter = null;
	private PrintWriter actionStatementPrintWriter = null;
	private PrintWriter pageStatementPrintWriter = null;
	private PrintWriter objectBindingStatementPrintWriter = null;
	
	public FragmentConfigurationWriters() {
		super();
		
		// Create configuration string writers
		fieldStatementStringWriter = new StringWriter();
		actionStatementStringWriter = new StringWriter();
		pageStatementStringWriter = new StringWriter();
		objectBindingStatementStringWriter = new StringWriter();

		// Create configuration print writers
		fieldStatementPrintWriter = new PrintWriter(fieldStatementStringWriter, true);
		actionStatementPrintWriter = new PrintWriter(actionStatementStringWriter, true);
		pageStatementPrintWriter = new PrintWriter(pageStatementStringWriter, true);
		objectBindingStatementPrintWriter = new PrintWriter(objectBindingStatementStringWriter, true);
	}

	public void close() {

		fieldStatementPrintWriter.close();
		actionStatementPrintWriter.close();
		pageStatementPrintWriter.close();
		objectBindingStatementPrintWriter.close();
	}
	
	public String getFieldStatements() {
		return fieldStatementStringWriter.toString();
	}

	public String getActionStatements() {
		return actionStatementStringWriter.toString();
	}

	public String getPageStatements() {
		return pageStatementStringWriter.toString();
	}

	public String getObjectBindingStatements() {
		return objectBindingStatementStringWriter.toString();
	}

	public PrintWriter getFieldStatementWriter() {
		return fieldStatementPrintWriter;
	}

	public PrintWriter getActionStatementWriter() {
		return actionStatementPrintWriter;
	}

	public PrintWriter getPageStatementWriter() {
		return pageStatementPrintWriter;
	}

	public PrintWriter getObjectBindingStatementWriter() {
		return objectBindingStatementPrintWriter;
	}
}
}
