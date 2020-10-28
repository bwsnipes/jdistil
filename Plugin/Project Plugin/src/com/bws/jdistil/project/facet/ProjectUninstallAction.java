package com.bws.jdistil.project.facet;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class ProjectUninstallAction implements IDelegate {

  /**
   * Uninstalls all JDistil application resources.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  public void execute(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {

    // Uninstall all resources
    uninstallServlet(project, projectFacetVersion, configuration, progressMonitor);
    uninstallClasses(project, projectFacetVersion, configuration, progressMonitor);
    uninstallPropertyFiles(project, projectFacetVersion, configuration, progressMonitor);
    uninstallLibraries(project, projectFacetVersion, configuration, progressMonitor);
    uninstallSql(project, projectFacetVersion, configuration, progressMonitor);
	}

  /**
   * Uninstalls the controller servlet.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void uninstallServlet(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get web artifact associated with project
    WebArtifactEdit artifact = WebArtifactEdit.getWebArtifactEditForWrite(project);
    
    try {
      // Add servlet and mapping to web app
      final WebApp webApp = artifact.getWebApp();
      
      // Attempt to retrieve servlet
      Servlet servlet = webApp.getServletNamed("Controller");
      
      if (servlet != null) {
        
        // Remove servlet
        webApp.getServlets().remove(servlet);
        
        // Attempt to find servlet mapping
        ServletMapping mapping = webApp.getServletMapping(servlet);
        
        // Removing servlet mapping
        if (mapping != null) {
          webApp.getServletMappings().remove(mapping);
        }
      }
    
      // Save changes to web app
      artifact.saveIfNecessary(progressMonitor);
    }
    finally {
      artifact.dispose();
    }
  }
  
  /**
   * Uninstalls all class files into a new configuration package directory.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void uninstallClasses(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Create java project
    IJavaProject javaProject = JavaCore.create(project);
    
    // Initialize source package fragment root
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

    if (sourcePackageFragmentRoot != null) {
      
      // Get all elements defined in source package
      IJavaElement[] sourcePackageElements = sourcePackageFragmentRoot.getChildren();
      
      for (IJavaElement sourcePackageElement : sourcePackageElements) {
        
        // Check for configuration package
        if (sourcePackageElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT &&
            sourcePackageElement.getElementName().endsWith("configuration")) {
          
          // Cast to package fragment
          IPackageFragment configurationPackage = (IPackageFragment)sourcePackageElement;
          
          // Get all elements defined in configuration package
          IJavaElement[] configurationPackageElements = configurationPackage.getChildren();
          
          for (IJavaElement configurationPackageElement : configurationPackageElements) {
            
            // Check for configuration classes
            if (configurationPackageElement.getElementType() == IJavaElement.COMPILATION_UNIT) {
              
              // Get compilation unit name
              String compilationUnitName = configurationPackageElement.getElementName();
              
              if (compilationUnitName.equals("ActionIds.java") ||
                  compilationUnitName.equals("AttributeNames.java") ||
                  compilationUnitName.equals("Configuration.java") ||
                  compilationUnitName.equals("Constants.java") ||
                  compilationUnitName.equals("FieldIds.java") ||
                  compilationUnitName.equals("PageIds.java")) {
                
                // Get underlying resource
                IResource resource = configurationPackageElement.getUnderlyingResource();
                
                // Delete resource
                if (resource != null && resource.exists()) {
                  resource.delete(true, progressMonitor);
                }
              }
            }            
          }
        }
      }
    }
  }
  
  /**
   * Uninstalls all property files into a new properties directory.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void uninstallPropertyFiles(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get library folder
    IFolder folder = project.getFolder(new Path("properties"));
    
    if (folder.exists()) {

      // Get properties file path
      IPath propertiesPath = folder.getFullPath();
  
      // Create new classpath entries
      List<IClasspathEntry> newClasspathEntries = new ArrayList<IClasspathEntry>(); 
  
      // Create java project
      IJavaProject javaProject = JavaCore.create(project);
      
      // Get existing classpath entries
      IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();
      
      for (IClasspathEntry classpathEntry : classpathEntries) {
      
        // Get classpath entry path
        IPath classpathEntryPath = classpathEntry.getPath();

        // Exclude properties file from classpath entries
        if (!classpathEntryPath.equals(propertiesPath)) {
          newClasspathEntries.add(classpathEntry);
        }
      }
      
      // Convert new classpath entries back to array
      classpathEntries = newClasspathEntries.toArray(new IClasspathEntry[newClasspathEntries.size()]);
      
      // Set project classpath using new classpath entries
      javaProject.setRawClasspath(classpathEntries, progressMonitor);
      
      // Delete folder
      folder.delete(true, progressMonitor);
    }
  }
  
  /**
   * Uninstalls all library JAR files into the applications web library directory.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void uninstallLibraries(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get library folder
    IFolder folder = project.getFolder(new Path("WebContent/WEB-INF/lib"));
    
    if (folder.exists()) {

      // Get JAR files
      IFile coreJarFile = folder.getFile("jdistil-core.jar");
      IFile codesJarFile = folder.getFile("jdistil-codes.jar");
      IFile codesAppJarFile = folder.getFile("jdistil-codes-app.jar");
      IFile securityJarFile = folder.getFile("jdistil-security.jar");
      IFile securityAppJarFile = folder.getFile("jdistil-security-app.jar");

      // Get JAR file paths
      IPath coreJarPath = coreJarFile.getFullPath();
      IPath codesJarPath = codesJarFile.getFullPath();
      IPath codesAppJarPath = codesAppJarFile.getFullPath();
      IPath securityJarPath = securityJarFile.getFullPath();
      IPath securityAppJarPath = securityAppJarFile.getFullPath();

      // Create new classpath entries
      List<IClasspathEntry> newClasspathEntries = new ArrayList<IClasspathEntry>(); 

      // Create java project
      IJavaProject javaProject = JavaCore.create(project);
      
      // Get existing classpath entries
      IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();
      
      for (IClasspathEntry classpathEntry : classpathEntries) {
      
        // Get classpath entry path
        IPath classpathEntryPath = classpathEntry.getPath();
        
        // Exclude JAR files from classpath entries
        if (!classpathEntryPath.equals(coreJarPath) &&
            !classpathEntryPath.equals(codesJarPath) &&
            !classpathEntryPath.equals(codesAppJarPath) &&
            !classpathEntryPath.equals(securityJarPath) &&
            !classpathEntryPath.equals(securityAppJarPath)) {
          newClasspathEntries.add(classpathEntry);
        }
      }
      
      // Convert new classpath entries back to array
      classpathEntries = newClasspathEntries.toArray(new IClasspathEntry[newClasspathEntries.size()]);
      
      // Set project classpath using new classpath entries
      javaProject.setRawClasspath(classpathEntries, progressMonitor);
      
      // Delete core JAR file
      if (coreJarFile.exists()) {
        coreJarFile.delete(true, progressMonitor);
      }

      // Delete codes JAR file
      if (codesJarFile.exists()) {
        codesJarFile.delete(true, progressMonitor);
      }

      // Delete codes app JAR file
      if (codesAppJarFile.exists()) {
        codesAppJarFile.delete(true, progressMonitor);
      }

      // Delete security JAR file
      if (securityJarFile.exists()) {
        securityJarFile.delete(true, progressMonitor);
      }

      // Delete security app JAR file
      if (securityAppJarFile.exists()) {
        securityAppJarFile.delete(true, progressMonitor);
      }
    }
  }
  
  /**
   * Uninstalls all SQL resources into a new SQL directory.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void uninstallSql(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get SQL folder
    IFolder folder = project.getFolder(new Path("sql"));
    
    // Delete folder
    if (folder.exists()) {
      folder.delete(true, progressMonitor);
    }
  }
  
}
