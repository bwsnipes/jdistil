package com.bws.jdistil.project.fragment.data;


/**
 * Data object containing boolean attribute data.
 */
public class BooleanAttribute extends Attribute {

  /**
   * Creates a new boolean attribute.
   */
  public BooleanAttribute() {
    super();
  }

  /**
   * Returns attribute type.
   * @return String Attribute type.
   */
  public String getType() {
    return AttributeTypes.BOOLEAN;
  }

  /**
   * Returns a detailed attribute type description.
   * @return String Detailed type description.
   */
  public String getTypeDescription() {
    return getType();
  }

  /**
   * Returns the Java type.
   * @return String Java type.
   */
  public String getJavaType() {
    return "Boolean";
  }
  
}
