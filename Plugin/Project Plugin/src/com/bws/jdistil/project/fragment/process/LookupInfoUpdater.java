package com.bws.jdistil.project.fragment.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

import com.bws.jdistil.project.fragment.DefinitionWizardData;
import com.bws.jdistil.project.fragment.data.Attribute;
import com.bws.jdistil.project.fragment.data.AttributeTypes;
import com.bws.jdistil.project.fragment.data.LookupAttribute;
import com.bws.jdistil.project.util.ResourceFinder;
import com.bws.jdistil.project.util.ResourceReader;
import com.bws.jdistil.project.util.TextConverter;

/**
 * Adds new categories to the category IDs class file and codes SQL file.
 * Also sets the category ID of all lookup attributes defining new categories.
 * @author Bryan Snipes
 */
public class LookupInfoUpdater {

  /**
   * Line separator.
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Creates a new lookup information updater.
   */
  public LookupInfoUpdater() {
    super();
  }
  
  /**
   * Adds new categories to the category IDs class file and codes SQL file.
   * Also sets the category ID of all lookup attributes defining new categories.
   * @param definitionWizardData Definition wizard data.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   * @throws IOException
   */
  public static void process(DefinitionWizardData definitionWizardData, IProgressMonitor progressMonitor) 
      throws CoreException, IOException, ProcessException {
    
    // Get the targeted project
    IProject project = definitionWizardData.getProject();

    // Create java project
    IJavaProject javaProject = JavaCore.create(project);
    
    // Find category IDs compilation unit
    ICompilationUnit categoryIdsCompilationUnit = ResourceFinder.findCompilationUnit(javaProject, "configuration", "CategoryIds.java");
      
    // Verify compilation unit was found
    if (categoryIdsCompilationUnit == null) {
      throw new ProcessException("Error updating lookup information: CategoryIds class not found.");
    }

    // Get category IDs type
    IType categoryIdsType = categoryIdsCompilationUnit.findPrimaryType();
    
    // Get max category ID
    Integer categoryId = findMaxCategoryId(categoryIdsType);
    
    // Initialize map of new categories
    Map<Integer, String> categories = new HashMap<Integer, String>();
    
    // Get entity attributes
    Collection<Attribute> attributes = definitionWizardData.getAttributes();

    for (Attribute attribute : attributes) {
      
      // Get attribute type
      String type = attribute.getType();
      
      if (type.equalsIgnoreCase(AttributeTypes.LOOKUP)) {
        
        // Cast to lookup attribute
        LookupAttribute lookupAttribute = (LookupAttribute)attribute;
        
        if (lookupAttribute.getCategoryId() == null) {
          
          // Increment category ID
          categoryId++;
          
          // Set category ID
          lookupAttribute.setCategoryId(categoryId);
          
          // Get category name
          String categoryName = TextConverter.convertCommonToConstant(lookupAttribute.getCategoryName());
          
          // Add category to map
          categories.put(categoryId, categoryName);
        }
      }
    }

    // Update lookup information
    updateCategoryClass(categoryIdsType, categories, progressMonitor);
    updateCategorySql(project, categories, progressMonitor);
  }

  /**
   * Returns the maximum category ID value available in the CategoryIds class.
   * @param categoryIdsType Category IDs type.
   * @throws CoreException 
   */
  private static Integer findMaxCategoryId(IType categoryIdsType)
      throws CoreException {
    
    // Initialize return value
    Integer maxCategoryId = 0;
    
    for (IField field : categoryIdsType.getFields()) {
      
      // Get category ID associated with field
      Integer categoryId = (Integer)field.getConstant();
      
      // Updated maximum category ID
      if (categoryId.intValue() > maxCategoryId.intValue()) {
        maxCategoryId = categoryId;
      }
    }
    
    return maxCategoryId;
  }
  
  /**
   * Updates the category IDs class file with any newly defined categories.
   * @param categoryIdsType Category IDs type.
   * @param categories Map of new category names keyed by category ID.
   * @throws CoreException 
   */
  private static void updateCategoryClass(IType categoryIdsType,
      Map<Integer, String> categories, IProgressMonitor progressMonitor)
      throws CoreException {
    
    for (Map.Entry<Integer, String> category : categories.entrySet()) {
      
      // Get category ID and name
      String categoryId = String.valueOf(category.getKey());
      String categoryName = TextConverter.convertCommonToConstant(category.getValue());
      
      // Create field contents
      String contents = "   public static final int " + categoryName + " = " + categoryId + ";";

      // Create category ID field
      categoryIdsType.createField(contents, null, true, progressMonitor);
    }
  }
  
  /**
   * Updates the codes SQL file with any newly defined categories.
   * @param project Current project.
   * @param resourceReader Resource reader.
   * @throws IOException, CoreException 
   */
  private static void updateCategorySql(IProject project,
      Map<Integer, String> categories, IProgressMonitor progressMonitor)
      throws IOException, IOException, CoreException {
    
    // Get SQL folder
    IFolder folder = project.getFolder(new Path("sql"));

    // Create resource reader
    ResourceReader resourceReader = new ResourceReader();
    
    // Get SQL template
    String insertCategoryTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/lookup/insert-category.txt");
    
    // Initialize SQL 
    StringBuffer sql = new StringBuffer();

    for (Map.Entry<Integer, String> category : categories.entrySet()) {
      
      // Get category ID and name
      String categoryId = String.valueOf(category.getKey());
      String categoryName = TextConverter.convertConstantToCommon(category.getValue());
        
      // Replace template variables
      String insertCategory = insertCategoryTemplate.replaceAll("CATEGORY-ID", String.valueOf(categoryId));
      insertCategory = insertCategory.replaceAll("CATEGORY-NAME", categoryName);
     
      // Append insert statement to SQL
      sql.append(insertCategory).append(LINE_SEPARATOR);
    }
    
    if (sql.length() > 0) {
      
      // Convert SQL into input stream
      ByteArrayInputStream sqlInputStream = new ByteArrayInputStream(sql.toString().getBytes());
      
      // Get category SQL file and append SQL content
      IFile categorySqlFile = folder.getFile("app-category.sql");
      categorySqlFile.appendContents(sqlInputStream, true, false, progressMonitor);
    }
  }
  
}
