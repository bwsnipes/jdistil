package com.bws.jdistil.project.util;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;

/**
 * Provides utility methods used to find project specific resources.
 */
public class ResourceFinder {

  /**
   * Creates a new resource finder. Defined with private access to prevent instantiation.
   */
  private ResourceFinder() {
    super();
  }
  
  /**
   * Returns the source package fragment root for a given Java project.
   * @param javaProject Java project.
   * @return ICompilationUnit Compilation unit representing targeted class.
   * @throws CoreException
   */
  public static IPackageFragmentRoot findSourcePackageFragmentRoot(IJavaProject javaProject)
      throws CoreException {
    
    // Initialize return value
    IPackageFragmentRoot sourcePackageFragmentRoot = null;
    
    // Get all package fragment roots
    IPackageFragmentRoot[] packageFragmentRoots = javaProject.getAllPackageFragmentRoots();
    
    // Find source package fragment root
    for (IPackageFragmentRoot packageFragmentRoot : packageFragmentRoots) {
      if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
        sourcePackageFragmentRoot = packageFragmentRoot;
        break;
      }
    }
    
    return sourcePackageFragmentRoot;
  }
  
  /**
   * Returns a compilation unit matching a given class name and 
   * containing package fragment name in a specified Java project.
   * @param javaProject Java project.
   * @param packageFragmentName Package fragment name containing target class.
   * @return ICompilationUnit Compilation unit representing targeted class.
   * @throws CoreException
   */
  public static IPackageFragment findPackageFragment(IJavaProject javaProject, String packageFragmentName)
      throws CoreException {
    
    // Initialize return value
    IPackageFragment packageFragment = null;
    
    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = findSourcePackageFragmentRoot(javaProject);
    
    if (sourcePackageFragmentRoot != null) {
      
      // Get all elements defined in source package
      IJavaElement[] sourcePackageElements = sourcePackageFragmentRoot.getChildren();
      
      for (IJavaElement sourcePackageElement : sourcePackageElements) {
        
        // Check for configuration package
        if (sourcePackageElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT &&
            sourcePackageElement.getElementName().endsWith(packageFragmentName)) {
          
          // Cast to package fragment
          packageFragment = (IPackageFragment)sourcePackageElement;
        }
      }
    }
    
    return packageFragment;
  }

  /**
   * Returns a compilation unit matching a given class name and 
   * containing package fragment name in a specified Java project.
   * @param javaProject Java project.
   * @param packageFragmentName Package fragment name containing target class.
   * @param className Target class name.
   * @return ICompilationUnit Compilation unit representing targeted class.
   * @throws CoreException
   */
  public static ICompilationUnit findCompilationUnit(IJavaProject javaProject, String packageFragmentName, String className)
      throws CoreException {
    
    // Initialize return value
    ICompilationUnit compilationUnit = null;
    
    // Retrieve source package fragment root
    IPackageFragmentRoot sourcePackageFragmentRoot = findSourcePackageFragmentRoot(javaProject);

    if (sourcePackageFragmentRoot != null) {
      
      // Get all elements defined in source package
      IJavaElement[] sourcePackageElements = sourcePackageFragmentRoot.getChildren();
      
      for (IJavaElement sourcePackageElement : sourcePackageElements) {
        
        // Check for configuration package
        if (sourcePackageElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT &&
            sourcePackageElement.getElementName().endsWith(packageFragmentName)) {
          
          // Cast to package fragment
          IPackageFragment targetPackageFragment = (IPackageFragment)sourcePackageElement;
          
          // Get all elements defined in target package
          IJavaElement[] targetPackageElements = targetPackageFragment.getChildren();
          
          for (IJavaElement targetPackageElement : targetPackageElements) {
            
            // Check for compilation units
            if (targetPackageElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
              
              // Get compilation unit name
              String compilationUnitName = targetPackageElement.getElementName();

              // Check for matching class name
              if (compilationUnitName.equals(className)) {
                compilationUnit = (ICompilationUnit)targetPackageElement;
                break;
              }
            }            
          }
        }
      }
    }
    
    return compilationUnit;
  }

  /**
   * Returns a java element matching a class name pattern from a specified project.
   * @param javaProject Java project to search.
   * @param classNamePattern Class name pattern used to search.
   * @return IJavaElement Java element representing the targeted class.
   */
  public static IJavaElement findClassElement(IJavaProject javaProject, String classNamePattern) throws CoreException {
    
    // Create search pattern
    SearchPattern searchPattern = SearchPattern.createPattern(classNamePattern, IJavaSearchConstants.CLASS, IJavaSearchConstants.IMPLEMENTORS, SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE);

    // Create search scope
    IJavaElement[] javaElements = new IJavaElement[] {javaProject};
    IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(javaElements, IJavaSearchScope.SOURCES);
    
    // Create search participants
    SearchParticipant[] searchParticipants = new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()};
    
    // Create search requestor
    ClassElementSearchRequestor searchRequestor = new ClassElementSearchRequestor();
    
    // Search
    SearchEngine searchEngine = new SearchEngine();
    searchEngine.search(searchPattern, searchParticipants, searchScope, searchRequestor, null);
    
    return (IJavaElement)searchRequestor.getClassElement();
  }

  /**
   * Returns the web content folder for a given project.
   * @param project Project.
   * @return IFolder Web content folder.
   */
  public static IFolder findWebContentFolder(IProject project) {
    IVirtualComponent component = ComponentCore.createComponent(project);
    IVirtualFolder contentFolder = component.getRootFolder();
    return (IFolder)contentFolder.getUnderlyingFolder();
  }

  
/**
 * Search requestor used to retrieve a single class element.
 */
private static class ClassElementSearchRequestor extends SearchRequestor {

  /**
   * Class element.
   */
  private Object classElement = null;
  
  /**
   * Set class element using matched element.
   */
  public void acceptSearchMatch(SearchMatch match) throws CoreException {
    classElement = match.getElement();
  }
  
  /**
   * Return the class element.
   * @return Object Class element.
   */
  public Object getClassElement() {
    return classElement;
  }
}

}
