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

public class JspGenerator {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static ResourceReader resourceReader = new ResourceReader();

	private static String associateListFieldTemplate = null;
	private static String associateDisplayFieldTemplate = null;
	private static String associateDisplayListFieldTemplate = null;
	private static String booleanEntityFieldTemplate = null;
	private static String booleanListFieldTemplate = null;
	private static String columnHeaderTemplate = null;
	private static String selectColumnHeaderTemplate = null;
	private static String columnTemplate = null;
	private static String columnSelectTemplate = null;
	private static String dependentMenuTemplate = null;
	private static String dependentLinkTemplate = null;
	private static String entitiesPageTemplate = null;
	private static String selectEntitiesPageTemplate = null;
	private static String entityPageTemplate = null;
	private static String filterDataTemplate = null;
	private static String headerLinkTemplate = null;
	private static String hiddenFieldTemplate = null;
	private static String lookupColumnTemplate = null;
	private static String lookupEntityFieldTemplate = null;
	private static String lookupFieldTemplate = null;
	private static String lookupMultipleColumnTemplate = null;
	private static String lookupMultipleEntityFieldTemplate = null;
	private static String lookupMultipleFieldTemplate = null;
	private static String nextPageBreadcrumbActionTemplate = null;
	private static String pagingHeaderTemplate = null;
	private static String previousPageBreadcrumbActionTemplate = null;
	private static String selectPageBreadcrumbActionTemplate = null;
	private static String textFieldTemplate = null;
	private static String memoFieldTemplate = null;
	private static String emailFieldTemplate = null;
	private static String phoneNumberFieldTemplate = null;
	private static String dateFieldTemplate = null;
	private static String timeFieldTemplate = null;
	private static String viewPageBreadcrumbActionTemplate = null;
	private static String textOperatorFieldTemplate = null;
	private static String dateOperatorFieldTemplate = null;
	private static String emailOperatorFieldTemplate = null;
	private static String phoneNumberOperatorFieldTemplate = null;
	private static String timeOperatorFieldTemplate = null;
	private static String memoOperatorFieldTemplate = null;
	private static String homePageTemplate = null;
	private static String headerPageTemplate = null;

	private void loadTemplates() throws GeneratorException {

		try {
			// Load templates
			if (associateListFieldTemplate == null) {
				associateListFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/associate-list-field.txt");
			}

			if (associateDisplayFieldTemplate == null) {
				associateDisplayFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/associate-display-field.txt");
			}

			if (associateDisplayListFieldTemplate == null) {
				associateDisplayListFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/associate-display-list-field.txt");
			}

			if (booleanEntityFieldTemplate == null) {
				booleanEntityFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/boolean-entity-field.txt");
			}

			if (booleanListFieldTemplate == null) {
				booleanListFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/boolean-list-field.txt");
			}

			if (columnHeaderTemplate == null) {
				columnHeaderTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/column-header.txt");
			}

			if (selectColumnHeaderTemplate == null) {
				selectColumnHeaderTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/select-column-header.txt");
			}

			if (columnTemplate == null) {
				columnTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/column.txt");
			}

			if (columnSelectTemplate == null) {
				columnSelectTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/column-select.txt");
			}

			if (dependentMenuTemplate == null) {
				dependentMenuTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/dependent-menu.txt");
			}

			if (dependentLinkTemplate == null) {
				dependentLinkTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/dependent-link.txt");
			}

			if (entitiesPageTemplate == null) {
				entitiesPageTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/entities-page.txt");
			}

			if (selectEntitiesPageTemplate == null) {
				selectEntitiesPageTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/select-entities-page.txt");
			}

			if (entityPageTemplate == null) {
				entityPageTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/entity-page.txt");
			}

			if (filterDataTemplate == null) {
				filterDataTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/filter-data.txt");
			}

			if (headerLinkTemplate == null) {
				headerLinkTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/header-link.txt");
			}

			if (hiddenFieldTemplate == null) {
				hiddenFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/hidden-entity-field.txt");
			}

			if (lookupColumnTemplate == null) {
				lookupColumnTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/lookup-column.txt");
			}

			if (lookupEntityFieldTemplate == null) {
				lookupEntityFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/lookup-entity-field.txt");
			}

			if (lookupFieldTemplate == null) {
				lookupFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/lookup-field.txt");
			}

			if (lookupMultipleColumnTemplate == null) {
				lookupMultipleColumnTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/lookup-multiple-column.txt");
			}

			if (lookupMultipleEntityFieldTemplate == null) {
				lookupMultipleEntityFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/lookup-multiple-entity-field.txt");
			}

			if (lookupMultipleFieldTemplate == null) {
				lookupMultipleFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/lookup-multiple-field.txt");
			}

			if (nextPageBreadcrumbActionTemplate == null) {
				nextPageBreadcrumbActionTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/breadcrumb-action.txt");
			}

			if (pagingHeaderTemplate == null) {
				pagingHeaderTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/paging-header.txt");
			}

			if (previousPageBreadcrumbActionTemplate == null) {
				previousPageBreadcrumbActionTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/breadcrumb-action.txt");
			}

			if (selectPageBreadcrumbActionTemplate == null) {
				selectPageBreadcrumbActionTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/breadcrumb-action.txt");
			}

			if (textFieldTemplate == null) {
				textFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/text-entity-field.txt");
			}

			if (memoFieldTemplate == null) {
				memoFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/memo-entity-field.txt");
			}

			if (emailFieldTemplate == null) {
				emailFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/email-entity-field.txt");
			}

			if (phoneNumberFieldTemplate == null) {
				phoneNumberFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/phone-number-entity-field.txt");
			}

			if (dateFieldTemplate == null) {
				dateFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/date-entity-field.txt");
			}

			if (timeFieldTemplate == null) {
				timeFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/time-entity-field.txt");
			}

			if (viewPageBreadcrumbActionTemplate == null) {
				viewPageBreadcrumbActionTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/breadcrumb-action.txt");
			}

			if (textOperatorFieldTemplate == null) {
				textOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/text-operator-field.txt");
			}

			if (dateOperatorFieldTemplate == null) {
				dateOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/date-operator-field.txt");
			}

			if (emailOperatorFieldTemplate == null) {
				emailOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/email-operator-field.txt");
			}

			if (phoneNumberOperatorFieldTemplate == null) {
				phoneNumberOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/phone-number-operator-field.txt");
			}

			if (timeOperatorFieldTemplate == null) {
				timeOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/time-operator-field.txt");
			}

			if (memoOperatorFieldTemplate == null) {
				memoOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/memo-operator-field.txt");
			}

			if (homePageTemplate == null) {
				homePageTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/home-page.txt");
			}

			if (headerPageTemplate == null) {
				headerPageTemplate = resourceReader.readResource("/com/bws/jdistil/builder/generator/template/jsp/header-page.txt");
			}
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error loading JSP templates: " + ioException.getMessage());
		}
	}

	public JspGenerator() {
		super();
	}

	public void process(Project project, String basePackageName, Path basePackagePath, Path webAppPath, String configurationPackageName) throws GeneratorException {
	    
		// Load templates
		loadTemplates();

		// Create non-fragment specific files
		createHomePage(basePackageName, webAppPath);
		createHeaderPage(basePackageName, webAppPath, project.getFragments());
		
		for (Fragment fragment : project.getFragments()) {
			
			try {
				// Create fragment web directory name
				String directoryName = TextConverter.convertCommonToCamel(fragment.getName(), true);
				
				// Create fragment web directory path
				Path fragmentWebPath = webAppPath.resolve(directoryName);
				
				// Create fragment web directory
				Files.createDirectories(fragmentWebPath);
				
				// Get parent entity name
				String parentName = fragment.getParentName();
				
				// Get all dependent fragments where this fragment is the parent
				List<Fragment> dependentFragments = project.getDependentFragments(fragment.getName());
				
				// Get all relationships associated with this fragment 
				List<Relationship> sourceRelationships = project.getSourceFragmentRelationships(fragment.getName());
				List<Relationship> targetRelationships = project.getTargetFragmentRelationships(fragment.getName());
				
				// Create JSP pages
				createViewEntitiesPage(fragment, dependentFragments, sourceRelationships, targetRelationships, parentName, basePackageName, configurationPackageName, fragmentWebPath);
				createSelectEntitiesPage(fragment, sourceRelationships, targetRelationships, parentName, basePackageName, configurationPackageName, fragmentWebPath);
				createEditEntityPage(fragment, sourceRelationships, targetRelationships, parentName, basePackageName, configurationPackageName, fragmentWebPath);
			}
			catch (IOException ioException) {

				throw new GeneratorException("Error creating JSPs files: " + ioException.getMessage());
			}
		}
	}

	private void createHomePage(String basePackageName, Path webAppPath) throws GeneratorException {

		// Update header content
		String homeJspContent = homePageTemplate.replaceAll("BASE-PACKAGE-NAME", basePackageName);

		try {
			// Create home content file
			Path homeJspPath = webAppPath.resolve("Home.jsp");
			Files.writeString(homeJspPath, homeJspContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {
	
			throw new GeneratorException("Error creating home page JSP file: " + ioException.getMessage());
		}
	}

	private void createHeaderPage(String basePackageName, Path webAppPath, List<Fragment> fragments) throws GeneratorException {

		// Initialize header link content
		StringBuffer headerLinkContent = new StringBuffer();
		
		for (Fragment fragment : fragments) {
			
			// Get entity name
			String entityName = fragment.getName();

			// Create view action name
			String constantEntityName = TextConverter.convertCommonToConstant(entityName);
			String viewActionName = "VIEW_" + TextConverter.convertToPlural(constantEntityName).toUpperCase();

			// Create view page name
			String viewPageName = TextConverter.convertToPlural(constantEntityName).toUpperCase();

			// Build header link
			String headerLink = headerLinkTemplate.replaceAll("ACTION-NAME", viewActionName);
			headerLink = headerLink.replaceAll("PAGE-NAME", viewPageName);
			
			headerLinkContent.append(headerLink).append(LINE_SEPARATOR);
		}

		// Update header content
		String headerJspContent = headerPageTemplate.replaceAll("BASE-PACKAGE-NAME", basePackageName);
		headerJspContent = headerJspContent.replaceAll("MENU-ITEMS", headerLinkContent.toString());

		try {
			// Create header content file
			Path headerJspPath = webAppPath.resolve("Header.jsp");
			Files.writeString(headerJspPath, headerJspContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {
	
			throw new GeneratorException("Error creating system JSP file: " + ioException.getMessage());
		}
	}

	private void createViewEntitiesPage(Fragment fragment, List<Fragment> dependentFragments, 
			List<Relationship> sourceRelationships, List<Relationship> targetRelationships, 
			String parentName, String basePackageName, String configurationPackageName, Path fragmentWebPath) throws GeneratorException {

		// Get search filter and result attributes
		List<Attribute> filterAttributes = fragment.getSearchFilterAttributes();
		List<Attribute> resultAttributes = fragment.getSearchResultAttributes();

		// Add configuration package name
		String entitiesPage = entitiesPageTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);

		// Get entity name
		String fragmentName = fragment.getName();

		// Create camel case template variables
		String camelCaseEntityName = TextConverter.convertCommonToCamel(fragmentName, false);
		String pageTitle = TextConverter.convertToPlural(camelCaseEntityName);

		// Create constant case template variables
		String constantEntityName = TextConverter.convertCommonToConstant(fragmentName);
		String pageName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
		String attributeName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
		String idFieldName = constantEntityName + "_ID";
		String sortFieldFieldName = constantEntityName + "_SORT_FIELD";
		String sortDirectionFieldName = constantEntityName + "_SORT_DIRECTION";
		String viewActionName = "VIEW_" + TextConverter.convertToPlural(constantEntityName).toUpperCase();
		String addActionName = "ADD_" + constantEntityName;
		String editActionName = "EDIT_" + constantEntityName;
		String deleteActionName = "DELETE_" + constantEntityName;

		// Get first field name in constant form
		Attribute firstAttribute = resultAttributes.get(0);
		String firstFieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(firstAttribute.getName());

		// Set start of breadcrumb trail value
		String isStartOfTrail = parentName == null ? "true" : "false";

		// Set template variables
		entitiesPage = entitiesPage.replaceAll("PAGE-TITLE", pageTitle);
		entitiesPage = entitiesPage.replaceAll("PAGE-NAME", pageName);
		entitiesPage = entitiesPage.replaceAll("ATTRIBUTE-NAME", attributeName);
		entitiesPage = entitiesPage.replaceAll("ID-FIELD-NAME", idFieldName);
		entitiesPage = entitiesPage.replaceAll("EDIT-FIELD-NAME", firstFieldName);
		entitiesPage = entitiesPage.replaceAll("DEFAULT-SORT-FIELD-NAME", firstFieldName);
		entitiesPage = entitiesPage.replaceAll("VIEW-ACTION-NAME", viewActionName);
		entitiesPage = entitiesPage.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
		entitiesPage = entitiesPage.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);
		entitiesPage = entitiesPage.replaceAll("ADD-ACTION-NAME", addActionName);
		entitiesPage = entitiesPage.replaceAll("EDIT-ACTION-NAME", editActionName);
		entitiesPage = entitiesPage.replaceAll("DELETE-ACTION-NAME", deleteActionName);
		entitiesPage = entitiesPage.replaceAll("IS-START-OF-TRAIL", isStartOfTrail);

		// Initialize hidden field statements
		StringBuffer hiddenFieldStatements = new StringBuffer("");

		if (parentName != null && parentName.trim().length() > 0) {

			// Get parent entity ID field name
			String parentEntityCommonName = TextConverter.convertCamelToCommon(parentName);
			String parentEntityConstantName = TextConverter.convertCommonToConstant(parentEntityCommonName);
			String parentIdFieldName = parentEntityConstantName + "_ID";

			// Create hidden parent ID field statement
			String hiddenParentIdFieldStatement = hiddenFieldTemplate.replaceAll("FIELD-NAME", parentIdFieldName);

			// Add filter criteria statement
			hiddenFieldStatements.append(hiddenParentIdFieldStatement).append(LINE_SEPARATOR);
		}

		// Add hidden fields
		entitiesPage = entitiesPage.replaceAll("HIDDEN-FIELDS", hiddenFieldStatements.toString());

		if (filterAttributes == null || filterAttributes.isEmpty()) {

			// Clear filter data
			entitiesPage = entitiesPage.replaceAll("FILTER-DATA", "");
		}
		else {

			// Initialize filter field statements
			StringBuffer filterFieldStatements = new StringBuffer(LINE_SEPARATOR);

			for (Attribute filterAttribute : filterAttributes) {

				// Create common template variable
				String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttribute.getName()) + "_FILTER";

				// Get attribute type
				AttributeType type = filterAttribute.getType();

				if (type.equals(AttributeType.LOOKUP)) {

					// Get category ID
					String categoryId = TextConverter.convertCommonToConstant(filterAttribute.getLookupCategory());

					// Initialize filter field statement
					String filterFieldStatement = null;

					if (filterAttribute.getIsLookupMultipleValues()) {

						// Create multiple value filter field statement
						filterFieldStatement = lookupMultipleFieldTemplate.replaceAll("FIELD-NAME", fieldName);
						filterFieldStatement = filterFieldStatement.replaceAll("CATEGORY-ID", categoryId);
					} 
					else {

						// Create single value filter field statement
						filterFieldStatement = lookupFieldTemplate.replaceAll("FIELD-NAME", fieldName);
						filterFieldStatement = filterFieldStatement.replaceAll("CATEGORY-ID", categoryId);
					}

					// Add filter criteria statement
					filterFieldStatements.append(filterFieldStatement).append(LINE_SEPARATOR);
				} 
				else if (type.equals(AttributeType.BOOLEAN)) {

					// Create filter field statement
					String filterFieldStatement = booleanListFieldTemplate.replaceAll("FIELD-NAME", fieldName);

					// Add filter criteria statement
					filterFieldStatements.append(filterFieldStatement).append(LINE_SEPARATOR);
				}
				else {

					// Initialize max length
					String maxLength = null;

					if (type.equals(AttributeType.TEXT) ||
						type.equals(AttributeType.MEMO) ||
						type.equals(AttributeType.EMAIL) ||
						type.equals(AttributeType.PHONE) ||
						type.equals(AttributeType.POSTAL_CODE)) {

						maxLength = String.valueOf(filterAttribute.getTextMaxLength());
					}
					else if (type.equals(AttributeType.DATE)) {

						// Use standard length for dates
						maxLength = "10";
					}
					else if (type.equals(AttributeType.TIME)) {

						// Use standard length for time
						maxLength = "5";
					}
					else if (type.equals(AttributeType.NUMERIC)) {

						// Get precision and scale
						Integer precision = filterAttribute.getNumericPrecision();
						Integer scale = filterAttribute.getNumericScale();

						// Set max length based on precision and scale
						if (scale == null || scale.intValue() == 0) {
							
							maxLength = String.valueOf(precision);
						}
						else {
							
							maxLength = String.valueOf(precision + 1);
						}
					}

					// Create operator field name
					String operatorFieldName = fieldName + "_OPERATOR";

					// Initialize text mode and default operator name
					String textMode = "false";
					String defaultOperatorName = "EQUALS";

					if (type.equals(AttributeType.TEXT) || 
						type.equals(AttributeType.MEMO) ||
						type.equals(AttributeType.EMAIL) ||
						type.equals(AttributeType.PHONE) ||
						type.equals(AttributeType.POSTAL_CODE)) {

						// Set text mode and default operator name
						textMode = "true";
						defaultOperatorName = "CONTAINS";
					}

					// Initialize filter field statement
					String filterFieldStatement = null;

					// Set filter field statement template
					if (type.equals(AttributeType.DATE)) {

						filterFieldStatement = dateOperatorFieldTemplate;
					} 
					else if (type.equals(AttributeType.TIME)) {

						filterFieldStatement = timeOperatorFieldTemplate;
					} 
					else {

						filterFieldStatement = textOperatorFieldTemplate;
					}

					// Create filter field statement
					filterFieldStatement = filterFieldStatement.replaceAll("FIELD-NAME", fieldName);
					filterFieldStatement = filterFieldStatement.replaceAll("OPERATOR-NAME", operatorFieldName);
					filterFieldStatement = filterFieldStatement.replaceAll("TEXT-MODE", textMode);
					filterFieldStatement = filterFieldStatement.replaceAll("DEFAULT-NAME", defaultOperatorName);
					filterFieldStatement = filterFieldStatement.replaceAll("MAX-LENGTH", maxLength);

					// Add filter criteria statement
					filterFieldStatements.append(filterFieldStatement).append(LINE_SEPARATOR);
				}
			}

			// Create group state field ID
			String groupStateFieldId = constantEntityName + "_GROUP_STATE";

			// Create filter data definition
			String filterData = filterDataTemplate.replaceAll("FILTER-FIELDS", filterFieldStatements.toString());
			filterData = filterData.replaceAll("GROUP-STATE-ID", groupStateFieldId);
			filterData = filterData.replaceAll("VIEW-ACTION-NAME", viewActionName);
			filterData = filterData.replaceAll("PAGE-TITLE", pageTitle);

			// Add filter data to entities page
			entitiesPage = entitiesPage.replaceAll("FILTER-DATA", filterData);
		}

		if (!fragment.getIsPaginationSupported()) {

			// Clear paging header
			entitiesPage = entitiesPage.replaceAll("PAGING-HEADER", "<p/>");

			// Create individual breadcrumb actions
			String viewPageBreadcrumbAction = viewPageBreadcrumbActionTemplate.replaceAll("ACTION-ID", viewActionName);

			// Create and populate breadcrumb actions
			StringBuffer breadcrumbActions = new StringBuffer();
			breadcrumbActions.append(viewPageBreadcrumbAction).append(LINE_SEPARATOR);

			// Set breadcrumb actions template variables
			entitiesPage = entitiesPage.replaceAll("BREADCRUMB-ACTIONS", breadcrumbActions.toString());
		}
		else {

			// Create paging field names
			String currentPageNumberFieldName = constantEntityName + "_CURRENT_PAGE_NUMBER";
			String selectedPageNumberFieldName = constantEntityName + "_SELECTED_PAGE_NUMBER";

			// Create paging action names
			String previousPageActionName = "VIEW_" + constantEntityName + "_PREVIOUS_PAGE";
			String selectPageActionName = "VIEW_" + constantEntityName + "_SELECT_PAGE";
			String nextPageActionName = "VIEW_" + constantEntityName + "_NEXT_PAGE";

			// Set paging header template variables
			String pagingHeader = pagingHeaderTemplate.replaceAll("CURRENT-PAGE-NUMBER-FIELD-NAME", currentPageNumberFieldName);
			pagingHeader = pagingHeader.replaceAll("SELECTED-PAGE-NUMBER-FIELD-NAME", selectedPageNumberFieldName);
			pagingHeader = pagingHeader.replaceAll("PREVIOUS-PAGE-ACTION-NAME", previousPageActionName);
			pagingHeader = pagingHeader.replaceAll("SELECT-PAGE-ACTION-NAME", selectPageActionName);
			pagingHeader = pagingHeader.replaceAll("NEXT-PAGE-ACTION-NAME", nextPageActionName);

			// Set paging header template variable
			entitiesPage = entitiesPage.replaceAll("PAGING-HEADER", pagingHeader);

			// Create individual breadcrumb actions
			String viewPageBreadcrumbAction = viewPageBreadcrumbActionTemplate.replaceAll("ACTION-ID", viewActionName);
			String previousPageBreadcrumbAction = previousPageBreadcrumbActionTemplate.replaceAll("ACTION-ID", previousPageActionName);
			String selectPageBreadcrumbAction = selectPageBreadcrumbActionTemplate.replaceAll("ACTION-ID", selectPageActionName);
			String nextPageBreadcrumbAction = nextPageBreadcrumbActionTemplate.replaceAll("ACTION-ID", nextPageActionName);

			// Create and populate breadcrumb actions
			StringBuffer breadcrumbActions = new StringBuffer();
			breadcrumbActions.append(viewPageBreadcrumbAction).append(LINE_SEPARATOR);
			breadcrumbActions.append(previousPageBreadcrumbAction).append(LINE_SEPARATOR);
			breadcrumbActions.append(selectPageBreadcrumbAction).append(LINE_SEPARATOR);
			breadcrumbActions.append(nextPageBreadcrumbAction);

			// Set breadcrumb actions template variables
			entitiesPage = entitiesPage.replaceAll("BREADCRUMB-ACTIONS", breadcrumbActions.toString());
		}

		// Initialize column headers and columns
		StringBuffer columnHeaders = new StringBuffer();
		StringBuffer columns = new StringBuffer();

		for (Attribute resultAttribute : resultAttributes) {

			// Create field name
			String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(resultAttribute.getName());

			// Initialize category ID and multiple value indicator
			String categoryId = null;
			boolean isMultipleValues = false;

			// Get attribute type
			AttributeType type = resultAttribute.getType();

			if (type.equals(AttributeType.LOOKUP)) {

				// Get category ID
				categoryId = TextConverter.convertCommonToConstant(resultAttribute.getLookupCategory());
				isMultipleValues = resultAttribute.getIsLookupMultipleValues();
			}

			// Create column header
			String columnHeader = columnHeaderTemplate.replaceAll("VIEW-ACTION-NAME", viewActionName);
			columnHeader = columnHeader.replaceAll("DISPLAY-FIELD-NAME", fieldName);
			columnHeader = columnHeader.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
			columnHeader = columnHeader.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);

			// Append column header
			columnHeaders.append(columnHeader).append(LINE_SEPARATOR);

			// Avoid including field used for edit link
			if (!fieldName.equals(firstFieldName)) {

				// Initialize column
				String column = null;

				if (categoryId != null) {

					if (isMultipleValues) {

						// Create multiple value lookup column
						column = lookupMultipleColumnTemplate.replaceAll("FIELD-NAME", fieldName);
						column = column.replaceAll("CATEGORY-ID", categoryId);
					}
					else {

						// Create single value lookup column
						column = lookupColumnTemplate.replaceAll("FIELD-NAME", fieldName);
						column = column.replaceAll("CATEGORY-ID", categoryId);
					}
				}
				else {

					// Create column
					column = columnTemplate.replaceAll("FIELD-NAME", fieldName);
				}

				// Append column
				columns.append(column).append(LINE_SEPARATOR);
			}
		}
		
		// Create import statements
		StringBuffer importStatements = new StringBuffer();
		
		if (sourceRelationships != null) {
			
			for (Relationship relationship : sourceRelationships) {
				
				if (relationship.getIsTargetIncludedInView()) {
					
					// Update view entities page based on source relationship
					updateViewEntitiesPage(relationship, basePackageName, importStatements, columnHeaders, columns, true);
				}
			}
		}
		
		if (targetRelationships != null) {
			
			for (Relationship relationship : targetRelationships) {
				
				if (relationship.getIsBidirectional() && relationship.getIsSourceIncludedInView()) {
					
					// Update view entities page based on target relationship
					updateViewEntitiesPage(relationship, basePackageName, importStatements, columnHeaders, columns, false);
				}
			}
		}

		// Update view entities page based on dependent fragments
		updateViewEntitiesPage(fragmentName, dependentFragments, columnHeaders, columns);
		
		// Set page template variables
		entitiesPage = entitiesPage.replaceAll("IMPORTS", importStatements.toString());
		entitiesPage = entitiesPage.replaceAll("COLUMN-HEADERS", columnHeaders.toString());
		entitiesPage = entitiesPage.replaceAll("COLUMNS", columns.toString());

		try {
			// Create entities page file name
			String entitiesPageFileName = pageTitle + ".jsp";

			// Create view entities JSP file
			Path entitiesPageFilePath = fragmentWebPath.resolve(entitiesPageFileName);
			Files.writeString(entitiesPageFilePath, entitiesPage, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating view entities JSP file: " + ioException.getMessage());
		}
	}

	private void updateViewEntitiesPage(String parentEntityName, List<Fragment> dependentFragments, StringBuffer columnHeaders, StringBuffer columns) {

		if (dependentFragments != null && !dependentFragments.isEmpty()) {
			
			// Add column header to support dependent menu column
			columnHeaders.append("          <core:td>&nbsp;</core:td>").append(LINE_SEPARATOR);
			
			// Create dependent links string buffer
			StringBuffer dependentLinks = new StringBuffer();
			
			for (Fragment dependentFragment : dependentFragments) {
				
				// Get entity name
				String dependentFragmentName = dependentFragment.getName();

				// Create view action name
				String constantEntityName = TextConverter.convertCommonToConstant(dependentFragmentName);
				String viewActionName = "VIEW_" + TextConverter.convertToPlural(constantEntityName).toUpperCase();

				// Create dependent page name
				String dependentPageName = TextConverter.convertToPlural(constantEntityName).toUpperCase();

				// Get parent entity ID field name
				String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
				String parentEntityConstantName = TextConverter.convertCommonToConstant(parentEntityCommonName);
				String parentIdFieldName = parentEntityConstantName + "_ID";

				// Build dependent link
				String dependentLink = dependentLinkTemplate.replaceAll("DEPENDENT-ACTION-NAME", viewActionName);
				dependentLink = dependentLink.replaceAll("DEPENDENT-PAGE-NAME", dependentPageName);
				dependentLink = dependentLink.replaceAll("PARENT-ID-FIELD-NAME", parentIdFieldName);

				// Add dependent link to buffer
				dependentLinks.append(dependentLink).append(LINE_SEPARATOR);
			}
			
			// Build dependent menu
			String dependentMenu = dependentMenuTemplate.replace("DEPENDENT-LINKS", dependentLinks.toString());
			
			// Add dependent menu to columns
			columns.append(dependentMenu).append(LINE_SEPARATOR);
		}
	}

	public void updateViewEntitiesPage(Relationship relationship, String basePackageName, StringBuffer importStatements, 
			StringBuffer columnHeaders, StringBuffer columns, boolean isSourceRelationship) {

		// Get source data object name
		String sourceDataObjectName = relationship.getSourceFragmentName();

		// Get target data object name
		String targetDataObjectName = relationship.getTargetFragmentName();
		String targetAttributeName = relationship.getTargetAttributeName();

		// Get target sub-package and package names
		String targetSubPackageName = TextConverter.convertCommonToCamel(targetDataObjectName, true);
		String targetPackageName = basePackageName + "." + targetSubPackageName;
		
		// Set many-to-many indicator
		boolean isManyToManyAssociation = relationship.getAssociation().equals(AssociationType.MANY_TO_MANY);

		updateViewEntitiesPage(sourceDataObjectName, targetPackageName,
				targetDataObjectName, targetAttributeName, isManyToManyAssociation,
				importStatements, columnHeaders, columns);
	}

	private void updateViewEntitiesPage(String primaryEntityName, String secondaryPackageName,
			String secondaryEntityName, String secondaryAttributeName, boolean isManyToManyAssociation,
			StringBuffer imports, StringBuffer columnHeaders, StringBuffer columns) {

		// Convert to primary base names
		String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
		String primaryConstantName = TextConverter.convertCommonToConstant(primaryCommonName);

		// Convert to secondary base names
		String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
		String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);
		String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, false);
		String secondaryAttributeConstantName = TextConverter.convertCommonToConstant(secondaryAttributeName);

		// Create template variables
		String viewActionName = "VIEW_" + TextConverter.convertToPlural(primaryConstantName).toUpperCase();
		String associateComponentType = "associate";
		String associateFieldId = primaryConstantName + "_" + secondaryConstantName + "_ID";
		String managerClassName = secondaryUpperCaseCamel + "Manager";
		String displayFieldId = secondaryConstantName + "_" + secondaryAttributeConstantName;
		String sortFieldFieldName = primaryConstantName + "_SORT_FIELD";
		String sortDirectionFieldName = primaryConstantName + "_SORT_DIRECTION";

		if (isManyToManyAssociation) {

			// Pluralize template variables
			associateComponentType = associateComponentType + "List";
			associateFieldId = associateFieldId + "S";
		}

		// Create data manager import statement
		String importStatement = "<%@ page import=\"" + secondaryPackageName + "." + managerClassName + "\" %>";

		// Append associate data manager import
		imports.append(importStatement).append(LINE_SEPARATOR);

		// Create column header
		String columnHeader = columnHeaderTemplate.replaceAll("VIEW-ACTION-NAME", viewActionName);
		columnHeader = columnHeader.replaceAll("DISPLAY-FIELD-NAME", associateFieldId);
		columnHeader = columnHeader.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
		columnHeader = columnHeader.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);

		// Append column header
		columnHeaders.append(columnHeader).append(LINE_SEPARATOR);

		// Initialize associate display field
		String associateDisplayField = isManyToManyAssociation ? associateDisplayListFieldTemplate : associateDisplayFieldTemplate;

		// Build associate display field
		associateDisplayField = associateDisplayField.replaceAll("ASSOCIATE-FIELD-ID", associateFieldId);
		associateDisplayField = associateDisplayField.replaceAll("ATTRIBUTE-NAME", primaryConstantName);
		associateDisplayField = associateDisplayField.replaceAll("MANAGER-CLASS-NAME", managerClassName);
		associateDisplayField = associateDisplayField.replaceAll("DISPLAY-FIELD-ID", displayFieldId);

		// Append associate display column
		columns.append(associateDisplayField).append(LINE_SEPARATOR);
	}
	
	private void createSelectEntitiesPage(Fragment fragment, List<Relationship> sourceRelationships, List<Relationship> targetRelationships, 
			String parentName, String basePackageName, String configurationPackageName, Path fragmentWebPath) throws GeneratorException {

		// Get filter and column attribute names
		List<Attribute> filterAttributes = fragment.getSearchFilterAttributes();
		List<Attribute> resultAttributes = fragment.getSearchResultAttributes();

		// Add configuration package name
		String selectEntitiesPage = selectEntitiesPageTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);

		// Get entity name
		String entityName = fragment.getName();

		// Create base names
		String constantEntityName = TextConverter.convertCommonToConstant(entityName);
		String pluralConstantEntityName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
		String camelCaseEntityName = TextConverter.convertCommonToCamel(entityName, false);

		// Create template variables
		String pageId = constantEntityName + "_" + "SELECTION";
		String formId = camelCaseEntityName + "Selection";
		String attributeName = pluralConstantEntityName;
		String selectedAttributeName = "SELECTED" + "_" + pluralConstantEntityName;
		String sortFieldFieldName = constantEntityName + "_SORT_FIELD";
		String sortDirectionFieldName = constantEntityName + "_SORT_DIRECTION";
		String selectActionName = "SELECT_" + pluralConstantEntityName;
		String addActionName = "SELECT_" + constantEntityName + "_ADD";
		String removeActionName = "SELECT_" + constantEntityName + "_REMOVE";
		String closeActionName = "SELECT_" + constantEntityName + "_CLOSE";

		// Get first field name in constant form
		Attribute firstAttribute = resultAttributes.get(0);
		String firstFieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(firstAttribute.getName());

		// Set template variables
		selectEntitiesPage = selectEntitiesPage.replaceAll("PAGE-ID", pageId);
		selectEntitiesPage = selectEntitiesPage.replaceAll("FORM-ID", formId);
		selectEntitiesPage = selectEntitiesPage.replaceAll("AVAILABLE-ATTRIBUTE-NAME", attributeName);
		selectEntitiesPage = selectEntitiesPage.replaceAll("SELECTED-ATTRIBUTE-NAME", selectedAttributeName);
		selectEntitiesPage = selectEntitiesPage.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
		selectEntitiesPage = selectEntitiesPage.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);
		selectEntitiesPage = selectEntitiesPage.replaceAll("SELECT-ACTION-NAME", selectActionName);
		selectEntitiesPage = selectEntitiesPage.replaceAll("SELECT-ADD-ACTION-NAME", addActionName);
		selectEntitiesPage = selectEntitiesPage.replaceAll("SELECT-REMOVE-ACTION-NAME", removeActionName);
		selectEntitiesPage = selectEntitiesPage.replaceAll("SELECT-CLOSE-ACTION-NAME", closeActionName);
		selectEntitiesPage = selectEntitiesPage.replaceAll("SORT-FIELD-NAME", firstFieldName);

		if (filterAttributes == null || filterAttributes.isEmpty()) {

			// Clear filter data
			selectEntitiesPage = selectEntitiesPage.replaceAll("FILTER-DATA", "");
		} 
		else {

			// Initialize filter field statements
			StringBuffer filterFieldStatements = new StringBuffer(LINE_SEPARATOR);

			for (Attribute filterAttribute : filterAttributes) {

				// Create common template variable
				String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttribute.getName()) + "_FILTER";

				// Get attribute type
				AttributeType type = filterAttribute.getType();

				if (type.equals(AttributeType.LOOKUP)) {

					// Get category ID
					String categoryId = TextConverter.convertCommonToConstant(filterAttribute.getLookupCategory());

					// Initialize filter field statement
					String filterFieldStatement = null;

					if (filterAttribute.getIsLookupMultipleValues()) {

						// Create multiple value filter field statement
						filterFieldStatement = lookupMultipleFieldTemplate.replaceAll("FIELD-NAME", fieldName);
						filterFieldStatement = filterFieldStatement.replaceAll("CATEGORY-ID", categoryId);
					} 
					else {

						// Create single value filter field statement
						filterFieldStatement = lookupFieldTemplate.replaceAll("FIELD-NAME", fieldName);
						filterFieldStatement = filterFieldStatement.replaceAll("CATEGORY-ID", categoryId);
					}

					// Add filter criteria statement
					filterFieldStatements.append(filterFieldStatement).append(LINE_SEPARATOR);
				} 
				else if (type.equals(AttributeType.BOOLEAN)) {

					// Create filter field statement
					String filterFieldStatement = booleanListFieldTemplate.replaceAll("FIELD-NAME", fieldName);

					// Add filter criteria statement
					filterFieldStatements.append(filterFieldStatement).append(LINE_SEPARATOR);
				} 
				else {

					// Initialize max length
					String maxLength = null;

					if (type.equals(AttributeType.TEXT) ||
						type.equals(AttributeType.MEMO) ||
						type.equals(AttributeType.EMAIL) ||
						type.equals(AttributeType.PHONE) ||
						type.equals(AttributeType.POSTAL_CODE)) {

						maxLength = String.valueOf(filterAttribute.getTextMaxLength());
					}
					else if (type.equals(AttributeType.DATE)) {

						// Use standard length for dates
						maxLength = "10";
					}
					else if (type.equals(AttributeType.TIME)) {

						// Use standard length for time
						maxLength = "5";
					}
					else if (type.equals(AttributeType.NUMERIC)) {

						// Get precision and scale
						Integer precision = filterAttribute.getNumericPrecision();
						Integer scale = filterAttribute.getNumericScale();

						// Set max length based on precision and scale
						if (scale == null || scale.intValue() == 0) {
							
							maxLength = String.valueOf(precision);
						}
						else {
							
							maxLength = String.valueOf(precision + 1);
						}
					}

					// Create operator field name
					String operatorFieldName = fieldName + "_OPERATOR";

					// Initialize text mode and default operator name
					String textMode = "false";
					String defaultOperatorName = "EQUALS";

					if (type.equals(AttributeType.TEXT) ||
						type.equals(AttributeType.MEMO) ||
						type.equals(AttributeType.EMAIL) ||
						type.equals(AttributeType.PHONE) ||
						type.equals(AttributeType.POSTAL_CODE)) {

						// Set text mode and default operator name
						textMode = "true";
						defaultOperatorName = "CONTAINS";
					}

					// Initialize filter field statement
					String filterFieldStatement = null;

					// Set filter field statement template
					if (type.equals(AttributeType.DATE)) {

						filterFieldStatement = dateOperatorFieldTemplate;
					} 
					else if (type.equals(AttributeType.TIME)) {

						filterFieldStatement = timeOperatorFieldTemplate;
					} 
					else {

						filterFieldStatement = textOperatorFieldTemplate;
					}

					// Create filter field statement
					filterFieldStatement = filterFieldStatement.replaceAll("FIELD-NAME", fieldName);
					filterFieldStatement = filterFieldStatement.replaceAll("OPERATOR-NAME", operatorFieldName);
					filterFieldStatement = filterFieldStatement.replaceAll("TEXT-MODE", textMode);
					filterFieldStatement = filterFieldStatement.replaceAll("DEFAULT-NAME", defaultOperatorName);
					filterFieldStatement = filterFieldStatement.replaceAll("MAX-LENGTH", maxLength);

					// Add filter criteria statement
					filterFieldStatements.append(filterFieldStatement).append(LINE_SEPARATOR);
				}
			}

			// Create group state field ID
			String groupStateFieldId = constantEntityName + "_GROUP_STATE";

			// Create filter data definition
			String filterData = filterDataTemplate.replaceAll("FILTER-FIELDS", filterFieldStatements.toString());
			filterData = filterData.replaceAll("GROUP-STATE-ID", groupStateFieldId);
			filterData = filterData.replaceAll("VIEW-ACTION-NAME", selectActionName);
			filterData = filterData.replaceAll("PAGE-TITLE", formId);

			// Add filter data to entities page
			selectEntitiesPage = selectEntitiesPage.replaceAll("FILTER-DATA", filterData);
		}

		if (!fragment.getIsPaginationSupported()) {

			// Clear paging header
			selectEntitiesPage = selectEntitiesPage.replaceAll("PAGING-HEADER", "<p/>");
		} 
		else {

			// Create paging field names
			String currentPageNumberFieldName = constantEntityName + "_CURRENT_PAGE_NUMBER";
			String selectedPageNumberFieldName = constantEntityName + "_SELECTED_PAGE_NUMBER";

			// Create paging action names
			String previousPageActionName = "SELECT_" + constantEntityName + "_PREVIOUS_PAGE";
			String selectPageActionName = "SELECT_" + constantEntityName + "_SELECT_PAGE";
			String nextPageActionName = "SELECT_" + constantEntityName + "_NEXT_PAGE";

			// Set paging header template variables
			String pagingHeader = pagingHeaderTemplate.replaceAll("CURRENT-PAGE-NUMBER-FIELD-NAME",
					currentPageNumberFieldName);
			pagingHeader = pagingHeader.replaceAll("SELECTED-PAGE-NUMBER-FIELD-NAME", selectedPageNumberFieldName);
			pagingHeader = pagingHeader.replaceAll("PREVIOUS-PAGE-ACTION-NAME", previousPageActionName);
			pagingHeader = pagingHeader.replaceAll("SELECT-PAGE-ACTION-NAME", selectPageActionName);
			pagingHeader = pagingHeader.replaceAll("NEXT-PAGE-ACTION-NAME", nextPageActionName);

			// Set paging header template variable
			selectEntitiesPage = selectEntitiesPage.replaceAll("PAGING-HEADER", pagingHeader);
		}

		// Initialize column headers
		StringBuffer availableColumnHeaders = new StringBuffer();
		StringBuffer selectedColumnHeaders = new StringBuffer();

		// Initialize columns
		StringBuffer columns = new StringBuffer();

		// Create selection column
		String idFieldName = constantEntityName + "_ID";
		String idColumn = columnSelectTemplate.replaceAll("FIELD-NAME", idFieldName);

		// Append column
		columns.append(idColumn).append(LINE_SEPARATOR);

		for (Attribute resultAttribute : resultAttributes) {

			// Create field name
			String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(resultAttribute.getName());

			// Initialize category ID and multiple value indicator
			String categoryId = null;
			boolean isMultipleValues = false;

			// Get attribute type
			AttributeType type = resultAttribute.getType();

			if (type.equals(AttributeType.LOOKUP)) {

				// Get category ID
				categoryId = TextConverter.convertCommonToConstant(resultAttribute.getLookupCategory());
				isMultipleValues = resultAttribute.getIsLookupMultipleValues();
			}

			// Create available column header
			String availableColumnHeader = columnHeaderTemplate.replaceAll("VIEW-ACTION-NAME", selectActionName);
			availableColumnHeader = availableColumnHeader.replaceAll("DISPLAY-FIELD-NAME", fieldName);
			availableColumnHeader = availableColumnHeader.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
			availableColumnHeader = availableColumnHeader.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);

			// Append available column header
			availableColumnHeaders.append(availableColumnHeader).append(LINE_SEPARATOR);

			// Create selected column header
			String selectedColumnHeader = selectColumnHeaderTemplate.replaceAll("FIELD-NAME", fieldName);

			// Append selected column header
			selectedColumnHeaders.append(selectedColumnHeader).append(LINE_SEPARATOR);

			// Initialize column
			String column = null;

			if (categoryId != null) {

				if (isMultipleValues) {

					// Create multiple value lookup column
					column = lookupMultipleColumnTemplate.replaceAll("FIELD-NAME", fieldName);
					column = column.replaceAll("CATEGORY-ID", categoryId);
				} 
				else {

					// Create single value lookup column
					column = lookupColumnTemplate.replaceAll("FIELD-NAME", fieldName);
					column = column.replaceAll("CATEGORY-ID", categoryId);
				}
			} 
			else {

				// Create column
				column = columnTemplate.replaceAll("FIELD-NAME", fieldName);
			}

			// Append column
			columns.append(column).append(LINE_SEPARATOR);
		}

		// Set page template variables
		selectEntitiesPage = selectEntitiesPage.replaceAll("AVAILABLE-COLUMN-HEADERS", availableColumnHeaders.toString());
		selectEntitiesPage = selectEntitiesPage.replaceAll("SELECTED-COLUMN-HEADERS", selectedColumnHeaders.toString());
		selectEntitiesPage = selectEntitiesPage.replaceAll("COLUMNS", columns.toString());

		try {
			// Create entities page file name
			String selectEntitiesPageFileName = formId + ".jsp";

			// Create view entities JSP file
			Path selectEntitiesPageFilePath = fragmentWebPath.resolve(selectEntitiesPageFileName);
			Files.writeString(selectEntitiesPageFilePath, selectEntitiesPage, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating view entities JSP file: " + ioException.getMessage());
		}
	}

	private void createEditEntityPage(Fragment fragment, List<Relationship> sourceRelationships, List<Relationship> targetRelationships, 
			String parentName, String basePackageName, String configurationPackageName, Path fragmentWebPath) throws GeneratorException {

		// Get attributes
		List<Attribute> attributes = fragment.getAttributes();

		// Add configuration package name
		String entityPage = entityPageTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);

		// Get entity name
		String entityName = fragment.getName();

		// Create template variables
		String pageTitle = TextConverter.convertCommonToCamel(entityName, false);
		String dataObjectName = TextConverter.convertCommonToCamel(entityName, false);
		String packageName = basePackageName + "." + dataObjectName;
		String constantEntityName = TextConverter.convertCommonToConstant(entityName);
		String constantPluralEntityName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
		String pageName = constantEntityName;
		String attributeName = constantEntityName;
		String idFieldName = constantEntityName + "_ID";
		String versionFieldName = constantEntityName + "_VERSION";
		String currentPageNumberFieldName = constantEntityName + "_CURRENT_PAGE_NUMBER";
		String sortFieldFieldName = constantEntityName + "_SORT_FIELD";
		String sortDirectionFieldName = constantEntityName + "_SORT_DIRECTION";
		String viewActionName = "VIEW_" + constantPluralEntityName;
		String saveActionName = "SAVE_" + constantEntityName;
		String cancelActionName = "CANCEL_" + constantEntityName;

		// Set template variables
		entityPage = entityPage.replaceAll("ENTITY-PACKAGE-NAME", packageName);
		entityPage = entityPage.replaceAll("ENTITY-CLASS-NAME", dataObjectName);
		entityPage = entityPage.replaceAll("PAGE-TITLE", pageTitle);
		entityPage = entityPage.replaceAll("PAGE-NAME", pageName);
		entityPage = entityPage.replaceAll("ATTRIBUTE-NAME", attributeName);
		entityPage = entityPage.replaceAll("ID-FIELD-NAME", idFieldName);
		entityPage = entityPage.replaceAll("VERSION-FIELD-NAME", versionFieldName);
		entityPage = entityPage.replaceAll("CURRENT-PAGE-NUMBER", currentPageNumberFieldName);
		entityPage = entityPage.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
		entityPage = entityPage.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);
		entityPage = entityPage.replaceAll("VIEW-ACTION-NAME", viewActionName);
		entityPage = entityPage.replaceAll("SAVE-ACTION-NAME", saveActionName);
		entityPage = entityPage.replaceAll("CANCEL-ACTION-NAME", cancelActionName);
		entityPage = entityPage.replaceAll("PARENT-PAGE-ID", pageName);
		entityPage = entityPage.replaceAll("PARENT-DATA-OBJECT-CLASS-NAME", dataObjectName);

		// Initialize hidden filter field statements
		StringBuffer hiddenFilterFieldStatements = new StringBuffer("");

		// Get parent entity name
		String parentEntityName = fragment.getParentName();
		
		if (parentEntityName != null && parentEntityName.trim().length() > 0) {

			// Get parent entity ID field name
			String parentEntityConstantName = TextConverter.convertCommonToConstant(parentEntityName);
			String parentIdFieldName = parentEntityConstantName + "_ID";

			// Create hidden parent ID field statement
			String hiddenParentIdFieldStatement = hiddenFieldTemplate.replaceAll("FIELD-NAME", parentIdFieldName);

			// Add filter criteria statement
			hiddenFilterFieldStatements.append(hiddenParentIdFieldStatement).append(LINE_SEPARATOR);
		}

		// Add hidden fields to entities page
		entityPage = entityPage.replaceAll("HIDDEN-FIELDS", hiddenFilterFieldStatements.toString());

		// Initialize field statements
		StringBuffer fieldStatements = new StringBuffer();

		for (Attribute attribute : attributes) {

			// Get attribute properties
			String name = attribute.getName();
			AttributeType type = attribute.getType();

			// Create field name template variable
			String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(name);

			if (type.equals(AttributeType.LOOKUP)) {

				// Get category ID
				String categoryId = TextConverter.convertCommonToConstant(attribute.getLookupCategory());

				// Initialize field statement
				String fieldStatement = null;

				if (attribute.getIsLookupMultipleValues()) {

					// Create multiple value field statement
					fieldStatement = lookupMultipleEntityFieldTemplate.replaceAll("FIELD-NAME", fieldName);
					fieldStatement = fieldStatement.replaceAll("ATTRIBUTE-NAME", attributeName);
					fieldStatement = fieldStatement.replaceAll("CATEGORY-ID", categoryId);
				} 
				else {

					// Create single value field statement
					fieldStatement = lookupEntityFieldTemplate.replaceAll("FIELD-NAME", fieldName);
					fieldStatement = fieldStatement.replaceAll("ATTRIBUTE-NAME", attributeName);
					fieldStatement = fieldStatement.replaceAll("CATEGORY-ID", categoryId);
				}

				// Add field statement
				fieldStatements.append(fieldStatement).append(LINE_SEPARATOR);
			} 
			else if (type.equals(AttributeType.BOOLEAN)) {

				// Create field statement
				String fieldStatement = booleanEntityFieldTemplate.replaceAll("FIELD-NAME", fieldName);
				fieldStatement = fieldStatement.replaceAll("ATTRIBUTE-NAME", attributeName);

				// Add field statement
				fieldStatements.append(fieldStatement).append(LINE_SEPARATOR);
			} 
			else {

				// Initialize template and max length
				String fieldTemplate = null;
				String maxLength = null;

				// Get attribute type specific max length
				if (type.equals(AttributeType.TEXT)) {

					fieldTemplate = textFieldTemplate;

					maxLength = String.valueOf(attribute.getTextMaxLength());
				} 
				else if (type.equals(AttributeType.MEMO)) {

					fieldTemplate = memoFieldTemplate;

					maxLength = String.valueOf(attribute.getTextMaxLength());
				} 
				else if (type.equals(AttributeType.EMAIL)) {

					fieldTemplate = emailFieldTemplate;

					maxLength = String.valueOf(attribute.getTextMaxLength());
				} 
				else if (type.equals(AttributeType.PHONE)) {

					fieldTemplate = phoneNumberFieldTemplate;

					maxLength = String.valueOf(attribute.getTextMaxLength());
				} 
				else if (type.equals(AttributeType.POSTAL_CODE)) {

					fieldTemplate = textFieldTemplate;

					maxLength = String.valueOf(attribute.getTextMaxLength());
				} 
				else if (type.equals(AttributeType.NUMERIC)) {

					fieldTemplate = textFieldTemplate;

					int numericLength = attribute.getNumericPrecision();

					if (attribute.getNumericScale() > 0) {
						numericLength = numericLength + attribute.getNumericScale() + 1;
					}

					maxLength = String.valueOf(numericLength);
				} 
				else if (type.equals(AttributeType.DATE)) {

					fieldTemplate = dateFieldTemplate;

					maxLength = "10";
				} 
				else if (type.equals(AttributeType.TIME)) {

					fieldTemplate = timeFieldTemplate;

					maxLength = "5";
				}

				// Create field statement
				String fieldStatement = fieldTemplate.replaceAll("FIELD-NAME", fieldName);
				fieldStatement = fieldStatement.replaceAll("ATTRIBUTE-NAME", attributeName);
				fieldStatement = fieldStatement.replaceAll("MAX-LENGTH", maxLength);

				// Add field statement
				fieldStatements.append(fieldStatement).append(LINE_SEPARATOR);
			}
		}
		
		// Create import statements
		StringBuffer importStatements = new StringBuffer();
		
		if (sourceRelationships != null) {
			
			for (Relationship relationship : sourceRelationships) {
				
				// Update edit entity page based on source relationship
				updateEditEntityPage(relationship, basePackageName, importStatements, fieldStatements, true);
			}
		}
		
		if (targetRelationships != null) {
			
			for (Relationship relationship : targetRelationships) {
				
				if (relationship.getIsBidirectional()) {
					
					// Update edit entity page based on target relationship
					updateEditEntityPage(relationship, basePackageName, importStatements, fieldStatements, false);
				}
			}
		}
		
		// Set page template variables
		entityPage = entityPage.replaceAll("IMPORTS", importStatements.toString());
		entityPage = entityPage.replaceAll("FIELDS", fieldStatements.toString());

		try {
			// Create edit entity page file name
			String entityPageFileName = pageTitle + ".jsp";

			// Create edit entity JSP file
			Path entityPageFilePath = fragmentWebPath.resolve(entityPageFileName);
			Files.writeString(entityPageFilePath, entityPage, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating edit entity JSP file: " + ioException.getMessage());
		}
	}
	
	public void updateEditEntityPage(Relationship relationship, String basePackageName, StringBuffer importStatements, 
			StringBuffer fieldStatements, boolean isSourceRelationship) {

		// Get source data object name
		String sourceDataObjectName = relationship.getSourceFragmentName();

		// Get target data object name
		String targetDataObjectName = relationship.getTargetFragmentName();
		String targetAttributeName = relationship.getTargetAttributeName();

		// Get target sub-package and package names
		String targetSubPackageName = TextConverter.convertCommonToCamel(targetDataObjectName, true);
		String targetPackageName = basePackageName + "." + targetSubPackageName;
		
		// Set many-to-many indicator
		boolean isManyToManyAssociation = relationship.getAssociation().equals(AssociationType.MANY_TO_MANY);

		updateEditEntityPage(sourceDataObjectName, targetPackageName,
				targetDataObjectName, targetAttributeName, isManyToManyAssociation,
				importStatements, fieldStatements);
	}

	private void updateEditEntityPage(String primaryEntityName, String secondaryPackageName,
			String secondaryEntityName, String secondaryAttributeName, boolean isManyToManyAssociation,
			StringBuffer imports, StringBuffer fieldStatements) {
		
		// Convert to primary base names
		String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
		String primaryConstantName = TextConverter.convertCommonToConstant(primaryCommonName);

		// Convert to secondary base names
		String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
		String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);
		String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, false);
		String secondaryPluralName = TextConverter.convertToPlural(secondaryCommonName);
		String secondaryPluralConstantName = TextConverter.convertCommonToConstant(secondaryPluralName);
		String secondaryAttributeConstantName = TextConverter.convertCommonToConstant(secondaryAttributeName);

		// Create template variables
		String associateComponentType = "associate";
		String associateFieldId = primaryConstantName + "_" + secondaryConstantName + "_ID";
		String managerClassName = secondaryUpperCaseCamel + "Manager";
		String selectActionName = "SELECT_" + secondaryPluralConstantName;
		String displayFieldId = secondaryConstantName + "_" + secondaryAttributeConstantName;

		if (isManyToManyAssociation) {
	
			// Pluralize template variables
			associateComponentType = associateComponentType + "List";
			associateFieldId = associateFieldId + "S";
		}

		// Create data manager import statement
		String importStatement = "<%@ page import=\"" + secondaryPackageName + "." + managerClassName + "\" %>";

		// Append associate data manager import
		imports.append(importStatement).append(LINE_SEPARATOR);

		// Build associate list field
		String associateListField = associateListFieldTemplate.replaceAll("ASSOCIATE-COMPONENT-TYPE", associateComponentType);
		associateListField = associateListField.replaceAll("ASSOCIATE-FIELD-ID", associateFieldId);
		associateListField = associateListField.replaceAll("ATTRIBUTE-NAME", primaryConstantName);
		associateListField = associateListField.replaceAll("MANAGER-CLASS-NAME", managerClassName);
		associateListField = associateListField.replaceAll("DISPLAY-FIELD-ID", displayFieldId);
		associateListField = associateListField.replaceAll("ACTION-ID", selectActionName);

		// Append associate data manager import
		fieldStatements.append(associateListField).append(LINE_SEPARATOR);
	}

}