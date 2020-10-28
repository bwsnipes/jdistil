package com.bws.jdistil.project.fragment.data;


/**
 * Data object containing memo attribute data.
 */
public class MemoAttribute extends Attribute {

  /**
   * Maximum length.
   */
  private Integer maxLength;

  /**
   * Creates a new memo attribute.
   */
  public MemoAttribute() {
    super();
  }

  /**
   * Returns the maximum length.
   * @return Integer Maximum length.
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * Sets the maximum length.
   * @param maxLength Maximum length.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * Returns attribute type.
   * @return String Attribute type.
   */
  public String getType() {
    return AttributeTypes.MEMO;
  }

  /**
   * Returns a detailed attribute type description.
   * @return String Detailed type description.
   */
  public String getTypeDescription() {
    return getType() + "(" + maxLength + ")";
  }

  /**
   * Returns the Java type.
   * @return String Java type.
   */
  public String getJavaType() {
    return "String";
  }
  
}
