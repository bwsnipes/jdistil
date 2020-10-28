package com.bws.jdistil.project.fragment;

import java.util.List;

import org.eclipse.core.resources.IProject;

import com.bws.jdistil.project.fragment.data.Attribute;


/**
 * Definition wizard data. 
 */
public class DefinitionWizardData {
	
  /**
   * Project selected in workbench.
   */
  private IProject project = null;

  /**
   * Entity name.
   */
  private String entityName = null;
  
  /**
   * Package name.
   */
  private String packageName = null;
  
  /**
   * Parent entity name.
   */
  private String parentEntityName = null;
  
  /** 
   * List of attributes.
   */
  private List<Attribute> attributes = null;

  /**
	 * Creates a new definition wizard data.
	 */
	public DefinitionWizardData() {
		super();
	}

	/**
	 * Returns the project.
	 * @return IProject project.
	 */
  public IProject getProject() {
    return project;
  }

  /**
   * Sets the project.
   * @param project Project.
   */
  public void setProject(IProject project) {
    this.project = project;
  }

  /**
   * Returns the entity name.
   * @return String Entity name.
   */
  public String getEntityName() {
    return entityName;
  }

  /**
   * Sets the entity name.
   * @param entityName Entity name.
   */
  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  /**
   * Returns the package name.
   * @return String Package name.
   */
  public String getPackageName() {
    return packageName;
  }

  /**
   * Sets the package name.
   * @param packageName Package name.
   */
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  /**
   * Returns the parent entity name.
   * @return String Parent entity name.
   */
  public String getParentEntityName() {
    return parentEntityName;
  }

  /**
   * Sets the parent entity name
   * @param parentEntityName Parent entity name.
   */
  public void setParentEntityName(String parentEntityName) {
    this.parentEntityName = parentEntityName;
  }

  /**
   * Returns the list of attributes.
   * @return List List of attributes.
   */
  public List<Attribute> getAttributes() {
    return attributes;
  }

  /**
   * Sets the list of attributes.
   * @param attributes List of attributes.
   */
  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }

}