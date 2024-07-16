package com.bws.jdistil.builder.generator;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bws.jdistil.builder.data.Project;
import com.bws.jdistil.builder.generator.util.ResourceReader;

public class AppGenerator {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private Path workingDirectoryPath = null;
	private Path outputPath = null;
	private Path projectPath = null;
	private Path javaPath = null;
	private Path resourcesPath = null;
	private Path sqlPath = null;
	private Path webAppPath = null;
	private Path basePackagePath = null;
	private Path configurationPackagePath = null;
	
	private String basePackageName = null;
	private String configurationPackageName = null;

	private ResourceReader resourceReader = new ResourceReader();

	public AppGenerator(Project project) {
		super();
	}

	public void execute(Project project) throws GeneratorException {

		// src/main/java
		// src/main/resources
		// src/main/resources/properties
		// src/main/resources/sql
		// src/main/webapp
		// src/main/webapp/codes
		// src/main/webapp/WEB-INF
		
		// Create all base application directories
		createBaseDirectories(project);
		createPackageNames(project);
		
		// Create application artifacts
		createMavenFiles(project);
		createConfigurationFiles(project);
		createPropertyFiles(project);
		createWebContentFiles(project);
		createSqlFiles(project);

		// Create constant references
		Map<String, String> fieldConstants = new HashMap<String, String>();
		Map<String, String> actionConstants = new HashMap<String, String>();
		Map<String, String> pageConstants = new HashMap<String, String>();
		Map<String, String> categoryConstants = new HashMap<String, String>();
		
		// Create fragment specific configuration files
	    ConfigurationGenerator configurationGenerator = new ConfigurationGenerator();
	    configurationGenerator.process(project, basePackageName, configurationPackageName, configurationPackagePath,
	    		fieldConstants, actionConstants, pageConstants, categoryConstants);

		// Create fragment specific data source files
	    DataSourceGenerator dataSourceGenerator = new DataSourceGenerator();
	    dataSourceGenerator.process(project, basePackageName, basePackagePath);
	    
		// Create fragment specific process files
	    ProcessGenerator processGenerator = new ProcessGenerator();
	    processGenerator.process(project, basePackageName, basePackagePath, configurationPackageName);

		// Create fragment specific JSP files
	    JspGenerator jspGenerator = new JspGenerator();
	    jspGenerator.process(project, basePackageName, basePackagePath, webAppPath, configurationPackageName);

		// Create fragment specific SQL files
	    SqlGenerator sqlGenerator = new SqlGenerator();
	    sqlGenerator.process(project, sqlPath);

		// Create lookup info SQL files
	    LookupInfoGenerator lookupInfoGenerator = new LookupInfoGenerator();
	    lookupInfoGenerator.process(sqlPath, categoryConstants);

		// Create security info SQL files
	    SecurityInfoGenerator securityInfoGenerator = new SecurityInfoGenerator();
	    securityInfoGenerator.process(project, sqlPath, fieldConstants, actionConstants);
	}
	
	public void createBaseDirectories(Project project) throws GeneratorException {

		try {
			// Get current working directory
			workingDirectoryPath = FileSystems.getDefault().getPath(".").toAbsolutePath();
			
			// Build output path
			outputPath = workingDirectoryPath.resolve("Output");
			
			// Create output path directory
			Files.createDirectories(outputPath);
			
			
			// Build project directory
			projectPath = outputPath.resolve(project.getName());
			
			// Create project path directory
			Files.createDirectories(projectPath);
			
			
			// Build maven java directory
			javaPath = projectPath.resolve("src/main/java");
			
			// Create maven java directories
			Files.createDirectories(javaPath);
			
			
			// Build maven resources directory
			resourcesPath = projectPath.resolve("src/main/resources");
			
			// Create maven resources directories
			Files.createDirectories(resourcesPath);
			
			
			// Build maven webapp directory
			webAppPath = projectPath.resolve("src/main/webapp");
			
			// Create maven webapp directories
			Files.createDirectories(webAppPath);
			
			
			// Convert base package name to folder path
			String basePackageName = project.getBasePackageName().replace(".", "/");
			
			// Build base package path
			basePackagePath = javaPath.resolve(basePackageName);
			
			// Create base package path directories
			Files.createDirectories(basePackagePath);
			

			// Build configuration package path
			configurationPackagePath = basePackagePath.resolve("configuration");
			
			// Create configuration package directories
			Files.createDirectories(configurationPackagePath);
		}
		catch (IOException ioException) {
			
			throw new GeneratorException("Error creating base directories: " + ioException.getMessage());
		}
	}

	private void createPackageNames(Project project) {
		
		// Get configuration package names
		basePackageName = project.getBasePackageName();
		configurationPackageName = basePackageName.concat(".configuration");
	}
	
	private void createMavenFiles(Project project) throws GeneratorException {

		try {
			// Get pom file content
			String pomContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/pom.txt");

			// Update pom file content
			pomContent = pomContent.replace("PROJECT_NAME", project.getName());

			// Create pom file
			Path pomPath = projectPath.resolve("pom.xml");
			Files.writeString(pomPath, pomContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating Maven configuration files: " + ioException.getMessage());
		}
	}

	private void createConfigurationFiles(Project project) throws GeneratorException {

		try {
			// Get view home file content
			String viewHomeContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/ViewHome.txt");

			// Update configuration package name
			viewHomeContent = viewHomeContent.replaceAll("BASE_PACKAGE_NAME", basePackageName);
			
			// Create view home file
			Path viewHomePath = configurationPackagePath.resolve("ViewHome.java");
			Files.writeString(viewHomePath, viewHomeContent, StandardOpenOption.CREATE_NEW);
			
			
			// Get constants file content
			String constantsContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/configuration/Constants.txt");

			// Update constants file content
			constantsContent = constantsContent.replaceAll("PACKAGE_NAME", configurationPackageName);

			// Create constants file
			Path constantsPath = configurationPackagePath.resolve("Constants.java");
			Files.writeString(constantsPath, constantsContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating configuration class files: " + ioException.getMessage());
		}
	}

	private void createPropertyFiles(Project project) throws GeneratorException {

		try {
			// Build properties path
			Path propertiesPath = resourcesPath.resolve("properties");
			
			// Create properties directory
			Files.createDirectories(propertiesPath);
			
			
			// Build configuration class names
			String configurationClassNames = new String("com.bws.jdistil.codes.app.configuration.Configuration,").
					concat("com.bws.jdistil.security.app.configuration.Configuration,").
					concat(project.getBasePackageName()).concat(".configuration.Configuration");

			// Get core properties content
			String corePropertiesContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/properties/core.properties");

			// Update property values
			corePropertiesContent = corePropertiesContent.replace("application.configuration=",	"application.configuration=" + configurationClassNames);
			corePropertiesContent = corePropertiesContent.replace("welcome.action.id=", "welcome.action.id=A1");
			corePropertiesContent = corePropertiesContent.replace("logon.action.id=", "logon.action.id=SCA1");
			corePropertiesContent = corePropertiesContent.replace("security.manager.factory=", "security.manager.factory=com.bws.jdistil.security.app.SecurityManagerFactory");
			corePropertiesContent = corePropertiesContent.replace("sort.ascending.image=", "sort.ascending.image=images/ascending.png");
			corePropertiesContent = corePropertiesContent.replace("sort.descending.image=",	"sort.descending.image=images/descending.png");

			// Create core properties file
			Path corePropertiesPath = propertiesPath.resolve("core.properties");
			Files.writeString(corePropertiesPath, corePropertiesContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating property files: " + ioException.getMessage());
		}
	}


	private void createWebContentFiles(Project project) throws GeneratorException {

		try {
			// Get core css file content
			String coreCssContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/core.css");
	
			// Create core css file
			Path coreCssPath = webAppPath.resolve("core.css");
			Files.writeString(coreCssPath, coreCssContent, StandardOpenOption.CREATE_NEW);
	
			
			// Get core js file content
			String coreJsContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/core.js");
	
			// Create core js file
			Path coreJsPath = webAppPath.resolve("core.js");
			Files.writeString(coreJsPath, coreJsContent, StandardOpenOption.CREATE_NEW);
	
			
			// Build images path
			Path imagesPath = webAppPath.resolve("images");
			
			// Create images directory
			Files.createDirectories(imagesPath);
			

			// Read add image content
			byte[] addImageInputStream = resourceReader.readByteResource("/com/bws/jdistil/builder/generator/artifact/web/add.png");
			
			// Create add image file
			Path addImagePath = imagesPath.resolve("add.png");
			Files.write(addImagePath, addImageInputStream, StandardOpenOption.CREATE_NEW);

			
			// Read delete image content
			byte[] deleteImageInputStream = resourceReader.readByteResource("/com/bws/jdistil/builder/generator/artifact/web/delete.png");
			
			// Create delete image file
			Path deleteImagePath = imagesPath.resolve("delete.png");
			Files.write(deleteImagePath, deleteImageInputStream, StandardOpenOption.CREATE_NEW);

			
			// Read ascending image content
			byte[] ascendingImageInputStream = resourceReader.readByteResource("/com/bws/jdistil/builder/generator/artifact/web/ascending.png");
			
			// Create ascending image file
			Path ascendingImagePath = imagesPath.resolve("ascending.png");
			Files.write(ascendingImagePath, ascendingImageInputStream, StandardOpenOption.CREATE_NEW);

			
			// Read descending image content
			byte[] descendingImageInputStream = resourceReader.readByteResource("/com/bws/jdistil/builder/generator/artifact/web/descending.png");
			
			// Create descending image file
			Path descendingImagePath = imagesPath.resolve("descending.png");
			Files.write(descendingImagePath, descendingImageInputStream, StandardOpenOption.CREATE_NEW);


			// Build codes path
			Path codesPath = webAppPath.resolve("codes");
			
			// Create codes directory
			Files.createDirectories(codesPath);


			// Read codes content
			String codesJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/Codes.jsp");

			// Update codes content
			codesJspContent = insertHeader(codesJspContent);
			codesJspContent = insertAddImage(codesJspContent, "CODE");
			codesJspContent = insertDeleteImage(codesJspContent, "CODE");

			// Create codes content file
			Path codesJspPath = codesPath.resolve("Codes.jsp");
			Files.writeString(codesJspPath, codesJspContent, StandardOpenOption.CREATE_NEW);
			

			// Read code content
			String codeJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/Code.jsp");

			// Update code content
			codeJspContent = insertHeader(codeJspContent);

			// Create code content file
			Path codeJspPath = codesPath.resolve("Code.jsp");
			Files.writeString(codeJspPath, codeJspContent, StandardOpenOption.CREATE_NEW);

			
			// Build security path
			Path securityPath = webAppPath.resolve("security");
			
			// Create security directory
			Files.createDirectories(securityPath);


			// Read logon content
			String logonJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/Logon.jsp");

			// Create logon content file
			Path logonJspPath = securityPath.resolve("Logon.jsp");
			Files.writeString(logonJspPath, logonJspContent, StandardOpenOption.CREATE_NEW);


			// Read change password content
			String changePasswordJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/ChangePassword.jsp");

			// Update change password content
			changePasswordJspContent = insertHeader(changePasswordJspContent);

			// Create change password content file
			Path changePasswordJspPath = securityPath.resolve("ChangePassword.jsp");
			Files.writeString(changePasswordJspPath, changePasswordJspContent, StandardOpenOption.CREATE_NEW);


			// Read change domain content
			String changeDomainJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/ChangeDomain.jsp");

			// Update change domain content
			changeDomainJspContent = insertHeader(changeDomainJspContent);

			// Create change domain content file
			Path changeDomainJspPath = securityPath.resolve("ChangeDomain.jsp");
			Files.writeString(changeDomainJspPath, changeDomainJspContent, StandardOpenOption.CREATE_NEW);


			// Read domains content
			String domainsJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/Domains.jsp");

			// Update domains content
			domainsJspContent = insertHeader(domainsJspContent);
			domainsJspContent = insertAddImage(domainsJspContent, "DOMAIN");
			domainsJspContent = insertDeleteImage(domainsJspContent, "DOMAIN");

			// Create domains content file
			Path domainsJspPath = securityPath.resolve("Domains.jsp");
			Files.writeString(domainsJspPath, domainsJspContent, StandardOpenOption.CREATE_NEW);


			// Read domain content
			String domainJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/Domain.jsp");

			// Update domain content
			domainJspContent = insertHeader(domainJspContent);

			// Create domain content file
			Path domainJspPath = securityPath.resolve("Domain.jsp");
			Files.writeString(domainJspPath, domainJspContent, StandardOpenOption.CREATE_NEW);


			// Read users content
			String usersJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/Users.jsp");

			// Update users content
			usersJspContent = insertHeader(usersJspContent);
			usersJspContent = insertAddImage(usersJspContent, "USER");
			usersJspContent = insertDeleteImage(usersJspContent, "USER");

			// Create users content file
			Path usersJspPath = securityPath.resolve("Users.jsp");
			Files.writeString(usersJspPath, usersJspContent, StandardOpenOption.CREATE_NEW);


			// Read user content
			String userJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/User.jsp");

			// Update user content
			userJspContent = insertHeader(userJspContent);

			// Create user content file
			Path userJspPath = securityPath.resolve("User.jsp");
			Files.writeString(userJspPath, userJspContent, StandardOpenOption.CREATE_NEW);
			

			// Read roles content
			String rolesJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/Roles.jsp");

			// Update roles content
			rolesJspContent = insertHeader(rolesJspContent);
			rolesJspContent = insertAddImage(rolesJspContent, "ROLE");
			rolesJspContent = insertDeleteImage(rolesJspContent, "ROLE");

			// Create roles content file
			Path rolesJspPath = securityPath.resolve("Roles.jsp");
			Files.writeString(rolesJspPath, rolesJspContent, StandardOpenOption.CREATE_NEW);


			// Read role content
			String roleJspContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/Role.jsp");

			// Update role content
			roleJspContent = insertHeader(roleJspContent);

			// Create role content file
			Path roleJspPath = securityPath.resolve("Role.jsp");
			Files.writeString(roleJspPath, roleJspContent, StandardOpenOption.CREATE_NEW);
			
			
			// Build web-inf path
			Path webInfPath = webAppPath.resolve("WEB-INF");
			
			// Create web-inf directory
			Files.createDirectories(webInfPath);


			// Read web configuration content
			String webContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/web/web.txt");

			// Update web content
			webContent = webContent.replace("PROJECT_NAME", project.getName());

			// Create web content file
			Path webXmlPath = webInfPath.resolve("web.xml");
			Files.writeString(webXmlPath, webContent, StandardOpenOption.CREATE_NEW);
		}
		catch (IOException ioException) {

			throw new GeneratorException("Error creating web content files: " + ioException.getMessage());
		}
	}

	private void createSqlFiles(Project project) throws GeneratorException {

		try {
			// Build sql path
			sqlPath = resourcesPath.resolve("sql");
			
			// Create sql directory
			Files.createDirectories(sqlPath);


			// Get core sql file content
			String coreSqlContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/sql/core.sql");
	
			// Create core sql file
			Path coreSqlPath = webAppPath.resolve("core.sql");
			Files.writeString(coreSqlPath, coreSqlContent, StandardOpenOption.CREATE_NEW);

			
			// Get codes sql file content
			String codesSqlContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/sql/codes.sql");
	
			// Create codes sql file
			Path codesSqlPath = webAppPath.resolve("codes.sql");
			Files.writeString(codesSqlPath, codesSqlContent, StandardOpenOption.CREATE_NEW);

			
			// Get security sql file content
			String securitySqlContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/sql/security.sql");
	
			// Create security sql file
			Path securitySqlPath = webAppPath.resolve("security.sql");
			Files.writeString(securitySqlPath, securitySqlContent, StandardOpenOption.CREATE_NEW);

			
			// Get permission sql file content
			String permissionSqlContent = resourceReader.readResource("/com/bws/jdistil/builder/generator/artifact/sql/permission.sql");
	
			// Create permission sql file
			Path permissionSqlPath = webAppPath.resolve("permission.sql");
			Files.writeString(permissionSqlPath, permissionSqlContent, StandardOpenOption.CREATE_NEW);
		} 
		catch (IOException ioException) {

			throw new GeneratorException("Error creating SQL files: " + ioException.getMessage());
		}
	}

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

}
