package com.bws.jdistil.project.fragment.data;


/**
 * Data object containing lookup attribute data.
 */
public class LookupAttribute extends Attribute {

  /**
   * Lookup category ID.
   */
  private Integer categoryId;

  /**
   * Lookup category name.
   */
  private String categoryName;

  /**
   * Multiple values indicator.
   */
  private boolean multipleValues;

  /**
   * Creates a new lookup attribute.
   */
  public LookupAttribute() {
    super();
  }

  /**
   * Returns the lookup category ID.
   * @return Integer Lookup category ID.
   */
  public Integer getCategoryId() {
    return categoryId;
  }

  /**
   * Sets the lookup category ID.
   * @param categoryId Lookup category ID.
   */
  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Returns the lookup category name.
   * @return String Lookup category name.
   */
  public String getCategoryName() {
    return categoryName;
  }

  /**
   * Sets the lookup category name.
   * @param categoryName Lookup category name.
   */
  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  /**
   * Returns the multiple values indicator.
   * @return boolean Multiple values indicator.
   */
  public boolean getMultipleValues() {
    return multipleValues;
  }

  /**
   * Sets the multiple values indicator.
   * @param multipleValues Multiple values indicator.
   */
  public void setMultipleValues(boolean multipleValues) {
    this.multipleValues = multipleValues;
  }

  /**
   * Returns attribute type.
   * @return String Attribute type.
   */
  public String getType() {
    return AttributeTypes.LOOKUP;
  }

  /**
   * Returns a detailed attribute type description.
   * @return String Detailed type description.
   */
  public String getTypeDescription() {
    String multipleValuesDescription = multipleValues ? ", multiple" : "";
    return getType() + "(" + categoryName + multipleValuesDescription + ")";
  }

  /**
   * Returns the Java type.
   * @return String Java type.
   */
  public String getJavaType() {
    return "Integer";
  }
  
}

