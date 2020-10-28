package com.bws.jdistil.project.fragment.data;


/**
 * Data object containing email attribute data.
 */
public class EmailAttribute extends Attribute {

  /**
   * Maximum length.
   */
  private static final Integer MAX_LENGTH = new Integer(40);

  /**
   * Creates a new email attribute.
   */
  public EmailAttribute() {
    super();
  }

  /**
   * Returns the maximum length.
   * @return Integer Maximum length.
   */
  public Integer getMaxLength() {
    return MAX_LENGTH;
  }

  /**
   * Returns attribute type.
   * @return String Attribute type.
   */
  public String getType() {
    return AttributeTypes.EMAIL;
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
    return "String";
  }
  
}
