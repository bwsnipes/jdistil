package com.bws.jdistil.builder.data;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class DataManager {

	private String projectsPath = System.getProperty("user.home") + File.separator + "projects" + File.separator;

	private File projectsDirectory = null;
	
	private static DataManager dataManager = new DataManager();
	
	private DataManager() {
		super();
	}
	
	public static DataManager getInstance() {
		return dataManager;
	}
	
	private File ensureProjectsDirectory() throws IOException {
		
		// Get projects directory
		projectsDirectory = new File(projectsPath);
		
		// Create projects directory if it does not exist
		if (!projectsDirectory.exists()) {
			projectsDirectory.mkdirs();
		}
		
		return projectsDirectory;
	}
	
	private File getProjectFile(String projectName) throws IOException {
		
		// Ensure projects directory is created
		ensureProjectsDirectory();
		
		// Build project file 
		File projectFile = new File(projectsPath.concat(projectName).concat(".xml"));
		
		return projectFile;
	}
	
	public List<String> findProjectNames() {
		
		List<String> projectNames = new ArrayList<String>();
		
		try {
			// Ensure projects directory is created
			ensureProjectsDirectory();
			
			File[] projectFiles = projectsDirectory.listFiles();
			
			if (projectFiles != null) {
				
				for (File projectFile : projectFiles) {
					
					String fileName = projectFile.getName();
					
					if (fileName.toLowerCase().endsWith(".xml")) {
						
						int index = fileName.lastIndexOf(".xml");
						String projectName = fileName.substring(0, index);
						projectNames.add(projectName);
					}
				}
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		
		return projectNames;
	}
	
	public Project getProject(String projectName) {

		Project project = null;
		
		try {
			JAXBContext jc = JAXBContext.newInstance(Project.class);
			Unmarshaller m = jc.createUnmarshaller();
			project = (Project)m.unmarshal(getProjectFile(projectName));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return project;
	}
	
	public void saveProject(Project project) {
		
		try {
			JAXBContext jc = JAXBContext.newInstance(Project.class);
			Marshaller m = jc.createMarshaller();
			m.marshal(project, getProjectFile(project.getName()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		List<Attribute> attributes1 = new ArrayList<Attribute>();

		Attribute attribute1 = new Attribute();
		attribute1.setIsLookupMultipleValues(Boolean.FALSE);
		attribute1.setIsRequired(Boolean.TRUE);
		attribute1.setName("attrib1");
		attribute1.setType(AttributeType.TEXT);
		attribute1.setTextMaxLength(10);
		attributes1.add(attribute1);
		
		Attribute attribute2 = new Attribute();
		attribute2.setIsLookupMultipleValues(Boolean.FALSE);
		attribute2.setIsRequired(Boolean.TRUE);
		attribute2.setName("attrib2");
		attribute2.setType(AttributeType.EMAIL);
		attribute2.setTextMaxLength(10);
		attributes1.add(attribute2);
		
		Fragment fragment1 = new Fragment();
		fragment1.setName("frag1");
		fragment1.setIsPaginationSupported(Boolean.TRUE);
		fragment1.setPageSize(10);
		fragment1.setAttributes(attributes1);

		List<Attribute> attributes2 = new ArrayList<Attribute>();

		Attribute attribute3 = new Attribute();
		attribute3.setIsLookupMultipleValues(Boolean.FALSE);
		attribute3.setIsRequired(Boolean.TRUE);
		attribute3.setName("attrib3");
		attribute3.setType(AttributeType.TEXT);
		attribute3.setTextMaxLength(10);
		attributes2.add(attribute3);
		
		Attribute attribute4 = new Attribute();
		attribute4.setIsLookupMultipleValues(Boolean.FALSE);
		attribute4.setIsRequired(Boolean.TRUE);
		attribute4.setName("attrib4");
		attribute4.setType(AttributeType.TEXT);
		attribute4.setTextMaxLength(10);
		attributes2.add(attribute4);
		
		Fragment fragment2 = new Fragment();
		fragment2.setName("frag2");
		fragment2.setIsPaginationSupported(Boolean.TRUE);
		fragment2.setPageSize(10);
		fragment2.setAttributes(attributes2);

		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(fragment1);
		fragments.add(fragment2);
		
		Project project = new Project();
		project.setBasePackageName("base package");
		project.setName("name");
		project.setCreated(LocalDateTime.now());
		project.setUpdated(LocalDateTime.now().minusDays(1));
		project.setLastGenerated(LocalDateTime.now().minusDays(2));
		project.setFragments(fragments);
		
		DataManager dataManager = new DataManager();
		dataManager.saveProject(project);
		Project project2 = dataManager.getProject(project.getName());
		System.out.println(project2);
		List<String> projectNames = dataManager.findProjectNames();
		System.out.println(projectNames);
		
	}

}
