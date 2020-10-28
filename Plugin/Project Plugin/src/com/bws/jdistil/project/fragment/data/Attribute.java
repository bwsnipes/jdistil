package com.bws.jdistil.project.fragment.data;

/**
 * Data object containing attribute data.
 */
public abstract class Attribute {

  /**
   * Attribute name.
   */
  private String name;
  
  /**
   * Required indicator.
   */
  private boolean isRequired;

  /**
   * Creates a new attribute object.
   */
  public Attribute() {
    super();
  }

  /**
   * Returns the attribute name.
   * @return String Attribute name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the attribute name.
   * @param name Attribute name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the required indicator.
   * @return boolean Required indicator.
   */
  public boolean getIsRequired() {
    return isRequired;
  }

  /**
   * Sets the required indicator.
   * @param isRequired Required indicator.
   */
  public void setIsRequired(boolean isRequired) {
    this.isRequired = isRequired;
  }

  /**
   * Returns the attribute type.
   * @return String Attribute type.
   */
  public abstract String getType();

  /**
   * Returns a detailed attribute type description.
   * @return String Detailed type description.
   */
  public abstract String getTypeDescription();

  /**
   * Returns the Java type.
   * @return String Java type.
   */
  public abstract String getJavaType();

}
