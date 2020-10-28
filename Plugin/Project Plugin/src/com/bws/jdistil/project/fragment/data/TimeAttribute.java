package com.bws.jdistil.project.fragment.data;


/**
 * Data object containing time attribute data.
 */
public class TimeAttribute extends Attribute {

  /**
   * Creates a new time attribute.
   */
  public TimeAttribute() {
    super();
  }

  /**
   * Returns attribute type.
   * @return String Attribute type.
   */
  public String getType() {
    return AttributeTypes.TIME;
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
    return "Date";
  }
  
}
