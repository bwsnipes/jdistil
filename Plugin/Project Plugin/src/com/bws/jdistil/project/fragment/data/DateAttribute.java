package com.bws.jdistil.project.fragment.data;


/**
 * Data object containing date attribute data.
 */
public class DateAttribute extends Attribute {

  /**
   * Creates a new date attribute.
   */
  public DateAttribute() {
    super();
  }

  /**
   * Returns attribute type.
   * @return String Attribute type.
   */
  public String getType() {
    return AttributeTypes.DATE;
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
