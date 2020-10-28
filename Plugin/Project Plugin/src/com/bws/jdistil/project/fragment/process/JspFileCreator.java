package com.bws.jdistil.project.fragment.process;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;

import com.bws.jdistil.project.fragment.DefinitionWizardData;
import com.bws.jdistil.project.fragment.RelationshipWizardData;
import com.bws.jdistil.project.fragment.ViewWizardData;
import com.bws.jdistil.project.fragment.data.Attribute;
import com.bws.jdistil.project.fragment.data.AttributeTypes;
import com.bws.jdistil.project.fragment.data.EmailAttribute;
import com.bws.jdistil.project.fragment.data.LookupAttribute;
import com.bws.jdistil.project.fragment.data.MemoAttribute;
import com.bws.jdistil.project.fragment.data.NumericAttribute;
import com.bws.jdistil.project.fragment.data.PhoneNumberAttribute;
import com.bws.jdistil.project.fragment.data.PostalCodeAttribute;
import com.bws.jdistil.project.fragment.data.TextAttribute;
import com.bws.jdistil.project.util.ResourceFinder;
import com.bws.jdistil.project.util.ResourceReader;
import com.bws.jdistil.project.util.TextConverter;

/**
 * Creates JSP files supporting the new entity defined in the application fragment wizard.
 * @author Bryan Snipes
 */
public class JspFileCreator {

  /**
   * Line separator.
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Resource reader.
   */
  private static ResourceReader resourceReader = new ResourceReader();
  
  /**
   * Associate list field template.
   */
  private static String associateListFieldTemplate = null;
  
  /**
   * Associate display field template.
   */
  private static String associateDisplayFieldTemplate = null;
  
  /**
   * Associate display list field template.
   */
  private static String associateDisplayListFieldTemplate = null;
  
  /**
   * Boolean entity field template.
   */
  private static String booleanEntityFieldTemplate = null;
  
  /**
   * Boolean list field template.
   */
  private static String booleanListFieldTemplate = null;
  
  /**
   * Column header template.
   */
  private static String columnHeaderTemplate = null;
  
  /**
   * Select column header template.
   */
  private static String selectColumnHeaderTemplate = null;
  
  /**
   * Column template.
   */
  private static String columnTemplate = null;
  
  /**
   * Column select template.
   */
  private static String columnSelectTemplate = null;
  
  /**
   * Dependent menu template.
   */
  private static String dependentMenuTemplate = null;
  
  /**
   * Dependent link template.
   */
  private static String dependentLinkTemplate = null;
  
  /**
   * Entities page template.
   */
  private static String entitiesPageTemplate = null;
  
  /**
   * Select etities page template.
   */
  private static String selectEntitiesPageTemplate = null;
  
  /**
   * Entity page template.
   */
  private static String entityPageTemplate = null;
  
  /**
   * Filter data template.
   */
  private static String filterDataTemplate = null;
  
  /**
   * Header link template.
   */
  private static String headerLinkTemplate = null;
  
  /**
   * Hidden field template.
   */
  private static String hiddenFieldTemplate = null;
  
  /**
   * Lookup column template.
   */
  private static String lookupColumnTemplate = null;
  
  /**
   * Lookup entity field template.
   */
  private static String lookupEntityFieldTemplate = null;
  
  /**
   * Lookup field template.
   */
  private static String lookupFieldTemplate = null;
  
  /**
   * Lookup multiple column template.
   */
  private static String lookupMultipleColumnTemplate = null;
  
  /**
   * Lookup multiple entity field template.
   */
  private static String lookupMultipleEntityFieldTemplate = null;
  
  /**
   * Lookup multiple field template.
   */
  private static String lookupMultipleFieldTemplate = null;
  
  /**
   * Next page breadcrumb action template.
   */
  private static String nextPageBreadcrumbActionTemplate = null;
  
  /**
   * Paging header template.
   */
  private static String pagingHeaderTemplate = null;
  
  /**
   * Previous page breadcrumb action template.
   */
  private static String previousPageBreadcrumbActionTemplate = null;
  
  /**
   * Select page breadcrumb action template.
   */
  private static String selectPageBreadcrumbActionTemplate = null;
  
  /**
   * Text field template.
   */
  private static String textFieldTemplate = null;
  
  /**
   * Memo field template.
   */
  private static String memoFieldTemplate = null;
  
  /**
   * Email field template.
   */
  private static String emailFieldTemplate = null;
  
  /**
   * Phone number field template.
   */
  private static String phoneNumberFieldTemplate = null;
  
  /**
   * Date field template.
   */
  private static String dateFieldTemplate = null;
  
  /**
   * Time field template.
   */
  private static String timeFieldTemplate = null;
  
  /**
   * View page breadcrumb action template.
   */
  private static String viewPageBreadcrumbActionTemplate = null;
  
  /**
   * Text operator field template.
   */
  private static String textOperatorFieldTemplate = null;
  
  /**
   * Date operator field template.
   */
  private static String dateOperatorFieldTemplate = null;
  
  /**
   * Email operator field template.
   */
  private static String emailOperatorFieldTemplate = null;
  
  /**
   * Phone number operator field template.
   */
  private static String phoneNumberOperatorFieldTemplate = null;
  
  /**
   * Time operator field template.
   */
  private static String timeOperatorFieldTemplate = null;
  
  /**
   * Memo operator field template.
   */
  private static String memoOperatorFieldTemplate = null;
  
	/**
	 * Loads all templates. 
	 * @throws IOException
	 */
  private static void loadTemplates() throws IOException {
  	
    // Load templates
  	if (associateListFieldTemplate == null) {
  		associateListFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/associate-list-field.txt");
  	}

  	if (associateDisplayFieldTemplate == null) {
  		associateDisplayFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/associate-display-field.txt");
  	}

  	if (associateDisplayListFieldTemplate == null) {
  		associateDisplayListFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/associate-display-list-field.txt");
  	}

  	if (booleanEntityFieldTemplate == null) {
			booleanEntityFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/boolean-entity-field.txt");
  	}

  	if (booleanListFieldTemplate == null) {
			booleanListFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/boolean-list-field.txt");
  	}

  	if (columnHeaderTemplate == null) {
			columnHeaderTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/column-header.txt");
  	}

  	if (selectColumnHeaderTemplate == null) {
  		selectColumnHeaderTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/select-column-header.txt");
  	}

  	if (columnTemplate == null) {
			columnTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/column.txt");
  	}

  	if (columnSelectTemplate == null) {
			columnSelectTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/column-select.txt");
  	}

  	if (dependentMenuTemplate == null) {
			dependentMenuTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/dependent-menu.txt");
  	}

  	if (dependentLinkTemplate == null) {
			dependentLinkTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/dependent-link.txt");
  	}

  	if (entitiesPageTemplate == null) {
			entitiesPageTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/entities-page.txt");
  	}

  	if (selectEntitiesPageTemplate == null) {
  		selectEntitiesPageTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/select-entities-page.txt");
  	}

  	if (entityPageTemplate == null) {
			entityPageTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/entity-page.txt");
  	}

  	if (filterDataTemplate == null) {
			filterDataTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/filter-data.txt");
  	}

  	if (headerLinkTemplate == null) {
			headerLinkTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/header-link.txt");
  	}

  	if (hiddenFieldTemplate == null) {
			hiddenFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/hidden-entity-field.txt");
  	}

  	if (lookupColumnTemplate == null) {
			lookupColumnTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/lookup-column.txt");
  	}

  	if (lookupEntityFieldTemplate == null) {
			lookupEntityFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/lookup-entity-field.txt");
  	}

  	if (lookupFieldTemplate == null) {
			lookupFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/lookup-field.txt");
  	}

  	if (lookupMultipleColumnTemplate == null) {
			lookupMultipleColumnTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/lookup-multiple-column.txt");
  	}

  	if (lookupMultipleEntityFieldTemplate == null) {
			lookupMultipleEntityFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/lookup-multiple-entity-field.txt");
  	}

  	if (lookupMultipleFieldTemplate == null) {
			lookupMultipleFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/lookup-multiple-field.txt");
  	}

  	if (nextPageBreadcrumbActionTemplate == null) {
			nextPageBreadcrumbActionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/breadcrumb-action.txt");
  	}

  	if (pagingHeaderTemplate == null) {
  		pagingHeaderTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/paging-header.txt");
  	}

  	if (previousPageBreadcrumbActionTemplate == null) {
			previousPageBreadcrumbActionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/breadcrumb-action.txt");
  	}

  	if (selectPageBreadcrumbActionTemplate == null) {
			selectPageBreadcrumbActionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/breadcrumb-action.txt");
  	}

  	if (textFieldTemplate == null) {
			textFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/text-entity-field.txt");
  	}

  	if (memoFieldTemplate == null) {
  		memoFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/memo-entity-field.txt");
  	}

  	if (emailFieldTemplate == null) {
  		emailFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/email-entity-field.txt");
  	}

  	if (phoneNumberFieldTemplate == null) {
  		phoneNumberFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/phone-number-entity-field.txt");
  	}

  	if (dateFieldTemplate == null) {
  		dateFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/date-entity-field.txt");
  	}

  	if (timeFieldTemplate == null) {
  		timeFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/time-entity-field.txt");
  	}

  	if (viewPageBreadcrumbActionTemplate == null) {
			viewPageBreadcrumbActionTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/breadcrumb-action.txt");
  	}

  	if (textOperatorFieldTemplate == null) {
			textOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/text-operator-field.txt");
  	}

  	if (dateOperatorFieldTemplate == null) {
  		dateOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/date-operator-field.txt");
  	}

  	if (emailOperatorFieldTemplate == null) {
  		emailOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/email-operator-field.txt");
  	}
  	
  	if (phoneNumberOperatorFieldTemplate == null) {
  		phoneNumberOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/phone-number-operator-field.txt");
  	}

  	if (timeOperatorFieldTemplate == null) {
  		timeOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/time-operator-field.txt");
  	}
  	
  	if (memoOperatorFieldTemplate == null) {
  		memoOperatorFieldTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/jsp/memo-operator-field.txt");
  	}
  }
  
  /**
   * Creates a new JSP file creator.
   */
  public JspFileCreator() {
    super();
  }
  
  /**
   * Creates JSP class files for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException Process exception.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  public static void process(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {

    // Load templates
    loadTemplates();

    // Get the targeted project
    IProject project = definitionWizardData.getProject();
    
    // Create java project
    IJavaProject javaProject = JavaCore.create(project);

    // Initialize configuration package name
    String configurationPackageName = null;
    
    // Find configuration compilation unit
    IPackageFragment configurationPackageFragment = ResourceFinder.findPackageFragment(javaProject, "configuration");
    
    if (configurationPackageFragment != null) {
      configurationPackageName = configurationPackageFragment.getElementName();
    }
    else {

      // Throw error message
      throw new ProcessException("Error creating JSP files: Unable to locate configuration package.");
    }

    // Get parent entity name
    String parentEntityName = getParentEntityName(definitionWizardData);

    // Create JSP pages
    createViewEntitiesPage(definitionWizardData, viewWizardData, javaProject, configurationPackageName, parentEntityName, progressMonitor);
    createSelectEntitiesPage(definitionWizardData, viewWizardData, javaProject, configurationPackageName, parentEntityName, progressMonitor);
    createEditEntityPage(definitionWizardData, viewWizardData, javaProject, configurationPackageName, parentEntityName, progressMonitor);
    updateParentEntitiesPage(definitionWizardData, viewWizardData, javaProject, configurationPackageName, parentEntityName, progressMonitor);
    
    if (parentEntityName == null || parentEntityName.trim().length() == 0) {

    	// Add view entity action link to header
    	updateHeaderPage(definitionWizardData, viewWizardData, javaProject, configurationPackageName, progressMonitor);
    }
  }
  
  /**
   * Updates JSP class files for a fragment relationship.
   * @param relationshipWizardData Relationship wizard data.
   * @param viewWizardData View wizard data.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException Process exception.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  public static void process(RelationshipWizardData relationshipWizardData, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {

    // Load templates
    loadTemplates();

    // Get the targeted project
    IProject project = relationshipWizardData.getProject();
    
    // Create java project
    IJavaProject javaProject = JavaCore.create(project);

    // Get entity name and attribute name
    String entity1Name = relationshipWizardData.getDataObject1Name();
    String attribute1Name = relationshipWizardData.getAttribute1Name();

    // Get base package and entity name
    String basePackage1Name = TextConverter.getPackageName(entity1Name);
    String baseEntity1Name = TextConverter.getBaseClassName(entity1Name);
    
    // Get entity name and attribute name
    String entity2Name = relationshipWizardData.getDataObject2Name();
    String attribute2Name = relationshipWizardData.getAttribute2Name();

    // Get base package and entity name
    String basePackage2Name = TextConverter.getPackageName(entity2Name);
    String baseEntity2Name = TextConverter.getBaseClassName(entity2Name);
    
    boolean isManyToManyAssociation = relationshipWizardData.getAssociationType().equals(RelationshipWizardData.MANY_TO_MANY);
    
  	// Update data object for entity 1
    updateEditEntityPage(baseEntity1Name, basePackage2Name, baseEntity2Name, attribute2Name, isManyToManyAssociation, javaProject, progressMonitor);
    
    if (relationshipWizardData.isAttribute2IncludeInView()) {
    	
    	// Update view entities page
    	updateViewEntitiesPage(baseEntity1Name, basePackage2Name, baseEntity2Name, attribute2Name, isManyToManyAssociation, javaProject, progressMonitor);
    }
    
    if (relationshipWizardData.isBidirectional()) {

    	// Update data object for entity 2
      updateEditEntityPage(baseEntity2Name, basePackage1Name, baseEntity1Name, attribute1Name, isManyToManyAssociation, javaProject, progressMonitor);

      if (relationshipWizardData.isAttribute1IncludeInView()) {
      	
      	// Update view entities page
      	updateViewEntitiesPage(baseEntity2Name, basePackage1Name, baseEntity1Name, attribute1Name, isManyToManyAssociation, javaProject, progressMonitor);
      }
    }
  }
  
  /**
   * Adds an associate display field to the view entities page.
   * @param primaryEntityName Primary entity name.
   * @param secondaryPackageName Secondary entity package name.
   * @param secondaryEntityName Secondary entity name.
   * @param secondaryAttributeName Secondary entity attribute name.
   * @param isManyToManyAssociation Many to many association indicator.
   * @param javaProject Java project.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws ProcessException Process exception.
   * @throws IOException IO exception.
   */
  private static void updateViewEntitiesPage(String primaryEntityName, String secondaryPackageName, String secondaryEntityName, String secondaryAttributeName,
  		boolean isManyToManyAssociation, IJavaProject javaProject, IProgressMonitor progressMonitor) 
  		throws CoreException, ProcessException, IOException {
    
    // Convert to primary base names
    String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
    String primaryConstantName = TextConverter.convertCommonToConstant(primaryCommonName);
    String primaryUpperCaseCamel = TextConverter.convertCommonToCamel(primaryCommonName, false);
    String primaryLowerCaseCamel = TextConverter.convertCommonToCamel(primaryCommonName, true);

    // Convert to secondary base names
    String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
    String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);
    String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, false);
    String secondaryAttributeConstantName = TextConverter.convertCommonToConstant(secondaryAttributeName);
    
    // Create page name
    String pageName = TextConverter.convertToPlural(primaryUpperCaseCamel) + ".jsp";
  	
    // Retrieve web content folder
    IFolder folder = ResourceFinder.findWebContentFolder(javaProject.getProject());
    
    // Get entities folder
    IFolder entitiesFolder = folder.getFolder(primaryLowerCaseCamel);
    
    // Create if necessary
    if (!entitiesFolder.exists()) {
    	entitiesFolder.create(true, true, progressMonitor);
    }
    
    // Get view entities page file
    IFile viewEntitiesPageFile = entitiesFolder.getFile(pageName);
    
    if (viewEntitiesPageFile != null) {
      
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
    	
      // Create column header
      String columnHeader = columnHeaderTemplate.replaceAll("VIEW-ACTION-NAME", viewActionName);
      columnHeader = columnHeader.replaceAll("DISPLAY-FIELD-NAME", associateFieldId);
      columnHeader = columnHeader.replaceAll("SORT-FIELD-NAME", sortFieldFieldName);
      columnHeader = columnHeader.replaceAll("SORT-DIRECTION-NAME", sortDirectionFieldName);
    	
      // Initialize associate display field
      String associateDisplayField = isManyToManyAssociation ? associateDisplayListFieldTemplate : associateDisplayFieldTemplate;
      
      // Build associate display field
      associateDisplayField = associateDisplayField.replaceAll("ASSOCIATE-FIELD-ID", associateFieldId);
      associateDisplayField = associateDisplayField.replaceAll("ATTRIBUTE-NAME", primaryConstantName);
      associateDisplayField = associateDisplayField.replaceAll("MANAGER-CLASS-NAME", managerClassName);
      associateDisplayField = associateDisplayField.replaceAll("DISPLAY-FIELD-ID", displayFieldId);

      // Read file contents
      String contents = readFileContents(viewEntitiesPageFile);
      
      // Add data manager import statement
      String modifiedContents = addDataManagerImport(secondaryPackageName, managerClassName, contents);
      
      // Create pattern to match table data containing menu link column header
      Pattern menuPattern = Pattern.compile("<core:table.*<core:th.*(</core:td>\\s*<core:td>&nbsp;</core:td>).*</core:th>", Pattern.DOTALL);
      
      // Create menu matcher
      Matcher menuMatcher = menuPattern.matcher(modifiedContents);

      if (menuMatcher.find()) {

        // Get position just before table data element containing add entity link
        int insertPosition = menuMatcher.start(1) + new String("</core:td>").length();

        // Insert associate display field
        modifiedContents = insertContents(modifiedContents, columnHeader, insertPosition);
      }
      else {
      	
        // Create pattern to match table data containing add link column header
        menuPattern = Pattern.compile("<core:table.*<core:th.*(</core:td>\\s*<core:td align=\"center\">\\s*<core:link).*</core:th>", Pattern.DOTALL);
        
        // Create menu matcher
        menuMatcher = menuPattern.matcher(modifiedContents);

        if (menuMatcher.find()) {

          // Get position just before table data element containing add entity link
          int insertPosition = menuMatcher.start(1) + new String("</core:td>").length();

          // Insert associate display field
          modifiedContents = insertContents(modifiedContents, columnHeader, insertPosition);
        }
        else {
        	
          // Throw exception if insert position not found
          throw new ProcessException("Error adding associate display field to view entities JSP: Valid table header insert position not found.");
        }
      }
      
      // Create pattern to match table data containing dependent menu links
      menuPattern = Pattern.compile("<core:table.*<core:tr.*(<core:td>\\s*<core:popupMenu>).*</core:tr>.*</core:table>", Pattern.DOTALL);
      
      // Create menu matcher
      menuMatcher = menuPattern.matcher(modifiedContents);
      
      if (menuMatcher.find()) {
        
        // Get position just before table data element containing dependent menu links
        int insertPosition = menuMatcher.start(1);

        // Insert associate display field
        modifiedContents = insertContents(modifiedContents, associateDisplayField, insertPosition);
      }
      else {
        
        // Create pattern to match table data containing dependent menu links
        menuPattern = Pattern.compile("<core:table.*<core:tr.*(</core:td>\\s*<core:td align=\"center\">\\s*<core:link).*</core:tr>.*</core:table>", Pattern.DOTALL);

        // Create menu matcher
        menuMatcher = menuPattern.matcher(modifiedContents);
        
        if (menuMatcher.find()) {
        	
          // Get position just before table data element containing dependent menu links
          int insertPosition = menuMatcher.start(1) + new String("</core:td>").length();

          // Insert associate display field
          modifiedContents = insertContents(modifiedContents, associateDisplayField, insertPosition);
        }
        else {
        	
          // Throw exception if insert position not found
          throw new ProcessException("Error adding associate display field to view entities JSP: Valid table row insert position not found.");
        }
      }
      
      // Update file contents
      byte[] modifiedBytes = modifiedContents.getBytes();
      ByteArrayInputStream modifiedInputStream = new ByteArrayInputStream(modifiedBytes);
      viewEntitiesPageFile.setContents(modifiedInputStream, true, false, progressMonitor);
    }
  }
  
  /**
   * Updates an edit entity page with an associate component.
   * @param primaryEntityName Primary entity name.
   * @param secondaryPackageName Secondary entity package name.
   * @param secondaryEntityName Secondary entity name.
   * @param secondaryAttributeName Secondary entity attribute name.
   * @param isManyToManyAssociation Many to many association indicator.
   * @param javaProject Java project.
   * @param progressMonitor Progress monitor.
   * @throws CoreException Core exception.
   * @throws ProcessException Process exception.
   * @throws IOException IO exception.
   */
  private static void updateEditEntityPage(String primaryEntityName, String secondaryPackageName, String secondaryEntityName, String secondaryAttributeName,
  		boolean isManyToManyAssociation, IJavaProject javaProject, IProgressMonitor progressMonitor) 
  		throws CoreException, ProcessException, IOException {
  	
    // Convert to primary base names
    String primaryCommonName = TextConverter.convertCamelToCommon(primaryEntityName);
    String primaryConstantName = TextConverter.convertCommonToConstant(primaryCommonName);
    String primaryUpperCaseCamel = TextConverter.convertCommonToCamel(primaryCommonName, false);
    String primaryLowerCaseCamel = TextConverter.convertCommonToCamel(primaryCommonName, true);

    // Convert to secondary base names
    String secondaryCommonName = TextConverter.convertCamelToCommon(secondaryEntityName);
    String secondaryConstantName = TextConverter.convertCommonToConstant(secondaryCommonName);
    String secondaryUpperCaseCamel = TextConverter.convertCommonToCamel(secondaryCommonName, false);
    String secondaryPluralName = TextConverter.convertToPlural(secondaryCommonName);
    String secondaryPluralConstantName = TextConverter.convertCommonToConstant(secondaryPluralName);
    String secondaryAttributeConstantName = TextConverter.convertCommonToConstant(secondaryAttributeName);
    
    // Create page name
    String pageName = primaryUpperCaseCamel + ".jsp";
  	
    // Retrieve web content folder
    IFolder folder = ResourceFinder.findWebContentFolder(javaProject.getProject());
    
    // Get entity folder
    IFolder entityFolder = folder.getFolder(primaryLowerCaseCamel);
    
    // Create if necessary
    if (!entityFolder.exists()) {
    	entityFolder.create(true, true, progressMonitor);
    }
    
    // Get edit entity page file
    IFile editEntityPageFile = entityFolder.getFile(pageName);
    
    if (editEntityPageFile != null) {
      
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
    	
      // Build associate list field
      String associateListField = associateListFieldTemplate.replaceAll("ASSOCIATE-COMPONENT-TYPE", associateComponentType);
      associateListField = associateListField.replaceAll("ASSOCIATE-FIELD-ID", associateFieldId);
      associateListField = associateListField.replaceAll("ATTRIBUTE-NAME", primaryConstantName);
      associateListField = associateListField.replaceAll("MANAGER-CLASS-NAME", managerClassName);
      associateListField = associateListField.replaceAll("DISPLAY-FIELD-ID", displayFieldId);
      associateListField = associateListField.replaceAll("ACTION-ID", selectActionName);

      // Initialize modified contents
      String modifiedContents = null;
      
      // Read header file contents
      String contents = readFileContents(editEntityPageFile);
      
      // Add data manager import statement
      modifiedContents = addDataManagerImport(secondaryPackageName, managerClassName, contents);
      
      // Create pattern to match button separation tag
      Pattern buttonSeparationPattern = Pattern.compile("<tr>\\s*<td colspan=\"2\">&nbsp;</td>.*</table>\\s*</core:form>", Pattern.DOTALL);
      
      // Create matcher
      Matcher matcher = buttonSeparationPattern.matcher(modifiedContents);
      
      if (matcher.find()) {
        
        // Get position to insert associate field
        int insertPosition = matcher.start();
        
        // Insert dependent action
        modifiedContents = insertContents(modifiedContents, associateListField, insertPosition);
      }
      else {
        
        // Throw exception if parent entity file not found
        throw new ProcessException("Error adding associate list field to edit entity JSP: Button separation does not exist.");
      }
      
      // Update file contents
      byte[] modifiedBytes = modifiedContents.getBytes();
      ByteArrayInputStream modifiedInputStream = new ByteArrayInputStream(modifiedBytes);
      editEntityPageFile.setContents(modifiedInputStream, true, false, progressMonitor);
    }
  }
  
  private static String addDataManagerImport(String packageName, String dataManagerName, String contents) throws ProcessException {
  	
  	// Initialize return value
  	StringBuffer modifiedContents = new StringBuffer();
  	
    // Create pattern to match first import tag
    Pattern importPattern = Pattern.compile("<%@ page import.*%>", Pattern.DOTALL);
    
    // Create matcher
    Matcher matcher = importPattern.matcher(contents);
    
    if (matcher.find()) {
      
      // Get position to insert data
      int position = matcher.start();
      
      // Create import statement
      String importStatement = "<%@ page import=\"" + packageName + "." + dataManagerName + "\" %>";
      
      // Insert import statement
      modifiedContents.append(contents.substring(0, position));
      modifiedContents.append(importStatement);
      modifiedContents.append(LINE_SEPARATOR);
      modifiedContents.append(contents.substring(position));
    }
    else {
      
      // Throw exception if parent entity file not found
      throw new ProcessException("Error adding data manager import to edit entity JSP: No existing import statements exist.");
    }
    
    return modifiedContents.toString();
  }
  
  /**
   * Creates a view entities page file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param javaProject Java project.
   * @param configurationPackageName Configuration package name.
   * @param parentEntityName Parent entity name.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException Process exception.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void createViewEntitiesPage(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
  		IJavaProject javaProject, String configurationPackageName, String parentEntityName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    // Create attribute lookup
    Map<String, Attribute> attributeLookup = createAttributeLookup(attributes);
    
    // Get filter and column attribute names
    String[] filterAttributeNames = viewWizardData.getFilterAttributeNames();
    String[] columnAttributeNames = viewWizardData.getColumnAttributeNames();
    
    // Add configuration package name
    String entitiesPage = entitiesPageTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);

    // Get entity name
    String entityName = definitionWizardData.getEntityName();
    
    // Create camel case template variables
    String camelCaseEntityName = TextConverter.convertCommonToCamel(entityName, false);
    String directoryName = TextConverter.convertCommonToCamel(entityName, true);
    String pageTitle = TextConverter.convertToPlural(camelCaseEntityName);

    // Create constant case template variables
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
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
    String firstFieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(columnAttributeNames[0]);

    // Set start of breadcrumb trail value
    String isStartOfTrail = parentEntityName == null ? "true" : "false";
    
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
    
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Get parent entity ID field name
      String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      String parentEntityConstantName = TextConverter.convertCommonToConstant(parentEntityCommonName);
      String parentIdFieldName = parentEntityConstantName + "_ID";

      // Create hidden parent ID field statement
      String hiddenParentIdFieldStatement = hiddenFieldTemplate.replaceAll("FIELD-NAME", parentIdFieldName);
        
      // Add filter criteria statement
      hiddenFieldStatements.append(hiddenParentIdFieldStatement).append(LINE_SEPARATOR);
    }
    
    // Add hidden fields
    entitiesPage = entitiesPage.replaceAll("HIDDEN-FIELDS", hiddenFieldStatements.toString());
    
    
    if (filterAttributeNames == null || filterAttributeNames.length == 0) {
    
      // Clear filter data
      entitiesPage = entitiesPage.replaceAll("FILTER-DATA", "");
    }
    else {
      
      // Initialize filter field statements
      StringBuffer filterFieldStatements = new StringBuffer(LINE_SEPARATOR);
      
      for (String filterAttributeName : filterAttributeNames) {
        
        // Lookup attribute
        Attribute filterAttribute = attributeLookup.get(filterAttributeName);
        
        // Create common template variable
        String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttributeName) + "_FILTER";

        // Get attribute type
        String type = filterAttribute.getType();
        
        if (type.equalsIgnoreCase(AttributeTypes.LOOKUP)) {
          
          // Get category ID
          LookupAttribute lookupAttribute = (LookupAttribute)filterAttribute;
          String categoryId = TextConverter.convertCommonToConstant(lookupAttribute.getCategoryName());
          
          // Initialize filter field statement
          String filterFieldStatement = null;
          
          if (lookupAttribute.getMultipleValues()) {
            
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
        else if (type.equalsIgnoreCase(AttributeTypes.BOOLEAN)) {
        
          // Create filter field statement
          String filterFieldStatement = booleanListFieldTemplate.replaceAll("FIELD-NAME", fieldName);
          
          // Add filter criteria statement
          filterFieldStatements.append(filterFieldStatement).append(LINE_SEPARATOR);
        }
        else {
          
          // Initialize max length
          String maxLength = null;
          
          if (type.equalsIgnoreCase(AttributeTypes.TEXT)) {
            
            // Get max length from text attribute
          	TextAttribute textAttribute = (TextAttribute)filterAttribute;
            maxLength = String.valueOf(textAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.MEMO)) {
            
            // Get max length from memo attribute
          	MemoAttribute memoAttribute = (MemoAttribute)filterAttribute;
            maxLength = String.valueOf(memoAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.EMAIL)) {
            
            // Get max length from email attribute
          	EmailAttribute emailAttribute = (EmailAttribute)filterAttribute;
            maxLength = String.valueOf(emailAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER)) {
            
            // Get max length from phone number attribute
          	PhoneNumberAttribute phoneNumberAttribute = (PhoneNumberAttribute)filterAttribute;
            maxLength = String.valueOf(phoneNumberAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.POSTAL_CODE)) {
            
            // Get max length from postal code attribute
          	PostalCodeAttribute postalCodeAttribute = (PostalCodeAttribute)filterAttribute;
            maxLength = String.valueOf(postalCodeAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.DATE)) {

          	// Use standard length for dates
          	maxLength = "10";
          }
          else if (type.equalsIgnoreCase(AttributeTypes.TIME)) {

          	// Use standard length for time
          	maxLength = "5";
          }
          else if (type.equalsIgnoreCase(AttributeTypes.NUMERIC)) {

          	// Cast to numeric attribute
          	NumericAttribute numericAttribute = (NumericAttribute)filterAttribute;
            
          	// Get precision and scale
          	Integer precision = numericAttribute.getPrecision();
          	Integer scale = numericAttribute.getScale();
          	
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
        			
        	if (type.equalsIgnoreCase(AttributeTypes.TEXT) || 
        			type.equalsIgnoreCase(AttributeTypes.MEMO) || 
        			type.equalsIgnoreCase(AttributeTypes.EMAIL) || 
        			type.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER) || 
        			type.equalsIgnoreCase(AttributeTypes.POSTAL_CODE)) {
        		
        		// Set text mode and default operator name
          	textMode = "true";
          	defaultOperatorName = "CONTAINS"; 
        	}
        	
          // Initialize filter field statement
          String filterFieldStatement = null;

          // Set filter field statement template
          if (type.equalsIgnoreCase(AttributeTypes.DATE)) {
          	
          	filterFieldStatement = dateOperatorFieldTemplate;
        	}
          else if (type.equalsIgnoreCase(AttributeTypes.TIME)) {
          	
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
    
    if (!viewWizardData.getIsPagingEnabled()) {
      
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
    
    for (String columnAttributeName : columnAttributeNames) {
      
      // Lookup attribute
      Attribute attribute = attributeLookup.get(columnAttributeName);
      
      // Create field name
      String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(columnAttributeName);

      // Initialize category ID and multiple value indicator
      String categoryId = null;
      boolean isMultipleValues = false;
      
      // Get attribute type
      String type = attribute.getType();
      
      if (type.equalsIgnoreCase(AttributeTypes.LOOKUP)) {

        // Get category ID
        LookupAttribute lookupAttribute = (LookupAttribute)attribute;
        categoryId = TextConverter.convertCommonToConstant(lookupAttribute.getCategoryName());
        isMultipleValues = lookupAttribute.getMultipleValues();
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
        
        // Appende column
        columns.append(column).append(LINE_SEPARATOR);
      }
    }

    // Set page template variables
    entitiesPage = entitiesPage.replaceAll("COLUMN-HEADERS", columnHeaders.toString());
    entitiesPage = entitiesPage.replaceAll("COLUMNS", columns.toString());
    
    // Create input stream using entities page content
    ByteArrayInputStream entitiesPageInputStream = new ByteArrayInputStream(entitiesPage.getBytes());
    
    // Create entities page file name
    String entitiesPageFileName =  pageTitle + ".jsp";

    // Retrieve web content folder
    IFolder folder = ResourceFinder.findWebContentFolder(javaProject.getProject());
    
    // Retrieve entity directory
    IFolder directory = folder.getFolder(directoryName);
    
    // Create directory if necessary
    if (!directory.exists()) {
    	directory.create(true, true, progressMonitor);
    }
    
    // Create entities page file
    IFile entitiesPageFile = directory.getFile(entitiesPageFileName);
    entitiesPageFile.create(entitiesPageInputStream, true, progressMonitor);
  }

  /**
   * Creates a select entities page file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param javaProject Java project.
   * @param configurationPackageName Configuration package name.
   * @param parentEntityName Parent entity name.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException Process exception.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void createSelectEntitiesPage(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
  		IJavaProject javaProject, String configurationPackageName, String parentEntityName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    // Create attribute lookup
    Map<String, Attribute> attributeLookup = createAttributeLookup(attributes);
    
    // Get filter and column attribute names
    String[] filterAttributeNames = viewWizardData.getFilterAttributeNames();
    String[] columnAttributeNames = viewWizardData.getColumnAttributeNames();
    
    // Add configuration package name
    String selectEntitiesPage = selectEntitiesPageTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);

    // Get entity name
    String entityName = definitionWizardData.getEntityName();
    
    // Create base names
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
    String pluralConstantEntityName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String camelCaseEntityName = TextConverter.convertCommonToCamel(entityName, false);
    String directoryName = TextConverter.convertCommonToCamel(entityName, true);

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
    String firstFieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(columnAttributeNames[0]);

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

    if (filterAttributeNames == null || filterAttributeNames.length == 0) {
    
      // Clear filter data
      selectEntitiesPage = selectEntitiesPage.replaceAll("FILTER-DATA", "");
    }
    else {
      
      // Initialize filter field statements
      StringBuffer filterFieldStatements = new StringBuffer(LINE_SEPARATOR);
      
      for (String filterAttributeName : filterAttributeNames) {
        
        // Lookup attribute
        Attribute filterAttribute = attributeLookup.get(filterAttributeName);
        
        // Create common template variable
        String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(filterAttributeName) + "_FILTER";

        // Get attribute type
        String type = filterAttribute.getType();
        
        if (type.equalsIgnoreCase(AttributeTypes.LOOKUP)) {
          
          // Get category ID
          LookupAttribute lookupAttribute = (LookupAttribute)filterAttribute;
          String categoryId = TextConverter.convertCommonToConstant(lookupAttribute.getCategoryName());
          
          // Initialize filter field statement
          String filterFieldStatement = null;
          
          if (lookupAttribute.getMultipleValues()) {
            
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
        else if (type.equalsIgnoreCase(AttributeTypes.BOOLEAN)) {
        
          // Create filter field statement
          String filterFieldStatement = booleanListFieldTemplate.replaceAll("FIELD-NAME", fieldName);
          
          // Add filter criteria statement
          filterFieldStatements.append(filterFieldStatement).append(LINE_SEPARATOR);
        }
        else {
          
          // Initialize max length
          String maxLength = null;
          
          if (type.equalsIgnoreCase(AttributeTypes.TEXT)) {
            
            // Get max length from text attribute
          	TextAttribute textAttribute = (TextAttribute)filterAttribute;
            maxLength = String.valueOf(textAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.MEMO)) {
            
            // Get max length from memo attribute
          	MemoAttribute memoAttribute = (MemoAttribute)filterAttribute;
            maxLength = String.valueOf(memoAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.EMAIL)) {

            // Get max length from email attribute
          	EmailAttribute emailAttribute = (EmailAttribute)filterAttribute;
            maxLength = String.valueOf(emailAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER)) {

            // Get max length from phone number attribute
          	PhoneNumberAttribute phoneNumberAttribute = (PhoneNumberAttribute)filterAttribute;
            maxLength = String.valueOf(phoneNumberAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.POSTAL_CODE)) {

            // Get max length from postal code attribute
          	PostalCodeAttribute postalCodeAttribute = (PostalCodeAttribute)filterAttribute;
            maxLength = String.valueOf(postalCodeAttribute.getMaxLength());
          }
          else if (type.equalsIgnoreCase(AttributeTypes.DATE)) {

          	// Use standard length for dates
          	maxLength = "10";
          }
          else if (type.equalsIgnoreCase(AttributeTypes.TIME)) {

          	// Use standard length for time
          	maxLength = "5";
          }
          else if (type.equalsIgnoreCase(AttributeTypes.NUMERIC)) {

          	// Cast to numeric attribute
          	NumericAttribute numericAttribute = (NumericAttribute)filterAttribute;
            
          	// Get precision and scale
          	Integer precision = numericAttribute.getPrecision();
          	Integer scale = numericAttribute.getScale();
          	
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
        			
        	if (type.equalsIgnoreCase(AttributeTypes.TEXT) ||
        			type.equalsIgnoreCase(AttributeTypes.MEMO) ||
        			type.equalsIgnoreCase(AttributeTypes.EMAIL) ||
        			type.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER) ||
        			type.equalsIgnoreCase(AttributeTypes.POSTAL_CODE)) {
        		
        		// Set text mode and default operator name
          	textMode = "true";
          	defaultOperatorName = "CONTAINS"; 
        	}
        	
          // Initialize filter field statement
          String filterFieldStatement = null;

          // Set filter field statement template
          if (type.equalsIgnoreCase(AttributeTypes.DATE)) {
          	
          	filterFieldStatement = dateOperatorFieldTemplate;
        	}
          else if (type.equalsIgnoreCase(AttributeTypes.TIME)) {
          	
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
    
    if (!viewWizardData.getIsPagingEnabled()) {
      
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
      String pagingHeader = pagingHeaderTemplate.replaceAll("CURRENT-PAGE-NUMBER-FIELD-NAME", currentPageNumberFieldName);
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
    
    
    for (String columnAttributeName : columnAttributeNames) {
      
      // Lookup attribute
      Attribute attribute = attributeLookup.get(columnAttributeName);
      
      // Create field name
      String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(columnAttributeName);

      // Initialize category ID and multiple value indicator
      String categoryId = null;
      boolean isMultipleValues = false;
      
      // Get attribute type
      String type = attribute.getType();
      
      if (type.equalsIgnoreCase(AttributeTypes.LOOKUP)) {

        // Get category ID
        LookupAttribute lookupAttribute = (LookupAttribute)attribute;
        categoryId = TextConverter.convertCommonToConstant(lookupAttribute.getCategoryName());
        isMultipleValues = lookupAttribute.getMultipleValues();
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
    
    // Create input stream using select entities page content
    ByteArrayInputStream selectEntitiesPageInputStream = new ByteArrayInputStream(selectEntitiesPage.getBytes());
    
    // Create select entities page file name
    String selectEntitiesPageFileName = formId + ".jsp";

    // Retrieve web content folder
    IFolder folder = ResourceFinder.findWebContentFolder(javaProject.getProject());
    
    // Retrieve entity directory
    IFolder directory = folder.getFolder(directoryName);
    
    // Create directory if necessary
    if (!directory.exists()) {
    	directory.create(true, true, progressMonitor);
    }
    
    // Create select entities page file
    IFile selectEntitiesPageFile = directory.getFile(selectEntitiesPageFileName);
    selectEntitiesPageFile.create(selectEntitiesPageInputStream, true, progressMonitor);
  }

  /**
   * Creates a view entities page file for the entity defined in the application fragment wizard.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param javaProject Java project.
   * @param configurationPackageName Configuration package name.
   * @param parentEntityName Parent entity name.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException
   * @throws CoreException
   * @throws IOException
   */
  private static void createEditEntityPage(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
  		IJavaProject javaProject, String configurationPackageName, String parentEntityName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Get attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();
    
    // Add configuration package name
    String entityPage = entityPageTemplate.replaceAll("CONFIGURATION-PACKAGE-NAME", configurationPackageName);

    // Get package name and entity name
    String packageName = definitionWizardData.getPackageName();
    String entityName = definitionWizardData.getEntityName();

    // Create template variables
    String pageTitle = TextConverter.convertCommonToCamel(entityName, false);
    String dataObjectName = TextConverter.convertCommonToCamel(entityName, false);
    String constantEntityName = TextConverter.convertCommonToConstant(entityName);
    String constantPluralEntityName = TextConverter.convertToPlural(constantEntityName).toUpperCase();
    String directoryName = TextConverter.convertCommonToCamel(entityName, true);
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
    
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Get parent entity ID field name
      String parentEntityCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      String parentEntityConstantName = TextConverter.convertCommonToConstant(parentEntityCommonName);
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
      String type = attribute.getType();
      
      // Create field name template variable
      String fieldName = constantEntityName + "_" + TextConverter.convertCommonToConstant(name);

      if (type.equalsIgnoreCase(AttributeTypes.LOOKUP)) {
        
        // Get category ID
        LookupAttribute lookupAttribute = (LookupAttribute)attribute;
        String categoryId = TextConverter.convertCommonToConstant(lookupAttribute.getCategoryName());

        // Initialize field statement
        String fieldStatement = null;
        
        if (lookupAttribute.getMultipleValues()) {
          
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
      else if (type.equalsIgnoreCase(AttributeTypes.BOOLEAN)) {
        
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
        if (type.equalsIgnoreCase(AttributeTypes.TEXT)) {
        	
        	fieldTemplate = textFieldTemplate;
        	
          TextAttribute textAttribute = (TextAttribute)attribute;
          maxLength = String.valueOf(textAttribute.getMaxLength());
        }
        else if (type.equalsIgnoreCase(AttributeTypes.MEMO)) {
        	
        	fieldTemplate = memoFieldTemplate;
        	
          MemoAttribute memoAttribute = (MemoAttribute)attribute;
          maxLength = String.valueOf(memoAttribute.getMaxLength());
        }
        else if (type.equalsIgnoreCase(AttributeTypes.EMAIL)) {
        	
        	fieldTemplate = emailFieldTemplate;
        	
          EmailAttribute emailAttribute = (EmailAttribute)attribute;
          maxLength = String.valueOf(emailAttribute.getMaxLength());
        }
        else if (type.equalsIgnoreCase(AttributeTypes.PHONE_NUMBER)) {
        	
        	fieldTemplate = phoneNumberFieldTemplate;
        	
          PhoneNumberAttribute phoneNumberAttribute = (PhoneNumberAttribute)attribute;
          maxLength = String.valueOf(phoneNumberAttribute.getMaxLength());
        }
        else if (type.equalsIgnoreCase(AttributeTypes.POSTAL_CODE)) {
        	
        	fieldTemplate = textFieldTemplate;
        	
          PostalCodeAttribute postalCodeAttribute = (PostalCodeAttribute)attribute;
          maxLength = String.valueOf(postalCodeAttribute.getMaxLength());
        }
        else if (type.equalsIgnoreCase(AttributeTypes.NUMERIC)) {
        	
        	fieldTemplate = textFieldTemplate;
        	
          NumericAttribute numericAttribute = (NumericAttribute)attribute;
          int numericLength = numericAttribute.getPrecision();
          
          if (numericAttribute.getScale() > 0) {
          	numericLength = numericLength + numericAttribute.getScale() + 1; 
          }
          
          maxLength = String.valueOf(numericLength);
        }
        else if (type.equalsIgnoreCase(AttributeTypes.DATE)) {
        	
        	fieldTemplate = dateFieldTemplate;
        	
          maxLength = "10";
        }
        else if (type.equalsIgnoreCase(AttributeTypes.TIME)) {
        	
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

    // Set page template variables
    entityPage = entityPage.replaceAll("FIELDS", fieldStatements.toString());

    // Create input stream using entity page content
    ByteArrayInputStream entityPageInputStream = new ByteArrayInputStream(entityPage.getBytes());
    
    // Create entity page file name
    String entityPageFileName = pageTitle + ".jsp";

    // Retrieve web content folder
    IFolder folder = ResourceFinder.findWebContentFolder(javaProject.getProject());
    
    // Retrieve entity directory
    IFolder directory = folder.getFolder(directoryName);
    
    // Create directory if necessary
    if (!directory.exists()) {
    	directory.create(true, true, progressMonitor);
    }

    // Create entity page file
    IFile entityPageFile = directory.getFile(entityPageFileName);
    entityPageFile.create(entityPageInputStream, true, progressMonitor);
  }
  
  /**
   * Adds a dependent link to a parent entities page.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param javaProject Java project.
   * @param configurationPackageName Configuration package name.
   * @param parentEntityName Parent entity name.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException Process exception.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void updateParentEntitiesPage(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
  		IJavaProject javaProject, String configurationPackageName, String parentEntityName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Add dependency to parent entity JSP
    if (parentEntityName != null && parentEntityName.trim().length() > 0) {
      
      // Create parent page title
      String parentPageTitle = TextConverter.convertToPlural(parentEntityName);
      
      // Create parent entities page file name
      String parentEntitiesPageFileName = parentPageTitle + ".jsp";

      // Retrieve web content folder
      IFolder folder = ResourceFinder.findWebContentFolder(javaProject.getProject());
      
      // Create parent directory name
      String parentCommonName = TextConverter.convertCamelToCommon(parentEntityName);
      String parentDirectoryName = TextConverter.convertCommonToCamel(parentCommonName, true);
      
      // Retrieve parent directory
      IFolder parentDirectory = folder.getFolder(parentDirectoryName);
      
      // Create parent directory if necessary
      if (!parentDirectory.exists()) {
      	parentDirectory.create(true, true, progressMonitor);
      }
      
      // Get parent entities file
      IFile parentEntitiesPageFile = parentDirectory.getFile(parentEntitiesPageFileName);
      
      if (parentEntitiesPageFile != null) {
        
        // Get entity name
        String entityName = definitionWizardData.getEntityName();
        
        // Create view action name
        String constantEntityName = TextConverter.convertCommonToConstant(entityName);
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

        // Initialize modified contents
        String modifiedContents = null;
        
        // Read parent entity file contents
        String contents = readFileContents(parentEntitiesPageFile);
        
        // Create pattern to match dependent menu end tag
        Pattern menuPattern = Pattern.compile("<core:table.*<core:tr.*<core:td.*(</core:link>.*</core:popupMenu>).*</core:td>.*</core:tr>.*</core:table>", Pattern.DOTALL);
        
        // Create menu matcher
        Matcher menuMatcher = menuPattern.matcher(contents);
        
        if (!menuMatcher.find()) {
          
          // Build dependent menu
          String dependentMenu = dependentMenuTemplate.replaceAll("DEPENDENT-LINK", dependentLink);
          
          // Insert dependent menu
          modifiedContents = insertDependentMenu(contents, dependentMenu);
        }
        else {
          
          // Get position of dependent menu end tag
          int menuPosition = menuMatcher.start(1) + new String("</core:link>").length();
          
          // Insert dependent action
          modifiedContents = insertContents(contents, dependentLink, menuPosition);
        }
        
        // Update file contents
        byte[] modifiedBytes = modifiedContents.getBytes();
        ByteArrayInputStream modifiedInputStream = new ByteArrayInputStream(modifiedBytes);
        parentEntitiesPageFile.setContents(modifiedInputStream, true, false, progressMonitor);
      }
      else {
        
        // Throw exception if parent entity file not found
        throw new ProcessException("Error adding dependents to parent JSP: Parent entity file does not exist.");
      }
    }
  }
  
  /**
   * Adds a view entities link to the header page.
   * @param definitionWizardData Definition wizard data.
   * @param viewWizardData View wizard data.
   * @param javaProject Java project.
   * @param configurationPackageName Configuration package name.
   * @param progressMonitor Progress monitor.
   * @throws ProcessException Process exception.
   * @throws CoreException Core exception.
   * @throws IOException IO exception.
   */
  private static void updateHeaderPage(DefinitionWizardData definitionWizardData, ViewWizardData viewWizardData, 
  		IJavaProject javaProject, String configurationPackageName, IProgressMonitor progressMonitor) 
      throws ProcessException, CoreException, IOException {
    
    // Retrieve web content folder
    IFolder folder = ResourceFinder.findWebContentFolder(javaProject.getProject());
    
    // Get header file
    IFile headerPageFile = folder.getFile("Header.jsp");
    
    if (headerPageFile != null) {
      
      // Get entity name
      String entityName = definitionWizardData.getEntityName();
      
      // Create view action name
      String constantEntityName = TextConverter.convertCommonToConstant(entityName);
      String viewActionName = "VIEW_" + TextConverter.convertToPlural(constantEntityName).toUpperCase();
      
      // Create view page name
      String viewPageName = TextConverter.convertToPlural(constantEntityName).toUpperCase();

      // Build header link
      String headerLink = headerLinkTemplate.replaceAll("ACTION-NAME", viewActionName);
      headerLink = headerLink.replaceAll("PAGE-NAME", viewPageName);

      // Initialize modified contents
      String modifiedContents = null;
      
      // Read header file contents
      String contents = readFileContents(headerPageFile);
      
      // Create pattern to match data menu start tag
      Pattern menuPattern = Pattern.compile("</core:menu>\\s*</core:menuBar>\\s*</core:form>", Pattern.DOTALL);
      
      // Create menu matcher
      Matcher menuMatcher = menuPattern.matcher(contents);
      
      if (menuMatcher.find()) {
        
        // Get position to insert header link
        int insertPosition = menuMatcher.start();
        
        // Insert dependent action
        modifiedContents = insertContents(contents, headerLink, insertPosition);
      }
      else {
        
        // Throw exception if parent entity file not found
        throw new ProcessException("Error adding view entities link to header JSP: Data menu does not exist.");
      }
      
      // Update file contents
      byte[] modifiedBytes = modifiedContents.getBytes();
      ByteArrayInputStream modifiedInputStream = new ByteArrayInputStream(modifiedBytes);
      headerPageFile.setContents(modifiedInputStream, true, false, progressMonitor);
    }
  }
  
  /**
   * Return page file contents.
   * @param pageFile Page file.
   * @return String File contents.
   * @throws Exception
   */
  private static String readFileContents(IFile pageFile) throws CoreException, IOException {
    
    // Initialize return value
    StringBuffer contents = new StringBuffer();
   
    // Get buffered file reader
    BufferedReader reader = new BufferedReader(new InputStreamReader(pageFile.getContents()));
    
    // Initialize read buffer
    char[] buffer = new char[1000];
    
    // Read first set of characters
    int count = reader.read(buffer, 0, 1000);
    
    while (count != -1) {

      // Append line to contents
      contents.append(buffer, 0, count);
      
      // Read next set of characters
      count = reader.read(buffer, 0, 1000);
    }
    
    return contents.toString();
  }
  
  /**
   * Inserts a dependent menu into the contents of a parent entity JSP page.
   * @param contents Parent entity page content.
   * @param dependentMenu Dependent menu text.
   * @return String Modified contents including menu.
   */
  private static String insertDependentMenu(String contents, String dependentMenu) throws ProcessException {
    
    // Initialize return value
    StringBuffer modifiedContents = new StringBuffer();
    
    // Create pattern to match last table data element in table header 
    Pattern tableHeaderPattern = Pattern.compile("<core:table.*<core:th.*(</core:td>.*<core:td.*</core:td>).*</core:th>.*</core:table>", Pattern.DOTALL);
    
    // Create table header matcher
    Matcher tableHeaderMatcher = tableHeaderPattern.matcher(contents);

    if (tableHeaderMatcher.find()) {

      // Get position of last table data element in table header
      int tableHeaderPosition = tableHeaderMatcher.start(1) + new String("</core:td>").length();
      
      // Create pattern to match last table data element in table row 
      Pattern tableRowPattern = Pattern.compile("<core:table.*<core:tr.*(</core:td>.*<core:td.*</core:td>).*</core:tr>.*</core:table>", Pattern.DOTALL);
      
      // Create table row matcher
      Matcher tableRowMatcher = tableRowPattern.matcher(contents);
      
      if (tableRowMatcher.find()) {
        
        // Get position of last table data element in table row
        int tableRowPosition = tableRowMatcher.start(1) + new String("</core:td>").length();

        // Insert dependent column header and menu
        modifiedContents.append(contents.substring(0, tableHeaderPosition));
        modifiedContents.append(LINE_SEPARATOR);
        modifiedContents.append("          <core:td>&nbsp;</core:td>");
        modifiedContents.append(contents.substring(tableHeaderPosition, tableRowPosition));
        modifiedContents.append(LINE_SEPARATOR);
        modifiedContents.append(dependentMenu);
        modifiedContents.append(contents.substring(tableRowPosition));
      }
      else {
        
        // Throw exception
        throw new ProcessException("Error inserting dependent menu: Table row not found.");
      }
    }
    else {
      
      // Throw exception
      throw new ProcessException("Error inserting dependent menu: Table header not found.");
    }

    return modifiedContents.toString();
  }
  
  /**
   * Inserts a value into the contents of a JSP page.
   * @param contents Page content.
   * @param position Position to insert the value.
   * @param value Value text.
   * @return String Modified contents including value.
   */
  private static String insertContents(String contents, String value, int position) {
    
    // Initialize return value
    StringBuffer modifiedContents = new StringBuffer();
    
    // Insert dependent action
    modifiedContents.append(contents.substring(0, position));
    modifiedContents.append(LINE_SEPARATOR);
    modifiedContents.append(value);
    modifiedContents.append(contents.substring(position));
    
    return modifiedContents.toString();
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