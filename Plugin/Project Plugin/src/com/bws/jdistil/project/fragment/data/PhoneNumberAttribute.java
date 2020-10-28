package com.bws.jdistil.project.fragment.data;


/**
 * Data object containing phone number attribute data.
 */
public class PhoneNumberAttribute extends Attribute {

  /**
   * Maximum length.
   */
  private static final Integer MAX_LENGTH = new Integer(12);

  /**
   * Creates a new phone number attribute.
   */
  public PhoneNumberAttribute() {
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
    return AttributeTypes.PHONE_NUMBER;
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
