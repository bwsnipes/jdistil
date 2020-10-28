package com.bws.jdistil.project.facet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.j2ee.webapplication.ServletType;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.jst.j2ee.webapplication.WelcomeFileList;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

import com.bws.jdistil.project.util.ResourceReader;

/**
 * Installs all JDistil application resources.
 * @author Bryan Snipes
 */
public class ProjectInstallAction implements IDelegate {

  /**
   * Line separator.
   */
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Resource reader.
   */
  private ResourceReader resourceReader = new ResourceReader();
  
  /**
   * Creates a new project install action.
   */
  public ProjectInstallAction() {
    super();
  }
  
  /**
   * Installs all JDistil application resources.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
	public void execute(IProject project, IProjectFacetVersion projectFacetVersion, 
	    Object configuration, IProgressMonitor progressMonitor) throws CoreException {

	  // Install all resources
	  installServlet(project, projectFacetVersion, configuration, progressMonitor);
	  installClasses(project, projectFacetVersion, configuration, progressMonitor);
    installPropertyFiles(project, projectFacetVersion, configuration, progressMonitor);
	  installLibraries(project, projectFacetVersion, configuration, progressMonitor);
    installWebContent(project, projectFacetVersion, configuration, progressMonitor);
	  installSql(project, projectFacetVersion, configuration, progressMonitor);
	}

  /**
   * Installs the controller servlet.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  @SuppressWarnings("unchecked")
  private void installServlet(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get web artifact associated with project
    final WebArtifactEdit artifact = WebArtifactEdit.getWebArtifactEditForWrite(project);
    
    try {
      // Create servlet type
      final ServletType servletType = WebapplicationFactory.eINSTANCE.createServletType();
      servletType.setClassName("com.bws.jdistil.core.servlet.http.Controller");

      // Create servlet
      final Servlet servlet = WebapplicationFactory.eINSTANCE.createServlet();
      servlet.setWebType(servletType);
      servlet.setServletName("Controller");
      
      // Create servlet mapping
      final ServletMapping mapping = WebapplicationFactory.eINSTANCE.createServletMapping();
      mapping.setServlet(servlet);
      mapping.setUrlPattern("/Controller");

      // Create welcome file list
      final WelcomeFileList welcomeFileList = WebapplicationFactory.eINSTANCE.createWelcomeFileList();
      welcomeFileList.addFileNamed("Home.jsp");
      
      // Add servlet, servlet mapping and welcome files to web app
      final WebApp webApp = artifact.getWebApp();
      webApp.getServlets().add(servlet);
      webApp.getServletMappings().add(mapping);
      webApp.setFileList(welcomeFileList);
      
      // Save changes to web app
      artifact.saveIfNecessary(progressMonitor);
    }
    finally {
      artifact.dispose();
    }
  }
  
  /**
   * Installs all class files into a new configuration package directory.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void installClasses(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get project configuration
    ProjectConfiguration projectConfig = (ProjectConfiguration)configuration;
    
    // Get base package name
    String basePackageName = projectConfig.getBasePackageName();

    // Initialize configuration package name
    String configurationPackageName = "configuration";
    
    // Build configuration package name
    if (basePackageName != null && basePackageName.trim().length() > 0) {
      configurationPackageName = basePackageName + "." + configurationPackageName;
    }
    
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
      
      // Create and open base package fragment
      IPackageFragment basePackageFragment = sourcePackageFragmentRoot.createPackageFragment(basePackageName, true, progressMonitor);
      basePackageFragment.open(progressMonitor);

      // Create and open configuration package fragment
      IPackageFragment configurationPackageFragment = sourcePackageFragmentRoot.createPackageFragment(configurationPackageName, true, progressMonitor);
      configurationPackageFragment.open(progressMonitor);
      
      try {
        // Get view home file content
        String viewHomeContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/configuration/ViewHome.txt");
        
        // Update configuration package name
        viewHomeContent = viewHomeContent.replace("BASE_PACKAGE_NAME", basePackageName);
        viewHomeContent = viewHomeContent.replace("PACKAGE_NAME", configurationPackageName);
        
        // Create new action IDs file
        basePackageFragment.createCompilationUnit("ViewHome.java", viewHomeContent, true, progressMonitor);
      	
      	// Get action IDs file content
        String actionIdsContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/configuration/ActionIds.txt");
        
        // Update configuration package name
        actionIdsContent = actionIdsContent.replace("PACKAGE_NAME", configurationPackageName);
        
        // Create new action IDs file
        configurationPackageFragment.createCompilationUnit("ActionIds.java", actionIdsContent, true, progressMonitor);

        // Get attribute names file content
        String attributeNamesContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/configuration/AttributeNames.txt");
        
        // Update configuration package name
        attributeNamesContent = attributeNamesContent.replace("PACKAGE_NAME", configurationPackageName);
        
        // Create new attribute names file
        configurationPackageFragment.createCompilationUnit("AttributeNames.java", attributeNamesContent, true, progressMonitor);


        // Get configuration file content
        String configurationContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/configuration/Configuration.txt");
        
        // Update configuration package name
        configurationContent = configurationContent.replace("BASE_PACKAGE_NAME", basePackageName);
        configurationContent = configurationContent.replace("PACKAGE_NAME", configurationPackageName);
        
        // Create new configuration file
        configurationPackageFragment.createCompilationUnit("Configuration.java", configurationContent, true, progressMonitor);
        
        
        // Get constants file content
        String constantsContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/configuration/Constants.txt");
        
        // Update configuration package name
        constantsContent = constantsContent.replace("PACKAGE_NAME", configurationPackageName);
        
        // Create new configuration file
        configurationPackageFragment.createCompilationUnit("Constants.java", constantsContent, true, progressMonitor);

        
        // Get field IDs file content
        String fieldIdsContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/configuration/FieldIds.txt");
        
        // Update configuration package name
        fieldIdsContent = fieldIdsContent.replace("PACKAGE_NAME", configurationPackageName);
        
        // Create new field IDs file
        configurationPackageFragment.createCompilationUnit("FieldIds.java", fieldIdsContent, true, progressMonitor);

      
        // Get page IDs file content
        String pageIdsContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/configuration/PageIds.txt");
        
        // Update configuration package name
        pageIdsContent = pageIdsContent.replace("PACKAGE_NAME", configurationPackageName);
        
        // Create new page IDs file
        configurationPackageFragment.createCompilationUnit("PageIds.java", pageIdsContent, true, progressMonitor);

        
        // Get category IDs file content
        String categoryIdsContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/configuration/CategoryIds.txt");
        
        // Update configuration package name
        categoryIdsContent = categoryIdsContent.replace("PACKAGE_NAME", configurationPackageName);
        
        // Create new category IDs file
        configurationPackageFragment.createCompilationUnit("CategoryIds.java", categoryIdsContent, true, progressMonitor);
      }
      catch (IOException ioException) {
        
        // Create and throw exception
        String errorMessage = "Error installing class files: " + ioException.getMessage();
        Status status = new Status(Status.ERROR, projectFacetVersion.getPluginId(), errorMessage);
        CoreException coreException = new CoreException(status);
        
        throw coreException;
      }

      // Initialize fragment configuration package name
      String fragmentConfigurationPackageName = configurationPackageName + ".fragments";

      // Create fragment configuration package fragment
      sourcePackageFragmentRoot.createPackageFragment(fragmentConfigurationPackageName, true, progressMonitor);
    }
  }
  
  /**
   * Installs all property files into a new properties directory.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void installPropertyFiles(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get project configuration
    ProjectConfiguration projectConfig = (ProjectConfiguration)configuration;
    
    // Build configuration class names 
    String configurationClassNames = "com.bws.jdistil.codes.app.configuration.Configuration," +
        "com.bws.jdistil.security.app.configuration.Configuration," +
        projectConfig.getBasePackageName() + ".configuration.Configuration";
    
    // Get properties folder
    IFolder folder = project.getFolder(new Path("properties"));
    
    // Create folder if necessary
    if (!folder.exists()) {
      folder.create(true, true, progressMonitor);
    }
    
    try {
      // Get core properties content
      String corePropertiesContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/properties/core.properties");
      
      // Update property values
      corePropertiesContent = corePropertiesContent.replace("application.configuration=", "application.configuration=" + configurationClassNames);
      corePropertiesContent = corePropertiesContent.replace("welcome.action.id=", "welcome.action.id=A1");
      corePropertiesContent = corePropertiesContent.replace("logon.action.id=", "logon.action.id=SCA1");
      corePropertiesContent = corePropertiesContent.replace("security.manager.factory=", "security.manager.factory=com.bws.jdistil.security.app.SecurityManagerFactory");
      corePropertiesContent = corePropertiesContent.replace("sort.ascending.image=", "sort.ascending.image=images/ascending.png");
      corePropertiesContent = corePropertiesContent.replace("sort.descending.image=", "sort.descending.image=images/descending.png");
      
      // Get input stream from file contents
      ByteArrayInputStream corePropertiesInputStream = new ByteArrayInputStream(corePropertiesContent.getBytes());
      
      // Create new core properties file
      IFile corePropertiesFile = folder.getFile("core.properties");
      corePropertiesFile.create(corePropertiesInputStream, true, progressMonitor);
      

      // Create source entry for properties path
      IClasspathEntry propertySourceClasspathEntry = JavaCore.newSourceEntry(folder.getFullPath());
      
      // Create java project
      IJavaProject javaProject = JavaCore.create(project);
      
      // Get existing classpath entries
      IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();

      // Create and populate new classpath entries
      List<IClasspathEntry> newClasspathEntries = new ArrayList<IClasspathEntry>(); 
      newClasspathEntries.addAll(Arrays.asList(classpathEntries));
      newClasspathEntries.add(propertySourceClasspathEntry);
      
      // Convert new classpath entries back to array
      classpathEntries = newClasspathEntries.toArray(new IClasspathEntry[newClasspathEntries.size()]);
      
      // Set project classpath using new classpath entries
      javaProject.setRawClasspath(classpathEntries, progressMonitor);
    }
    catch (IOException ioException) {
      
      // Create and throw exception
      String errorMessage = "Error installing property files: " + ioException.getMessage();
      Status status = new Status(Status.ERROR, projectFacetVersion.getPluginId(), errorMessage);
      CoreException coreException = new CoreException(status);
      
      throw coreException;
    }
  }

  private Properties loadVersionProperties(IProjectFacetVersion projectFacetVersion) throws CoreException {
  	
  	// Create properties
    Properties versionProperties = new Properties();

    // Get version input stream
    InputStream versionInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/libraries/version.properties"); 
    
    try {
    	
    	// Load version properties
      versionProperties.load(versionInputStream);
    }
    catch (IOException ioException) {
    	
      // Create and throw exception
      String errorMessage = "Error reading verion properties: " + ioException.getMessage();
      Status status = new Status(Status.ERROR, projectFacetVersion.getPluginId(), errorMessage);
      CoreException coreException = new CoreException(status);
      
      throw coreException;
    }
    
    return versionProperties;
  }
  
	/**
	 * Installs all library JAR files into the applications web library directory.
	 * @param project Target project.
	 * @param projectFacetVersion Project facet version.
	 * @param configuration Project facet configuration.
	 * @param progressMonitor Progress monitor.
	 * @throws CoreException
	 */
	private void installLibraries(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
	  
		// Load version properties
		Properties versionProperties = loadVersionProperties(projectFacetVersion);
		
		// Read library versions
		String coreVersion = versionProperties.getProperty("jdistil-core-version");
		String codesVersion = versionProperties.getProperty("jdistil-codes-version");
		String codesAppVersion = versionProperties.getProperty("jdistil-codes-app-version");
		String securityVersion = versionProperties.getProperty("jdistil-security-version");
		String securityAppVersion = versionProperties.getProperty("jdistil-security-app-version");
		
		// Build version specific JAR file names
		String coreJarFileName = "jdistil-core-" + coreVersion + ".jar";
		String codesJarFileName = "jdistil-codes-" + codesVersion + ".jar";
		String codesAppJarFileName = "jdistil-codes-app-" + codesAppVersion + ".jar";
		String securityJarFileName = "jdistil-security-" + securityVersion + ".jar";
		String securityAppJarFileName = "jdistil-security-app-" + securityAppVersion + ".jar";

    // Get library folder
    IFolder folder = project.getFolder(new Path("WebContent/WEB-INF/lib"));
    
    // Create folder if necessary
    if (!folder.exists()) {
      folder.create(true, true, progressMonitor);
    }
    
    // Get input stream for core JAR file
    InputStream coreJarInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/libraries/" + coreJarFileName); 
    
    // Create new core JAR file
    IFile coreJarFile = folder.getFile(coreJarFileName);
    coreJarFile.create(coreJarInputStream, true, progressMonitor);

    // Create library entry for core JAR file
    IClasspathEntry coreClasspathEntry = JavaCore.newLibraryEntry(coreJarFile.getFullPath(), null, null);
    
    
    // Get input stream for codes JAR file
    InputStream codesJarInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/libraries/" + codesJarFileName); 
    
    // Create new codes JAR file
    IFile codesJarFile = folder.getFile(codesJarFileName);
    codesJarFile.create(codesJarInputStream, true, progressMonitor);

    // Create library entry for codes JAR file
    IClasspathEntry codesClasspathEntry = JavaCore.newLibraryEntry(codesJarFile.getFullPath(), null, null);
    
    
    // Get input stream for codes app JAR file
    InputStream codesAppJarInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/libraries/" + codesAppJarFileName); 
    
    // Create new codes app JAR file
    IFile codesAppJarFile = folder.getFile(codesAppJarFileName);
    codesAppJarFile.create(codesAppJarInputStream, true, progressMonitor);

    // Create library entry for codes app JAR file
    IClasspathEntry codesAppClasspathEntry = JavaCore.newLibraryEntry(codesAppJarFile.getFullPath(), null, null);
    
    
    // Get input stream for security JAR file
    InputStream securityJarInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/libraries/" + securityJarFileName); 
    
    // Create new security JAR file
    IFile securityJarFile = folder.getFile(securityJarFileName);
    securityJarFile.create(securityJarInputStream, true, progressMonitor);

    // Create library entry for security JAR file
    IClasspathEntry securityClasspathEntry = JavaCore.newLibraryEntry(securityJarFile.getFullPath(), null, null);
    
    
    // Get input stream for security app JAR file
    InputStream securityAppJarInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/libraries/" + securityAppJarFileName); 
    
    // Create new security app JAR file
    IFile securityAppJarFile = folder.getFile(securityAppJarFileName);
    securityAppJarFile.create(securityAppJarInputStream, true, progressMonitor);

    // Create library entry for security app JAR file
    IClasspathEntry securityAppClasspathEntry = JavaCore.newLibraryEntry(securityAppJarFile.getFullPath(), null, null);
    
    
    // Create java project
    IJavaProject javaProject = JavaCore.create(project);
    
    // Get existing classpath entries
    IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();

    // Create and populate new classpath entries
    List<IClasspathEntry> newClasspathEntries = new ArrayList<IClasspathEntry>(); 
    newClasspathEntries.addAll(Arrays.asList(classpathEntries));
    newClasspathEntries.add(coreClasspathEntry);
    newClasspathEntries.add(codesClasspathEntry);
    newClasspathEntries.add(codesAppClasspathEntry);
    newClasspathEntries.add(securityClasspathEntry);
    newClasspathEntries.add(securityAppClasspathEntry);
    
    // Convert new classpath entries back to array
    classpathEntries = newClasspathEntries.toArray(new IClasspathEntry[newClasspathEntries.size()]);
    
    // Set project classpath using new classpath entries
    javaProject.setRawClasspath(classpathEntries, progressMonitor);
	}
	
  /**
   * Installs all web content files into the applications web content directory.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void installWebContent(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get web content folder
    IFolder folder = project.getFolder(new Path("WebContent"));
    
    // Create folder if necessary
    if (!folder.exists()) {
      folder.create(true, true, progressMonitor);
    }
    
    // Get input stream for core css file
    InputStream coreCssInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/web/core.css"); 
    
    // Create new core css file
    IFile coreCssFile = folder.getFile("core.css");
    coreCssFile.create(coreCssInputStream, true, progressMonitor);
    
    
    // Get input stream for core js file
    InputStream coreJsInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/web/core.js"); 
    
    // Create new core js file
    IFile coreJsFile = folder.getFile("core.js");
    coreJsFile.create(coreJsInputStream, true, progressMonitor);


    // Get project configuration
    ProjectConfiguration projectConfig = (ProjectConfiguration)configuration;
    
    // Get base package name
    String basePackageName = projectConfig.getBasePackageName();
    
    try {
	  	// Read header JSP content
	    String headerJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Header.jsp");
	    
	    // Update with base package name
	    headerJspContent = headerJspContent.replaceAll("BASE-PACKAGE-NAME", basePackageName);
	    
	    // Convert content to input stream
	    byte[] headerJspBytes = headerJspContent.getBytes();
	    ByteArrayInputStream headerJspInputStream = new ByteArrayInputStream(headerJspBytes);

	    // Create new header JSP file
	    IFile headerJspFile = folder.getFile("Header.jsp");
	    headerJspFile.create(headerJspInputStream, true, progressMonitor);

	    
	  	// Read home JSP content
	    String homeJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Home.jsp");
	    
	    // Update with base package name
	    homeJspContent = homeJspContent.replaceAll("BASE-PACKAGE-NAME", basePackageName);
	    
	    // Convert content to input stream
	    byte[] homeJspBytes = homeJspContent.getBytes();
	    ByteArrayInputStream homeJspInputStream = new ByteArrayInputStream(homeJspBytes);

	    // Create new home JSP file
	    IFile homeJspFile = folder.getFile("Home.jsp");
	    homeJspFile.create(homeJspInputStream, true, progressMonitor);

	    
	  	// Get images folder
	    folder = project.getFolder(new Path("WebContent/images"));
	    
	    // Create images folder if necessary
	    if (!folder.exists()) {
	      folder.create(true, true, progressMonitor);
	    }
	    
	    // Get input stream for add image file
	    InputStream addImageInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/web/add.png"); 
	    
	    // Create new add image file
	    IFile addImageFile = folder.getFile("add.png");
	    addImageFile.create(addImageInputStream, true, progressMonitor);
	    
	    
	    // Get input stream for delete image file
	    InputStream deleteImageInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/web/delete.png"); 
	    
	    // Create new delete image file
	    IFile deleteImageFile = folder.getFile("delete.png");
	    deleteImageFile.create(deleteImageInputStream, true, progressMonitor);
	    

	    // Get input stream for ascending image file
	    InputStream ascendingImageInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/web/ascending.png"); 
	    
	    // Create new ascending image file
	    IFile ascendingImageFile = folder.getFile("ascending.png");
	    ascendingImageFile.create(ascendingImageInputStream, true, progressMonitor);
	    
	    
	    // Get input stream for descending image file
	    InputStream descendingImageInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/web/descending.png"); 
	    
	    // Create new descending image file
	    IFile descendingImageFile = folder.getFile("descending.png");
	    descendingImageFile.create(descendingImageInputStream, true, progressMonitor);
	    
	    
    	// Get codes folder
      folder = project.getFolder(new Path("WebContent/codes"));
      
      // Create folder if necessary
      if (!folder.exists()) {
        folder.create(true, true, progressMonitor);
      }
      
	  	// Read codes JSP content
	    String codesJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Codes.jsp");
	    
	    // Insert header reference
	    String modifiedCodesJspContent = insertHeader(codesJspContent);
	    modifiedCodesJspContent = insertAddImage(modifiedCodesJspContent, "CODE");
	    modifiedCodesJspContent = insertDeleteImage(modifiedCodesJspContent, "CODE");
	    
	    // Convert modified content to input stream
	    byte[] codesJspBytes = modifiedCodesJspContent.getBytes();
	    ByteArrayInputStream codesJspInputStream = new ByteArrayInputStream(codesJspBytes);
	    
	    // Create new codes JSP file
	    IFile codesJspFile = folder.getFile("Codes.jsp");
	    codesJspFile.create(codesJspInputStream, true, progressMonitor);
	    
	    // Get input stream for code JSP file
	    String codeJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Code.jsp");
	    
	    // Insert header reference
	    String modifiedCodeJspContent = insertHeader(codeJspContent);

	    // Convert modified content to input stream
	    byte[] codeJspBytes = modifiedCodeJspContent.getBytes();
	    ByteArrayInputStream codeJspInputStream = new ByteArrayInputStream(codeJspBytes);

	    // Create new code JSP file
	    IFile codeJspFile = folder.getFile("Code.jsp");
	    codeJspFile.create(codeJspInputStream, true, progressMonitor);
	    
	
	    // Get security folder
	    folder = project.getFolder(new Path("WebContent/security"));
	    
	    // Create folder if necessary
	    if (!folder.exists()) {
	      folder.create(true, true, progressMonitor);
	    }
	    
	    // Get input stream for logon JSP file
	    InputStream logonJspInputStream = getClass().getResourceAsStream("/com/bws/jdistil/project/facet/resources/web/Logon.jsp"); 
	    
	    // Create new logon JSP file
	    IFile logonJspFile = folder.getFile("Logon.jsp");
	    logonJspFile.create(logonJspInputStream, true, progressMonitor);
	    
	    
	  	// Read change password JSP content
	    String changePasswordJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/ChangePassword.jsp");
	    
	    // Insert header reference
	    String modifiedChangePasswordJspContent = insertHeader(changePasswordJspContent);
	    
	    // Convert modified content to input stream
	    byte[] changePasswordJspBytes = modifiedChangePasswordJspContent.getBytes();
	    ByteArrayInputStream changePasswordJspInputStream = new ByteArrayInputStream(changePasswordJspBytes);
	    
	    // Create new change password JSP file
	    IFile changePasswordJspFile = folder.getFile("ChangePassword.jsp");
	    changePasswordJspFile.create(changePasswordJspInputStream, true, progressMonitor);
	    
	    
	  	// Read change domain JSP content
	    String changeDomainJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/ChangeDomain.jsp");
	    
	    // Insert header reference
	    String modifiedChangeDomainJspContent = insertHeader(changeDomainJspContent);
	    
	    // Convert modified content to input stream
	    byte[] changeDomainJspBytes = modifiedChangeDomainJspContent.getBytes();
	    ByteArrayInputStream changeDomainJspInputStream = new ByteArrayInputStream(changeDomainJspBytes);
	    
	    // Create new change domain JSP file
	    IFile changeDomainJspFile = folder.getFile("ChangeDomain.jsp");
	    changeDomainJspFile.create(changeDomainJspInputStream, true, progressMonitor);
	    
	    
	    // Read domains JSP content
	    String domainsJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Domains.jsp");
	    
	    // Insert header reference
	    String modifiedDomainsJspContent = insertHeader(domainsJspContent);
	    modifiedDomainsJspContent = insertAddImage(modifiedDomainsJspContent, "DOMAIN");
	    modifiedDomainsJspContent = insertDeleteImage(modifiedDomainsJspContent, "DOMAIN");
	    
	    // Convert modified content to input stream
	    byte[] domainsJspBytes = modifiedDomainsJspContent.getBytes();
	    ByteArrayInputStream domainsJspInputStream = new ByteArrayInputStream(domainsJspBytes);
	    
	    // Create new domains JSP file
	    IFile domainsJspFile = folder.getFile("Domains.jsp");
	    domainsJspFile.create(domainsJspInputStream, true, progressMonitor);
	    

	    // Get input stream for domain JSP file
	    String domainJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Domain.jsp");
	    
	    // Insert header reference
	    String modifiedDomainJspContent = insertHeader(domainJspContent);
	    
	    // Convert modified content to input stream
	    byte[] domainJspBytes = modifiedDomainJspContent.getBytes();
	    ByteArrayInputStream domainJspInputStream = new ByteArrayInputStream(domainJspBytes);
	    
	    // Create new domain JSP file
	    IFile domainJspFile = folder.getFile("Domain.jsp");
	    domainJspFile.create(domainJspInputStream, true, progressMonitor);
	    
	    
	    // Read users JSP content
	    String usersJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Users.jsp");
	    
	    // Insert header reference
	    String modifiedUsersJspContent = insertHeader(usersJspContent);
	    modifiedUsersJspContent = insertAddImage(modifiedUsersJspContent, "USER");
	    modifiedUsersJspContent = insertDeleteImage(modifiedUsersJspContent, "USER");
	    
	    // Convert modified content to input stream
	    byte[] usersJspBytes = modifiedUsersJspContent.getBytes();
	    ByteArrayInputStream usersJspInputStream = new ByteArrayInputStream(usersJspBytes);
	    
	    // Create new users JSP file
	    IFile usersJspFile = folder.getFile("Users.jsp");
	    usersJspFile.create(usersJspInputStream, true, progressMonitor);
	    

	    // Get input stream for user JSP file
	    String userJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/User.jsp");
	    
	    // Insert header reference
	    String modifiedUserJspContent = insertHeader(userJspContent);
	    
	    // Convert modified content to input stream
	    byte[] userJspBytes = modifiedUserJspContent.getBytes();
	    ByteArrayInputStream userJspInputStream = new ByteArrayInputStream(userJspBytes);
	    
	    // Create new user JSP file
	    IFile userJspFile = folder.getFile("User.jsp");
	    userJspFile.create(userJspInputStream, true, progressMonitor);
	    

	    // Read roles JSP content
	    String rolesJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Roles.jsp");
	    
	    // Insert header reference
	    String modifiedRolesJspContent = insertHeader(rolesJspContent);
	    modifiedRolesJspContent = insertAddImage(modifiedRolesJspContent, "ROLE");
	    modifiedRolesJspContent = insertDeleteImage(modifiedRolesJspContent, "ROLE");
	    
	    // Convert modified content to input stream
	    byte[] rolesJspBytes = modifiedRolesJspContent.getBytes();
	    ByteArrayInputStream rolesJspInputStream = new ByteArrayInputStream(rolesJspBytes);
	    
	    // Create new roles JSP file
	    IFile rolesJspFile = folder.getFile("Roles.jsp");
	    rolesJspFile.create(rolesJspInputStream, true, progressMonitor);
	    
	    
	    // Get input stream for role JSP file
	    String roleJspContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/web/Role.jsp");
	    
	    // Insert header reference
	    String modifiedRoleJspContent = insertHeader(roleJspContent);
	    
	    // Convert modified content to input stream
	    byte[] roleJspBytes = modifiedRoleJspContent.getBytes();
	    ByteArrayInputStream roleJspInputStream = new ByteArrayInputStream(roleJspBytes);
	    
	    // Create new role JSP file
	    IFile roleJspFile = folder.getFile("Role.jsp");
	    roleJspFile.create(roleJspInputStream, true, progressMonitor);
    }
    catch (IOException ioException) {
      
      // Create and throw exception
      String errorMessage = "Error installing web content files: " + ioException.getMessage();
      Status status = new Status(Status.ERROR, projectFacetVersion.getPluginId(), errorMessage);
      CoreException coreException = new CoreException(status);
      
      throw coreException;
    }
  }
  
  /**
   * Inserts a reference to the JSP header file into the contents of a given JSP page.
   * The reference is inserted just before the first "core:form" tag in the given JSP page.
   * @param content JSP file content to modify.
   * @return String Modified JSP file content.
   */
  private String insertHeader(String content) {
  	
    // Initialize return value
    StringBuffer modifiedContents = new StringBuffer();
    
    // Create pattern to match dependent menu end tag
    Pattern menuPattern = Pattern.compile("<div class=\"page\">", Pattern.DOTALL);
    
    // Create menu matcher
    Matcher menuMatcher = menuPattern.matcher(content);
    
    if (!menuMatcher.find()) {
    	
    	// Return original contents
    	modifiedContents.append(content);
    }
    else {
    	
    	// Get position to insert header reference
    	int insertPosition = menuMatcher.start();
    	
      // Insert header reference action
      modifiedContents.append(content.substring(0, insertPosition));
      modifiedContents.append("<jsp:include page=\"../Header.jsp\" />");
      modifiedContents.append(LINE_SEPARATOR);
      modifiedContents.append(content.substring(insertPosition));
    }
    
    return modifiedContents.toString();
  }
  
  /**
   * Inserts an add image into the contents of a given JSP page.
   * @param content JSP file content to modify.
   * @param contextName Context name.
   * @return String Modified JSP file content.
   */
  private String insertAddImage(String content, String contextName) {
  	
    // Initialize return value
    StringBuffer modifiedContents = new StringBuffer();
    
    String contextActionName = "ADD_" + contextName.toUpperCase();
    String targetText = "<core:link.*" + contextActionName + ".*(<core:actionData).*/>\\s*</core:link>\\s*</core:td>\\s*</core:th>";
    
    // Create pattern to match context specific action name
    Pattern menuPattern = Pattern.compile(targetText, Pattern.DOTALL);
    
    // Create menu matcher
    Matcher menuMatcher = menuPattern.matcher(content);
    
    if (menuMatcher.find()) {
    	
    	String imageReference = "<img src=\"images/add.png\" class=\"actionImage\" />";
    	
    	// Get position to insert image reference
    	int insertPosition = menuMatcher.start(1);
    	
      // Insert header reference action
      modifiedContents.append(content.substring(0, insertPosition));
      modifiedContents.append(imageReference);
      modifiedContents.append(LINE_SEPARATOR);
      modifiedContents.append(content.substring(insertPosition));
    }
    
    return modifiedContents.toString();
  }
  
  /**
   * Inserts a delete image into the contents of a given JSP page.
   * @param content JSP file content to modify.
   * @param contextName Context name.
   * @return String Modified JSP file content.
   */
  private String insertDeleteImage(String content, String contextName) {
  	
    // Initialize return value
    StringBuffer modifiedContents = new StringBuffer();
    
    String contextActionName = "DELETE_" + contextName.toUpperCase();
    String targetText = "<core:link.*" + contextActionName + ".*(<core:actionData).*/>\\s*</core:link>\\s*</core:td>\\s*</core:tr>\\s*</core:table>";
    
    // Create pattern to match context specific action name
    Pattern menuPattern = Pattern.compile(targetText, Pattern.DOTALL);
    
    // Create menu matcher
    Matcher menuMatcher = menuPattern.matcher(content);
    
    if (menuMatcher.find()) {
    	
    	String imageReference = "<img src=\"images/delete.png\" class=\"actionImage\" />";
    	
    	// Get position to insert image reference
    	int insertPosition = menuMatcher.start(1);
    	
      // Insert header reference action
      modifiedContents.append(content.substring(0, insertPosition));
      modifiedContents.append(imageReference);
      modifiedContents.append(LINE_SEPARATOR);
      modifiedContents.append(content.substring(insertPosition));
    }
    
    return modifiedContents.toString();
  }
  
  /**
   * Installs all SQL resources into a new SQL directory.
   * @param project Target project.
   * @param projectFacetVersion Project facet version.
   * @param configuration Project facet configuration.
   * @param progressMonitor Progress monitor.
   * @throws CoreException
   */
  private void installSql(IProject project, IProjectFacetVersion projectFacetVersion, 
      Object configuration, IProgressMonitor progressMonitor) throws CoreException {
    
    // Get SQL folder
    IFolder folder = project.getFolder(new Path("sql"));
    
    // Create folder if necessary
    if (!folder.exists()) {
      folder.create(true, true, progressMonitor);
    }
    
    try {
      // Get core SQL content
      String coreSqlContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/sql/core.sql");
      
      // Get input stream for core SQL file
      ByteArrayInputStream coreSqlInputStream = new ByteArrayInputStream(coreSqlContent.getBytes());

      // Create new core SQL file
      IFile coreSqlFile = folder.getFile("core.sql");
      coreSqlFile.create(coreSqlInputStream, true, progressMonitor);

      
      // Get codes SQL content
      String codesSqlContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/sql/codes.sql");
      
      // Get input stream for codes SQL file
      ByteArrayInputStream codesSqlInputStream = new ByteArrayInputStream(codesSqlContent.getBytes());

      // Create new codes SQL file
      IFile codesSqlFile = folder.getFile("codes.sql");
      codesSqlFile.create(codesSqlInputStream, true, progressMonitor);
      
      
      // Get security SQL content
      String securitySqlContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/sql/security.sql");
      
      // Get input stream for security SQL file
      ByteArrayInputStream securitySqlInputStream = new ByteArrayInputStream(securitySqlContent.getBytes());

      // Create new security SQL file
      IFile securitySqlFile = folder.getFile("security.sql");
      securitySqlFile.create(securitySqlInputStream, true, progressMonitor);

      
      // Get permission SQL content
      String permissionSqlContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/sql/permission.sql");
      
      // Get input stream for permission SQL file
      ByteArrayInputStream permissionSqlInputStream = new ByteArrayInputStream(permissionSqlContent.getBytes());

      // Create new permission SQL file
      IFile permissionSqlFile = folder.getFile("permission.sql");
      permissionSqlFile.create(permissionSqlInputStream, true, progressMonitor);

      
      // Get application category SQL content
      String appCategorySqlContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/sql/app-category.sql");
      
      // Get input stream for application category SQL file
      ByteArrayInputStream appCategorySqlInputStream = new ByteArrayInputStream(appCategorySqlContent.getBytes());

      // Create new application category SQL file
      IFile appCategorySqlFile = folder.getFile("app-category.sql");
      appCategorySqlFile.create(appCategorySqlInputStream, true, progressMonitor);

    
      // Get application entity SQL content
      String appEntitySqlContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/sql/app-entity.sql");
      
      // Get input stream for application entity SQL file
      ByteArrayInputStream appEntitySqlInputStream = new ByteArrayInputStream(appEntitySqlContent.getBytes());

      // Create new application entity SQL file
      IFile appEntitySqlFile = folder.getFile("app-entity.sql");
      appEntitySqlFile.create(appEntitySqlInputStream, true, progressMonitor);

      
      // Get application security SQL content
      String appSecuritySqlContent = resourceReader.readResource("/com/bws/jdistil/project/facet/resources/sql/app-security.sql");
      
      // Get input stream for application security SQL file
      ByteArrayInputStream appSecuritySqlInputStream = new ByteArrayInputStream(appSecuritySqlContent.getBytes());

      // Create new application security SQL file
      IFile appSecuritySqlFile = folder.getFile("app-security.sql");
      appSecuritySqlFile.create(appSecuritySqlInputStream, true, progressMonitor);
    }
    catch (IOException ioException) {
      
      // Create and throw exception
      String errorMessage = "Error installing SQL files: " + ioException.getMessage();
      Status status = new Status(Status.ERROR, projectFacetVersion.getPluginId(), errorMessage);
      CoreException coreException = new CoreException(status);
      
      throw coreException;
    }
  }

}
