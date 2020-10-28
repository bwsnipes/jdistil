package com.bws.jdistil.project.fragment.data;


/**
 * Data object containing numeric attribute data.
 */
public class NumericAttribute extends Attribute {

  /**
   * Value type.
   */
  private String valueType;

  /**
   * Numeric percision.
   */
  private Integer precision;

  /**
   * Numeric scale.
   */
  private Integer scale;

  /**
   * Creates a new numeric attribute.
   */
  public NumericAttribute() {
    super();
  }

  /**
   * Returns the value type.
   * @return int Value type.
   */
  public String getValueType() {
    return valueType;
  }

  /**
   * Sets the value type.
   * @param valueType Value type.
   */
  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

  /**
   * Returns the numeric percision.
   * @return Integer Numeric percision.
   */
  public Integer getPrecision() {
    return precision;
  }

  /**
   * Sets the numeric percision.
   * @param precision Numeric percision.
   */
  public void setPrecision(Integer precision) {
    this.precision= precision;
  }

  /**
   * Returns the numeric scale.
   * @return Integer Numeric scale.
   */
  public Integer getScale() {
    return scale;
  }

  /**
   * Sets the numeric scale.
   * @param scale Numeric scale.
   */
  public void setScale(Integer scale) {
    this.scale= scale;
  }

  /**
   * Returns attribute type.
   * @return String Attribute type.
   */
  public String getType() {
    return AttributeTypes.NUMERIC;
  }

  /**
   * Returns a detailed attribute type description.
   * @return String Detailed type description.
   */
  public String getTypeDescription() {
    return getType() + "(" + precision + "," + scale + "," + valueType + ")";
  }

  /**
   * Returns the Java type.
   * @return String Java type.
   */
  public String getJavaType() {
    return scale > 0 ? "Double" : "Integer";
  }
  
}


