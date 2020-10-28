package com.bws.jdistil.project.fragment;

import org.eclipse.core.resources.IProject;

/**
 * Relationship wizard data. 
 */
public class RelationshipWizardData {

	/**
	 * Many to one association type.
	 */
	public static final String MANY_TO_ONE = "Many to One";
	
	/**
	 * Many to many association type.
	 */
	public static final String MANY_TO_MANY = "Many to Many";

	/**
   * Project selected in workbench.
   */
  private IProject project = null;

  /**
   * Data object 1 name.
   */
  private String dataObject1Name = null;
  
  /**
   * Attribute 1 name.
   */
  private String attribute1Name = null;
  
  /**
   * Attribute 1 include in view indicator.
   */
  private boolean attribute1IncludeInView = false;
  
  /**
   * Association type.
   */
  private String associationType = MANY_TO_ONE;
  
  /**
   * Bidirectional indicator.
   */
  private boolean bidirectional = false;
  
  /**
   * Data object 2 name.
   */
  private String dataObject2Name = null;
  
  /**
   * Attribute 2 name.
   */
  private String attribute2Name = null;
  
  /**
   * Attribute 2 required indicator.
   */
  private boolean attribute2Required = false;
  
  /**
   * Attribute 2 include in view indicator.
   */
  private boolean attribute2IncludeInView = false;
  
  /**
	 * Creates a new relationship wizard data.
	 */
	public RelationshipWizardData() {
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
   * Returns the data object 1 name.
   * @return String Data object 1 name.
   */
  public String getDataObject1Name() {
    return dataObject1Name;
  }

  /**
   * Sets the data object 1 name.
   * @param dataObject1Name Data object 1 name.
   */
  public void setDataObject1Name(String dataObject1Name) {
    this.dataObject1Name = dataObject1Name;
  }

  /**
   * Returns the attribute 1 name.
   * @return String Attribute 1 name.
   */
  public String getAttribute1Name() {
    return attribute1Name;
  }

  /**
   * Sets the attribute 1 name.
   * @param attribute1Name Attribute 1 name.
   */
  public void setAttribute1Name(String attribute1Name) {
    this.attribute1Name = attribute1Name;
  }

  /**
   * Returns the attribute 1 include in view indicator.
   * @return boolean Attribute 1 include in view indicator.
   */
  public boolean isAttribute1IncludeInView() {
		return attribute1IncludeInView;
	}

  /**
   * Sets the attribute 1 include in view indicator.
   * @param attribute1IncludeInView Attribute 1 include in view indicator.
   */
	public void setAttribute1IncludeInView(boolean attribute1IncludeInView) {
		this.attribute1IncludeInView = attribute1IncludeInView;
	}

	/**
   * Returns the association type.
   * @return String Association type.
   */
  public String getAssociationType() {
		return associationType;
	}

  /**
   * Set the association type.
   * @param associationType Association type.
   */
	public void setAssociationType(String associationType) {
		this.associationType = associationType;
	}

	/**
	 * Returns the bidirectional indicator.
	 * @return boolean Bidirectional indicator.
	 */
	public boolean isBidirectional() {
		return bidirectional;
	}

	/**
	 * Sets the bidirectional indicator.
	 * @param bidirectional Bidirectional indicator.
	 */
	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}

	/**
   * Returns the data object 2 name.
   * @return String Data object 2 name.
   */
  public String getDataObject2Name() {
    return dataObject2Name;
  }

  /**
   * Sets the data object 2 name.
   * @param dataObject2Name Data object 2 name.
   */
  public void setDataObject2Name(String dataObject2Name) {
    this.dataObject2Name = dataObject2Name;
  }

  /**
   * Returns the attribute 2 name.
   * @return String Attribute 2 name.
   */
  public String getAttribute2Name() {
    return attribute2Name;
  }

  /**
   * Sets the attribute 2 name.
   * @param attribute2Name Attribute 2 name.
   */
  public void setAttribute2Name(String attribute2Name) {
    this.attribute2Name = attribute2Name;
  }

  /**
   * Returns the attribute 2 required indicator.
   * @return boolean Attribute 2 required indicator.
   */
  public boolean isAttribute2Required() {
		return attribute2Required;
	}

  /**
   * Sets the attribute 2 required indicator.
   * @param attribute2Required Attribute 2 required indicator.
   */
	public void setAttribute2Required(boolean attribute2Required) {
		this.attribute2Required = attribute2Required;
	}

  /**
   * Returns the attribute 2 include in view indicator.
   * @return boolean Attribute 2 include in view indicator.
   */
  public boolean isAttribute2IncludeInView() {
		return attribute2IncludeInView;
	}

  /**
   * Sets the attribute 2 include in view indicator.
   * @param attribute2IncludeInView Attribute 2 include in view indicator.
   */
	public void setAttribute2IncludeInView(boolean attribute2IncludeInView) {
		this.attribute2IncludeInView = attribute2IncludeInView;
	}

}